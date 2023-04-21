package de.forsthaus.common.menu.tree;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Treecell;
import de.forsthaus.common.menu.util.ILabelElement;

/**
 * @author bbruhns
 * @changes 12/10/2009:sge opens the zul-file window in a tab.<br>
 * 
 */
public class DefaultTreecell extends Treecell implements EventListener, Serializable, ILabelElement {

    private static final long serialVersionUID = 5221385297281381652L;

    private static final Logger logger = Logger.getLogger(DefaultTreecell.class);

    private String zulNavigation;

    @Override
    public void onEvent(Event event) throws Exception {
        try {
            final int workWithTabs = 1;
            if (workWithTabs == 1) {
                final Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
                final Center center = bl.getCenter();
                final Tabs tabs = (Tabs) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter");
                Tab checkTab = null;
                try {
                    checkTab = (Tab) tabs.getFellow("tab_" + this.getId());
                    checkTab.setSelected(true);
                } catch (final ComponentNotFoundException ex) {
                }
                if (checkTab == null) {
                    final Tab tab = new Tab();
                    tab.setId("tab_" + this.getId());
                    tab.setLabel(this.getLabel().trim());
                    tab.setClosable(true);
                    tab.setParent(tabs);
                    final Tabpanels tabpanels = (Tabpanels) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter").getFellow("tabpanelsBoxIndexCenter");
                    final Tabpanel tabpanel = new Tabpanel();
                    tabpanel.setHeight("100%");
                    tabpanel.setStyle("padding: 0px;");
                    tabpanel.setParent(tabpanels);
                    Executions.createComponents(getZulNavigation(), tabpanel, null);
                    tab.setSelected(true);
                }
            } else {
                final Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
                final Center center = bl.getCenter();
                center.getChildren().clear();
                Executions.createComponents(getZulNavigation(), center, null);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("-->[" + getId() + "] calling zul-file: " + getZulNavigation());
            }
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    private String getZulNavigation() {
        return this.zulNavigation;
    }

    @Override
    public void setZulNavigation(String zulNavigation) {
        this.zulNavigation = zulNavigation;
        if (!StringUtils.isEmpty(zulNavigation)) {
            addEventListener("onClick", this);
        }
    }
}
