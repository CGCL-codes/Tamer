package http;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import http.HttpRequest.Key;

/**
 * Simple Web Server
 *
 * @author Will
 */
public class Coordinator {

    private static final ResourceBundle locale;

    private static final ResourceBundle mime;

    private static final String escapeURL;

    private static final String escapeHTM;

    private static final SimpleDateFormat timestamp;

    public static final File appPath;

    static {
        locale = ResourceBundle.getBundle("res.en-US");
        mime = ResourceBundle.getBundle("res.mime");
        escapeURL = getString("http.escapeURL");
        escapeHTM = getString("http.escapeHTM");
        timestamp = new SimpleDateFormat(String.format("%s '%s'", getString("http.date"), getString("config.GMT")), java.util.Locale.US);
        timestamp.setTimeZone(java.util.TimeZone.getTimeZone(getString("config.GMT")));
        appPath = new File(System.getProperty("user.dir"));
    }

    private final List<ServeThread> servePool;

    private final List<ListenThread> listenPool;

    private final List<ReadThread> readPool;

    private final Queue<ServeThread> queue;

    private final Semaphore queuer;

    private Logger log;

    /**
     * Incoming
     *
     * @param port  the port that will be listened on
     */
    public Coordinator(Integer workerThreads, Integer... ports) throws IOException {
        this.servePool = new ArrayList<ServeThread>();
        this.listenPool = new ArrayList<ListenThread>();
        this.readPool = new ArrayList<ReadThread>();
        this.queue = new LinkedList<ServeThread>();
        this.queuer = new Semaphore(workerThreads, true);
        addThreads(workerThreads, ports);
    }

    private void addThreads(Integer workerThreads, Integer[] ports) throws IOException {
        Thread temp;
        for (int i = 0; i < workerThreads; ++i) {
            temp = new ServeThread(this);
            temp.start();
            registerThread(temp);
        }
        for (Integer port : ports) {
            temp = new ListenThread(port, this);
            temp.start();
            registerThread(temp);
        }
    }

    public void startServer() {
    }

    /**
     * This method returns a string from the demo's resource bundle.
     */
    public static String getString(String key) {
        String value;
        try {
            value = locale.getString(key);
        } catch (Exception ex) {
            value = "Could not find resource: " + key;
        }
        return value;
    }

    /**
     * This method returns a string from the demo's resource bundle.
     */
    public static String getMime(String key) {
        String value;
        try {
            value = mime.getString(key);
        } catch (Exception ex) {
            value = "text/plain";
        }
        return value;
    }

    public synchronized void registerThread(Thread added) {
        if (added instanceof ServeThread) {
            this.servePool.add((ServeThread) added);
            this.queue.add((ServeThread) added);
        } else if (added instanceof ListenThread) {
            this.listenPool.add((ListenThread) added);
        } else if (added instanceof ReadThread) {
            this.readPool.add((ReadThread) added);
        }
    }

    public synchronized void unregisterThread(Thread added) {
        if (added instanceof ServeThread) {
            this.servePool.remove((ServeThread) added);
            this.queue.remove((ServeThread) added);
        } else if (added instanceof ListenThread) {
            this.listenPool.remove((ListenThread) added);
        } else if (added instanceof ReadThread) {
            this.readPool.remove((ReadThread) added);
        }
    }

    public ServeThread popServerQueue() {
        try {
            queuer.acquire();
        } catch (InterruptedException ex) {
            return null;
        }
        return queue.poll();
    }

    public void pushServerQueue(ServeThread t) {
        queue.add(t);
        queuer.release();
    }

    public String[] readRequest(Socket in, int timeOut) {
        try {
            ReadThread reader = new ReadThread(in, timeOut);
            registerThread(reader);
            List<String> request = reader.read();
            unregisterThread(reader);
            return request.toArray(new String[request.size()]);
        } catch (IOException ex) {
            return new String[0];
        }
    }

    public void sendGetRequest(HttpRequest request, OutputStream out) throws IOException {
        String buffer = unescapeURL(request.getValue(Key.RESOURCE));
        if (buffer.endsWith("/")) {
            buffer = buffer.substring(0, buffer.length() - 1) + getString("pages.index");
        } else if (request.source.isLoopbackAddress()) {
            if (buffer.endsWith(getString("command.shutdown"))) {
                buffer = getString("pages.shutdown");
                shutdown();
            }
        }
        try {
            InputStream inBody = openResource(buffer);
            InputStream inHead = createHeader(200, buffer, inBody.available());
            sendResponseSilent(out, new InputStreamMixer(inHead, inBody));
        } catch (IOException ex) {
            if (buffer.endsWith(getString("pages.index"))) {
                InputStream inBody = sendFileList(buffer);
                InputStream inHead = createHeader(200, buffer, inBody.available());
                sendResponseSilent(out, new InputStreamMixer(inHead, inBody));
            } else {
                buffer = getString("pages.404");
                InputStream inBody = openResource(buffer);
                InputStream inHead = createHeader(404, buffer, inBody.available());
                sendResponseSilent(out, new InputStreamMixer(inHead, inBody));
            }
        }
    }

