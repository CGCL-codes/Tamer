package pl.edu.pjwstk.p2pp.resources;

import pl.edu.pjwstk.p2pp.objects.ResourceObject;
import pl.edu.pjwstk.p2pp.util.P2PPUtils;

/**
 * Message resource object as defined in PJIIT's extension to P2PP.
 *
 * @author Maciej Skorupka s3874@pjwstk.edu.pl
 */
public class MessageResourceObject extends ResourceObject {

    /**
     * Creates MessageResourceObject with given subtype.
     *
     * @param contentSubType
     */
    public MessageResourceObject(byte contentSubType) {
        super(P2PPUtils.MESSAGE_CONTENT_TYPE, contentSubType);
    }

    /**
     * Creates empty MessageResourceObject.
     */
    public MessageResourceObject() {
        super(P2PPUtils.MESSAGE_CONTENT_TYPE, (byte) 0);
    }

    @Override
    public String getValueAsString() {
        return "TODO Message Value As String";
    }

    /**
     * Returns value of this message as byte array.
     */
    public byte[] getMessageValue() {
        return value.getValue();
    }

    /**
     * Sets new value of this message.
     *
     * @param newValue
     * @return
     */
    public void setMessageValue(byte[] newValue) {
        value.setValue(newValue);
    }
}
