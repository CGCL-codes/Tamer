package com.uglygreencar.games.seaHunter.web;

import java.util.Date;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

@Validation()
@Conversion()
public class HelloWorldAction extends ActionSupport {

    private Date now;

    private String name;

    @TypeConversion(converter = "com.uglygreencar.games.seaHunter.web.DateConverter")
    @RequiredFieldValidator(message = "Please enter the date")
    public void setDateNow(Date now) {
        this.now = now;
    }

    public Date getDateNow() {
        return now;
    }

    @RequiredStringValidator(message = "Please enter a name", trim = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String execute() throws Exception {
        return SUCCESS;
    }
}