    private static InputStream sendFileList(String resource) throws IOException {
        String path = resource.substring(0, resource.lastIndexOf("/") + 1);
        File folder = new File(appPath.getAbsolutePath() + path);
        File[] files = folder.listFiles();
        StringBuffer str = new StringBuffer(getString("listing.header"));
        if (files != null) {
            String temp, item = getString("listing.item");
            for (int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) temp = files[i].getName() + "/"; else temp = files[i].getName();
                str.append(String.format(item, escapeURL(temp), escapeHTML(temp)));
            }
        } else if (!folder.exists()) {
            path = getString("listing.notfound");
        }
        String preProc = streamToString(openResource(getString("pages.listing")));
        String postProc = preProc.replace("<%PATH%>", path.replace("/", "&#47;")).replace("<%LIST%>", str);
        return new ByteArrayInputStream(postProc.getBytes());
    }

    public static InputStream openResource(String resource) throws IOException {
        if (!resource.startsWith("/")) return null;
        BufferedInputStream fetch;
        try {
            fetch = new BufferedInputStream(Coordinator.class.getResourceAsStream(getString("pages") + resource));
            fetch.available();
        } catch (IOException ex) {
            fetch = new BufferedInputStream(new FileInputStream(new File(appPath.getAbsolutePath() + resource)));
            fetch.available();
        }
        return fetch;
    }

    public static String streamToString(InputStream in) {
        try {
            byte[] data = new byte[in.available()];
            in.read(data);
            return new String(data);
        } catch (IOException ex) {
            return null;
        }
    }

    public static void sendResponseSilent(OutputStream out, InputStream resource) {
        try {
            sendResponse(out, resource);
        } catch (Exception ex) {
            System.err.println(getString("err.silent_response_fail") + ex);
        }
    }

    public static void sendResponse(OutputStream out, InputStream input) throws Exception {
        byte[] data;
        if (input.available() > 4096) {
            data = new byte[4096];
            while (input.available() >= 4096) {
                input.read(data);
                out.write(data);
            }
        }
        data = new byte[input.available()];
        if (data.length > 0) {
            input.read(data);
            out.write(data);
        }
        input.close();
    }

    public synchronized void shutdown() {
        Iterator<ListenThread> i = listenPool.iterator();
        while (i.hasNext()) i.next().halt();
        Iterator<ServeThread> j = servePool.iterator();
        while (j.hasNext()) j.next().halt();
        Iterator<ReadThread> k = readPool.iterator();
        while (k.hasNext()) k.next().halt();
    }

    public static String escapeHTML(String input) {
        StringBuffer s = new StringBuffer();
        char c;
        for (int i = 0; i < input.length(); ++i) {
            c = input.charAt(i);
            if (escapeHTM.indexOf(c) > -1) s.append("&#" + (int) c + ";"); else s.append(c);
        }
        return s.toString();
    }

    public static String escapeURL(String input) {
        StringBuffer s = new StringBuffer();
        char c;
        for (int i = 0; i < input.length(); ++i) {
            c = input.charAt(i);
            if (escapeURL.indexOf(c) > -1) s.append("%" + Integer.toHexString(c)); else s.append(c);
        }
        return s.toString();
    }

    public static String unescapeURL(String input) {
        try {
            StringBuffer s = new StringBuffer();
            char c;
            for (int i = 0; i < input.length(); ++i) {
                c = input.charAt(i);
                if (c == '%' && i + 3 <= input.length()) {
                    Integer x = Integer.decode("0x" + input.substring(i + 1, i + 3));
                    if (x != null) c = (char) x.intValue(); else c = 0;
                    i += 2;
                } else if (c == '+') {
                    c = ' ';
                }
                if (c > 31 && c < 127) s.append(c);
            }
            return s.toString();
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static String getExt(String resource) {
        int i;
        if ((i = resource.lastIndexOf(".")) > -1) return resource.substring(i + 1);
        if ((i = resource.lastIndexOf("/")) > -1) return resource.substring(i + 1);
        return "bin";
    }

    /**
     * 200=ok, 201=created, 202=accepted, 204=nocontent, 301=movedpermanently,
     * 302=movedtemporarily, 304=notmodified, 400=badrequest, 401=unauthorized,
     * 403=forbidden, 404=notfound, 500=internalservererror, 501=notimplemented,
     * 502=badgateway, 503=serviceunavailable
     */
    public static InputStream createHeader(int code, String resource, int size) {
        String ext = getMime(getExt(resource));
        StringBuilder s = new StringBuilder();
        s.append(String.format("HTTP/1.1 %s OK\n", code));
        s.append(String.format("Content-Type: %s\n", ext));
        s.append(String.format("Accept-Ranges: %s\n", "bytes"));
        s.append(String.format("Expires: %s\n", -1));
        s.append(String.format("Date: %s\n", getTimestamp()));
        s.append(String.format("Content-length: %s\n", size));
        s.append("\n");
        return new ByteArrayInputStream(s.toString().getBytes());
    }

    public static String getTimestamp() {
        return timestamp.format(new java.util.Date());
    }

    public synchronized Logger getLogger() {
        if (log == null) {
            log = new Logger();
            Runtime.getRuntime().addShutdownHook(log);
        }
        return log;
    }
}
