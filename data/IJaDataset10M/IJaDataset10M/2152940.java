package org.dllearner.test.junit;

import java.util.Collections;
import org.dllearner.core.AbstractReasonerComponent;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.ComponentManager;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.core.owl.KB;
import org.dllearner.kb.KBFile;
import org.dllearner.kb.OWLFile;
import org.dllearner.parser.KBParser;
import org.dllearner.parser.ParseException;
import org.dllearner.reasoning.OWLAPIReasoner;

/**
 * Some ontologies to simplify unit tests.
 * 
 * @author Jens Lehmann
 *
 */
public final class TestOntologies {

    public enum TestOntology {

        EMPTY, SIMPLE, SIMPLE_NO_DR, SIMPLE_NO_DISJOINT, SIMPLE_NO_DR_DISJOINT, SIMPLE2, SIMPLE3, R1SUBR2, DATA1, FIVE_ROLES, FATHER, FATHER_OE, CARCINOGENESIS, EPC_OE, KRK_ZERO_ONE, DBPEDIA_OWL, TRAINS_OWL, RHO1, SWORE, MDM
    }

    ;

    public static AbstractReasonerComponent getTestOntology(TestOntology ont) {
        String kbString = "";
        String owlFile = "";
        if (ont.equals(TestOntology.EMPTY)) {
        } else if (ont.equals(TestOntology.SIMPLE)) {
            kbString += "OPDOMAIN(hasChild) = human.\n";
            kbString += "OPRANGE(hasChild) = human.\n";
            kbString += "OPDOMAIN(hasPet) = human.\n";
            kbString += "OPRANGE(hasPet) = animal.\n";
            kbString += "Subrole(hasChild, has).\n";
            kbString += "Subrole(hasPet, has).\n";
            kbString += "bird SUB animal.\n";
            kbString += "cat SUB animal.\n";
            kbString += "(human AND animal) = BOTTOM.\n";
        } else if (ont.equals(TestOntology.SIMPLE_NO_DR)) {
            kbString += "Subrole(hasChild, has).\n";
            kbString += "Subrole(hasPet, has).\n";
            kbString += "bird SUB animal.\n";
            kbString += "cat SUB animal.\n";
            kbString += "(human AND animal) = BOTTOM.\n";
        } else if (ont.equals(TestOntology.SIMPLE_NO_DISJOINT)) {
            kbString += "OPDOMAIN(hasChild) = human.\n";
            kbString += "OPRANGE(hasChild) = human.\n";
            kbString += "OPDOMAIN(hasPet) = human.\n";
            kbString += "OPRANGE(hasPet) = animal.\n";
            kbString += "Subrole(hasChild, has).\n";
            kbString += "Subrole(hasPet, has).\n";
            kbString += "bird SUB animal.\n";
            kbString += "cat SUB animal.\n";
            kbString += "human SUB TOP.\n";
        } else if (ont.equals(TestOntology.SIMPLE_NO_DR_DISJOINT)) {
            kbString += "Subrole(hasChild, has).\n";
            kbString += "Subrole(hasPet, has).\n";
            kbString += "bird SUB animal.\n";
            kbString += "cat SUB animal.\n";
            kbString += "human SUB TOP.\n";
        } else if (ont.equals(TestOntology.SIMPLE2)) {
            kbString += "Subrole(r2,r3).\n";
            kbString += "a1 SUB TOP.\n";
            kbString += "a2 SUB a3.\n";
            kbString += "r1(a,b).\n";
        } else if (ont.equals(TestOntology.SIMPLE3)) {
            kbString += "a1 SUB a2.\n";
            kbString += "Subrole(r1,r2).\n";
        } else if (ont.equals(TestOntology.R1SUBR2)) {
            kbString += "Subrole(r1,r2).\n";
            kbString += "a1 SUB TOP.\n";
            kbString += "a2 SUB TOP.\n";
        } else if (ont.equals(TestOntology.DATA1)) {
            kbString += "man SUB person.\n";
            kbString += "woman SUB person.\n";
            kbString += "man(eric).\n";
            kbString += "woman(diana).\n";
            kbString += "married(eric,diana).\n";
            kbString += "hasChild(eric,frank).\n";
            kbString += "hasChild(eric,tim).\n";
        } else if (ont.equals(TestOntology.FIVE_ROLES)) {
            kbString += "r1(a,b).\n";
            kbString += "r2(a,b).\n";
            kbString += "r3(a,b).\n";
            kbString += "r4(a,b).\n";
            kbString += "r5(a,b).\n";
        } else if (ont.equals(TestOntology.RHO1)) {
            kbString += "suv SUB car.\n";
            kbString += "limo SUB car.\n";
            kbString += "man SUB person.\n";
            kbString += "woman SUB person.\n";
            kbString += "(person AND car) = BOTTOM.\n";
            kbString += "OPDOMAIN(hasOwner) = car.\n";
            kbString += "OPRANGE(hasOwner) = person.\n";
            kbString += "hasOwner(opel123,person123).\n";
        } else if (ont.equals(TestOntology.FATHER)) {
            owlFile = "../examples/father.owl";
        } else if (ont.equals(TestOntology.FATHER_OE)) {
            owlFile = "../examples/family/father_oe.owl";
        } else if (ont.equals(TestOntology.CARCINOGENESIS)) {
            owlFile = "../examples/carcinogenesis/carcinogenesis.owl";
        } else if (ont.equals(TestOntology.EPC_OE)) {
            owlFile = "../test/epc/sap_epc_oe.owl";
        } else if (ont.equals(TestOntology.KRK_ZERO_ONE)) {
            owlFile = "../test/krk/KRK_ZERO_ONE.owl";
        } else if (ont.equals(TestOntology.DBPEDIA_OWL)) {
            owlFile = "/home/jl/promotion/ontologien/dbpedia.owl";
        } else if (ont.equals(TestOntology.TRAINS_OWL)) {
            owlFile = "../test/cross-benchmark/trains/trains.owl";
        } else if (ont.equals(TestOntology.SWORE)) {
            owlFile = "../examples/swore/swore.rdf";
        } else if (ont.equals(TestOntology.MDM)) {
            owlFile = "../test/MDM0.73.owl";
        }
        try {
            ComponentManager cm = ComponentManager.getInstance();
            KnowledgeSource source;
            if (!kbString.isEmpty() || ont.equals(TestOntology.EMPTY)) {
                KB kb = KBParser.parseKBFile(kbString);
                source = new KBFile(kb);
            } else {
                source = new OWLFile(owlFile);
            }
            AbstractReasonerComponent rc = new OWLAPIReasoner(Collections.singleton(source));
            rc.init();
            return rc;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ComponentInitException e) {
            e.printStackTrace();
        }
        throw new Error("Test ontology could not be created.");
    }
}
