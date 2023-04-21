package com.uglygreencar.games.crazy8.web;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

/**
 * 
 */
@Conversion()
public class IndexAction extends ActionSupport {

    private static final long serialVersionUID = 6742231495218430695L;

    private Date now = new Date(System.currentTimeMillis());

    @TypeConversion(converter = "com.uglygreencar.games.crazy8.web.DateConverter")
    public Date getDateNow() {
        return now;
    }

    public String execute() throws Exception {
        now = new Date(System.currentTimeMillis());
        return SUCCESS;
    }
}
