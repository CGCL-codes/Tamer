package kanjitori.graphics.ortho.hud;

import com.jme.scene.state.AlphaState;
import com.jme.scene.state.ZBufferState;
import kanjitori.graphics.AbstractThing;
import kanjitori.graphics.ortho.Ortho;
import kanjitori.graphics.ortho.hud.Hud;

/**
 *
 * @author Pirx
 */
public abstract class AbstractHud extends Ortho implements Hud {

    private static Hud INSTANCE;

    /** Creates a new instance of AbstractHud */
    public AbstractHud() {
        INSTANCE = this;
    }

    public static Hud getHud() {
        return INSTANCE;
    }
}
