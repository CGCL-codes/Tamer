package eu.soa4all.reasoner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.deri.wsmo4j.io.parser.rdf.RDFParser;
import org.omwg.ontology.Ontology;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.sti2.wsmo4j.factory.WsmlFactoryContainer;
import org.wsml.reasoner.api.LPReasoner;
import org.wsml.reasoner.api.inconsistency.InconsistencyException;
import org.wsml.reasoner.impl.DefaultWSMLReasonerFactory;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.FactoryContainer;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;
import at.sti2.semanticspaces.api.ISemanticSpace;
import at.sti2.semanticspaces.api.ISpaceModel;
import at.sti2.semanticspaces.api.exceptions.SemanticSpaceException;
import at.sti2.semanticspaces.impl.SemanticSpaceImpl;
import at.sti2.semanticspaces.impl.model.SpaceModelType;
import eu.soa4all.reasoner.Config;
import eu.soa4all.reasoner.util.ontologies.LocalOntologyLoader;
import eu.soa4all.reasoner.util.ontologies.OntologyLoader;
import eu.soa4all.reasoner.util.ontologies.OntologyNamespaceExtractor;

/**
 * Factory that returns initialized LP Reasoners.
 */
public class WsmlReasonerFactory {

    private static Logger logger = Logger.getLogger(WsmlReasonerFactory.class);

    private WsmlReasonerFactory() {
    }

    public static LPReasoner getFreshReasoner(Config config) {
        logger.debug("Creating new LPReasoner");
        List<Ontology> localOntologies = readOntologies(config.localOntologiesDirectory);
        Set<Ontology> ontologies = new HashSet<Ontology>(localOntologies);
        LPReasoner lpReasoner = DefaultWSMLReasonerFactory.getFactory().createCoreReasoner(null);
        try {
            lpReasoner.registerOntologies(ontologies);
        } catch (InconsistencyException e) {
            logger.error("Can not register ontologies with the reasoner.", e);
            return null;
        }
        logger.info("Returning initialized Reasoner.");
        return lpReasoner;
    }

    public static LPReasoner getFreshSpacesReasoner(Config config) {
        logger.debug("Creating new LPReasoner attached to Spaces");
        Collection<File> ontologyFiles = LocalOntologyLoader.listFiles(new File(config.semanticSpacesLocalOntoDir), true);
        Set<Ontology> ontologies = new HashSet<Ontology>();
        LPReasoner lpReasoner = DefaultWSMLReasonerFactory.getFactory().createCoreReasoner(null);
        try {
            ISemanticSpace semSpaceImpl = new SemanticSpaceImpl();
            semSpaceImpl.createSpace(new URIImpl("http://someURI.com#test"), SpaceModelType.LocalRepository);
            System.out.println(semSpaceImpl.listSpaces());
            ISpaceModel model = semSpaceImpl.getSpace(new URIImpl("http://someURI.com#test"));
            for (File file : ontologyFiles) {
                if (file.getAbsolutePath().contains(".svn") == true) {
                    continue;
                }
                String filename = file.getAbsolutePath();
                if (filename.endsWith(".xml")) {
                    logger.info("Loading file : " + filename);
                    Model tempModel = RDF2Go.getModelFactory().createModel();
                    tempModel.open();
                    tempModel.readFrom(new FileInputStream(file));
                    model.addModel(tempModel);
                }
            }
            StringWriter dump = new StringWriter();
            model.writeTo(dump, org.ontoware.rdf2go.model.Syntax.Turtle);
            FactoryContainer container = new WsmlFactoryContainer();
            Parser rdfParser = new RDFParser(org.deri.wsmo4j.io.parser.rdf.RDFParser.Syntax.TURTLE, container);
            Ontology importedOntology = (Ontology) rdfParser.parse(dump.getBuffer())[0];
            ontologies.add(importedOntology);
            List<Ontology> localOntologies = readOntologies(config.localOntologiesDirectory);
            ontologies.addAll(localOntologies);
            lpReasoner.registerOntologies(ontologies);
        } catch (SemanticSpaceException e) {
            e.printStackTrace();
        } catch (ModelRuntimeException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (InvalidModelException e) {
            e.printStackTrace();
        } catch (InconsistencyException e) {
            e.printStackTrace();
        }
        return lpReasoner;
    }

    private static List<Ontology> readOntologies(String localOntologyDirectory) {
        if ((localOntologyDirectory == null) || (localOntologyDirectory.isEmpty())) return new ArrayList<Ontology>();
        LocalOntologyLoader loader = new LocalOntologyLoader();
        try {
            loader.load(localOntologyDirectory);
        } catch (Exception e) {
            logger.error("Error reading from local ontology directory " + localOntologyDirectory, e);
        }
        return loader.getOntologies();
    }

    private static Set<Ontology> loadServiceOntologies(Ontology servicesOntology) {
        Set<Ontology> result = new HashSet<Ontology>();
        OntologyNamespaceExtractor extractor = new OntologyNamespaceExtractor();
        Set<URI> namespaces = extractor.extractNamespaces(servicesOntology);
        OntologyLoader loader = new OntologyLoader();
        for (URI namespace : namespaces) {
            Ontology ontology = loader.loadUnknownOntology(namespace.toString());
            if (ontology != null) result.add(ontology);
        }
        return result;
    }
}
