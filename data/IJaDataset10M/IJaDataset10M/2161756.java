package com.hack23.cia.web.impl.ui.navigationview.admin.parliament;

import com.hack23.cia.model.impl.sweden.ParliamentMember;
import com.hack23.cia.web.impl.ui.container.admin.parliament.ParliamentMemberContainer;
import com.hack23.cia.web.impl.ui.form.admin.parliament.ParliamentMemberForm;
import com.hack23.cia.web.impl.ui.navigationview.common.BaseEntityList;
import com.hack23.cia.web.impl.ui.navigationview.common.ListView;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ModelAndView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class ParliamentMembersNavigationView.
 */
@SuppressWarnings("serial")
public class ParliamentMembersNavigationView extends AbstractParliamentNavigationView<ParliamentMember, ParliamentMemberContainer> {

    /** The list view. */
    private ListView listView;

    @Override
    public final Panel createComponentPanel() throws Exception {
        GridLayout gl = new GridLayout(1, 1);
        gl.setSizeFull();
        gl.setSpacing(true);
        ParliamentMemberContainer parliamentMemberContainer = new ParliamentMemberContainer();
        createContainer(parliamentMemberContainer, getParliamentMemberLoaderService().getAllImplementations());
        listView = new ListView(new BaseEntityList(parliamentMemberContainer, getNavigator(), getUriPrefix(), getUserSessionDTO()), new ParliamentMemberForm(getUserSessionDTO(), null));
        gl.addComponent(listView);
        listView.setSizeFull();
        return new Panel("Parliament Members", gl);
    }

    @Override
    public final String getWarningForNavigatingFrom() {
        return null;
    }

    @Override
    public final void navigateTo(final String requestedDataId) {
        getLogger().info(requestedDataId);
        if (requestedDataId == null) {
            listView.setSecondComponent(new ParliamentMemberForm(getUserSessionDTO(), null));
        } else {
            listView.setSecondComponent(new ParliamentMemberForm(getUserSessionDTO(), getParliamentMemberLoaderService().load(Long.valueOf(requestedDataId))));
        }
    }

    @Override
    public final void process(final ModelAndView modelAndView) {
    }
}
