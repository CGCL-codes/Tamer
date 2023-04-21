package org.doit.muffin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.doit.io.ByteArray;
import org.doit.io.HtmlObjectStream;
import org.doit.io.InputObjectStream;
import org.doit.io.OutputObjectStream;
import org.doit.io.SourceObjectStream;
import org.doit.util.ReusableThread;

/**
 * HTTP transaction handler.  A handler is created by muffin.Server for
 * each HTTP transaction.  Given a socket, the handler will deal with
 * the request, reply, and invoke request, reply, and content filters.
 *
 * @see Server
 * @author Mark Boyns
 * @author Fabien Le Floc'h (decryption server option, ContentFilter threads fix)
 */
public class Handler implements Runnable {

    static final boolean DEBUG = false;

    private Monitor monitor = null;

    private FilterManager manager = null;

    private Options options = null;

    private Client client = null;

    private Socket socket = null;

    Request request = null;

    Reply reply = null;

    private HttpRelay http = null;

    private int currentLength = -1;

    private int contentLength = -1;

    private Filter filterList[];

    private long idle = 0;

    private double bytesPerSecond = 0;

    /**
     * Create a Handler.
     */
    protected Handler(Monitor m, FilterManager manager, Options options, Socket socket) {
        this.monitor = m;
        this.manager = manager;
        this.options = options;
        this.socket = socket;
    }

    /**
     * Close all connections associated with this handler.
     */
    private synchronized void close() {
        if (client != null) {
            client.close();
            client = null;
        }
        if (http != null) {
            http.close();
            http = null;
        }
    }

