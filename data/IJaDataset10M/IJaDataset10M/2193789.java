package org.semanticweb.owlapi.api.test;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Information Management Group<br>
 * Date: 06-Jun-2008<br><br>
 */
public class ObjectPropertySimplifierTestCase extends AbstractOWLAPITestCase {

    public void testNamedSimplification() {
        OWLObjectProperty p = getFactory().getOWLObjectProperty(IRI.create("p"));
        OWLObjectPropertyExpression exp = p.getSimplified();
        assertEquals(p, exp);
    }

    public void testInverseSimplification() {
        OWLObjectProperty p = getFactory().getOWLObjectProperty(IRI.create("p"));
        OWLObjectPropertyExpression inv = getFactory().getOWLObjectInverseOf(p);
        OWLObjectPropertyExpression exp = inv.getSimplified();
        assertEquals(inv, exp);
    }

    public void testInverseInverseSimplification() {
        OWLObjectProperty p = getFactory().getOWLObjectProperty(IRI.create("p"));
        OWLObjectPropertyExpression inv = getFactory().getOWLObjectInverseOf(p);
        OWLObjectPropertyExpression inv2 = getFactory().getOWLObjectInverseOf(inv);
        OWLObjectPropertyExpression exp = inv2.getSimplified();
        assertEquals(p, exp);
    }

    public void testInverseInverseInverseSimplification() {
        OWLObjectProperty p = getFactory().getOWLObjectProperty(IRI.create("p"));
        OWLObjectPropertyExpression inv = getFactory().getOWLObjectInverseOf(p);
        OWLObjectPropertyExpression inv2 = getFactory().getOWLObjectInverseOf(inv);
        OWLObjectPropertyExpression inv3 = getFactory().getOWLObjectInverseOf(inv2);
        OWLObjectPropertyExpression exp = inv3.getSimplified();
        assertEquals(inv, exp);
    }
}
