package android.webkit;

import android.content.Context;
import android.net.WebAddress;
import android.net.ParseException;
import android.net.http.EventHandler;
import android.net.http.Headers;
import android.net.http.HttpAuthHeader;
import android.net.http.RequestHandle;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.CacheManager.CacheResult;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class LoadListener extends Handler implements EventHandler {

    private static final String LOGTAG = "webkit";

    private static final int MSG_CONTENT_HEADERS = 100;

    private static final int MSG_CONTENT_DATA = 110;

    private static final int MSG_CONTENT_FINISHED = 120;

    private static final int MSG_CONTENT_ERROR = 130;

    private static final int MSG_LOCATION_CHANGED = 140;

    private static final int MSG_LOCATION_CHANGED_REQUEST = 150;

    private static final int MSG_STATUS = 160;

    private static final int MSG_SSL_CERTIFICATE = 170;

    private static final int MSG_SSL_ERROR = 180;

    private static final int HTTP_OK = 200;

    private static final int HTTP_MOVED_PERMANENTLY = 301;

    private static final int HTTP_FOUND = 302;

    private static final int HTTP_SEE_OTHER = 303;

    private static final int HTTP_NOT_MODIFIED = 304;

    private static final int HTTP_TEMPORARY_REDIRECT = 307;

    private static final int HTTP_AUTH = 401;

    private static final int HTTP_NOT_FOUND = 404;

    private static final int HTTP_PROXY_AUTH = 407;

    private static HashMap<String, String> sCertificateTypeMap;

    static {
        sCertificateTypeMap = new HashMap<String, String>();
        sCertificateTypeMap.put("application/x-x509-ca-cert", CertTool.CERT);
        sCertificateTypeMap.put("application/x-x509-user-cert", CertTool.CERT);
        sCertificateTypeMap.put("application/x-pkcs12", CertTool.PKCS12);
    }

    private static int sNativeLoaderCount;

    private final ByteArrayBuilder mDataBuilder = new ByteArrayBuilder(8192);

    private String mUrl;

    private WebAddress mUri;

    private boolean mPermanent;

    private String mOriginalUrl;

    private Context mContext;

    private BrowserFrame mBrowserFrame;

    private int mNativeLoader;

    private String mMimeType;

    private String mEncoding;

    private String mTransferEncoding;

    private int mStatusCode;

    private String mStatusText;

    public long mContentLength;

    private boolean mCancelled;

    private boolean mAuthFailed;

    private CacheLoader mCacheLoader;

    private CacheManager.CacheResult mCacheResult;

    private boolean mFromCache = false;

    private HttpAuthHeader mAuthHeader;

    private int mErrorID = OK;

    private String mErrorDescription;

    private SslError mSslError;

    private RequestHandle mRequestHandle;

    private RequestHandle mSslErrorRequestHandle;

    private String mMethod;

    private Map<String, String> mRequestHeaders;

    private byte[] mPostData;

    private boolean mSynchronous;

    private Vector<Message> mMessageQueue;

    private boolean mIsMainPageLoader;

    private Headers mHeaders;

    public static LoadListener getLoadListener(Context context, BrowserFrame frame, String url, int nativeLoader, boolean synchronous, boolean isMainPageLoader) {
        sNativeLoaderCount += 1;
        return new LoadListener(context, frame, url, nativeLoader, synchronous, isMainPageLoader);
    }

    public static int getNativeLoaderCount() {
        return sNativeLoaderCount;
    }

    LoadListener(Context context, BrowserFrame frame, String url, int nativeLoader, boolean synchronous, boolean isMainPageLoader) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener constructor url=" + url);
        }
        mContext = context;
        mBrowserFrame = frame;
        setUrl(url);
        mNativeLoader = nativeLoader;
        mSynchronous = synchronous;
        if (synchronous) {
            mMessageQueue = new Vector<Message>();
        }
        mIsMainPageLoader = isMainPageLoader;
    }

    /**
     * We keep a count of refs to the nativeLoader so we do not create
     * so many LoadListeners that the GREFs blow up
     */
    private void clearNativeLoader() {
        sNativeLoaderCount -= 1;
        mNativeLoader = 0;
    }

    public void handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_CONTENT_HEADERS:
                handleHeaders((Headers) msg.obj);
                break;
            case MSG_CONTENT_DATA:
                if (mNativeLoader != 0 && !ignoreCallbacks()) {
                    commitLoad();
                }
                break;
            case MSG_CONTENT_FINISHED:
                handleEndData();
                break;
            case MSG_CONTENT_ERROR:
                handleError(msg.arg1, (String) msg.obj);
                break;
            case MSG_LOCATION_CHANGED:
                doRedirect();
                break;
            case MSG_LOCATION_CHANGED_REQUEST:
                Message contMsg = obtainMessage(MSG_LOCATION_CHANGED);
                Message stopMsg = obtainMessage(MSG_CONTENT_FINISHED);
                mBrowserFrame.getCallbackProxy().onFormResubmission(stopMsg, contMsg);
                break;
            case MSG_STATUS:
                HashMap status = (HashMap) msg.obj;
                handleStatus(((Integer) status.get("major")).intValue(), ((Integer) status.get("minor")).intValue(), ((Integer) status.get("code")).intValue(), (String) status.get("reason"));
                break;
            case MSG_SSL_CERTIFICATE:
                handleCertificate((SslCertificate) msg.obj);
                break;
            case MSG_SSL_ERROR:
                handleSslError((SslError) msg.obj);
                break;
        }
    }

    /**
     * @return The loader's BrowserFrame.
     */
    BrowserFrame getFrame() {
        return mBrowserFrame;
    }

    Context getContext() {
        return mContext;
    }

    boolean isSynchronous() {
        return mSynchronous;
    }

    /**
     * @return True iff the load has been cancelled
     */
    public boolean cancelled() {
        return mCancelled;
    }

    /**
     * Parse the headers sent from the server.
     * @param headers gives up the HeaderGroup
     * IMPORTANT: as this is called from network thread, can't call native
     * directly
     */
    public void headers(Headers headers) {
        if (DebugFlags.LOAD_LISTENER) Log.v(LOGTAG, "LoadListener.headers");
        sendMessageInternal(obtainMessage(MSG_CONTENT_HEADERS, headers));
    }

    private void handleHeaders(Headers headers) {
        if (mCancelled) return;
        mHeaders = headers;
        ArrayList<String> cookies = headers.getSetCookie();
        for (int i = 0; i < cookies.size(); ++i) {
            CookieManager.getInstance().setCookie(mUri, cookies.get(i));
        }
        long contentLength = headers.getContentLength();
        if (contentLength != Headers.NO_CONTENT_LENGTH) {
            mContentLength = contentLength;
        } else {
            mContentLength = 0;
        }
        String contentType = headers.getContentType();
        if (contentType != null) {
            parseContentTypeHeader(contentType);
            if (mMimeType.equals("text/plain") || mMimeType.equals("application/octet-stream")) {
                String contentDisposition = headers.getContentDisposition();
                String url = null;
                if (contentDisposition != null) {
                    url = URLUtil.parseContentDisposition(contentDisposition);
                }
                if (url == null) {
                    url = mUrl;
                }
                String newMimeType = guessMimeTypeFromExtension(url);
                if (newMimeType != null) {
                    mMimeType = newMimeType;
                }
            } else if (mMimeType.equals("text/vnd.wap.wml")) {
                mMimeType = "text/plain";
            } else {
                if (mMimeType.equals("application/vnd.wap.xhtml+xml")) {
                    mMimeType = "application/xhtml+xml";
                }
            }
        } else {
            guessMimeType();
        }
        boolean mustAuthenticate = (mStatusCode == HTTP_AUTH || mStatusCode == HTTP_PROXY_AUTH);
        boolean isProxyAuthRequest = (mStatusCode == HTTP_PROXY_AUTH);
        mAuthFailed = false;
        if (mAuthHeader != null) {
            mAuthFailed = (mustAuthenticate && isProxyAuthRequest == mAuthHeader.isProxy());
            if (!mAuthFailed && mAuthHeader.isProxy()) {
                Network network = Network.getInstance(mContext);
                if (network.isValidProxySet()) {
                    synchronized (network) {
                        network.setProxyUsername(mAuthHeader.getUsername());
                        network.setProxyPassword(mAuthHeader.getPassword());
                    }
                }
            }
        }
        mAuthHeader = null;
        if (mustAuthenticate) {
            if (mStatusCode == HTTP_AUTH) {
                mAuthHeader = parseAuthHeader(headers.getWwwAuthenticate());
            } else {
                mAuthHeader = parseAuthHeader(headers.getProxyAuthenticate());
                if (mAuthHeader != null) {
                    mAuthHeader.setProxy();
                }
            }
        }
        if ((mStatusCode == HTTP_OK || mStatusCode == HTTP_FOUND || mStatusCode == HTTP_MOVED_PERMANENTLY || mStatusCode == HTTP_TEMPORARY_REDIRECT) && mNativeLoader != 0) {
            if (!mFromCache && mRequestHandle != null) {
                mCacheResult = CacheManager.createCacheFile(mUrl, mStatusCode, headers, mMimeType, false);
            }
            if (mCacheResult != null) {
                mCacheResult.encoding = mEncoding;
            }
        }
        commitHeadersCheckRedirect();
    }

    /**
     * @return True iff this loader is in the proxy-authenticate state.
     */
    boolean proxyAuthenticate() {
        if (mAuthHeader != null) {
            return mAuthHeader.isProxy();
        }
        return false;
    }

    /**
     * Report the status of the response.
     * TODO: Comments about each parameter.
     * IMPORTANT: as this is called from network thread, can't call native
     * directly
     */
    public void status(int majorVersion, int minorVersion, int code, String reasonPhrase) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener: from: " + mUrl + " major: " + majorVersion + " minor: " + minorVersion + " code: " + code + " reason: " + reasonPhrase);
        }
        HashMap status = new HashMap();
        status.put("major", majorVersion);
        status.put("minor", minorVersion);
        status.put("code", code);
        status.put("reason", reasonPhrase);
        mDataBuilder.clear();
        mMimeType = "";
        mEncoding = "";
        mTransferEncoding = "";
        sendMessageInternal(obtainMessage(MSG_STATUS, status));
    }

    private void handleStatus(int major, int minor, int code, String reason) {
        if (mCancelled) return;
        mStatusCode = code;
        mStatusText = reason;
        mPermanent = false;
    }

    /**
     * Implementation of certificate handler for EventHandler.
     * Called every time a resource is loaded via a secure
     * connection. In this context, can be called multiple
     * times if we have redirects
     * @param certificate The SSL certifcate
     * IMPORTANT: as this is called from network thread, can't call native
     * directly
     */
    public void certificate(SslCertificate certificate) {
        sendMessageInternal(obtainMessage(MSG_SSL_CERTIFICATE, certificate));
    }

    private void handleCertificate(SslCertificate certificate) {
        if (mIsMainPageLoader) {
            mBrowserFrame.certificate(certificate);
        }
    }

    /**
     * Implementation of error handler for EventHandler.
     * Subclasses should call this method to have error fields set.
     * @param id The error id described by EventHandler.
     * @param description A string description of the error.
     * IMPORTANT: as this is called from network thread, can't call native
     * directly
     */
    public void error(int id, String description) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.error url:" + url() + " id:" + id + " description:" + description);
        }
        sendMessageInternal(obtainMessage(MSG_CONTENT_ERROR, id, 0, description));
    }

    private void handleError(int id, String description) {
        mErrorID = id;
        mErrorDescription = description;
        detachRequestHandle();
        notifyError();
        tearDown();
    }

    /**
     * Add data to the internal collection of data. This function is used by
     * the data: scheme, about: scheme and http/https schemes.
     * @param data A byte array containing the content.
     * @param length The length of data.
     * IMPORTANT: as this is called from network thread, can't call native
     * directly
     * XXX: Unlike the other network thread methods, this method can do the
     * work of decoding the data and appending it to the data builder because
     * mDataBuilder is a thread-safe structure.
     */
    public void data(byte[] data, int length) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.data(): url: " + url());
        }
        boolean sendMessage = false;
        synchronized (mDataBuilder) {
            sendMessage = mDataBuilder.isEmpty();
            mDataBuilder.append(data, 0, length);
        }
        if (sendMessage) {
            sendMessageInternal(obtainMessage(MSG_CONTENT_DATA));
        }
    }

    /**
     * Event handler's endData call. Send a message to the handler notifying
     * them that the data has finished.
     * IMPORTANT: as this is called from network thread, can't call native
     * directly
     */
    public void endData() {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.endData(): url: " + url());
        }
        sendMessageInternal(obtainMessage(MSG_CONTENT_FINISHED));
    }

    private void handleEndData() {
        if (mCancelled) return;
        switch(mStatusCode) {
            case HTTP_MOVED_PERMANENTLY:
                mPermanent = true;
            case HTTP_FOUND:
            case HTTP_SEE_OTHER:
            case HTTP_TEMPORARY_REDIRECT:
                if (mStatusCode == HTTP_TEMPORARY_REDIRECT) {
                    if (mRequestHandle != null && mRequestHandle.getMethod().equals("POST")) {
                        sendMessageInternal(obtainMessage(MSG_LOCATION_CHANGED_REQUEST));
                    } else if (mMethod != null && mMethod.equals("POST")) {
                        sendMessageInternal(obtainMessage(MSG_LOCATION_CHANGED_REQUEST));
                    } else {
                        sendMessageInternal(obtainMessage(MSG_LOCATION_CHANGED));
                    }
                } else {
                    sendMessageInternal(obtainMessage(MSG_LOCATION_CHANGED));
                }
                return;
            case HTTP_AUTH:
            case HTTP_PROXY_AUTH:
                if (mAuthHeader != null && (Network.getInstance(mContext).isValidProxySet() || !mAuthHeader.isProxy())) {
                    Network.getInstance(mContext).handleAuthRequest(this);
                    return;
                }
                break;
            case HTTP_NOT_MODIFIED:
                if (mCacheLoader != null) {
                    mCacheLoader.load();
                    mFromCache = true;
                    if (DebugFlags.LOAD_LISTENER) {
                        Log.v(LOGTAG, "LoadListener cache load url=" + url());
                    }
                    return;
                }
                break;
            case HTTP_NOT_FOUND:
            default:
                break;
        }
        detachRequestHandle();
        tearDown();
    }

    void setCacheLoader(CacheLoader c) {
        mCacheLoader = c;
        mFromCache = true;
    }

    /**
     * Check the cache for the current URL, and load it if it is valid.
     *
     * @param headers for the request
     * @return true if cached response is used.
     */
    boolean checkCache(Map<String, String> headers) {
        CacheResult result = CacheManager.getCacheFile(url(), headers);
        mCacheLoader = null;
        mFromCache = false;
        if (result != null) {
            mCacheLoader = new CacheLoader(this, result);
            if (!headers.containsKey(CacheManager.HEADER_KEY_IFNONEMATCH) && !headers.containsKey(CacheManager.HEADER_KEY_IFMODIFIEDSINCE)) {
                if (DebugFlags.LOAD_LISTENER) {
                    Log.v(LOGTAG, "FrameLoader: HTTP URL in cache " + "and usable: " + url());
                }
                mCacheLoader.load();
                mFromCache = true;
                return true;
            }
        }
        return false;
    }

    /**
     * SSL certificate error callback. Handles SSL error(s) on the way up
     * to the user.
     * IMPORTANT: as this is called from network thread, can't call native
     * directly
     */
    public boolean handleSslErrorRequest(SslError error) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.handleSslErrorRequest(): url:" + url() + " primary error: " + error.getPrimaryError() + " certificate: " + error.getCertificate());
        }
        if (Network.getInstance(mContext).checkSslPrefTable(this, error)) {
            return true;
        }
        if (isSynchronous()) {
            mRequestHandle.handleSslErrorResponse(false);
            return true;
        }
        sendMessageInternal(obtainMessage(MSG_SSL_ERROR, error));
        if (!mCancelled) {
            mSslErrorRequestHandle = mRequestHandle;
        }
        return !mCancelled;
    }

    private void handleSslError(SslError error) {
        if (!mCancelled) {
            mSslError = error;
            Network.getInstance(mContext).handleSslErrorRequest(this);
        } else if (mSslErrorRequestHandle != null) {
            mSslErrorRequestHandle.handleSslErrorResponse(true);
        }
        mSslErrorRequestHandle = null;
    }

    /**
     * @return HTTP authentication realm or null if none.
     */
    String realm() {
        if (mAuthHeader == null) {
            return null;
        } else {
            return mAuthHeader.getRealm();
        }
    }

    /**
     * Returns true iff an HTTP authentication problem has
     * occured (credentials invalid).
     */
    boolean authCredentialsInvalid() {
        return (mAuthFailed && !(mAuthHeader.isDigest() && mAuthHeader.getStale()));
    }

    /**
     * @return The last SSL error or null if there is none
     */
    SslError sslError() {
        return mSslError;
    }

    /**
     * Handles SSL error(s) on the way down from the user
     * (the user has already provided their feedback).
     */
    void handleSslErrorResponse(boolean proceed) {
        if (mRequestHandle != null) {
            mRequestHandle.handleSslErrorResponse(proceed);
        }
        if (!proceed) {
            commitLoad();
            tearDown();
        }
    }

    /**
     * Uses user-supplied credentials to restart a request. If the credentials
     * are null, cancel the request.
     */
    void handleAuthResponse(String username, String password) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.handleAuthResponse: url: " + mUrl + " username: " + username + " password: " + password);
        }
        if (username != null && password != null) {
            if (mAuthHeader != null && mRequestHandle != null) {
                mAuthHeader.setUsername(username);
                mAuthHeader.setPassword(password);
                int scheme = mAuthHeader.getScheme();
                if (scheme == HttpAuthHeader.BASIC) {
                    boolean isProxy = mAuthHeader.isProxy();
                    mRequestHandle.setupBasicAuthResponse(isProxy, username, password);
                } else {
                    if (scheme == HttpAuthHeader.DIGEST) {
                        boolean isProxy = mAuthHeader.isProxy();
                        String realm = mAuthHeader.getRealm();
                        String nonce = mAuthHeader.getNonce();
                        String qop = mAuthHeader.getQop();
                        String algorithm = mAuthHeader.getAlgorithm();
                        String opaque = mAuthHeader.getOpaque();
                        mRequestHandle.setupDigestAuthResponse(isProxy, username, password, realm, nonce, qop, algorithm, opaque);
                    }
                }
            }
        } else {
            commitLoad();
            tearDown();
        }
    }

    /**
     * This is called when a request can be satisfied by the cache, however,
     * the cache result could be a redirect. In this case we need to issue
     * the network request.
     * @param method
     * @param headers
     * @param postData
     */
    void setRequestData(String method, Map<String, String> headers, byte[] postData) {
        mMethod = method;
        mRequestHeaders = headers;
        mPostData = postData;
    }

    /**
     * @return The current URL associated with this load.
     */
    String url() {
        return mUrl;
    }

    /**
     * @return The current WebAddress associated with this load.
     */
    WebAddress getWebAddress() {
        return mUri;
    }

    /**
     * @return URL hostname (current URL).
     */
    String host() {
        if (mUri != null) {
            return mUri.mHost;
        }
        return null;
    }

    /**
     * @return The original URL associated with this load.
     */
    String originalUrl() {
        if (mOriginalUrl != null) {
            return mOriginalUrl;
        } else {
            return mUrl;
        }
    }

    void attachRequestHandle(RequestHandle requestHandle) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.attachRequestHandle(): " + "requestHandle: " + requestHandle);
        }
        mRequestHandle = requestHandle;
    }

    void detachRequestHandle() {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.detachRequestHandle(): " + "requestHandle: " + mRequestHandle);
        }
        mRequestHandle = null;
    }

    void downloadFile() {
        mCacheResult = null;
        mBrowserFrame.getCallbackProxy().onDownloadStart(url(), mBrowserFrame.getUserAgentString(), mHeaders.getContentDisposition(), mMimeType, mContentLength);
        cancel();
    }

    static boolean willLoadFromCache(String url) {
        boolean inCache = CacheManager.getCacheFile(url, null) != null;
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "willLoadFromCache: " + url + " in cache: " + inCache);
        }
        return inCache;
    }

    void resetCancel() {
        mCancelled = false;
    }

    String mimeType() {
        return mMimeType;
    }

    String transferEncoding() {
        return mTransferEncoding;
    }

    long contentLength() {
        return mContentLength;
    }

    private void commitHeadersCheckRedirect() {
        if (mCancelled) return;
        if ((mStatusCode >= 301 && mStatusCode <= 303) || mStatusCode == 307) {
            return;
        }
        commitHeaders();
    }

    private void commitHeaders() {
        if (mIsMainPageLoader && sCertificateTypeMap.containsKey(mMimeType)) {
            return;
        }
        int nativeResponse = createNativeResponse();
        nativeReceivedResponse(nativeResponse);
    }

    /**
     * Create a WebCore response object so that it can be used by
     * nativeReceivedResponse or nativeRedirectedToUrl
     * @return native response pointer
     */
    private int createNativeResponse() {
        int statusCode = (mStatusCode == HTTP_NOT_MODIFIED && mCacheLoader != null) ? HTTP_OK : mStatusCode;
        final int nativeResponse = nativeCreateResponse(mUrl, statusCode, mStatusText, mMimeType, mContentLength, mEncoding);
        if (mHeaders != null) {
            mHeaders.getHeaders(new Headers.HeaderCallback() {

                public void header(String name, String value) {
                    nativeSetResponseHeader(nativeResponse, name, value);
                }
            });
        }
        return nativeResponse;
    }

    /**
     * Commit the load.  It should be ok to call repeatedly but only before
     * tearDown is called.
     */
    private void commitLoad() {
        if (mCancelled) return;
        if (mIsMainPageLoader) {
            String type = sCertificateTypeMap.get(mMimeType);
            if (type != null) {
                byte[] cert = new byte[mDataBuilder.getByteSize()];
                int offset = 0;
                while (true) {
                    ByteArrayBuilder.Chunk c = mDataBuilder.getFirstChunk();
                    if (c == null) break;
                    if (c.mLength != 0) {
                        System.arraycopy(c.mArray, 0, cert, offset, c.mLength);
                        offset += c.mLength;
                    }
                    mDataBuilder.releaseChunk(c);
                }
                CertTool.addCertificate(mContext, type, cert);
                mBrowserFrame.stopLoading();
                return;
            }
        }
        PerfChecker checker = new PerfChecker();
        ByteArrayBuilder.Chunk c;
        while (true) {
            c = mDataBuilder.getFirstChunk();
            if (c == null) break;
            if (c.mLength != 0) {
                if (mCacheResult != null) {
                    try {
                        mCacheResult.outStream.write(c.mArray, 0, c.mLength);
                    } catch (IOException e) {
                        mCacheResult = null;
                    }
                }
                nativeAddData(c.mArray, c.mLength);
            }
            mDataBuilder.releaseChunk(c);
            checker.responseAlert("res nativeAddData");
        }
    }

    /**
     * Tear down the load. Subclasses should clean up any mess because of
     * cancellation or errors during the load.
     */
    void tearDown() {
        if (mCacheResult != null) {
            if (getErrorID() == OK) {
                CacheManager.saveCacheFile(mUrl, mCacheResult);
            }
            mCacheResult = null;
        }
        if (mNativeLoader != 0) {
            PerfChecker checker = new PerfChecker();
            nativeFinished();
            checker.responseAlert("res nativeFinished");
            clearNativeLoader();
        }
    }

    /**
     * Helper for getting the error ID.
     * @return errorID.
     */
    private int getErrorID() {
        return mErrorID;
    }

    /**
     * Return the error description.
     * @return errorDescription.
     */
    private String getErrorDescription() {
        return mErrorDescription;
    }

    /**
     * Notify the loader we encountered an error.
     */
    void notifyError() {
        if (mNativeLoader != 0) {
            String description = getErrorDescription();
            if (description == null) description = "";
            nativeError(getErrorID(), description, url());
            clearNativeLoader();
        }
    }

    /**
     * Cancel a request.
     * FIXME: This will only work if the request has yet to be handled. This
     * is in no way guarenteed if requests are served in a separate thread.
     * It also causes major problems if cancel is called during an
     * EventHandler's method call.
     */
    public void cancel() {
        if (DebugFlags.LOAD_LISTENER) {
            if (mRequestHandle == null) {
                Log.v(LOGTAG, "LoadListener.cancel(): no requestHandle");
            } else {
                Log.v(LOGTAG, "LoadListener.cancel()");
            }
        }
        if (mRequestHandle != null) {
            mRequestHandle.cancel();
            mRequestHandle = null;
        }
        mCacheResult = null;
        mCancelled = true;
        clearNativeLoader();
    }

    private int mCacheRedirectCount;

    private void doRedirect() {
        if (mCancelled) {
            return;
        }
        if (mCacheRedirectCount >= RequestHandle.MAX_REDIRECT_COUNT) {
            handleError(EventHandler.ERROR_REDIRECT_LOOP, mContext.getString(R.string.httpErrorRedirectLoop));
            return;
        }
        String redirectTo = mHeaders.getLocation();
        if (redirectTo != null) {
            int nativeResponse = createNativeResponse();
            redirectTo = nativeRedirectedToUrl(mUrl, redirectTo, nativeResponse);
            if (mCancelled) {
                return;
            }
            if (redirectTo == null) {
                Log.d(LOGTAG, "Redirection failed for " + mHeaders.getLocation());
                cancel();
                return;
            } else if (!URLUtil.isNetworkUrl(redirectTo)) {
                final String text = mContext.getString(R.string.open_permission_deny) + "\n" + redirectTo;
                nativeAddData(text.getBytes(), text.length());
                nativeFinished();
                clearNativeLoader();
                return;
            }
            if (mOriginalUrl == null) {
                mOriginalUrl = mUrl;
            }
            if (mCacheResult != null) {
                if (getErrorID() == OK) {
                    CacheManager.saveCacheFile(mUrl, mCacheResult);
                }
                mCacheResult = null;
            }
            setUrl(redirectTo);
            if (mRequestHeaders == null) {
                mRequestHeaders = new HashMap<String, String>();
            }
            boolean fromCache = false;
            if (mCacheLoader != null) {
                mCacheRedirectCount++;
                fromCache = true;
            }
            if (!checkCache(mRequestHeaders)) {
                if (mRequestHandle != null) {
                    mRequestHandle.setupRedirect(mUrl, mStatusCode, mRequestHeaders);
                } else {
                    Network network = Network.getInstance(getContext());
                    if (!network.requestURL(mMethod, mRequestHeaders, mPostData, this)) {
                        handleError(EventHandler.ERROR_BAD_URL, mContext.getString(R.string.httpErrorBadUrl));
                        return;
                    }
                }
                if (fromCache) {
                    mRequestHandle.setRedirectCount(mCacheRedirectCount);
                }
            } else if (!fromCache) {
                mCacheRedirectCount = mRequestHandle.getRedirectCount() + 1;
            }
        } else {
            commitHeaders();
            commitLoad();
            tearDown();
        }
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.onRedirect(): redirect to: " + redirectTo);
        }
    }

    /**
     * Parses the content-type header.
     * The first part only allows '-' if it follows x or X.
     */
    private static final Pattern CONTENT_TYPE_PATTERN = Pattern.compile("^((?:[xX]-)?[a-zA-Z\\*]+/[\\w\\+\\*-]+[\\.[\\w\\+-]+]*)$");

    void parseContentTypeHeader(String contentType) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "LoadListener.parseContentTypeHeader: " + "contentType: " + contentType);
        }
        if (contentType != null) {
            int i = contentType.indexOf(';');
            if (i >= 0) {
                mMimeType = contentType.substring(0, i);
                int j = contentType.indexOf('=', i);
                if (j > 0) {
                    i = contentType.indexOf(';', j);
                    if (i < j) {
                        i = contentType.length();
                    }
                    mEncoding = contentType.substring(j + 1, i);
                } else {
                    mEncoding = contentType.substring(i + 1);
                }
                mEncoding = mEncoding.trim().toLowerCase();
                if (i < contentType.length() - 1) {
                    mTransferEncoding = contentType.substring(i + 1).trim().toLowerCase();
                }
            } else {
                mMimeType = contentType;
            }
            mMimeType = mMimeType.trim();
            try {
                Matcher m = CONTENT_TYPE_PATTERN.matcher(mMimeType);
                if (m.find()) {
                    mMimeType = m.group(1);
                } else {
                    guessMimeType();
                }
            } catch (IllegalStateException ex) {
                guessMimeType();
            }
        }
        mMimeType = mMimeType.toLowerCase();
    }

    /**
     * @return The HTTP-authentication object or null if there
     * is no supported scheme in the header.
     * If there are several valid schemes present, we pick the
     * strongest one. If there are several schemes of the same
     * strength, we pick the one that comes first.
     */
    private HttpAuthHeader parseAuthHeader(String header) {
        if (header != null) {
            int posMax = 256;
            int posLen = 0;
            int[] pos = new int[posMax];
            int headerLen = header.length();
            if (headerLen > 0) {
                boolean quoted = false;
                for (int i = 0; i < headerLen && posLen < posMax; ++i) {
                    if (header.charAt(i) == '\"') {
                        quoted = !quoted;
                    } else {
                        if (!quoted) {
                            if (header.regionMatches(true, i, HttpAuthHeader.BASIC_TOKEN, 0, HttpAuthHeader.BASIC_TOKEN.length())) {
                                pos[posLen++] = i;
                                continue;
                            }
                            if (header.regionMatches(true, i, HttpAuthHeader.DIGEST_TOKEN, 0, HttpAuthHeader.DIGEST_TOKEN.length())) {
                                pos[posLen++] = i;
                                continue;
                            }
                        }
                    }
                }
            }
            if (posLen > 0) {
                for (int i = 0; i < posLen; i++) {
                    if (header.regionMatches(true, pos[i], HttpAuthHeader.DIGEST_TOKEN, 0, HttpAuthHeader.DIGEST_TOKEN.length())) {
                        String sub = header.substring(pos[i], (i + 1 < posLen ? pos[i + 1] : headerLen));
                        HttpAuthHeader rval = new HttpAuthHeader(sub);
                        if (rval.isSupportedScheme()) {
                            return rval;
                        }
                    }
                }
                for (int i = 0; i < posLen; i++) {
                    if (header.regionMatches(true, pos[i], HttpAuthHeader.BASIC_TOKEN, 0, HttpAuthHeader.BASIC_TOKEN.length())) {
                        String sub = header.substring(pos[i], (i + 1 < posLen ? pos[i + 1] : headerLen));
                        HttpAuthHeader rval = new HttpAuthHeader(sub);
                        if (rval.isSupportedScheme()) {
                            return rval;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * If the content is a redirect or not modified we should not send
     * any data into WebCore as that will cause it create a document with
     * the data, then when we try to provide the real content, it will assert.
     *
     * @return True iff the callback should be ignored.
     */
    private boolean ignoreCallbacks() {
        return (mCancelled || mAuthHeader != null || (mStatusCode > 300 && mStatusCode < 400 && mStatusCode != 305));
    }

    /**
     * Sets the current URL associated with this load.
     */
    void setUrl(String url) {
        if (url != null) {
            mUri = null;
            if (URLUtil.isNetworkUrl(url)) {
                mUrl = URLUtil.stripAnchor(url);
                try {
                    mUri = new WebAddress(mUrl);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                mUrl = url;
            }
        }
    }

    /**
     * Guesses MIME type if one was not specified. Defaults to 'text/html'. In
     * addition, tries to guess the MIME type based on the extension.
     *
     */
    private void guessMimeType() {
        if (URLUtil.isDataUrl(mUrl) && mMimeType.length() != 0) {
            cancel();
            final String text = mContext.getString(R.string.httpErrorBadUrl);
            handleError(EventHandler.ERROR_BAD_URL, text);
        } else {
            mMimeType = "text/html";
            String newMimeType = guessMimeTypeFromExtension(mUrl);
            if (newMimeType != null) {
                mMimeType = newMimeType;
            }
        }
    }

    /**
     * guess MIME type based on the file extension.
     */
    private String guessMimeTypeFromExtension(String url) {
        if (DebugFlags.LOAD_LISTENER) {
            Log.v(LOGTAG, "guessMimeTypeFromExtension: url = " + url);
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
    }

    /**
     * Either send a message to ourselves or queue the message if this is a
     * synchronous load.
     */
    private void sendMessageInternal(Message msg) {
        if (mSynchronous) {
            mMessageQueue.add(msg);
        } else {
            sendMessage(msg);
        }
    }

    void loadSynchronousMessages() {
        if (DebugFlags.LOAD_LISTENER && !mSynchronous) {
            throw new AssertionError();
        }
        for (int size = mMessageQueue.size(); size > 0; size--) {
            handleMessage(mMessageQueue.remove(0));
        }
    }

    /**
     * Create a new native response object.
     * @param url The url of the resource.
     * @param statusCode The HTTP status code.
     * @param statusText The HTTP status text.
     * @param mimeType HTTP content-type.
     * @param expectedLength An estimate of the content length or the length
     *                       given by the server.
     * @param encoding HTTP encoding.
     * @return The native response pointer.
     */
    private native int nativeCreateResponse(String url, int statusCode, String statusText, String mimeType, long expectedLength, String encoding);

    /**
     * Add a response header to the native object.
     * @param nativeResponse The native pointer.
     * @param key String key.
     * @param val String value.
     */
    private native void nativeSetResponseHeader(int nativeResponse, String key, String val);

    /**
     * Dispatch the response.
     * @param nativeResponse The native pointer.
     */
    private native void nativeReceivedResponse(int nativeResponse);

    /**
     * Add data to the loader.
     * @param data Byte array of data.
     * @param length Number of objects in data.
     */
    private native void nativeAddData(byte[] data, int length);

    /**
     * Tell the loader it has finished.
     */
    private native void nativeFinished();

    /**
     * tell the loader to redirect
     * @param baseUrl The base url.
     * @param redirectTo The url to redirect to.
     * @param nativeResponse The native pointer.
     * @return The new url that the resource redirected to.
     */
    private native String nativeRedirectedToUrl(String baseUrl, String redirectTo, int nativeResponse);

    /**
     * Tell the loader there is error
     * @param id
     * @param desc
     * @param failingUrl The url that failed.
     */
    private native void nativeError(int id, String desc, String failingUrl);
}
