package de.fzi.mappso.align;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import de.fzi.kadmos.api.Alignment;
import de.fzi.kadmos.api.IncompatibleAlignmentsException;
import de.fzi.kadmos.api.algorithm.AlignmentAlgorithmException;
import de.fzi.kadmos.parser.AlignmentParser;
import de.fzi.kadmos.parser.AlignmentParserException;
import de.fzi.kadmos.parser.impl.INRIAFormatParser;
import de.fzi.mappso.align.MapPSOAlignmentAlgorithm;

/**
 * Test to verify that the algorithm runs. There is no test about quality or anything else
 * but the fact that the algorithm runs without errors.
 * 
 * @author Juergen Bock (bock@fzi.de)
 *
 */
public class TracerBulletTest {

    private static File PARTIAL_ALIGNMENT;

    private static OWLOntology ontology1;

    private static OWLOntology ontology2;

    private MapPSOAlignmentAlgorithm algo;

    @BeforeClass
    public static void setUpBeforeClass() throws OWLOntologyCreationException, URISyntaxException {
        PropertyConfigurator.configure("log4j.properties");
        IRI onto1IRI = IRI.create(TracerBulletTest.class.getResource("/ontologies/testOnto1.owl"));
        IRI onto2IRI = IRI.create(TracerBulletTest.class.getResource("/ontologies/testOnto2.owl"));
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ontology1 = manager.loadOntology(onto1IRI);
        ontology2 = manager.loadOntology(onto2IRI);
        PARTIAL_ALIGNMENT = new File(TracerBulletTest.class.getResource("/ontologies/partial.rdf").toURI());
        Config.getMainConfig().put("clusterMap", "4:l");
        Config.getMainConfig().put("iterations", "20");
    }

    @Before
    public void setUp() throws Exception {
        Config.getMainConfig().setDefaults();
        algo = new MapPSOAlignmentAlgorithm();
    }

    @Test
    public final void testSingleAlign() throws AlignmentAlgorithmException {
        Config.getMainConfig().put("clusterMap", "1:l");
        algo.align(ontology1, ontology2);
    }

    @Test
    public final void testAlign() throws AlignmentAlgorithmException {
        algo.align(ontology1, ontology2);
    }

    @Test
    public final void testPartialAlign() throws AlignmentAlgorithmException, IncompatibleAlignmentsException, AlignmentParserException, IllegalArgumentException, IOException {
        Config.getMainConfig().put("clusterMap", "1:l 1:p");
        AlignmentParser alignParser;
        alignParser = INRIAFormatParser.getInstance();
        alignParser.setCorrespondenceFactory(MapPSOCorrespondenceFactory.getInstance());
        alignParser.setOntology1(ontology1);
        alignParser.setOntology2(ontology2);
        Alignment partialAlignment = alignParser.parse(PARTIAL_ALIGNMENT);
        MapPSOAlignment align = algo.align(ontology1, ontology2, partialAlignment);
        assertTrue(align.getFitness() >= 0);
        assertTrue(align.getFitness() <= 1);
    }
}
