package com.tinywebgears.tuatara.webapp.gui.panel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.appengine.api.datastore.Key;
import com.tinywebgears.tuatara.core.dao.DataPersistenceException;
import com.tinywebgears.tuatara.core.model.ContactIF;
import com.tinywebgears.tuatara.core.model.Person;
import com.tinywebgears.tuatara.framework.action.AbstractSingleContextAction;
import com.tinywebgears.tuatara.framework.action.ContextActionIF;
import com.tinywebgears.tuatara.framework.action.ContextActionProperties;
import com.tinywebgears.tuatara.framework.common.ApplicationException;
import com.tinywebgears.tuatara.framework.common.ObjectHelper;
import com.tinywebgears.tuatara.framework.gui.component.ComponentId;
import com.tinywebgears.tuatara.framework.gui.component.ComponentUpdateException;
import com.tinywebgears.tuatara.framework.gui.input.WebInputComponentIF;
import com.tinywebgears.tuatara.framework.gui.message.UserMessageException;
import com.tinywebgears.tuatara.framework.gui.resource.WebResourceContext;
import com.tinywebgears.tuatara.framework.model.DataChangeListenerIF;
import com.tinywebgears.tuatara.framework.resource.PropertyId;
import com.tinywebgears.tuatara.webapp.gui.main.WebApplication;
import com.tinywebgears.tuatara.webapp.gui.model.ContactProperties;
import com.tinywebgears.tuatara.webapp.gui.model.ModifyPersonProperties;
import com.tinywebgears.tuatara.webapp.gui.model.PersonProperties;

public class ModifyPersonPanel extends ModifyAbstractContactPanel {

    private static final PropertyId PROP_PREFIX = PropertyId.create(ModifyPersonPanel.class);

    private final Logger logger = LoggerFactory.getLogger(ModifyPersonPanel.class);

    private final ModifyPersonInputForm modifyPersonForm;

    private final ModifyPersonProperties modifyPersonProperties;

    private final ModifyPersonProperties initialModifyPersonProperties;

    private final ModifyPersonAction modifyAndListAction;

    private final ModifyPersonAction modifyAndAddAction;

    private final ContextActionIF<ModifyPersonProperties> restoreAction;

    public ModifyPersonPanel(ComponentId id, WebResourceContext webResourceContext) {
        super(id, webResourceContext);
        modifyPersonForm = new ModifyPersonInputForm(id.append("person-form"), webResourceContext);
        modifyPersonForm.getValueModel().addListener(new DataChangeListenerIF<PersonProperties>() {

            @Override
            public void dataChanged(PersonProperties value) {
                setActionsContext();
            }
        });
        modifyPersonProperties = new ModifyPersonProperties(null, null, null);
        initialModifyPersonProperties = new ModifyPersonProperties(null, null, null);
        modifyAndListAction = new ModifyPersonAction(new ContextActionProperties(getString(MODIFY_AND_LIST_LABEL_ID), getString(MODIFY_AND_LIST_TOOLTIP_ID)), PostModificationView.BACK_TO_LIST);
        modifyAndAddAction = new ModifyPersonAction(new ContextActionProperties(getString(MODIFY_AND_ADD_LABEL_ID), getString(MODIFY_AND_ADD_TOOLTIP_ID)), PostModificationView.ADD);
        restoreAction = new AbstractSingleContextAction<ModifyPersonProperties>(ModifyPersonProperties.class, new ContextActionProperties(getString(RESTORE_LABEL_ID), getString(RESTORE_TOOLTIP_ID))) {

            @Override
            protected void performAction(ModifyPersonProperties context) throws ApplicationException {
                restoreInitialValue();
                setFormValues();
                setActionsContext();
            }
        };
    }

    @Override
    protected void doFocus() throws ComponentUpdateException, UserMessageException {
        modifyPersonForm.focus();
    }

    @Override
    protected ContactIF loadContact(Key key) throws DataPersistenceException, UserMessageException {
        Person person = WebApplication.getInstance().getContactManager().getPersonByKey(key);
        logger.debug("Person: " + person);
        modifyPersonProperties.setPersonProperties(new PersonProperties(person.getGivenNames(), person.getSurname()));
        modifyPersonProperties.setContactProperties(new ContactProperties(person.getGroup(), person.getActivity(), person.getComment()));
        modifyPersonProperties.setKey(key);
        saveInitialValue();
        setFormValues();
        setActionsContext();
        return person;
    }

    private void saveInitialValue() {
        initialModifyPersonProperties.setPersonProperties(modifyPersonProperties.getPersonProperties());
        initialModifyPersonProperties.setContactProperties(modifyPersonProperties.getContactProperties());
    }

    private void restoreInitialValue() {
        modifyPersonProperties.setPersonProperties(initialModifyPersonProperties.getPersonProperties());
        modifyPersonProperties.setContactProperties(initialModifyPersonProperties.getContactProperties());
    }

    private void setFormValues() {
        ContactProperties cp = modifyPersonProperties.getContactProperties();
        PersonProperties pp = modifyPersonProperties.getPersonProperties();
        modifyContactForm.setValue(cp);
        modifyPersonForm.setValue(pp);
    }

    @Override
    protected void setActionsContext() {
        modifyPersonProperties.setContactProperties(modifyContactForm.getValue());
        modifyPersonProperties.setPersonProperties(modifyPersonForm.getValue());
        if (ObjectHelper.equals(initialModifyPersonProperties, modifyPersonProperties)) {
            modifyAndListAction.setContext(null);
            modifyAndAddAction.setContext(modifyPersonProperties);
            restoreAction.setContext(null);
        } else {
            modifyAndListAction.setContext(modifyPersonProperties);
            modifyAndAddAction.setContext(modifyPersonProperties);
            restoreAction.setContext(modifyPersonProperties);
        }
    }

    @Override
    protected WebInputComponentIF getConcreteContactForm() {
        return modifyPersonForm;
    }

    @Override
    protected ContextActionIF getRestoreAction() {
        return restoreAction;
    }

    @Override
    protected ContextActionIF getModifyAndAddAction() {
        return modifyAndAddAction;
    }

    @Override
    protected ContextActionIF getModifyAndListAction() {
        return modifyAndListAction;
    }

    @Override
    protected String getCreateViewName() {
        return WebApplication.VIEW_CREATE_PERSON;
    }

    @Override
    protected String getListViewName() {
        return WebApplication.VIEW_PERSONS_SUMMARY;
    }

    private class ModifyPersonAction extends ModifyAbstractContactAction<ModifyPersonProperties> {

        public ModifyPersonAction(ContextActionProperties properties, PostModificationView nextView) {
            super(ModifyPersonProperties.class, properties, nextView);
        }

        @Override
        protected boolean canEnable(ModifyPersonProperties context) {
            return context != null && context.getPersonProperties() != null && context.getContactProperties() != null;
        }

        @Override
        protected void modifyContact(ModifyPersonProperties context) throws DataPersistenceException, UserMessageException {
            WebApplication.getInstance().getContactManager().modifyPerson(context.getKey(), context.getPersonProperties(), context.getContactProperties());
        }
    }
}
