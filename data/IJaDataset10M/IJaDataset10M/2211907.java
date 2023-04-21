package se.sics.jipv6.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import se.sics.jipv6.core.IPStack;
import se.sics.jipv6.core.TCPConnection;
import se.sics.jipv6.core.TCPListener;
import se.sics.jipv6.core.TCPPacket;

public class HttpServer implements TCPListener, Runnable {

    private IPStack ipStack;

    private TCPConnection serverConnection;

    private Hashtable servlets = new Hashtable();

    private Vector pending = new Vector();

    private String status = "";

    public HttpServer(IPStack stack) {
        ipStack = stack;
        serverConnection = ipStack.listen(80);
        serverConnection.setTCPListener(this);
        new Thread(this).start();
    }

    public void connectionClosed(TCPConnection connection) {
    }

    public void newConnection(TCPConnection connection) {
        handleConnection(connection);
    }

    public void tcpDataReceived(TCPConnection source, TCPPacket packet) {
    }

    public void registerServlet(String path, HttpServlet servlet) {
        servlets.put(path, servlet);
    }

    private synchronized void handleConnection(TCPConnection connection) {
        System.out.println("%%% HttpServer: gotten new connection, adding to pending...");
        pending.addElement(connection);
        notify();
    }

    private void handlePendingConnections() {
        while (true) {
            TCPConnection connection = null;
            synchronized (this) {
                while (pending.size() == 0) try {
                    System.out.println("%%% HttpServer: worker waiting...");
                    status = "waiting for connections";
                    wait();
                    System.out.println("%%% HttpServer: worker notified...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                status = "got connection";
                connection = (TCPConnection) pending.firstElement();
                pending.removeElementAt(0);
            }
            InputStream input = connection.getInputStream();
            OutputStream output = connection.getOutputStream();
            connection.setTimeout(5000);
            try {
                System.out.println("%%% HttpServer: reading req line from: " + input);
                status = "reading request line";
                String reqLine = readLine(input);
                reqLine = reqLine.trim();
                if (!handleRequest(reqLine, input, output, connection)) {
                    output.write("HTTP/1.0 404 NOT FOUND\r\n\r\n".getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                    input.close();
                } catch (IOException e) {
                }
                connection.close();
            }
        }
    }

    private boolean handleRequest(String reqLine, InputStream input, OutputStream output, TCPConnection connection) throws IOException {
        int space = reqLine.indexOf(' ');
        if (space != -1) {
            String method = reqLine.substring(0, space);
            String path = reqLine.substring(space + 1, reqLine.lastIndexOf(' '));
            System.out.println("Method: " + method);
            System.out.println("Path: " + path);
            int query = reqLine.indexOf('?');
            if (query > 0) {
                path = path.substring(0, query);
            }
            status = "finding servlet: " + path;
            HttpServlet servlet = (HttpServlet) servlets.get(path);
            if (servlet != null) {
                HttpServletRequest req = new HttpServletRequest(connection, method, path);
                HttpServletResponse resp = new HttpServletResponse(connection);
                status = "Servicing servlet";
                servlet.service(req, resp);
                return true;
            }
        }
        return false;
    }

    public void run() {
        System.out.println("%%% HttpServer: worker thread started...");
        handlePendingConnections();
    }

    private String readLine(InputStream input) throws IOException {
        StringBuffer sb = new StringBuffer();
        int c;
        while (((c = input.read()) != -1)) {
            if (c != '\r') sb.append((char) c);
            if (c == '\n') return sb.toString();
        }
        return null;
    }

    public void printStatus(PrintStream out) {
        out.println("HttpServer status: " + status);
    }
}
