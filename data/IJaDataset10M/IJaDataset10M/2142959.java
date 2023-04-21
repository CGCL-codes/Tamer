package HTTPClient;

import java.io.IOException;

/**
 * This holds various information about an active response. Used by the
 * StreamDemultiplexor and RespInputStream.
 *
 * @version	0.3-3  06/05/2001
 * @author	Ronald Tschal�r
 * @since	V0.2
 */
final class ResponseHandler {

    /** the response stream */
    RespInputStream stream;

    /** the response class */
    Response resp;

    /** the response class */
    Request request;

    /** signals that the demux has closed the response stream, and that
	therefore no more data can be read */
    boolean eof = false;

    /** this is non-null if the stream has an exception pending */
    IOException exception = null;

    /**
     * Creates a new handler. This also allocates the response input
     * stream.
     *
     * @param resp     the reponse
     * @param request  the request
     * @param demux    our stream demultiplexor.
     */
    ResponseHandler(Response resp, Request request, StreamDemultiplexor demux) {
        this.resp = resp;
        this.request = request;
        this.stream = new RespInputStream(demux, this);
        Log.write(Log.DEMUX, "Demux: Opening stream " + this.stream.hashCode() + " for demux (" + demux.hashCode() + ")");
    }

    /** holds the string that marks the end of this stream; used for
	multipart delimited responses. */
    private byte[] endbndry = null;

    /** holds the compilation of the above string */
    private int[] end_cmp = null;

    /**
     * return the boundary string for this response. Set's up the
     * InputStream buffer if neccessary.
     *
     * @param  MasterStream the input stream from which the stream demux
     *                      is reading.
     * @return the boundary string.
     */
    byte[] getEndBoundary(BufferedInputStream MasterStream) throws IOException, ParseException {
        if (endbndry == null) setupBoundary(MasterStream);
        return endbndry;
    }

    /**
     * return the compilation of the boundary string for this response.
     * Set's up the InputStream buffer if neccessary.
     *
     * @param  MasterStream the input stream from which the stream demux
     *                      is reading.
     * @return the compiled boundary string.
     */
    int[] getEndCompiled(BufferedInputStream MasterStream) throws IOException, ParseException {
        if (end_cmp == null) setupBoundary(MasterStream);
        return end_cmp;
    }

    /**
     * Gets the boundary string, compiles it for searching, and initializes
     * the buffered input stream.
     */
    void setupBoundary(BufferedInputStream MasterStream) throws IOException, ParseException {
        String endstr = "--" + Util.getParameter("boundary", resp.getHeader("Content-Type")) + "--\r\n";
        endbndry = endstr.getBytes("8859_1");
        end_cmp = Util.compile_search(endbndry);
        MasterStream.markForSearch();
    }
}
