package net.sourceforge.buildprocess.autodeploy.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.Element;

/**
 * Represents the <code>configurationfile</code> tag in the AutoDeploy XML
 * configuration file
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class ConfigurationFile implements Serializable, Cloneable {

    /**
    * Generated Serial Version UID
    */
    private static final long serialVersionUID = -1898011382653346087L;

    private static final Log log = LogFactory.getLog(ConfigurationFile.class);

    private String name;

    private String uri;

    private String path;

    private boolean active;

    private boolean blocker;

    private LinkedList mappings;

    /**
    * Default constructor
    */
    public ConfigurationFile() {
        log.debug("Create a ConfigurationFile object");
        this.mappings = new LinkedList();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getBlocker() {
        return this.blocker;
    }

    public void setBlocker(boolean blocker) {
        this.blocker = blocker;
    }

    /**
    * Add a new <code>Mapping</code> in the <code>ConfigurationFile</code>
    * mappings container
    * 
    * @param mapping
    *           the <code>Mapping</code> to add
    */
    public void addMapping(Mapping mapping) throws ModelObjectAlreadyExistsException {
        if (this.getMapping(mapping.getKey()) != null) {
            log.error("Mapping key already exists in the configuration file");
            throw new ModelObjectAlreadyExistsException("Mapping key already exists in the configuration file");
        }
        this.mappings.add(mapping);
    }

    /**
    * Get the <code>Mapping</code> list in the <code>ConfigurationFile</code>
    * mappings container
    * 
    * @return the <code>Mapping</code> list
    */
    public List getMappings() {
        return this.mappings;
    }

    /**
    * Overwrite the <code>Mapping</code> list in the
    * <code>ConfigurationFile</code> mappings container
    * 
    * @param mappings
    *           the new <code>Mapping</code> list
    */
    public void setMappings(LinkedList mappings) {
        this.mappings = mappings;
    }

    /**
    * Get the <code>Mapping</code> identified by a given key in the
    * <code>ConfigurationFile</code> mappings container
    * 
    * @param key
    *           the <code>Mapping</code> key
    * @return the <code>Mapping</code> found or null if no
    *         <code>Mapping</code> found
    */
    public Mapping getMapping(String key) {
        for (Iterator mappingIterator = this.getMappings().iterator(); mappingIterator.hasNext(); ) {
            Mapping mapping = (Mapping) mappingIterator.next();
            if (mapping.getKey().equals(key)) {
                return mapping;
            }
        }
        return null;
    }

    public Object clone() throws CloneNotSupportedException {
        ConfigurationFile clone = new ConfigurationFile();
        clone.setName(this.getName());
        clone.setUri(this.getUri());
        clone.setPath(this.getPath());
        clone.setActive(this.getActive());
        clone.setBlocker(this.getBlocker());
        for (Iterator mappingIterator = this.mappings.iterator(); mappingIterator.hasNext(); ) {
            Mapping mapping = (Mapping) mappingIterator.next();
            clone.mappings.add((Mapping) mapping.clone());
        }
        return clone;
    }

    /**
    * Transform the <code>ConfigurationFile</code> POJO to a DOM element
    * 
    * @param document
    *           the core XML document
    * @return the DOM element
    */
    protected Element toDOMElement(CoreDocumentImpl document) {
        ElementImpl element = new ElementImpl(document, "configurationfile");
        element.setAttribute("name", this.getName());
        element.setAttribute("uri", this.getUri());
        element.setAttribute("path", this.getPath());
        element.setAttribute("active", new Boolean(this.getActive()).toString());
        element.setAttribute("blocker", new Boolean(this.getBlocker()).toString());
        ElementImpl mappings = new ElementImpl(document, "mappings");
        for (Iterator mappingIterator = this.getMappings().iterator(); mappingIterator.hasNext(); ) {
            Mapping mapping = (Mapping) mappingIterator.next();
            mappings.appendChild(mapping.toDOMElement(document));
        }
        element.appendChild(mappings);
        return element;
    }
}
