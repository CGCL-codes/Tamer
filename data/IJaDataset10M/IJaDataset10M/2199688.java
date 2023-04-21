package eu.fbk.hlt.edits.distance.cost.scheme;

import java.io.File;
import java.util.Map;
import eu.fbk.hlt.common.EDITSException;
import eu.fbk.hlt.common.SerializationManager;
import eu.fbk.hlt.edits.EDITS;
import eu.fbk.hlt.edits.distance.algorithms.EditDistanceAlgorithm;
import eu.fbk.hlt.edits.distance.cost.scheme.data.XMLCostScheme;
import eu.fbk.hlt.edits.distance.cost.scheme.data.XMLCostSchemeConstant;
import eu.fbk.hlt.edits.distance.cost.scheme.data.XMLCostSchemeConstantType;
import eu.fbk.hlt.edits.distance.cost.scheme.data.XMLCostSchemeOperation;
import eu.fbk.hlt.edits.rules.RulesRepository;

/**
 * @author Milen Kouylekov
 */
public class XmlCostSchemeGenerator {

    private static boolean dynamic = false;

    public static void addDeletion(XMLCostScheme scheme, boolean optimizable) {
        if (optimizable) {
            XMLCostSchemeConstant c = new XMLCostSchemeConstant();
            c.setName("OPDeletion");
            c.setValue("0.5");
            c.setType(XMLCostSchemeConstantType.NUMBER);
            scheme.getConstant().add(c);
        }
        XMLCostSchemeOperation o = new XMLCostSchemeOperation();
        if (dynamic) o.setCost("(* " + (optimizable ? "OPDeletion" : "0.5") + " (size (words H)))"); else o.setCost(optimizable ? "OPDeletion" : "0.5");
        scheme.getDeletion().add(o);
    }

    public static void addInserion(XMLCostScheme scheme, boolean optimizable) {
        if (optimizable) {
            XMLCostSchemeConstant c = new XMLCostSchemeConstant();
            c.setName("OPInsertion");
            c.setValue("0.5");
            c.setType(XMLCostSchemeConstantType.NUMBER);
            scheme.getConstant().add(c);
        }
        XMLCostSchemeOperation o = new XMLCostSchemeOperation();
        if (dynamic) o.setCost("(* " + (optimizable ? "OPInsertion" : "0.5") + " (size (words T)))"); else o.setCost(optimizable ? "OPInsertion" : "0.5");
        scheme.getInsertion().add(o);
    }

    public static void addNotSubstitution(XMLCostScheme scheme, boolean optimizable, boolean distance, boolean normalized, boolean words) {
        if (optimizable) {
            XMLCostSchemeConstant c = new XMLCostSchemeConstant();
            c.setName("OPDifferent");
            c.setValue("1");
            c.setType(XMLCostSchemeConstantType.NUMBER);
            scheme.getConstant().add(c);
        }
        XMLCostSchemeOperation o = new XMLCostSchemeOperation();
        if (dynamic) {
            if (distance) {
                if (optimizable) o.setCost("(* (+ (size (words T)) (size (words H))) (*  OPDifferent (distance (a.token A) (a.token B)" + (normalized ? " :normalize" : "") + ")))"); else o.setCost("(* (+ (size (words T)) (size (words H))) (distance (a.token A) (a.token B)" + (normalized ? " :normalize" : "") + "))");
            } else {
                if (optimizable) o.setCost("(* (+ (size (words T)) (size (words H))) OPDifferent)"); else o.setCost("(+ (size (words T)) (size (words H)))");
            }
        } else {
            if (distance) {
                if (optimizable) o.setCost("(*  OPDifferent (distance (a.token A) (a.token B)" + (normalized ? " :normalize" : "") + "))"); else o.setCost("(distance (a.token A) (a.token B)" + (normalized ? " :normalize" : "") + ")");
            } else {
                if (optimizable) o.setCost("OPDifferent"); else o.setCost("1");
            }
        }
        scheme.getSubstitution().add(o);
    }

