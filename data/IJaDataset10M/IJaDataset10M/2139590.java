package org.qsari.effectopedia.ontologies;

import java.util.ArrayList;
import org.jdom.Element;
import org.jdom.Namespace;
import org.qsari.effectopedia.base.IndexedObject;
import org.qsari.effectopedia.base.XMLExportable;
import org.qsari.effectopedia.base.XMLImportable;
import org.qsari.effectopedia.defaults.DefaultTextProperties;

public class OntologyClass extends IndexedObject implements XMLImportable, XMLExportable, Cloneable {

    public OntologyClass(Ontology ontology) {
        super();
        autoSetId();
        this.ontology = ontology;
        ontology.add(this);
    }

    public OntologyClass(Ontology ontology, String name) {
        super();
        autoSetId();
        this.ontology = ontology;
        this.name = name;
        ontology.add(this);
    }

    public void setName(String name) {
        if (((name == null) && (this.name != null)) || (!name.equals(this.name))) {
            this.name = name;
        }
    }

    public String getName() {
        if (this.name == null) return DefaultTextProperties.INSTANCE.getDefault("Enzyme.name"); else return this.name;
    }

    public void cloneFieldsTo(OntologyClass clone) {
        clone = this;
    }

    public OntologyClass clone() {
        return this;
    }

    public String toString() {
        return getName();
    }

    public void loadFromXMLElement(Element element, Namespace namespace) {
        if (element != null) {
            super.loadFromXMLElement(element, namespace);
            name = element.getChildText("name", namespace);
        }
    }

    public Element storeToXMLElement(Element element, Namespace namespace, boolean visualAttributes) {
        super.storeToXMLElement(element, namespace, visualAttributes);
        Element cl = new Element("Class", namespace);
        cl.setAttribute("name", name, namespace);
        element.addContent(cl);
        return element;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    public ArrayList<OntologyInstance> getAllOntologyInstances() {
        return ontology.getAllOntologyInstances(this);
    }

    public ArrayList<OntologyInstances> getAllOntologyInstanceCollections() {
        return ontology.getAllOntologyInstanceCollections(this);
    }

    public OntologyInstance getInstance(String name) {
        return ontology.getInstance(this, name);
    }

    public long autoId() {
        return ontologyClassIDs++;
    }

    protected String name;

    protected long instanceIDs = 0;

    protected long collectionIDs = 0;

    protected Ontology ontology;

    protected static long ontologyClassIDs = 0;
}
