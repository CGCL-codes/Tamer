package jfun.yan.xml.nuts;

/**
 * The Nut class for &lt;entry&gt; tag.
 * <p>
 * @author Ben Yu
 * Nov 9, 2005 11:51:38 PM
 */
public class MapEntry extends LiteralNut {

    private Object key;

    private Class key_type;

    public Class getKey_type() {
        return key_type;
    }

    public void setKey_type(Class key_type) {
        this.key_type = key_type;
    }

    public Object getKey() {
        return key_type == null ? key : convert(key_type, key);
    }

    public void setKey(Object key) {
        this.key = key;
    }

    void eval() {
        checkMandatory("key", key);
        checkMandatory("val", getVal());
    }
}
