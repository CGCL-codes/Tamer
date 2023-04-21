package com.devunion.salon.web.action;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.struts.action.Action;
import org.apache.struts.util.MessageResources;
import org.springframework.web.struts.MappingDispatchActionSupport;

/**
 * @author Timoshenko Alexander
 */
public class CoreAction extends MappingDispatchActionSupport {

    protected MessageResources resources = MessageResources.getMessageResources("ApplicationResources");

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
