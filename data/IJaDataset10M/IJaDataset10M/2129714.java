package org.spirit.form;

import java.util.Calendar;
import org.spirit.form.base.BotListBaseForm;

/**
 * This is class is used by botverse.
 * 
 * @author Berlin Brown
 * 
 */
public class BotListPostSectionsForm extends BotListBaseForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6339470817330574172L;

    private String generatedId;

    private Calendar createdOn;

    private String sectionName;

    /**
		 * @return the createdOn
		 */
    public Calendar getCreatedOn() {
        return createdOn;
    }

    /**
		 * @param createdOn the createdOn to set
		 */
    public void setCreatedOn(Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
		 * @return the generatedId
		 */
    public String getGeneratedId() {
        return generatedId;
    }

    /**
		 * @param generatedId the generatedId to set
		 */
    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    /**
		 * @return the sectionName
		 */
    public String getSectionName() {
        return sectionName;
    }

    /**
		 * @param sectionName the sectionName to set
		 */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
