package com.hy.mydesktop.client.component.factory;

import java.util.List;
import java.util.Map;
import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.desktop.client.Shortcut;
import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.hy.mydesktop.client.component.event.AppEvents;
import com.hy.mydesktop.client.component.meta.ButtonModelEnum;
import com.hy.mydesktop.client.component.meta.ShortcutModelEnum;
import com.hy.mydesktop.client.component.mvc.controller.meta.WindowControllerModelEnum;
import com.hy.mydesktop.client.component.mvc.event.EventStructureMetaModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaNodeModel;

/**
 * 
 * <ul>
 * <li>开发作者：花宏宇</li>
 * <li>设计日期：2010-9-9；时间：下午01:09:41</li>
 * <li>类型名称：ShortcutFactory</li>
 * <li>设计目的：ButtonFactory用于创建按钮，并且根据GxtComponentMetaModel的信息，
 * 对按钮进行个性化配置</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
public class ShortcutFactory {

    public static Desktop createShortcut(Desktop desktop, GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        return createBaseShortcut(desktop, gxtComponentMetaNodeModel);
    }

    /**
	 * 依据GxtComponentMetaModel对按钮，进行个性化配置
	 * @param gxtComponentMetaModel
	 * @return
	 */
    private static Desktop createBaseShortcut(final Desktop desktop, GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        Shortcut shortcut = createDefaultShortcut();
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.TEXT.name().toLowerCase()) != null) {
            shortcut.setText((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.TEXT.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.TITLE.name().toLowerCase()) != null) {
            shortcut.setTitle((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.TITLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.ID.name().toLowerCase()) != null) {
            shortcut.setId((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.ID.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.ICON.name().toLowerCase()) != null) {
            shortcut.setIconStyle((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.ICON.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.DATA.name().toLowerCase()) != null) {
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.REGISTEREVENTTYPES.name().toLowerCase()) != null) {
            final Map<String, EventStructureMetaModel> appEventCodes = gxtComponentMetaNodeModel.get(ShortcutModelEnum.REGISTEREVENTTYPES.name().toLowerCase());
            for (final String code : appEventCodes.keySet()) {
                Integer i = Integer.valueOf(code);
                switch(i) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        SelectionListener<ComponentEvent> shortcutListener = new SelectionListener<ComponentEvent>() {

                            @Override
                            public void componentSelected(ComponentEvent ce) {
                                Map<String, Object> data = (Map<String, Object>) appEventCodes.get(code);
                                EventStructureMetaModel eventstructuremetamodel = (EventStructureMetaModel) data.get("eventstructuremetamodel");
                                System.out.println("Shortcut触发AppEvents.DESKTOP_SHORTCUT_ONCLICK_INIT事件:" + "控制器的Controller Id 是" + eventstructuremetamodel.getControllerId() + "" + eventstructuremetamodel.getResponseModeId());
                                Dispatcher.forwardEvent(AppEvents.DESKTOP_SHORTCUT_ONCLICK_INIT, data);
                            }
                        };
                        shortcut.addSelectionListener(shortcutListener);
                        break;
                    case 101:
                        break;
                    default:
                        break;
                }
            }
        }
        desktop.addShortcut(shortcut);
        return desktop;
    }

    /**
	 * DefaultButton的基本UI效果
	 * 按钮显示的文字是“确定”
	 * 按钮中等大小
	 * @return
	 */
    private static Shortcut createDefaultShortcut() {
        Shortcut s1 = new Shortcut();
        s1.setText("xxxx Shortcut");
        s1.setId("grid-win-shortcut");
        s1.setTitle("new Shortcut");
        return s1;
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

    public static Shortcut createShortcutToDesktop(Desktop desktop, GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        Shortcut shortcut = createDefaultShortcut();
        shortcut = AbstractComponetFactory.configureAbstractComponet(shortcut, gxtComponentMetaNodeModel);
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.TEXT.name().toLowerCase()) != null) {
            shortcut.setText((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.TEXT.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.TITLE.name().toLowerCase()) != null) {
            shortcut.setTitle((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.TITLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.ID.name().toLowerCase()) != null) {
            shortcut.setId((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.ID.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.ICON.name().toLowerCase()) != null) {
            shortcut.setIconStyle((String) gxtComponentMetaNodeModel.get(ShortcutModelEnum.ICON.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.DATA.name().toLowerCase()) != null) {
        }
        if (gxtComponentMetaNodeModel.get(ShortcutModelEnum.REGISTEREVENTTYPES.name().toLowerCase()) != null) {
            final Map<String, Object> appEventCodes = gxtComponentMetaNodeModel.get(ShortcutModelEnum.REGISTEREVENTTYPES.name().toLowerCase());
            for (final String code : appEventCodes.keySet()) {
                Integer i = Integer.valueOf(code);
                switch(i) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        SelectionListener<ComponentEvent> shortcutListener = new SelectionListener<ComponentEvent>() {

                            @Override
                            public void componentSelected(ComponentEvent ce) {
                                Map<String, Object> data = (Map<String, Object>) appEventCodes.get(code);
                                EventStructureMetaModel eventstructuremetamodel = (EventStructureMetaModel) data.get("eventstructuremetamodel");
                                System.out.println("Shortcut触发AppEvents.DESKTOP_SHORTCUT_ONCLICK_INIT事件:" + "控制器的Controller Id 是" + eventstructuremetamodel.getControllerId() + "" + eventstructuremetamodel.getResponseModeId());
                                Dispatcher.forwardEvent(AppEvents.DESKTOP_SHORTCUT_ONCLICK_INIT, data);
                            }
                        };
                        shortcut.addSelectionListener(shortcutListener);
                        break;
                    case 4:
                        SelectionListener<ComponentEvent> shortcutListener2 = new SelectionListener<ComponentEvent>() {

                            @Override
                            public void componentSelected(ComponentEvent ce) {
                                Map<String, Object> data = (Map<String, Object>) appEventCodes.get(code);
                                EventStructureMetaModel eventstructuremetamodel = (EventStructureMetaModel) data.get("eventstructuremetamodel");
                                System.out.println("Shortcut触发AppEvents.DESKTOP_SHORTCUT_ONCLICK_INIT事件:" + eventstructuremetamodel.getControllerId() + eventstructuremetamodel.getResponseModeId());
                                Dispatcher.forwardEvent(AppEvents.DESKTOP_SHORTCUT_ONCLICK, data);
                            }
                        };
                        shortcut.addSelectionListener(shortcutListener2);
                        break;
                    case 101:
                        break;
                    default:
                        break;
                }
            }
        }
        desktop.addShortcut(shortcut);
        return shortcut;
    }
}
