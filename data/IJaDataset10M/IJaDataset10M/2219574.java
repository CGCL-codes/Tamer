package naru.aweb.http;

import java.nio.ByteBuffer;
import java.util.Map;
import org.apache.log4j.Logger;
import naru.async.pool.BuffersUtil;
import naru.async.pool.PoolBase;
import naru.async.pool.PoolManager;
import naru.aweb.auth.AuthSession;
import naru.aweb.config.Config;
import naru.aweb.core.RealHost;
import naru.aweb.util.ServerParser;

/**
 * HTTP KeeyALive�ɑ΂�������Ǘ�����
 * ���ڑ��Ɠ����ɍ쐬����邽�߁ASocket�ɑ΂�������Ǘ�����
 * @author Naru
 *
 */
public class KeepAliveContext extends PoolBase {

    private static Logger logger = Logger.getLogger(KeepAliveContext.class);

    private static Config config = Config.getConfig();

    public static void setConnectionHandler(HeaderParser responseHeader, boolean isProxy, boolean isKeepAlive) {
        String value;
        if (isKeepAlive) {
            value = HeaderParser.CONNECION_KEEP_ALIVE;
        } else {
            value = HeaderParser.CONNECION_CLOSE;
        }
        if (isProxy) {
            responseHeader.setHeader(HeaderParser.PROXY_CONNECTION_HEADER, value);
        } else {
            responseHeader.setHeader(HeaderParser.CONNECTION_HEADER, value);
        }
    }

    public static void setConnectionHandler(Map<String, String> responseHeader, boolean isProxy, boolean isKeepAlive) {
        String value;
        if (isKeepAlive) {
            value = HeaderParser.CONNECION_KEEP_ALIVE;
        } else {
            value = HeaderParser.CONNECION_CLOSE;
        }
        if (isProxy) {
            responseHeader.put(HeaderParser.PROXY_CONNECTION_HEADER, value);
        } else {
            responseHeader.put(HeaderParser.CONNECTION_HEADER, value);
        }
    }

    private RequestContext requestContext;

    private ServerParser acceptServer;

    private RealHost realHost;

    private ServerParser proxyTargetServer;

    private boolean isSelfProxy;

    private boolean isKeepAlive;

    private boolean isChunked;

    private int requestsCount;

    private int maxKeepAliveRequests;

    private long keepAliveTimeout;

    private boolean isAllowChunked;

    private boolean isProxy;

    private boolean isSslProxy;

    private boolean isSendLastChunk;

    private boolean isCloseServerHandle = false;

    private WebClientHandler webClientHandler;

    public void recycle() {
        maxKeepAliveRequests = config.getMaxKeepAliveRequests();
        keepAliveTimeout = config.getKeepAliveTimeout();
        requestsCount = 0;
        isSendLastChunk = isChunked = isProxy = isSslProxy = isKeepAlive = isAllowChunked = isSelfProxy = false;
        setupedHandler = null;
        setWebClientHandler(null);
        if (requestContext != null) {
            requestContext.unref(true);
            requestContext = null;
        }
        isCloseServerHandle = false;
        setProxyTargetServer(null);
        if (acceptServer != null) {
            acceptServer.unref();
            acceptServer = null;
        }
        realHost = null;
    }

    private synchronized void setWebClientHandler(WebClientHandler webClientHandler) {
        if (webClientHandler != null) {
            webClientHandler.ref();
        }
        if (this.webClientHandler != null) {
            if (this.webClientHandler.isConnect()) {
                this.webClientHandler.asyncClose(null);
            }
            this.webClientHandler.unref();
        }
        this.webClientHandler = webClientHandler;
    }

