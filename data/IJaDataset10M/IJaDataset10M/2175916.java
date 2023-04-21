package com.ontotext.wsmo4j.ontology;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.omwg.ontology.Attribute;
import org.omwg.ontology.Concept;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import com.ontotext.wsmo4j.common.EntityImpl;

public class AttributeImpl extends EntityImpl implements Attribute {

    private Concept domain;

    private LinkedHashSet ranges;

    private int minCardinality = 0;

    private int maxCardinality = Integer.MAX_VALUE;

    private boolean constraining;

    public AttributeImpl(Identifier thisID, Concept domain) {
        super(thisID);
        this.domain = domain;
        ranges = new LinkedHashSet();
    }

    public Concept getConcept() throws SynchronisationException {
        return this.domain;
    }

    public boolean isConstraining() {
        return constraining;
    }

    public void setConstraining(boolean constraining) {
        this.constraining = constraining;
    }

    public int getMinCardinality() {
        return this.minCardinality;
    }

    public void setMinCardinality(int min) {
        this.minCardinality = min;
    }

    public int getMaxCardinality() {
        return this.maxCardinality;
    }

    public void setMaxCardinality(int max) {
        this.maxCardinality = max;
    }

    public Set listTypes() {
        return Collections.unmodifiableSet(ranges);
    }

    public void addType(Object type) throws InvalidModelException {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        ranges.add(type);
    }

    public void removeType(Object type) throws InvalidModelException {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        ranges.remove(type);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || false == object instanceof Attribute) {
            return false;
        }
        Attribute otherAttr = (Attribute) object;
        return domain.equals(otherAttr.getConcept()) && minCardinality == otherAttr.getMinCardinality() && maxCardinality == otherAttr.getMaxCardinality() && constraining == otherAttr.isConstraining() && ranges.equals(otherAttr.listTypes()) && super.equals(otherAttr);
    }
}
