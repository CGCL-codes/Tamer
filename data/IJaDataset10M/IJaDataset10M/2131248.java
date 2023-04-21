package de.haw.mussDasSein.serialization;

import java.util.HashMap;
import java.util.Map;
import de.haw.mussDasSein.model.Komponente;
import de.haw.mussDasSein.rule.LUT;
import de.haw.mussDasSein.rule.SelectableCondition;

public class RuleMotor {

    public static void main(String[] args) {
        LUT ruleSet = new LUT();
        try {
            SelectableCondition A1 = new SelectableCondition("1.8 ECOTEC [103 KW]");
            SelectableCondition B1 = new SelectableCondition("manuelles 5-Gang-Getriebe (MT5)");
            SelectableCondition B2 = new SelectableCondition("Easytronic (MTA5)");
            A1.setRuleSet(ruleSet);
            ruleSet.addNurMitCondition(A1.getIdentifier(), B1.or(B2));
            SelectableCondition A2 = new SelectableCondition("1.6 CNG ecoFlex Turbo [110 KW]");
            SelectableCondition B3 = new SelectableCondition("manuelles 6-Gang-Getriebe (MT6)");
            A2.setRuleSet(ruleSet);
            ruleSet.addNurMitCondition(A2.getIdentifier(), B3);
            SelectableCondition A3 = new SelectableCondition("1.7 CDTI ecoFlex [92 KW]");
            SelectableCondition B4 = new SelectableCondition("manuelles 6-Gang-Getriebe (MT6)(Diesel)");
            A3.setRuleSet(ruleSet);
            ruleSet.addNurMitCondition(A3.getIdentifier(), B4);
            XMLParser.parseObjectToFile(ruleSet, "XMLData/Rules/MotorRules.xml");
            ruleSet = null;
            ruleSet = XMLParser.parseXMLFile(LUT.class, "XMLData/Rules/MotorRules.xml");
            System.out.println(ruleSet.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
