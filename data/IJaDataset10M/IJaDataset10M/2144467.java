package sourceforge.pebblesframewor.gwt.client.envelopes;

import java.io.Serializable;

/**
 * Field
 * @author JunSun Whang
 * @version $Id: FieldEnvelope.java 128 2009-05-03 21:18:28Z junsunwhang $
 */
public class FieldEnvelope implements Serializable {

    public enum FieldType {

        Unset, String, Integer, TimeStamp
    }

    ;

    private static final long serialVersionUID = 0l;

    private String name = "";

    private FieldType type = FieldType.Unset;

    public FieldEnvelope() {
    }

    public FieldEnvelope(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getType() {
        return this.type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }
}
