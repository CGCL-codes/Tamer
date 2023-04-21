package org.sti2.elly.reasoning.iris;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.sti2.elly.api.basics.IRuleBase;
import org.sti2.elly.api.factory.IBasicFactory;
import org.sti2.elly.api.factory.ITermFactory;
import org.sti2.elly.api.reasoning.IReasoner;
import org.sti2.elly.api.reasoning.ReasoningException;
import org.sti2.elly.basics.BasicFactory;
import org.sti2.elly.parser.EllyParser;
import org.sti2.elly.parser.EllyParserException;
import org.sti2.elly.terms.TermFactory;
import org.sti2.elly.transformation.ElpTransformation;
import org.sti2.elly.util.ResourceHelper;

/**
 * @author Daniel Winkler
 *
 */
public class FoodPreferencesTest {

    private static String filename = "food_preferences.elp";

    private static IBasicFactory BASIC = BasicFactory.getInstance();

    private static ITermFactory TERM = TermFactory.getInstance();

    public IRuleBase getRuleBase() {
        EllyParser parser = new EllyParser();
        String program = "";
        IRuleBase rb = null;
        try {
            program = ResourceHelper.loadResource(filename);
        } catch (Exception e) {
            System.out.println("Unable to load input file: " + e.getMessage());
            e.printStackTrace();
            throw new AssertionError("specify file!");
        }
        try {
            rb = parser.parse(program);
        } catch (EllyParserException e) {
            System.out.println("Error at parsing file:" + e.getMessage());
            e.printStackTrace();
            throw new AssertionError(e);
        }
        return ElpTransformation.toDatalog(rb);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public final void testIsSatisfiable() throws ReasoningException {
        System.out.println("Testing testIsSatisfiable");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        System.out.println(reasoner.isSatisfiable());
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#allConcepts()}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testAllConcepts() throws ReasoningException {
        System.out.println("Testing testAllConcepts");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        System.out.println(reasoner.allConcepts());
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#allIndividuals()}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testAllIndividuals() throws ReasoningException {
        System.out.println("Testing testAllIndividuals");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        System.out.println(reasoner.allIndividuals());
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#allInstancesOf(org.sti2.elly.api.basics.IConceptDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testAllInstancesOf() throws ReasoningException {
        System.out.println("Testing testAllInstancesOf");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        System.out.println(reasoner.allInstancesOf(BASIC.createAtomicConcept("NoThing")));
        System.out.println(reasoner.allInstancesOf(BASIC.createAtomicConcept("Vegan")));
        System.out.println(reasoner.allInstancesOf(BASIC.createAtomicConcept("Unhappy")));
        System.out.println(reasoner.allInstancesOf(BASIC.createIntersectionConcept(BASIC.createAtomicConcept("Unhappy"), BASIC.createAtomicConcept("Vegetarian"))));
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#allInstancesOf(org.sti2.elly.api.basics.IConceptDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testAllInstancesOfIRoleDescription() throws ReasoningException {
        System.out.println("Testing testAllInstancesOfIRoleDescription");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.allInstancesOf(BASIC.createAtomicRole("r"));
        reasoner.allInstancesOf(BASIC.createAtomicRole("s"));
        reasoner.allInstancesOf(BASIC.createAtomicRole("t"));
        reasoner.allInstancesOf(BASIC.createAtomicRole("p"));
        reasoner.allInstancesOf(BASIC.createIntersectionRole(BASIC.createAtomicRole("r"), BASIC.createAtomicRole("s")));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#allRoles()}.
	 */
    @Test
    public final void testAllRoles() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#deRegister()}.
	 */
    @Test
    public final void testDeRegister() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#equivalentConceptOf(org.sti2.elly.api.basics.IConceptDescription)}.
	 */
    @Test
    public final void testEquivalentClassesOf() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#equivalentRoleOf(org.sti2.elly.api.basics.IRoleDescription)}.
	 */
    @Test
    public final void testEquivalentRoleOf() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isSatisfiable()}.
	 */
    @Test
    public final void testIsConsistent() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isSatisfiable(org.sti2.elly.api.basics.IConceptDescription)}.
	 */
    @Test
    public final void testIsConsistentIConceptDescription() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isSatisfiable(org.sti2.elly.api.basics.IRoleDescription)}.
	 */
    @Test
    public final void testIsConsistentIRoleDescription() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isEquivalentConcept(org.sti2.elly.api.basics.IConceptDescription, org.sti2.elly.api.basics.IConceptDescription)}.
	 */
    @Test
    public final void testIsEquivalentClass() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isEquivalentRole(org.sti2.elly.api.basics.IRoleDescription, org.sti2.elly.api.basics.IRoleDescription)}.
	 */
    @Test
    public final void testIsEquivalentRole() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isInstanceOf(org.sti2.elly.api.terms.IIndividual, org.sti2.elly.api.basics.IConceptDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testIsInstanceOfIIndividualIConceptDescription() throws ReasoningException {
        System.out.println("Testing testIsInstanceOfIIndividualIConceptDescription");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("a")), BASIC.createAtomicConcept("E"));
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("b")), BASIC.createAtomicConcept("E"));
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("c")), BASIC.createAtomicConcept("E"));
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("a")), BASIC.createAtomicConcept("C"));
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("b")), BASIC.createAtomicConcept("C"));
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("c")), BASIC.createAtomicConcept("C"));
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("a")), BASIC.createAtomicConcept("H"));
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("b")), BASIC.createAtomicConcept("H"));
        reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("c")), BASIC.createAtomicConcept("H"));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isInstanceOf(org.sti2.elly.api.terms.IIndividual, org.sti2.elly.api.terms.IIndividual, org.sti2.elly.api.basics.IRoleDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testIsInstanceOfIIndividualIIndividualIRoleDescription() throws ReasoningException {
        System.out.println("Testing testIsInstanceOfIIndividualIIndividualIRoleDescription");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        assertTrue(reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("a"), TERM.createIndividual("a")), BASIC.createAtomicRole("r")));
        assertTrue(reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("a"), TERM.createIndividual("b")), BASIC.createAtomicRole("t")));
        assertTrue(reasoner.isInstanceOf(BASIC.createTuple(TERM.createIndividual("c"), TERM.createIndividual("c")), BASIC.createAtomicRole("q")));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isSubConceptOf(org.sti2.elly.api.basics.IConceptDescription, org.sti2.elly.api.basics.IConceptDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testIsSubConceptOf() throws ReasoningException {
        System.out.println("Testing IsSubConceptOf");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.isSubConceptOf(BASIC.createAtomicConcept("E"), BASIC.createAtomicConcept("C"));
        reasoner.isSubConceptOf(BASIC.createAtomicConcept("C"), BASIC.createAtomicConcept("E"));
        reasoner.isSubConceptOf(BASIC.createAtomicConcept("G"), BASIC.createAtomicConcept("E"));
        reasoner.isSubConceptOf(BASIC.createAtomicConcept("F1"), BASIC.createAtomicConcept("G"));
        reasoner.isSubConceptOf(BASIC.createAtomicConcept("G"), BASIC.createAtomicConcept("F1"));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#isSubRoleOf(org.sti2.elly.api.basics.IRoleDescription, org.sti2.elly.api.basics.IRoleDescription)}.
	 */
    @Test
    public final void testIsSubRoleOf() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#register(org.sti2.elly.api.basics.IRuleBase)}.
	 */
    @Test
    public final void testRegister() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#subConceptOf(org.sti2.elly.api.basics.IConceptDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testSubConceptOf() throws ReasoningException {
        System.out.println("Testing testSubConceptOf");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.subConceptOf(BASIC.createAtomicConcept("E"));
        reasoner.subConceptOf(BASIC.createAtomicConcept("D"));
        reasoner.subConceptOf(BASIC.createAtomicConcept("G"));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#subRoleOf(org.sti2.elly.api.basics.IRoleDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testSubRoleOf() throws ReasoningException {
        System.out.println("Testing testSubRoleOf");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.subRoleOf(BASIC.createAtomicRole("r"));
        reasoner.subRoleOf(BASIC.createAtomicRole("t"));
        reasoner.subRoleOf(BASIC.createAtomicRole("q"));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#superConceptOf(org.sti2.elly.api.basics.IConceptDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testSuperConceptOf() throws ReasoningException {
        System.out.println("Testing testSuperConceptOf");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.superConceptOf(BASIC.createAtomicConcept("C"));
        reasoner.superConceptOf(BASIC.createAtomicConcept("D"));
        reasoner.superConceptOf(BASIC.createAtomicConcept("F"));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#superRoleOf(org.sti2.elly.api.basics.IRoleDescription)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testSuperRoleOf() throws ReasoningException {
        System.out.println("Testing testSuperRoleOf");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.superRoleOf(BASIC.createAtomicRole("r"));
        reasoner.superRoleOf(BASIC.createAtomicRole("t"));
        reasoner.superRoleOf(BASIC.createAtomicRole("p"));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#conceptsOf(org.sti2.elly.api.terms.IIndividual)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testTypesOfIIndividual() throws ReasoningException {
        System.out.println("Testing testTypesOfIIndividual");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.conceptsOf(BASIC.createTuple(TERM.createIndividual("a")));
        reasoner.conceptsOf(BASIC.createTuple(TERM.createIndividual("b")));
        reasoner.conceptsOf(BASIC.createTuple(TERM.createIndividual("c")));
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.sti2.elly.reasoning.iris.IrisReasoner#rolesOf(org.sti2.elly.api.terms.IIndividual, org.sti2.elly.api.terms.IIndividual)}.
	 * @throws ReasoningException 
	 */
    @Test
    public final void testTypesOfIIndividualIIndividual() throws ReasoningException {
        System.out.println("Testing testTypesOfIIndividualIIndividual");
        IRuleBase ruleBase = getRuleBase();
        IReasoner reasoner = new IrisReasoner();
        reasoner.register(ruleBase);
        reasoner.rolesOf(BASIC.createTuple(TERM.createIndividual("a"), TERM.createIndividual("b")));
        reasoner.rolesOf(BASIC.createTuple(TERM.createIndividual("a"), TERM.createIndividual("c")));
        reasoner.rolesOf(BASIC.createTuple(TERM.createIndividual("c"), TERM.createIndividual("a")));
        fail("Not yet implemented");
    }
}
