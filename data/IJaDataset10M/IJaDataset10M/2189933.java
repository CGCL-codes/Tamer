package uk.ac.reload.straker.datamodel.learningdesign.components.activities;

import uk.ac.reload.straker.datamodel.DataModel;

/**
 * ActivityStructure Ref class
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivityStructureRef.java,v 1.5 2006/07/10 11:50:36 phillipus Exp $
 */
public class ActivityStructureRef extends ActivityRef {

    public static String XML_ELEMENT_NAME = "activity-structure-ref";

    public static String IMAGE_NAME = ICON_ACTIVITYSTRUCTURE_REF;

    /**
     * Constructor
     * @param dataModel The DataModel
     */
    public ActivityStructureRef(DataModel dataModel) {
        super(dataModel);
    }

    /**
     * Default constructor with ref
     * @param as The component referenced
     */
    public ActivityStructureRef(ActivityStructure as) {
        super(as);
    }

    public String getImageName() {
        return IMAGE_NAME;
    }

    public String getXMLElementName() {
        return XML_ELEMENT_NAME;
    }
}
