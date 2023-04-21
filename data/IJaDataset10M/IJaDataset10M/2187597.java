package net.naijatek.myalumni.framework.struts;

import java.util.Date;
import net.naijatek.myalumni.util.date.DateConverter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.validator.ValidatorActionForm;

public class MyAlumniBaseForm extends ValidatorActionForm {

    private static Log logger = LogFactory.getLog(MyAlumniBaseForm.class);

    static {
        ConvertUtils.register(new DateConverter(), Date.class);
        logger.info("Date Converters registered...");
    }

    private String lastModification;

    private String lastModifiedBy;

    private String lastModifiedDate;

    private String buttonAction;

    public String getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(final String lastModification) {
        this.lastModification = lastModification;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
