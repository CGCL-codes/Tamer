package com.rapidminer.gui.new_plotter.gui.treenodes;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import com.rapidminer.gui.new_plotter.StaticDebug;
import com.rapidminer.gui.new_plotter.configuration.DimensionConfig.PlotDimension;
import com.rapidminer.gui.new_plotter.configuration.PlotConfiguration;
import com.rapidminer.gui.new_plotter.configuration.RangeAxisConfig;

/**
 * Never use the setUserObject method of this class! Instead use the exchangePlotConfiguration in PlotConfigurationTreeModel class.
 * 
 * @author Nils Woehler
 *
 */
public class PlotConfigurationTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    public PlotConfigurationTreeNode(PlotConfiguration plotConfig) {
        super(plotConfig);
    }

    public int getRangeAxisIndex(RangeAxisConfig rangeAxis) {
        for (Object child : children) {
            if (rangeAxis == ((DefaultMutableTreeNode) child).getUserObject()) {
                return children.indexOf(child);
            }
        }
        return -1;
    }

    public int getDimensionConfigIndex(PlotDimension dimension) {
        for (Object child : children) {
            if (child instanceof DimensionConfigTreeNode) {
                if (((DimensionConfigTreeNode) child).getDimension() == dimension) {
                    return children.indexOf(child);
                }
            }
        }
        return -1;
    }

    public TreeNode getChild(RangeAxisConfig rangeAxis) {
        int rangeAxisIndex = getRangeAxisIndex(rangeAxis);
        if (rangeAxisIndex < 0) {
            return null;
        }
        return getChildAt(rangeAxisIndex);
    }

    public TreeNode getChild(PlotDimension dimension) {
        int dimensionConfigIndex = getDimensionConfigIndex(dimension);
        if (dimensionConfigIndex < 0) {
            return null;
        }
        return getChildAt(dimensionConfigIndex);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public PlotConfiguration getUserObject() {
        return (PlotConfiguration) super.getUserObject();
    }
}