    public static void addResources(Map<String, RulesRepository> engines, XMLCostScheme scheme, boolean objectsAreWords, boolean optimizable, boolean edges) {
        for (String name : engines.keySet()) {
            RulesRepository xx = engines.get(name);
            XMLCostSchemeConstant c = new XMLCostSchemeConstant();
            c.setName("OPP" + name);
            c.setValue("0.0");
            c.setType(XMLCostSchemeConstantType.NUMBER);
            scheme.getConstant().add(c);
            if (optimizable) {
                c = new XMLCostSchemeConstant();
                c.setName("OPV" + name);
                c.setValue("0");
                c.setType(XMLCostSchemeConstantType.NUMBER);
                scheme.getConstant().add(c);
            }
            if (xx.containsWordRules()) {
                String[] aa = XmlCostSchemeGenerator.addResourceWords(name, "token", objectsAreWords, optimizable, edges);
                XMLCostSchemeOperation o = new XMLCostSchemeOperation();
                if (edges) {
                    o.getCondition().add("(set FA (word (from A)))");
                    o.getCondition().add("(set TA (word (to A)))");
                    o.getCondition().add("(set FB (word (from B)))");
                    o.getCondition().add("(set TB (word (to B)))");
                } else if (!objectsAreWords) o.getCondition().add("(and (is-word-node A) (is-word-node B))");
                for (int i = 0; i < aa.length - 1; i++) o.getCondition().add(aa[i]);
                o.setCost(aa[aa.length - 1]);
                scheme.getSubstitution().add(o);
                aa = XmlCostSchemeGenerator.addResourceWords(name, "lemma", objectsAreWords, optimizable, edges);
                o = new XMLCostSchemeOperation();
                if (edges) {
                    o.getCondition().add("(set FA (word (from A)))");
                    o.getCondition().add("(set TA (word (to A)))");
                    o.getCondition().add("(set FB (word (from B)))");
                    o.getCondition().add("(set TB (word (to B)))");
                } else if (!objectsAreWords) o.getCondition().add("(and (is-word-node A) (is-word-node B))");
                for (int i = 0; i < aa.length - 1; i++) o.getCondition().add(aa[i]);
                o.setCost(aa[aa.length - 1]);
                scheme.getSubstitution().add(o);
            }
            if (xx.containsStringRules()) {
                String[] aa = XmlCostSchemeGenerator.addResourceString(name, "token", objectsAreWords, optimizable, edges);
                XMLCostSchemeOperation o = new XMLCostSchemeOperation();
                if (edges) {
                    o.getCondition().add("(set FA (word (from A)))");
                    o.getCondition().add("(set TA (word (to A)))");
                    o.getCondition().add("(set FB (word (from B)))");
                    o.getCondition().add("(set TB (word (to B)))");
                } else if (!objectsAreWords) o.getCondition().add("(and (is-word-node A) (is-word-node B))");
                for (int i = 0; i < aa.length - 1; i++) o.getCondition().add(aa[i]);
                o.setCost(aa[aa.length - 1]);
                scheme.getSubstitution().add(o);
                aa = XmlCostSchemeGenerator.addResourceString(name, "lemma", objectsAreWords, optimizable, edges);
                o = new XMLCostSchemeOperation();
                if (edges) {
                    o.getCondition().add("(set FA (word (from A)))");
                    o.getCondition().add("(set TA (word (to A)))");
                    o.getCondition().add("(set FB (word (from B)))");
                    o.getCondition().add("(set TB (word (to B)))");
                } else if (!objectsAreWords) o.getCondition().add("(and (is-word-node A) (is-word-node B))");
                for (int i = 0; i < aa.length - 1; i++) o.getCondition().add(aa[i]);
                o.setCost(aa[aa.length - 1]);
                scheme.getSubstitution().add(o);
            }
        }
    }

    public static String[] addResourceString(String name, String attribute, boolean objectsAreWords, boolean optimizable, boolean edges) {
        if (edges) {
            StringBuilder b = new StringBuilder();
            b.append("(set FX2 (entail ");
            b.append("(a." + attribute + " FA) (a." + attribute + " FB)");
            b.append(" :" + name + "))");
            StringBuilder b2 = new StringBuilder();
            b2.append("(set TX2 (entail ");
            b2.append("(a." + attribute + " TA) (a." + attribute + " TB)");
            b2.append(" :" + name + "))");
            String rule = "(or (and (not (null FX)) (> FX OPP" + name + ") (or (equals (a.lemma TA) (a.lemma TB)) (equals (a.token TA) (a.token TB)))) (and (not (null TX)) (> TX OPP" + name + ") (or (equals (a.lemma FA) (a.lemma FB)) (equals (a.token FA) (a.token FB)))))";
            if (dynamic) return new String[] { b.toString(), b2.toString(), "(set TX (if (null TX2) 0.0 TX2))", "(set FX (if (null FX2) 0.0 FX2))", rule, (!optimizable ? "0.0" : "(* (+ (size (words T)) (size (words H))) OPV" + name + ")") }; else return new String[] { b.toString(), b2.toString(), "(set TX (if (null TX2) 0.0 TX2))", "(set FX (if (null FX2) 0.0 FX2))", rule, (!optimizable ? "0.0" : "OPV" + name) };
        } else {
            StringBuilder b = new StringBuilder();
            b.append("(set X (entail ");
            if (objectsAreWords) {
                b.append("(a." + attribute + " A) (a." + attribute + " B)");
            } else {
                b.append("(a." + attribute + " (word A)) (a." + attribute + " (word B))");
            }
            b.append(" :" + name + "))");
            if (dynamic) return new String[] { b.toString(), "(not (null X))", "(> X OPP" + name + ")", (!optimizable ? "0.0" : "(* (+ (size (words T)) (size (words H))) OPV" + name + ")") }; else return new String[] { b.toString(), "(not (null X))", "(> X OPP" + name + ")", (!optimizable ? "0.0" : "OPV" + name) };
        }
    }

