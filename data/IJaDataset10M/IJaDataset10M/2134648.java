package com.sun.spot.spotworld.gridview;

import com.sun.spot.spotworld.common.IUICommand;
import com.sun.spot.spotworld.common.UICommand;
import com.sun.spot.spotworld.common.LocaleUtil;
import com.sun.spot.spotworld.gui.IUIObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * A GVTangibleObject is a kind of GVObject that lives in GridView that can be manipulated. It can also be
 * animated with the Animator class.
 */
public class GVTangibleObject extends GVObject implements MouseListener, MouseMotionListener, ActionListener {

    public void init() {
        super.init();
        addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (!(getLooseParent() instanceof GVTangibleObject)) return;
        if ((e.getClickCount() == 1 && e.getButton() == e.BUTTON1) && !e.isPopupTrigger()) mouseButton1Pressed(e);
        if (e.getClickCount() == 1 && e.getButton() == e.BUTTON2) mouseButton2Pressed(e);
        if (e.getClickCount() == 1 && e.isPopupTrigger()) mouseButton3Pressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == e.BUTTON1 && !e.isPopupTrigger()) ((GVTangibleObject) getRoot()).mouseButton1Released(e);
        if (e.getButton() == e.BUTTON2) ((GVTangibleObject) getRoot()).mouseButton2Released(e);
        if (e.isPopupTrigger()) ((GVTangibleObject) getRoot()).mouseButton3Released(e);
        if (e.getClickCount() == 1 && e.isPopupTrigger()) mouseButton3Pressed(e);
    }

    public void mouseButton1Pressed(MouseEvent e) {
        if (!e.isShiftDown()) getView().deselectAll();
        if (e.isShiftDown() && isSelected()) deselect(); else select();
        int saveDX = e.getX() - getX();
        int saveDY = e.getY() - getY();
        ShadowingHolder holder = new ShadowingHolder();
        holder.setGridView(getGridView());
        holder.setOriginator(this);
        GridView v = getView();
        holder.addToView(v);
        holder.addAndEncompass(this);
        holder.setDragOffset(e.getX(), e.getY());
        addMouseMotionListener(holder);
    }

    public void mouseButton2Pressed(MouseEvent e) {
    }

    public void mouseButton3Pressed(MouseEvent e) {
        getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
    }

    public synchronized void mouseButton1Released(MouseEvent e) {
        removeMouseMotionListener(this);
    }

    public void mouseButton2Released(MouseEvent e) {
    }

    public void mouseButton3Released(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        ((GVTangibleObject) getRoot()).mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
    }

    public JPopupMenu getPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        Vector<JMenuItem> v = getMenuItems();
        for (JMenuItem item : v) {
            menu.add(item);
        }
        return menu;
    }

    public Vector<JMenuItem> getMenuItems() {
        Vector<JMenuItem> v = new Vector<JMenuItem>();
        return v;
    }

    protected Vector<JMenuItem> menuHelper(Vector<UICommand> commands) {
        Vector<JMenuItem> menuItems = new Vector<JMenuItem>();
        for (UICommand command : commands) {
            final UICommand command2 = command;
            if (command2.getSubUICommands().size() > 0) {
                JMenu submenu = new JMenu(command2.getName());
                submenu.setToolTipText(command2.getToolTip());
                Vector<JMenuItem> v = menuHelper(command2.getSubUICommands());
                for (JMenuItem item : v) submenu.add(item);
                menuItems.add(submenu);
            } else {
                JMenuItem menuItem = new JMenuItem(command2.getName());
                menuItem.setToolTipText(command2.getToolTip());
                menuItem.setEnabled(command2.isEnabled());
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        command2.go((IUIObject) GVTangibleObject.this);
                    }
                });
                menuItems.add(menuItem);
            }
        }
        return menuItems;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals(LocaleUtil.getString("Hide"))) {
            GridView gv = getView();
            removeFromView();
            gv.repaint();
        }
    }

    /**
     * Animate from current position to final position.
     *
     * @param goalObject If null, xF, Yf indicate target position, else they are offsets from this object.
     * @param xF position to move to, x coordinate, or if goalObject is not null, offset from the goalObject.
     * @param yF position to move to, y coordinate, or if goalObject is not null, offset from the goalObject.
     * @param dur duration in seconds
     * @param dt step size of animation, in seconds
     * @param slowIn fraction of time (dur) spent in initial acceleration (slow in) portion of animation
     * @param slowOut fraction of time (dur)spent in deceleration.
     */
    public synchronized void animateTo(GVObject goalObject, final int xF, final int yF, final double dur, final double dt, final double slowIn, final double slowOut) {
        killAllAnimations();
        currentAnimator = new Animator(this, goalObject, xF, yF, dur, dt, slowIn, slowOut);
        currentAnimator.start();
    }

    public synchronized void animateToAndBlock(GVObject goalObject, final int xF, final int yF, final double dur, final double dt, final double slowIn, final double slowOut) {
        killAllAnimations();
        try {
            animateTo(goalObject, xF, yF, dur, dt, slowIn, slowOut);
            currentAnimator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void killAllAnimations() {
        if (currentAnimator != null) {
            currentAnimator.stopAllAnimations();
            try {
                currentAnimator.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
