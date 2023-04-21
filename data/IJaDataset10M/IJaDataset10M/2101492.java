package de.schlund.pfixcore.webservice;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mleidig@schlund.de
 */
public class HttpServiceResponse implements ServiceResponse {

    HttpServletResponse httpResponse;

    public HttpServiceResponse(HttpServletResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Object getUnderlyingResponse() {
        return httpResponse;
    }

    public void setContentType(String ctype) {
        httpResponse.setContentType(ctype);
    }

    public void setCharacterEncoding(String encoding) {
        httpResponse.setCharacterEncoding(encoding);
    }

    public String getCharacterEncoding() {
        return httpResponse.getCharacterEncoding();
    }

    public void setMessage(String message) throws IOException {
        byte[] bytes = message.getBytes("UTF-8");
        OutputStream out = httpResponse.getOutputStream();
        out.write(bytes);
        out.flush();
        out.close();
    }

    public Writer getMessageWriter() throws IOException {
        return httpResponse.getWriter();
    }

    public OutputStream getMessageStream() throws IOException {
        return httpResponse.getOutputStream();
    }
}
