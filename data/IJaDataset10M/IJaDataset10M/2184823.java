package org.tripcom.security.stubs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openrdf.model.Graph;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.vocabulary.RDF;
import org.tripcom.security.util.RDFUtil;
import org.tripcom.security.util.Resources;
import org.tripcom.security.util.Util;
import org.tripcom.security.vocabularies.TSOnto;

/**
 * Utility functions for handling metadata information.
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public final class MetadataUtil {

    /** The shared log object. */
    private static Log log = LogFactory.getLog(MetadataUtil.class);

    /**
	 * Private constructor prevents object instantiation.
	 */
    private MetadataUtil() {
    }

    /**
	 * Load all the RDF hierarchy information contained in the classpath
	 * resources matching the filter.
	 * 
	 * @param classpathFilter a regex specifying which resources should be
	 *            considered; if null, all the Turtle resources will be
	 *            considered.
	 * @return the RDF hierarchy data loaded from the classpath.
	 */
    public static Map<String, Collection<String>> loadHierarchy(String classpathFilter) {
        Graph graph = new GraphImpl();
        for (String resourceName : Resources.retrieveClasspathResourceNames(classpathFilter != null ? classpathFilter : ".*\\.ttl")) {
            System.out.println("*** " + resourceName + " ***");
            graph.addAll(RDFUtil.loadRDFFromClasspath(resourceName, null));
        }
        return parseHierarchy(graph);
    }

    /**
	 * Parse the RDF hierarchy data provided and return a hierarchy map
	 * associating a space to its children.
	 * 
	 * @param graph the RDF hierarchy data to parse (not null).
	 * @return a hierarchy map associating a space with its children.
	 */
    public static Map<String, Collection<String>> parseHierarchy(Graph graph) {
        if (graph == null) {
            throw new NullPointerException();
        }
        Map<String, Collection<String>> result;
        result = new HashMap<String, Collection<String>>();
        for (Statement stmt : Util.iterable(graph.match(null, RDF.TYPE, TSOnto.Space))) {
            if (!(stmt.getSubject() instanceof URI)) {
                if (log.isWarnEnabled()) {
                    log.warn("Space is a blank node: " + stmt.getSubject());
                }
                continue;
            }
            URI space = (URI) stmt.getSubject();
            String spaceURL = space.toString();
            Collection<String> children = new HashSet<String>();
            for (Statement stmt2 : Util.iterable(graph.match(space, TSOnto.hasSubspace, null))) {
                if (!(stmt2.getObject() instanceof URI)) {
                    if (log.isWarnEnabled()) {
                        log.warn("Subspace is a blank node: " + stmt2.getSubject());
                    }
                    continue;
                }
                children.add(stmt2.getObject().toString());
            }
            for (Statement stmt2 : Util.iterable(graph.match(null, TSOnto.isSubspaceOf, space))) {
                if (!(stmt2.getSubject() instanceof URI)) {
                    if (log.isWarnEnabled()) {
                        log.warn("Subspace is a blank node: " + stmt2.getSubject());
                    }
                    continue;
                }
                children.add(stmt2.getSubject().toString());
            }
            if (!children.isEmpty()) {
                result.put(spaceURL, Collections.unmodifiableCollection(children));
            }
        }
        return result;
    }

    /**
	 * Return the URLs of the root spaces contained in the specified hierarchy.
	 * 
	 * @param hierarchy a map associating each space with its children (not
	 *            null).
	 * @return a set with the URLs of all the root spaces of the hierarchy.
	 */
    public static Set<String> retrieveRootSpaces(Map<String, Collection<String>> hierarchy) {
        if (hierarchy == null) {
            throw new NullPointerException();
        }
        Set<String> result = new HashSet<String>(hierarchy.keySet());
        for (Map.Entry<String, Collection<String>> entry : hierarchy.entrySet()) {
            result.removeAll(entry.getValue());
        }
        return result;
    }
}
