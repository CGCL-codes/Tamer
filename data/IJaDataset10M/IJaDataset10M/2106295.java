package net.sf.genomeview.gui;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.FocusManager;
import net.sf.genomeview.data.Model;
import net.sf.genomeview.gui.menu.edit.RemoveAction;
import net.sf.genomeview.gui.menu.navigation.AnnotationEndAction;
import net.sf.genomeview.gui.menu.navigation.AnnotationMoveLeftAction;
import net.sf.genomeview.gui.menu.navigation.AnnotationMoveRightAction;
import net.sf.genomeview.gui.menu.navigation.AnnotationStartAction;
import net.sf.genomeview.gui.menu.navigation.AnnotationZoomInAction;
import net.sf.genomeview.gui.menu.navigation.AnnotationZoomOutAction;

/**
 * 
 * @author Thomas Abeel
 *
 */
public class Hotkeys implements KeyEventDispatcher {

    private Model model;

    public Hotkeys(Model model) {
        this.left = new AnnotationMoveLeftAction(model);
        this.right = new AnnotationMoveRightAction(model);
        this.zoomin = new AnnotationZoomInAction(model);
        this.zoomout = new AnnotationZoomOutAction(model);
        this.model = model;
        this.start = new AnnotationStartAction(model);
        this.end = new AnnotationEndAction(model);
        this.remove = new RemoveAction(model);
    }

    private Action left, right, zoomin, zoomout, start, end, remove;

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (!FocusManager.getCurrentManager().getActiveWindow().equals(model.getGUIManager().getParent())) return false;
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_HOME:
                    start.actionPerformed(null);
                    return true;
                case KeyEvent.VK_END:
                    end.actionPerformed(null);
                    return true;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_NUMPAD4:
                    left.actionPerformed(null);
                    return true;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_NUMPAD6:
                    right.actionPerformed(null);
                    return true;
                case KeyEvent.VK_ADD:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_PLUS:
                case KeyEvent.VK_EQUALS:
                    zoomin.actionPerformed(null);
                    return true;
                case KeyEvent.VK_SUBTRACT:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_MINUS:
                    zoomout.actionPerformed(null);
                    return true;
                case KeyEvent.VK_DELETE:
                case KeyEvent.VK_BACK_SPACE:
                    remove.actionPerformed(null);
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
