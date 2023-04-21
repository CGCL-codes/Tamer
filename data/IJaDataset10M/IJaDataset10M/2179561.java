package org.personalsmartspace.pss_sm_api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestOWLSSparqlParser {

    private static final String BASE_OWL = "BASE <http://www.owl-ontologies.com/Ontology1245756153.owl> ";

    private static final String OWLS_FILE_URI = "/TestPersistService.owl";

    private static final String ALL_NODES_QUERY = BASE_OWL + "SELECT * WHERE { ?x ?y ?z.}";

    private static OWLSSparqlParser parser;

    private String owlsUrl;

    @BeforeClass
    public static void runBeforeEveryTest() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        owlsUrl = this.getClass().getResource(OWLS_FILE_URI).toString();
        assertNotNull(owlsUrl);
        System.out.println("OWL-S URL : " + owlsUrl);
        parser = new OWLSSparqlParser(owlsUrl);
    }

    @After
    public void tearDown() throws Exception {
        owlsUrl = null;
        parser = null;
    }

    @Test
    public void testexecuteQuery() throws Exception {
        parser.executeQuery(ALL_NODES_QUERY);
    }
}
