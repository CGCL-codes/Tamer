package com.hack23.cia.web.views.navigationview.user;

import com.hack23.cia.web.viewfactory.api.common.ModelAndView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class DashboardNavigationView.
 */
@SuppressWarnings("serial")
public class DashboardNavigationView extends AbstractUserNavigationView {

    @Override
    public final Panel createComponentPanel() {
        return new Panel("Dashboard", createGridLayout());
    }

    public GridLayout createGridLayout() {
        GridLayout gl = new GridLayout(3, 3);
        gl.setSizeFull();
        gl.setSpacing(true);
        for (int i = 0; i < 9; i++) {
            Panel p = new Panel("Board " + i);
            gl.addComponent(p);
            p.setSizeFull();
        }
        return gl;
    }

    @Override
    public final String getWarningForNavigatingFrom() {
        return null;
    }

    @Override
    public final void navigateTo(final String requestedDataId) {
    }

    @Override
    public final void process(final ModelAndView modelAndView) {
    }
}
