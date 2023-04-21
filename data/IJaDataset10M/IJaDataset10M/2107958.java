package diet.message;

import java.io.Serializable;
import javax.swing.text.AttributeSet;

public class MessageWYSIWYGDocumentSyncFromClientRemove extends Message implements Serializable {

    private int offset;

    private int length;

    private AttributeSet attr;

    public MessageWYSIWYGDocumentSyncFromClientRemove(String email, String username, int offset, int length, AttributeSet a) {
        this(email, username, offset, length);
        this.attr = a;
    }

    public MessageWYSIWYGDocumentSyncFromClientRemove(String email, String username, int offset, int length) {
        super(email, username);
        this.setOffset(offset);
        this.setLength(length);
    }

    public AttributeSet getAttributeSet() {
        return attr;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public String getMessageClass() {
        return "WYSIWYGDocSyncFromClientRemove";
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
