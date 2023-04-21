package org.tolven.ae.helper;

import java.io.File;
import java.util.ArrayList;
import org.tolven.app.entity.AccountMenuStructure;
import org.tolven.app.entity.MSColumn;
import org.tolven.core.entity.Account;
import org.tolven.core.entity.AccountType;
import org.tolven.logging.TolvenLogger;
import org.tolven.menuStructure.Alignment;
import org.tolven.menuStructure.Application;
import org.tolven.menuStructure.Column;
import org.tolven.menuStructure.List;
import org.tolven.menuStructure.Menu;
import org.tolven.menuStructure.MenuBase;
import org.tolven.menuStructure.Placeholder;
import org.tolven.menuStructure.Portal;
import org.tolven.menuStructure.Portlet;
import org.tolven.menuStructure.PortletColumn;
import org.tolven.menuStructure.TrimList;
import org.tolven.teditor.dbaccess.AMSValueObject;

public class ApplicationBuilder {

    private java.util.List<AccountMenuStructure> rowsFromDb;

    AccountType accountType;

    Account account;

    java.util.List<MSColumn> columns;

    int depth = 1;

    private Application application;

    public ApplicationBuilder(AMSValueObject ms) {
        this.accountType = ms.getAccountType();
        this.account = ms.getAccount();
        this.rowsFromDb = ms.getRowsFromDb();
        this.columns = ms.getColumns();
    }

    public void buildApplication() {
        addApplication(rowsFromDb.get(0));
    }

    public Application getApplication() {
        buildApplication();
        return application;
    }

    private void addApplication(AccountMenuStructure row) {
        application = new Application();
        application.setCreatable(accountType.isCreatable());
        application.setCreateAccountPage(accountType.getCreateAccountPage());
        application.setCss(accountType.getCSS());
        application.setHomePage(accountType.getHomePage());
        application.setLogo(accountType.getLogo());
        application.setTitle(accountType.getLongDesc());
        application.setName(accountType.getKnownType());
        application.getRules().add("rules" + File.separator + accountType.getKnownType() + ".drl");
        Menu rootMenu = new Menu();
        rootMenu.setName(row.getNode());
        rootMenu.setPage(row.getTemplate());
        rootMenu.setTitle(row.getText());
        rootMenu.setVisible(row.getVisible());
        depth++;
        for (AccountMenuStructure child : getChildren(row)) {
            if ("tab".equals(child.getRole())) {
                rootMenu.getMenusAndPortalsAndTimelines().add(getMenu(child));
            } else if ("placeholder".equals(child.getRole())) {
                if (child.getSequence() < 0) {
                    application.getTrimMenus().add(getTrimMenu(child));
                } else {
                    application.getPlaceholders().add(getPlaceholder(child));
                }
            }
        }
        depth--;
        application.setMenu(rootMenu);
    }

    private Placeholder getTrimMenu(AccountMenuStructure child) {
        Placeholder trimmenu = new Placeholder();
        trimmenu.setHeading(child.getMenuTemplate());
        trimmenu.setName(child.getNode());
        trimmenu.setPage(child.getTemplate());
        trimmenu.setTitle(child.getText());
        trimmenu.setVisible(child.getVisible());
        return trimmenu;
    }

    private MenuBase getMenu(AccountMenuStructure child) {
        Menu menu = new Menu();
        menu.setName(child.getNode());
        menu.setPage(child.getTemplate());
        menu.setTitle(child.getText());
        menu.setVisible(child.getVisible());
        depth++;
        for (AccountMenuStructure childMenu : getChildren(child)) {
            if ("list".equals(childMenu.getRole())) {
                menu.getMenusAndPortalsAndTimelines().add(getList(childMenu));
            } else if ("trimlist".equals(childMenu.getRole())) {
                menu.getMenusAndPortalsAndTimelines().add(getTrimlist(childMenu));
            } else if ("tab".equals(childMenu.getRole())) {
                menu.getMenusAndPortalsAndTimelines().add(getMenu(childMenu));
            }
        }
        depth--;
        return menu;
    }

    private MenuBase getTrimlist(AccountMenuStructure node) {
        TrimList trimlist = new TrimList();
        trimlist.setName(node.getNode());
        trimlist.setPage(node.getTemplate());
        trimlist.setPlaceholder(node.getReferenced().getNode());
        trimlist.setTitle(node.getText());
        trimlist.setVisible(node.getVisible());
        depth++;
        java.util.List<Column> columns = trimlist.getColumns();
        columns.addAll(getColumns(node));
        depth--;
        return trimlist;
    }

