package org.kwantu.zakwantu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.kwantu.app.WicketApplicationController;
import org.kwantu.app.context.ApplicationContext;
import org.kwantu.m2.model.ui.KwantuPage;
import org.kwantu.m2.model.ui.KwantuPanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This page renders a list of Kwantu User Interface pages
 */
public class KwantuPageListPanel extends Panel {

    private static final Log log = LogFactory.getLog(KwantuPageListPanel.class);

    public KwantuPageListPanel(String id) {
        super(id);
        init();
    }

    protected WicketApplicationController getController() {
        return ((ZakwantuApplication) getApplication()).getController();
    }

    protected void init() {
        WicketApplicationController controller = getController();
        if (controller == null) {
            log.error("controller is null");
            return;
        }
        makeList(WicketApplicationController.KEY_APPLICATION_RUNTIME, "userPermissionList");
        makeList(WicketApplicationController.KEY_APPLICATION, "pagelist");
        makeList(WicketApplicationController.KEY_M2BROWSER, "m2pagelist");
    }

    private void makeList(final String key, final String wicketId) {
        ApplicationContext appContext = getController().getApplicationContext(key);
        List pageNames = new ArrayList();
        if (appContext != null) {
            log.debug("Finding context for " + key + " context " + appContext);
            for (KwantuPanel p : appContext.getUiPanels()) {
                if (p instanceof KwantuPage && p.isAbsolutePath()) {
                    pageNames.add(p.getName());
                }
            }
        }
        Collections.sort(pageNames);
        ListView listView = new ListView(wicketId, pageNames) {

            @Override
            public void populateItem(final ListItem item) {
                final String pageName = (String) item.getModelObject();
                Link link = new Link("link", new Model(pageName)) {

                    @Override
                    public void onClick() {
                        KwantuPageListPanel.this.getController().setCurrentContext(key);
                        setResponsePage(new KwantuPageRenderer(pageName));
                    }
                };
                link.add(new Label("name", new Model(pageName)));
                item.add(link);
            }
        };
        addOrReplace(listView);
    }
}
