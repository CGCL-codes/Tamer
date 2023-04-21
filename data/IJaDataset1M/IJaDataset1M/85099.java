package ca.uhn.hl7v2.llp;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Implements the "Minimal Lower Layer Protocol" from the HL7 Implementation 
 * Guide, Appendix C.  In other words, provides a reader and a writer that can be 
 * used to communicate with a server that uses the minimal LLP.
 * 
 * @see MinLLPReader
 * @see MinLLPWriter 
 * @author Bryan Tripp
 */
public class MinLowerLayerProtocol extends LowerLayerProtocol {

    private Charset charset;

    /** 
     * Creates new MinLowerLayerProtocol 
     */
    public MinLowerLayerProtocol() {
    }

    /**
     * Creates an HL7Reader that implements message reading according to 
     * this protocol.  
     */
    public HL7Reader getReader(InputStream in) throws LLPException {
        try {
            if (charset != null) {
                return new MinLLPReader(in, charset);
            } else {
                return new MinLLPReader(in);
            }
        } catch (IOException e) {
            throw new LLPException("Can't create MinLLPReader with the given input stream: " + e.getMessage(), e);
        }
    }

    /**
     * Creates an HL7Writer that implements message writing according to 
     * this protocol.  
     */
    public HL7Writer getWriter(OutputStream out) throws LLPException {
        try {
            if (charset != null) {
                return new MinLLPWriter(out, charset);
            } else {
                return new MinLLPWriter(out);
            }
        } catch (IOException e) {
            throw new LLPException("Can't create MinLLPWriter with the given output stream: " + e.getMessage(), e);
        }
    }

    /**
     * Provides a charset to use for character encoding
     * @param theCharset The charset to use
     * @since 1.3
     */
    public void setCharset(Charset theCharset) {
        charset = theCharset;
    }
}
