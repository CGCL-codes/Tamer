package org.scohen.juploadr.uploadapi.zooomr.authentication;

import org.scohen.juploadr.uploadapi.zooomr.DefaultZooomrHandler;
import org.xml.sax.SAXException;

/**
 * @author steve
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ZooomrAuthHandler extends DefaultZooomrHandler {

    private ZooomrAuth auth;

    public ZooomrAuthHandler(Object auth) {
        super(auth);
        this.auth = (ZooomrAuth) auth;
    }

    public void characters(char[] chars, int start, int end) throws SAXException {
        String cdata = new String(chars, start, end).trim();
        if (cdata.length() > 0) {
            if ("username".equals(lastTag)) {
                auth.setUsername(cdata);
            } else if ("pro".equals(lastTag)) {
                auth.setPro("1".equals(cdata.trim()));
            } else if ("limit".equals(lastTag)) {
                auth.setMonthlyUploadLimit(Long.parseLong(cdata));
            } else if ("used".equals(lastTag)) {
                auth.setCurrentUploadUsed(Long.parseLong(cdata));
            }
        }
    }
}
