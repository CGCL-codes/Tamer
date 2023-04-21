package drcl.comp.contract;

import drcl.comp.Contract;

/**
 * Defines the format of the <i>property</i> message exported by a
 * {@link drcl.comp.Component component} and provides utility methods
 * to retrieve individual fields in such a message.
 *
 * <p>A property message contains the following fields:
 * <table border=1>
 * <tr> <td align=center> FIELD <td align=center> TYPE <td align=center> DESCRIPTION </tr>
 * <tr>
 *     <td valign=top> Property
 *     <td valign=top> <code>Object</code>
 *     <td > The property object.
 * </tr>
 * </table>
 */
public class PropertyContract extends Contract {

    public static final PropertyContract INSTANCE = new PropertyContract();

    public String getName() {
        return "ACA Property Message Format";
    }

    public Object getContractContent() {
        return null;
    }

    public static class Message extends ComponentMessage {

        Object property;

        public Message(Object property_) {
            property = property_;
        }

        public Object getProperty() {
            return property;
        }

        public Contract getContract() {
            return INSTANCE;
        }

        public Object clone() {
            return new Message(property instanceof drcl.ObjectCloneable ? ((drcl.ObjectCloneable) property).clone() : property);
        }

        public String toString(String separator_) {
            return "PROPERTY" + separator_ + property;
        }
    }
}
