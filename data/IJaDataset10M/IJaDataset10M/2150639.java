package com.hoydaa.livemessage.core.wicket;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.protocol.http.WebApplication;
import com.hoydaa.livemessage.core.wicket.page.BundleSelectionPage;

public class LiveMessageApplication extends WebApplication {

    public static final ResourceReference EDIT_PNG = new ResourceReference(LiveMessageApplication.class, "edit.png");

    public LiveMessageApplication() {
    }

    @Override
    protected void init() {
        super.init();
    }

    public Class<BundleSelectionPage> getHomePage() {
        return BundleSelectionPage.class;
    }
}
