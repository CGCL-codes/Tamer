package com.fujitsu.arcon.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.unicore.Vsite;
import org.unicore.upl.ConsignJob;
import org.unicore.upl.Reply;
import org.unicore.upl.Request;
import org.unicore.upl.ServerRequest;
import org.unicore.utility.PacketisedInputStream;
import org.unicore.utility.PacketisedOutputStream;

/**
 * Manages connection to a Gateway at the UPL level. UPL messages can
 * be passed to Vsites, the lifetime of the connection can be managed.
 *
 * @author Sven van den Berghe, Fujitsu Laboratories of Europe
 *
 * @version $Revision: 1.3 $ $Date: 2006/03/10 21:23:47 $
 *
 **/
public abstract class Connection implements com.fujitsu.arcon.common.Connection {

    public static class Exception extends java.lang.Exception {

        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static int compression = 0;

    /**
     * Set all new connections following this call to use compression
	 * on the ZIP stream (underlying the UPL connection) when
     * sending data to the Vsite.
     *
     **/
    public static void compressFiles() {
        compression = 9;
    }

    /**
     * Set all new connections following this call to not use compression
	 * on the ZIP stream (underlying the UPL connection) when
     * sending data to the Vsite.
     *
     **/
    public static void doNotCompressFiles() {
        compression = 0;
    }

    private static Map established_connections = new HashMap();

    private static boolean encrypt = true;

    /**
     * Set all new connections following this call to use compression
	 * on the ZIP stream (underlying the UPL connection) when
     * sending data to the Vsite.
     *
     **/
    static boolean doNotEncrypt() {
        return !encrypt;
    }

    public static Connection getConnection(VsiteTh vsite) throws Connection.Exception {
        Connection c = getConnection(vsite.getReference());
        c.setVsite(vsite.getVsite());
        return c;
    }

    /**
     * Get a connection to a Gateway.
     *
     * Will return an existing connection if it exists
     * otherwise will establish a new connection.
	 *
	 * @param reference The Vsite details.
     *
     **/
    public static Connection getConnection(Reference reference) throws Connection.Exception {
        List l = (List) established_connections.get(reference);
        if (l == null) {
            l = Collections.synchronizedList(new LinkedList());
            established_connections.put(reference, l);
        }
        Connection c = null;
        try {
            do {
                c = (Connection) l.remove(0);
            } while (c.closed);
        } catch (IndexOutOfBoundsException iobex) {
        }
        if (c == null) {
            if (reference instanceof Reference.SSL) {
                c = new SSLConnection(reference);
            } else {
                c = new SocketConnection(reference);
            }
            c.gw_list = l;
        }
        return c;
    }

    private Vsite vsite;

    private List gw_list;

    protected Socket socket;

    /**
	 * @return The raw socket established to the Gateway (and thus the NJS).
	 *
	 **/
    public Socket getSocket() {
        return socket;
    }

    protected ObjectOutputStream output;

    public ObjectOutputStream getObjectOutputStream() throws IOException {
        if (output == null) {
            output = new ObjectOutputStream(socket.getOutputStream());
        }
        return output;
    }

    protected ObjectInputStream input;

    private static boolean keep_open = false;

    /**
     * Should a UPL connection be kept open after
     * use (a UPL trasnaction) or should it be closed.
     *
     * This is global and will apply to all Connections 
	 * created after this call.
     *
     **/
    public static void setKeepOpen(boolean b) {
        keep_open = b;
    }

    public static boolean keepOpen() {
        return keep_open;
    }

    /**
     * Establishes a connection with a server.
     **/
    void connect() throws Connection.Exception {
        _connect();
        closed = false;
    }

    abstract void _connect() throws Connection.Exception;

    /**
     * Send a UPL request to the Gateway (no streamed files).
     *
     * @see org.unicore.upl.Request
     *
     **/
    public Reply send(Request request) throws Connection.Exception {
        try {
            return doSend(request);
        } catch (Connection.Exception cex) {
            if (keepOpen()) {
                CLogger.status("Trying again ..." + cex.getMessage());
                connect();
                return doSend(request);
            } else {
                throw cex;
            }
        }
    }

