package com.idna.gav.rules.international.volt.impl;

import org.dom4j.Document;
import com.idna.common.utils.XmlUtility;
import com.idna.gav.rules.international.volt.CatOneCountryRule;

public class SlovakiaRule extends CatOneCountryRule {

    public void applyCountrySpecificRules(Document doc) {
        replaceSpecialChars(doc, surnameElementName);
        replaceSpecialChars(doc, streetElementName);
        removeStreetType(doc);
        XmlUtility.removeElement(doc, postTownElementName);
    }
}
