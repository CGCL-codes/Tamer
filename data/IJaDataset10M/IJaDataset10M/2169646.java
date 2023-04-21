package org.jcrpg.world.ai.body.part.mammal.humanoid;

import org.jcrpg.world.ai.body.BodyPart;

public class Hand extends BodyPart {

    static float[] ratio = { 0.1f, 0.4f };

    @Override
    public float[] getPlacingRatioXY() {
        return ratio;
    }
}
