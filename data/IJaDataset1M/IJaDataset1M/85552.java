package com.xucia.jsponic.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import com.xucia.jsponic.remote.ClientConnection.IndividualRequest;

public class JsponicResponse extends HttpServletResponseWrapper {

    public JsponicResponse(HttpServletResponse response) {
        super(response);
    }

    /**
	 * This takes an object and returns a JSPON string representation
	 * @param returnValue The object to output
	 * @return JSPON string representation
	 */
    public String outputReturnObject(Object returnValue) {
        IndividualRequest requestHandler = ClientConnection.getCurrentObjectResponse();
        return requestHandler.getValueString(returnValue, true);
    }

    public String outputWaitingResponse() throws IOException {
        IndividualRequest requestHandler = ClientConnection.getCurrentObjectResponse();
        return requestHandler.outputWaitingData();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new RedirectedOutputStream(super.getOutputStream());
    }

    OutputStream byteArrayStream = new ByteArrayOutputStream();

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) writer = new PrintWriter(getOutputStream(), true);
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) writer.flush();
        super.flushBuffer();
    }

    public static class RedirectedOutputStream extends ServletOutputStream {

        public RedirectedOutputStream(OutputStream stream) {
            this.stream = stream;
        }

        public void write(byte b[]) throws IOException {
            stream.write(b);
        }

        public void write(byte b[], int off, int len) throws IOException {
            stream.write(b, off, len);
        }

        public void write(int b) throws IOException {
            stream.write(b);
        }

        private OutputStream stream;
    }

    private PrintWriter writer;
}
