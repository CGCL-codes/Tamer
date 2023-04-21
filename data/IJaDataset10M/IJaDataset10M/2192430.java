package yager.render;

import yager.world.ColoringAttributes;

/**
 * @author Ryan Hild (therealfreaker@sourceforge.net)
 */
public final class ColoringShader implements Shader {

    private static final ColoringAttributes defaultColoring = new ColoringAttributes();

    public static final StateMap coloringState = new StateMap();

    private static final int type = StateMap.getNextStateType();

    private final ColoringAttributes coloring;

    public ColoringShader(ColoringAttributes coloringAttributes) {
        if (coloringAttributes == null) coloringAttributes = defaultColoring;
        coloring = coloringAttributes;
        coloringState.assignState(coloring);
    }

    public final ColoringAttributes getColoringAttributes() {
        return coloring;
    }

    public final int getType() {
        return type;
    }

    public final int getIdentifier() {
        return coloring.getState().getIdentifier();
    }
}
