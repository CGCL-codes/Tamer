package com.hack23.cia.web.impl.ui.navigationview.content;

import com.hack23.cia.model.api.sweden.configuration.ParliamentData;
import com.hack23.cia.web.impl.ui.container.content.ParliamentContainer;
import com.hack23.cia.web.impl.ui.form.content.ParliamentForm;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ModelAndView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class ParliamentNavigationView.
 */
@SuppressWarnings("serial")
public class ParliamentNavigationView extends AbstractParliamentNavigationView<ParliamentData, ParliamentContainer> {

    /** The gl. */
    private final GridLayout gl = new GridLayout(1, 1);

    @Override
    public final Panel createComponentPanel() {
        gl.setSizeFull();
        gl.setSpacing(true);
        final ParliamentForm component = new ParliamentForm(getUserSessionDTO(), getParliamentLoaderService().loadById(1L));
        gl.addComponent(component);
        component.setSizeFull();
        return new Panel("Parliament", gl);
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
