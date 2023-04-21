package se.liu.oschi129.game.objects.blocks.breakable;

import se.liu.oschi129.animation.animationspace.AnimationSpace;

/**
 * This is a predefined breakable block with the animation img_block10
 */
public class ObjectBreakableBlock10 extends ObjectBreakableBlock {

    public ObjectBreakableBlock10(double x, double y) {
        super(AnimationSpace.get("img_block10"), true, x, y, 16, 16);
        setImageSpeed(0.2);
        for (int i = 0; i < 4; i++) addPart(new ObjectBreakableBlock10Part01(0, 0));
    }

    public ObjectBreakableBlock10(boolean visible, double x, double y) {
        this(x, y);
        setVisible(true);
    }
}
