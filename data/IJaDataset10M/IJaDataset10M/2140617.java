package com.jgeppert.struts2.jquery.showcase;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage(value = "showcase")
public class RemoteLink extends ActionSupport {

    private static final long serialVersionUID = -2847324847159590391L;

    @Action(value = "/remote-link", results = { @Result(location = "remote-link.jsp", name = "success") })
    public String execute() throws Exception {
        return SUCCESS;
    }
}
