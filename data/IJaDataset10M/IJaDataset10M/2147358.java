package org.phylowidget;

import java.util.List;
import org.andrewberman.ui.unsorted.Json;
import org.phylowidget.tree.PhyloNode;
import org.phylowidget.ui.JavascriptAccessibleMethods;
import processing.core.ExtendedPApplet;
import processing.core.PApplet;

public abstract class PWPublicMethods extends ExtendedPApplet implements JavascriptAccessibleMethods {

    private PhyloTree getTree_obj() {
        return (PhyloTree) PWPlatform.getInstance().getThisAppContext().trees().getTree();
    }

    private PhyloNode getCurNode() {
        return getTree_obj().getHoveredNode();
    }

    public double getHoveredBranchLength() {
        return getTree_obj().getBranchLength(getCurNode());
    }

    public double getHoveredDepthToRoot() {
        return getTree_obj().getDepthToRoot(getCurNode());
    }

    public double getHoveredDistanceToRoot() {
        return getTree_obj().getHeightToRoot(getCurNode());
    }

    public int getHoveredLeafCount() {
        return getTree_obj().getNumEnclosedLeaves(getCurNode());
    }

    public double getHoveredMaxTreeLength() {
        return getTree_obj().getMaxHeightToLeaf(getCurNode());
    }

    public String getHoveredNode() {
        PhyloNode n = getTree_obj().getHoveredNode();
        if (n != null) {
            return n.getTree().getLabel(n);
        } else {
            return "";
        }
    }

    public int getHoveredNodeCount() {
        return getTree_obj().getAllNodes(getCurNode()).size();
    }

    public int getLeafCount() {
        return getTree_obj().getNumEnclosedLeaves(getTree_obj().getRoot());
    }

    public String[] getLeafNames() {
        List<PhyloNode> leaves = getTree_obj().getAllLeaves();
        String[] leafNames = new String[leaves.size()];
        for (int i = 0; i < leaves.size(); i++) {
            leafNames[i] = getTree_obj().getLabel(leaves.get(i));
        }
        return leafNames;
    }

    public double getMaxTreeLength() {
        return getTree_obj().getMaxTreeLength();
    }

    public int getNodeCount() {
        return getTree_obj().getNodeCount();
    }

    public double getTotalTreeLength() {
        return getTree_obj().getTotalTreeLength();
    }

    public String getTree() {
        return getTreeNHX();
    }

    public String getTreeNewick() {
        return getTree_obj().getNewick();
    }

    public String getTreeNHX() {
        return getTree_obj().getNHX();
    }

    public String getTreeString() {
        return getTreeNHX();
    }

    public String getTreeNeXML() {
        return getTree_obj().getNeXML();
    }

    public String getNodeInfoJSON() {
        return Json.hashToJson(getCurNode().getNodeInfo());
    }

    public abstract String getUrlParameters();

    public abstract String getClipboardString();

    public abstract void setClipboard(String clip);

    public abstract void setMessage(String message);

    public abstract void setTree(String tree);

    public abstract void setAnnotation(String nodeLabel, String key, String value);

    public abstract void setAnnotations(String nodeLabel, String annotationJson);

    public abstract void transformTree(String url);
}
