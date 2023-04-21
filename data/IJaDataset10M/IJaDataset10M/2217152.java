package snipsnap.formatter.name;

/**
 * Formatter that capitalizes the name of weblogs and comments of weblogse
 *
 * @author stephan
 * @version $Id:WeblogNameFormatter.java 1859 2006-08-08 15:10:07 +0200 (Tue, 08 Aug 2006) leo $
 */
public class WeblogNameFormatter implements NameFormatter {

    private NameFormatter parent = new NoneFormatter();

    public void setParent(NameFormatter parent) {
        this.parent = parent;
    }

    public String format(String name) {
        String parentName = parent.format(name);
        if (parentName.matches(".*/[0-9]{4}-[0-9][0-9]-[0-9][0-9]/[0-9]+")) {
            int lastSlashIndex = parentName.lastIndexOf('/');
            String date = parentName.substring(parentName.lastIndexOf("/", lastSlashIndex - 1) + 1, lastSlashIndex);
            return date + " #" + parentName.substring(lastSlashIndex + 1);
        }
        return parentName;
    }
}
