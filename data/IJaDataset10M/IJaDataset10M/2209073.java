package jaxlib.management.openmbean;

import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.SimpleType;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: OpenAttributes.java 2269 2007-03-16 08:40:10Z joerg_wassmer $
 */
public class OpenAttributes extends Object {

    protected OpenAttributes() throws InstantiationException {
        throw new InstantiationException();
    }

    public static final OpenMBeanAttributeInfoSupport[] EMPTY_ATTRIBUTE_INFO = new OpenMBeanAttributeInfoSupport[0];

    public static OpenMBeanAttributeInfoSupport createEnumAttributeInfo(Class<? extends Enum> enumType, String name, String description, boolean isReadable, boolean isWritable, boolean isIs) throws OpenDataException {
        Enum[] enumValues = enumType.getEnumConstants();
        if (enumValues == null) throw new IllegalArgumentException("Not an enum type: " + enumType);
        String[] legalValues = new String[enumValues.length];
        for (int i = enumValues.length; --i >= 0; ) legalValues[i] = enumValues[i].name();
        enumValues = null;
        return new OpenMBeanAttributeInfoSupport(name, description, SimpleType.STRING, isReadable, isWritable, isIs, null, legalValues);
    }
}