    public static String[] addResourceWords(String name, String attribute, boolean objectsAreWords, boolean optimizable, boolean edges) {
        StringBuilder b = new StringBuilder();
        b.append("(set X (entail ");
        if (objectsAreWords) {
            b.append("A B");
        } else {
            b.append("(word A) (word B)");
        }
        b.append(" :" + name + "))");
        return new String[] { b.toString(), "(not (null X))", "(> X OPP" + name + ")", (!optimizable ? "0.0" : "(* (+ (size (words T)) (size (words H))) OPV" + name + ")") };
    }

    public static void addSubstitutionEdges(XMLCostScheme scheme, boolean optimizable) {
        if (optimizable) {
            XMLCostSchemeConstant c = new XMLCostSchemeConstant();
            c.setName("OPSubstitution");
            c.setValue("0");
            c.setType(XMLCostSchemeConstantType.NUMBER);
            scheme.getConstant().add(c);
        }
        XMLCostSchemeOperation o = new XMLCostSchemeOperation();
        o.getCondition().add("(set FA (word (from A)))");
        o.getCondition().add("(set TA (word (to A)))");
        o.getCondition().add("(set FB (word (from B)))");
        o.getCondition().add("(set TB (word (to B)))");
        o.getCondition().add("(or (equals (a.token FA) (a.token FB)) (equals (a.lemma FA) (a.lemma FB)))");
        o.getCondition().add("(or (equals (a.token TA) (a.token TB)) (equals (a.lemma TA) (a.lemma TB)))");
        o.setCost(optimizable ? "OPSubstitution" : "0");
        scheme.getSubstitution().add(o);
    }

    public static void addSubstitutionNodes(XMLCostScheme scheme, boolean optimizable) {
        if (optimizable) {
            XMLCostSchemeConstant c = new XMLCostSchemeConstant();
            c.setName("OPSubstitution");
            c.setValue("0");
            c.setType(XMLCostSchemeConstantType.NUMBER);
            scheme.getConstant().add(c);
        }
        XMLCostSchemeOperation o = new XMLCostSchemeOperation();
        o.getCondition().add("(and (is-label-node A) (is-label-node B))");
        o.setCost("0");
        scheme.getSubstitution().add(o);
        o = new XMLCostSchemeOperation();
        o.getCondition().add("(and (is-word-node A) (is-word-node B))");
        o.getCondition().add("(or (equals (a.token (word A)) (a.token (word B))) (equals (a.lemma (word A)) (a.lemma (word B))))");
        o.setCost(optimizable ? "OPSubstitution" : "0");
        scheme.getSubstitution().add(o);
    }

    public static void addSubstitutionWords(XMLCostScheme scheme, boolean optimizable) {
        if (optimizable) {
            XMLCostSchemeConstant c = new XMLCostSchemeConstant();
            c.setName("OPSubstitution");
            c.setValue("0");
            c.setType(XMLCostSchemeConstantType.NUMBER);
            scheme.getConstant().add(c);
        }
        XMLCostSchemeOperation o = new XMLCostSchemeOperation();
        o.getCondition().add("(or (equals (a.token A) (a.token B)) (equals (a.lemma A) (a.lemma B)))");
        o.setCost(optimizable ? "OPSubstitution" : "0");
        scheme.getSubstitution().add(o);
    }

    public static XMLCostScheme generate(EditDistanceAlgorithm a, Map<String, RulesRepository> engines) {
        XMLCostScheme scheme = new XMLCostScheme();
        boolean distance = a.isDistanceSubsitution() || a.isNormalizedDistanceSubsitution();
        boolean normalize = a.isNormalizedDistanceSubsitution();
        if (a.needsInsertion()) addInserion(scheme, a.insertionIsOptimizable());
        if (a.needsDeletion()) addDeletion(scheme, a.deletionIsOptimizable());
        if (a.matchEdges() && a.worksWithEdges()) addSubstitutionEdges(scheme, a.substitutionIsOptimizable()); else {
            if (a.worksWithWords()) addSubstitutionWords(scheme, a.substitutionIsOptimizable()); else addSubstitutionNodes(scheme, a.substitutionIsOptimizable());
        }
        addResources(engines, scheme, a.worksWithWords(), a.substitutionIsOptimizable(), a.worksWithEdges() && a.matchEdges());
        addNotSubstitution(scheme, a.substitutionIsOptimizable(), distance && !(a.matchEdges() && a.worksWithEdges()), normalize, a.worksWithWords());
        return scheme;
    }

    public static String generateCostScheme(EditDistanceAlgorithm a, Map<String, RulesRepository> engines) throws EDITSException {
        XMLCostScheme scheme = generate(a, engines);
        String date = SerializationManager.getDate() + "-scheme.xml";
        String tempdir = EDITS.system().tempdir();
        new File(tempdir).mkdir();
        String file = tempdir + "/" + date;
        int i = 1;
        while (new File(file).exists()) {
            file = tempdir + "/" + i + "-" + date;
            i++;
        }
        XmlCostScheme.saveCostScheme(file, scheme, true);
        return file;
    }
}
