package org.xsocket.connection;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.channels.FileChannel;
import javax.management.JMException;
import org.xsocket.Execution;
import org.xsocket.MaxReadSizeExceededException;

/**
*
* @author grro@xsocket.org
*/
public final class SimpleFileServer extends Server {

    public SimpleFileServer(int port, String dir) throws IOException {
        super(port, new ServerHandler(dir));
    }

    public static void main(String[] args) throws IOException, JMException {
        int port = Integer.parseInt(args[0]);
        String dir = args[1];
        SimpleFileServer server = new SimpleFileServer(port, dir);
        ConnectionUtils.registerMBean(server);
        server.run();
    }

    private static final class ServerHandler implements IDataHandler {

        private String dir = null;

        public ServerHandler(String dir) {
            this.dir = dir;
            System.out.println("SimpleFileServer filestore " + dir);
        }

        public boolean onData(INonBlockingConnection con) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            con.markReadPosition();
            try {
                String cmd = con.readStringByDelimiter("\r\n");
                String name = con.readStringByDelimiter("\r\n");
                if (cmd.equalsIgnoreCase("put")) {
                    int size = con.readInt();
                    con.removeReadMark();
                    con.setHandler(new NetToFileStreamer(this, new File(dir + File.separator + name), size));
                } else if (cmd.equalsIgnoreCase("get")) {
                    con.removeReadMark();
                    File file = new File(dir + File.separator + name);
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    FileChannel fc = raf.getChannel();
                    con.write((int) file.length());
                    long size = con.transferFrom(fc);
                    fc.close();
                    raf.close();
                    System.out.println("SimpleFileServer file download " + file.getAbsolutePath() + " (size=" + size + ")");
                } else {
                    con.write("unsupported command\r\n");
                    con.close();
                }
                return true;
            } catch (BufferUnderflowException bue) {
                con.resetToReadMark();
                return true;
            }
        }
    }

    private static final class NetToFileStreamer implements IDataHandler {

        private final IDataHandler orgHandler;

        private final File file;

        private RandomAccessFile raf;

        private FileChannel fc;

        private int size = 0;

        private int remaining = 0;

        public NetToFileStreamer(IDataHandler orgHandler, File file, int size) throws IOException {
            this.orgHandler = orgHandler;
            this.file = file;
            this.size = size;
            remaining = size;
        }

        public boolean onData(INonBlockingConnection con) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            if (fc == null) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                raf = new RandomAccessFile(file, "rw");
                fc = raf.getChannel();
            }
            try {
                int available = con.available();
                if ((available <= 0) || (remaining == 0)) {
                    return true;
                }
                if (available < remaining) {
                    con.transferTo(fc, available);
                    remaining = remaining - available;
                } else {
                    con.transferTo(fc, remaining);
                    fc.close();
                    raf.close();
                    System.out.println("SimpleFileServer file uploaded " + file.getAbsolutePath() + " (size=" + size + ")");
                    remaining = 0;
                    con.setHandler(orgHandler);
                }
            } catch (IOException ioe) {
                fc.close();
                file.delete();
                throw ioe;
            }
            return true;
        }
    }
}
