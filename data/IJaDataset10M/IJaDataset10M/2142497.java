package org.appspy.viewer.client.paramchooser;

import org.appspy.viewer.client.ViewerModule;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public abstract class AbstractParamChooser implements OldParamChooser {

    protected ViewerModule mAdminModule = null;

    protected String mValue = null;

    public ViewerModule getAdminModule() {
        return mAdminModule;
    }

    public void setAdminModule(ViewerModule adminModule) {
        mAdminModule = adminModule;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
