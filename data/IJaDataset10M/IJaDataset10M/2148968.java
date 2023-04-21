package de.tum.in.botl.heuristics.implementation;

import java.util.*;
import de.tum.in.botl.metamodel.implementation.*;

/**
 * <p>�berschrift: Systementwicklungsprojekt</p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Organisation: </p>
 * @author Georgi Todorov
 * @version 1.0
 */
public class Applicability {

    /**
   * dependsOn yields to the set of source object variables of a given rule of which
   * the identity of a target object variable is one-to-one dependent.
   * @param ov - An object Variable
   * @param r - A rule
   * @return
   */
    public static Object dependsOn(ObjectVariable ov, Rule r) {
        if (ov.getOiv().getValue().startsWith("$")) {
            Vector ovLeftHandSide = r.getSourceModelVariable().getObjectVariables();
            return searchDependObjectVariable(ov, ovLeftHandSide);
        } else if (ov.getOiv().getValue().equals("#")) {
            return new Term("#");
        } else {
            return new ArrayList();
        }
    }

    /**
   *
   * @param ov
   * @param ovLeftHandSide all object variables on the left side of the rule
   * @return
   */
    private static ArrayList searchDependObjectVariable(ObjectVariable ov, Vector ovLeftHandSide) {
        ArrayList dependency = new ArrayList();
        int size = ovLeftHandSide.size();
        ObjectVariable ovHelp = null;
        for (Iterator it = ovLeftHandSide.iterator(); it.hasNext(); ) {
            ovHelp = (ObjectVariable) it.next();
            if (ovHelp.getOiv() == ov.getOiv()) {
                dependency.add(ovHelp);
            }
        }
        return dependency;
    }

    /**
   * attDependsOn yields to the set of source object variables of a given rule of which
   * an attribute value of a target object variable depends.
   * @param ov - An object variable
   * @param att - An attribute
   * @param r - A rule
   * @return
   */
    public static ArrayList attDependsOn(ObjectVariable ov, Attribute att, Rule r) {
        ArrayList result = null;
        AttributeVariable av = getAttributeVariableForAttribute(att, ov);
        if (!(av.getT().getValue().startsWith("$")) || (av.getT().getValue().equals("#"))) {
            result = new ArrayList();
        } else if (av.getT().getValue().startsWith("$")) {
            result = new ArrayList();
            Vector ovLeftHandSide = r.getSourceModelVariable().getObjectVariables();
            Vector attributeVariables = null;
            ObjectVariable helpOV = null;
            for (Iterator it = ovLeftHandSide.iterator(); it.hasNext(); ) {
                helpOV = (ObjectVariable) it.next();
                attributeVariables = helpOV.getAttributeVariables();
                int sizeOfAV = attributeVariables.size();
                for (int i = 0; i < sizeOfAV; i++) {
                    if (av.getT() == ((AttributeVariable) attributeVariables.elementAt(i)).getT()) {
                        result.add(helpOV);
                        i = sizeOfAV;
                    }
                }
            }
        } else {
        }
        return result;
    }

    /**
   * Returns an attribute variable, which is in the specified object variable contained and
   * has a pointer to the specified attribute
   * @param att - An attribute
   * @param ov - An object variable
   * @return
   */
    private static AttributeVariable getAttributeVariableForAttribute(Attribute att, ObjectVariable ov) {
        AttributeVariable result = null;
        Vector attributeVariables = ov.getAttributeVariables();
        int size = attributeVariables.size();
        int counter = 0;
        AttributeVariable av = null;
        while ((counter < size)) {
            av = (AttributeVariable) attributeVariables.elementAt(counter);
            if (att == av.getAttribute()) {
                result = av;
                counter = size;
            } else {
                counter++;
            }
        }
        return result;
    }