    private Reply doSend(Request request) throws Connection.Exception {
        try {
            if (output == null) {
                output = new ObjectOutputStream(socket.getOutputStream());
            }
            if (keepOpen()) output.reset();
            output.writeObject(request);
            output.flush();
            return (Reply) getObjectInputStream().readObject();
        } catch (java.lang.Exception ex) {
            _close();
            throw new Connection.Exception("Error sending Request: " + ex.getMessage());
        }
    }

    /**
     * Send a UPL request to the Gateway with the files as streamed bytes.
     *
     * @see org.unicore.upl.ConsignJob
     *
     **/
    public Reply send(ConsignJob request, PortfolioTh[] portfolios) throws Connection.Exception {
        try {
            return doSend(request, portfolios);
        } catch (Connection.Exception cex) {
            if (keepOpen()) {
                CLogger.status("Trying again ..." + cex.getMessage());
                connect();
                return doSend(request, portfolios);
            } else {
                throw cex;
            }
        }
    }

    private byte[] buffer = new byte[4 * 4096];

    private Reply doSend(ConsignJob request, PortfolioTh[] portfolios) throws Connection.Exception {
        if (portfolios == null) return doSend(request);
        try {
            if (output == null) {
                output = new ObjectOutputStream(socket.getOutputStream());
            }
            output.reset();
            output.writeObject(request);
            output.flush();
            ZipOutputStream zos = getDataOutputStream();
            for (int i = 0; i < portfolios.length; i++) {
                String dir_name = portfolios[i].getPortfolio().getUPLDirectoryName() + "/";
                for (int j = 0; j < portfolios[i].getFiles().length; j++) {
                    if (portfolios[i].getFiles()[j].exists()) {
                        String name;
                        if (portfolios[i].getDestinationNames() == null) {
                            name = portfolios[i].getFiles()[j].getName();
                        } else {
                            name = portfolios[i].getDestinationNames()[j];
                        }
                        transferFiles(dir_name, portfolios[i].getFiles()[j], name, zos);
                    } else {
                        CLogger.status("Adding no overwrite marker to stream <" + dir_name + "> " + portfolios[i].getFiles()[j].getName());
                        ZipEntry marker = new ZipEntry(dir_name);
                        marker.setExtra(new byte[1]);
                        zos.putNextEntry(marker);
                        zos.closeEntry();
                    }
                }
            }
            CLogger.status("Done sending streamed");
            doneWithDataOutputStream();
            return (Reply) getObjectInputStream().readObject();
        } catch (java.lang.Exception ex) {
            _close();
            throw new Connection.Exception("Error sending Request with streamed: " + ex.getMessage());
        }
    }

    private void transferFiles(String stream_path, File tr_file, String name, java.util.zip.ZipOutputStream zos) throws IOException {
        if (tr_file.isDirectory()) {
            File[] files = tr_file.listFiles();
            for (int i = 0; i < files.length; i++) {
                transferFiles(stream_path + name + "/", files[i], files[i].getName(), zos);
            }
        } else {
            ZipEntry file = new ZipEntry(stream_path + name);
            file.setSize(tr_file.length());
            byte[] mode = new byte[1];
            mode[0] = 0;
            if (tr_file.canRead()) mode[0] += 4;
            if (tr_file.canWrite()) mode[0] += 2;
            file.setExtra(mode);
            CLogger.status("Streaming: " + file + " as " + stream_path + name);
            zos.putNextEntry(file);
            FileInputStream fis = new FileInputStream(tr_file);
            long first = System.currentTimeMillis();
            long second = System.currentTimeMillis();
            long overhead = second - first;
            long middle = System.currentTimeMillis();
            int read = fis.read(buffer);
            long read_end = System.currentTimeMillis();
            long read_time = (read_end - middle - overhead);
            long write_time = 0;
            int total = 0;
            while (read >= 0) {
                total += read;
                long write_start = System.currentTimeMillis();
                zos.write(buffer, 0, read);
                middle = System.currentTimeMillis();
                read = fis.read(buffer);
                read_end = System.currentTimeMillis();
                write_time += (middle - write_start - overhead);
                read_time += (read_end - middle - overhead);
            }
            fis.close();
            zos.closeEntry();
            CLogger.status("Streamed <" + total + "> bytes of: " + file + "in <" + write_time / 1000.0 + "><" + read_time / 1000.0 + ">");
        }
    }

