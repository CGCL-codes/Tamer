package org.jrichclient.richdock.dockingport.tabbar;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import org.jrichclient.richdock.Dockable;
import org.jrichclient.richdock.helper.DragHelper;
import org.jrichclient.richdock.icons.ImageResources;

@SuppressWarnings("serial")
public class TabBarComponent extends TabComponent {

    private final TabBarDockingPort dockingPort;

    private final Dockable dockable;

    private final DragHelper dragHelper;

    private final PropertyChangeListener dockableListener;

    private final MouseListener tabMouseListener;

    public TabBarComponent(TabBarDockingPort dockingPort, Dockable dockable, Rotation rotation) {
        super(dockable.getTitle(), ImageResources.createIcon(dockable.getIconFile()), SwingConstants.LEADING, rotation);
        this.dockingPort = dockingPort;
        this.dockable = dockable;
        dragHelper = new DragHelper(dockable, this);
        dockableListener = new DockableListener();
        dockable.addPropertyChangeListener(dockableListener);
        tabMouseListener = new TabMouseListener();
        addMouseListener(tabMouseListener);
    }

    public Dockable getDockable() {
        return dockable;
    }

    private class DockableListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent event) {
            String propertyName = event.getPropertyName();
            Object newValue = event.getNewValue();
            if (Dockable.PROPERTYNAME_TITLE.equals(propertyName)) setText((String) newValue); else if (Dockable.PROPERTYNAME_ICON_FILE.equals(propertyName)) setIcon(ImageResources.createIcon((String) newValue)); else if (Dockable.PROPERTYNAME_TOOL_TIP_TEXT.equals(propertyName)) setToolTipText((String) newValue);
        }
    }

    private class TabMouseListener extends MouseAdapter {

        private Border defaultBorder;

        private Border hoverBorder;

        public TabMouseListener() {
            defaultBorder = new RoundedRectBorder(getBackground(), Color.GRAY, 4, 4, 1);
            hoverBorder = new RoundedRectBorder(getBackground(), Color.BLACK, 4, 4, 1);
        }

        @Override
        public void mouseEntered(MouseEvent event) {
            setBorder(hoverBorder);
        }

        @Override
        public void mouseExited(MouseEvent event) {
            setBorder(defaultBorder);
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            dockingPort.setSelectedDockable(getDockable());
        }

        @Override
        public void mousePressed(MouseEvent event) {
            maybeShowPopup(event);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            maybeShowPopup(event);
        }

        private void maybeShowPopup(MouseEvent event) {
            if (event.isPopupTrigger()) {
                JPopupMenu popupMenu = dockable.getPopupMenu();
                if (popupMenu != null) popupMenu.show(event.getComponent(), event.getX(), event.getY());
            }
        }
    }

    public void dispose() {
        dragHelper.dispose();
        dockable.removePropertyChangeListener(dockableListener);
        removeMouseListener(tabMouseListener);
    }
}
