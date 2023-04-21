package com.jmt.game.client.ui;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jmt.game.core.Context;
import com.jmt.game.core.GamePiece;
import com.jmt.game.geometry.GeometryFactory;

public class Avatar extends Node {

    protected static final float ALPHA_CHANNEL_FOR_INVISIBLE = 0.45f;

    protected static final float ALPHA_CHANNEL_FOR_VISIBLE = 1.0f;

    public Avatar(GamePiece renderable) throws Exception {
        super(renderable.getType() + ": " + renderable.getName());
        Node modelNode = GeometryFactory.get().getGeometry(name, renderable.getGeometryDescriptor());
        attachChild(modelNode);
        BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        bs.setTestEnabled(true);
        bs.setTestFunction(BlendState.TestFunction.GreaterThan);
        setRenderState(bs);
        setVisible(renderable.isVisible());
        setModelBound(new BoundingBox());
        updateModelBound();
    }

    public void setVisible(boolean visible) {
        ColorRGBA baseColor = new ColorRGBA(1, 1, 1, 1);
        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        if (visible) {
            baseColor.a = ALPHA_CHANNEL_FOR_VISIBLE;
        } else {
            baseColor.a = ALPHA_CHANNEL_FOR_INVISIBLE;
        }
        ms.setDiffuse(baseColor);
        setRenderState(ms);
    }
}
