package org.eaasyst.eaa.batch.forms;

import org.apache.struts.validator.ValidatorForm;

/**
 * <p>A Struts validator form for delete applications.</p>
 *
 * @version 2.9
 * @author Jeff Chilton
 */
public class BatchJobConfirmForm extends ValidatorForm {

    private static final long serialVersionUID = 1;

    private String jobName = null;

    private String jobItems = null;

    /**
	 * Returns the jobItems.
	 * @return String
	 */
    public String getJobItems() {
        return jobItems;
    }

    /**
	 * Returns the jobName.
	 * @return String
	 */
    public String getJobName() {
        return jobName;
    }

    /**
	 * Sets the jobItems.
	 * @param jobItems The jobItems to set
	 */
    public void setJobItems(String jobItems) {
        this.jobItems = jobItems;
    }

    /**
	 * Sets the jobName.
	 * @param jobName The jobName to set
	 */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
