package org.wilmascope.viewplugin;

import org.wilmascope.view.ClusterView;
import org.wilmascope.view.Colours;
import org.wilmascope.view.LODSphere;

public class CollapsedClusterView extends ClusterView {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5066843390374848586L;

    public CollapsedClusterView() {
        setTypeName("CollapsedClusterView");
    }

    protected void setupDefaultMaterial() {
        setupDefaultAppearance(Colours.pinkMaterial);
    }

    protected void setupHighlightMaterial() {
        setupHighlightAppearance(Colours.yellowMaterial);
    }

    public void init() {
        LODSphere sphere = new LODSphere(1.0f, getAppearance());
        sphere.makePickable(this);
        sphere.addToTransformGroup(getTransformGroup());
        org.wilmascope.graph.Cluster c = (org.wilmascope.graph.Cluster) getNode();
        float r = (float) Math.pow(3.0f * c.getMass() / (4.0f * Math.PI), 1.0d / 3.0d) / 4.0f;
        setRadius(r);
    }
}
