package browser;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: StyleData.java,v 1.1 2005-07-29 17:14:05 davidsch Exp $
 */
public class StyleData {

    private String _name;

    public StyleData(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String toString() {
        return _name;
    }
}
