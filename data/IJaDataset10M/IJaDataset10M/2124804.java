package org.orchesta.mathena.aquamatic.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.orchesta.mathena.aquamatic.Aquamatic;
import org.orchesta.mathena.aquamatic.model.GameMap;
import org.orchesta.mathena.aquamatic.model.ResourceManager;

public class AquaMouseListener implements MouseListener {

    private GameMap gm = ResourceManager.getInstance().getGameMap();

    private Aquamatic a;

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
        if (gm.isSuccess()) return;
        Point p = arg0.getPoint();
        int y = p.x / 30;
        int x = p.y / 30;
        int state = gm.process(x, y);
        if (state == 1) a.increaseStep();
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void setAquamatic(Aquamatic a) {
        this.a = a;
    }
}
