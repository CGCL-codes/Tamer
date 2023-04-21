package com.blueskyminds.struts2.urlplugin.apps;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Date Started: 21/02/2008
 * <p/>
 * History:
 */
public class ExampleAction extends ActionSupport {

    private String name;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