    /**
     * Flush all data to the client.
     */
    private void flush() {
        if (client != null) {
            try {
                client.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        boolean keepAlive = false;
        Exception reason = null;
        Thread.currentThread().setName("Handler(" + socket.getInetAddress().getHostAddress() + ")");
        try {
            client = createClient(socket);
            client.setTimeout(options.getInteger("muffin.readTimeout"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            monitor.register(this);
            do {
                request = null;
                reply = null;
                filterList = null;
                idle = System.currentTimeMillis();
                monitor.update(this);
                try {
                    request = client.read();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                idle = 0;
                monitor.update(this);
                try {
                    keepAlive = processRequest();
                } catch (IOException ioe) {
                    reason = ioe;
                    keepAlive = false;
                } catch (FilterException fe) {
                    reason = fe;
                    keepAlive = false;
                }
                if (request != null && reply != null) {
                    if (reply != null && currentLength > 0) {
                        reply.setHeaderField("Content-length", currentLength);
                    }
                    LogFile logFile = Main.getLogFile();
                    if (logFile != null) {
                        logFile.log(request, reply);
                    }
                }
            } while (keepAlive);
        } finally {
            monitor.unregister(this);
        }
        if (reason != null && reason.getMessage() != null && reason.getMessage().indexOf("Broken pipe") == -1) {
            if (client != null && request != null) {
                error(client.getOutputStream(), reason, request);
            }
            if (!(reason instanceof FilterException)) {
                if (request != null) {
                    System.out.println("Exception '" + reason.getMessage() + "' getting url: " + request.getURL());
                } else {
                    reason.printStackTrace(System.out);
                }
            }
        }
        close();
    }

    private boolean verifyUrlSyntax(Request request) {
        boolean syntaxOk = true;
        String url = request.getURL();
        if (url.startsWith("/")) {
            request.setURL("http://" + Main.getMuffinHost().getHostName() + ":" + options.getString("muffin.port") + url);
        } else if (url.startsWith("https://")) {
            System.out.println("Netscape keep-alive bug: " + url);
            syntaxOk = false;
        } else if (!url.startsWith("http://")) {
            System.out.println("Unknown URL: " + url);
            syntaxOk = false;
        }
        return syntaxOk;
    }

    private Client createClient(Socket socket) throws IOException {
        return new Client(socket);
    }

    private boolean processRequest() throws IOException, FilterException {
        boolean keepAlive = false;
        while (reply == null) {
            boolean secure = false;
            filterList = manager.createFilters(request.getURL());
            if (request.isSecure()) {
                secure = true;
            } else if (!verifyUrlSyntax(request)) {
                return false;
            }
            if (options.getBoolean("muffin.proxyKeepAlive")) {
                keepAlive = (request.containsHeaderField("Proxy-Connection") && request.getHeaderField("Proxy-Connection").equals("Keep-Alive"));
            }
            if (!options.getBoolean("muffin.passthru")) {
                String location = redirect(request);
                if (location != null) {
                    Reply r = Reply.createRedirect(location);
                    client.write(r);
                    return keepAlive;
                }
                filter(request);
            }
            http = createHttpFilter(request);
            if (http == null) {
                if (secure) {
                    http = createHttpsRelay();
                } else {
                    http = createHttpRelay(request, socket);
                }
            } else if (secure && !(http instanceof HttpsConnection)) {
                System.out.println("ignoring non https filter " + http.getClass());
                http = createHttpsRelay();
            }
            try {
                http.sendRequest(request);
                if (http instanceof Http) {
                    ((Http) http).setTimeout(options.getInteger("muffin.readTimeout"));
                }
                reply = http.recvReply(request);
            } catch (RetryRequestException e) {
                http.close();
                http = null;
            }
            if (http != null) {
                if (reply.headerCount() == 0) {
                    String url = request.getURL();
                    if (url.endsWith("/") || url.endsWith(".html") || url.endsWith(".htm")) {
                        reply.setHeaderField("Content-type", "text/html");
                    }
                }
                monitor.update(this);
                if (!options.getBoolean("muffin.passthru")) {
                    if (!options.getBoolean("muffin.dontUncompress")) {
                        uncompressContent(reply);
                    }
                    filter(reply);
                }
                reply.removeHeaderField("Proxy-Connection");
                if (keepAlive && reply.containsHeaderField("Content-length")) {
                    reply.setHeaderField("Proxy-Connection", "Keep-Alive");
                } else {
                    keepAlive = false;
                }
                currentLength = -1;
                contentLength = -1;
                try {
                    contentLength = Integer.parseInt(reply.getHeaderField("Content-length"));
                } catch (NumberFormatException e) {
                }
                monitor.update(this);
                if (secure) {
                    HttpsConnection https = (HttpsConnection) http;
                    int timeout = options.getInteger("muffin.readTimeout");
                    client.write(reply);
                    try {
                        client.setTimeout(timeout);
                        https.setTimeout(timeout);
                        Copy cp = new Copy(client.getInputStream(), https.getOutputStream());
                        ReusableThread thread = Main.getThread();
                        thread.setName("Copy(" + https.toString() + ", " + client.toString() + ")");
                        thread.setRunnable(cp);
                        flushCopy(https.getInputStream(), client.getOutputStream(), -1, true);
                        client.close();
                    } catch (InterruptedIOException iioe) {
                    }
                } else if (reply.hasContent()) {
                    try {
                        processContent();
                    } catch (IOException e) {
                        if (http instanceof Http) {
                            ((Http) http).reallyClose();
                        } else {
                            http.close();
                        }
                        http = null;
                        client.close();
                        client = null;
                        throw e;
                    }
                    if (contentLength == 0) {
                        client.close();
                    }
                    if (reply.getContent() != null) reply.getContent().close();
                } else {
                    client.write(reply);
                }
                http.close();
            }
        }
        return keepAlive;
    }

    private HttpRelay createHttpsRelay() throws IOException {
        HttpRelay http;
        if (options.useHttpsProxy()) {
            http = new Https(options.getString("muffin.httpsProxyHost"), options.getInteger("muffin.httpsProxyPort"), true);
        } else {
            http = new Https(request.getHost(), request.getPort(), false);
        }
        return http;
    }

    private HttpRelay createHttpRelay(Request request, Socket socket) throws IOException {
        HttpRelay http;
        if (HttpdFactory.sendme(request)) {
            http = HttpdFactory.getFactory().createHttpd(socket);
        } else if (options.useHttpProxy()) {
            http = Http.open(SocketCreator.getDefault(), options.getString("muffin.httpProxyHost"), options.getInteger("muffin.httpProxyPort"), true);
        } else {
            http = Http.open(SocketCreator.getDefault(), request.getHost(), request.getPort());
        }
        return http;
    }

    private HttpRelay createHttpFilter(Request request) {
        for (int i = 0; i < filterList.length; i++) {
            if (filterList[i] instanceof HttpFilter) {
                HttpFilter filter = (HttpFilter) filterList[i];
                if ((filter != null) && filter.wantRequest(request)) {
                    return filter;
                }
            }
        }
        return null;
    }

    private InputStream readChunkedTransfer(InputStream in) throws IOException {
        ByteArrayOutputStream chunks = new ByteArrayOutputStream(8192);
        int size = 0;
        contentLength = 0;
        while ((size = reply.getChunkSize(in)) > 0) {
            contentLength += size;
            copy(in, chunks, size, true);
            reply.readLine(in);
        }
        reply.getChunkedFooter(in);
        reply.removeHeaderField("Transfer-Encoding");
        reply.setHeaderField("Content-length", contentLength);
        return new ByteArrayInputStream(chunks.toByteArray());
    }

    private void processContent() throws IOException {
        InputStream in;
        boolean chunked = false;
        if (reply.containsHeaderField("Transfer-Encoding") && reply.getTransferEncoding().equals("chunked")) {
            in = readChunkedTransfer(reply.getContent());
            chunked = true;
        } else {
            in = reply.getContent();
        }
        if (in == null) {
            System.out.println("No inputstream");
            return;
        }
        if (options.getBoolean("muffin.passthru") || !contentNeedsFiltration()) {
            writeReply(in, contentLength, true);
        } else {
            if (options.getBoolean("muffin.proxyKeepAlive")) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(8192);
                filter(in, buffer, contentLength, !chunked);
                writeReply(new ByteArrayInputStream(buffer.toByteArray()), buffer.size(), false);
            } else {
                reply.removeHeaderField("Content-length");
                OutputStream os = writeReplyAndGetOutputStream();
                filter(in, os, -1, !chunked);
                if (os instanceof GZIPOutputStream) {
                    ((GZIPOutputStream) os).finish();
                }
            }
        }
    }

    private OutputStream writeReplyAndGetOutputStream() throws IOException {
        if (contentNeededToGzip()) {
            reply.setHeaderField("Content-Encoding", "gzip");
            reply.removeHeaderField("Content-length");
            client.write(reply);
            return new GZIPOutputStream(client.getOutputStream());
        } else {
            client.write(reply);
            return client.getOutputStream();
        }
    }

    private boolean contentNeededToGzip() {
        boolean force_gzip_content = options.getBoolean("muffin.forceGzipContent");
        boolean gzip_content = (force_gzip_content || options.getBoolean("muffin.gzipContent")) && reply.getHeaderField("Content-Encoding") == null && reply.getContentType().startsWith("text/");
        if (gzip_content) {
            if (force_gzip_content) return true;
            final String accept_encoding = request.getHeaderField("accept-encoding");
            return null != accept_encoding && accept_encoding.toLowerCase().indexOf("gzip") >= 0;
        }
        return false;
    }

    private void writeReply(InputStream in, int length, boolean monitored) throws IOException {
        boolean gzip_content = contentNeededToGzip();
        if (gzip_content) {
            reply.setHeaderField("Content-Encoding", "gzip");
            if (length >= 0) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
                {
                    GZIPOutputStream os = new GZIPOutputStream(bos);
                    copy(in, os, length, false);
                    os.close();
                }
                reply.setHeaderField("Content-length", bos.size());
                client.write(reply);
                copy(new ByteArrayInputStream(bos.toByteArray()), client.getOutputStream(), bos.size(), monitored);
            } else {
                reply.removeHeaderField("Content-length");
                client.write(reply);
                GZIPOutputStream os = new GZIPOutputStream(client.getOutputStream());
                copy(in, os, length, monitored);
                os.finish();
                client.getOutputStream().flush();
            }
        } else {
            client.write(reply);
            copy(in, client.getOutputStream(), contentLength, true);
        }
    }

    /**
     * Pass a request through the redirect filters.
     *
     * @param r a request
     */
    private String redirect(Request r) {
        for (int i = 0; i < filterList.length; i++) {
            if (filterList[i] instanceof RedirectFilter) {
                RedirectFilter rf = (RedirectFilter) filterList[i];
                if (rf.needsRedirection(r)) {
                    String location = rf.redirect(r);
                    return location;
                }
            }
        }
        return null;
    }

    /**
     * Pass a reply through the filters.
     *
     * @param r a reply
     */
    private void filter(Reply r) throws FilterException {
        for (int i = 0; i < filterList.length; i++) {
            if (filterList[i] instanceof ReplyFilter) {
                ((ReplyFilter) (filterList[i])).filter(r);
            }
        }
    }

    /**
     * Pass a request through the filters.
     *
     * @param r a request
     */
    private void filter(Request r) throws FilterException {
        for (int i = 0; i < filterList.length; i++) {
            if (filterList[i] instanceof RequestFilter) {
                ((RequestFilter) (filterList[i])).filter(r);
            }
        }
    }

    private boolean contentNeedsFiltration() {
        for (int i = 0; i < filterList.length; i++) {
            if (filterList[i] instanceof ContentFilter) {
                ContentFilter filter = (ContentFilter) filterList[i];
                if (filter.needsFiltration(request, reply)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void filter(InputStream in, OutputStream out, int length, boolean monitored) throws IOException {
        List inputObjectsList = new LinkedList();
        InputObjectStream inputObjects = new InputObjectStream();
        SourceObjectStream srcObjects;
        if (reply.containsHeaderField("Content-type") && reply.getContentType().equals("text/html")) {
            srcObjects = new HtmlObjectStream(inputObjects);
        } else {
            srcObjects = new SourceObjectStream(inputObjects);
        }
        for (int i = 0; i < filterList.length; i++) {
            if (filterList[i] instanceof ContentFilter) {
                ContentFilter filter = (ContentFilter) filterList[i];
                if (filter.needsFiltration(request, reply)) {
                    OutputObjectStream oo = new OutputObjectStream();
                    InputObjectStream io = new InputObjectStream(oo);
                    filter.setInputObjectStream(inputObjects);
                    filter.setOutputObjectStream(oo);
                    inputObjectsList.add(io);
                    ReusableThread rt = Main.getThread();
                    rt.setPriority(Thread.MIN_PRIORITY);
                    rt.setRunnable(filter);
                    inputObjects = io;
                }
            }
        }
        srcObjects.setSourceInputStream(in);
        srcObjects.setSourceLength(length);
        ReusableThread srcThread = Main.getThread();
        srcThread.setName("ObjectStream Source(" + socket.getInetAddress().getHostAddress() + ")");
        srcThread.setPriority(Thread.MIN_PRIORITY);
        srcThread.setRunnable(srcObjects);
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        try {
            copy(inputObjects, out, monitored);
        } catch (Exception e) {
            for (Iterator i = inputObjectsList.iterator(); i.hasNext(); ) {
                InputObjectStream ios = (InputObjectStream) i.next();
                if (ios != null) {
                    ios.done();
                }
            }
            if (srcObjects != null) {
                srcObjects.close();
            }
            throw new IOException("IO error while copying stream");
        }
    }

    /**
     * Return the content length.
     */
    int getTotalBytes() {
        return contentLength > 0 ? contentLength : 0;
    }

    /**
     * Return the number of bytes read so far.
     */
    int getCurrentBytes() {
        return currentLength > 0 ? currentLength : 0;
    }

    /**
     * Uncompress gzip encoded content.
     */
    private void uncompressContent(Reply reply) throws IOException {
        String encoding = reply.getHeaderField("Content-Encoding");
        if (encoding != null && encoding.toLowerCase().indexOf("gzip") != -1) {
            reply.removeHeaderField("Content-Encoding");
            InputStream gzipIn = new GZIPInputStream(reply.getContent());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            copy(gzipIn, buffer, -1, false);
            String oldContentLength = reply.getHeaderField("Content-length");
            if (oldContentLength != null) {
                reply.setHeaderField(Reply.GzipContentLengthAttribute, oldContentLength);
            }
            reply.setHeaderField("Content-length", buffer.size());
            gzipIn = new ByteArrayInputStream(buffer.toByteArray());
            reply.setContent(gzipIn);
        }
    }

    /**
     * Send a error message to the client.
     *
     * @param out client
     * @param e exception that occurred
     * @param r request
     */
    private void error(OutputStream out, Exception e, Request r) {
        StringBuffer buf = new StringBuffer();
        buf.append("While trying to retrieve the URL: <a href=\"" + r.getURL() + "\">" + r.getURL() + "</a>\r\n");
        buf.append("<p>\r\nThe following error was encountered:\r\n<p>\r\n");
        buf.append("<ul><li>" + e.toString() + "</ul>\r\n");
        byte[] err = HttpErrorFactory.getFactory().createError(400, buf.toString()).toString().getBytes();
        try {
            out.write(err, 0, err.length);
            out.flush();
        } catch (Exception ex) {
        }
    }

    /**
     * Copy in to out.
     *
     * @param in InputStream
     * @param out OutputStream
     * @param monitored Update the Monitor
     */
    private void copy(InputStream in, OutputStream out, int length, boolean monitored) throws IOException {
        if (length == 0) {
            return;
        }
        int n;
        byte buffer[] = new byte[8192];
        long start = System.currentTimeMillis();
        long now = 0, then = start;
        bytesPerSecond = 0;
        if (monitored) {
            currentLength = 0;
        }
        for (; ; ) {
            n = (length > 0) ? Math.min(length, buffer.length) : buffer.length;
            n = in.read(buffer, 0, n);
            if (n < 0) {
                break;
            }
            out.write(buffer, 0, n);
            if (monitored) {
                currentLength += n;
                monitor.update(this);
            }
            now = System.currentTimeMillis();
            bytesPerSecond = currentLength / ((now - start) / 1000.0);
            if (now - then > 1000) {
                out.flush();
            }
            if (length != -1) {
                length -= n;
                if (length == 0) {
                    break;
                }
            }
            then = now;
        }
        out.flush();
        if (DEBUG) {
            System.out.println(currentLength + " bytes processed in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds " + ((int) bytesPerSecond / 1024) + " kB/s");
        }
    }

    /**
     * Copy in to out.
     *
     * @param in InputStream
     * @param out OutputStream
     * @param monitored Update the Monitor
     */
    private void flushCopy(InputStream in, OutputStream out, int length, boolean monitored) throws IOException {
        if (length == 0) {
            return;
        }
        int n;
        byte buffer[] = new byte[8192];
        long start = System.currentTimeMillis();
        bytesPerSecond = 0;
        if (monitored) {
            currentLength = 0;
        }
        for (; ; ) {
            n = (length > 0) ? Math.min(length, buffer.length) : buffer.length;
            n = in.read(buffer, 0, n);
            if (n < 0) {
                break;
            }
            out.write(buffer, 0, n);
            out.flush();
            if (monitored) {
                currentLength += n;
                monitor.update(this);
            }
            bytesPerSecond = currentLength / ((System.currentTimeMillis() - start) / 1000.0);
            if (length != -1) {
                length -= n;
                if (length == 0) {
                    break;
                }
            }
        }
        out.flush();
        if (DEBUG) {
            System.out.println(currentLength + " bytes processed in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds " + ((int) bytesPerSecond / 1024) + " kB/s");
        }
    }

    /**
     * Copy in to out.
     *
     * @param in InputObjectStream
     * @param out OutputStream
     * @param monitored Update the Monitor
     */
    private void copy(InputObjectStream in, OutputStream out, boolean monitored) throws IOException {
        Object obj;
        long start = System.currentTimeMillis();
        long now = 0, then = start;
        bytesPerSecond = 0;
        if (monitored) {
            currentLength = 0;
        }
        for (; ; ) {
            obj = in.read();
            if (obj == null) {
                break;
            }
            if (obj instanceof ByteArray) {
                ByteArray bytes = (ByteArray) obj;
                bytes.writeTo(out);
                currentLength += bytes.length();
            } else if (obj instanceof Byte) {
                Byte b = (Byte) obj;
                out.write(b.byteValue());
                currentLength++;
            } else {
                System.out.println("Unknown object: " + obj.toString());
            }
            if (monitored) {
                monitor.update(this);
                Thread.yield();
            }
            now = System.currentTimeMillis();
            bytesPerSecond = currentLength / ((now - start) / 1000.0);
            if (now - then > 1000) {
                out.flush();
            }
            then = now;
        }
        out.flush();
        if (DEBUG) {
            System.out.println(currentLength + " bytes filtered in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds " + ((int) bytesPerSecond / 1024) + " kB/s");
        }
    }

    /**
     * Return a string represenation of the hander's state.
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("CLIENT ");
        str.append(socket.getInetAddress().getHostAddress());
        str.append(":");
        str.append(socket.getPort());
        str.append(" - ");
        if (request == null) {
            str.append("idle " + ((System.currentTimeMillis() - idle) / 1000.0) + " sec");
        } else {
            if (reply != null && currentLength > 0) {
                str.append("(");
                str.append(currentLength);
                if (contentLength > 0) {
                    str.append("/");
                    str.append(contentLength);
                }
                str.append(" ");
                str.append(((int) bytesPerSecond / 1024) + " kB/s");
                str.append(") ");
            }
            str.append(request.getURL());
        }
        return str.toString();
    }
}
