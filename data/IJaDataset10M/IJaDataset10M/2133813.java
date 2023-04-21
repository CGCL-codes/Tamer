package com.c2b2.ipoint.presentation.forms.fieldtypes.crm;

import com.c2b2.ipoint.model.User;
import com.c2b2.ipoint.model.crm.UserCategory;
import com.c2b2.ipoint.presentation.forms.fieldtypes.ObjectFilter;
import java.util.Collection;

/**
  * $Id: UserFieldFilterByCategory.java,v 1.1 2006/04/27 15:49:49 steve Exp $
  *
  * Copyright 2006 C2B2 Consulting Limited. All rights reserved.
  * Use of this code is subject to license.
  * Please check your license agreement for usage restrictions
  *
  * This class can be attached to a UserField to filter the field via UserCategories.
  *
  * @author $Author: steve $
  * @version $Revision: 1.1 $
  * $Date: 2006/04/27 15:49:49 $
  *
  */
public class UserFieldFilterByCategory extends ObjectFilter {

    /**
   * Creates a new Filter
   * @param category The category to use to filter the list
   * @param allChildren If true all users in this category and child categories are allowed
   */
    public UserFieldFilterByCategory(UserCategory category, boolean allChildren) {
        if (allChildren) {
            myAllowedUsers = category.getAllChildUsers();
        } else {
            myAllowedUsers = category.getUsers();
        }
    }

    /**
   * Accepts an object
   * @param o The Object to test
   * @return true if the User is a member of the category
   */
    public boolean accept(Object o) {
        boolean result = false;
        User user = (User) o;
        if (myAllowedUsers.contains(user)) {
            result = true;
        }
        return result;
    }

    private Collection<User> myAllowedUsers;
}
