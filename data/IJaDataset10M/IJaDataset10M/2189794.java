package org.sti2.elly.transformation.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.sti2.elly.api.basics.IAtom;
import org.sti2.elly.api.basics.IRule;
import org.sti2.elly.api.factory.IBasicFactory;
import org.sti2.elly.api.factory.ITermFactory;
import org.sti2.elly.api.terms.IIndividual;
import org.sti2.elly.basics.BasicFactory;
import org.sti2.elly.terms.TermFactory;
import org.sti2.elly.transformation.helpers.RuleGroundingFactory;
import org.sti2.elly.util.Rules;

public class RuleGroundingFactoryTest extends TestCase {

    private static IBasicFactory BASIC = BasicFactory.getInstance();

    private static ITermFactory TERM = TermFactory.getInstance();

    private RuleGroundingFactory grFactory;

    private Set<IIndividual> individuals;

    IAtom safeConcept;

    IAtom unsafeConcept;

    IAtom constConcept;

    IAtom safeRole;

    IAtom safeRoleEQ;

    IAtom safeRole1;

    IAtom safeRole2;

    IAtom unsafeRole;

    IAtom constRole;

    List<IAtom> roles;

    @Before
    protected void setUp() {
        individuals = new HashSet<IIndividual>();
        for (int i = 0; i < 10; i++) {
            individuals.add(TERM.createIndividual("individual__" + i));
        }
        grFactory = new RuleGroundingFactory(individuals);
        roles = new ArrayList<IAtom>();
        safeConcept = BASIC.createAtom(BASIC.createAtomicConcept("safeConcept"), BASIC.createTuple(TERM.createVariable("safeVariable", true)));
        unsafeConcept = BASIC.createAtom(BASIC.createAtomicConcept("unsafeConcept"), BASIC.createTuple(TERM.createVariable("unsafeVariable", false)));
        constConcept = BASIC.createAtom(BASIC.createAtomicConcept("safeConcept"), BASIC.createTuple(TERM.createIndividual("const")));
        safeRole = BASIC.createAtom(BASIC.createAtomicRole("safeRole"), BASIC.createTuple(TERM.createVariable("safeVariable1", true), TERM.createVariable("safeVariable2", true)));
        safeRoleEQ = BASIC.createAtom(BASIC.createAtomicRole("safeRole"), BASIC.createTuple(TERM.createVariable("safeVariable", true), TERM.createVariable("safeVariable", true)));
        safeRole1 = BASIC.createAtom(BASIC.createAtomicRole("safeRole"), BASIC.createTuple(TERM.createVariable("safeVariable1", true), TERM.createVariable("unsafeVariable2", false)));
        safeRole2 = BASIC.createAtom(BASIC.createAtomicRole("safeRole"), BASIC.createTuple(TERM.createVariable("unsafeVariable1", false), TERM.createVariable("safeVariable2", true)));
        unsafeRole = BASIC.createAtom(BASIC.createAtomicRole("safeRole"), BASIC.createTuple(TERM.createVariable("unsafeVariable1", false), TERM.createVariable("unsafeVariable2", false)));
        constRole = BASIC.createAtom(BASIC.createAtomicRole("safeRole"), BASIC.createTuple(TERM.createIndividual("const1"), TERM.createIndividual("const2")));
        roles.add(safeRole);
        roles.add(safeRoleEQ);
        roles.add(safeRole1);
        roles.add(safeRole2);
        roles.add(unsafeRole);
        roles.add(constRole);
    }

    @Test
    public void testGetGroundedRules() {
        List<IAtom> head = new ArrayList<IAtom>();
        List<IAtom> body = new ArrayList<IAtom>();
        head.add(safeConcept);
        for (IAtom role : roles) {
            body.clear();
            body.add(role);
            IRule rule = BASIC.createRule(head, body);
            List<IRule> rules = grFactory.getGroundedRules(rule);
            System.out.println("Calculating " + rule);
            assertEquals(Math.round(Math.pow(individuals.size(), Rules.getSafeVariables(rule).size())), rules.size());
        }
    }
}
