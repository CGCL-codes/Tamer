package com.hack23.cia.web.impl.ui.viewfactory.impl.admin;

import com.hack23.cia.web.impl.ui.common.ApplicationUserState;
import com.hack23.cia.web.impl.ui.common.ApplicationUserStateHolder;
import com.hack23.cia.web.impl.ui.common.Navigator.View;
import com.hack23.cia.web.impl.ui.viewfactory.api.admin.MonitorModelAndView;

/**
 * The Class MonitorViewFactoryImpl.
 */
public class MonitorViewFactoryImpl extends AbstractAdminViewFactoryImpl<MonitorModelAndView> {

    /**
     * Instantiates a new monitor view factory impl.
     */
    public MonitorViewFactoryImpl() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class getSupportedModelAndView() {
        return MonitorModelAndView.class;
    }

    @Override
    public final void processSpecificView(final MonitorModelAndView modelAndView) {
        ApplicationUserState userState = ApplicationUserStateHolder.getUserState();
        if (userState != null) {
            View currentView = userState.getNavigator().getCurrentView();
            currentView.process(modelAndView);
        } else {
        }
    }
}
