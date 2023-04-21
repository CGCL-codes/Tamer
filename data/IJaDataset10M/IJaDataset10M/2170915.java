package com.crearic.tools.javabeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

/**
 * This is a normal JButton with the possibility to add a JPopupMenu, when this
 * happen the button paints an arrow on the bottom right corner and the 
 * button shows a popup menu when the arrow is clicked.
 * Example to create a DropDownButton:
 * <p><code>
 *	private DropDownButton dropDown = new DropDownButton();
 * 	dropDown.setIcon(new ImageIcon(getClass().getResource("an_image.png")));
 *	dropDown.setActionCommand("some action");
 * 	dropDown.addActionListener(a_listener);
 * 	dropDown.setJPopupMenu(aPopupMenu);
 * </code></p>
 */
public class DropDownButton extends JButton implements MouseListener {

    /** Size of the arrow that will be painted in the button. */
    private static final int ARROW_SIZE = 12;

    private JPopupMenu popupMenu;

    private Rectangle clickMenuArea;

    private boolean showPopup = false;

    private boolean alwaysShow = false;

    /**
	 * Creates a new instance of DropDownButton.
	 */
    public DropDownButton() {
        addMouseListener(this);
    }

    /**
	 * Sets the popup menu that will be shown when the user clicks with the mouse
	 * int the right-bottom corner. If the popup menu is null, the button will
	 * act as a normal button.
	 * @param popupMenu A JPopupMenu to show with the button.
	 */
    public void setJPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    /**
	 * Gets the actual popup menu of the component, or null if any.
	 * @return the popup menu of the component.
	 */
    public JPopupMenu getJPopupMenu() {
        return popupMenu;
    }

    public void setAlwaysShowsPopup(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    /**
	 * This method call their super implementation (super.paintComponent()) to do the
	 * normal view of a button, and if the component have a popup menu to show, this
	 * method also paint an arrow at the bottom right corner of the button.
	 * @param g the graphics class to paint something in the component.
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (popupMenu != null) {
            int height = getHeight();
            int width = getWidth();
            int initialX = width - ARROW_SIZE - getInsets().right;
            int initialY = height - ARROW_SIZE;
            Polygon arrow = new Polygon(new int[] { initialX, initialX + ARROW_SIZE, initialX + (ARROW_SIZE / 2) }, new int[] { initialY, initialY, initialY + (ARROW_SIZE / 2) }, 3);
            clickMenuArea = new Rectangle(initialX, initialY, ARROW_SIZE + getInsets().right, ARROW_SIZE);
            g.setColor(Color.DARK_GRAY);
            g.fillPolygon(arrow);
        } else if (clickMenuArea != null) {
            clickMenuArea = null;
        }
    }

    /**
	 * Intecept the action event and show a menu if the mouse clicked in the popup
	 * menu area of the button, otherwise fire the action event and act as a normal
	 * button.
	 * @param event the ActionEvent object.
	 */
    protected void fireActionPerformed(ActionEvent event) {
        if (showPopup) {
            popupMenu.show(this, 0, getHeight());
        } else {
            super.fireActionPerformed(event);
        }
        showPopup = false;
    }

    /**
	 * Nothing.
	 * @param e the mouse event.
	 */
    public void mouseClicked(MouseEvent e) {
    }

    /**
	 * Take the mouse click position and determine if the popup menu will be shown
	 * in the action event.
	 * @param e the Mouse Event.
	 */
    public void mousePressed(MouseEvent e) {
        if (alwaysShow || (clickMenuArea != null && clickMenuArea.contains(e.getPoint()))) {
            showPopup = true;
        }
    }

    /**
	 * Nothing.
	 * @param e the mouse event.
	 */
    public void mouseReleased(MouseEvent e) {
    }

    /**
	 * Nothing.
	 * @param e the mouse event.
	 */
    public void mouseEntered(MouseEvent e) {
    }

    /**
	 * Nothing.
	 * @param e the mouse event.
	 */
    public void mouseExited(MouseEvent e) {
    }
}
