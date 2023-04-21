package JavaOrc.diagram;

/**
 * @class AssociationItem
 *
 * @date 08-20-2001
 * @author Eric Crahen
 * @version 1.0
 *
 */
public class AssociationItem extends DesignItem {

    public AssociationItem() {
        this(null);
    }

    public AssociationItem(String name) {
        super(name);
    }

    public String getType() {
        return "dependency";
    }
}
