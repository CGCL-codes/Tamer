package org.mcisb.ontology.sbo;

import java.util.*;
import org.junit.*;
import org.mcisb.ontology.*;
import org.sbml.jsbml.*;

/**
 *
 * @author Neil Swainston
 */
public class SboUtilsTest {

    /**
	 * 
	 */
    private final SboUtils utils = SboUtils.getInstance();

    /**
	 *
	 * @throws Exception
	 */
    public SboUtilsTest() throws Exception {
    }

    /**
	 *
	 * @throws Exception
	 */
    @Test
    public void getOntologyTerm() throws Exception {
        final Set<OntologyTerm> ontologyTermsSet = new HashSet<OntologyTerm>();
        final List<OntologyTerm> ontologyTermsList = new ArrayList<OntologyTerm>();
        SboTerm ontologyTerm = (SboTerm) utils.getOntologyTerm("Henri-Michaelis-Menten rate law");
        ontologyTermsSet.add(ontologyTerm);
        ontologyTermsList.add(ontologyTerm);
        test(ontologyTerm);
        ontologyTerm = (SboTerm) utils.getOntologyTerm("SBO:0000029");
        ontologyTermsSet.add(ontologyTerm);
        ontologyTermsList.add(ontologyTerm);
        test(ontologyTerm);
        ontologyTerm = (SboTerm) utils.getOntologyTerm(29);
        ontologyTermsSet.add(ontologyTerm);
        ontologyTermsList.add(ontologyTerm);
        test(ontologyTerm);
        Assert.assertTrue(ontologyTermsSet.size() == 1);
        Assert.assertTrue(ontologyTermsList.size() == 3);
    }

    /**
	 *
	 * @param ontologyTerm
	 * @throws Exception
	 */
    public void test(final SboTerm ontologyTerm) throws Exception {
        Assert.assertTrue(ontologyTerm.getName().equals("Henri-Michaelis-Menten rate law"));
        Assert.assertTrue(ontologyTerm.getFormula().equals("lambda(kcat, Et, S, Ks, kcat*Et*S/(Ks+S))"));
        Assert.assertTrue(ontologyTerm.getIntId() == 29);
        Assert.assertTrue(utils.getShortName(ontologyTerm.getRawMath(), 373).equals("Ks"));
        final KineticLaw kineticLaw = new KineticLaw();
        kineticLaw.setMath(JSBML.readMathMLFromString(ontologyTerm.getMath()));
        Assert.assertTrue(JSBML.formulaToString(kineticLaw.getMath()).equals("lambda(kcat, Et, S, Ks, kcat*Et*S/(Ks+S))"));
    }
}
