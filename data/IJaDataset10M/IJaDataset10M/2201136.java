package mx4j.tools.remote.soap.axis.ser;

import javax.xml.namespace.QName;
import org.apache.axis.encoding.ser.BaseSerializerFactory;

/**
 * @version $Revision: 1.3 $
 */
public class MBeanServerNotificationFilterSerFactory extends BaseSerializerFactory {

    public MBeanServerNotificationFilterSerFactory(Class javaType, QName xmlType) {
        super(MBeanServerNotificationFilterSer.class, xmlType, javaType);
    }
}
