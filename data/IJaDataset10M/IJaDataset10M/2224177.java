package javax.servlet.http;

import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

/**
 An InputStream that implements HTTP/1.1 chunking.
 <P>
 This class lets a Servlet read its request data as an HTTP/1.1 chunked
 stream.  Chunked streams are a way to send arbitrary-length data without
 having to know beforehand how much you're going to send.  They are
 introduced by a "Transfer-Encoding: chunked" header, so if such a header
 appears in an HTTP request you should use this class to read any data.
 <P>
 Sample usage:
 <BLOCKQUOTE><PRE><CODE>
 InputStream in = req.getInputStream();
 if ( "chunked".equals( req.getHeader( "Transfer-Encoding" ) ) )
     in = new ChunkedInputStream( in );
 </CODE></PRE></BLOCKQUOTE>
 <P>
 Because it would be impolite to make the authors of every Servlet include
 the above code, this is general done at the server level so that it
 happens automatically.  Servlet authors will generally not create
 ChunkedInputStreams.  This is in contrast with ChunkedOutputStream,
 which Servlets have to call themselves if they want to use it.
 <P>
 <A HREF="/resources/classes/Acme/Serve/servlet/http/ChunkedInputStream.java">Fetch the software.</A><BR>
 <A HREF="/resources/classes/Acme.tar.gz">Fetch the entire Acme package.</A>
*/
public class ChunkedInputStream extends FilterInputStream {

    private DataInputStream din;

    private int contentLength;

    public ChunkedInputStream(InputStream in) {
        super(new DataInputStream(in));
        din = (DataInputStream) this.in;
        contentLength = 0;
    }

    private byte[] b1 = new byte[1];

    public int read() throws IOException {
        if (read(b1, 0, 1) == -1) return -1;
        return b1[0];
    }

    private int chunkCount = 0;

    public int read(byte b[], int off, int len) throws IOException {
        if (chunkCount == 0) {
            startChunk();
            if (chunkCount == 0) return -1;
        }
        int toRead = Math.min(chunkCount, len);
        int r = din.read(b, off, toRead);
        if (r != -1) chunkCount -= r;
        return r;
    }

    private void startChunk() throws IOException {
        String line = din.readLine();
        try {
            chunkCount = Integer.parseInt(line, 16);
        } catch (NumberFormatException e) {
            throw new IOException("malformed chunk");
        }
        contentLength += chunkCount;
        if (chunkCount == 0) readFooters();
    }

    private Vector footerNames = null;

    private Vector footerValues = null;

    private void readFooters() throws IOException {
        footerNames = new Vector();
        footerValues = new Vector();
        String line;
        while (true) {
            line = din.readLine();
            if (line.length() == 0) break;
            int colon = line.indexOf(':');
            if (colon != -1) {
                String name = line.substring(0, colon).toLowerCase();
                String value = line.substring(colon + 1).trim();
                footerNames.addElement(name.toLowerCase());
                footerValues.addElement(value);
            }
        }
    }

    public String getFooter(String name) {
        if (!isDone()) return null;
        int i = footerNames.indexOf(name.toLowerCase());
        if (i == -1) return null;
        return (String) footerValues.elementAt(i);
    }

    public Enumeration getFooters() {
        if (!isDone()) return null;
        return footerNames.elements();
    }

    public int getContentLength() {
        if (!isDone()) return -1;
        return contentLength;
    }

    public boolean isDone() {
        return footerNames != null;
    }
}
