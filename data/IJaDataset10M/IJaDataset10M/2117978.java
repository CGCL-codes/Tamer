package com.induslogic.uddi;

import java.util.*;
import java.io.*;

/**
 * BusinessServices.java
 *
 *
 * Created: Thu Jul 12 18:32:03 2001
 *
 * @author <a href="mailto: "Satyendra Sharma</a>
 * @version
 */
public class BusinessServices extends UddiObject {

    public BusinessServices() {
        super(UddiTags.BUSINESS_SERVICES);
    }

    public BusinessServices(UddiObject o) {
        doc = o.doc;
    }

    public BusinessServices(InputStream is) throws UDDIXmlException, IOException {
        super(is);
    }

    public void setBusinessService(BusinessService bs) {
        addElement(bs);
    }

    public Enumeration getBusinessService() {
        Vector v = new Vector();
        Enumeration e = getElementsNamed(UddiTags.BUSINESS_SERVICE);
        while (e.hasMoreElements()) {
            BusinessService bs = new BusinessService((UddiObject) e.nextElement());
            v.addElement(bs);
        }
        return v.elements();
    }
}
