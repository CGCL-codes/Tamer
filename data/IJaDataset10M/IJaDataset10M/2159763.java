package pogvue.io;

import java.util.*;

public class FormatPropertyVector {

    Vector formatProps = new Vector();

    Vector formatDescs = new Vector();

    public void add(String description, String className) {
        formatProps.addElement(new FormatProperty(description, className));
        formatDescs.addElement(description);
    }

    public Vector getFormatNames() {
        return formatDescs;
    }

    public String getClassName(int ind) {
        return ((FormatProperty) formatProps.elementAt(ind)).getClassName();
    }

    public String getSchemeName(int ind) {
        return ((FormatProperty) formatProps.elementAt(ind)).getDescription();
    }

    public boolean contains(String description) {
        return formatDescs.contains(description);
    }

    public int indexOf(String description) {
        return formatDescs.indexOf(description);
    }
}
