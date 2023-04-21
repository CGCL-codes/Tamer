package uk.ac.reload.straker.datamodel.learningdesign.expressions;

import uk.ac.reload.straker.datamodel.DataModel;

/**
 * Subtract type
 * 
 * @author Phillip Beauvoir
 * @version $Id: SubtractType.java,v 1.4 2006/07/10 11:50:40 phillipus Exp $
 */
public class SubtractType extends CalculateType {

    public static String XML_ELEMENT_NAME = "subtract";

    /**
     * Constructor
     * @param dataModel
     */
    public SubtractType(DataModel dataModel) {
        super(dataModel);
    }

    public String getXMLElementName() {
        return XML_ELEMENT_NAME;
    }
}