    private MenuBase getList(AccountMenuStructure node) {
        List list = new List();
        list.setDrilldown(node.getRepeating());
        list.setName(node.getNode());
        list.setPage(node.getTemplate());
        list.setTitle(node.getText());
        list.setVisible(node.getVisible());
        java.util.List<Column> columns = list.getColumns();
        columns.addAll(getColumns(node));
        return list;
    }

    private Placeholder getPlaceholder(AccountMenuStructure node) {
        Placeholder placeholder = new Placeholder();
        placeholder.setHeading(node.getMenuTemplate());
        placeholder.setName(node.getNode());
        placeholder.setPage(node.getTemplate());
        placeholder.setTitle(node.getText());
        placeholder.setVisible(node.getVisible());
        depth++;
        for (AccountMenuStructure child : getChildren(node)) {
            if ("placeholder".equals(child.getRole())) {
                placeholder.getPlaceholders().add(getPlaceholder(child));
            } else if ("tab".equals(child.getRole())) {
                placeholder.getMenusAndPortalsAndTimelines().add(getMenu(child));
            } else if ("portal".equals(child.getRole())) {
                placeholder.getMenusAndPortalsAndTimelines().add(getPortal(child));
            }
        }
        depth--;
        return placeholder;
    }

    private MenuBase getPortal(AccountMenuStructure node) {
        Portal portal = new Portal();
        portal.setName(node.getNode());
        portal.setPage(node.getTemplate());
        portal.setTitle(node.getText());
        portal.setVisible(node.getVisible());
        depth++;
        for (AccountMenuStructure child : getChildren(node)) {
            if (child.getColumnNumber() > 0) {
                java.util.List<PortletColumn> portletcolumns = portal.getPortletColumns();
                while (portletcolumns.size() < (child.getColumnNumber())) portletcolumns.add(new PortletColumn());
                PortletColumn pc = portletcolumns.get(child.getColumnNumber() - 1);
                pc.getPortlets().add(getPortlet(child));
            }
        }
        depth--;
        return portal;
    }

    private Portlet getPortlet(AccountMenuStructure p) {
        Portlet portlet = new Portlet();
        portlet.setDrilldown(p.getRepeating());
        portlet.setName(p.getNode());
        portlet.setPage(p.getTemplate());
        portlet.setTitle(p.getText());
        portlet.setVisible(p.getVisible());
        return portlet;
    }

    protected void debug(AccountMenuStructure node) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < depth; i++) {
            buff.append("\t");
        }
        TolvenLogger.info(buff.toString(), ApplicationBuilder.class);
        TolvenLogger.info(node.getNode() + "(" + node.getRole() + ")", ApplicationBuilder.class);
    }

    private java.util.List<AccountMenuStructure> getChildren(AccountMenuStructure parent) {
        java.util.List<AccountMenuStructure> children = new ArrayList<AccountMenuStructure>();
        for (AccountMenuStructure row : rowsFromDb) {
            if (row.getParent() == parent) {
                children.add(row);
            }
        }
        return children;
    }

    private java.util.List<Column> getColumns(AccountMenuStructure node) {
        java.util.List<Column> cols = new ArrayList<Column>();
        for (MSColumn col : columns) {
            if (col.getMenuStructure().getId() == node.getId()) {
                Column msc = new Column();
                msc.setAlign(Alignment.fromValue(col.getAlign()));
                msc.setName(col.getHeading());
                msc.setVisible(col.getVisible());
                msc.setWidth(col.getWidth());
                if ("reference".equals(col.getInternal())) {
                    msc.setReference(true);
                    if (col.getDisplayFunction() != null) {
                        msc.setFormat(col.getDisplayFunction());
                        msc.setInternal(col.getDisplayFunctionArguments());
                    } else {
                    }
                } else if ("instantiate".equals(col.getInternal())) {
                    msc.setInstantiate(true);
                    msc.setInternal(col.getDisplayFunction());
                } else {
                    msc.setInternal(col.getInternal());
                    msc.setFormat(col.getDisplayFunction());
                }
                cols.add(msc);
            }
        }
        return cols;
    }
}
