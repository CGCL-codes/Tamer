package com.meterware.xml;

import com.meterware.xml.WriteableXMLElement;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public interface WriteableRootElement extends WriteableXMLElement {

    /**
     * Returns the DOCTYPE node to be written for the document containing this root element. May return null if
     * no validation is to be performed.
     */
    String getDocType();
}
