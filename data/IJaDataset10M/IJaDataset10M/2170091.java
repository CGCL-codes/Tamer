package org.formaria.swing.docking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * A panel for use in a docking framework. The sidebars are
 * added to a border layout in the parent. Initially these sidebars do not
 * appear as they have no content, as buttons are added for docked panels the 
 * sidebars reappear once the container's layout is revalidated. Clicking on 
 * an area outside of the preview causes the preview to disappear, while
 * clicking on the preview button redocks the panel.
 * <p>Copyright: (c) Formaria Ltd., 2008<br>
 * License:      see license.txt
 * @version $Revision: 1.2 $
 */
public class DockingSideBar extends JPanel {

    private static final int PREVIEW_SIZE = 250;

    private FontMetrics fm;

    private Color sidebarBkColor, sidebarTextColor, sidebarRolloverTextColor;

    private boolean isVertical;

    private String docklocation;

    /**
   * The default size controls the 'thickness' of the sidebar
   */
    private Dimension defSize = new Dimension(25, 25);

    private Container glassPane;

    private DockingPreview dockingPreview;

    /**
   * 
   * Creates a new instance of DockingSideBar for use with the MultiSplitPane.
   * The side bar is invisible while it is empty, but shows a button for each component
   * that docks into it. Clicking the button then restores the docked component
   * <p>
   * The colours of the header are controlled with the <code>dockingSidebar</code> 
   * style and <code>dockingSidebar/active</code> for the active tab colours
   * </p>
   * @param gpane the GlassPane
   * @param location the BorderLayout position into which this sidebar fits
   */
    public DockingSideBar(Container gpane, String location) {
        glassPane = gpane;
        docklocation = location.toLowerCase();
        isVertical = !docklocation.equals("south");
        setOpaque(true);
        setLayout(new BoxLayout(this, isVertical ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS));
        glassPane.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
                redispatchMouseEvent(e, false);
            }

            public void mouseClicked(MouseEvent e) {
                redispatchMouseEvent(e, false);
            }

