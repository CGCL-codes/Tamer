package com.infotel.ifsic;

import org.apache.log4j.Logger;
import com.opensymphony.xwork2.ActionSupport;

public class Test extends ActionSupport {

    Logger logger = Logger.getLogger(Test.class);

    public String handleAjax() {
        logger.debug("handleAjax");
        return SUCCESS;
    }

    public String prepare() {
        logger.debug("prepare");
        return SUCCESS;
    }
}
