package org.redemptionhost.web.panels;

import java.io.Serializable;
import org.apache.wicket.markup.html.panel.Panel;

public class HostNavigationPanel extends Panel implements Serializable {

    private static final long serialVersionUID = 1L;

    public HostNavigationPanel(String id, String disabledId) {
        super(id);
    }
}
