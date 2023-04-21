package com.javaxyq.event;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.SceneCanvas;
import com.javaxyq.widget.Player;

public class CanvasMouseHandler implements MouseListener, MouseMotionListener {

    public void mouseClicked(MouseEvent e) {
        GameCanvas _canvas = ApplicationHelper.getApplication().getContext().getWindow().getCanvas();
        if (_canvas instanceof SceneCanvas) {
            SceneCanvas canvas = (SceneCanvas) _canvas;
            Player player = ApplicationHelper.getApplication().getContext().getPlayer();
            if (e.getButton() == MouseEvent.BUTTON1) {
                Point p = e.getPoint();
                if (canvas.isHover(player)) {
                    player.stop(false);
                    return;
                }
                List<Player> npcs = canvas.getNpcs();
                for (Player npc : npcs) {
                    if (canvas.isHover(npc)) {
                        npc.fireEvent(new PlayerEvent(npc, PlayerEvent.TALK));
                        return;
                    }
                }
                canvas.click(e.getPoint());
                canvas.requestFocus(true);
                Point coords = canvas.viewToScene(p);
                player.fireEvent(new PlayerEvent(player, PlayerEvent.WALK, coords));
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                player.stop(false);
                player.changeDirection(e.getPoint());
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}
