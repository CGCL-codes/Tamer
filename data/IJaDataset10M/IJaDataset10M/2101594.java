package org.exist.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XUpdateQueryService;

/**
 * an ant task to update a collection or resource using XUpdate.
 *
 * @author  peter.klotz@blue-elephant-systems.com
 */
public class XMLDBXUpdateTask extends AbstractXMLDBTask {

    private String resource = null;

    private String commands = null;

    public void execute() throws BuildException {
        if (uri == null) {
            throw (new BuildException("You have to specify an XMLDB collection URI"));
        }
        log("XUpdate command is: " + commands, Project.MSG_DEBUG);
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
                XUpdateQueryService service = (XUpdateQueryService) base.getService("XUpdateQueryService", "1.0");
                if (resource != null) {
                    log("Updating resource: " + resource, Project.MSG_INFO);
                    Resource res = base.getResource(resource);
                    if (res == null) {
                        String msg = "Resource " + resource + " not found.";
                        if (failonerror) {
                            throw (new BuildException(msg));
                        } else {
                            log(msg, Project.MSG_ERR);
                        }
                    } else {
                        service.updateResource(resource, commands);
                    }
                } else {
                    log("Updating collection: " + base.getName(), Project.MSG_INFO);
                    service.update(commands);
                }
            }
        } catch (XMLDBException e) {
            String msg = "XMLDB exception during XUpdate: " + e.getMessage();
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
     * @param  resource
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }
}
