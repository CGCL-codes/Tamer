package uk.org.ogsadai.service.axis.types;

import java.io.IOException;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BaseSerializerFactory;
import org.apache.axis.encoding.ser.EnumSerializer;
import org.apache.axis.utils.BeanPropertyDescriptor;
import org.apache.axis.utils.BeanUtils;
import org.apache.axis.utils.JavaUtils;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;

/**
 * <tt>BeanSerializer</tt> Factory for <tt>ODBeanSerializer</tt>.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ODBeanSerializerFactory extends BaseSerializerFactory {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    protected transient TypeDesc typeDesc = null;

    protected transient BeanPropertyDescriptor[] propertyDescriptor = null;

    /**
     * Constructor.
     *
     * Invokes <tt>BaseSerializerFactory(ODBeanSerializer.class, 
     * xmlType, javaType)</tt>.
     *
     * @param javaType
     *     See <tt>BaseSerializerFactory#BaseSerializerFactory/2</tt>
     *     <tt>javaType</tt> argument.
     * @param xmlType
     *     See <tt>BaseSerializerFactory#BaseSerializerFactory/2</tt>
     *     <tt>xmlType</tt> argument.
     */
    public ODBeanSerializerFactory(Class javaType, QName xmlType) {
        super(ODBeanSerializer.class, xmlType, javaType);
    }
}
