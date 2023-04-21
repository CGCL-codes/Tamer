package com.gwtaf.core.client.layout;

public class FlowLayoutData extends LayoutData {

    private float ratio;

    public FlowLayoutData(int minWidth, int minHeight) {
        super(minWidth, minHeight);
    }

    public FlowLayoutData(int minWidth, int minHeight, float ratio) {
        super(minWidth, minHeight);
        this.ratio = ratio;
    }

    public FlowLayoutData(FlowLayoutData o) {
        super(o);
        this.ratio = o.ratio;
    }

    public float getRatio() {
        return ratio;
    }

    public boolean isSizable() {
        return ratio != 0.0f;
    }
}
