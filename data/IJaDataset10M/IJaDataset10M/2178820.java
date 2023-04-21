package org.ontoware.rdf2go.model.impl;

import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.ModelRemovePatterns;
import org.ontoware.rdf2go.model.TriplePattern;
import org.ontoware.rdf2go.model.node.NodeOrVariable;
import org.ontoware.rdf2go.model.node.ResourceOrVariable;
import org.ontoware.rdf2go.model.node.UriOrVariable;

/**
 * The implementation first searches for all matching triples, copies them to
 * memory and then removes them. This is very inefficient. Please override!
 * 
 * @author voelkel
 * 
 */
public abstract class AbstractModelRemovePatterns extends AbstractModelAddRemove implements ModelRemovePatterns {

    /**
     * 
     */
    private static final long serialVersionUID = 6919483551303893410L;

    @Override
    public void removeStatements(TriplePattern triplePattern) throws ModelRuntimeException {
        ModelAddRemoveMemoryImpl toBeRemoved = new ModelAddRemoveMemoryImpl();
        toBeRemoved.addAll(this.findStatements(triplePattern));
        this.removeAll(toBeRemoved.iterator());
    }

    @Override
    public void removeStatements(ResourceOrVariable subject, UriOrVariable predicate, NodeOrVariable object) throws ModelRuntimeException {
        ModelAddRemoveMemoryImpl toBeRemoved = new ModelAddRemoveMemoryImpl();
        toBeRemoved.addAll(this.findStatements(subject, predicate, object));
        this.removeAll(toBeRemoved.iterator());
    }
}
