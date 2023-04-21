package neon.tools.objects;

import org.jdom.Element;
import java.awt.datatransfer.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ObjectTreeNode extends DefaultMutableTreeNode implements Transferable {

    private Element properties;

    private ObjectType type;

    public ObjectTreeNode(Element e, ObjectType t) {
        properties = e;
        type = t;
    }

    public Element getProperties() {
        return properties;
    }

    public ObjectType getType() {
        return type;
    }

    public String toString() {
        String id = properties.getAttributeValue("id");
        if (properties.getNamespacePrefix().equals("ext")) {
            return "<html><font color=\"red\">" + id + "</font></html>";
        } else {
            return id;
        }
    }

    public enum ObjectType {

        ITEM, CLOTHING, WEAPON, BOOK, LIGHT, CREATURE, NPC, CONTAINER, DOOR, MONEY, POTION, SCROLL, SPELL, LEVEL_CREATURE, LEVEL_ITEM, LEVEL_SPELL, FOOD
    }

    public Object getTransferData(DataFlavor flavor) {
        return this;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return null;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
    }
}
