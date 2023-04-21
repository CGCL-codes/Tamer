package com.webobjects.monitor.rest;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.monitor._private.MHost;
import com.webobjects.monitor._private.MSiteConfig;
import er.extensions.eof.ERXKeyFilter;
import er.extensions.foundation.ERXStringUtilities;

public class MSiteConfigController extends JavaMonitorController {

    public MSiteConfigController(WORequest request) {
        super(request);
    }

    public WOActionResults updateAction() throws Throwable {
        checkPassword();
        if (siteConfig().hostArray().count() == 0) {
            throw new IllegalStateException("You cannot update the SiteConfig before adding a host.");
        }
        MSiteConfig siteConfig = (MSiteConfig) object(ERXKeyFilter.filterWithAttributes());
        update(siteConfig, ERXKeyFilter.filterWithAttributes());
        pushValues(siteConfig);
        return response(siteConfig, ERXKeyFilter.filterWithAttributes());
    }

    private void pushValues(MSiteConfig newSiteConfig) {
        String newHashedPassword = newSiteConfig.password();
        String currentHashedPassword = siteConfig().password();
        if (!ERXStringUtilities.stringIsNullOrEmpty(newHashedPassword)) {
            siteConfig()._setOldPassword();
        }
        newSiteConfig.values().removeObjectForKey("password");
        NSMutableDictionary newValues = siteConfig().values();
        newValues.removeObjectForKey("password");
        newValues.addEntriesFromDictionary(newSiteConfig.values());
        siteConfig().updateValues(newValues);
        if (!ERXStringUtilities.stringIsNullOrEmpty(newHashedPassword)) {
            siteConfig().values().takeValueForKey(newHashedPassword, "password");
        } else if (!ERXStringUtilities.stringIsNullOrEmpty(currentHashedPassword)) {
            siteConfig().values().takeValueForKey(currentHashedPassword, "password");
        }
        handler().sendUpdateSiteToWotaskds();
        if (!ERXStringUtilities.stringIsNullOrEmpty(newHashedPassword)) {
            siteConfig()._resetOldPassword();
        }
    }
}
