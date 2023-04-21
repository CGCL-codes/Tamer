package com.techstar.dmis.web.form;

import java.util.ArrayList;
import java.util.List;
import org.apache.struts.upload.FormFile;
import com.techstar.framework.web.form.BaseForm;
import com.techstar.dmis.dto.BhActionrecordDto;

/**
 * @author 
 * @date
 */
public class BhActionrecordForm extends BaseForm {

    private BhActionrecordDto bhActionrecordDto = new BhActionrecordDto();

    private FormFile actiondescription;

    public BhActionrecordForm() {
    }

    public BhActionrecordDto getBhActionrecordDto() {
        return bhActionrecordDto;
    }

    public void setBhActionrecordDto(BhActionrecordDto bhActionrecordDto) {
        this.bhActionrecordDto = bhActionrecordDto;
    }

    public FormFile getActiondescription() {
        return actiondescription;
    }

    public void setActiondescription(FormFile actiondescription) {
        this.actiondescription = actiondescription;
    }
}
