package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>newproject.jsp</tt> page.</p>
 */
public class EditUser_en extends ListResourceBundle {

    private static Object contents[][] = { { "head.title", "Edit {0}" }, { "banner.title", "Edit {0}" }, { "link.projects", "Projects" }, { "link.users", "Users" }, { "link.logout", "Log out" }, { "form.summary", "Edit user information" }, { "form.email", "E-mail:" }, { "form.state", "State:" }, { "form.roles", "Roles:" }, { "form.projects", "Projects:" }, { "form.submit", "Submit" }, { "selst.active", "Active" }, { "selst.inactive", "Inactive" }, { "selroles.all", "All roles" }, { "selpjs.all", "All projects" } };

    public EditUser_en() {
    }

    public Object[][] getContents() {
        return contents;
    }
}
