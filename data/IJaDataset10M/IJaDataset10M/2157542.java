package com.sts.webmeet.content.client;

import com.sts.webmeet.common.PackageUtil;
import com.sts.webmeet.client.*;
import java.util.ResourceBundle;

public abstract class AbstractContent implements Content {

    public String getIcon() {
        String str = "/images/" + PackageUtil.getShortPackage(getClass().getName()) + ".gif";
        return str;
    }

    public void setResourceBundle(java.util.ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public ResourceBundle getResourceBundle() {
        return this.bundle;
    }

    public ControlList[] getUserSpecificControls() {
        return new ControlList[0];
    }

    public ControlList[] getMeetingControls() {
        return new ControlList[0];
    }

    public void onModerator(boolean bModerator) {
    }

    public void init() {
    }

    public final String getShortPackageName() {
        return strShortPackageName;
    }

    public void destroy() {
    }

    public final void setClientContext(ClientContext context) {
        this.context = context;
    }

    public final ClientContext getClientContext() {
        return context;
    }

    private ClientContext context;

    private ResourceBundle bundle;

    private final String strShortPackageName = PackageUtil.getShortPackage(getClass());
}
