package net.sf.etl.parsers.internal.term_parser.flattened;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DirectedAcyclicGraph<E> {

    /** nodes that do not have children */
    final Set<Node<E>> leafs = new HashSet<Node<E>>();

    /** nodes that do not have parent */
    final Set<Node<E>> roots = new HashSet<Node<E>>();

    /**
	 * A map from objects to nodes
	 */
    final Map<E, Node<E>> objects = new HashMap<E, Node<E>>();

    /**
	 * This call represent directed acyclic graph and also provides a number of
	 * operations on it.
	 */
    public DirectedAcyclicGraph() {
        super();
    }

    /**
	 * Get node for object. If node already exists it is returned, otherwise it
	 * is created.
	 * 
	 * @param o
	 *            an object to be wrapped into node
	 * @return a new node
	 */
    public Node<E> getNode(E o) {
        Node<E> rc = objects.get(o);
        if (rc == null) {
            rc = new Node<E>(this, o);
            objects.put(o, rc);
            leafs.add(rc);
            roots.add(rc);
        }
        return rc;
    }

    /**
	 * This method minimizes amount of immediate parents and children for all
	 * nodes
	 */
    public void minimizeImmediate() {
        for (final Node<E> n : objects.values()) {
            n.minimizeImmediate();
        }
    }

    /**
	 * Rank comparator. It compares two nodes by rank. It is used to sort nodes
	 * topologically.
	 */
    private static Comparator<Node<?>> RANK_COMPARATOR = new Comparator<Node<?>>() {

        public int compare(Node<?> o1, Node<?> o2) {
            return o1.rank - o2.rank;
        }
    };

    /**
	 * @return sort nodes topologically
	 */
    public List<Node<E>> topologicalSortNodes() {
        final ArrayList<Node<E>> rc = new ArrayList<Node<E>>(objects.values());
        Collections.sort(rc, RANK_COMPARATOR);
        return rc;
    }

    /**
	 * This interface is used to get information from nodes.
	 * 
	 * @param <E>
	 *            element type
	 */
    public static final class Node<E> {

        /**
		 * Graph
		 */
        final DirectedAcyclicGraph<E> dag;

        /** node rank. */
        int rank = 0;

        /**
		 * A value wrapped into node
		 */
        private final E value;

        /**
		 * a collection of immediate parents
		 */
        private final Set<Node<E>> immediateParents = new HashSet<Node<E>>();

        /**
		 * a collection of immediate parents
		 */
        private final Set<Node<E>> allParents = new HashSet<Node<E>>();

        /**
		 * a collection of immediate children
		 */
        private final Set<Node<E>> immediateChildren = new HashSet<Node<E>>();

        /**
		 * a collection of all children
		 */
        private final Set<Node<E>> allChildren = new HashSet<Node<E>>();

        /**
		 * A constructor
		 * 
		 * @param dag
		 *            graph that holds the node
		 * 
		 * @param value
		 */
        public Node(DirectedAcyclicGraph<E> dag, E value) {
            this.dag = dag;
            this.value = value;
        }

        /**
		 * minimize number of immediate children and parents of all nodes. This
		 * is an optimization step in order not to consider indirect imports.
		 */
        void minimizeImmediate() {
            final HashSet<Node<E>> children = new HashSet<Node<E>>(immediateChildren);
            for (final Node<E> child : immediateChildren) {
                children.removeAll(child.allChildren);
            }
            immediateChildren.retainAll(children);
            final HashSet<Node<E>> parents = new HashSet<Node<E>>(immediateParents);
            for (final Node<E> parent : immediateParents) {
                parents.removeAll(parent.allParents);
            }
            immediateParents.retainAll(parents);
        }

        /**
		 * @param parent
		 *            a node to be checked
		 * @return true if parent is actually a parent node
		 */
        public boolean hasImmediateParent(E parent) {
            return hasImmediateParentNode(dag.getNode(parent));
        }

        /**
		 * @param parent
		 *            a node to be checked
		 * @return true if parent is actually a parent node
		 */
        public boolean hasImmediateParentNode(Node<E> parent) {
            return immediateParents.contains(parent);
        }

        /**
		 * @param child
		 *            a node to be checked
		 * @return true if child is actually an immediate child node
		 */
        public boolean hasImmediateChild(E child) {
            return immediateParents.contains(dag.getNode(child));
        }

        /**
		 * @param parent
		 *            a node to be checked
		 * @return true if parent is actually a parent node
		 */
        public boolean hasParent(E parent) {
            return hasParentNode(dag.getNode(parent));
        }

        /**
		 * @param parent
		 *            a node to be checked
		 * @return true if parent is actually a parent node
		 */
        public boolean hasParentNode(Node<E> parent) {
            return allParents.contains(parent);
        }

        /**
		 * Add parent node
		 * 
		 * @param parent
		 *            an new parent for this node
		 * @return true if node is added, false if adding node would have create
		 *         cycle in the graph
		 */
        public boolean addParent(E parent) {
            return addParentNode(dag.getNode(parent));
        }

        /**
		 * Add pair to the graph
		 * 
		 * @param parent
		 *            a parent node
		 * @param child
		 *            a child node
		 * @return true if pair was created or false if it would have created
		 *         cycle
		 */
        private boolean addPair(Node<E> parent, Node<E> child) {
            if (child == parent) {
                return false;
            } else if (child.allChildren.contains(parent)) {
                return false;
            } else if (child.immediateParents.contains(parent)) {
                return true;
            } else {
                dag.leafs.remove(parent);
                parent.immediateChildren.add(child);
                parent.allChildren.add(child);
                parent.allChildren.addAll(child.allChildren);
                for (final Node<E> grandParentNode : parent.allParents) {
                    grandParentNode.allChildren.add(child);
                    grandParentNode.allChildren.addAll(child.allChildren);
                }
                dag.roots.remove(child);
                child.immediateParents.add(parent);
                child.allParents.add(parent);
                child.allParents.addAll(parent.allParents);
                for (final Node<E> grandChildNode : child.allChildren) {
                    grandChildNode.allParents.add(parent);
                    grandChildNode.allParents.addAll(parent.allParents);
                }
                child.updateRank(parent.rank + 1);
                return true;
            }
        }

        /**
		 * Update rank
		 * 
		 * @param newRank
		 *            new rank that node should have
		 */
        private void updateRank(int newRank) {
            if (rank < newRank) {
                rank = newRank;
                for (final Node<E> n : immediateChildren) {
                    n.updateRank(rank + 1);
                }
            }
        }

        /**
		 * Add child node
		 * 
		 * @param child
		 *            a new child node
		 * @return true if node is added, false if adding node would have create
		 *         cycle in the graph
		 */
        public boolean addChild(E child) {
            return addPair(this, dag.getNode(child));
        }

        /**
		 * @return Returns the value.
		 */
        public E getValue() {
            return value;
        }

        /**
		 * @return iterator over immediate parents
		 */
        public Iterator<E> immediateParentsIterator() {
            return new NodeUnwrapIterator<E>(immediateParents.iterator());
        }

        /**
		 * Add parent node
		 * 
		 * @param parent
		 *            an new parent for this node
		 * @return true if node is added, false if adding node would have create
		 *         cycle in the graph
		 */
        public boolean addParentNode(Node<E> parent) {
            return addPair(parent, this);
        }

        /**
		 * @return a collection of immediate parents
		 */
        public Set<Node<E>> immedaiteParentNodes() {
            return Collections.unmodifiableSet(immediateParents);
        }
    }

    /**
	 * @return list of nodes sorted topologically (parents are first)
	 */
    public List<E> topologicalSortObjects() {
        final List<E> rc = new ArrayList<E>();
        for (final Node<E> n : topologicalSortNodes()) {
            rc.add(n.value);
        }
        return rc;
    }

    /**
	 * This iterator iterates over values contained in nodes.
	 * 
	 * @author const
	 * @param <E>
	 *            element type
	 */
    static class NodeUnwrapIterator<E> implements Iterator<E> {

        /** iterator over collection of nodes */
        final Iterator<Node<E>> i;

        /**
		 * A constructor from collection iterator
		 * 
		 * @param i
		 */
        public NodeUnwrapIterator(Iterator<Node<E>> i) {
            super();
            this.i = i;
        }

        /**
		 * {@inheritDoc}
		 */
        public void remove() {
            i.remove();
        }

        /**
		 * {@inheritDoc}
		 */
        public boolean hasNext() {
            return i.hasNext();
        }

        /**
		 * {@inheritDoc}
		 */
        public E next() {
            return i.next().getValue();
        }
    }

    /**
	 * This is basic definition gatherer algorithm implementation over DAG. A
	 * set of abstract methods is quite ad hoc, and it will be possibly refined
	 * later.
	 * 
	 * @author const
	 * @param <DefinitionHolder>
	 *            a holder node that holds definitions
	 * @param <DefinitionKey>
	 *            key that identifies definition within holder
	 * @param <Definition>
	 *            definition
	 */
    public abstract static class DefinitionGatherer<DefinitionHolder, DefinitionKey, Definition> {

        /**
		 * Gather definitions related to definition holders. The algorithm
		 * assumes that it has been already applied to all immediate parents of
		 * this node in topological sort order.
		 * 
		 * @param sourceNode
		 *            a node for which definitions will be gathered.
		 */
        public final void gatherDefinitions(DefinitionHolder sourceNode) {
            final Map<DefinitionKey, Definition> existingDefinitions = definitionMap(sourceNode);
            final Map<DefinitionKey, HashSet<Definition>> allDefinitions = new HashMap<DefinitionKey, HashSet<Definition>>();
            for (final Iterator<DefinitionHolder> i = getHolderNode(sourceNode).immediateParentsIterator(); i.hasNext(); ) {
                final DefinitionHolder parentHolder = i.next();
                for (Definition definitionFromParent : definitionMap(parentHolder).values()) {
                    final DefinitionKey definitionFromParentKey = definitionKey(definitionFromParent);
                    if (!existingDefinitions.containsKey(definitionFromParentKey)) {
                        HashSet<Definition> definitions = allDefinitions.get(definitionFromParentKey);
                        if (definitions == null) {
                            definitions = new HashSet<Definition>();
                            allDefinitions.put(definitionFromParentKey, definitions);
                        }
                        if (!definitions.contains(definitionFromParent)) {
                            for (final Iterator<Definition> k = definitions.iterator(); k.hasNext(); ) {
                                final Definition existingDefinition = k.next();
                                if (definitionNode(existingDefinition).hasParentNode(definitionNode(definitionFromParent))) {
                                    definitionFromParent = null;
                                    break;
                                } else if (definitionNode(definitionFromParent).hasParentNode(definitionNode(existingDefinition))) {
                                    k.remove();
                                }
                            }
                            if (definitionFromParent != null) {
                                definitions.add(definitionFromParent);
                            }
                        }
                    }
                }
            }
            for (final Map.Entry<DefinitionKey, HashSet<Definition>> e : allDefinitions.entrySet()) {
                final HashSet<Definition> v = e.getValue();
                if (v.size() != 1) {
                    reportDuplicates(sourceNode, e.getKey(), v);
                }
                existingDefinitions.put(e.getKey(), includingDefinition(sourceNode, v.iterator().next()));
            }
        }

        /**
		 * In case if definition is wrapped when it is added to the all
		 * definitions map, this method allows to find original definition in
		 * defining context.
		 * 
		 * @param def
		 *            a potentially wrapped definition
		 * @return an original definition that was wrapped.
		 */
        protected Definition originalDefinition(Definition def) {
            return def;
        }

        /**
		 * When object is included from parent holder, this callback method give
		 * subclasses a chance to perform an additional processing on the node
		 * or to replace it with derived node.
		 * 
		 * @param sourceHolder
		 *            a new holder for the definition
		 * @param object
		 *            an object to process
		 * @return a processed object
		 */
        protected Definition includingDefinition(DefinitionHolder sourceHolder, Definition object) {
            return object;
        }

        /**
		 * Report problem with definitions. The method checks if there is an
		 * actually conflict (for example if definitions are the same, there is
		 * no conflict). The method also has a chance to resolve conflict by
		 * removing objects from set.
		 * 
		 * @param sourceHolder
		 *            a source holder for definition.
		 * @param key
		 *            a key for which conflict exists
		 * @param duplicateNodes
		 *            a set of duplicate definitions
		 */
        protected abstract void reportDuplicates(DefinitionHolder sourceHolder, DefinitionKey key, HashSet<Definition> duplicateNodes);

        /**
		 * Get an DAG node for definition holder
		 * 
		 * @param definitionHolder
		 *            a definition holder
		 * @return a actual node that contains definition
		 */
        protected abstract Node<DefinitionHolder> getHolderNode(DefinitionHolder definitionHolder);

        /**
		 * Get the defining DAG node for definition
		 * 
		 * @param definition
		 *            a definition to examine
		 * @return the defining DAG node for definition
		 */
        protected abstract Node<DefinitionHolder> definitionNode(Definition definition);

        /**
		 * Get key of the definition
		 * 
		 * @param definition
		 *            a definition to examine
		 * @return the key that identifies definition
		 */
        protected abstract DefinitionKey definitionKey(Definition definition);

        /**
		 * Get the map that contains definition that are directly contained by
		 * definition holder object. The method updates the definition map After
		 * it stops working
		 * 
		 * @param holder
		 *            a holder object to examine
		 * @return the map of immediate definitions.
		 */
        protected abstract Map<DefinitionKey, Definition> definitionMap(DefinitionHolder holder);
    }

    /**
	 * Base class for import definition gatherer. It adds standard error
	 * reporting mechanism
	 * 
	 * @author const
	 * @param <DefinitionHolder>
	 *            a holder node that holds definitions
	 * @param <DefinitionKey>
	 *            key that identifies definition within holder
	 * @param <Definition>
	 *            definition
	 * @param <ImportedObject>
	 *            an object imported though definition
	 * 
	 */
    public abstract static class ImportDefinitionGatherer<DefinitionHolder, DefinitionKey, Definition, ImportedObject> extends DefinitionGatherer<DefinitionHolder, DefinitionKey, Definition> {

        /**
		 * {@inheritDoc}
		 */
        @Override
        protected final void reportDuplicates(DefinitionHolder sourceHolder, DefinitionKey key, HashSet<Definition> duplicateNodes) {
            ImportedObject importedObject = null;
            for (final Definition gi : duplicateNodes) {
                if (importedObject == null) {
                    importedObject = importedObject(gi);
                } else if (importedObject == importedObject(gi)) {
                } else {
                    reportDuplicateImportError(sourceHolder, key);
                    break;
                }
            }
        }

        /**
		 * This method is used to report duplicates
		 * 
		 * @param sourceHolder
		 *            definition holder node
		 * @param key
		 *            a key for which error happened
		 */
        protected abstract void reportDuplicateImportError(DefinitionHolder sourceHolder, DefinitionKey key);

        /**
		 * @param importDefinition
		 *            an import definition
		 * @return an object that is being imported by this definition
		 */
        protected abstract ImportedObject importedObject(Definition importDefinition);
    }
}
