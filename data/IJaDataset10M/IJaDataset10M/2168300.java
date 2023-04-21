package biz.xsoftware.vm.server.project.map;

/**
 * @author Dean Hiller
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RootNode extends MapNode {

    protected boolean isValidChildClass(Object o) {
        if (o instanceof BranchNode) return true;
        return false;
    }

    /**
	 * NEED TO ADD JAVADOC
	 * @param nodeB1
	 */
    public void addBranchNode(MapNode nodeB1) {
        maps.add(nodeB1);
    }
}
