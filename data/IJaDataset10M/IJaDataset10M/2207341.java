package org.makagiga.desktop;

import static org.makagiga.commons.UI._;
import java.awt.Point;
import java.beans.PropertyVetoException;
import javax.swing.Action;
import org.makagiga.commons.*;
import org.makagiga.plugins.PluginInfo;

public final class DesktopToolBar {

    private static DesktopToolBar _instance;

    private MButton addWidgetButton;

    public static synchronized DesktopToolBar getInstance() {
        if (_instance == null) _instance = new DesktopToolBar();
        return _instance;
    }

    public static synchronized boolean isInstance() {
        return _instance != null;
    }

    public void setVisible(final boolean value) {
        addWidgetButton.setVisible(value);
    }

    public void updateToolBar(final MToolBar toolBar, final Action toggleDesktopAction) {
        addWidgetButton = new MButton(_("Add Widget"), "ui/newfile") {

            @Override
            protected MMenu onPopupMenu() {
                MMenu menu = new MMenu();
                Desktop.getInstance().createNewWidgetMenu(menu, null);
                return menu;
            }
        };
        addWidgetButton.setPopupMenuEnabled(true);
        toolBar.addButton(addWidgetButton, MToolBar.SHOW_TEXT);
        toolBar.add(toggleDesktopAction);
        toolBar.addSeparator();
    }

    private DesktopToolBar() {
    }

    /**
	 * @since 2.0
	 */
    public static final class NewWidgetAction extends MAction {

        private PluginInfo info;

        private Point widgetLocation;

        public NewWidgetAction(final PluginInfo info, final Point widgetLocation) {
            super(info.toString(), info.getIcon());
            this.info = info;
            this.widgetLocation = widgetLocation;
            setHTMLHelp(info.shortDescription.get());
        }

        @Override
        public void onAction() {
            Widget widget = Desktop.getInstance().addWidget(info.getID(), widgetLocation);
            if (widget == null) return;
            widget.select();
        }
    }

    /**
	 * @since 2.0
	 */
    public static final class SelectWidgetAction extends MAction {

        private Widget widget;

        public SelectWidgetAction(final Widget widget) {
            super(widget.getTitle(), widget.getFrameIcon());
            this.widget = widget;
        }

        @Override
        public void onAction() {
            try {
                if (widget.isSelected()) widget.setIcon(!widget.isIcon()); else widget.setIcon(false);
                widget.setSelected(true);
            } catch (PropertyVetoException exception) {
            }
        }
    }
}
