package uk.ac.reload.straker.datamodel.learningdesign.method;

import java.util.Iterator;
import org.jdom.Element;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.DataModel;
import uk.ac.reload.straker.datamodel.learningdesign.LD_CheckList;
import uk.ac.reload.straker.datamodel.learningdesign.LD_CheckListItem;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.types.IItemContainer;
import uk.ac.reload.straker.datamodel.learningdesign.types.ItemContainer;
import uk.ac.reload.straker.datamodel.learningdesign.types.OnCompletionType;

/**
 * Act
 * 
 * @author Phillip Beauvoir
 * @version $Id: Act.java,v 1.9 2006/07/10 11:50:45 phillipus Exp $
 */
public class Act extends LD_DataComponent implements IItemContainer {

    public static String XML_ELEMENT_NAME = "act";

    public static String IMAGE_NAME = ICON_ACT;

    /**
     * Complete Act Type
     */
    private CompleteActType _completeActType;

    /**
     * On Completion Type
     */
    private OnCompletionType _onCompletionType;

    /**
     * Constructor
     * @param dataModel The DataModel
     */
    public Act(DataModel dataModel) {
        this(dataModel, null);
    }

    /**
     * Constructor
     * @param dataModel The DataModel
     * @param title The Title
     */
    public Act(DataModel dataModel, String title) {
        super(dataModel, title);
    }

    /**
     * This will add a default Role Part.
     */
    public void setDefaults() {
        if (getChildren().length == 0) {
            RolePart rolePart = new RolePart(getDataModel(), "Role Part");
            rolePart.setDefaults();
            addChild(rolePart);
        }
    }

    /**
     * Over-ride to ensure we have one
     * @return The id of the component
     */
    public String getIdentifier() {
        if (super.getIdentifier() == null) {
            setIdentifier(generateID("act-"));
        }
        return super.getIdentifier();
    }

    public String getImageName() {
        return IMAGE_NAME;
    }

    /**
     * Find a RolePart by its Ref
     * @param rolepartRef
     * @return The RolePart or null if not found
     */
    protected RolePart findRolePart(String rolepartRef) {
        RolePart rolePart = null;
        if (rolepartRef != null) {
            rolePart = (RolePart) getChildByIdentifer(rolepartRef, false);
        }
        return rolePart;
    }

    /**
     * @return The CompleteActType
     */
    public CompleteActType getCompleteActType() {
        if (_completeActType == null) {
            _completeActType = new CompleteActType(this);
        }
        return _completeActType;
    }

    /**
     * @return The OnCompletionType
     */
    public OnCompletionType getOnCompletionType() {
        if (_onCompletionType == null) {
            _onCompletionType = new OnCompletionType(this);
        }
        return _onCompletionType;
    }

    public void addCheckListItems(LD_CheckList checkList, String category) {
        category += ": " + getTitle();
        if (_completeActType != null) {
            _completeActType.addCheckListItems(checkList, category);
        }
        if (_onCompletionType != null) {
            _onCompletionType.addCheckListItems(checkList, category);
        }
        DataComponent[] children = getChildren();
        if (children.length == 0) {
            LD_CheckListItem item = new LD_CheckListItem(category + ": must have at least one Role Part", true);
            checkList.addCheckListItem(item);
        }
        for (int i = 0; i < children.length; i++) {
            ((LD_DataComponent) children[i]).addCheckListItems(checkList, category);
        }
        if (_metadata != null) {
            _metadata.addCheckListItems(checkList, category);
        }
    }

    public ItemContainer[] getItemContainers() {
        return new ItemContainer[] { getOnCompletionType().getFeedbackDescriptionType().getItemContainer() };
    }

    /**
     * Destroy this Component
     */
    public void dispose() {
        super.dispose();
        if (_completeActType != null) {
            _completeActType.dispose();
            _completeActType = null;
        }
        if (_onCompletionType != null) {
            _onCompletionType.dispose();
            _onCompletionType = null;
        }
    }

    public Element marshall2XML(Element parentElement) {
        Element elementAct = super.marshall2XML(parentElement);
        if (getTitle() != null && getTitle().length() > 0) {
            Element title = new Element("title");
            title.setText(getTitle());
            elementAct.addContent(title);
        }
        DataComponent[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            children[i].marshall2XML(elementAct);
        }
        if (_completeActType != null) {
            _completeActType.marshall2XML(elementAct);
        }
        if (_onCompletionType != null) {
            _onCompletionType.marshall2XML(elementAct);
        }
        if (_metadata != null) {
            _metadata.marshall2XML(elementAct);
        }
        return elementAct;
    }

    public void unmarshallXML(Element element) {
        super.unmarshallXML(element);
        Element title = element.getChild("title");
        if (title != null) {
            setTitle(title.getText());
        }
        Iterator i = element.getChildren("role-part").iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            RolePart rolepart = new RolePart(getDataModel());
            addChild(rolepart);
            rolepart.unmarshallXML(e);
        }
        Element e = element.getChild("complete-act");
        if (e != null) {
            getCompleteActType().unmarshallXML(e);
        }
        e = element.getChild("on-completion");
        if (e != null) {
            getOnCompletionType().unmarshallXML(e);
        }
        Element md = element.getChild("metadata");
        if (md != null) {
            getMetadata().unmarshallXML(md);
        }
    }

    public String getXMLElementName() {
        return XML_ELEMENT_NAME;
    }
}
