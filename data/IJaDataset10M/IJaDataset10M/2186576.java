package com.gorillalogic.dex.taglib;

import javax.servlet.jsp.*;
import com.gorillalogic.dal.Universe;
import com.gorillalogic.dal.Table;
import com.gorillalogic.dex.*;
import com.gorillalogic.dal.*;
import org.apache.log4j.Logger;

/**
 *
 * @author  Stu
 */
public class MenuTag extends GorillaTag {

    Logger logger = Logger.getLogger(MenuTag.class);

    private static int TAB = 4;

    public int doStartTag() throws JspTagException {
        if (_class == null) {
            _class = "dexMenuItem";
        }
        MenuBean menu = (MenuBean) pageContext.getSession().getAttribute(Names.MENU);
        PkgTable root = null;
        if (menu == null) {
            try {
                root = Universe.factory.defaultDataWorld().getRootPkg();
            } catch (OperationException e) {
                String msg = "Error getting root package";
                JspTagException ex = new JspTagException(msg);
                logger.error(msg, e);
                throw ex;
            }
            try {
                menu = new MenuBean(root);
            } catch (DexException e) {
                throw new JspTagException("Error retrieving menu bean");
            }
            pageContext.getSession().setAttribute(Names.MENU, menu);
        }
        String[] attrs = { attr("class", "dexMenu") };
        out(tag("div", attrs, displayMenu(menu, 0)));
        return SKIP_BODY;
    }

    public String displayMenu(MenuBean menu, int indent) throws JspTagException {
        String folder = Icons.Small.FOLDER;
        String openfolder = Icons.Small.FOLDER_OPEN;
        String minus = Icons.Small.MINUS;
        String plus = Icons.Small.PLUS;
        String table = Icons.Small.TABLE;
        String folderlink = img(Icons.Small.FOLER_SPACE);
        String folderspace = img(Icons.Small.FOLER_SPACE);
        StringBuffer spacer = new StringBuffer();
        for (int i = 0; i < indent - 1; i++) {
            spacer.append(folderspace);
        }
        if (indent > 0) {
            spacer.append(folderlink);
        }
        StringBuffer s = new StringBuffer();
        TableBean[] tableBeans;
        try {
            tableBeans = menu.getTables();
        } catch (DexException e) {
            throw new JspTagException("Error getting table");
        }
        for (int i = 0; i < tableBeans.length; i++) {
            TableBean t = tableBeans[i];
            String[] attrs1 = { attr("class", _class), attr("width", "100%") };
            s.append(tag("div", attrs1, spacer + a("getTable.dex", null, table, (String) t.getName() + "(" + t.getRowCount() + ")", "setNext=dexTable=" + t.toString())));
        }
        MenuBean[] menuBeans = menu.getSubMenus();
        for (int i = 0; i < menuBeans.length; i++) {
            MenuBean item = menuBeans[i];
            String name = item.getName();
            String id = item.getMenuID();
            String[] attrs1 = { attr("class", _class) };
            if (((MenuBean) item).isOpen()) {
                s.append(tag("div", attrs1, spacer + a("setBeanProps.dex", null, minus, img(openfolder, null) + name, Names.MENU + ".closed=" + id)));
                s.append(displayMenu((MenuBean) item, indent + 1));
            } else {
                s.append(tag("div", attrs1, spacer + a("setBeanProps.dex", null, plus, img(folder, null) + name, Names.MENU + ".open=" + id)));
            }
        }
        return s.toString();
    }
}
