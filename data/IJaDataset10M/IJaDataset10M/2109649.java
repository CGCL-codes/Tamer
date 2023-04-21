package org.arastreju.core.ontology.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.arastreju.api.ontology.model.sn.ResourceNode;

/**
 * <p>
 * Contains a set of RDF-Query-Bindings in "Variable-To-ResourceNode" - Representations.
 * This Binding-Representation is realized with the help of a HashMap
 * </p>
 * 
 * @author Nils Bleisch
 *
 */
public class QueryResultBindingElem implements Iterable<ResourceNode>, Iterator<ResourceNode> {

    private final int INITIAL_CAPACITY = 3;

    private HashMap<String, ResourceNode> binding = new HashMap<String, ResourceNode>(INITIAL_CAPACITY);

    private Iterator<ResourceNode> it = null;

    public QueryResultBindingElem() {
        resetIterator();
    }

    public void add(String variable, ResourceNode res) {
        binding.put(variable, res);
        resetIterator();
    }

    public ResourceNode getResourceNode(String variable) {
        return binding.get(variable);
    }

    public Collection<String> getBindingNames() {
        return binding.keySet();
    }

    public Iterator<ResourceNode> iterator() {
        return this;
    }

    public boolean hasNext() {
        return it.hasNext();
    }

    public ResourceNode next() {
        return it.next();
    }

    public void remove() {
        it.remove();
        binding.clear();
        resetIterator();
    }

    public void resetIterator() {
        it = binding.values().iterator();
    }
}
