package fi.hip.gb.serializer;

import java.util.Iterator;
import java.util.Vector;
import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializerFactory;

/**
 * @author Juho Karppinen
 * @version $Id: URLSerializerFactory.java 102 2004-11-12 14:31:37Z jkarppin $
 */
public class URLSerializerFactory implements SerializerFactory {

    private Vector mechanisms;

    public URLSerializerFactory() {
    }

    public javax.xml.rpc.encoding.Serializer getSerializerAs(String mechanismType) {
        return new URLSerializer();
    }

    public Iterator getSupportedMechanismTypes() {
        if (mechanisms == null) {
            mechanisms = new Vector();
            mechanisms.add(Constants.AXIS_SAX);
        }
        return mechanisms.iterator();
    }
}
