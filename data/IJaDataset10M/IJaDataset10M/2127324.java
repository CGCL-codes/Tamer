package org.zeroexchange.web.page.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;
import org.zeroexchange.exception.BusinessLogicException;
import org.zeroexchange.i18n.LocalizedValue;
import org.zeroexchange.i18n.read.LocalizationReader;
import org.zeroexchange.model.i18n.BasicResourceTypes;
import org.zeroexchange.model.user.Role;
import org.zeroexchange.resource.write.ResourceWriter;
import org.zeroexchange.web.Messages;
import org.zeroexchange.web.annotations.Bookmarkable;
import org.zeroexchange.web.components.GeneralFeedbackPanel;
import org.zeroexchange.web.components.ValidatableField;
import org.zeroexchange.web.components.container.ComponentsList;
import org.zeroexchange.web.components.i18n.LocalizedInput;
import org.zeroexchange.web.components.link.SubmitLinkPanel;
import org.zeroexchange.web.navigation.target.PageTarget;

/**
 * @author black
 *
 */
@Bookmarkable
@Secured(Role.ACEGITOKEN_USER)
public class EditResource extends ResourceDetails {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(EditResource.class);

    private static final String CKEY_CONTRACT_TOOLBAR = "toolbar";

    private static final String MKEY_RESOURCE_SAVE = "resource.save";

    @SpringBean
    private LocalizationReader localizationReader;

    @SpringBean
    private ResourceWriter resourceWriter;

    private LocalizedInput localizedInput;

    private Form resourceForm;

    /**
     * Constructor.
     */
    public EditResource(PageParameters pageParameters) {
        super(pageParameters);
        initResource();
        add(new GeneralFeedbackPanel("messagesPanel"));
        add(resourceForm = getResourceForm());
        add(new ComponentsList(CKEY_CONTRACT_TOOLBAR) {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Component> getComponents(String componentId) {
                return getResourceToolbarItems(componentId);
            }
        });
    }

    /**
     * Returns contract's form. 
     */
    protected Form getResourceForm() {
        Form<Serializable> form = new Form<Serializable>("resourceForm", new Model<Serializable>()) {

            private static final long serialVersionUID = 1L;

            {
                List<LocalizedValue> resourceTitles = new ArrayList<LocalizedValue>(localizationReader.getStrings(getCurrentResource(), BasicResourceTypes.TITLE));
                localizedInput = new LocalizedInput("title", new ListModel<LocalizedValue>(resourceTitles));
                localizedInput.setRequired(true);
                add(new ValidatableField("titleMarker").addToBorderBody(localizedInput));
            }

            @Override
            protected void onSubmit() {
            }
        };
        return form;
    }

    /**
     * Returns components for the resource's toolbar.
     */
    protected List<Component> getResourceToolbarItems(String componentId) {
        final List<Component> toolbarActions = new ArrayList<Component>();
        toolbarActions.add(new SubmitLinkPanel(componentId, new ResourceModel(MKEY_RESOURCE_SAVE), resourceForm) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                try {
                    saveSubmittedResourceData();
                    PageTarget previousTarget = getPreviousPage();
                    setResponsePage(previousTarget.getPageClass(), previousTarget.getPageParameters());
                } catch (BusinessLogicException e) {
                    log.error("", e);
                    error(getString(Messages.INTERNAL_ERROR));
                }
            }
        });
        return toolbarActions;
    }

    /**
     * Updates the transient contract.
     */
    protected void saveSubmittedResourceData() {
        resourceWriter.save(getCurrentResource(), localizedInput.getModelObject());
    }
}
