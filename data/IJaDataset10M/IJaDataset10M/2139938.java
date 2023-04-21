package com.google.code.sapien.action.admin;

import com.google.code.sapien.security.RequiresAdministrator;
import com.google.code.sapien.security.RolePermissions;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Administrator index page action.
 * @author Adam
 * @version $Id$
 * 
 * Created on Jun 3, 2009 at 8:27:16 PM 
 */
public class IndexAction extends ActionSupport {

    /**
	 * Serial Version UID.
	 */
    private static final long serialVersionUID = 1286166174373540244L;

    /**
	 * {@inheritDoc}
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
    @Override
    @RequiresAdministrator
    public String execute() throws Exception {
        return super.execute();
    }
}
