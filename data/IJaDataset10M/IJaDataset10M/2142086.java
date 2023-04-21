package com.rapidminer.gui.renderer.cluster;

import com.rapidminer.gui.graphs.ClusterModelGraphCreator;
import com.rapidminer.gui.graphs.GraphCreator;
import com.rapidminer.gui.renderer.AbstractGraphRenderer;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.clustering.ClusterModel;
import com.rapidminer.operator.clustering.HierarchicalClusterModel;

/**
 * A renderer for the graph view of cluster models.
 * 
 * @author Ingo Mierswa
 */
public class ClusterModelGraphRenderer extends AbstractGraphRenderer {

    @Override
    public GraphCreator<String, String> getGraphCreator(Object renderable, IOContainer ioContainer) {
        if (renderable instanceof HierarchicalClusterModel) {
            return new ClusterModelGraphCreator((HierarchicalClusterModel) renderable);
        } else if (renderable instanceof ClusterModel) {
            return new ClusterModelGraphCreator((ClusterModel) renderable);
        } else {
            return null;
        }
    }
}
