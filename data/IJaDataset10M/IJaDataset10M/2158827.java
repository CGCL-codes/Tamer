package hu.gbalage.owl.cms.core;

import hu.gbalage.owl.cms.core.impl.FileOntologyStorage;
import hu.gbalage.owl.cms.core.manager.CoreManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLOntologyStorageException;
import org.semanticweb.owl.model.UnknownOWLOntologyException;

/**
 * @author balage
 *
 */
public class CMSCore {

    private static CMSCore instance = null;

    public static CMSCore getInstance() {
        if (instance == null) instance = new CMSCore();
        return instance;
    }

    private OWLOntologyManager manager = CoreManager.getInstance().getOWLOntologyManager();

    private OWLReasoner reasoner = new Reasoner(manager);

    private OntologyStorage storage = new FileOntologyStorage(new File(Config.getProperty(Config.cache_basedir)));

    ;

    private CMSCore() {
        for (URI uri : storage.getAll()) {
            try {
                manager.loadOntology(storage.load(uri));
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
        }
        try {
            reasoner.loadOntologies(manager.getOntologies());
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
    }

    public void loadExternalOntology(URI uri) throws OWLOntologyCreationException, UnknownOWLOntologyException, OWLOntologyStorageException, FileNotFoundException {
        OWLOntology ontology = manager.loadOntologyFromPhysicalURI(uri);
        manager.saveOntology(ontology, storage.save(ontology.getURI()));
        try {
            reasoner.clearOntologies();
            reasoner.loadOntologies(manager.getOntologies());
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
    }
}
