package net.infonode.gui.hover.action;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * @author johan
 */
public class DelayedHoverExitAction implements HoverListener {

    private HashMap timers = new HashMap();

    private HashMap exitEvents = new HashMap();

    private HoverListener action;

    private int delay;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public HoverListener getHoverAction() {
        return action;
    }

    public void forceExit(Component component) {
        if (timers.containsKey(component)) {
            ((Timer) timers.get(component)).stop();
            timers.remove(component);
            HoverEvent event = (HoverEvent) exitEvents.get(component);
            exitEvents.remove(component);
            action.mouseExited(event);
        }
    }

    public void mouseEntered(HoverEvent event) {
        final Component c = event.getSource();
        if (timers.containsKey(c)) ((Timer) timers.get(c)).stop(); else {
            Timer t = new Timer(delay, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    forceExit(c);
                }
            });
            t.setRepeats(false);
            timers.put(c, t);
            action.mouseEntered(event);
        }
    }

    public void mouseExited(HoverEvent event) {
        exitEvents.put(event.getSource(), event);
        ((Timer) timers.get(event.getSource())).restart();
    }
}
