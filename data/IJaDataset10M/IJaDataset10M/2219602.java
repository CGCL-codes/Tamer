package uk.ac.reload.straker.datamodel.learningdesign.expressions;

import uk.ac.reload.straker.datamodel.DataModel;

/**
 * Or type
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrType.java,v 1.4 2006/07/10 11:50:42 phillipus Exp $
 */
public class OrType extends OperatorType {

    public static String XML_ELEMENT_NAME = "or";

    /**
     * Constructor
     * @param dataModel
     */
    public OrType(DataModel dataModel) {
        super(dataModel);
    }

    public String getXMLElementName() {
        return XML_ELEMENT_NAME;
    }
}
