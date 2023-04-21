package org.apache.fop.fo;

import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;

public class EnumProperty extends Property {

    public static class Maker extends Property.Maker {

        protected Maker(String propName) {
            super(propName);
        }

        /**
         * Called by subclass if no match found.
         */
        public Property checkEnumValues(String value) {
            MessageHandler.errorln("Unknown enumerated value for property '" + getPropName() + "': " + value);
            return null;
        }

        protected Property findConstant(String value) {
            return null;
        }

        public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws FOPException {
            if (p instanceof EnumProperty) return p; else return null;
        }
    }

    private int value;

    public EnumProperty(int explicitValue) {
        this.value = explicitValue;
    }

    public int getEnum() {
        return this.value;
    }

    public Object getObject() {
        return new Integer(this.value);
    }
}
