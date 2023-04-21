package de.nameless.graphicEngine.lib;

import de.nameless.graphicEngine.animation.NEabstractAnimation;

public class NEflowAnimation extends NEabstractAnimation<NEWater> {

    private float flowSpeed = 0.03f;

    @Override
    public void doAnimation(float TBM) {
        NEWater.flow += flowSpeed * TBM;
        if (NEWater.flow >= 0.5f) NEWater.flow -= 0.5f;
    }

    @Override
    public void goFinal() {
    }
}
