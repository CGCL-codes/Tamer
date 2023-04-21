package gov.lanl.xmlsig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;

public class GzipTransform extends TransformSpi {

    /** {@link org.apache.commons.logging}logging facility */
    static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(GzipTransform.class.getName());

    /** Field implementedTransformURI */
    public static final String implementedTransformURI = "http://www.iana.org/assignments/http-parameters#gzip";

    /**
     * Method engineGetURI
     * 
     *  
     */
    protected String engineGetURI() {
        return GzipTransform.implementedTransformURI;
    }

    public boolean wantsOctetStream() {
        return true;
    }

    public boolean wantsNodeSet() {
        return false;
    }

    public boolean returnsOctetStream() {
        return true;
    }

    public boolean returnsNodeSet() {
        return false;
    }

    /**
     * Method enginePerformTransform
     * 
     * @param input
     *  
     */
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput input) throws IOException, TransformationException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (input.isByteArray()) {
                byte[] gzipinput = input.getBytes();
                GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(gzipinput));
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    baos.write(buf, 0, len);
                }
            }
            XMLSignatureInput output = new XMLSignatureInput(baos.toByteArray());
            return output;
        } catch (Exception ex) {
            Object exArgs[] = { ex.getMessage() };
            throw new TransformationException("generic.EmptyMessage", exArgs, ex);
        }
    }

    static {
        org.apache.xml.security.Init.init();
    }
}
