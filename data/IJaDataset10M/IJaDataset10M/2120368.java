package com.c2b2.ipoint.messaging.pswincom.sendresponse;

import javax.xml.bind.annotation.XmlRegistry;
import com.c2b2.ipoint.messaging.pswincom.sendresponse.SESSION.MSGLST;
import com.c2b2.ipoint.messaging.pswincom.sendresponse.SESSION.MSGLST.MSG;
import com.c2b2.ipoint.messaging.pswincom.sendresponse.SESSION.QRYLST;
import com.c2b2.ipoint.messaging.pswincom.sendresponse.SESSION.QRYLST.QRY;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.c2b2.ipoint.messaging.pswincom.sendresponse package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.c2b2.ipoint.messaging.pswincom.sendresponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QRY }
     * 
     */
    public QRY createSESSIONQRYLSTQRY() {
        return new QRY();
    }

    /**
     * Create an instance of {@link SESSION }
     * 
     */
    public SESSION createSESSION() {
        return new SESSION();
    }

    /**
     * Create an instance of {@link QRYLST }
     * 
     */
    public QRYLST createSESSIONQRYLST() {
        return new QRYLST();
    }

    /**
     * Create an instance of {@link MSG }
     * 
     */
    public MSG createSESSIONMSGLSTMSG() {
        return new MSG();
    }

    /**
     * Create an instance of {@link MSGLST }
     * 
     */
    public MSGLST createSESSIONMSGLST() {
        return new MSGLST();
    }
}
