package spellcast.ui;

import javax.swing.*;

/**
 *
 * @author Barrie Treloar
 */
public class LeftHandedGesturePopupMenu extends GesturePopupMenu {

    public LeftHandedGesturePopupMenu() {
        super();
    }

    Icon getPalmIcon() {
        return GestureIcon.getPalmLefthandIcon();
    }

    Icon getDigitIcon() {
        return GestureIcon.getDigitLefthandIcon();
    }

    Icon getFingersIcon() {
        return GestureIcon.getFingersLefthandIcon();
    }

    Icon getWaveIcon() {
        return GestureIcon.getWaveLefthandIcon();
    }

    Icon getClapIcon() {
        return GestureIcon.getClapLefthandIcon();
    }

    Icon getSnapIcon() {
        return GestureIcon.getSnapLefthandIcon();
    }

    Icon getKnifeIcon() {
        return GestureIcon.getKnifeLefthandIcon();
    }
}
