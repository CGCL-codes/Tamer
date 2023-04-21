package com.hack23.cia.web.views.ui;

import com.vaadin.Application;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class DashboardNavigationView.
 */
@SuppressWarnings("serial")
public class DashboardNavigationView extends CustomComponent implements Navigator.View {

    /** The gl. */
    GridLayout gl = new GridLayout(3, 3);

    public void init(Navigator navigator, Application application) {
        setSizeFull();
        gl.setSizeFull();
        setCompositionRoot(gl);
        gl.setSpacing(true);
        for (int i = 0; i < 9; i++) {
            Panel p = new Panel("Board " + i);
            gl.addComponent(p);
            p.setSizeFull();
        }
    }

    public String getWarningForNavigatingFrom() {
        return null;
    }

    public void navigateTo(String requestedDataId) {
    }
}
