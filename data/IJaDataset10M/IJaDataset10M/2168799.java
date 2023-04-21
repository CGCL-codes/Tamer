package com.vmladenov.objects.resultobjects;

import com.vmladenov.objects.mainobjects.Page;

/**
 * User: Ventsislav Mladenov - Invincible
 * Date: 2007-1-1
 * Time: 22:30:59
 */
public class ResultPage extends Page {

    public ResultPage() {
    }

    public ResultForm getForm(String formname) {
        return (ResultForm) super.getForm(formname);
    }
}
