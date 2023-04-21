package org.web3d.x3d;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 * UrlStatus.java
 * Created on Aug 22, 2008
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public class UrlStatus {

    public static final int MISMATCHED_CASE = -2;

    public static final int INDETERMINATE = -1;

    public static final int SUCCESS = HttpStatus.SC_OK;

    public static final int FAILURE = HttpStatus.SC_NOT_FOUND;

    private static Protocol myhttpsProtocolHandler = null;

    public static Object checkStatus(String url, File baseDir, UrlStatusListener listener) {
        Runner r = new Runner();
        Thread thr = new Thread(r, "org.web3d.x3d.tools.UrlStatus");
        thr.setPriority(Thread.NORM_PRIORITY);
        ActionPacket packet = new ActionPacket(url, baseDir, listener, thr);
        r.setData(packet);
        thr.start();
        return packet;
    }

    static class Runner implements Runnable {

        ActionPacket packet;

        public void setData(ActionPacket packet) {
            this.packet = packet;
        }

        @Override
        public void run() {
            String protocol;
            URL urlObj = null;
            try {
                urlObj = new URL(packet.url);
                protocol = urlObj.getProtocol();
            } catch (MalformedURLException murl) {
                protocol = "file";
            }
            try {
                if (protocol.equalsIgnoreCase("http")) finish(handleHttp()); else if (protocol.equalsIgnoreCase("https")) finish(handleHttps()); else if (protocol.equalsIgnoreCase("ftp")) finish(handleFtp()); else if (protocol.equalsIgnoreCase("file")) finish(handleFile(urlObj, packet.url)); else {
                    finish(INDETERMINATE);
                }
            } catch (UnknownHostException uhe) {
                finish(isNetworkUp() ? FAILURE : INDETERMINATE);
                return;
            } catch (LinkageError le) {
                System.err.println("Linkage error in UrlStatus, upgrade httpclient and sslsocketfactory");
                System.err.println(le.getLocalizedMessage());
                finish(INDETERMINATE);
                return;
            } catch (Exception ex) {
                System.err.println(ex.getClass().getSimpleName() + " in UrlStatus");
                System.err.println(ex.getLocalizedMessage());
                finish(INDETERMINATE);
            }
        }

        private int handleHttp() throws Exception {
            HttpClient client = new HttpClient();
            HeadMethod head = new HeadMethod(packet.url);
            return client.executeMethod(head);
        }

        /**
     * This is only necessary to allow self-signed certs to pass.
     * @throws java.lang.Exception
     */
        @SuppressWarnings("deprecation")
        private int handleHttps() throws Exception {
            if (myhttpsProtocolHandler == null) {
                myhttpsProtocolHandler = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
                Protocol.registerProtocol("https", myhttpsProtocolHandler);
            }
            return handleHttp();
        }

        /**
     * Can try to support, but don't see any uses in scene archives
     * @throws java.lang.Exception
     */
        private int handleFtp() throws Exception {
            return UrlStatus.INDETERMINATE;
        }

        /**
     * 
     * @param url might be null
     * @throws java.lang.Exception
     */
        private int handleFile(URL url, String originalFileName) throws Exception {
            String filePath;
            File f, parentDirectory;
            if (url != null) {
                filePath = url.getPath();
                if (filePath.startsWith("/") || filePath.indexOf(":") != -1) {
                    f = new File(filePath);
                } else {
                    f = new File(packet.baseDir, filePath);
                }
            } else {
                filePath = packet.url;
                int idx = filePath.lastIndexOf('#');
                if (idx > 0) {
                    filePath = filePath.substring(0, idx);
                }
                f = new File(packet.baseDir, filePath);
            }
            parentDirectory = new File(f.getParent());
            for (int i = 0; i < parentDirectory.listFiles().length; i++) {
                String newFileName = parentDirectory.listFiles()[i].getName();
                if (parentDirectory.listFiles()[i].isDirectory()) continue;
                if (!originalFileName.equals(newFileName) && originalFileName.equalsIgnoreCase(newFileName)) return UrlStatus.MISMATCHED_CASE;
            }
            if (f.exists()) return UrlStatus.SUCCESS; else return UrlStatus.FAILURE;
        }

        private void finish(int status) {
            synchronized (packet) {
                packet.lis.statusIn(packet, status);
            }
            nullOut(packet);
            packet = null;
        }

        private boolean isNetworkUp() {
            try {
                Method isUpMethod = NetworkInterface.class.getDeclaredMethod("isUp", (Class[]) null);
                Method isLoopback = NetworkInterface.class.getDeclaredMethod("isLoopback", (Class[]) null);
                Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                while (en.hasMoreElements()) {
                    NetworkInterface ni = en.nextElement();
                    if (isUpMethod.invoke(ni, (Object[]) null) == Boolean.TRUE && isLoopback.invoke(ni, (Object[]) null) == Boolean.FALSE) return true;
                }
                return false;
            } catch (Exception e) {
                return true;
            }
        }
    }

    private static void nullOut(ActionPacket pkt) {
        pkt.thread = null;
        pkt.lis = null;
        pkt.url = null;
    }

    public static void stopListener(Object key) {
        synchronized (key) {
            try {
                ((ActionPacket) key).thread.interrupt();
                nullOut((ActionPacket) key);
            } catch (Throwable t) {
                System.err.println("UrlStatus.stopListener called with invalid key: " + t.getClass().getSimpleName() + ": " + t.getLocalizedMessage());
            }
        }
        key = null;
    }

    public static interface UrlStatusListener {

        /**
     * Status receiver.  After this is called, the held listener object will be discarded.
     * @param keyPack
     * @param status One of SUCCESS, FAILURE or BADURL
     */
        public void statusIn(Object keyPack, int status);
    }

    public static class ActionPacket {

        String url;

        UrlStatusListener lis;

        Thread thread;

        File baseDir;

        ActionPacket(String url, File baseDir, UrlStatusListener lis, Thread t) {
            this.url = url;
            this.baseDir = baseDir;
            this.lis = lis;
            this.thread = t;
        }
    }
}
