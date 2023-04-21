package com.cirnoworks.cis.impl.monkey3;

import com.cirnoworks.cis.Frame;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * @author Cloudee
 * 
 */
public class QuadFrame implements Frame {

    private Picture p;

    private Node axis;

    private Node parent;

    private Node parentUI;

    private final float rcmdcX;

    private final float rcmdcY;

    private String name;

    private final Monkey3UI ui;

    public QuadFrame(Monkey3UI ui, String name, Node parent, Node parentUI, Picture p, float rcmdcX, float rcmdcY) {
        this.name = name;
        this.p = p;
        this.axis = new Node(p.getName() + "#p");
        this.rcmdcX = rcmdcX * ui.getMasterScaleX();
        this.rcmdcY = rcmdcY * ui.getMasterScaleY();
        this.parent = parent;
        this.parentUI = parentUI;
        this.ui = ui;
        axis.attachChild(p);
    }

    public void draw(float rot, float rotX, float rotY, float scaleX, float scaleY, int color, int alpha, float x, float y, boolean ui, boolean flipH, boolean flipV, float[] additional) {
        color = 0xffffff;
        axis.setLocalScale(scaleX, scaleY, 1.0f);
        axis.setLocalTranslation(x + rcmdcX, this.ui.getHeight() - y - rcmdcY, 0);
        axis.getLocalRotation().fromAngles(rotX, rotY, rot);
        p.getMaterial().setColor("m_Color", GlobalColorPoolMonkey.getColor((alpha << 24) | (color & 0xffffff)));
        p.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        (ui ? parentUI : parent).attachChild(axis);
    }

    public boolean isPointerInRange(float rot, float rotX, float rotY, float scaleX, float scaleY, float x, float y, float cx, float cy) {
        BoundingVolume bound = p.getWorldBound();
        if (bound == null) {
            return false;
        }
        return bound.intersects(new Ray(new Vector3f(cx, this.ui.getHeight() - cy, -1000), new Vector3f(0, 0, 1)));
    }
}
