package org.jgroups.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

public class UnixDomainSocket {

    public static final int STREAM = 1;

    public static final int DGRAM_RECV = 2;

    public static final int DGRAM_SEND = 3;

    static {
        String curDir = System.getProperty("user.dir");
        System.load(curDir + "/lib/network-lib/libunixdomainsocket.so");
    }

    private UDSIStream moIn;

    private UDSOStream moOut;

    private UDSIDgram muIn;

    private UDSODgram muOut;

    private int miTimeout;

    private int m;

    private String nom;

    private static native int nativeOpenSTREAM(String socketFile);

    private static native int nativeRead(int nativeSocketFileHandle);

    private static native int nativeWrite(int nativeSocketFileHandle, int data);

    private static native int nativeOpenDGRAMRecv(String socketFile);

    private static native int nativeOpenDGRAMSend(String socketFile);

    private static native String nativeRecv(int nativeSocketFileHandle);

    private static native int nativeRecvbyte(int nativeSocketFileHandle);

    private static native int nativeSend(int nativeSocketFileHandle, String socketFile, String datos);

    private static native int nativeSendbyte(int nativeSocketFileHandle, String socketFile, int datos);

    private static native void nativeClose(int nativeSocketFileHandle);

    private static native void nativeCloseInput(int nativeSocketFileHandle);

    private static native void nativeCloseOutput(int nativeSocketFileHandle);

    private int miNativeSocketFileHandle;

    /**
    *
    * Creates a unix domain socket and connects it to the server
    * specified by the socket file.
    *
    * @param sSocketFile Name of the socket file
    * 
    * @throws IOException If unable to construct the socket
    *
    **/
    public UnixDomainSocket(String sSocketFile, int mode) throws IOException {
        switch(mode) {
            case DGRAM_RECV:
                if ((miNativeSocketFileHandle = nativeOpenDGRAMRecv(sSocketFile)) < 0) {
                    throw new IOException("Unable to open Unix Domain Socket");
                }
                muIn = new UDSIDgram();
                break;
            case DGRAM_SEND:
                if ((miNativeSocketFileHandle = nativeOpenDGRAMSend(sSocketFile)) < 0) {
                    throw new IOException("Unable to open Unix Domain Socket");
                }
                muOut = new UDSODgram();
                break;
            case STREAM:
                if ((miNativeSocketFileHandle = nativeOpenSTREAM(sSocketFile)) < 0) {
                    throw new IOException("Unable to open Unix Domain Socket");
                }
                moIn = new UDSIStream();
                moOut = new UDSOStream();
                break;
        }
        nom = sSocketFile;
        m = mode;
    }

    /**
    * Returns an input stream for this socket.
    *
    * @return An input stream for reading bytes from this socket
    *
    **/
    public InputStream getInputStream() {
        if (m == this.STREAM) {
            return (InputStream) moIn;
        } else {
            return (InputStream) muIn;
        }
    }

    /**
    * Returns an output stream for this socket.
    *
    * @return An output stream for writing bytes to this socket
    *
    */
    public OutputStream getOutputStream() {
        if (m == this.STREAM) {
            return (OutputStream) moOut;
        } else {
            return (OutputStream) muOut;
        }
    }

    /**
    * Sets the read timeout for the socket. If a read call blocks for
    * the specified amount of time it will be cancelled, and a
    * java.io.InterruptedIOException will be thrown. A timeout of zero
    * is interpreted as an infinite timeout.
    *
    * @param iTimeout The specified timeout, in milliseconds.
    *
    */
    public void setTimeout(int iTimeout) {
        miTimeout = iTimeout;
    }

    /**
    * Closes the socket.
    *
    */
    public void close() {
        nativeClose(miNativeSocketFileHandle);
    }

    /**
    * Local private wrapper class for the native input stream. 
    *
    */
    private class UDSIStream extends InputStream {

