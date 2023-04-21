package org.chatka.wmunit;

import java.io.FileNotFoundException;
import org.dom4j.DocumentException;

/**
 * @author jtopin01
 * @deprecated Class org.chatka.wmunit.WSDLFile is deprecated. WUnit does not use wsdl file now.
 */
public class WSDLFile extends com.infovide.qac.wunit.WSDLFile {

    public WSDLFile(String s) throws FileNotFoundException, DocumentException {
        super(s);
    }
}
