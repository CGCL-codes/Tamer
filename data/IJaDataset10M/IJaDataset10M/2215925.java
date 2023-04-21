package org.mcisb.ontology;

import java.util.*;
import org.junit.*;
import org.sbml.jsbml.*;

/**
 * 
 *
 * @author Neil Swainston
 */
public class OntologyTermTest {

    /**
	 * 
	 */
    private static final String ONTOLOGY_NAME = Ontology.CHEBI;

    /**
	 * 
	 */
    private static final String TERM_NAME = "TERM_NAME";

    /**
	 * 
	 */
    private static final String TERM_ID = "TERM_ID";

    /**
	 * 
	 */
    private static final String LINK = "urn:miriam:obo.chebi:TERM_ID";

    /**
	 * 
	 */
    private final Collection<String> synonyms = Arrays.asList(TERM_ID, TERM_NAME, TERM_NAME, TERM_ID);

    /**
	 * 
	 */
    private final OntologyTerm ontologyTerm = new OntologyTerm(OntologyFactory.getOntology(ONTOLOGY_NAME), TERM_ID);

    /**
	 * 
	 */
    private final OntologyTerm ontologyTerm2 = new OntologyTerm(OntologyFactory.getOntology(ONTOLOGY_NAME), "Random");

    /**
	 * 
	 */
    private final OntologyTerm ontologyTerm3 = new OntologyTerm(OntologyFactory.getOntology(ONTOLOGY_NAME), TERM_ID);

    /**
	 * 
	 */
    private final OntologyTerm ontologyTerm4 = new OntologyTerm(OntologyFactory.getOntology(Ontology.EC), TERM_ID);

    /**
	 * 
	 */
    private final Collection<OntologyTerm> xrefs = Arrays.asList(ontologyTerm4, ontologyTerm2, ontologyTerm3, ontologyTerm2, ontologyTerm4, ontologyTerm3, ontologyTerm2, ontologyTerm2, ontologyTerm2, ontologyTerm4);

    /**
	 *
	 * @throws Exception
	 */
    public OntologyTermTest() throws Exception {
        ontologyTerm.setName(TERM_NAME);
        for (Iterator<String> iterator = synonyms.iterator(); iterator.hasNext(); ) {
            ontologyTerm.addSynonym(iterator.next());
        }
        for (Iterator<OntologyTerm> iterator = xrefs.iterator(); iterator.hasNext(); ) {
            ontologyTerm.addXref(iterator.next(), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS);
        }
    }

    /**
	 *
	 */
    @Test
    public void getId() {
        Assert.assertEquals(ontologyTerm.getId(), TERM_ID);
    }

    /**
	 *
	 * @throws Exception
	 */
    @Test
    public void getName() throws Exception {
        Assert.assertEquals(ontologyTerm.getName(), TERM_NAME);
    }

    /**
	 *
	 */
    @Test
    public void getOntology() {
        Assert.assertEquals(ontologyTerm.getOntology().getName(), ONTOLOGY_NAME);
    }

    /**
	 *
	 * @throws Exception
	 */
    @Test
    public void getSynonyms() throws Exception {
        final Collection<String> ontologySynoymns = ontologyTerm.getSynonyms();
        Assert.assertTrue(ontologySynoymns.size() == 2);
        Assert.assertTrue(ontologySynoymns.containsAll(synonyms));
    }

    /**
	 *
	 */
    @Test
    public void toUri() {
        Assert.assertEquals(ontologyTerm.toUri(), LINK);
    }

    /**
	 *
	 * @throws Exception
	 */
    @Test
    public void getXrefs() throws Exception {
        final Collection<OntologyTerm> ontologyXrefs = ontologyTerm.getXrefs().keySet();
        Assert.assertTrue(ontologyXrefs.size() == 3);
        Assert.assertTrue(ontologyXrefs.containsAll(xrefs));
    }

    /**
	 *
	 */
    @Test
    public void equals() {
        Assert.assertEquals(ontologyTerm, ontologyTerm);
        Assert.assertEquals(ontologyTerm, ontologyTerm3);
        Assert.assertFalse(ontologyTerm.equals(ontologyTerm2));
        Assert.assertFalse(ontologyTerm.equals(ontologyTerm4));
    }

    /**
	 *
	 */
    @Test
    public void hashCodeTest() {
        Assert.assertTrue(ontologyTerm.hashCode() == ontologyTerm.hashCode());
        Assert.assertTrue(ontologyTerm.hashCode() == ontologyTerm3.hashCode());
        Assert.assertFalse(ontologyTerm.hashCode() == ontologyTerm2.hashCode());
        Assert.assertFalse(ontologyTerm.hashCode() == ontologyTerm4.hashCode());
    }

    /**
	 *
	 */
    @Test
    public void compareTo() {
        Assert.assertTrue(ontologyTerm.compareTo(ontologyTerm) == 0);
        Assert.assertTrue(ontologyTerm.compareTo(ontologyTerm3) == 0);
        Assert.assertTrue(ontologyTerm.compareTo(ontologyTerm2) > 0);
        Assert.assertTrue(ontologyTerm.compareTo(ontologyTerm4) < 0);
    }
}
