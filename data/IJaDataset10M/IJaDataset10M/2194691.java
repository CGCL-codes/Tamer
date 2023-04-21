package com.dayatang.ibank.webapp.actions;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

/**
 * 
 */
@Conversion()
public class IndexAction extends BaseAction {

    private Date now = new Date(System.currentTimeMillis());

    @TypeConversion(converter = "org.webapp.struts2.DateConverter")
    public Date getDateNow() {
        return now;
    }

    public String execute() throws Exception {
        now = new Date(System.currentTimeMillis());
        return SUCCESS;
    }
}
