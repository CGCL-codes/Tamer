package com.gwtaf.ext.app.client.controller.editor;

import com.gwtaf.core.client.util.MessageDialog;
import com.gwtaf.ext.app.client.controller.common.IBreadCrumb;

public class SiteBreadCrumb implements IBreadCrumb {

    private IStackedEditorSite site;

    public SiteBreadCrumb(IStackedEditorSite site) {
        this.site = site;
    }

    public void activate() {
        if (!site.show()) MessageDialog.alert("Switch to site not possible", "Can't switch site, the current site has been changed.", MessageDialog.OK);
    }

    public boolean canSelectSuccessor() {
        return false;
    }

    public IBreadCrumb[] getSuccessorList() {
        return null;
    }

    public String getTitle() {
        return site.getTitle();
    }

    public String getIcon() {
        return site.getIcon();
    }
}
