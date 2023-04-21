package org.gcreator.pineapple.gui;

import java.awt.FlowLayout;
import java.lang.ref.WeakReference;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.gcreator.pineapple.events.Event;

/**
 * An event tab renderer. Puts the image on the left of the tab.
 * @author Luís Reis
 */
public class EventTabRenderer extends JPanel {

    private static final long serialVersionUID = 721764836069516400L;

    private WeakReference<JTabbedPane> tabs;

    public EventTabRenderer(JTabbedPane tabs) {
        this.tabs = new WeakReference<JTabbedPane>(tabs);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);
        IconFace i = new IconFace();
        i.setVisible(true);
        add(i);
        TabFace t = new TabFace();
        t.setVisible(true);
        add(t);
    }

    private class IconFace extends JLabel {

        private static final long serialVersionUID = 2550458438833011687L;

        @Override
        public Icon getIcon() {
            JTabbedPane t = tabs.get();
            String s = t.getTitleAt(t.indexOfTabComponent(EventTabRenderer.this));
            if (s.equals(Event.TYPE_CREATE)) {
                return EventCellRenderer.CREATE_IMAGE;
            } else if (s.equals(Event.TYPE_DESTROY)) {
                return EventCellRenderer.DESTROY_IMAGE;
            } else if (s.equals(Event.TYPE_DRAW)) {
                return EventCellRenderer.DRAW_IMAGE;
            } else if (s.equals(Event.TYPE_UPDATE)) {
                return EventCellRenderer.UPDATE_IMAGE;
            } else if (s.equals(Event.TYPE_KEYPRESS)) {
                return EventCellRenderer.KEY_IMAGE;
            } else if (s.equals(Event.TYPE_KEYRELEASE)) {
                return EventCellRenderer.KEY_IMAGE;
            } else if (s.equals(Event.TYPE_KEYPRESSED)) {
                return EventCellRenderer.KEY_IMAGE;
            }
            return null;
        }
    }

    private class TabFace extends JLabel {

        private static final long serialVersionUID = 2550458438833011687L;

        @Override
        public String getText() {
            try {
                JTabbedPane t = tabs.get();
                return t.getTitleAt(t.indexOfTabComponent(EventTabRenderer.this));
            } catch (Exception e) {
                return "";
            }
        }
    }
}
