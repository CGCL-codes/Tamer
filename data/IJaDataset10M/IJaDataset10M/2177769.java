package org.apache.ws.jaxme.xs.jaxb.impl;

import org.apache.ws.jaxme.xs.XSObject;
import org.apache.ws.jaxme.xs.impl.XSEnumerationImpl;
import org.apache.ws.jaxme.xs.impl.XSUtil;
import org.apache.ws.jaxme.xs.jaxb.JAXBEnumeration;
import org.apache.ws.jaxme.xs.jaxb.JAXBTypesafeEnumMember;
import org.apache.ws.jaxme.xs.xml.XsEEnumeration;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class JAXBEnumerationImpl extends XSEnumerationImpl implements JAXBEnumeration {

    private final JAXBTypesafeEnumMember member;

    /** <p>Creates a new instance of JAXBEnumerationImpl.</p>
   */
    public JAXBEnumerationImpl(XSObject pParent, XsEEnumeration pBaseEnumeration) throws SAXException {
        super(pParent, pBaseEnumeration);
        member = (JAXBTypesafeEnumMember) XSUtil.getSingleAppinfo(getAnnotations(), JAXBTypesafeEnumMember.class);
    }

    public JAXBTypesafeEnumMember getJAXBTypesafeEnumMember() {
        return member;
    }
}
