package com.jvito.gui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import com.jvito.plot.Plot;

/**
 * A node for the {@link Navigator} for Plots.
 * 
 * @author Daniel Hakenjos
 * @version $Id: PlotTreeNode.java,v 1.3 2008/04/12 14:28:11 djhacker Exp $
 * 
 */
public class PlotTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 2442680427026531237L;

    private Plot plot;

    /**
	 * Init the node with a plot.
	 */
    public PlotTreeNode(Plot plot) {
        super();
        this.plot = plot;
        this.setUserObject(plot);
    }

    /**
	 * @see DefaultMutableTreeNode#add(javax.swing.tree.MutableTreeNode)
	 */
    @Override
    public void add(MutableTreeNode node) {
    }

    /**
	 * Always returns treu, because a plot is always a leaf.
	 */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
	 * Gets the plot of the node.
	 */
    public Plot getPlot() {
        return plot;
    }
}