        public int read() throws IOException {
            int miData;
            if (miTimeout > 0) {
                UnixDomainSocketReadThread oThread = new UnixDomainSocketReadThread();
                oThread.setDaemon(true);
                oThread.start();
                try {
                    oThread.join(miTimeout);
                } catch (InterruptedException e) {
                }
                if (oThread.isAlive()) {
                    oThread = null;
                    throw new InterruptedIOException("Unix Domain Socket read() call timed out");
                } else {
                    miData = oThread.getData();
                }
            } else {
                miData = nativeRead(miNativeSocketFileHandle);
            }
            return miData;
        }

        public void close() throws IOException {
            nativeCloseInput(miNativeSocketFileHandle);
        }
    }

    /**
    * Local private wrapper class for the native output stream. 
    *
    */
    private class UDSOStream extends OutputStream {

        public void write(int iData) throws IOException {
            if ((nativeWrite(miNativeSocketFileHandle, iData)) < 0) {
                throw new IOException("Unable to write to Unix Domain Socket");
            }
        }

        public void close() throws IOException {
            nativeCloseOutput(miNativeSocketFileHandle);
        }
    }

    /**
    * Thread class reads a byte of data from the socket. Used for enforcing
    * timeouts.
    *
    */
    private class UnixDomainSocketReadThread extends Thread {

        private int miData;

        public void run() {
            miData = nativeRead(miNativeSocketFileHandle);
        }

        public int getData() {
            return miData;
        }
    }

    public class UDSIDgram extends InputStream {

        public int read() throws IOException {
            int datos;
            if (miTimeout > 0) {
                UDSRecvbyteThread oThread = new UDSRecvbyteThread();
                oThread.setDaemon(true);
                oThread.start();
                try {
                    oThread.join(miTimeout);
                } catch (InterruptedException e) {
                }
                if (oThread.isAlive()) {
                    oThread = null;
                    throw new InterruptedIOException("Unix Domain Socket read() call timed out");
                } else {
                    datos = oThread.getDatos();
                }
            } else {
                datos = nativeRecvbyte(miNativeSocketFileHandle);
            }
            return datos;
        }

        public String readString() throws IOException {
            String datos;
            if (miTimeout > 0) {
                UDSRecvThread oThread = new UDSRecvThread();
                oThread.setDaemon(true);
                oThread.start();
                try {
                    oThread.join(miTimeout);
                } catch (InterruptedException e) {
                }
                if (oThread.isAlive()) {
                    oThread = null;
                    throw new InterruptedIOException("Unix Domain Socket read() call timed out");
                } else {
                    datos = oThread.getDatos();
                }
            } else {
                datos = nativeRecv(miNativeSocketFileHandle);
            }
            return datos;
        }

        public void close() throws IOException {
            nativeCloseInput(miNativeSocketFileHandle);
        }
    }

    /**
    * Local private wrapper class for the native output stream. 
    *
    */
    public class UDSODgram extends OutputStream {

        public void write(String datos) throws IOException {
            if ((nativeSend(miNativeSocketFileHandle, nom, datos)) < 0) {
                throw new IOException("Unable to send to Unix Domain Socket");
            }
        }

        public void close() throws IOException {
            nativeCloseOutput(miNativeSocketFileHandle);
        }

        public void write(int datos) throws IOException {
            if ((nativeSendbyte(miNativeSocketFileHandle, nom, datos)) < 0) {
                throw new IOException("Unable to send to Unix Domain Socket");
            }
        }
    }

    /**
    * Thread class reads a byte of data from the socket. Used for enforcing
    * timeouts.
    *
    */
    private class UDSRecvThread extends Thread {

        private String datos;

        public void run() {
            datos = nativeRecv(miNativeSocketFileHandle);
        }

        public String getDatos() {
            return datos;
        }
    }

    private class UDSRecvbyteThread extends Thread {

        private int datos;

        public void run() {
            datos = nativeRecvbyte(miNativeSocketFileHandle);
        }

        public int getDatos() {
            return datos;
        }
    }
}