    private PacketisedInputStream pis = null;

    private ZipInputStream zis = null;

    /**
     * Get the inputstream from the NJS with the streamed Outcome files.
     *
     **/
    public ZipInputStream getStreamedInputStream() throws IOException {
        if (zis == null) {
            pis = new PacketisedInputStream(socket.getInputStream());
            zis = new ZipInputStream(pis);
        }
        return zis;
    }

    /**
     * Finished with Zip stream.
     *
     * Clears dross from end of a Zip stream (empties packets). Always call
	 * at the end of reading some streamed data from a server.
     *
     **/
    public void doneWithStreamedInputStream() {
        if (pis != null) {
            long skipped = -1;
            try {
                skipped = pis.skip(Long.MAX_VALUE);
            } catch (java.lang.Exception ex) {
            }
            CLogger.status("Skipped <" + skipped + "> bytes at end of PacketisedInputStream");
        }
    }

    /**
     * Finished with a Connection. This method must be called on all
	 * Connections when they are done with. If this is not called, then this
	 * Connection willl never be available to be returned in other calls of
	 * {@link #getConnection}.
     *
     **/
    public void done() {
        if (closed) return;
        if (keepOpen()) {
            if (!gw_list.contains(this)) {
                gw_list.add(this);
                CLogger.status("Keeping connection to " + socket.getInetAddress().getHostName() + " open.");
            }
        } else {
            _close();
        }
    }

    private boolean closed = false;

    private void _close() {
        try {
            socket.close();
        } catch (java.lang.Exception ex) {
        }
        input = null;
        output = null;
        closed = true;
        CLogger.status("Closing connection to " + socket.getInetAddress().getHostName());
    }

    protected void finalize() {
        done();
    }

    public void setVsite(Vsite vsite) {
        this.vsite = vsite;
    }

    public Reply sendServerRequest(ServerRequest sr) {
        try {
            sr.setVsite(vsite);
            return send(sr);
        } catch (Connection.Exception e) {
            CLogger.status("Problem sending request through common interface: " + e.getMessage());
            return null;
        }
    }

    public void writeServerRequest(ServerRequest request) {
        try {
            request.setVsite(vsite);
            if (output == null) {
                output = new ObjectOutputStream(socket.getOutputStream());
            }
            if (keepOpen()) output.reset();
            output.writeObject(request);
            output.flush();
        } catch (java.lang.Exception ex) {
            CLogger.status("Problem writing request through common interface: " + ex.getMessage());
        }
    }

    public Reply readReply() {
        try {
            return (Reply) getObjectInputStream().readObject();
        } catch (java.lang.Exception ex) {
            CLogger.status("Problem reading reply through common interface: " + ex.getMessage());
        }
        return null;
    }

    public ZipInputStream getDataInputStream() throws IOException {
        return getStreamedInputStream();
    }

    private ZipOutputStream zos = null;

    private PacketisedOutputStream pos;

    private BufferedOutputStream bos = null;

    public ZipOutputStream getDataOutputStream() throws IOException {
        if (zos == null) {
            pos = new PacketisedOutputStream(socket.getOutputStream());
            bos = new BufferedOutputStream(pos);
            zos = new ZipOutputStream(bos);
            zos.setLevel(compression);
        }
        return zos;
    }

    public void doneWithDataOutputStream() throws IOException {
        try {
            zos.finish();
            zos.flush();
        } catch (IOException ex) {
        }
        IOException to_throw = null;
        try {
            bos.flush();
        } catch (IOException ex) {
            to_throw = ex;
        }
        try {
            pos.finish();
        } catch (IOException ex) {
            if (to_throw == null) to_throw = ex;
        }
        if (to_throw != null) throw to_throw;
    }

    /**
	 * The common interface has done with the Connection, but we may need
	 * to hold it open for other connection attempts, unless there was an
	 * error - when we want to close it down hard.
	 */
    public void closeError() {
        _close();
    }

    public void closeOK() {
        doneWithStreamedInputStream();
        if (zos != null) try {
            zos.flush();
        } catch (IOException e) {
            closeError();
        }
        done();
    }

    public ObjectInputStream getObjectInputStream() throws IOException {
        if (input == null) {
            input = new ObjectInputStream(socket.getInputStream());
        }
        return input;
    }
}
