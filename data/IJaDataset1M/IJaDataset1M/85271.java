package yager.render;

import yager.world.PolygonAttributes;

/**
 * @author Ryan Hild (therealfreaker@sourceforge.net)
 */
public final class PolygonShader implements Shader {

    public static final StateMap polygonState = new StateMap();

    public static final PolygonAttributes defaultPolygon = new PolygonAttributes();

    private static final int type = StateMap.getNextStateType();

    private final PolygonAttributes polygon;

    public PolygonShader(PolygonAttributes attributes) {
        if (attributes == null) attributes = defaultPolygon;
        polygon = attributes;
        polygonState.assignState(polygon);
    }

    public PolygonAttributes getPolygonAttributes() {
        return polygon;
    }

    public final int getType() {
        return type;
    }

    public final int getIdentifier() {
        return polygon.getState().getIdentifier();
    }
}
