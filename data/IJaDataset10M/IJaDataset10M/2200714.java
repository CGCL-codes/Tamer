package org.blueoxygen.papaje.jobs;

import java.util.ArrayList;
import org.blueoxygen.papaje.entity.*;

public class ListJobs extends JobsForm {

    private String fieldId = "job";

    public String execute() {
        setJobs(getManager().getList("FROM " + Jobs.class.getName() + " c WHERE c.logInformation.activeFlag=1", null, null));
        return SUCCESS;
    }

    /**
	 * @return the fieldId
	 */
    public String getFieldId() {
        return fieldId;
    }

    /**
	 * @param fieldId the fieldId to set
	 */
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }
}
