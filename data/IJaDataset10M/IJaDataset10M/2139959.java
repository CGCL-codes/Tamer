package marquee.xmlrpc.serializers;

import marquee.xmlrpc.XmlRpcCustomSerializer;
import marquee.xmlrpc.XmlRpcException;

/**
 *  @author  Greger Ohlson (greger.ohlson@marquee.se)
 *  @version $Revision: 1.2 $
 *  @since   JDK 1.1
 */
public class IntArraySerializer implements XmlRpcCustomSerializer {

    /**
     *  <describe>
     */
    public Class getSupportedClass() {
        return int[].class;
    }

    /**
     *  <describe>
     */
    public void serialize(Object value, StringBuffer output) throws XmlRpcException {
        output.append("<array><data>");
        int[] array = (int[]) value;
        for (int i = 0; i < array.length; ++i) {
            output.append("<value><i4>");
            output.append(Integer.toString(array[i]));
            output.append("</i4></value>");
        }
        output.append("</data></array>");
    }
}
