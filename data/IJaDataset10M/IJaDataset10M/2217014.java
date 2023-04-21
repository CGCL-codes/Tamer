package com.idna.gav.rules.international.volt.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import com.idna.gav.rules.international.volt.CatThreeCountryRule;

public class IrelandRule extends CatThreeCountryRule {

    public void applyCountrySpecificRules(Document doc) {
        removeCharsFromSurname(doc);
        applySurnamePeriodRule(doc);
        changeSurnameMcToMac(doc);
        removeNonSignificantZerosFromPremise(doc);
        removeStreetType(doc);
        specificDublinRule(doc, localityElementName);
        specificDublinRule(doc, postTownElementName);
    }

    private void specificDublinRule(Document doc, String path) {
        Node node = doc.selectSingleNode(path);
        String searchStr = "DUBLIN";
        if (node != null && node.hasContent()) {
            String nodeStr = node.getText().toUpperCase();
            if (nodeStr.equalsIgnoreCase(searchStr) || StringUtils.contains(nodeStr, searchStr)) {
                node.setText("Dublin. 01.");
            }
        }
    }

    private void changeSurnameMcToMac(Document doc) {
        Node surnameNode = doc.selectSingleNode(surnameElementName);
        String surname = surnameNode.getText();
        surname = surname.toUpperCase();
        StringBuffer sB = new StringBuffer();
        String[] words = surname.split(" ");
        for (String word : words) {
            if (word.startsWith("MC")) {
                word = word.replaceFirst("MC", "MAC");
            }
            sB.append(word + " ");
        }
        surnameNode.setText(sB.toString().trim());
    }
}
