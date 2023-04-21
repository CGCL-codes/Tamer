package ch.laoe.plugin;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import ch.laoe.clip.AClip;
import ch.laoe.clip.ALayer;
import ch.laoe.ui.Debug;
import ch.laoe.ui.GClipEditor;
import ch.laoe.ui.GClipFrame;
import ch.laoe.ui.GLanguage;
import ch.laoe.ui.GPersistence;
import ch.laoe.ui.GToolkit;
import ch.laoe.ui.Laoe;
import ch.oli4.persistence.Persistence;
import ch.oli4.persistence.Persistence.PersistenceListener;

/***********************************************************

This file is part of LAoE.

LAoE is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published
by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

LAoE is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LAoE; if not, write to the Free Software Foundation,
Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


Class:			GPlugin
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	parent class of all plugins.  

History:
Date:			Description:									Autor:
30.08.00		erster Entwurf									oli4
28.03.01		set focussed plugin individually			oli4

***********************************************************/
public abstract class GPlugin implements ActionListener, MouseListener, MouseMotionListener, PersistenceListener {

    /**
	*	constructor
	*/
    public GPlugin(GPluginHandler ph) {
        setPluginHandler(ph);
        persistance = GPersistence.createPersistance();
        persistance.addPersistanceListener(this);
    }

    protected static class Worker implements Runnable {

        public Worker(Runnable r) {
            Debug.println(1, "worker starts");
            runnable = r;
            new Thread(this).start();
        }

        private static Object o = new Object();

        private Runnable runnable;

        private boolean running = false;

        private boolean testSetRunning() {
            synchronized (o) {
                if (running) {
                    return false;
                }
                running = true;
                return true;
            }
        }

        private void clearRunning() {
            synchronized (o) {
                running = false;
            }
        }

        public void run() {
            if (testSetRunning()) {
                try {
                    Debug.println(1, "worker thread begin");
                    runnable.run();
                    Debug.println(1, "worker thread end");
                } catch (Exception e) {
                } finally {
                    clearRunning();
                }
            } else {
                Debug.println(1, "worker thread refused");
            }
        }
    }

    /**
	 * returns the normal one-word name of the plugin. 
	 * @return
	 */
    public abstract String getName();

    private final String getDescription() {
        return getName() + "Description";
    }

    /**
	 * returns a short one-liner text, explaining the function when moving/dragging/clicking the mouse with
	 * the given keys up/down. all 4 combinations are valid. text MUST be translated!
	 * @param shiftDown
	 * @param ctrlDown
	 * @return
	 */
    public final String getKeyMouseHelp(boolean shiftDown, boolean ctrlDown) {
        return getName() + "KeyMouse" + (shiftDown ? "Shift" : "") + (ctrlDown ? "Ctrl" : "");
    }

    protected GPluginHandler pluginHandler;

    protected GPersistence persistance;

    protected void setPluginHandler(GPluginHandler ph) {
        pluginHandler = ph;
    }

    public boolean isVisible() {
        return false;
    }

    protected AClip getFocussedClip() {
        try {
            return pluginHandler.getFocussedClipEditor().getClip();
        } catch (NullPointerException npe) {
            return null;
        }
    }

    protected ALayer getSelectedLayer() {
        try {
            return pluginHandler.getFocussedClipEditor().getClip().getSelectedLayer();
        } catch (NullPointerException npe) {
            return null;
        }
    }

    protected GClipEditor getFocussedClipEditor() {
        return pluginHandler.getFocussedClipEditor();
    }

    protected GClipFrame getFocussedClipFrame() {
        return Laoe.getInstance().getFocussedClipFrame();
    }

    protected void autoScaleFocussedClip() {
        try {
            pluginHandler.getFocussedClipEditor().getClip().getPlotter().autoScale();
        } catch (NullPointerException npe) {
        }
    }

    protected final void repaintFocussedClipEditor() {
        try {
            pluginHandler.getFocussedClipEditor().repaint();
            getFocussedClipFrame().getStatusBar().update();
        } catch (NullPointerException npe) {
        }
    }

