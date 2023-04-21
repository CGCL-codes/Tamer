package com.medsol.common.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.Conversion;

/**
 * 
 */
@Conversion()
public class IndexAction extends ActionSupport {

    public String execute() throws Exception {
        return SUCCESS;
    }
}
