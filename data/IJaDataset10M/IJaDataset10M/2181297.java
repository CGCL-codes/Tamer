package uk.ac.reload.straker.datamodel.learningdesign.components.environments;

import java.util.Iterator;
import org.jdom.Element;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.DataModel;
import uk.ac.reload.straker.datamodel.learningdesign.LD_CheckList;
import uk.ac.reload.straker.datamodel.learningdesign.LD_CheckListItem;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;

/**
 * Environments
 * 
 * @author Phillip Beauvoir
 * @version $Id: Environments.java,v 1.9 2006/07/10 11:50:37 phillipus Exp $
 */
public class Environments extends LD_DataComponent {

    public static String XML_ELEMENT_NAME = "environments";

    public static String IMAGE_NAME = ICON_ENVIRONMENTS;

    /**
     * Constructor
     * @param dataModel The DataModel
     */
    public Environments(DataModel dataModel) {
        super(dataModel);
    }

    /**
     * This will add a default Environment.
     */
    public void setDefaults() {
        if (getChildren().length == 0) {
            Environment env = new Environment(getDataModel(), "Environment");
            addChild(env);
        }
    }

    /**
     * Get a Component given its identifier or null if not found.
     * This will search Environments and their children
     * @param idref
     * @return The found Component or null if not found.
     */
    public DataComponent getComponentByIdentifier(String idref) {
        if (idref == null) {
            return null;
        }
        return getChildByIdentifer(idref, true);
    }

    /**
     * @return All Environment children as an array of Environment types
     */
    public Environment[] getEnvironmentsToArray() {
        return (Environment[]) _children.toArray(new Environment[_children.size()]);
    }

    public boolean canDelete() {
        return false;
    }

    public String getImageName() {
        return IMAGE_NAME;
    }

    /**
     * @return A suitable Title message for a UI
     */
    public String getTitleMessage() {
        return getXMLElementFriendlyName();
    }

    public String getDescriptionMessage() {
        return "Add Environments";
    }

    public String toString() {
        return getTitleMessage();
    }

    public boolean canRename() {
        return false;
    }

    public void addCheckListItems(LD_CheckList checkList, String category) {
        int listCount = checkList.getCheckListItemCount();
        DataComponent[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            ((LD_DataComponent) children[i]).addCheckListItems(checkList, category);
        }
        if (checkList.getCheckListItemCount() == listCount) {
            LD_CheckListItem item = new LD_CheckListItem(category + ": checks out OK", false);
            checkList.addCheckListItem(item);
        }
    }

    /**
     * Destroy this Component
     */
    public void dispose() {
        super.dispose();
    }

    public Element marshall2XML(Element parentElement) {
        if (!canMarshall2XML()) {
            return null;
        }
        Element element = super.marshall2XML(parentElement);
        DataComponent[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            children[i].marshall2XML(element);
        }
        return element;
    }

    public boolean canMarshall2XML() {
        return getChildren().length > 0;
    }

    public void unmarshallXML(Element element) {
        super.unmarshallXML(element);
        Iterator i = element.getChildren("environment").iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            Environment env = new Environment(getDataModel());
            addChild(env);
            env.unmarshallXML(e);
        }
    }

    public String getXMLElementName() {
        return XML_ELEMENT_NAME;
    }
}
