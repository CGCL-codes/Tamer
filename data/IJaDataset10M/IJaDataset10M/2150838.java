package org.jiopi.ibean.kernel.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.jiopi.ibean.kernel.repository.RemoteFileManager;
import org.jiopi.ibean.share.ShareConstants;
import org.jiopi.ibean.share.ShareUtil.IOUtil;
import org.jiopi.ibean.share.ShareUtil.MyFileLock;

/**
 * 
 * kernel的资源工具类
 * 
 * @since iBean0.1 2010.4.25
 * @version 0.1
 */
public class ResourceUtil extends org.jiopi.ibean.share.ShareUtil.ResourceUtil {

    private static Logger logger = Logger.getLogger(ResourceUtil.class);

    /**
	 * 判断资源是否存在
	 * 
	 * 
	 * v0.1:支持file http协议资源
	 * @param url
	 * @param cacheDir 如果给定了cacheDir，则优先检查本地缓存
	 * @return
	 * @since 0.1
	 */
    public static boolean isResourceExists(URI uri, File cacheDir, UsernamePasswordCredentials creds) {
        String protocol = uri.getScheme();
        if ("file".equals(protocol)) {
            try {
                return new File(uri.toURL().getFile()).exists();
            } catch (MalformedURLException e) {
                logger.warn(e);
            }
        } else if ("http".equals(protocol)) {
            if (cacheDir != null) {
                try {
                    File localFile = RemoteFileManager.getRemoteCacheFile(uri.toURL(), cacheDir);
                    if (localFile.isFile()) {
                        return true;
                    }
                } catch (MalformedURLException e) {
                }
            }
            HttpClient httpclient = createHttpClient(creds);
            try {
                HttpHead httphead = new HttpHead(uri);
                HttpResponse response = httpclient.execute(httphead);
                if (response != null) {
                    StatusLine statusLine = response.getStatusLine();
                    int status = statusLine.getStatusCode() / 100;
                    if (status == 2) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
            } finally {
                httpclient.getConnectionManager().shutdown();
            }
        }
        return false;
    }

    /**
	 * 检查远程HTTP资源是否与本地文件一致
	 * 
	 * @param url
	 * @param localFile
	 * @return
	 * @throws IOException
	 * @since 0.1
	 */
    public static boolean isSameHttpContent(final String url, final File localFile, UsernamePasswordCredentials creds) throws IOException {
        if (localFile.isFile()) {
            long localContentLength = localFile.length();
            long localLastModified = localFile.lastModified() / 1000;
            long contentLength = -1;
            long lastModified = -1;
            HttpClient httpclient = createHttpClient(creds);
            try {
                HttpHead httphead = new HttpHead(url);
                HttpResponse response = httpclient.execute(httphead);
                if (response != null) {
                    StatusLine statusLine = response.getStatusLine();
                    int status = statusLine.getStatusCode() / 100;
                    if (status == 2) {
                        Header lastModifiedHeader = response.getFirstHeader("Last-Modified");
                        Header contentLengthHeader = response.getFirstHeader("Content-Length");
                        if (contentLengthHeader != null) {
                            contentLength = Integer.parseInt(contentLengthHeader.getValue());
                        }
                        if (lastModifiedHeader != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                            formatter.setDateFormatSymbols(new DateFormatSymbols(Locale.US));
                            try {
                                lastModified = formatter.parse(lastModifiedHeader.getValue()).getTime() / 1000;
                            } catch (ParseException e) {
                                logger.error(e);
                            }
                        }
                    } else {
                        return true;
                    }
                }
            } finally {
                httpclient.getConnectionManager().shutdown();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("local:" + localContentLength + " " + localLastModified);
                logger.debug("remote:" + contentLength + " " + lastModified);
            }
            if (contentLength != -1 && localContentLength != contentLength) return false;
            if (lastModified != -1 && lastModified != localLastModified) return false;
            if (contentLength == -1 && lastModified == -1) return false;
            return true;
        }
        return false;
    }

    /**
	 * 将http协议的url对应的资源拷贝到文件中
	 * 
	 * 使用文件锁等待其他下载线程
	 * 
	 * 下载完毕后将比对文件大小(与head头),并将新文件的最后更改日期改为服务器返回的最后更改日期
	 * 
	 * @param url
	 * @param outputFile
	 * @param rewrite 是否覆盖,仅在资源内容大小相同时有效
	 * @throws IOException
	 */
    public static void copyHttpContent(final String url, final File outputFile, UsernamePasswordCredentials creds) throws IOException {
        if (outputFile.exists() && outputFile.isDirectory()) return;
        String outputFilePath = outputFile.getAbsolutePath();
        String outputFilePathTemp = outputFilePath + ".tmp";
        File tmpDownloadFile = FileUtil.createNewFile(outputFilePathTemp, false);
        if (!tmpDownloadFile.isFile()) return;
        MyFileLock fl = FileUtil.tryLockTempFile(tmpDownloadFile, 1000, ShareConstants.connectTimeout);
        if (fl != null) {
            try {
                long contentLength = -1;
                long lastModified = -1;
                OutputStream out = null;
                InputStream in = null;
                HttpClient httpclient = createHttpClient(creds);
                try {
                    HttpGet httpget = new HttpGet(url);
                    HttpResponse response = httpclient.execute(httpget);
                    StatusLine statusLine = response.getStatusLine();
                    int status = statusLine.getStatusCode() / 100;
                    if (status == 2) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            Header lastModifiedHeader = response.getFirstHeader("Last-Modified");
                            Header contentLengthHeader = response.getFirstHeader("Content-Length");
                            if (contentLengthHeader != null) {
                                contentLength = Integer.parseInt(contentLengthHeader.getValue());
                            }
                            if (lastModifiedHeader != null) {
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                                formatter.setDateFormatSymbols(new DateFormatSymbols(Locale.US));
                                try {
                                    lastModified = formatter.parse(lastModifiedHeader.getValue()).getTime();
                                } catch (ParseException e) {
                                    logger.error(e);
                                }
                            }
                            in = entity.getContent();
                            out = new BufferedOutputStream(new FileOutputStream(tmpDownloadFile));
                            IOUtil.copyStreams(in, out);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Get HTTP File ERROR:" + url, e);
                } finally {
                    IOUtil.close(in);
                    IOUtil.close(out);
                    httpclient.getConnectionManager().shutdown();
                }
                if (tmpDownloadFile.isFile()) {
                    if ((contentLength == -1 && tmpDownloadFile.length() > 0) || tmpDownloadFile.length() == contentLength) {
                        IOUtil.copyFile(tmpDownloadFile, outputFile);
                        if (lastModified > 0) outputFile.setLastModified(lastModified);
                    }
                }
            } finally {
                tmpDownloadFile.delete();
                fl.release();
            }
        }
    }

    private static HttpClient createHttpClient(UsernamePasswordCredentials creds) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        if (creds != null) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
            httpclient.setCredentialsProvider(credsProvider);
        }
        HttpParams params = httpclient.getParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, ShareConstants.connectTimeout);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, ShareConstants.connectTimeout);
        return httpclient;
    }
}
