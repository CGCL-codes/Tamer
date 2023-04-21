package com.hack23.cia.web.views.navigationview.admin.commandcenter;

import com.hack23.cia.model.sweden.impl.Ballot;
import com.hack23.cia.web.common.BeanLocator;
import com.hack23.cia.web.viewfactory.api.common.ModelAndView;
import com.hack23.cia.web.views.container.admin.BallotContainer;
import com.hack23.cia.web.views.form.admin.BallotForm;
import com.hack23.cia.web.views.navigationview.common.BaseEntityList;
import com.hack23.cia.web.views.navigationview.common.ListView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class ParliamentDataAgentsNavigationView.
 */
@SuppressWarnings("serial")
public class ParliamentDataAgentsNavigationView extends AbstractAdminAgentNavigationView<Ballot, BallotContainer> {

    /** The list view. */
    private ListView listView;

    @Override
    public final Panel createComponentPanel() throws Exception {
        GridLayout gl = new GridLayout(1, 1);
        gl.setSizeFull();
        gl.setSpacing(true);
        BallotContainer ballotContainer = new BallotContainer();
        createContainer(ballotContainer, BeanLocator.getParliamentService().getBallots());
        listView = new ListView(new BaseEntityList(ballotContainer, getNavigator(), getUriPrefix()), new BallotForm(getUserSessionDTO(), null));
        gl.addComponent(listView);
        listView.setSizeFull();
        return new Panel("Ballots", gl);
    }

    @Override
    public final String getWarningForNavigatingFrom() {
        return null;
    }

    @Override
    public final void navigateTo(final String requestedDataId) {
        getLogger().info(requestedDataId);
        if (requestedDataId == null) {
            listView.setSecondComponent(new BallotForm(getUserSessionDTO(), null));
        } else {
            listView.setSecondComponent(new BallotForm(getUserSessionDTO(), BeanLocator.getParliamentService().loadBallot(Long.valueOf(requestedDataId))));
        }
    }

    @Override
    public final void process(final ModelAndView modelAndView) {
    }
}
