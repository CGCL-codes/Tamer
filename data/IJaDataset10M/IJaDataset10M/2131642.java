package snipsnap.formatter.name;

/**
 * Formatter that capitalizes the name
 *
 * @author stephan
 * @version $Id:PathRemoveFormatter.java 1859 2006-08-08 15:10:07 +0200 (Tue, 08 Aug 2006) leo $
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
