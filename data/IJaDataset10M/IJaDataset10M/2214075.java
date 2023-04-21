package mx4j.tools.remote.soap.axis.ser;

import javax.xml.namespace.QName;
import org.apache.axis.encoding.ser.BaseDeserializerFactory;

/**
 * @version $Revision: 1.4 $
 */
public class SubjectDeserFactory extends BaseDeserializerFactory {

    public SubjectDeserFactory(Class javaType, QName xmlType) {
        super(SubjectDeser.class, xmlType, javaType);
    }
}