            /**
       * Hide the popup if one is visible
       */
            public void mousePressed(MouseEvent e) {
                if (dockingPreview != null) {
                    Point pt = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), dockingPreview);
                    if (dockingPreview.contains(pt)) return;
                    hidePopup();
                    redispatchMouseEvent(e, true);
                }
            }
        });
        glassPane.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                if (dockingPreview != null) {
                    Point srcPt = e.getPoint();
                    Point pt = SwingUtilities.convertPoint((Component) e.getSource(), srcPt, dockingPreview);
                    if (dockingPreview.contains(pt)) return; else if (docklocation.equals("west") && (srcPt.x < dockingPreview.getX())) return; else if (docklocation.equals("east") && (srcPt.x > dockingPreview.getX())) return; else if (docklocation.equals("south") && (srcPt.y > dockingPreview.getY())) return;
                    Component focusComp = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (!isChildOf(dockingPreview, focusComp)) hidePopup();
                }
                redispatchMouseEvent(e, true);
            }
        });
    }

    /**
   * Carry out any post creation styling. This method is called one the
   * application frameowrk has been setup. By default the method sets the 
   * colours of the component as the style manager is now available.
   */
    public void applyStyles(Color sidebarBkColor, Color sidebarTextColor, Color sidebarRolloverTextColor) {
        this.sidebarBkColor = sidebarBkColor;
        this.sidebarTextColor = sidebarTextColor;
        this.sidebarRolloverTextColor = sidebarRolloverTextColor;
        setBackground(sidebarBkColor);
    }

    /**
   * Add new button for the docked panel
   * @parm dockingPanel the panel being docked
   */
    public void add(Dockable dockingPanel) {
        add(new DockButton(dockingPanel, isVertical));
        revalidate();
    }

    /**
   * Remove the button for the docked panel
   * @parm dockingPanel the panel being docked
   */
    public void removeDockable(Dockable dockingPanel) {
        int count = getComponentCount();
        for (int i = 0; i < count; i++) {
            DockButton child = (DockButton) getComponent(i);
            if (child.dockable == dockingPanel) {
                remove(child);
                revalidate();
                return;
            }
        }
    }

    /**
   * Get the preferred size of the component. This method returns values that 
   * dictate how the panel is displayed in the MultiSplitPane
   */
    public Dimension getPreferredSize() {
        if (getComponentCount() == 0) return new Dimension(0, 0);
        if (defSize != null) return defSize;
        return super.getPreferredSize();
    }

    /**
   * Show a preview of the target panel's content. The preview is shown next to
   * the sidebar and 'borrows' the content of the docked panel.
   * @todo fixup popup location for other sidebar locations
   * @param target the docking panel who's content will temporarily be displayed
   * in a preview window
   */
    public void showPopup(Dockable target) {
        hidePopup();
        dockingPreview = new DockingPreview(glassPane, target);
        Point location = getLocation();
        Point gpPoint = SwingUtilities.convertPoint(this, new Point(0, 0), glassPane);
        if (docklocation.equals("west")) {
            dockingPreview.setLocation(getWidth(), gpPoint.y);
            dockingPreview.setSize(PREVIEW_SIZE, getHeight());
        } else if (docklocation.equals("east")) {
            dockingPreview.setLocation(location.x - PREVIEW_SIZE, gpPoint.y);
            dockingPreview.setSize(PREVIEW_SIZE, getHeight());
        } else if (docklocation.equals("south")) {
            dockingPreview.setLocation(gpPoint.x, gpPoint.y - PREVIEW_SIZE);
            dockingPreview.setSize(getWidth(), PREVIEW_SIZE);
        } else {
            dockingPreview = null;
            return;
        }
        glassPane.setLayout(null);
        glassPane.add(dockingPreview);
        glassPane.setVisible(true);
    }

    /**
   * Hide the preview of the target panel's content. As the panel is hidden the
   * docked panel's content is restored
   * @param target the docking panel who's content will temporarily be displayed
   * in a preview window
   */
    public void hidePopup() {
        if (dockingPreview != null) {
            Dockable target = dockingPreview.getDockable();
            dockingPreview.setVisible(false);
            glassPane.remove(dockingPreview);
            glassPane.setVisible(false);
            repaint();
            target.dockedContainer.fireDockingPanelListeners(DockingPanel.PREVIEW_CLOSED);
        }
        dockingPreview = null;
    }

    /**
   * Forward the mouse event to the component under the glass pane
   * @see http://java.sun.com/docs/books/tutorial/uiswing/components/rootpane.html
   */
    private void redispatchMouseEvent(MouseEvent e, boolean repaint) {
        Point glassPanePoint = e.getPoint();
        Container container = getRootPane().getContentPane();
        Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, container);
        if (containerPoint.y < 0) {
        } else {
            Component component = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);
            if (component != null) {
                Point componentPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, component);
                component.dispatchEvent(new MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers(), componentPoint.x, componentPoint.y, e.getClickCount(), e.isPopupTrigger()));
            }
        }
    }

    /** 
   * Test if one component is a child, grandchild or descendant of the parent component
   * @param parent the parent component
   * @c the child component
   */
    private boolean isChildOf(Component parent, Component c) {
        while (c != null) {
            if (c == parent) return true;
            c = c.getParent();
        }
        return false;
    }

    /**
   * A button placed on the sidebar for each docked panel. The button displays
   * vertical text for the vertical sidebars. When the mouse hovers over the
   * button a preview of the docked panel is displayed. The preview is dismissed
   * by clicking the button again or by clicking anywhere else.
   */
    public class DockButton extends JButton implements ActionListener, MouseListener {

        /**
     * The button's text is not added to the button, so that the JButton itself
     * does not paint the text.
     */
        private String title;

        private Dimension preferredSize;

        private Dockable dockable;

        private boolean hasMouse;

        private boolean isVertical;

        /**
     * Create a new sidebar button
     * @param p the docking panel that is associated with this button
     * @param vertical is the button in a vertical sidebar or not
     */
        public DockButton(Dockable p, boolean vertical) {
            super();
            setBackground(sidebarBkColor);
            dockable = p;
            dockable.launchContainer = this;
            title = p.title;
            isVertical = vertical;
            if (!isVertical) setText(title);
            Dimension d = super.getPreferredSize();
            setSize(new Dimension(d.height, d.width));
            setBorder(new EmptyBorder(0, 0, 0, 0));
            if (dockable.icon != null) setIcon(dockable.icon);
            setOpaque(false);
            addActionListener(this);
            addMouseListener(this);
        }

        /**
     * Get the dockable associated with this button
     */
        public Dockable getDockable() {
            return dockable;
        }

        /**
     * When the button is clicked dock the dockable back into its docking panel
     * @param e the mouse event
     */
        public void actionPerformed(ActionEvent e) {
            if (dockable != null) {
                Container parentContainer = getParent();
                CardPanel cardPanel = dockable.getCardPanel();
                cardPanel.restoreViews();
                dockable.dockedContainer.restoreContent(dockable);
                parentContainer.remove(this);
                parentContainer.repaint();
                if (parentContainer.getComponentCount() == 0) ((JComponent) parentContainer).revalidate();
            }
        }

        /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
        public void mouseClicked(MouseEvent e) {
        }

        /**
     * Invoked when a mouse button has been pressed on a component.
     */
        public void mousePressed(MouseEvent e) {
            hasMouse = false;
            hidePopup();
        }

        /**
     * Invoked when a mouse button has been released on a component.
     */
        public void mouseReleased(MouseEvent e) {
            hasMouse = false;
            hidePopup();
        }

        /**
     * Invoked when the mouse enters a component. After a short delay a preview
     * of the dockable content is initiated
     */
        public void mouseEntered(MouseEvent e) {
            if (!hasMouse && (dockingPreview == null) && !glassPane.isVisible()) {
                hasMouse = true;
                final Dockable dockable = ((DockButton) e.getSource()).getDockable();
                Timer timer = new Timer(1000, new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        if (hasMouse) showPopup(dockable);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }

        /**
     * Invoked when the mouse exits a component.
     */
        public void mouseExited(MouseEvent e) {
            hasMouse = false;
        }

        /**
     * If a border has been set on this component, returns the
     * border's insets; otherwise calls <code>super.getInsets</code>.
     *
     * @return the value of the insets property
     * @see #setBorder
     */
        public Insets getInsets() {
            return new Insets(1, 1, 1, 1);
        }

        /**
     * Draw the button text if it is to be show vertically
     * @todo build a better way of rotating the button text
     */
        public void paintComponent(Graphics g) {
            if (fm == null) {
                fm = g.getFontMetrics(getFont());
                preferredSize = null;
                revalidate();
            }
            ButtonModel bm = getModel();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            Rectangle rect = getBounds();
            RoundRectangle2D.Float clipRect = new RoundRectangle2D.Float(0, 0, rect.width, rect.height, 10, 10);
            RoundRectangle2D.Float buttonRect = new RoundRectangle2D.Float(1, 1, rect.width - 2, rect.height - 3, 8, 8);
            RoundRectangle2D.Float innerRect = new RoundRectangle2D.Float(2, 2, rect.width - 4, rect.height - 5, 6, 6);
            g2d.clip(clipRect);
            Color bkColor = getBackground();
            Color bkColorDk = bkColor.darker();
            GradientPaint gradient = new GradientPaint(0.0F, 0.0F, bkColor.brighter(), 0.0F, (float) rect.height, bkColor, true);
            if (bm.isRollover()) {
                if (!bm.isPressed()) g2d.setPaint(gradient); else g2d.setPaint(bkColor);
                g2d.fill(buttonRect);
            }
            if (bm.isRollover()) {
                g2d.setPaint(new Color(bkColorDk.getRed(), bkColorDk.getGreen(), bkColorDk.getBlue(), bm.isPressed() ? 156 : 128));
                g2d.draw(buttonRect);
                GradientPaint innerGradient = new GradientPaint(0.0F, 0.0F, new Color(255, 255, 255, 200), 0.0F, (float) rect.height, new Color(255, 255, 255, 0), true);
                g2d.setPaint(innerGradient);
                g2d.draw(innerRect);
            }
            int x = 0;
            Icon icon = null;
            if (bm.isPressed()) icon = getPressedIcon(); else if (bm.isRollover()) icon = getRolloverIcon(); else if (bm.isRollover()) icon = getRolloverIcon(); else if (!bm.isEnabled()) icon = getDisabledIcon(); else if (bm.isSelected()) icon = getSelectedIcon();
            if (icon == null) icon = getIcon();
            if (!isVertical) {
                if (icon != null) {
                    int h = icon.getIconHeight();
                    int w = icon.getIconWidth();
                    x += w + 4;
                    icon.paintIcon(this, g, 4, Math.max(4, ((rect.height - h) / 2) + 1));
                }
                String text = getText();
                if ((text != null) && (text.length() > 0)) {
                    FontMetrics fm = g.getFontMetrics();
                    g2d.setColor(bm.isRollover() ? sidebarRolloverTextColor : sidebarTextColor);
                    g2d.drawString(text, x + 4, rect.height / 2 + fm.getDescent() + 2);
                }
            } else {
                int w = fm.stringWidth(title);
                int iconW = 0;
                if (icon != null) iconW = icon.getIconWidth();
                preferredSize = new Dimension(defSize.width, w + 16 + iconW);
                x = 0;
                if (icon != null) {
                    int iconH = icon.getIconHeight();
                    icon.paintIcon(this, g2d, (rect.width - iconH) / 2, rect.height - 2 - iconW);
                }
                AffineTransform at = g2d.getTransform();
                g2d.translate(0, preferredSize.width - 1);
                g2d.rotate(-1.57);
                g2d.setColor(bm.isRollover() ? sidebarRolloverTextColor : sidebarTextColor);
                g2d.drawString(title, x + (preferredSize.width - w - 8.0F), fm.getHeight() + 2.0F);
                g2d.setTransform(at);
            }
        }

        /**
     * Calculates the button size based on the text length.
     * @todo get the font metrics and get a more accurate size for the text
     * @return the preferred size
     */
        public Dimension getPreferredSize() {
            if (!isVertical) return new Dimension(super.getPreferredSize().width + 8, defSize.height);
            if (preferredSize != null) return preferredSize;
            int textWidth = 16;
            if (fm == null) {
                Graphics g = getGraphics();
                fm = g.getFontMetrics();
                g.dispose();
            }
            textWidth = fm.stringWidth(title) + 16;
            Icon icon = getIcon();
            int iconW = 0;
            if (icon != null) iconW = icon.getIconWidth();
            preferredSize = new Dimension(defSize.width, textWidth + iconW);
            return new Dimension(getFont().getSize() + 16, defSize.height);
        }

        /**
     * Get the minimum size for this component
     * @return the minimum size
     */
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        /**
     * Get the maximum size for this component
     * @return the maximum size
     */
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }
    }
}
