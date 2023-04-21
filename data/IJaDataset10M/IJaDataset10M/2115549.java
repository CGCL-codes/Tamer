package com.c2b2.ipoint.presentation.forms;

import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.PortletType;
import com.c2b2.ipoint.model.SecureItem;
import com.c2b2.ipoint.presentation.PresentationException;

/**
  * $Id: EditPortletTypePermissions.java,v 1.1 2006/08/28 17:48:00 steve Exp $
  * 
  * Copyright 2006 C2B2 Consulting Limited. All rights reserved.
  * Use of this code is subject to license.
  * Please check your license agreement for usage restrictions
  * 
  * This class implements a form for editing the permissions of portlet
  * types. 
  * 
  * @author $Author: steve $
  * @version $Revision: 1.1 $
  * $Date: 2006/08/28 17:48:00 $
  * 
  */
public class EditPortletTypePermissions extends SecureItemPermissions {

    public static final String PORTLET_TYPE_ATTRIBUTE = "PortletTypeID";

    public EditPortletTypePermissions() {
    }

    protected String getEditHeading() {
        return "Groups Allowed to Create Portlets of this type";
    }

    protected SecureItem getSecureItem() {
        if (myPortletType == null) {
            String typeID = (String) getAttribute(PORTLET_TYPE_ATTRIBUTE);
            if (typeID != null) {
                try {
                    myPortletType = PortletType.find(typeID);
                } catch (PersistentModelException e) {
                    myPortletType = null;
                }
            }
        }
        return myPortletType;
    }

    protected String getSecureItemIdentifier() {
        String result = "";
        if (myPortletType != null) {
            result = Long.toString(myPortletType.getID());
        }
        return result;
    }

    protected String getViewHeading() {
        return "Groups Allowed to View the Portlet Type";
    }

    public boolean canRender() throws PresentationException {
        super.canRender();
        return myPR.getCurrentUser().isAdministrator();
    }

    private PortletType myPortletType;
}
