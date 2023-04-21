package org.ct42.r42.structure;

/**
 * Moves all nodes following the forward cinematics paradigm over the scene 
 * hierarchie.
 * 
 * @author cthiele
 */
public class Animator implements NodeVisitor {

    private double timepoint;

    public Animator(double timepoint) {
        this.timepoint = timepoint;
    }

    /**
	 * @see org.ct42.r42.structure.NodeVisitor#visit(org.ct42.r42.structure.Node)
	 */
    public void visit(Node node) {
        node.animate(this.timepoint);
    }
}