    /**
	 * ���N�G�X�g�w�b�_�����āAKeepAliveContext��ԋp����B
	 * 
	 * @param context�@���keepAliveContext
	 * @param requestHeader ���N�G�X�g�w�b�_
	 * @return
	 */
    public void startRequest(HeaderParser requestHeader) {
        isSendLastChunk = isChunked = isKeepAlive = isAllowChunked = false;
        if (requestsCount >= maxKeepAliveRequests) {
            logger.debug("reach maxKeepAliveRequests." + maxKeepAliveRequests);
            isKeepAlive = false;
            return;
        }
        String connection = null;
        if (isProxy) {
            if (!config.isProxyKeepAlive()) {
                isKeepAlive = false;
                return;
            }
            connection = requestHeader.getHeader(HeaderParser.PROXY_CONNECTION_HEADER);
            if (connection != null) {
                connection.trim();
            }
            if (HeaderParser.CONNECION_CLOSE.equalsIgnoreCase(connection)) {
                isKeepAlive = false;
                return;
            }
        } else {
            if (!config.isWebKeepAlive()) {
                isKeepAlive = false;
                return;
            }
            connection = requestHeader.getHeader(HeaderParser.CONNECTION_HEADER);
            if (connection != null) {
                connection.trim();
            }
            if (HeaderParser.CONNECION_CLOSE.equalsIgnoreCase(connection)) {
                isKeepAlive = false;
                return;
            }
        }
        boolean isAllowChunked = config.isAllowChunked();
        String version = requestHeader.getReqHttpVersion();
        if (HeaderParser.HTTP_VESION_10.equalsIgnoreCase(version)) {
            if (!HeaderParser.CONNECION_KEEP_ALIVE.equalsIgnoreCase(connection)) {
                isKeepAlive = false;
                return;
            }
            isAllowChunked = false;
        }
        this.isKeepAlive = true;
        this.isAllowChunked = isAllowChunked;
        this.requestsCount++;
        return;
    }

    public WebClientHandler getWebClientHandler(boolean isHttps, String targetServer, int targetPort) {
        if (webClientHandler != null) {
            if (webClientHandler.isSameConnection(isHttps, targetServer, targetPort)) {
                return webClientHandler;
            }
        }
        WebClientHandler newWebClientHandler = WebClientHandler.create(isHttps, targetServer, targetPort);
        setWebClientHandler(newWebClientHandler);
        return newWebClientHandler;
    }

    public boolean isKeepAlive() {
        return isKeepAlive;
    }

    public void setKeepAlive(boolean isKeepAlive) {
        if (this.isKeepAlive) {
            this.isKeepAlive = isKeepAlive;
        }
    }

    public boolean isChunked() {
        return isChunked;
    }

    public long getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    private static final byte[] DATA_AND_LAST_CHUNK = "\r\n0\r\n\r\n".getBytes();

    private static final byte[] LAST_CHUNK = "0\r\n\r\n".getBytes();

    private static final byte[] CRLF = "\r\n".getBytes();

    /**
	 * 
	 * @param buffers�@null�̏ꍇ���X�|���X�I�[������킷
	 * @return
	 */
    public ByteBuffer[] chunkedIfNeed(boolean isLast, ByteBuffer[] buffers) {
        if (!isChunked) {
            return buffers;
        }
        ByteBuffer head = null;
        if (buffers == null) {
            if (isLast && !isSendLastChunk) {
                isSendLastChunk = true;
                return BuffersUtil.toByteBufferArray(ByteBuffer.wrap(LAST_CHUNK));
            }
            throw new IllegalArgumentException("chunkedIfNeed");
        } else {
            long length = BuffersUtil.remaining(buffers);
            String headString = Long.toHexString(length) + "\r\n";
            head = ByteBuffer.wrap(headString.getBytes());
        }
        ByteBuffer tail = null;
        if (isLast) {
            isSendLastChunk = true;
            tail = ByteBuffer.wrap(DATA_AND_LAST_CHUNK);
        } else {
            tail = ByteBuffer.wrap(CRLF);
        }
        ByteBuffer[] chunkedBuffer = BuffersUtil.concatenate(head, buffers, tail);
        return chunkedBuffer;
    }

    private WebServerHandler setupedHandler;

    public boolean prepareResponse(WebServerHandler handler, HeaderParser responseHeader, long commitContentLength) {
        logger.debug("prepareResponse handler.cid:" + handler + ":webClientHandler.cid:" + webClientHandler);
        if (setupedHandler != null) {
            throw new IllegalStateException("fail to setup." + setupedHandler);
        }
        if (responseHeader.getStatusCode() == null) {
            isKeepAlive = false;
        }
        setupedHandler = handler;
        isChunked = false;
        if (!isKeepAlive) {
            setConnectionHandler(responseHeader, isProxy, false);
            return false;
        }
        String transferEncoding = responseHeader.getHeader(HeaderParser.TRANSFER_ENCODING_HEADER);
        boolean isAreadyChunked = HeaderParser.TRANSFER_ENCODING_CHUNKED.equalsIgnoreCase(transferEncoding);
        long contentLength = responseHeader.getContentLength();
        if (isAreadyChunked) {
            isChunked = false;
        } else if (contentLength < 0 && commitContentLength < 0) {
            if (!isAllowChunked) {
                isKeepAlive = false;
                setConnectionHandler(responseHeader, isProxy, false);
                return false;
            }
            isChunked = true;
        }
        setConnectionHandler(responseHeader, isProxy, true);
        responseHeader.setHeader(HeaderParser.KEEP_ALIVE_HEADER, "timeout=" + (keepAliveTimeout / 1000) + ", max=" + (maxKeepAliveRequests - requestsCount));
        if (isChunked) {
            responseHeader.removeHeader(HeaderParser.CONTENT_LENGTH_HEADER);
            responseHeader.setHeader(HeaderParser.TRANSFER_ENCODING_HEADER, HeaderParser.TRANSFER_ENCODING_CHUNKED);
            responseHeader.setResHttpVersion(HeaderParser.HTTP_VESION_11);
        } else if (contentLength < 0 && commitContentLength >= 0) {
            responseHeader.setContentLength(commitContentLength);
        }
        return true;
    }

