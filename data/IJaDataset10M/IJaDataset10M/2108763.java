package com.hack23.cia.web.impl.ui.navigationview.content;

import com.hack23.cia.model.api.application.content.LanguageContentData;
import com.hack23.cia.web.impl.ui.container.content.LanguageContentContainer;
import com.hack23.cia.web.impl.ui.form.content.LanguageContentForm;
import com.hack23.cia.web.impl.ui.navigationview.common.ListView;
import com.hack23.cia.web.impl.ui.navigationview.common.PersistedModelObjectList;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ModelAndView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class LanguagesContentsNavigationView.
 */
@SuppressWarnings("serial")
public class LanguagesContentsNavigationView extends AbstractContentNavigationView<LanguageContentData, LanguageContentContainer> {

    /** The list view. */
    private ListView listView;

    @Override
    public final Panel createComponentPanel() throws Exception {
        final GridLayout gl = new GridLayout(1, 1);
        gl.setSizeFull();
        gl.setSpacing(true);
        final LanguageContentContainer languageContainer = new LanguageContentContainer(getApplicationModelFactory());
        createContainer(languageContainer, getLanguageContentLoaderService().getAll());
        listView = new ListView(new PersistedModelObjectList(languageContainer, getNavigator(), getUriPrefix(), getUserSessionDTO()), new LanguageContentForm(getUserSessionDTO(), null));
        gl.addComponent(listView);
        listView.setSizeFull();
        return new Panel("Language Content", gl);
    }

    @Override
    public final String getWarningForNavigatingFrom() {
        return null;
    }

    @Override
    public final void navigateTo(final String requestedDataId) {
        getLogger().info(requestedDataId);
        if (requestedDataId == null) {
            listView.setSecondComponent(new LanguageContentForm(getUserSessionDTO(), null));
        } else {
            listView.setSecondComponent(new LanguageContentForm(getUserSessionDTO(), getLanguageContentLoaderService().loadById(Long.valueOf(requestedDataId))));
        }
    }

    @Override
    public final void process(final ModelAndView modelAndView) {
    }
}
