package uk.ac.reload.straker.datamodel.learningdesign.types;

import uk.ac.reload.straker.datamodel.DataModel;
import uk.ac.reload.straker.datamodel.learningdesign.LD_ComponentRef;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;

/**
 * Item Ref Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemRef.java,v 1.4 2006/07/10 11:50:38 phillipus Exp $
 */
public class ItemRef extends LD_ComponentRef {

    public static String XML_ELEMENT_NAME = "item-ref";

    /**
     * Constructor
     * @param dataModel
     */
    public ItemRef(DataModel dataModel) {
        super(dataModel);
    }

    /**
     * Default constructor with ref
     * @param act The component referenced
     */
    public ItemRef(ItemType item) {
        super(item);
    }

    public LD_DataComponent getReferencedComponent() {
        return getLearningDesign().findGlobalItemType(getRef());
    }

    public String getXMLElementName() {
        return XML_ELEMENT_NAME;
    }
}
