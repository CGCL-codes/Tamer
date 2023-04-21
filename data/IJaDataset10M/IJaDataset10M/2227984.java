package org.apache.axis2.jaxws.utility;

import org.apache.axis2.jaxws.ExceptionFactory;
import org.apache.axis2.jaxws.i18n.Messages;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.WebServiceException;
import java.lang.reflect.Method;

/**
 * Provides convenience methods to construct a SOAP 1.1 or SOAP 1.2 SAAJ MessageFactory or
 * SOAPFactory. The code uses reflection; thus, when Axis2 upgrades to SAAJ 1.3, no changes will be
 * neded to this class.
 */
public class SAAJFactory {

    private static final String SOAP11_ENV_NS = "http://schemas.xmlsoap.org/soap/envelope/";

    private static final String SOAP12_ENV_NS = "http://www.w3.org/2003/05/soap-envelope";

    public static final String SOAP_1_1_PROTOCOL = "SOAP 1.1 Protocol";

    public static final String SOAP_1_2_PROTOCOL = "SOAP 1.2 Protocol";

    public static final String DYNAMIC_PROTOCOL = "Dynamic Protocol";

    /**
     * Create SOAPFactory using information from the envelope namespace
     *
     * @param namespace
     * @return
     */
    public static SOAPFactory createSOAPFactory(String namespace) throws WebServiceException, SOAPException {
        Method m = getSOAPFactoryNewInstanceProtocolMethod();
        SOAPFactory sf = null;
        if (m == null) {
            if (namespace.equals(SOAP11_ENV_NS)) {
                sf = SOAPFactory.newInstance();
            } else {
                throw ExceptionFactory.makeWebServiceException(Messages.getMessage("SOAP12WithSAAJ12Err"));
            }
        } else {
            String protocol = DYNAMIC_PROTOCOL;
            if (namespace.equals(SOAP11_ENV_NS)) {
                protocol = SOAP_1_1_PROTOCOL;
            } else if (namespace.equals(SOAP12_ENV_NS)) {
                protocol = SOAP_1_2_PROTOCOL;
            }
            try {
                sf = (SOAPFactory) m.invoke(null, new Object[] { protocol });
            } catch (Exception e) {
                throw ExceptionFactory.makeWebServiceException(e);
            }
        }
        return sf;
    }

    /**
     * Create MessageFactory using information from the envelope namespace
     *
     * @param namespace
     * @return
     */
    public static MessageFactory createMessageFactory(String namespace) throws WebServiceException, SOAPException {
        Method m = getMessageFactoryNewInstanceProtocolMethod();
        MessageFactory mf = null;
        if (m == null) {
            if (namespace.equals(SOAP11_ENV_NS)) {
                mf = MessageFactory.newInstance();
            } else {
                throw ExceptionFactory.makeWebServiceException(Messages.getMessage("SOAP12WithSAAJ12Err"));
            }
        } else {
            String protocol = DYNAMIC_PROTOCOL;
            if (namespace.equals(SOAP11_ENV_NS)) {
                protocol = SOAP_1_1_PROTOCOL;
            } else if (namespace.equals(SOAP12_ENV_NS)) {
                protocol = SOAP_1_2_PROTOCOL;
            }
            try {
                mf = (MessageFactory) m.invoke(null, new Object[] { protocol });
            } catch (Exception e) {
                throw ExceptionFactory.makeWebServiceException(e);
            }
        }
        return mf;
    }

    private static Method messageFactoryNewInstanceProtocolMethod = null;

    /**
     * SAAJ 1.3 has a newInstance method that has a protocol parameter.
     *
     * @return newInstance(String) method if available
     */
    private static Method getMessageFactoryNewInstanceProtocolMethod() {
        if (messageFactoryNewInstanceProtocolMethod == null) {
            try {
                messageFactoryNewInstanceProtocolMethod = MessageFactory.class.getMethod("newInstance", new Class[] { String.class });
            } catch (Exception e) {
                messageFactoryNewInstanceProtocolMethod = null;
            }
        }
        return messageFactoryNewInstanceProtocolMethod;
    }

    private static Method soapFactoryNewInstanceProtocolMethod = null;

    /**
     * SAAJ 1.3 has a newInstance method that has a protocol parameter.
     *
     * @return newInstance(String) method if available
     */
    private static Method getSOAPFactoryNewInstanceProtocolMethod() {
        if (soapFactoryNewInstanceProtocolMethod == null) {
            try {
                soapFactoryNewInstanceProtocolMethod = SOAPFactory.class.getMethod("newInstance", new Class[] { String.class });
            } catch (Exception e) {
                soapFactoryNewInstanceProtocolMethod = null;
            }
        }
        return soapFactoryNewInstanceProtocolMethod;
    }
}
