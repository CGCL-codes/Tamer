package com.seaglasslookandfeel.effect;

import javax.swing.UIManager;

/**
 * Customized Nimbus's drop shadow effect for SeaGlass.
 *
 * @author Kathryn Huxtable
 */
public class SeaGlassDropShadowEffect extends DropShadowEffect {

    /**
     * Creates a new SeaGlassDropShadowEffect object.
     */
    public SeaGlassDropShadowEffect() {
        color = UIManager.getColor("seaGlassDropShadow");
        angle = 90;
        distance = 1;
        size = 2;
        opacity = 0.15f;
    }
}
