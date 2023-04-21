package org.jcvi.vics.web.gwt.header.client;

import com.google.gwt.user.client.ui.RootPanel;
import org.jcvi.vics.web.gwt.common.client.BaseEntryPoint;
import org.jcvi.vics.web.gwt.common.client.Constants;
import org.jcvi.vics.web.gwt.common.client.SystemPageHeader;

/**
 * Thin wrapper to add just the header to a page
 */
public class Header extends BaseEntryPoint {

    public void onModuleLoad() {
        RootPanel.get(Constants.LOADING_PANEL_NAME).setVisible(false);
        RootPanel.get(Constants.HEADER_PANEL_NAME).add(SystemPageHeader.getHeader());
        RootPanel.get("page_wrapper").setVisible(true);
        RootPanel.get("footer").setVisible(true);
    }
}