    protected final void reloadFocussedClipEditor() {
        try {
            Debug.println(7, "reloadFocussedClipEditor()");
            pluginHandler.reloadAllPlugins(this);
            pluginHandler.getFocussedClipEditor().reload();
        } catch (NullPointerException npe) {
        }
    }

    protected void updateHistory(String s) {
        try {
            getFocussedClip().getHistory().store(loadIcon(), s);
            updateFrameTitle();
        } catch (NullPointerException npe) {
        }
    }

    protected void updateFrameTitle() {
        if (getFocussedClip().getHistory().hasUnsavedModifications()) {
            getFocussedClipFrame().setTitle(getFocussedClip().getName() + " *");
        } else {
            getFocussedClipFrame().setTitle(getFocussedClip().getName());
        }
    }

    protected ImageIcon loadIcon(String iconName) {
        return GToolkit.loadIcon(this, iconName);
    }

    private ImageIcon icon;

    protected final ImageIcon loadIcon() {
        if (icon == null) {
            icon = GToolkit.loadIcon(this, getName());
        }
        return icon;
    }

    public JButton createButton() {
        JButton b = createActionlessButton();
        b.addActionListener(this);
        return b;
    }

    public JButton createActionlessButton() {
        JButton b = new JButton(loadIcon(getName()));
        StringBuilder ttt = new StringBuilder();
        ttt.append("<html><body><b>" + GLanguage.translate(getName()));
        if (this instanceof GPluginFrame) {
            ttt.append("...");
        }
        ttt.append("</b><br>");
        ttt.append(GLanguage.translate(getDescription()));
        ttt.append("</body></html>");
        b.setToolTipText(ttt.toString());
        b.setPreferredSize(new Dimension(26, 26));
        b.setName(getName());
        return b;
    }

    /**
	*	helps the children to create their menuitems. if no icon should be used,
	*	set it to null. if no key accelerator should be used, set it to 0. 
	*/
    private JMenuItem createMenuItem(String iconName, String text, int key) {
        JMenuItem mi;
        String translatedText = GLanguage.translate(text);
        if (this instanceof GPluginFrame) translatedText = new String(translatedText + "...");
        if (iconName != null) {
            mi = new JMenuItem(translatedText, loadIcon(iconName));
        } else {
            mi = new JMenuItem(translatedText);
        }
        if (key != 0) mi.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK));
        mi.addActionListener(this);
        return mi;
    }

    public JMenuItem createMenuItem() {
        return createMenuItem(getName(), getName(), 0);
    }

    protected JMenuItem createMenuItem(int key) {
        return createMenuItem(getName(), getName(), key);
    }

    /**
	*	called each time when this plugin gets active. (e.g. on each plugin change)
	*/
    public void start() {
        Debug.println(5, "plugin " + getClass().getName() + " starts");
        if (isFocussingMouseEvents()) {
            pluginHandler.setFocussedPlugin(this);
        }
        repaintFocussedClipEditor();
    }

    /**
	 * called each time when this plugin should appear and focuss
	 */
    public void focuss() {
        Debug.println(5, "plugin " + getClass().getName() + " focusses");
    }

    /**
	 * returns true if this plugin focusses, to get mouse events
	 * @return
	 */
    protected boolean isFocussingMouseEvents() {
        return false;
    }

    /**
	*	called by ui if a plugin should reload its values (e.g. more often than on focus change)
	*/
    public void reload() {
        Debug.println(5, "plugin " + getClass().getName() + " reloads");
    }

    /**
	*	called by ui when this plugin needs to print onto the clip
	*/
    public void paintOntoClip(Graphics2D g2d, Rectangle rect) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        ((Component) e.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    protected Cursor createCustomCursor(String cursorName) {
        return GToolkit.createCustomCursor(this, cursorName);
    }

    public void onBackup(Persistence p) {
    }

    /**
	*	called from created button or menu-item
	*/
    public final void actionPerformed(ActionEvent e) {
        start();
        focuss();
    }
}
