package name.levering.ryan.sparql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import name.levering.ryan.sparql.common.GraphStatement;
import name.levering.ryan.sparql.common.Resource;
import name.levering.ryan.sparql.common.SPARQLConstants;
import name.levering.ryan.sparql.common.SPARQLValueFactory;
import name.levering.ryan.sparql.common.URI;
import name.levering.ryan.sparql.common.Value;
import name.levering.ryan.sparql.common.impl.InternalSPARQLValueFactory;
import name.levering.ryan.sparql.io.RDFHandler;
import name.levering.ryan.sparql.io.RDFHandlerException;

/**
 * This parser is responsible for handling the RDF input of a super manifest
 * file that specifies other manifest files to aggregate. It does it's work by
 * flattening the RDF structure into Maps and then rebuilding objects from the
 * Maps.
 * 
 * @author Ryan Levering
 * @version 1.0
 */
public class TestManifestParser implements RDFHandler {

    public static final URI MANIFEST_ENTRIES;

    public static final URI MANIFEST_NAME;

    public static final URI MANIFEST_FILE;

    static {
        String SPARQL_MANIFEST_NS = "http://ryan.levering.name/project/sparql/manifest#";
        SPARQLValueFactory factory = new InternalSPARQLValueFactory();
        MANIFEST_ENTRIES = factory.createURI(SPARQL_MANIFEST_NS + "entries");
        MANIFEST_NAME = factory.createURI(SPARQL_MANIFEST_NS + "name");
        MANIFEST_FILE = factory.createURI(SPARQL_MANIFEST_NS + "manifest");
    }

    private Collection manifests = null;

    private Resource startNode = null;

    private Map names = null;

    private Map manifestFiles = null;

    private Map linkedList = null;

    private Map entryNodes = null;

    /**
     * Creates a new TestManifestParser.
     */
    public TestManifestParser() {
    }

    /**
     * Initializes all the data variables since we have a new RDF file.
     */
    public void startRDF() throws RDFHandlerException {
        manifests = new ArrayList();
        startNode = null;
        names = new HashMap();
        manifestFiles = new HashMap();
        linkedList = new HashMap();
        entryNodes = new HashMap();
    }

    /**
     * Builds the test case list from the maps of object relationships. Since we
     * know we have all the statements, the graph should be complete. Follow the
     * object hierarchies to build each test case object from the values in the
     * node maps.
     */
    public void endRDF() throws RDFHandlerException {
        Resource start = startNode;
        while (start != null) {
            Resource entryNode = (Resource) entryNodes.get(start);
            SPARQLManifest manifest = new SPARQLManifest();
            manifest.setName(names.get(entryNode).toString());
            manifest.setManifestFile(manifestFiles.get(entryNode).toString());
            this.manifests.add(manifest);
            start = (Resource) linkedList.get(start);
        }
    }

    /**
     * Does nothing.
     * 
     * @param prefix ignored
     * @param namespace ignored
     */
    public void handleNamespace(String prefix, String namespace) throws RDFHandlerException {
    }

    /**
     * Assigns the subject and object to a map depending on their predicate
     * value.
     * 
     * @param statement the statement to take the objects from
     */
    public void handleStatement(GraphStatement statement) throws RDFHandlerException {
        Value subject = statement.getSubject();
        URI predicate = statement.getPredicate();
        Value object = statement.getObject();
        if (predicate.equals(MANIFEST_ENTRIES)) {
            startNode = (Resource) object;
        } else if (predicate.equals(MANIFEST_NAME)) {
            this.names.put(subject, object);
        } else if (predicate.equals(MANIFEST_FILE)) {
            this.manifestFiles.put(subject, object);
        } else if (predicate.equals(SPARQLConstants.LIST_REST)) {
            if (!object.equals(SPARQLConstants.LIST_NIL)) {
                this.linkedList.put(subject, object);
            }
        } else if (predicate.equals(SPARQLConstants.LIST_FIRST)) {
            this.entryNodes.put(subject, object);
        }
    }

    /**
     * Gets a collection of all the manifest files found in this super-manifest
     * file.
     * 
     * @param a collection of SPARQLManifest objects
     */
    public Collection getManifests() {
        return this.manifests;
    }

    /**
     * Does nothing.
     * 
     * @param comment ignored
     */
    public void handleComment(String comment) throws RDFHandlerException {
    }
}
