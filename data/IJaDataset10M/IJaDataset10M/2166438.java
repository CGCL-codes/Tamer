package org.openxava.qamanager.validators;

import org.openxava.util.Messages;
import org.openxava.validators.IValidator;
import java.util.Date;

/**
 * 
 * @author Janesh Kodikara
 */
public class ProjectRiskDetailValidator implements IValidator {

    private Date riskReported;

    private Date riskScheduledClose;

    private Date riskActualClose;

    public void validate(Messages errors) throws Exception {
        XavaDateValidator validator = new XavaDateValidator();
        validator.isStartDateAfter(errors, "riskReported", riskReported, "riskScheduledClose", riskScheduledClose);
        validator.isStartDateAfter(errors, "riskReported", riskReported, "riskActualClose", riskActualClose);
        validator.isFutureDate(errors, "riskReported", riskReported);
        validator.isFutureDate(errors, "riskActualClose", riskActualClose);
    }

    public Date getRiskReported() {
        return riskReported;
    }

    public void setRiskReported(Date riskReported) {
        this.riskReported = riskReported;
    }

    public Date getRiskScheduledClose() {
        return riskScheduledClose;
    }

    public void setRiskScheduledClose(Date riskScheduledClose) {
        this.riskScheduledClose = riskScheduledClose;
    }

    public Date getRiskActualClose() {
        return riskActualClose;
    }

    public void setRiskActualClose(Date riskActualClose) {
        this.riskActualClose = riskActualClose;
    }
}
