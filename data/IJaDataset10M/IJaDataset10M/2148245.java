package com.hy.mydesktop.client.component.factory;

import java.util.List;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.hy.mydesktop.client.component.meta.MenuModelEnum;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaNodeModel;

/**
 * 
 * <ul>
 * <li>开发作者：汤莉</li>
 * <li>设计日期：2010-8-30；时间：上午10:30:16</li>
 * <li>类型名称：MenuFactory</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
public class MenuFactory {

    /**
	 * 
	 * <ul>
	 * <li>方法含义：创建菜单</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-8-30；时间：下午上午10:30:43</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaModel
	 * @return
	 * 
	 */
    public static Menu createMenu(GxtComponentMetaNodeModel gxtComponentMetaModel) {
        return configurationMenu(gxtComponentMetaModel);
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：配置Menu的属性</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-8-30；时间：下午上午10:36:58</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    private static Menu configurationMenu(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        Menu menu = createDefaultMenu();
        menu = AbstractComponetFactory.configureAbstractComponet(menu, gxtComponentMetaNodeModel);
        if (gxtComponentMetaNodeModel.get(MenuModelEnum.SCROLL.name().toLowerCase()) != null) {
            menu.setEnableScrolling((Boolean) gxtComponentMetaNodeModel.get(MenuModelEnum.SCROLL.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(MenuModelEnum.MAXHEIGHT.name().toLowerCase()) != null) {
            menu.setMaxHeight((Integer) gxtComponentMetaNodeModel.get(MenuModelEnum.MAXHEIGHT.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(MenuModelEnum.ADD.name().toLowerCase()) != null) {
            List<BaseTreeModel> list = gxtComponentMetaNodeModel.get(MenuModelEnum.MENUITEM2.name().toLowerCase());
            for (BaseTreeModel j : list) {
                gxtComponentMetaNodeModel.set("text1", j.get("name"));
                if (j.getChildCount() > 0) {
                    gxtComponentMetaNodeModel.set("submenu", j.getChildren());
                }
                MenuItem item = MenuItemFactory.createMenuItem(gxtComponentMetaNodeModel);
                menu.add(item);
            }
        }
        return menu;
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：创建默认的Menu</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-8-30；时间：下午上午10:37:50</li>
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
    private static Menu createDefaultMenu() {
        Menu menu = new Menu();
        return menu;
    }
}