    private void closeServerHandleOnce(WebServerHandler handler) {
        if (isCloseServerHandle == false) {
            handler.asyncClose(null);
            isCloseServerHandle = true;
        }
    }

    /**
	 * ���X�|���X���I������ƍl������^�C�~���O�ŌĂяo�����
	 * �P���N�G�X�g�ŌĂяo�����P�񂾂��Ƃ͌���Ȃ��_�ɒ���
	 * @param handler �u���E�U�ƂȂ����Ă���handler
	 * @return
	 */
    public synchronized boolean commitResponse(WebServerHandler handler) {
        logger.debug("commitResponse handler.cid:" + handler + ":webClientHandler.cid:" + webClientHandler);
        if (setupedHandler == null) {
            setWebClientHandler(null);
            logger.debug("commitResponse done end of keepAlive not prepareResponse.handler:" + handler);
            closeServerHandleOnce(handler);
            return false;
        }
        if (handler != setupedHandler) {
            throw new IllegalStateException("fail to endOfResponse.setupedHandler:" + setupedHandler);
        }
        if (!isKeepAlive || handler.isHandlerClosed()) {
            setWebClientHandler(null);
            logger.debug("commitResponse done end of keepAlive.handler:" + handler);
            closeServerHandleOnce(handler);
            return false;
        }
        if (handler.orderCount() != 0) {
            logger.debug("commitResponse.left order wait for done write");
            return false;
        }
        setupedHandler = null;
        if (webClientHandler != null) {
            if (!webClientHandler.isKeepAlive()) {
                setWebClientHandler(null);
            }
        }
        logger.debug("commitResponse.handler.orderCount():" + handler.orderCount());
        logger.debug("commitResponse done keepAlive.handler:" + handler);
        logger.debug("commitResponse done keepAlive.webClientHandler:" + webClientHandler);
        isSendLastChunk = isChunked = isProxy = isKeepAlive = isAllowChunked = false;
        handler.setReadTimeout(keepAliveTimeout);
        handler.waitForNextRequest();
        return true;
    }

    public void finishedOfServerHandler() {
        setWebClientHandler(null);
    }

    public RequestContext getRequestContext() {
        if (requestContext == null) {
            requestContext = (RequestContext) PoolManager.getInstance(RequestContext.class);
        }
        return requestContext;
    }

    public void endOfResponse() {
        if (requestContext != null) {
            requestContext.unref(true);
            requestContext = null;
        }
    }

    public ServerParser getProxyTargetServer() {
        return proxyTargetServer;
    }

    public void setProxyTargetServer(ServerParser proxyTargetServer) {
        isSelfProxy = false;
        if (proxyTargetServer != null) {
            isSelfProxy = (config.getRealHost(proxyTargetServer) != null);
            proxyTargetServer.ref();
        }
        if (this.proxyTargetServer != null) {
            this.proxyTargetServer.unref();
        }
        this.proxyTargetServer = proxyTargetServer;
    }

    public void setAcceptServer(ServerParser acceptServer) {
        this.acceptServer = acceptServer;
        this.realHost = config.getRealHost(acceptServer);
    }

    public RealHost getRealHost() {
        return realHost;
    }

    public boolean isSelfProxy() {
        return isSelfProxy;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public void setProxy(boolean isProxy) {
        this.isProxy = isProxy;
    }

    public boolean isSslProxy() {
        return isSslProxy;
    }

    public void setSslProxy(boolean isSslProxy) {
        this.isSslProxy = isSslProxy;
    }
}
