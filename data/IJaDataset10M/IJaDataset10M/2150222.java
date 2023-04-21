package ecbm.entity.base;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author ito
 */
public class Cycle {

    private GameContainer gc;

    private StateBasedGame sb;

    private int delta;

    public Cycle() {
    }

    public Cycle(GameContainer gc, StateBasedGame sb, int delta) {
        this.gc = gc;
        this.sb = sb;
        this.delta = delta;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public GameContainer getGc() {
        return gc;
    }

    public void setGc(GameContainer gc) {
        this.gc = gc;
    }

    public StateBasedGame getSb() {
        return sb;
    }

    public void setSb(StateBasedGame sb) {
        this.sb = sb;
    }

    public void renew(GameContainer gc, StateBasedGame sb, int delta) {
        this.gc = gc;
        this.sb = sb;
        this.delta = delta;
    }
}
