package jgd.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import jgd.JGDXMLFilterChain;

/**
 * This filter not allow chars less than 0x1F 
 * @author rgato
 *
 */
public class JGDXMLFilterCharAllowed extends JGDXMLFilterChain {

    /**
	 * Constructor 
	 */
    public JGDXMLFilterCharAllowed() {
        super();
    }

    /**
	 * Constructor 
	 */
    public JGDXMLFilterCharAllowed(JGDXMLFilterRegenerateUTF filter) {
        super(filter);
    }

    /**
	 * @see jgd.JGDXMLFilter#getFilteredStream(java.io.InputStream)
	 */
    public InputStream getFilteredStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = is.read()) >= 0) {
            if (b > 0x1f) {
                baos.write(b);
            }
        }
        InputStream result = new ByteArrayInputStream(baos.toByteArray());
        if (sucesor != null) {
            result = sucesor.getFilteredStream(result);
        }
        return result;
    }
}
