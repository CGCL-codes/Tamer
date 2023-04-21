package org.epoline.mice;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

class MiceDesktopPane extends JDesktopPane {

    private JInternalFrame _invisibleInternalFrame;

    private static class FloatToggler extends AbstractAction {

        private MiceInternalFrameForDialogOrFrame m_frame;

        public FloatToggler(MiceInternalFrameForDialogOrFrame frame) {
            m_frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (m_frame.isIcon()) {
                    m_frame.setIcon(false);
                }
            } catch (Exception ignored) {
            }
            m_frame.setFloating(m_frame.isFloating() == false);
        }
    }

    private static class FocusAction extends AbstractAction {

        private JInternalFrame m_frame;

        public FocusAction(String name, Icon icon, JInternalFrame frame) {
            super(name);
            m_frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (m_frame.isIcon()) {
                    m_frame.setIcon(false);
                }
                m_frame.setSelected(true);
            } catch (Exception ignored) {
            }
        }
    }

    private class PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) getPopup().show(e.getComponent(), e.getX(), e.getY());
        }

        public void mouseReleased(MouseEvent e) {
            mousePressed(e);
        }
    }

    private static class SwingPopupBugFix implements PopupMenuListener {

        private JMenu m_submenu;

        private JPopupMenu m_popup;

        public SwingPopupBugFix(JMenu submenu, JPopupMenu popup) {
            m_submenu = submenu;
            m_popup = popup;
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent ignored) {
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent ignored) {
        }

        public void popupMenuCanceled(PopupMenuEvent ignored) {
            m_submenu.getPopupMenu().setVisible(false);
        }
    }

    /**
	 * MiceDesktopPane constructor comment.
	 */
    public MiceDesktopPane() {
        super();
        initialize();
    }

    public Component add(Component comp) {
        Component result = super.add(comp);
        if (comp instanceof JInternalFrame.JDesktopIcon) {
            comp.setBounds(0, 0, 0, 0);
        }
        return result;
    }

    /** 
	 * Showing dialogs always requires a parent frame in Swing, and that parent frame must be 
	 * showing onscreen. Sometimes this is not desirable, for instance if the product has no 
	 * frame at all. This method helps to overcome this challenge. It creates a new frame on 
	 * the screen, but makes it size (0,0) so
	 * that it is still invisible . . . (sorry Matt)
	 */
    public JInternalFrame getInvisibleInternalFrame() {
        if (_invisibleInternalFrame == null) {
            _invisibleInternalFrame = new JInternalFrame();
            _invisibleInternalFrame.setFrameIcon(MiceGlobal.getIcon(this, "smallEpo.gif"));
            _invisibleInternalFrame.setSize(0, 0);
            add(_invisibleInternalFrame);
        }
        return _invisibleInternalFrame;
    }

    private JPopupMenu getPopup() {
        Dimension nul = new Dimension(0, 0);
        JInternalFrame[] frames = getAllFrames();
        JPopupMenu popup = new JPopupMenu();
        JMenu submenu = new JMenu("Always on Top");
        submenu.setEnabled(false);
        for (int i = 0; i < frames.length; i++) {
            JInternalFrame frame = frames[i];
            try {
                frame.setSelected(false);
            } catch (Exception ignored) {
            }
            String name = frame.getTitle();
            Icon icon = frame.getFrameIcon();
            if (frame.getSize().equals(nul) == false && frame.isClosed() == false && frame.isVisible() == true) {
                popup.add(new FocusAction(name, icon, frame));
                if (frame instanceof MiceInternalFrameForDialogOrFrame) {
                    MiceInternalFrameForDialogOrFrame floater = (MiceInternalFrameForDialogOrFrame) frame;
                    JCheckBoxMenuItem item = new JCheckBoxMenuItem(name, floater.isFloating());
                    item.addActionListener(new FloatToggler(floater));
                    submenu.add(item);
                    submenu.setEnabled(true);
                }
            }
        }
        popup.addSeparator();
        popup.add(new SettingsIB().getLaunchAction());
        popup.add(submenu);
        popup.addSeparator();
        popup.add(new ExitIB().getLaunchAction());
        popup.addPopupMenuListener(new SwingPopupBugFix(submenu, popup));
        return popup;
    }

    public void initialize() {
        putClientProperty("JDesktopPane.dragMode", "outline");
        addMouseListener(new PopupListener());
        setMinimumSize(new Dimension(30, 30));
        setName("MiceDesktopPane");
    }

    /**
	 * To be removed when switching from JDK 1.2 to JDK 1.3,
	 * because JDesktopPane offers a function like this since JDK 1.3
	 */
    public void setSelectedFrame(MiceInternalFrame f) {
    }
}
