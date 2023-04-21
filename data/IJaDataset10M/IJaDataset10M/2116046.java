package com.antilia.web.ajaxdownload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.protocol.http.WebResponse;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MyPdfResource extends DynamicWebResource {

    private static final long serialVersionUID = 1L;

    static int BUFFER_SIZE = 10 * 1024;

    /**
	 * 
	 */
    public MyPdfResource() {
    }

    @Override
    protected ResourceState getResourceState() {
        return new ResourceState() {

            @Override
            public String getContentType() {
                return "application/pdf";
            }

            @Override
            public byte[] getData() {
                try {
                    return bytes(MyPdfResource.class.getResourceAsStream("test.pdf"));
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }

    public static byte[] bytes(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(is, out);
        return out.toByteArray();
    }

    @Override
    protected void setHeaders(WebResponse response) {
        super.setHeaders(response);
        response.setAttachmentHeader("test.pdf");
    }

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        while (true) {
            int tam = is.read(buf);
            if (tam == -1) {
                return;
            }
            os.write(buf, 0, tam);
        }
    }
}
