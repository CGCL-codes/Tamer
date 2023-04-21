package com.webobjects.monitor.application;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

public class InstConfirmDeletePage extends MonitorComponent {

    public InstConfirmDeletePage(WOContext aWocontext) {
        super(aWocontext);
    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1784360231414606374L;

    public WOComponent deleteClicked() {
        handler().startWriting();
        try {
            siteConfig().removeInstance_M(mySession().mInstance);
            if (siteConfig().hostArray().count() != 0) {
                handler().sendRemoveInstancesToWotaskds(new NSArray(mySession().mInstance), siteConfig().hostArray());
            }
        } finally {
            handler().endWriting();
        }
        return pageWithName("AppDetailPage");
    }

    public WOComponent cancelClicked() {
        return pageWithName("AppDetailPage");
    }
}
