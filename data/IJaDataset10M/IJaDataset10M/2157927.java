package org.apache.fop.fo.properties;

import java.util.Hashtable;
import org.apache.fop.datatypes.*;
import org.apache.fop.fo.*;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;

public class LetterValueMaker extends EnumProperty.Maker implements LetterValue {

    protected static final EnumProperty s_propALPHABETIC = new EnumProperty(ALPHABETIC);

    protected static final EnumProperty s_propTRADITIONAL = new EnumProperty(TRADITIONAL);

    protected static final EnumProperty s_propAUTO = new EnumProperty(AUTO);

    public static Property.Maker maker(String propName) {
        return new LetterValueMaker(propName);
    }

    protected LetterValueMaker(String name) {
        super(name);
    }

    public boolean isInherited() {
        return false;
    }

    public Property checkEnumValues(String value) {
        if (value.equals("alphabetic")) {
            return s_propALPHABETIC;
        }
        if (value.equals("traditional")) {
            return s_propTRADITIONAL;
        }
        if (value.equals("auto")) {
            return s_propAUTO;
        }
        return super.checkEnumValues(value);
    }

    private Property m_defaultProp = null;

    public Property make(PropertyList propertyList) throws FOPException {
        if (m_defaultProp == null) {
            m_defaultProp = make(propertyList, "auto", propertyList.getParentFObj());
        }
        return m_defaultProp;
    }
}
