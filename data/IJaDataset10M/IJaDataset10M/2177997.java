package com.hack23.cia.web.views.ui;

import com.hack23.cia.model.application.dto.common.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.core.impl.BaseEntity;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * The Class AbstractBaseEntityForm.
 */
public abstract class AbstractBaseEntityForm<FORM_OBJECT extends BaseEntity> extends Form {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The action bar. */
    private final HorizontalLayout actionBar = new HorizontalLayout();

    /**
	 * Instantiates a new abstract base entity form.
	 * 
	 * @param userSessionDTO
	 *            the user session dto
	 * @param formObject
	 *            the form object
	 */
    public AbstractBaseEntityForm(final UserSessionDTO userSessionDTO, FORM_OBJECT formObject) {
        this.setItemDataSource(new BeanItem(formObject));
        TextField versionField = new TextField(formObject.getVersion().toString());
        versionField.setReadOnly(true);
        addField("version", versionField);
        actionBar.setSpacing(true);
        Button okButton = new Button(userSessionDTO.getLanguageResource(Agency.LanguageContentKey.BUTTON_OK));
        Button cancelButton = new Button(userSessionDTO.getLanguageResource(Agency.LanguageContentKey.BUTTON_CANCEL));
        actionBar.addComponent(cancelButton);
        actionBar.addComponent(okButton);
        setFooter(actionBar);
    }

    /**
	 * Gets the action bar.
	 * 
	 * @return the action bar
	 */
    protected final HorizontalLayout getActionBar() {
        return actionBar;
    }
}
