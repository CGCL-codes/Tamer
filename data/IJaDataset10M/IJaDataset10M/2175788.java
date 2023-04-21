package se.kth.cid.identity.pathurn;

import se.kth.cid.identity.*;
import se.kth.cid.util.*;
import java.util.*;

/** This is a PathURNResolver that contains a table mapping paths
 *  to URIs.
 *
 *  @author Mikael Nilsson
 *  @version $Revision: 275 $
 */
public class TableResolver implements PathURNResolver {

    static class PathEntry {

        PathEntry(URI baseURI, MIMEType type) {
            this.baseURI = baseURI;
            this.type = type;
        }

        URI baseURI;

        MIMEType type;
    }

    class TableEntry {

        String path;

        Vector pathEntries;

        TableEntry(String path) {
            this.path = path;
            pathEntries = new Vector();
        }

        void addPathEntry(URI baseURI, MIMEType type) {
            String uri = baseURI.toString();
            pathEntries.add(new PathEntry(baseURI, type));
        }

        void addHits(Vector results, String file) {
            Iterator values = pathEntries.iterator();
            while (values.hasNext()) {
                PathEntry p = (PathEntry) values.next();
                try {
                    results.add(new ResolveResult(URIClassifier.parseURI(p.baseURI + file, null), path, p.baseURI, p.type));
                } catch (MalformedURIException e) {
                    Tracer.bug("Resulting URI was invalid:\n " + e.getMessage());
                }
            }
        }
    }

    /** The lookup table.
   *  Maps path (String) --> TableEntry.
   */
    Hashtable resolverTable;

    /** Constructs an empty TableResolver
   */
    public TableResolver() {
        resolverTable = new Hashtable();
    }

    /** Adds a path from the table.
   *
   *  @param path the patch to add.
   *  @param baseURI the base path of the patch
   *  @param type the MIME type for this base URI.
   */
    public void addPath(String path, URI baseURI, MIMEType type) {
        TableEntry tableEntry = (TableEntry) resolverTable.get(path);
        if (tableEntry == null) {
            tableEntry = new TableEntry(path);
            resolverTable.put(path, tableEntry);
        }
        tableEntry.addPathEntry(baseURI, type);
    }

    public ResolveResult[] resolve(PathURN urn) {
        Vector results = new Vector();
        String path = urn.getPath();
        int slash = path.lastIndexOf('/');
        String fragment = urn.getFragment();
        String file = "";
        if (fragment.length() != 0) file = "#" + fragment;
        while (slash != 0) {
            file = path.substring(slash) + file;
            path = path.substring(0, slash);
            TableEntry tableEntry = (TableEntry) resolverTable.get(path);
            if (tableEntry != null) tableEntry.addHits(results, file.substring(1));
            slash = path.lastIndexOf('/');
        }
        return (ResolveResult[]) results.toArray(new ResolveResult[results.size()]);
    }
}
