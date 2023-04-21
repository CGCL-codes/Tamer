package com.idna.gav.rules.international.volt.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import com.idna.gav.rules.international.volt.CatThreeCountryRule;

public class ItalyRule extends CatThreeCountryRule {

    private String[] italyStreetPrepositions = { "AUDIO", "DALLE", "DELLO", "LE", "AUTO", "DE", "DI", "LI", "CAPITAL", "DEGLI", "FIN", "MAC", "CENTRO", "DEI", "GLI", "MC", "CINE", "DEL", "IDEL", "PER", "CON", "DELLA", "IL", "RE", "DA", "DELLE", "IN", "SU", "DALLA", "DELLI", "LA", "VAN", "VON", "SAN" };

    public void applyCountrySpecificRules(Document doc) {
        replaceSpecialChars(doc, surnameElementName);
        removeStreetType(doc);
        removePrepositions(doc);
        applyPremisePeriodRule(doc);
        this.insertDelimetersIntoLocalityWithOutRegion(doc);
    }

    private void removePrepositions(Document doc) {
        Node node = doc.selectSingleNode(streetElementName);
        if (node != null && node.hasContent()) {
            String value = node.getText();
            String tmp = "";
            if (StringUtils.isNotBlank(value)) {
                value = value.toUpperCase();
                String[] streetTokens = value.split(" ");
                for (String token : streetTokens) {
                    for (String term : italyStreetPrepositions) {
                        if (term.equalsIgnoreCase(token.trim())) {
                            token = "";
                        }
                    }
                    tmp = (tmp + " " + token).trim();
                }
                node.setText(tmp.trim());
            }
        }
    }
}
