package wtanaka.praya.obj;

/**
 * Password config Obj.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @deprecated use wtanaka.praya.config.PasswordConfigItem instead
 * @see wtanaka.praya.config.PasswordConfigItem
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2001/10/03 07:17:28 $
 **/
public class PasswordConfigObj extends Obj implements java.io.Serializable {

    String key, data;

    ProtoObj parent;

    public PasswordConfigObj(ProtoObj parent, String key, String data) {
        this.parent = parent;
        this.key = key;
        this.data = data;
        if (data == null) data = "";
    }

    public String getKey() {
        return key;
    }

    public String getSubject() {
        return key;
    }

    public void setValue(char[] newValue) {
        if (newValue == null) throw new RuntimeException("don't pass null pointers here");
        StringBuffer newStringBuffer = new StringBuffer();
        for (int i = 0; i < newValue.length; ++i) newStringBuffer.append((char) newValue[i]);
        String newString = newStringBuffer.toString();
        if (!data.equals(newString)) {
            data = newString;
            parent.update(this);
        }
    }

    public String getValue() {
        return data;
    }

    /**
    * This is the string that gets displayed when this is rendered as an
    * obj.
    **/
    public String getContents() {
        return "[not shown]";
    }

    public static void main(String[] args) {
    }
}
