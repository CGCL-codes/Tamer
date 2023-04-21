package com.ncs.crm.client.welcome;

import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;

public class NewsPicPane extends HTMLPane {

    public NewsPicPane() {
        setPadding(0);
        setHeight("50%");
        setContentsURL("data/flexOutput/crmFlex.html");
        setContentsType(ContentsType.PAGE);
        setOverflow(Overflow.HIDDEN);
    }
}
