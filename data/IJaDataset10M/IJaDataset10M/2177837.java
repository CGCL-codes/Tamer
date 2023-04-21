package org.xactor.ws.atomictx.element;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.xactor.ws.Constants;
import org.xactor.ws.ElementWriter;
import org.xactor.ws.StringBufferFactory;
import org.xactor.ws.XMLFragmentMetadata;

/**
 * Base class for simple WS-AtomicTransaction XML elements.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public abstract class WSATSimpleElement implements Constants {

    protected WSATSimpleElement() {
    }

    protected XMLFragmentMetadata getSimpleElementMetadata(QName qname) {
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put(WSAT.Namespaces.PF_WSAT, WSAT.Namespaces.NS_WSAT_10);
        StringBuffer buff = StringBufferFactory.newStringBuffer();
        ElementWriter.writeEmptyElement(buff, qname);
        return new XMLFragmentMetadata(buff.toString(), namespaces);
    }
}
