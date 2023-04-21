package org.form4G.net.microServlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author efrigerio
 *
 */
public class HttpErrorResponse extends MSResponse {

    private String strDate = "";

    public HttpErrorResponse(MSServletRequest request, MSResponse response) throws IOException {
        super();
        URL url = this.getClass().getResource("HTTP_Error.htm");
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int acumBufSize = 0; (urlConnection.getContentLength() - acumBufSize) != 0; ) {
            byte[] buf = null;
            int bufSize = 0;
            if (urlConnection.getContentLength() > 0) {
                buf = new byte[urlConnection.getContentLength()];
                bufSize = inputStream.read(buf, 0, urlConnection.getContentLength());
            } else {
                buf = new byte[1024 * 64];
                bufSize = inputStream.read(buf);
            }
            if (bufSize != -1) {
                outputStream.write(buf, 0, bufSize);
                acumBufSize += bufSize;
            } else acumBufSize = urlConnection.getContentLength();
        }
        inputStream.close();
        outputStream.close();
        this.strDate = outputStream.toString();
        strDate = strDate.replaceAll("<%ResponseCode%>", response.getResponseCode() + "");
        strDate = strDate.replaceAll("<%FileName%>", request.getFileName());
        String str = response.getResponseMessage();
        str = (str == null) ? "" : str;
        strDate = strDate.replaceAll("<%ResponseMessage%>", str);
        this.setContentType("text/html");
        this.getWriter().print(strDate);
        this.getWriter().flush();
        this.getWriter().close();
    }
}
