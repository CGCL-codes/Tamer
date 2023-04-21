package com.hp.hpl.mars.portal.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileInputStream;
import org.junit.Test;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.mars.portal.PortalException;

/**
 *
 */
public class SparqlFindResourcesFactoryTest extends AbstractComponentFactoryTest {

    static Model model;

    protected static final String EX = "http://example.com/portal#";

    static {
        model = ModelFactory.createDefaultModel();
        File file = new File("src/test/resources/com.hp.hpl.mars.portal.component.SparqlFindResourcesFactory/model1.n3");
        try {
            model.read(new FileInputStream(file), "", "N3");
        } catch (Exception e) {
            throw new PortalException("Can read config model for test", e);
        }
    }

    @Test
    public void properties() throws Exception {
        SparqlFindResourcesFactory factory = new SparqlFindResourcesFactory();
        Resource r = getImmutableResource();
        SparqlFindResources component = (SparqlFindResources) factory.getComponent(r, componentManager, null);
        assertNotNull(component.getDataSource());
        assertEquals(component.getPrefixes(), "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        assertEquals(component.getSelectExpression(), "?result rdfs:class ?object");
        assertEquals(component.getResultVariableName(), "testResultVariableName");
        assertTrue(component.getLimit() == 10);
        assertTrue(component.getOffset() == 5);
        assertEquals(component.getOrderBy(), "o");
        assertFalse(component.isAscending());
    }

    @Test
    public void defaultPrefixes() throws Exception {
        SparqlFindResourcesFactory factory = new SparqlFindResourcesFactory();
        Resource r = model.getResource(EX + "defaultPrefixes");
        SparqlFindResources component = (SparqlFindResources) factory.getComponent(r, componentManager, null);
        String prefixes = component.getPrefixes();
        assertTrue(prefixes.indexOf("PREFIX rdf: <") >= 0);
        assertTrue(prefixes.indexOf("PREFIX rdfs: <") >= 0);
        assertFalse(prefixes.indexOf("PREFIX foobar: <") >= 0);
    }

    @Override
    protected Resource getImmutableResource() {
        return model.getResource(EX + "immutable");
    }

    @Override
    protected Resource getMutableResource() {
        return model.getResource(EX + "mutable");
    }

    @Override
    protected AbstractComponentFactory getTarget() {
        SparqlFindResourcesFactory factory = new SparqlFindResourcesFactory();
        return factory;
    }
}
