package com.ontotext.wsmo4j.common;

import java.util.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.mediator.Mediator;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
public class TopEntityImpl extends EntityImpl implements TopEntity {

    private String wsmlVariant;

    private Namespace defaultNS;

    private LinkedHashMap namespaces;

    private LinkedHashMap mediators;

    private LinkedHashMap ontologies;

    public TopEntityImpl(Identifier id) {
        super(id);
        namespaces = new LinkedHashMap();
        mediators = new LinkedHashMap();
        ontologies = new LinkedHashMap();
    }

    public String getWsmlVariant() {
        if (this.wsmlVariant == null) {
            return WSML.WSML_CORE;
        } else {
            return this.wsmlVariant;
        }
    }

    public void setWsmlVariant(String variant) {
        if (variant == null || variant.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.wsmlVariant = variant;
    }

    public Set listNamespaces() {
        return new LinkedHashSet(namespaces.values());
    }

    public void addNamespace(Namespace ns) {
        if (ns == null) {
            throw new IllegalArgumentException();
        }
        namespaces.put(ns.getPrefix(), ns);
    }

    public void removeNamespace(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        namespaces.remove(prefix);
    }

    public void removeNamespace(Namespace ns) {
        if (ns == null) {
            throw new IllegalArgumentException();
        }
        removeNamespace(ns.getPrefix());
    }

    public Namespace findNamespace(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        return (Namespace) namespaces.get(prefix);
    }

    public Namespace getDefaultNamespace() {
        return this.defaultNS;
    }

    public void setDefaultNamespace(Namespace ns) {
        this.defaultNS = ns;
    }

    /**
     * sets a default namespace for the container
     * @param iri IRI of namespace to set as default
     * @see #setDefaultNamespace(Namespace ns)
     */
    public void setDefaultNamespace(IRI iri) {
        this.defaultNS = new NamespaceImpl("", iri);
    }

    public void addMediator(Mediator mediator) {
        if (mediator == null) {
            throw new IllegalArgumentException();
        }
        mediators.put(mediator.getIdentifier(), mediator);
    }

    public void removeMediator(Mediator mediator) {
        if (mediator == null) {
            throw new IllegalArgumentException();
        }
        mediators.remove(mediator.getIdentifier());
    }

    public void removeMediator(IRI iri) {
        if (iri == null) {
            throw new IllegalArgumentException();
        }
        mediators.remove(iri);
    }

    public Set listMediators() {
        return new LinkedHashSet(mediators.values());
    }

    public void addOntology(Ontology ontology) {
        if (ontology == null) {
            throw new IllegalArgumentException();
        }
        ontologies.put(ontology.getIdentifier(), ontology);
    }

    public void removeOntology(IRI iri) {
        if (iri == null) {
            throw new IllegalArgumentException();
        }
        ontologies.remove(iri);
    }

    public void removeOntology(Ontology ontology) {
        if (ontology == null) {
            throw new IllegalArgumentException();
        }
        ontologies.remove(ontology.getIdentifier());
    }

    public Set listOntologies() {
        return new LinkedHashSet(ontologies.values());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || false == object instanceof TopEntity) {
            return false;
        }
        TopEntity entity = (TopEntity) object;
        if (wsmlVariant != null) {
            if (!wsmlVariant.equals(entity.getWsmlVariant())) {
                return false;
            }
        } else if (entity.getWsmlVariant() != null) {
            return false;
        }
        if (defaultNS != null) {
            if (!defaultNS.equals(entity.getDefaultNamespace())) {
                return false;
            }
        } else if (entity.getDefaultNamespace() != null) {
            return false;
        }
        return listNamespaces().equals(entity.listNamespaces()) && listMediators().equals(entity.listMediators()) && listOntologies().equals(entity.listOntologies()) && super.equals(entity);
    }
}
