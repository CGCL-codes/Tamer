package siouxsie.desktop.core;

import siouxsie.plugin.Plugin;

public class DummyPlugin extends Plugin {

    private DummyCoreApplication dummyCoreApplication;

    public DummyCoreApplication getDummyCoreApplication() {
        return dummyCoreApplication;
    }

    public void setDummyCoreApplication(DummyCoreApplication dummyCoreApplication) {
        this.dummyCoreApplication = dummyCoreApplication;
    }
}
