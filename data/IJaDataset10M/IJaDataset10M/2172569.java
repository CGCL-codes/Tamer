package org.isurf.dataintegrator.cpfr;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.isurf.dataintegrator.templates.TemplateCPFR;

public class WelcomeCPFR extends TemplateCPFR {

    @SuppressWarnings("unchecked")
    public WelcomeCPFR(PageParameters params) {
        super(params);
        add(new Label("welcome", "Welcome to the CPFR."));
        add(new BookmarkablePageLink("pageLinkPC2", PurchaseCondition.class));
        add(new BookmarkablePageLink("pageLinkEC2", ExceptionCriteria.class));
        add(new BookmarkablePageLink("pageLinkTILP2", TILP.class));
        add(new BookmarkablePageLink("pageLinkPA2", ProductActivity.class));
        add(new BookmarkablePageLink("pageLinkForecast2", Forecast.class));
        add(new BookmarkablePageLink("pageLinkEN2", ExceptionNotification.class));
        add(new BookmarkablePageLink("pageLinkPP2", ProductionPlan.class));
    }

    @Override
    public String getPageTitle() {
        return "Welcome to the CPFR";
    }
}
