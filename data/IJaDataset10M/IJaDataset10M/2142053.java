package net.sourceforge.javautil.gui.swing.css.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;
import javax.swing.JComponent;
import net.sourceforge.javautil.gui.swing.css.SwingCSSPsuedoClassEngine;

/**
 * This will handle mouse related psuedo classes.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class PsuedoMouse extends PsuedoClassEvent implements MouseListener {

    public static void addIfApplies(JComponent cmp, SwingCSSPsuedoClassEngine engine, Set<String> classes) {
        if (classes.contains("hover") || classes.contains("mouseDown") || classes.contains("click")) {
            PsuedoMouse pm = new PsuedoMouse(cmp, engine);
            pm.hoverEvent = classes.contains("hover");
            pm.mouseDownEvent = classes.contains("mouseDown");
            pm.clickedEvent = classes.contains("clicked");
            cmp.addMouseListener(pm);
        }
    }

    public static void removeIfApplies(JComponent cmp) {
        for (MouseListener ml : cmp.getMouseListeners()) {
            if (ml instanceof PsuedoMouse) cmp.removeMouseListener(ml);
        }
    }

    protected boolean hoverEvent = false;

    protected boolean mouseDownEvent = false;

    protected boolean clickedEvent = false;

    public PsuedoMouse(JComponent component, SwingCSSPsuedoClassEngine psuedoEngine) {
        super(component, psuedoEngine);
    }

    public void mouseClicked(MouseEvent e) {
        if (this.clickedEvent) this.togglePsuedoClass("clicked");
    }

    public void mouseEntered(MouseEvent e) {
        if (hoverEvent) this.activatePsuedoClass("hover");
    }

    public void mouseExited(MouseEvent e) {
        if (hoverEvent) this.deactivatePsuedoClass("hover");
    }

    public void mousePressed(MouseEvent e) {
        if (mouseDownEvent) this.activatePsuedoClass("mouseDown");
    }

    public void mouseReleased(MouseEvent e) {
        if (mouseDownEvent) this.deactivatePsuedoClass("mouseDown");
    }
}
