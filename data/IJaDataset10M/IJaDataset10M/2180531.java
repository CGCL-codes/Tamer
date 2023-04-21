package relex.frame;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Represents a group of child nodes that are connected by OR operator(s)
 */
class OrNode extends ASTNode {

    ArrayList<ASTNode> childNodeMatches;

    String print() {
        if (children.isEmpty()) return null;
        return " OR:" + children;
    }

    HashSet<String> getRelationNames() {
        if (childNodeMatches == null || childNodeMatches.isEmpty()) {
            return null;
        }
        HashSet<String> relationNames = new HashSet<String>(3);
        for (ASTNode childNode : childNodeMatches) {
            HashSet<String> childNodeNames = childNode.getRelationNames();
            if (childNodeNames != null) {
                relationNames.addAll(childNodeNames);
            }
        }
        return relationNames;
    }

    /**
	 * Returns true if at least 1 child node matches relex. Creates list of
	 * all matching child nodes (childNodeMatches) to be used later for
	 * variable substitution and validation
	 */
    boolean matchesRelex(String relex) {
        childNodeMatches = null;
        boolean pass = false;
        for (ASTNode childNode : children) {
            if (childNode.matchesRelex(relex)) {
                pass = true;
                if (childNodeMatches == null) {
                    childNodeMatches = new ArrayList<ASTNode>();
                }
                childNodeMatches.add(childNode);
            }
        }
        return pass;
    }

    /**
	 * Creates VarMapList with VarMaps from each valid condition
	 */
    boolean processVariableMatch(String relex) {
        nodeVarMapList = null;
        boolean pass = false;
        VarMapList newList = new VarMapList();
        for (ASTNode childNode : childNodeMatches) {
            if (childNode.processVariableMatch(relex)) {
                pass = true;
                if (childNode.getVarMapList() != null) {
                    newList.addAll(childNode.getVarMapList());
                }
            }
        }
        nodeVarMapList = newList;
        return pass;
    }
}
