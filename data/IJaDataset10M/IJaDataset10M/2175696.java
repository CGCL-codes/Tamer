package uk.ac.ebi.ontocat.test;

import org.junit.BeforeClass;
import uk.ac.ebi.ontocat.bioportal.BioportalOntologyService;

public class BioportalServiceTest extends AbstractOntologyServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        os = new BioportalOntologyService();
        ONTOLOGY_ACCESSION = "1070";
    }
}
