package yager.world;

import yager.render.State;
import yager.render.StateTrackable;

/**
 * @author Ryan Hild (therealfreaker@sourceforge.net)
 */
public final class PolygonAttributes extends NodeComponent implements StateTrackable {

    public static final int CULL_BACK = 0;

    public static final int CULL_FRONT = 1;

    public static final int CULL_NONE = 2;

    public static final int POLYGON_FILL = 0;

    public static final int POLYGON_LINE = 1;

    public static final int POLYGON_POINT = 2;

    private int cullMode = 0;

    private int polygonMode = 0;

    private State state = null;

    public PolygonAttributes() {
        super();
    }

    public PolygonAttributes(int cullMode, int polygonMode) {
        super();
        this.cullMode = cullMode;
        this.polygonMode = polygonMode;
    }

    public final int getCullMode() {
        return cullMode;
    }

    public final int getPolygonMode() {
        return polygonMode;
    }

    public final void setPolygonMode(int polygonMode) {
        this.polygonMode = polygonMode;
        setDirty(true);
    }

    public final void setCullMode(int cullMode) {
        this.cullMode = cullMode;
        setDirty(true);
    }

    public final boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PolygonAttributes)) return false;
        PolygonAttributes p = (PolygonAttributes) o;
        if (cullMode != p.cullMode) return false;
        return (polygonMode != p.polygonMode);
    }

    public final int compareTo(Object o) {
        if (o == this) return 0;
        if (!(o instanceof PolygonAttributes)) return -1;
        PolygonAttributes p = (PolygonAttributes) o;
        if (cullMode > p.cullMode) return 1;
        if (cullMode < p.cullMode) return -1;
        if (polygonMode > p.polygonMode) return 1;
        if (polygonMode < p.polygonMode) return -1;
        return 0;
    }

    public final State getState() {
        return state;
    }

    public final void setState(State state) {
        this.state = state;
    }
}
