package org.wilmascope.viewplugin;

import javax.media.j3d.Material;
import org.wilmascope.view.Colours;
import org.wilmascope.view.LODSphere;
import org.wilmascope.view.NodeView;

/**
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular Wilma graph drawing engine
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaOrg
 * @author Tim Dwyer
 * @version 1.0
 */
public class DefaultNodeView extends NodeView {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1543697002469728731L;

    Material nodeMat;

    Material highlightMat;

    public DefaultNodeView() {
        setTypeName("DefaultNodeView");
        nodeMat = Colours.redMaterial;
        highlightMat = Colours.yellowMaterial;
    }

    public DefaultNodeView(Material nodeMaterial, Material highlightMaterial) {
        setTypeName("DefaultNodeView");
        nodeMat = nodeMaterial;
        highlightMat = highlightMaterial;
    }

    protected void setupDefaultMaterial() {
        setupDefaultAppearance(nodeMat);
    }

    protected void setupHighlightMaterial() {
        setupHighlightAppearance(highlightMat);
    }

    protected void init() {
        LODSphere sphere = new LODSphere(1f, getAppearance());
        sphere.makePickable(this);
        sphere.addToTransformGroup(getTransformGroup());
    }
}
