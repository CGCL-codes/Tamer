package org.fspmboard.client.pages;

import org.fspmboard.client.Main;
import org.fspmboard.client.widgets.IntegrationPanel;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

/**
 * @author Holz, Roberto
 * 24.01.2009 | 14:24:03
 *
 */
public class Wiki extends LayoutContainer {

    private IntegrationPanel panel;

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setStyleAttribute("padding", "10px");
        panel = new IntegrationPanel();
        panel.setHeading(Main.constants.forum_title());
        panel.setUrl(Main.constants.wiki_url());
        panel.getOpenExternal().addSelectionListener(new SelectionListener<ToolBarEvent>() {

            @Override
            public void componentSelected(ToolBarEvent ce) {
                Window.open(Main.constants.wiki_url(), "", null);
            }
        });
        add(panel);
    }
}
