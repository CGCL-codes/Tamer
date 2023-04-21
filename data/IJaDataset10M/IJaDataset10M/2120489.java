package org.stlab.xd.registry;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.stlab.xd.manager.ISource;
import org.stlab.xd.registry.cache.ODPRegistryCacheException;
import org.stlab.xd.registry.cache.ODPRegistryCacheManager;
import org.stlab.xd.registry.cache.URIUnresolvableException;

public class ODPRegistrySource implements ISource {

    public static final String SOURCE_ID = "$$$$" + XDRegistryPlugin.PLUGIN_ID;

    public OWLOntology getOntologyFromRegistry(IRI ontologyIRI) {
        OWLOntology ontology = null;
        try {
            ontology = ODPRegistryCacheManager.getOntology(ontologyIRI.toURI());
        } catch (URIUnresolvableException e) {
            throw new RuntimeException("Unreachable location: " + ontologyIRI.toURI(), e);
        } catch (ODPRegistryCacheException e) {
            throw new RuntimeException("Cannot retrieve ontology: " + ontologyIRI.toURI(), e);
        }
        return ontology;
    }

    @Override
    public OWLOntology getOntology(IRI ontologyIRI) {
        return getOntologyFromRegistry(ontologyIRI);
    }

    @Override
    public String getSourceID() {
        return SOURCE_ID;
    }

    @Override
    public String getSourceName() {
        return "ODP Registries";
    }

    @Override
    public String getSourceProvider() {
        return XDRegistryPlugin.PLUGIN_ID;
    }

    @Override
    public OWLOntologyManager getOWLOntologyManager() {
        return ODPRegistryCacheManager.getManager();
    }
}