    /**
   *
   * @param mv - A model variable
   * @param ov1 - A set of object variables
   * @param ov2 - A set of object variables
   * @return
   */
    public static boolean determines(ModelVariable mv, ArrayList ovList1, ArrayList ovList2) {
        boolean result = true;
        Vector ovas = mv.getObjectVariableAssociations();
        Vector ovaes = new Vector();
        ObjectVariableAssociation ova = null;
        for (Iterator it = ovas.iterator(); it.hasNext(); ) {
            ova = (ObjectVariableAssociation) it.next();
            ovaes.add(ova.getFirstEnd());
            ovaes.add(ova.getSecondEnd());
        }
        ObjectVariable bufferOV = null;
        int sizeOV2 = ovList2.size();
        for (int i = 0; i < sizeOV2; i++) {
            bufferOV = (ObjectVariable) ovList2.get(i);
            if (!ovList1.contains(bufferOV)) {
                if (!determines(ovList1, bufferOV)) return false;
                setOVAEsofNotVisited(ovaes);
            }
        }
        return result;
    }

    /**
   * Sets all the object variable association ends of the vector ovaes to "not visited"
   * @param ovaes
   */
    private static void setOVAEsofNotVisited(Vector ovaes) {
        for (Iterator it = ovaes.iterator(); it.hasNext(); ) {
            ((ObjectVariableAssociationEnd) it.next()).setVisited(false);
        }
    }

    /**
   * Returns true, if an object variable from the list ovList the specified object variable ov determines,
   * otherwise false
   * @param ovList
   * @param ov
   * @return
   */
    private static boolean determines(ArrayList ovList, ObjectVariable ov) {
        boolean result = false;
        Vector ovaes = ov.getObjectVariableAssociationEnds();
        ObjectVariableAssociationEnd ovae = null;
        ObjectVariableAssociationEnd ovae2 = null;
        ObjectVariable bufferOV = null;
        Multiplicity mul = null;
        int sizeOVAEs = ovaes.size();
        for (int j = 0; j < sizeOVAEs; j++) {
            ovae = (ObjectVariableAssociationEnd) ovaes.elementAt(j);
            mul = ovae.getClassAssociationEnd().getMultiplicity();
            if ((mul.getLeftValue() == 1 && mul.getRightValue() == 1) || (mul.getLeftValue() == 0 && mul.getRightValue() == 1)) {
                if (!ovae.isVisited()) {
                    if (ovae == ovae.getObjectVariableAssociation().getFirstEnd()) ovae2 = ovae.getObjectVariableAssociation().getSecondEnd(); else ovae2 = ovae.getObjectVariableAssociation().getFirstEnd();
                    bufferOV = ovae2.getObjectVariable();
                    ovae.setVisited(true);
                    if (ovList.contains(bufferOV)) {
                        result = true;
                    } else {
                        result = determines(ovList, bufferOV);
                    }
                }
            } else {
            }
            if (result) return result;
        }
        return result;
    }

    /**
   * Theorem 4.1.2 (Applicability of a Rule)
   * @param r
   * @return
   */
    public static boolean isRuleApplicable(Rule r) {
        if (!isCriteriaTwoApplicabilityOfARule(r) || !isCriteriaThreeApplicabilityOfARule(r)) return false;
        return true;
    }

    private static boolean isCriteriaTwoApplicabilityOfARule(Rule r) {
        Vector ovRightSide1 = r.getDestinationModelVariable().getObjectVariables();
        int vectorSize = ovRightSide1.size();
        ObjectVariable ov = null;
        Object dependencies = null;
        for (int i = 0; i < vectorSize; i++) {
            ov = (ObjectVariable) ovRightSide1.elementAt(i);
            dependencies = dependsOn(ov, r);
            if (!helpFunctionForCriteriaTwo(ov, r) && !isOVDependencyDiamond(ov, r) && !areAllAVsDiamonds(ov)) return false;
        }
        return true;
    }

    private static boolean helpFunctionForCriteriaTwo(ObjectVariable ov, Rule r) {
        boolean result = true;
        if (!isOVDependencyVariable(ov, r) || !determinesAllAttributeVariables(ov, r)) return false;
        return result;
    }

