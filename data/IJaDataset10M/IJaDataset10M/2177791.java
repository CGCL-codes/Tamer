package com.induslogic.uddi;

import java.util.*;
import java.net.*;
import java.io.*;
import org.w3c.dom.*;

/**
 * Describe class <code>BusinessInfos</code> here.
 *
 * @author <a href="mailto:rohit@induslogic.com">rohit makin</a>
 * @version 
 * @since 
 * @see UddiObject
 */
public class BusinessInfos extends UddiObject {

    public BusinessInfos() {
        super(UddiTags.BUSINESS_INFOS);
    }

    public BusinessInfos(InputStream is) throws IOException, UDDIXmlException {
        super(is);
    }

    public BusinessInfos(UddiObject o) {
        doc = o.doc;
    }
}
