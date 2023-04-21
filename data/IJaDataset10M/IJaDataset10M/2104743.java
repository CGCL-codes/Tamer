package com.hack23.cia.web.impl.ui.navigationview.admin.configuration;

import java.util.List;
import com.hack23.cia.model.impl.application.common.Portal;
import com.hack23.cia.web.impl.ui.container.admin.configuration.PortalContainer;
import com.hack23.cia.web.impl.ui.form.admin.configuration.PortalForm;
import com.hack23.cia.web.impl.ui.navigationview.common.BaseEntityList;
import com.hack23.cia.web.impl.ui.navigationview.common.ListView;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ModelAndView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class PortalsNavigationView.
 */
@SuppressWarnings("serial")
public class PortalsNavigationView extends AbstractAdminConfigurationNavigationView<Portal, PortalContainer> {

    /** The list view. */
    private ListView listView;

    @Override
    public final Panel createComponentPanel() throws Exception {
        GridLayout gl = new GridLayout(1, 1);
        gl.setSizeFull();
        gl.setSpacing(true);
        List<Portal> portals = getPortalLoaderService().getAllImplementations();
        PortalContainer portalContainer = new PortalContainer();
        createContainer(portalContainer, portals);
        listView = new ListView(new BaseEntityList(portalContainer, getNavigator(), getUriPrefix(), getUserSessionDTO()), new PortalForm(getUserSessionDTO(), null));
        gl.addComponent(listView);
        return new Panel("Portals", gl);
    }

    @Override
    public final String getWarningForNavigatingFrom() {
        return null;
    }

    @Override
    public final void navigateTo(final String requestedDataId) {
        getLogger().info(requestedDataId);
        if (requestedDataId == null) {
            listView.setSecondComponent(new PortalForm(getUserSessionDTO(), null));
        } else {
            listView.setSecondComponent(new PortalForm(getUserSessionDTO(), getPortalLoaderService().load(Long.valueOf(requestedDataId))));
        }
    }

    @Override
    public final void process(final ModelAndView modelAndView) {
    }
}