    private static boolean determinesAllAttributeVariables(ObjectVariable ov, Rule r) {
        boolean result = true;
        Vector attributeVariables = ov.getAttributeVariables();
        int sizeAV = attributeVariables.size();
        for (int i = 0; i < sizeAV; i++) {
            if (!determines(r.getSourceModelVariable(), (ArrayList) dependsOn(ov, r), attDependsOn(ov, ((AttributeVariable) attributeVariables.elementAt(i)).getAttribute(), r))) return false;
        }
        return result;
    }

    private static boolean isCriteriaThreeApplicabilityOfARule(Rule r) {
        if (!checkRightSide(r)) return false;
        return true;
    }

    /**
   * Es wird gepr�ft, ob es auf der rechten Seite des Regels zwei OVs gibt, die von denselben Typ sind(z.B. Person).
   * Wenn das der Fall ist, der Regel ist nicht g�ltig und wird false zur�ckgegeben.
   * (iii) Theorem 4.1.2 (Applicability of a Rule)
   * @param r
   * @return
   */
    private static boolean checkRightSide(Rule r) {
        Vector ovRightSide = r.getDestinationModelVariable().getObjectVariables();
        int size = ovRightSide.size();
        ObjectVariable ov1 = null;
        ObjectVariable ov2 = null;
        for (int i = 0; i < size - 1; i++) {
            ov1 = (ObjectVariable) ovRightSide.elementAt(i);
            for (int j = i + 1; j < size; j++) {
                ov2 = (ObjectVariable) ovRightSide.elementAt(j);
                if (ov1.getTheClass() == ov2.getTheClass()) return false;
            }
        }
        return true;
    }

    /**
   * dependsOn(ov,r) = Diamond
   * @param ov
   * @param r
   * @return
   */
    private static boolean isOVDependencyDiamond(ObjectVariable ov, Rule r) {
        Object depends = dependsOn(ov, r);
        if (depends instanceof Term && !((Term) depends).getValue().equals("#")) return false;
        return true;
    }

    /**
   * dependsOn(ov,r) nicht in {null,Diamond,die leere Menge}
   * @param ov
   * @param r
   * @return
   */
    private static boolean isOVDependencyVariable(ObjectVariable ov, Rule r) {
        boolean result = true;
        ArrayList depends = (ArrayList) dependsOn(ov, r);
        if (depends == null) {
            result = false;
        } else if (depends.size() == 0) {
            result = false;
        } else if (depends.size() == 1 && depends.get(0) instanceof Term && ((Term) depends.get(0)).getValue().equals("#")) {
            result = false;
        }
        return result;
    }

    /**
   * Returns true, if all attribute variables of an object variable are diamonds
   * @param ov
   * @return
   */
    private static boolean areAllAVsDiamonds(ObjectVariable ov) {
        Vector attributevariables = ov.getAttributeVariables();
        int numberOfAVs = attributevariables.size();
        AttributeVariable av = null;
        for (int i = 0; i < numberOfAVs; i++) {
            av = (AttributeVariable) attributevariables.elementAt(i);
            if (!((Term) av.getT()).getValue().equals("#")) return false;
        }
        return true;
    }

    /**
   * Returns true if all key attributes of the specified object variable are constant, otherwise returns false
   * @param ov
   * @return
   */
    private static boolean areAllKeyAttributesConstant(ObjectVariable ov) {
        boolean result = true;
        Vector keyAttributes = ov.getTheClass().getPrimaryKeys();
        Attribute att = null;
        for (Iterator it = keyAttributes.iterator(); it.hasNext(); ) {
            att = (Attribute) it.next();
            if (containsAttribute(att, ov)) {
            }
        }
        return result;
    }

    /**
   * Returns an attributevariable, which is in the specified object variable contained and has a pointer to the specified attribute
   * @param att
   * @param ov
   * @return
   */
    private static boolean containsAttribute(Attribute att, ObjectVariable ov) {
        boolean result = false;
        Vector attributeVariables = ov.getAttributeVariables();
        int size = attributeVariables.size();
        int counter = 0;
        while ((result == false) && (counter < size)) {
            if (att == ((AttributeVariable) attributeVariables.elementAt(counter)).getAttribute()) {
                result = true;
                counter = size;
            } else {
                counter++;
            }
        }
        return result;
    }
}
