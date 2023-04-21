package org.equanda.persistence.xml;

import org.equanda.util.SaveException;
import org.equanda.util.xml.tree.CharacterData;
import org.equanda.util.xml.tree.Node;
import org.equanda.util.xml.tree.NodeWithChildConstraints;
import org.equanda.util.xml.tree.XMLTreeException;
import javolution.xml.sax.Attributes;

/**
 * representation of a &lt;choice&gt; node
 *
 * @author NetRom team
 */
public class OMChoice extends OMDefaultParent implements CharacterData, NodeWithChildConstraints {

    private String description = "";

    private String name;

    private String value;

    private boolean isFromClass;

    private static String[] childrenList = { "description" };

    public String[] getChildrenList() {
        return childrenList;
    }

    public void setAttribute(CharSequence name, CharSequence value) throws XMLTreeException {
        if (name.equals("name")) {
            this.name = value.toString();
        } else if (name.equals("value")) {
            this.value = value.toString();
        } else if (name.equals("class")) {
            this.name = value.toString();
            isFromClass = true;
        } else {
            throw new XMLTreeException(XMLTreeException.NOT_SUPPORTED_ERR, "attribute " + name + " value " + value + " not allowed");
        }
    }

    public boolean canAppendChild(CharSequence tag, Attributes attr) {
        return !tag.equals("description");
    }

    public void appendData(CharSequence arg) throws XMLTreeException {
        description += arg;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isFromClass() {
        return isFromClass;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return !description.equals("");
    }

    public void transform() {
        OMField field = (OMField) getParentNode();
        Node node = field.getParentNode();
        String tableName;
        while (!(node instanceof OMTable) && !(node instanceof OMRootTable)) node = node.getParentNode();
        if (node instanceof OMTable) {
            tableName = ((OMTable) node).getName();
        } else {
            tableName = ((OMRootTable) node).getName();
        }
        if (name == null) {
            gen.setFailed(true);
            SaveException.error("Choice in field " + tableName + "." + field.getName() + " - you have to specify choice class or choice name and choice value ");
            return;
        }
        if (!isFromClass && value == null) {
            gen.setFailed(true);
            SaveException.error("Choice in field " + tableName + "." + field.getName() + " - you have to specify a choice value");
            return;
        }
        if (field.isDate()) {
            gen.setFailed(true);
            SaveException.error("Choice in field " + tableName + "." + field.getName() + " is date so it cannot have choices");
            return;
        }
        if (field.isTimestamp()) {
            gen.setFailed(true);
            SaveException.error("Choice in field " + tableName + "." + field.getName() + " is timestamp so it cannot have choices");
            return;
        }
        if (field.isLink()) {
            gen.setFailed(true);
            SaveException.error("Choice in field " + tableName + "." + field.getName() + " is link so it cannot have choices");
            return;
        }
        if (!isFromClass) {
            if (field.isInt()) {
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    gen.setFailed(true);
                    SaveException.error("Choice in field " + tableName + "." + field.getName() + " - value for choice is not an integer (" + value + ")");
                    return;
                }
            }
            if (field.isBoolean()) {
                if ((!value.equals("true")) && (!value.equals("false"))) {
                    gen.setFailed(true);
                    SaveException.error("Choice in field " + tableName + "." + field.getName() + " - value for choice is not boolean (" + value + ")");
                    return;
                }
            }
            if (field.isDouble()) {
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    gen.setFailed(true);
                    SaveException.error("Choice in field " + tableName + "." + field.getName() + " - value for choice is not a double (" + value + ")");
                    return;
                }
            }
        }
    }
}
