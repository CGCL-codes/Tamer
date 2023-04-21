package com.jgeppert.struts2.jquery.mobile.showcase;

import org.apache.struts2.convention.annotation.ParentPackage;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage(value = "showcase")
public class Index extends ActionSupport {

    private static final long serialVersionUID = 2728708977513156502L;

    public String execute() throws Exception {
        return SUCCESS;
    }
}
