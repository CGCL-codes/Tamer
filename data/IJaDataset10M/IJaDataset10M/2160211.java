package org.apache.struts2.showcase.ajax.tree;

/**
 */
public class Toggle extends GetCategory {

    public String execute() throws Exception {
        super.execute();
        getCategory().toggle();
        return SUCCESS;
    }
}
