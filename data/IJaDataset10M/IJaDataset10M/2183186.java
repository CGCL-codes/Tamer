package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>editanswer.jsp</tt> page.</p>
 */
public class EditAnswer_en extends ListResourceBundle {

    private static Object contents[][] = { { "head.title", "Edit:  {0}" }, { "banner.title", "Edit Answer" }, { "link.projects", "Projects" }, { "link.users", "Users" }, { "link.logout", "Log out" }, { "link.delete", "Delete" }, { "form.summary", "Edit answer" }, { "form.answer", "Answer:" }, { "form.submit", "Submit" }, { "attachment.creation", "Created on {0}" }, { "error.emptyanswer", "The answer you submitted was empty.  " + "Please enter your answer and resubmit it." } };

    public EditAnswer_en() {
    }

    public Object[][] getContents() {
        return contents;
    }
}
