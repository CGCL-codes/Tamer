package org.commerce.mismo.xml;

import org.commerce.mismo.Identification;
import org.w3c.dom.Element;

/**
 * Creates the XML code for the borrower's identification
 *  
 * @version $Id: IdentificationXMLGenerator.java,v 1.1.1.1 2007/04/16 05:07:04 clafonta Exp $
 */
public class IdentificationXMLGenerator extends XMLGeneratorSupport {

    /**
     * The name of the identification XML element
     */
    public static final String IDENTIFICATION = "IDENTIFICATION";

    /**
     * The attribute name for the identification type
     */
    public static final String TYPE = "_Type";

    /**
     * The attribute name for the issue date for the identification
     */
    public static final String ISSUE_DATE = "_IssueDate";

    /**
     * The attribute name for the identification's expiration date
     */
    public static final String EXPIRATION_DATE = "_ExpirationDate";

    /**
     * The attribute name for the country that issued the identification
     */
    public static final String ISSUING_COUNTRY = "_IssuingCountry";

    /**
     * The attribute name for the state that issued the identification
     * (if the state issued it)
     */
    public static final String ISSUING_STATE = "_IssuingState";

    /**
     * Creates a new IDENTIFICATION element.
     * <p>
     * NOTE: this is based on a custome-generated XML element
     * because Mismo 2.3 doens't have this yet.
     * 
     * @param  context the environment in which the IDENTIFICATION element
     *         should be created
     * @param  mailingAddress the <code>Address</code> containing the data that
     *         should be contained in the resulting _MAIL_TO element
     * @return a new, populated _MAIL_TO element which includes the country
     */
    public Element getElement(XMLGenerationContext context, Identification identification) {
        Element node = context.createElement(IDENTIFICATION);
        setAttribute(node, EXPIRATION_DATE, identification.getExpirationDate());
        setAttribute(node, ISSUE_DATE, identification.getIssueDate());
        setAttribute(node, ISSUING_COUNTRY, identification.getIssuingCountry());
        setAttribute(node, ISSUING_STATE, identification.getIssuingState());
        setAttribute(node, TYPE, identification.getType().getCode());
        return node;
    }
}
