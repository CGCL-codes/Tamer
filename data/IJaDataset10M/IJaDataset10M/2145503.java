package hu.arguscab.ai.game;

import hu.arguscab.ai.FitnessFunction;
import hu.arguscab.ai.Model;
import hu.arguscab.game.WorldState;
import hu.arguscab.physic.Vector;

/**
 *
 * @author sharp
 */
public class FVelocity extends FitnessFunction {

    private int maxDistance;

    private float maxVelocity;

    private float fV, fD;

    private int max;

    @Override
    public float getMinValue() {
        return 0;
    }

    @Override
    public float getMaxValue() {
        return 100;
    }

    @Override
    public float calcF(Model m) {
        WorldState state = (WorldState) m.getState();
        Vector ship = state.getShipState().getPos();
        Vector target = ((Bot) state.getShipState().getShip().getController()).getTarget().getState().getPos();
        float x = Math.abs(target.getX() - ship.getX());
        float y = Math.abs(target.getY() - ship.getY());
        float d = Math.max(x, y);
        double f = (Math.log10(d + 1) / Math.log10(fD)) * fV;
        float v = Math.max(state.getShipState().getV().getLength(), state.getShipState().getA().getLength());
        return (float) ((maxVelocity - Math.abs(v - f)) / maxVelocity) * 100;
    }

    private float szar(int d, float v) {
        double f = (Math.log10(d + 1) / Math.log10(fD)) * fV;
        return (float) ((maxVelocity - Math.abs(v - f)) / maxVelocity) * 100;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public void setfD(float fD) {
        this.fD = fD;
    }

    public void setfV(float fV) {
        this.fV = fV;
    }

    public void init() {
        max = (int) (((Math.log10(maxDistance) / Math.log10(fD)) * fV) + 0.5f);
    }
}
