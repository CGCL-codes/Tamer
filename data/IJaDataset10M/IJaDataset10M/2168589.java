package eu.planets_project.ifr.core.wdt.impl.registry;

import javax.xml.namespace.QName;

/**
	*
	* @author Rainer Schmidt
	*
	* represents a service object that can be stored in a registry
	* might be removed by a common class later
	*/
public class Service {

    private String id = null;

    private String name = null;

    private String namespace = null;

    private String endpoint = null;

    private String dsc = null;

    private String category;

    private QName qName;

    public Service() {
    }

    public Service(String id, String name, String namespace, String endpoint, String category, QName qname) {
        this.id = id;
        this.name = name;
        this.namespace = namespace;
        this.endpoint = endpoint;
        this.category = category;
        this.qName = qname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setDescription(String dsc) {
        this.dsc = dsc;
    }

    public String getDescription() {
        return dsc;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setQName(QName qName) {
        this.qName = qName;
    }

    public QName getQName() {
        return qName;
    }

    public String toString() {
        return "service [id=" + id + ", name=" + name + ", endpoint=" + endpoint + ", qname=" + qName + "]";
    }
}
