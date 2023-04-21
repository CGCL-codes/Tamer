package com.hp.hpl.mars.portal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import org.junit.Ignore;
import org.junit.Test;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.mars.portal.component.ComponentFactoryCatalog;
import com.hp.hpl.mars.portal.component.MockComponentFactoryCatalog;
import com.hp.hpl.mars.portal.component.MockDescriptionFactory;
import com.hp.hpl.mars.portal.vocabulary.EXAMPLE;

public class RDFUtilTest {

    private static final String SKOS_NS = "http://www.w3.org/2004/02/skos/core#";

    private ComponentFactoryCatalog factoryCatalog = new MockComponentFactoryCatalog().getCatalog();

    private static final String SAMPLE_NAMESPACE = "http://example.com/portal#";

    private static final String fullClassName = RDFUtilTest.class.getName();

    private static MockDescriptionFactory descriptionFactory = new MockDescriptionFactory();

    /**
	 * If resource has no rdf:type value, then<br />
	 * getAllTypes() returns an empty list
	 */
    @Test
    public void getAllTypesOnNoType() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/getAllTypesOnNoType");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "getAllTypesOnNoType");
        Set<Resource> set = RDFUtil.getSortedTypes(resource);
        assertEquals(0, set.size());
    }

    /**
	 * If resource has some rdf:type values, but they are not in the catalog,
	 * then<br />
	 * getAllTypes() still returns both types.
	 */
    @Test
    public void getAllTypesOnUnrecognizedTypes() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/getAllTypesOnUnrecognizedTypes");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "getAllTypesOnUnrecognizedTypes");
        Set<Resource> set = RDFUtil.getSortedTypes(resource);
        assertEquals(2, set.size());
    }

    /**
	 * If resource has single rdf:type value from the catalogue, then<br />
	 * return that value in a list of size 1
	 */
    @Test
    public void getAllTypesForSingleType() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/getAllTypesForSingleType");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "getAllTypesForSingleType");
        Resource rType = model.getResource(SAMPLE_NAMESPACE + "MockWidget1");
        Set<Resource> set = RDFUtil.getSortedTypes(resource);
        assertEquals(1, set.size());
        assertTrue(set.contains(rType));
    }

    /**
	 * If resource has several types from the catalogue, then<br />
	 * getAllTypes() returns them in sorted order
	 */
    @Test
    public void getAllTypesSortOrder() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/getAllTypesSortOrder");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "getAllTypesSortOrder");
        Resource[] rType = new Resource[4];
        rType[0] = model.getResource(SAMPLE_NAMESPACE + "MockNoError");
        rType[1] = model.getResource(SAMPLE_NAMESPACE + "MockWidget1");
        rType[2] = model.getResource(SAMPLE_NAMESPACE + "MockWidget2");
        rType[3] = model.getResource(SAMPLE_NAMESPACE + "MockWidget3");
        Set<Resource> list = RDFUtil.getSortedTypes(resource);
        assertEquals(4, list.size());
        int count = 0;
        for (Iterator<Resource> i = list.iterator(); i.hasNext(); ) {
            assertEquals(rType[count++], i.next());
        }
    }

    @Test
    public void skipLiteralTypes() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/skipLiteralTypes");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "skipLiteralTypes");
        Set<Resource> list = RDFUtil.getSortedTypes(resource);
        Resource rType0 = model.getResource(SAMPLE_NAMESPACE + "Person");
        assertTrue(list.contains(rType0));
        assertEquals(1, list.size());
    }

    @Test
    public void getLabel() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "nationality_austrian");
        String label = RDFUtil.getLabel(resource);
        assertEquals("Austrian", label);
    }

    @Test
    public void getLabelWithLanguage() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "nationality_austrian");
        Locale locale = new Locale("lv");
        String label = RDFUtil.getLabel(resource, locale);
        assertEquals("austriešu", label);
    }

    @Test
    public void getNonexistentLabel1() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "nationality_unknown");
        String label = RDFUtil.getLabel(resource);
        assertNull(label);
    }

    @Test
    public void getNonexistentLabel2() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "nationality_langspecificonly");
        String label = RDFUtil.getLabel(resource);
        assertNull(label);
    }

    @Test
    public void getNonliteralLabel() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "nationality_nonliteral");
        String label = RDFUtil.getLabel(resource);
        assertNull(label);
    }

    @Test
    public void getIntLabel1() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "nationality_int_datatype");
        String label = RDFUtil.getLabel(resource);
        assertEquals("11", label);
    }

    @Test
    public void getIntLabel2() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = model.getResource(SAMPLE_NAMESPACE + "nationality_int_datatype");
        Locale locale = new Locale("lv");
        String label = RDFUtil.getLabel(resource, locale);
        assertEquals("17", label);
    }

    @Test
    public void getAmbiguousLabel() {
        Model model = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = model.getResource(EXAMPLE.NS + "nationality_ambiguous");
        String label = RDFUtil.getLabel(resource);
        assertTrue("aa".equals(label) || "bb".equals(label));
    }

    @Test
    public void getResourceByLabel() {
        Model ontology = descriptionFactory.getPortalDescription(fullClassName + "/combination_ontology");
        Resource resource = RDFUtil.getResourceByLabel(ontology, "Polish");
        assertTrue(resource.getURI().equals("http://example.com/portal#nationality_polish"));
        resource = RDFUtil.getResourceByLabel(ontology, "Hungarian");
        resource = RDFUtil.getResourceByLabel(ontology, "poļu", new Locale("lv"));
        assertTrue(resource.getURI().equals("http://example.com/portal#nationality_polish"));
        resource = RDFUtil.getResourceByLabel(ontology, "XX");
        assertNull(resource);
    }

    @Test
    public void getResourceByLabelPath() {
        Model ontology = descriptionFactory.getPortalDescription(fullClassName + "/hierarchy_ontology");
        Resource root = ontology.createResource(EXAMPLE.NS + "indo-european");
        Property downProperty = ontology.createProperty(SKOS_NS + "narrower");
        Property upProperty = ontology.createProperty(SKOS_NS + "broader");
        Resource r0 = RDFUtil.getResourceByLabelPath(ontology, root, downProperty, upProperty, "");
        assertEquals("indo-european", r0.getLocalName());
        Resource r1 = RDFUtil.getResourceByLabelPath(ontology, root, downProperty, upProperty, "/Germanic");
        assertEquals("germanic", r1.getLocalName());
        Resource r2 = RDFUtil.getResourceByLabelPath(ontology, root, downProperty, upProperty, "/Germanic/West Germanic");
        assertEquals("west_germanic", r2.getLocalName());
        Resource r3 = RDFUtil.getResourceByLabelPath(ontology, root, downProperty, upProperty, "/Germanic/West Germanic/Anglo Frisian");
        assertEquals("anglo_frisian", r3.getLocalName());
    }
}
