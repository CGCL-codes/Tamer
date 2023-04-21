package mil2525b;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author robert.harder
 */
public class HttpServer extends TcpServer implements TcpServer.Listener {

    private static final Pattern MIL2525B_PATTERN = Pattern.compile("^GET /mil2525b/(([a-zA-Z\\-]{15})).*");

    private static final Pattern COT_PATTERN = Pattern.compile("^GET /cot/(([a-zA-Z\\-]+)).*");

    private ExecutorService exec = Executors.newCachedThreadPool();

    private Collection<HttpServer.HttpListener> listeners = new LinkedList<HttpServer.HttpListener>();

    public HttpServer() {
        super(8000, new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Mil2525b Http Server");
                t.setDaemon(false);
                return t;
            }
        });
        this.addTcpServerListener(this);
    }

    public static void main(String[] args) throws InterruptedException {
        HttpServer hs = new HttpServer();
        hs.setPort(8000);
        try {
            hs.setPort(Integer.parseInt(args[0]));
        } catch (Exception exc) {
        }
        System.out.println("Listening on port " + hs.getPort());
        hs.start();
    }

    public void tcpServerStateChanged(TcpServer.Event evt) {
    }

    public void tcpServerSocketReceived(TcpServer.Event evt) {
        final Socket sock = evt.getSocket();
        exec.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    InputStream in = sock.getInputStream();
                    OutputStream out = sock.getOutputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line = reader.readLine();
                    Matcher mat = MIL2525B_PATTERN.matcher(line);
                    String mil2525b = null;
                    byte[] data = null;
                    if (mat.matches()) {
                        mil2525b = mat.group(1);
                        data = Mil2525b.getBytesFromMil2525b(mil2525b);
                    } else {
                        mat = COT_PATTERN.matcher(line);
                        if (mat.matches()) {
                            mil2525b = Mil2525b.convertCotTypeToMil2525b(mat.group(1));
                            data = Mil2525b.getBytesFromMil2525b(mil2525b);
                        }
                    }
                    if (data == null) {
                        out.write("HTTP/1.1 404 Not found\r\n".getBytes());
                    } else {
                        out.write("HTTP/1.1 200 OK\r\n".getBytes());
                        out.write("Content-Type: image/png\r\n".getBytes());
                        out.write(("Content-Length: " + data.length + "\r\n").getBytes());
                        out.write("\r\n".getBytes());
                        out.write(data);
                        fireHttpServerImageSent(mil2525b, data);
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                } finally {
                    try {
                        sock.close();
                    } catch (Exception exc) {
                    }
                }
            }
        });
    }

    /** Adds a {@link HttpListener}. */
    public synchronized void addHttpServerListener(HttpServer.HttpListener l) {
        listeners.add(l);
    }

    /** Removes a {@link HttpListener}. */
    public synchronized void removeHttpServerListener(HttpServer.HttpListener l) {
        listeners.remove(l);
    }

    /** Fires event on calling thread. */
    protected synchronized void fireHttpServerImageSent(String mil2525b, byte[] data) {
        HttpServer.HttpListener[] ll = listeners.toArray(new HttpServer.HttpListener[listeners.size()]);
        HttpEvent event = new HttpEvent(this, mil2525b, data);
        for (HttpServer.HttpListener l : ll) {
            l.httpServerImageSent(event);
        }
    }

    public static interface HttpListener extends java.util.EventListener {

        public abstract void httpServerImageSent(HttpEvent evt);
    }

    public static class HttpEvent extends java.util.EventObject {

        private String mil2525b;

        private byte[] data;

        /**
         * Creates a HttpEvent based on the given {@link TcpServer}.
         * @param src the source of the event
         */
        public HttpEvent(HttpServer src, String mil2525b, byte[] data) {
            super(src);
            this.mil2525b = mil2525b;
            this.data = data;
        }

        public String getMil2525b() {
            return this.mil2525b;
        }

        public byte[] getData() {
            return this.data;
        }
    }
}
