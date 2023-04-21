package com.induslogic.uddi;

import java.io.*;
import java.util.*;

/**
 * ChangeRecordDelete.java
 *
 * Created: Sun Nov 10 03:19:17 2001
 *
 * @author <a href="mailto:aseem.bansal@induslogic.com">Aseem Bansal</a>
 * @version
 */
public class ChangeRecordDelete extends UddiObject {

    public ChangeRecordDelete() {
        super(UddiTags.CHANGE_RECORD_DELETE);
    }

    public ChangeRecordDelete(InputStream is) throws UDDIXmlException, IOException {
        super(is);
    }

    public ChangeRecordDelete(UddiObject o) {
        doc = o.doc;
    }

    public void setGenericKey(UddiObject key) throws UDDIXmlException {
        UddiObject obj = null;
        try {
            obj = getGenericKey();
        } catch (UDDIXmlException error) {
        }
        if (((key.getName() == UddiTags.BUSINESS_KEY) || (key.getName() == UddiTags.SERVICE_KEY)) || ((key.getName() == UddiTags.BINDING_KEY) || (key.getName() == UddiTags.TMODEL_KEY))) addElement(key); else throw new UDDIXmlException("Error: Check your data type ");
    }

    public UddiObject getGenericKey() throws UDDIXmlException {
        UddiObject businessKey = null;
        try {
            businessKey = getElement(UddiTags.BUSINESS_KEY);
        } catch (UDDIXmlException error) {
        }
        if (businessKey == null) {
            UddiObject serviceKey = null;
            try {
                serviceKey = getElement(UddiTags.SERVICE_KEY);
            } catch (UDDIXmlException error) {
            }
            if (serviceKey == null) {
                UddiObject bindingKey = null;
                try {
                    bindingKey = getElement(UddiTags.BINDING_KEY);
                } catch (UDDIXmlException error) {
                }
                if (bindingKey == null) {
                    UddiObject tmodelKey = getElement(UddiTags.TMODEL_KEY);
                    return tmodelKey;
                } else return bindingKey;
            } else return serviceKey;
        } else return businessKey;
    }
}
