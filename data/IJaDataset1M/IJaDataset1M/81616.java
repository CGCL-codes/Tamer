package edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers.sequencingChilds;

import org.w3c.dom.Node;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.RollupConsiderations;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;

public class ParseRollupConsiderations {

    public RollupConsiderations rollupConsiderations = new RollupConsiderations();

    private boolean correct = false;

    public ParseRollupConsiderations(Node node) {
        correct = analizeRollupconsiderations(node);
    }

    private boolean analizeRollupconsiderations(Node rollupConsiderationsNode) {
        boolean correcte = true;
        String requiredForSatisfied = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "requiredForSatisfied");
        if (requiredForSatisfied.length() == 0) {
            if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) System.out.println("Atribut randomizationTiming no trobat.");
        } else {
            RollupConsiderations.rollupConsiderationsType searchType = searchRollupConsiderationsType(requiredForSatisfied);
            if (searchType != null) {
                rollupConsiderations.requiredForSatisfied = searchType;
            } else {
                correcte = false;
                if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) System.out.println("[ERROR Rollupconsiderations] requiredForSatisfied t�" + " un valor no v�lid.");
            }
        }
        String requiredForNotSatisfied = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "requiredForNotSatisfied");
        if (requiredForNotSatisfied.length() == 0) {
            if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) System.out.println("Atribut randomizationTiming no trobat.");
        } else {
            RollupConsiderations.rollupConsiderationsType searchType = searchRollupConsiderationsType(requiredForNotSatisfied);
            if (searchType != null) {
                rollupConsiderations.requiredForNotSatisfied = searchType;
            } else {
                correcte = false;
                if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) System.out.println("[ERROR Rollupconsiderations] requiredForNotSatisfied t�" + " un valor no v�lid.");
            }
        }
        String requiredForCompleted = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "requiredForCompleted");
        if (requiredForCompleted.length() == 0) {
            if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) System.out.println("Atribut randomizationTiming no trobat.");
        } else {
            RollupConsiderations.rollupConsiderationsType searchType = searchRollupConsiderationsType(requiredForCompleted);
            if (searchType != null) {
                rollupConsiderations.requiredForCompleted = searchType;
            } else {
                correcte = false;
                if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) System.out.println("[ERROR Rollupconsiderations] requiredForCompleted t�" + " un valor no v�lid.");
            }
        }
        String requiredForIncomplete = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "requiredForIncomplete");
        if (requiredForIncomplete.length() == 0) {
            if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) System.out.println("Atribut randomizationTiming no trobat.");
        } else {
            RollupConsiderations.rollupConsiderationsType searchType = searchRollupConsiderationsType(requiredForIncomplete);
            if (searchType != null) {
                rollupConsiderations.requiredForIncomplete = searchType;
            } else {
                correcte = false;
                if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) System.out.println("[ERROR Rollupconsiderations] requiredForIncomplete t�" + " un valor no v�lid.");
            }
        }
        String measureSatisfactionIfActive = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "measureSatisfactionIfActive");
        if (measureSatisfactionIfActive.length() == 0) {
            if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) System.out.println("Atribut randomizationTiming no trobat.");
        } else {
            rollupConsiderations.measureSatisfactionIfActive = measureSatisfactionIfActive.equals(Constants.FALSE) ? false : true;
        }
        return correcte;
    }

    private RollupConsiderations.rollupConsiderationsType searchRollupConsiderationsType(String searchedType) {
        RollupConsiderations.rollupConsiderationsType sortida = null;
        RollupConsiderations.rollupConsiderationsType[] rollupConsiderationsArray = RollupConsiderations.rollupConsiderationsType.values();
        for (int i = 0; i < rollupConsiderationsArray.length; i++) {
            if (rollupConsiderationsArray[i].toString().equals(searchedType)) {
                sortida = rollupConsiderationsArray[i];
                continue;
            }
        }
        return sortida;
    }

    public boolean getIsAllCorrect() {
        return correct;
    }
}
