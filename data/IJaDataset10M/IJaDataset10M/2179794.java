package org.apache.fop.fo.properties;

import java.util.Hashtable;
import org.apache.fop.datatypes.*;
import org.apache.fop.fo.*;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;

public class FontVariantMaker extends EnumProperty.Maker implements FontVariant {

    protected static final EnumProperty s_propNORMAL = new EnumProperty(NORMAL);

    protected static final EnumProperty s_propSMALL_CAPS = new EnumProperty(SMALL_CAPS);

    public static Property.Maker maker(String propName) {
        return new FontVariantMaker(propName);
    }

    protected FontVariantMaker(String name) {
        super(name);
    }

    public boolean isInherited() {
        return true;
    }

    public Property checkEnumValues(String value) {
        if (value.equals("normal")) {
            return s_propNORMAL;
        }
        if (value.equals("small-caps")) {
            return s_propSMALL_CAPS;
        }
        return super.checkEnumValues(value);
    }

    private Property m_defaultProp = null;

    public Property make(PropertyList propertyList) throws FOPException {
        if (m_defaultProp == null) {
            m_defaultProp = make(propertyList, "normal", propertyList.getParentFObj());
        }
        return m_defaultProp;
    }
}
