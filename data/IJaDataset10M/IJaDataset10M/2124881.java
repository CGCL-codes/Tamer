package com.volatileengine.material.states;

/**
 * @author darkprophet
 * 
 */
public class WireframeRenderState extends RenderState {

    public enum WireframeDrawMode {

        LINE, POINT
    }

    private ApplyFace applyFace;

    private float thickness;

    private WireframeDrawMode wireframeDrawMode;

    protected WireframeRenderState() {
        super();
    }

    /**
	 * @return the wireframeFace
	 */
    public ApplyFace getApplyFace() {
        return applyFace;
    }

    /**
	 * @return the wireframeLineThickness
	 */
    public float getThickness() {
        return thickness;
    }

    /**
	 * @param wireframeFace
	 *             the wireframeFace to set
	 */
    public void setApplyFace(ApplyFace wireframeFace) {
        this.applyFace = wireframeFace;
    }

    /**
	 * @param wireframeLineThickness
	 *             the wireframeLineThickness to set
	 */
    public void setThickness(float wireframeLineThickness) {
        this.thickness = wireframeLineThickness;
    }

    public WireframeDrawMode getWireframeDrawMode() {
        return wireframeDrawMode;
    }

    public void setWireframeDrawMode(WireframeDrawMode wireframeMode) {
        this.wireframeDrawMode = wireframeMode;
    }
}
