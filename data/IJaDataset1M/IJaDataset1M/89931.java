package org.exist.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.exist.xmldb.XmldbURI;
import java.net.URISyntaxException;

/**
 * an ant task to create a empty collection.
 *
 * @author  peter.klotz@blue-elephant-systems.com
 */
public class XMLDBCreateTask extends AbstractXMLDBTask {

    private String collection = null;

    public void execute() throws BuildException {
        if (uri == null) {
            throw (new BuildException("you have to specify an XMLDB collection URI"));
        }
        registerDatabase();
        try {
            log("Get base collection: " + uri, Project.MSG_DEBUG);
            Collection base = DatabaseManager.getCollection(uri, user, password);
            if (base == null) {
                String msg = "Collection " + uri + " could not be found.";
                if (failonerror) {
                    throw (new BuildException(msg));
                } else {
                    log(msg, Project.MSG_ERR);
                }
            } else {
                Collection root = null;
                if (collection != null) {
                    log("Creating collection " + collection + " in base collection " + uri, Project.MSG_DEBUG);
                    root = mkcol(base, uri, collection);
                } else {
                    root = base;
                }
                log("Created collection " + root.getName(), Project.MSG_INFO);
            }
        } catch (XMLDBException e) {
            String msg = "XMLDB exception caught: " + e.getMessage();
            if (failonerror) {
                throw (new BuildException(msg, e));
            } else {
                log(msg, e, Project.MSG_ERR);
            }
        } catch (URISyntaxException e) {
            String msg = "URISyntaxException: " + e.getMessage();
            if (failonerror) {
                throw (new BuildException(msg, e));
            } else {
                log(msg, e, Project.MSG_ERR);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  collection
     */
    public void setCollection(String collection) {
        this.collection = collection;
    }

    private Collection mkcol(Collection root, String base, String relPath) throws XMLDBException, URISyntaxException {
        CollectionManagementService mgtService;
        Collection current = root;
        Collection c;
        XmldbURI baseUri = XmldbURI.xmldbUriFor(base);
        XmldbURI collPath = XmldbURI.xmldbUriFor(relPath);
        log("BASEURI=" + baseUri, Project.MSG_DEBUG);
        log("RELPATH=" + relPath, Project.MSG_DEBUG);
        XmldbURI[] segments = collPath.getPathSegments();
        for (XmldbURI segment : segments) {
            baseUri = baseUri.append(segment);
            log("Get collection " + baseUri, Project.MSG_DEBUG);
            c = DatabaseManager.getCollection(baseUri.toString(), user, password);
            if (c == null) {
                log("Create collection management service for collection " + current.getName(), Project.MSG_DEBUG);
                mgtService = (CollectionManagementService) current.getService("CollectionManagementService", "1.0");
                log("Create child collection " + segment);
                current = mgtService.createCollection(segment.toString());
                log("Created collection " + current.getName() + '.');
            } else {
                current = c;
            }
        }
        return (current);
    }
}
