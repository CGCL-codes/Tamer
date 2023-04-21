package org.snipsnap.snip.name;

/**
 * Formatter that capitalizes the name
 *
 * @author stephan
 * @version $Id: PathRemoveFormatter.java 953 2003-08-27 09:22:47Z stephan $
 */
public class PathRemoveFormatter implements NameFormatter {

    private NameFormatter parent = new NoneFormatter();

    public void setParent(NameFormatter parent) {
        this.parent = parent;
    }

    public String format(String name) {
        String parentName = parent.format(name);
        int index = parentName.lastIndexOf("/");
        if (-1 == index) {
            return parentName;
        } else if (parentName.length() == index + 1) {
            return parentName.substring(0, index);
        } else {
            return parentName.substring(index + 1);
        }
    }
}
