package com.fitso.wicket.page.journal;

import java.io.Serializable;
import org.apache.wicket.PageParameters;
import org.joda.time.DateTime;
import com.fitso.wicket.FitsoSession;
import com.fitso.wicket.config.SecureComponent;
import com.fitso.wicket.page.FitsoNoCacheWebPage;

@SecureComponent
public class FoodJournalPage extends FitsoNoCacheWebPage implements Serializable {

    private static final long serialVersionUID = 1L;

    public FoodJournalPage() {
        this(new PageParameters("date=" + new DateTime().toLocalDate().toString()));
    }

    public FoodJournalPage(PageParameters parameters) {
        super();
        DateTime dt = new DateTime(parameters.getString("date"));
        add(new FoodJournalDataView("FoodJournalEntry", new FoodJournalDataProvider(FitsoSession.get().getUser(), dt)));
    }

    @Override
    protected String getPageTitle() {
        return "Food FoodJournalPage";
    }
}
