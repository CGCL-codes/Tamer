package uk.co.ordnancesurvey.rabbitparser.owlapiconverter.testinfrastructure;

import java.net.URISyntaxException;
import java.net.URL;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import uk.ac.manchester.cs.owl.OWLOntologyManagerImpl;

/**
 * Provides method for loading ontology fixturess
 * 
 * @author rdenaux
 * 
 */
public class OntologyFixtureFactory {

    public OWLOntology loadOntologyFromClassPath(IOntologyFixture aOntFixture) {
        OWLOntologyManager ontManager = OWLManager.createOWLOntologyManager();
        return loadOntologyFromClassPath(ontManager, aOntFixture);
    }

    /**
     * Loads an ontology from a class path resource using an existing
     * {@link RooModelManager}.
     * 
     * @param aCMM
     * @param aClassPathOfOntology
     * @return
     */
    public OWLOntology loadOntologyFromClassPath(OWLOntologyManager aOntManager, IOntologyFixture aOntologyFixture) {
        OWLOntology result = null;
        final ClassLoader cl = getClass().getClassLoader();
        final URL res = cl.getResource(aOntologyFixture.getClassPath());
        assert res != null : "Couldn't read " + aOntologyFixture;
        try {
            result = aOntManager.loadOntologyFromPhysicalURI(res.toURI());
        } catch (final OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
