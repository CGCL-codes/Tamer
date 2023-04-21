package com.googlecode.kipler.test;

import junit.framework.TestCase;
import com.googlecode.kipler.syntax.Role;
import com.googlecode.kipler.syntax.concept.BooleanConcept;
import com.googlecode.kipler.syntax.concept.BooleanConstructor;
import com.googlecode.kipler.syntax.concept.Bottom;
import com.googlecode.kipler.syntax.concept.Concept;
import com.googlecode.kipler.syntax.concept.ConceptName;
import com.googlecode.kipler.syntax.concept.InequalityConstructor;
import com.googlecode.kipler.syntax.concept.QualifiedNoRestriction;
import com.googlecode.kipler.syntax.concept.RoleQuantification;
import com.googlecode.kipler.syntax.concept.Top;
import com.googlecode.kipler.syntax.concept.RoleQuantification.Constructor;
import com.googlecode.kipler.transformations.Transformations;

public class ConceptSimplificationVisitorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testIntersectionWithBottom() {
        Concept c = new BooleanConcept(BooleanConstructor.INTERSECTION, new ConceptName("A"), new Bottom());
        assertEquals(new Bottom(), Transformations.simplify(c));
        c = new BooleanConcept(BooleanConstructor.INTERSECTION, new Bottom(), new ConceptName("A"));
        assertEquals(new Bottom(), Transformations.simplify(c));
    }

    public void testIntersectionWithTop() {
        Concept c = new BooleanConcept(BooleanConstructor.INTERSECTION, new ConceptName("A"), new Top());
        Concept expected = new ConceptName("A");
        assertEquals(expected, Transformations.simplify(c));
        c = new BooleanConcept(BooleanConstructor.INTERSECTION, new Top(), new ConceptName("A"));
        assertEquals(expected, Transformations.simplify(c));
    }

    public void testUnionWithBottom() {
        Concept c = new BooleanConcept(BooleanConstructor.UNION, new ConceptName("A"), new Bottom());
        Concept expected = new ConceptName("A");
        assertEquals(expected, Transformations.simplify(c));
        c = new BooleanConcept(BooleanConstructor.UNION, new Bottom(), new ConceptName("A"));
        assertEquals(expected, Transformations.simplify(c));
    }

    public void testUnionWithTop() {
        Concept c = new BooleanConcept(BooleanConstructor.UNION, new ConceptName("C"), new Top());
        assertEquals(new Top(), Transformations.simplify(c));
        c = new BooleanConcept(BooleanConstructor.UNION, new Top(), new ConceptName("A"));
        assertEquals(new Top(), Transformations.simplify(c));
    }

    public void testAtLeastRestrictionForNEqualTo0() {
        Concept c = new QualifiedNoRestriction(InequalityConstructor.AT_LEAST, 0, new Role("R"), new ConceptName("A"));
        Concept expected = new Top();
        assertEquals(expected, Transformations.simplify(c));
    }

    public void testAtMostRestrictionForNEqualTo0() {
        Concept c = new QualifiedNoRestriction(InequalityConstructor.AT_MOST, 0, new Role("R"), new ConceptName("A"));
        Concept expected = new RoleQuantification(Constructor.ALL, new Role("R"), new ConceptName("A").toggleNegated());
        assertEquals(expected, Transformations.simplify(c));
    }
}
