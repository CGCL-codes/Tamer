package World;

import org.simpleframework.xml.Attribute;

public class SignElement {

    @Attribute
    public String code;

    @Attribute
    public String desc;

    @Attribute
    public String info;

    @Override
    public String toString() {
        return desc;
    }
}
