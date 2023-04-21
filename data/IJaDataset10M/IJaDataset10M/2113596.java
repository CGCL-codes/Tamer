package org.joy.spider.download;

import java.net.*;
import java.io.*;

/**
 * 页面下载器，负责下载给定的页面
 */
public class Tourer {

    private StringBuilder pageBuffer = new StringBuilder();

    private int timeOut = 15000;

    private int maxSize = 1024 * 1024;

    private boolean cancelled;

    private boolean connecting;

    private URLConnection conn = null;

    private Thread downloadThread;

    private String charset;

    public Tourer() {
        downloadThread = Thread.currentThread();
    }

    public static void main(String[] args) throws DownloadException {
        Tourer t = new Tourer();
        System.out.println(t.download("http://www.126.com"));
    }

    /**
     * @param strURL 要获取连接的url
     * @return	如果成功，返回已经设置好的connection
     * @throws DownloadFailedException 如果连接过程失败，则抛出异常
     */
    private void setConnectionHeader(URLConnection conn) {
        conn.setConnectTimeout(getTimeOut());
        conn.setReadTimeout(getTimeOut());
        conn.setRequestProperty("accept", "image/png,*/*;q=0.5");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-CN; rv:1.8.1.13) Gecko/20080311 Firefox/2.0.0.13");
        conn.setRequestProperty("ua-cpu", "x86");
        conn.setRequestProperty("accept-charset", "gb2312,utf-8");
    }

    protected void openConnection(String URL) throws ConnectException {
        URL u = null;
        connecting = true;
        try {
            u = new URL(URL);
            conn = (URLConnection) u.openConnection();
            setConnectionHeader(conn);
            String contentType = conn.getContentType();
            if (contentType != null) {
                contentType = contentType.toLowerCase();
                if (!contentType.startsWith("text/html")) {
                    throw new ConnectException("内容不对");
                }
            }
            if (contentType != null && contentType.endsWith("utf-8")) {
                charset = "utf-8";
            } else {
                charset = "gb2312";
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ConnectException("Failed to connect " + URL);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new ConnectException("Failed to connect " + URL);
        }
        connecting = false;
    }

    /**
     * 下载给定的url的页面
     *
     * @param strURL 给定的url
     * @return 所下载页面的所有字符串
     * @throws 如果下载过程中失败，抛出下载失败异常
     */
    public String download(String URL) throws DownloadException {
        cancelled = false;
        BufferedReader reader = null;
        try {
            openConnection(URL);
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            char[] buffer = new char[2048];
            pageBuffer.delete(0, pageBuffer.length());
            int length = 0;
            length = reader.read(buffer);
            while (length != -1) {
                if (cancelled) {
                    throw new DownloadException("User Cancelled " + URL);
                }
                if (pageBuffer.length() > maxSize) {
                    throw new DownloadException("内容超常 " + URL);
                }
                pageBuffer.append(buffer, 0, length);
                length = reader.read(buffer);
            }
            return pageBuffer.toString();
        } catch (SocketTimeoutException e) {
            throw new DownloadException("Time out " + URL);
        } catch (ConnectException e) {
            throw new DownloadException("Connect failed: " + e.getMessage() + URL);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new DownloadException("IO Exception: " + e.getMessage() + URL);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                System.out.println("流关闭错误");
            }
        }
    }

    public void close() {
        cancelled = true;
        if (connecting) {
        }
        try {
            downloadThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Thread getDownloadThread() {
        return downloadThread;
    }
}
