package org.saxoprint.juddiv3.client.web;

import org.apache.commons.lang.StringUtils;
import org.saxoprint.juddiv3.client.core.JuddiService;
import org.saxoprint.juddiv3.client.core.JuddiServiceFactory;
import org.saxoprint.juddiv3.client.model.AuthentificationToken;
import org.saxoprint.juddiv3.client.model.BusinessEntity;

/**
 * BusinessEntityDialog Backing bean. Handles all functionality of BusinessEntityDialog.
 * @author smeissner
 * @date 17.08.2010
 */
public class BusinessEntityDialogBean {

    private JuddiService juddiService;

    private BusinessEntity current;

    /**
	 * Default constructor. Initialize instance.
	 */
    public BusinessEntityDialogBean() {
        juddiService = JuddiServiceFactory.getInstance();
    }

    /**
	 * Getter for current attribute.
	 * @return the current
	 */
    public BusinessEntity getCurrent() {
        return current;
    }

    /**
	 * Creates an new BusinessEntity object.
	 */
    public void newBusinessEntity() {
        current = new BusinessEntity();
    }

    /**
	 * Loads a BusinessEntity for display.
	 * @param businessKey Key of BusinessEntity to display.
	 */
    public void displayBusinessEntity(String businessKey) {
        current = juddiService.getBusinessEntity(businessKey);
    }

    /**
	 * Save current Business Entity
	 * @return null (no action)
	 */
    public String saveBusinessEntity() {
        if (StringUtils.isEmpty(current.getKey())) {
            AuthentificationToken authToken = juddiService.authenticate("root", "");
            juddiService.publishBusinessEntity(authToken, current);
        }
        return null;
    }
}
