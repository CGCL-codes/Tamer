package com.hp.hpl.jena.ontology.impl.test;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.test.ModelTestBase;

/**
 * <p>
 * Misc. tests for OntClass, over and above those in
 * {@link TestClassExpression}
 * </p>
 *
 * @author Ian Dickinson, HP Labs (<a href="mailto:Ian.Dickinson@hp.com">email</a>)
 * @version CVS $Id: TestOntClass.java,v 1.5 2006/03/22 13:53:13 andy_seaborne Exp $
 */
public class TestOntClass extends ModelTestBase {

    private static final String NS = "http://example.com/test#";

    public TestOntClass(String name) {
        super(name);
    }

    public void testSuperClassNE() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");
        assertNull(a.getSuperClass());
        assertFalse(a.hasSuperClass());
    }

    public void testSubClassNE() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");
        assertNull(a.getSubClass());
        assertFalse(a.hasSubClass());
    }

    public void testCreateIndividual() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");
        Individual i = a.createIndividual(NS + "i");
        assertTrue(i.hasRDFType(a));
        Individual j = a.createIndividual();
        assertTrue(j.hasRDFType(a));
    }

    public void testIsHierarchyRoot0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot1() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot2() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot3() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_TRANS_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot4() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot5() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot6() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.DAML_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot7() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.DAML_MEM_RULE_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot8() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }

    public void testIsHierarchyRoot9() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        assertTrue(a.isHierarchyRoot());
        assertFalse(b.isHierarchyRoot());
    }
}
