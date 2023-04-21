package com.hack23.cia.web.impl.ui.navigationview.admin.content;

import com.hack23.cia.model.impl.application.common.LanguageContent;
import com.hack23.cia.web.impl.ui.container.admin.content.LanguageContentContainer;
import com.hack23.cia.web.impl.ui.form.admin.content.LanguageContentForm;
import com.hack23.cia.web.impl.ui.navigationview.common.BaseEntityList;
import com.hack23.cia.web.impl.ui.navigationview.common.ListView;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ModelAndView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * The Class LanguagesContentsNavigationView.
 */
@SuppressWarnings("serial")
public class LanguagesContentsNavigationView extends AbstractContentNavigationView<LanguageContent, LanguageContentContainer> {

    /** The list view. */
    private ListView listView;

    @Override
    public final Panel createComponentPanel() throws Exception {
        GridLayout gl = new GridLayout(1, 1);
        gl.setSizeFull();
        gl.setSpacing(true);
        LanguageContentContainer languageContainer = new LanguageContentContainer();
        createContainer(languageContainer, getLanguageContentLoaderService().getAllImplementations());
        listView = new ListView(new BaseEntityList(languageContainer, getNavigator(), getUriPrefix(), getUserSessionDTO()), new LanguageContentForm(getUserSessionDTO(), null));
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
            listView.setSecondComponent(new LanguageContentForm(getUserSessionDTO(), getLanguageContentLoaderService().load(Long.valueOf(requestedDataId))));
        }
    }

    @Override
    public final void process(final ModelAndView modelAndView) {
    }
}
