package org.pandcorps.test.pandam;

import org.pandcorps.pandam.*;
import org.pandcorps.pandam.impl.*;

public class UnitPanplementation extends Panplementation {

    private final Panple pos = new ImplPanple(0, 0, 0);

    private boolean vis = true;

    private int rot = 0;

    private boolean mirror = false;

    private boolean flip = false;

    public UnitPanplementation(final Panctor actor) {
        super(actor);
    }

    @Override
    protected void updateView(final Panmage image) {
    }

    @Override
    protected void renderView() {
    }

    @Override
    public Panple getPosition() {
        return pos;
    }

    @Override
    public boolean isVisible() {
        return vis;
    }

    @Override
    public void setVisible(final boolean vis) {
        this.vis = vis;
    }

    @Override
    public int getRot() {
        return rot;
    }

    @Override
    public void setRot(final int rot) {
        this.rot = rot;
    }

    @Override
    public boolean isMirror() {
        return mirror;
    }

    @Override
    public void setMirror(final boolean mirror) {
        this.mirror = mirror;
    }

    @Override
    public boolean isFlip() {
        return flip;
    }

    @Override
    public void setFlip(final boolean flip) {
        this.flip = flip;
    }
}
