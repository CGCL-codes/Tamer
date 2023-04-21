package com.webobjects.monitor.application;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.monitor._private.MHost;

public class PathWizardPage1 extends MonitorComponent {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6797982622070654127L;

    public MHost aCurrentHost;

    public String callbackKeypath;

    public String callbackExpand;

    public WOComponent callbackPage;

    public boolean showFiles = true;

    public PathWizardPage1(WOContext aWocontext) {
        super(aWocontext);
    }

    public void setCallbackKeypath(String aValue) {
        callbackKeypath = aValue;
    }

    public void setCallbackExpand(String aValue) {
        callbackExpand = aValue;
    }

    public void setCallbackPage(WOComponent aValue) {
        callbackPage = aValue;
    }

    public void setShowFiles(boolean aValue) {
        showFiles = aValue;
    }

    public NSArray hostList() {
        NSArray aHostArray = siteConfig().hostArray();
        return aHostArray;
    }

    public WOComponent hostClicked() {
        PathWizardPage2 aPage = PathWizardPage2.create(context());
        aPage.setHost(aCurrentHost);
        aPage.setCallbackKeypath(callbackKeypath);
        aPage.setCallbackExpand(callbackExpand);
        aPage.setCallbackPage(callbackPage);
        aPage.setShowFiles(showFiles);
        return aPage;
    }

    public boolean onlyOneHost() {
        NSArray aHostList = hostList();
        if (aHostList != null && (aHostList.count() == 1)) {
            return true;
        }
        return false;
    }

    public boolean multipleHosts() {
        NSArray aHostList = hostList();
        if (aHostList != null && (aHostList.count() > 1)) {
            return true;
        }
        return false;
    }

    public boolean noHosts() {
        NSArray aHostList = hostList();
        if (aHostList == null || (aHostList.count() == 0)) {
            return true;
        }
        return false;
    }

    public WOComponent onlyHostClicked() {
        PathWizardPage2 aPage = PathWizardPage2.create(context());
        aPage.setHost((MHost) hostList().objectAtIndex(0));
        aPage.setCallbackKeypath(callbackKeypath);
        aPage.setCallbackExpand(callbackExpand);
        aPage.setCallbackPage(callbackPage);
        aPage.setShowFiles(showFiles);
        return aPage;
    }

    public static PathWizardPage1 create(WOContext context) {
        return (PathWizardPage1) context.page().pageWithName(PathWizardPage1.class.getName());
    }
}
