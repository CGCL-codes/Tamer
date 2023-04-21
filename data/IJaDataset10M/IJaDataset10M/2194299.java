package com.hy.mydesktop.client.component.factory;

import java.util.List;
import java.util.Map;
import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.desktop.client.Shortcut;
import com.extjs.gxt.desktop.client.StartMenu;
import com.extjs.gxt.desktop.client.TaskBar;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.hy.mydesktop.client.component.event.AppEvents;
import com.hy.mydesktop.client.component.meta.AbstractComponetModelEnum;
import com.hy.mydesktop.client.component.meta.DesktopMenuItemModelEnum;
import com.hy.mydesktop.client.component.meta.MenuItemModelEnum;
import com.hy.mydesktop.client.component.meta.ShortcutModelEnum;
import com.hy.mydesktop.client.component.mvc.event.EventStructureMetaModel;
import com.hy.mydesktop.client.mvc.core.event.manager.ComponentTrrigerTemplateUtil;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaNodeModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.ItemOfAssociateToWindow;
import com.hy.mydesktop.shared.persistence.domain.gxt.constant.ComponentMetaDataConstants;
import com.hy.mydesktop.shared.util.log.MyLoggerUtil;

/**
 * 
 * <ul>
 * <li>开发作者：花宏宇</li>
 * <li>设计日期：2010-9-17；时间：上午09:39:37</li>
 * <li>类型名称：DesktopMenuItemFactory</li>
 * <li>设计目的：Desktop的开始菜单下的MenuItem工厂</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
public class DesktopMenuItemFactory {

    /**
	 * 
	 * <ul>
	 * <li>方法含义：</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-9-17；时间：下午上午09:42:03</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param desktop
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    public static Desktop createMenuItem(Desktop desktop, GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        return configureMenuItem(desktop, gxtComponentMetaNodeModel);
    }

    public static MenuItem createMenuItemToDesktop(Desktop desktop, GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        MenuItem menuItem = createDefaultMenuItem();
        AbstractComponetFactory.configureAbstractComponet(menuItem, gxtComponentMetaNodeModel);
        if (gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.TEXT.name().toLowerCase()) != null) {
            menuItem.setText((String) gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.TEXT.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.ID.name().toLowerCase()) != null) {
            menuItem.setId((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.ID.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.ICON.name().toLowerCase()) != null) {
            menuItem.setIconStyle((String) gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.ICON.name().toLowerCase()));
        }
        menuItem = (MenuItem) GxtComponentMetaNodeModelChildrenItemsFactory.configureChildrenItems(menuItem, gxtComponentMetaNodeModel);
        desktop.getTaskBar().getStartMenu().add(menuItem);
        return menuItem;
    }

    private static Desktop configureMenuItem(final Desktop desktop, GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        MenuItem menuItem = createDefaultMenuItem();
        if (gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.TEXT.name().toLowerCase()) != null) {
            menuItem.setText((String) gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.TEXT.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.TITLE.name().toLowerCase()) != null) {
            menuItem.setTitle((String) gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.TITLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.ICON.name().toLowerCase()) != null) {
            menuItem.setIconStyle((String) gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.ICON.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(DesktopMenuItemModelEnum.REGISTEREVENTTYPES.name().toLowerCase()) != null) {
            final Map<String, Object> appEventCodes = gxtComponentMetaNodeModel.get(ShortcutModelEnum.REGISTEREVENTTYPES.name().toLowerCase());
            for (final String code : appEventCodes.keySet()) {
                Integer i = Integer.valueOf(code);
                switch(i) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 5:
                        SelectionListener<MenuEvent> shortcutListener = new SelectionListener<MenuEvent>() {

                            @Override
                            public void componentSelected(MenuEvent ce) {
                                Object data = appEventCodes.get(code);
                                Dispatcher.forwardEvent(AppEvents.DESKTOP_MENUITEM_ONCLICK_INIT, data);
                            }
                        };
                        menuItem.addSelectionListener(shortcutListener);
                        break;
                    case 101:
                        break;
                    default:
                        break;
                }
            }
        }
        desktop.getTaskBar().getStartMenu().add(menuItem);
        return desktop;
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-9-15；时间：下午上午11:05:29</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @return
	 */
    private static MenuItem createDefaultMenuItem() {
        MenuItem menuItem = new MenuItem();
        menuItem.setText("默认的DesktopMenuItem");
        menuItem.setTitle("title");
        menuItem.setIcon(IconHelper.createStyle("accordion"));
        return menuItem;
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：用来响应桌面的快捷方式的单击事件：模拟视窗系统中，窗口的所以显示效果</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-9-8；时间：下午下午03:32:05</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param ce
	 */
    private static void itemSelected(Desktop desktop, ComponentEvent ce) {
        Window w;
        if (ce instanceof MenuEvent) {
            MenuEvent me = (MenuEvent) ce;
            w = me.getItem().getData("window");
        } else {
            w = ce.getComponent().getData("window");
        }
        if (!desktop.getWindows().contains(w)) {
            desktop.addWindow(w);
        }
        if (w != null && !w.isVisible()) {
            w.show();
        } else {
            w.toFront();
        }
    }
}
