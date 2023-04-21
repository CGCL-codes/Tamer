package org.xith3d.demos.shooter_demo.graphics.ui;

import org.xith3d.render.Canvas3D;
import org.xith3d.ui.hud.HUD;

/**
 * The User Interface for the Shooter Demo.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class UI {

    private final HUD hud;

    public UI(Canvas3D canvas) {
        this.hud = new HUD(canvas, 1024f);
    }

    public HUD getHUD() {
        return hud;
    }
}
