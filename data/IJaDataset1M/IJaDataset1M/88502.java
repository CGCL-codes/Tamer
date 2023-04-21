package org.kalypso.nofdpidss.flood.risk.wizard.pages;

import java.util.GregorianCalendar;

public interface IFloodRiskTemplateDetails {

    public String getTemplateName();

    public String getTemplateDescription();

    public GregorianCalendar getTemplateDate();
}
