package com.hack23.cia.web.impl.ui.navigationview.content;

import com.hack23.cia.model.api.sweden.content.BallotData;
import com.hack23.cia.web.impl.ui.container.content.BallotContainer;
import com.hack23.cia.web.impl.ui.form.content.BallotForm;
import com.hack23.cia.web.impl.ui.navigationview.common.ListView;
import com.hack23.cia.web.impl.ui.navigationview.common.PersistedModelObjectList;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ModelAndView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class BallotsNavigationView.
 */
@SuppressWarnings("serial")
public class BallotsNavigationView extends AbstractParliamentNavigationView<BallotData, BallotContainer> {

    /** The list view. */
    private ListView listView;

    @Override
    public final Panel createComponentPanel() throws Exception {
        final GridLayout gl = new GridLayout(1, 1);
        gl.setSizeFull();
        gl.setSpacing(true);
        final BallotContainer ballotContainer = new BallotContainer(getSwedenModelFactory());
        createContainer(ballotContainer, getBallotLoaderService().getAll());
        listView = new ListView(new PersistedModelObjectList(ballotContainer, getNavigator(), getUriPrefix(), getUserSessionDTO()), new BallotForm(getUserSessionDTO(), null));
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
            listView.setSecondComponent(new BallotForm(getUserSessionDTO(), getBallotLoaderService().loadById(Long.valueOf(requestedDataId))));
        }
    }

    @Override
    public final void process(final ModelAndView modelAndView) {
    }
}
