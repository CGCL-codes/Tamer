package net.sf.istcontract.wsimport.protocol.xml;

import net.sf.istcontract.wsimport.util.exception.JAXWSExceptionBase;
import net.sf.istcontract.wsimport.util.localization.Localizable;

/**
 * @author WS Development Team
 */
public class XMLMessageException extends JAXWSExceptionBase {

    public XMLMessageException(String key, Object... args) {
        super(key, args);
    }

    public XMLMessageException(Throwable throwable) {
        super(throwable);
    }

    public XMLMessageException(Localizable arg) {
        super("server.rt.err", arg);
    }

    public String getDefaultResourceBundleName() {
        return "com.sun.xml.ws.resources.xmlmessage";
    }
}
