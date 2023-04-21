package com.loribel.tools.web.bo.generated;

import java.util.List;
import com.loribel.tools.web.bo.GBW_BOTestAbstract;
import com.loribel.tools.web.bo.GBW_HtmlContentBO;

public abstract class GBW_HtmlContentTestGen extends GBW_BOTestAbstract {

    public GBW_HtmlContentTestGen(String a_name) {
        super(a_name);
    }

    public final void setUp() {
        super.setUp();
    }

    public String getBOName() {
        return GBW_HtmlContentBO.BO_NAME;
    }

    public List getBOsForTest() {
        List retour = getBOsGeneric();
        retour.add(new GBW_HtmlContentBO());
        return retour;
    }
}
