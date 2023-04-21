package org.nakedobjects.viewer.swing.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

/**
 *  GuiTools is a loose collection of helper methods to deal with Swing's API.
 *
 *  @author beders@xtramind.com
 *  @version $Id: GuiTools.java,v 1.1 2004/06/13 19:56:24 steckman Exp $
 */
public class GuiTools {

    /**
	 * A default border (empty, 5 pixels on all sides).
	 */
    public static final EmptyBorder defaultBorder = new EmptyBorder(5, 5, 5, 5);

    public static int defaultInset = 2;

    /** No possibility to create an instance of GuiTools
	 */
    private GuiTools() {
    }

    /** For inserting constraints in the GridBagLayout manager **/
    public static void constrain(Container container, Component component, int grid_x, int grid_y, int grid_width, int grid_height, int fill, int anchor, double weight_x, double weight_y, int top, int left, int bottom, int right) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = grid_x;
        c.gridy = grid_y;
        c.gridwidth = grid_width;
        c.gridheight = grid_height;
        c.fill = fill;
        c.anchor = anchor;
        c.weightx = weight_x;
        c.weighty = weight_y;
        c.insets = new Insets(top, left, bottom, right);
        ((GridBagLayout) container.getLayout()).setConstraints(component, c);
        container.add(component);
    }

    /** Uses NONE as fill value and WEST as placement. */
    public static void constrain(Container container, Component component, int grid_x, int grid_y, int grid_width, int grid_height) {
        constrain(container, component, grid_x, grid_y, grid_width, grid_height, GridBagConstraints.NONE, GridBagConstraints.WEST, 0.0, 0.0, defaultInset, defaultInset, defaultInset, defaultInset);
    }

    /** This constrain uses fill_x of 1.0 and fill_y of 0.0 */
    public static void constrain(Container container, Component component, int grid_x, int grid_y, int grid_width, int grid_height, int fill, int place) {
        constrain(container, component, grid_x, grid_y, grid_width, grid_height, fill, place, 1.0, 0.0, defaultInset, defaultInset, defaultInset, defaultInset);
    }

    public static void constrain(Container container, Component component, int grid_x, int grid_y, int grid_width, int grid_height, int fill, int place, double weightx, double weighty) {
        constrain(container, component, grid_x, grid_y, grid_width, grid_height, fill, place, weightx, weighty, defaultInset, defaultInset, defaultInset, defaultInset);
    }

    /**
	 * Center the given JInternalFrame into it's root frame
	 * @param aFrame - the JInternalFrame to center
	 */
    public static void center(JInternalFrame aFrame) {
        Dimension d = SwingUtilities.getRoot(aFrame).getSize();
        Dimension s = aFrame.getSize();
        aFrame.setLocation((d.width - s.width) / 2, (d.height - s.height) / 2);
    }

    /** Center a window on the screen. #32134234 **/
    public static void center(Window w) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension s = w.getSize();
        w.setLocation((d.width - s.width) / 2, (d.height - s.height) / 2);
    }

    /** Center a <code>Window</code> over another <code>Window</code>.
	 *
	 * @param upperWindow the window to center
	 * @param lowerWindow the window over them the other window should be centered
	 */
    public static void centerOver(Window upperWindow, Window lowerWindow) {
        Dimension lowerDimension = lowerWindow.getSize();
        Dimension upperDimension = upperWindow.getSize();
        int x = lowerWindow.getLocationOnScreen().x;
        x += (lowerDimension.width - upperDimension.width) / 2;
        if (x < 0) {
            x = 0;
        }
        int y = lowerWindow.getLocationOnScreen().y;
        y += (lowerDimension.height - upperDimension.height) / 2;
        if (y < 0) {
            y = 0;
        }
        upperWindow.setLocation(x, y);
    }

    /** Helper method to support dragging from a component. */
    public static void installDragSupport(JComponent aComponent) {
        MouseMotionListener ml = new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                JComponent c = (JComponent) e.getSource();
                TransferHandler th = c.getTransferHandler();
                th.exportAsDrag(c, e, TransferHandler.COPY);
            }
        };
        aComponent.addMouseMotionListener(ml);
        aComponent.addMouseMotionListener(ml);
    }

    /** Helper method to attach a popup menu listener. */
    public static MouseListener installPopupListener(Component aComponent, JPopupMenu popup, int keyModifiers) {
        MouseListener ml = new PopupListener(popup, keyModifiers);
        aComponent.addMouseListener(ml);
        return ml;
    }

    /** Helper method to attach a popup menu listener. */
    public static MouseListener installPopupListener(Component aComponent, JPopupMenu popup) {
        return installPopupListener(aComponent, popup, -1);
    }

    /** Little helper class for popups. */
    static class PopupListener extends MouseAdapter {

        JPopupMenu popup;

        int modifiers;

        PopupListener(JPopupMenu aPopup, int keyModifiers) {
            popup = aPopup;
            modifiers = keyModifiers;
        }

        /** Invoked when a mouse button has been pressed on a component.
		 *
		 */
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                boolean showMenu = false;
                if (modifiers != -1) {
                    if ((e.getModifiers() & modifiers) == modifiers) {
                        showMenu = true;
                    }
                } else {
                    showMenu = true;
                }
                if (showMenu) popup.show((Component) e.getSource(), e.getX(), e.getY());
            }
        }
    }

    /** Get a list of available Look and Feels.
	 */
    public static LookAndFeelInfo[] getAvailableLookAndFeels() {
        Map allLFs = new HashMap();
        LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < infos.length; i++) {
            allLFs.put(infos[i].getClassName(), infos[i]);
        }
        LookAndFeel current = UIManager.getLookAndFeel();
        LookAndFeelInfo currentInfo = new LookAndFeelInfo(current.getName(), current.getClass().getName());
        allLFs.put(currentInfo.getClassName(), currentInfo);
        String knownLookAndFeels[] = { "com.incors.plaf.kunststoff.KunststoffLookAndFeel", "net.sourceforge.mlf.metouia.MetouiaLookAndFeel", "org.compiere.plaf.CompiereLookAndFeel", "com.digitprop.tonic.TonicLookAndFeel", "com.birosoft.liquid.LiquidLookAndFeel", "com.jgoodies.plaf.windows.ExtWindowsLookAndFeel", "com.jgoodies.plaf.plastic.PlasticLookAndFeel", "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel", "com.jgoodies.plaf.plastic.PlasticXPLookAndFeel", "com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel", "de.muntjak.tinylookandfeel.TinyLookAndFeel" };
        String knownLookAndFeelNames[] = { "Kunststoff", "Metouia", "Compiere", "Tonic", "Liquid", "Looks Extended Windows", "Looks Plastic", "Looks Plastic 3D", "Looks Plastic XP", "Oyoaha", "Tiny" };
        for (int i = 0; i < knownLookAndFeels.length; i++) {
            String lnF = knownLookAndFeels[i];
            if (getLookAndFeelClass(lnF) != null) {
                allLFs.put(lnF, new LookAndFeelInfo(knownLookAndFeelNames[i], lnF));
            }
        }
        int nr = allLFs.size();
        Set sortedSet = new TreeSet(new Comparator() {

            public int compare(Object o1, Object o2) {
                LookAndFeelInfo info1, info2;
                info1 = (LookAndFeelInfo) o1;
                info2 = (LookAndFeelInfo) o2;
                return info1.getName().compareTo(info2.getName());
            }
        });
        sortedSet.addAll(allLFs.values());
        infos = (LookAndFeelInfo[]) sortedSet.toArray(new LookAndFeelInfo[nr]);
        return infos;
    }

    /** Find the class. If not available, return null. */
    protected static Class getLookAndFeelClass(String aClassName) {
        Class result = null;
        try {
            result = Class.forName(aClassName);
        } catch (ClassNotFoundException e) {
        }
        return result;
    }
}
