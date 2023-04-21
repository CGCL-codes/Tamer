package com.idna.gav.rules.international.volt;

import org.dom4j.Document;
import org.dom4j.Element;
import com.idna.common.utils.XmlUtility;

/**
 * the rule to be applied:
 * =================================================================================================
 * | Forename | Surname  |  Door Number   |          Street          |   Locality | Region        |
 * =================================================================================================
 * |   None   |Required  |       None     | Premise &/or Street Name  |    None    | Required:      |
 * |          |          |                |                          |            | City or Region |
 * |          |          |                |                          |            |   or Province  |
 * ------------------------------------------------------------------------------------------------
 * Countries: BEL. 
 */
public abstract class CatFourCountryRule extends AbstractCountryRule {

    protected void removeRedundantData(Document doc) {
        movePostcode(doc);
        XmlUtility.removeElement(doc, forenameElementName);
        XmlUtility.removeElement(doc, subLocalityElementName);
        XmlUtility.removeElement(doc, postcodeElementName);
        removeAlphaCharactersFromPremiseRule(doc);
    }

    protected boolean allNodesPassedCheck(Document doc) {
        return (checkNodeSurnameElementName(doc) != null && checkNodePostcodeElementName(doc) != null && (checkNodePremiseElementName(doc) != null || checkNodeStreet(doc) != null));
    }

    private void movePostcode(Document request) {
        String postcode = request.valueOf(postcodeElementName);
        Element locality = (Element) request.selectSingleNode(localityElementName);
        if (locality == null) {
            Element address = (Element) request.selectSingleNode(addressElementName);
            locality = address.addElement("Locality");
        }
        locality.setText(postcode);
    }
}
