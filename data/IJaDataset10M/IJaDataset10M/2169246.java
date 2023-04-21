package fedora.services.sipcreator;

import java.util.Hashtable;
import java.util.Vector;
import beowulf.event.GraphListener;
import beowulf.model.Matrix;
import beowulf.model.graph.Edge;
import beowulf.model.graph.GraphModel;
import fedora.services.sipcreator.acceptor.SelectableEntryAcceptor;
import fedora.services.sipcreator.acceptor.SelectionAcceptor;
import fedora.services.sipcreator.metadata.Metadata;

public class ConversionRulesGraph implements GraphModel {

    private SelectableEntryAcceptor acceptor = new SelectionAcceptor(SelectableEntry.FULLY_SELECTED | SelectableEntry.PARTIALLY_SELECTED);

    private Vector nodes = new Vector();

    private Vector edges = new Vector();

    private Hashtable edgesFrom = new Hashtable();

    private Hashtable edgesTo = new Hashtable();

    private Hashtable nodesToIndices = new Hashtable();

    private Matrix edgesFromTo = new Matrix(new Vector());

    private int currentIndex = 0;

    private int getIndex(SelectableEntry entry) {
        Integer i = (Integer) nodesToIndices.get(entry);
        if (i == null) {
            i = new Integer(currentIndex++);
            nodesToIndices.put(entry, i);
            nodes.add(entry);
        }
        return i.intValue();
    }

    public ConversionRulesGraph(ConversionRules rules, SelectableEntry root) {
        constructGraph(rules, root, null, new Vector());
    }

    private void constructGraph(ConversionRules rules, SelectableEntry current, SelectableEntry parent, Vector ancestors) {
        if (parent != null) {
            addRelationships(rules, current, parent, "tree:parent");
            addRelationships(rules, parent, current, "tree:child");
        }
        for (int ctr = 0; ctr < ancestors.size(); ctr++) {
            addRelationships(rules, current, (SelectableEntry) ancestors.get(ctr), "tree:ancestor");
            addRelationships(rules, (SelectableEntry) ancestors.get(ctr), current, "tree:descendant");
        }
        Vector newAncestors = new Vector(ancestors);
        if (parent != null) {
            newAncestors.add(parent);
        }
        int childCount = current.getChildCount(acceptor);
        for (int ctr = 0; ctr < childCount; ctr++) {
            constructGraph(rules, current.getChildAt(ctr, acceptor), current, newAncestors);
        }
    }

    private void addRelationships(ConversionRules rules, SelectableEntry source, SelectableEntry destination, String relationshipType) {
        ConversionRules.ObjectTemplate sourceOT;
        ConversionRules.Relationship sourceRelationship;
        Metadata sourceMetadata;
        sourceOT = rules.getObjectTemplate("*");
        if (sourceOT != null) {
            for (int ctr1 = 0; ctr1 < sourceOT.getRelationshipCount(); ctr1++) {
                sourceRelationship = sourceOT.getRelationship(ctr1);
                if (isTarget(sourceRelationship, destination, relationshipType)) {
                    addEdge(source, destination, sourceRelationship.getName());
                }
            }
        }
        if (source.isDirectory()) {
            sourceOT = rules.getObjectTemplate("folder");
            if (sourceOT != null) {
                for (int ctr1 = 0; ctr1 < sourceOT.getRelationshipCount(); ctr1++) {
                    sourceRelationship = sourceOT.getRelationship(ctr1);
                    if (isTarget(sourceRelationship, destination, relationshipType)) {
                        addEdge(source, destination, sourceRelationship.getName());
                    }
                }
            }
        } else {
            sourceOT = rules.getObjectTemplate("file");
            if (sourceOT != null) {
                for (int ctr1 = 0; ctr1 < sourceOT.getRelationshipCount(); ctr1++) {
                    sourceRelationship = sourceOT.getRelationship(ctr1);
                    if (isTarget(sourceRelationship, destination, relationshipType)) {
                        addEdge(source, destination, sourceRelationship.getName());
                    }
                }
            }
        }
        for (int ctr1 = 0; ctr1 < source.getMetadataCount(); ctr1++) {
            sourceMetadata = source.getMetadata(ctr1);
            sourceOT = rules.getObjectTemplate(sourceMetadata.getType());
            if (sourceOT == null) continue;
            for (int ctr2 = 0; ctr2 < sourceOT.getRelationshipCount(); ctr2++) {
                sourceRelationship = sourceOT.getRelationship(ctr2);
                if (isTarget(sourceRelationship, destination, relationshipType)) {
                    addEdge(source, destination, sourceRelationship.getName());
                }
            }
        }
    }

    private boolean isTarget(ConversionRules.Relationship relationship, SelectableEntry entry, String relationType) {
        for (int ctr1 = 0; ctr1 < relationship.getTargetCount(); ctr1++) {
            if (!relationship.getTargetRelationship(ctr1).equals(relationType)) continue;
            if (relationship.getTargetNodeType(ctr1).equals("*")) {
                return true;
            }
            if (entry.isDirectory()) {
                if (relationship.getTargetNodeType(ctr1).equals("folder")) {
                    return true;
                }
            } else {
                if (relationship.getTargetNodeType(ctr1).equals("file")) {
                    return true;
                }
            }
            for (int ctr2 = 0; ctr2 < entry.getMetadataCount(); ctr2++) {
                Metadata parentMetadata = entry.getMetadata(ctr2);
                if (parentMetadata.getType().equals(relationship.getTargetNodeType(ctr1))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addEdge(SelectableEntry source, SelectableEntry destination, String cost) {
        int sourceIndex = getIndex(source);
        int destinationIndex = getIndex(destination);
        Edge edge = new Edge(source, destination, cost);
        Vector tempVector;
        edges.add(edge);
        tempVector = getEdgesFrom(source);
        if (tempVector == null) {
            tempVector = new Vector();
            edgesFrom.put(source, tempVector);
        }
        tempVector.add(edge);
        tempVector = getEdgesTo(destination);
        if (tempVector == null) {
            tempVector = new Vector();
            edgesTo.put(destination, tempVector);
        }
        tempVector.add(edge);
        tempVector = getEdgesFromTo(source, destination);
        if (tempVector == null) {
            tempVector = new Vector();
            edgesFromTo.add(sourceIndex, destinationIndex, tempVector);
        }
        tempVector.add(edge);
    }

    public void addGraphListener(GraphListener arg0) {
    }

    public void removeGraphListener(GraphListener arg0) {
    }

    public Vector getNodes() {
        return nodes;
    }

    public Vector getEdges() {
        return edges;
    }

    public Vector getEdgesFrom(Object node) {
        return (Vector) edgesFrom.get(node);
    }

    public Vector getEdgesTo(Object node) {
        return (Vector) edgesTo.get(node);
    }

    public Vector getEdgesFromTo(Object source, Object destination) {
        Integer sourceInteger = (Integer) nodesToIndices.get(source);
        Integer destinationInteger = (Integer) nodesToIndices.get(destination);
        if (sourceInteger == null || edgesFromTo.getRows() <= sourceInteger.intValue()) {
            return null;
        }
        if (destinationInteger == null || edgesFromTo.getCols() <= destinationInteger.intValue()) {
            return null;
        }
        return (Vector) edgesFromTo.get(sourceInteger.intValue(), destinationInteger.intValue());
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public boolean isDirected() {
        return true;
    }

    public boolean isSimple() {
        return true;
    }

    public boolean getUseDotEquals() {
        return true;
    }

    public String toString(boolean arg0) {
        return toString();
    }
}
