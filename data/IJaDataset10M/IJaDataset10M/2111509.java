package org.mcisb.ontology;

import java.util.*;
import org.junit.*;

/**
 * 
 *
 * @author Neil Swainston
 */
public class OntologyTest {

    /**
	 * 
	 */
    private static final String NAME = "NAME";

    /**
	 * 
	 */
    private static final String URI_IDENTIFIER = "URI_IDENTIFIER";

    /**
	 * 
	 */
    private static final String LINK_TEMPLATE = "LINK_TEMPLATE";

    /**
	 * 
	 */
    private static final String REGULAR_EXPRESSION = "REGULAR_EXPRESSION";

    /**
	 * 
	 */
    private final Ontology ontology = new Ontology(NAME, URI_IDENTIFIER, Arrays.asList(URI_IDENTIFIER), LINK_TEMPLATE, REGULAR_EXPRESSION);

    /**
	 *
	 */
    @Test
    public void getName() {
        Assert.assertEquals(ontology.getName(), NAME);
    }

    /**
	 *
	 */
    @Test
    public void getUrlIdentifier() {
        Assert.assertEquals(ontology.getUriIdentifier(), URI_IDENTIFIER);
    }

    /**
	 *
	 */
    @Test
    public void getUrlIdentifiers() {
        Assert.assertTrue(ontology.getUriIdentifiers().contains(URI_IDENTIFIER));
    }

    /**
	 *
	 */
    @Test
    public void getLinkTemplate() {
        Assert.assertEquals(ontology.getLinkTemplate(), LINK_TEMPLATE);
    }

    /**
	 *
	 */
    @Test
    public void getRegularExpression() {
        Assert.assertEquals(ontology.getRegularExpression(), REGULAR_EXPRESSION);
    }

    /**
	 *
	 */
    @Test
    public void toStringTest() {
        Assert.assertEquals(ontology.toString(), NAME);
    }
}
