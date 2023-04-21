package net.sourceforge.buildprocess.autodeploy.model;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.Element;

/**
 * Represents the <code>logaccess</code> tag in the AutoDeploy XML
 * configuration file
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class LogAccess implements Serializable, Cloneable {

    /**
    * Generated Serial Version UID
    */
    private static final long serialVersionUID = 4123640669769410049L;

    private static final Log log = LogFactory.getLog(LogAccess.class);

    private String name;

    private String path;

    /**
    * Default constructor to create a <code>LogAccess</code>
    */
    public LogAccess() {
        log.debug("Create a LogAccess object");
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object clone() throws CloneNotSupportedException {
        LogAccess clone = new LogAccess();
        clone.setName(this.getName());
        clone.setPath(this.getPath());
        return clone;
    }

    /**
    * Transform the <code>LogAccess</code> POJO to a DOM element
    * 
    * @param document
    *           the core XML document
    * @return the DOM element
    */
    protected Element toDOMElement(CoreDocumentImpl document) {
        ElementImpl element = new ElementImpl(document, "logaccess");
        element.setAttribute("name", this.getName());
        element.setAttribute("path", this.getPath());
        return element;
    }
}
