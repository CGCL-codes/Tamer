package com.peterhi.player.editor;

import java.awt.event.MouseEvent;

/**
 *
 * @author YUN TAO
 */
public abstract class AbstractBoundsEditor extends AbstractEditor {

    protected int downx, downy, movex, movey;

    protected boolean down;

    public AbstractBoundsEditor(Editor.Callback callback) {
        super(callback);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            downx = e.getX();
            downy = e.getY();
            down = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (down) {
            movex = e.getX();
            movey = e.getY();
            whiteboard.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            onFinished();
            down = false;
            downx = 0;
            downy = 0;
            movex = 0;
            movey = 0;
            whiteboard.repaint();
        }
    }
}
