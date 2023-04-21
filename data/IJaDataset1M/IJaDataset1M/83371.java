package com.googlecode.batchfb.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import com.googlecode.batchfb.util.RequestBuilder.BinaryAttachment;

/**
 * <p>Tool which writes multipart/form-data to a stream.</p>
 * 
 * <p>See <a href="http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4">http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4</a>.</p>
 * 
 * @author Jeff Schnitzer
 */
public class MultipartWriter {

    /** */
    private static final String MULTIPART_BOUNDARY = "**** an awful string which should never exist naturally ****" + Math.random();

    private static final String MULTIPART_BOUNDARY_SEPARATOR = "--" + MULTIPART_BOUNDARY;

    private static final String MULTIPART_BOUNDARY_END = MULTIPART_BOUNDARY_SEPARATOR + "--";

    /** */
    OutputStream out;

    /** */
    public MultipartWriter(RequestDefinition executor) throws IOException {
        executor.setHeader("Content-Type", "multipart/form-data; boundary=" + MULTIPART_BOUNDARY);
        this.out = executor.getContentOutputStream();
    }

    /**
	 * Write the params as multipart/form-data.  Params can include BinaryAttachemnt objects.
	 */
    public void write(Map<String, Object> params) throws IOException {
        LineWriter writer = new LineWriter(this.out);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            writer.println(MULTIPART_BOUNDARY_SEPARATOR);
            if (param.getValue() instanceof BinaryAttachment) {
                BinaryAttachment ba = (BinaryAttachment) param.getValue();
                writer.println("Content-Disposition: form-data; name=\"" + StringUtils.urlEncode(param.getKey()) + "\"; filename=\"" + StringUtils.urlEncode(ba.filename) + "\"");
                writer.println("Content-Type: " + ba.contentType);
                writer.println("Content-Transfer-Encoding: binary");
                writer.println();
                writer.flush();
                int read;
                byte[] chunk = new byte[8192];
                while ((read = ba.data.read(chunk)) > 0) this.out.write(chunk, 0, read);
            } else {
                writer.println("Content-Disposition: form-data; name=\"" + StringUtils.urlEncode(param.getKey()) + "\"");
                writer.println();
                writer.println(StringUtils.urlEncode(param.getValue().toString()));
            }
        }
        writer.println(MULTIPART_BOUNDARY_END);
        writer.flush();
    }
}
