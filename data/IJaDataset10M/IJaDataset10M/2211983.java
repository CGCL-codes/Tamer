package org.t2framework.vili.model.maven;

import net.skirnir.xom.annotation.Child;

public class Snapshots {

    private String enabled;

    public Snapshots() {
    }

    public Snapshots(boolean enabled) {
        this.enabled = String.valueOf(enabled);
    }

    public String getEnabled() {
        return enabled;
    }

    @Child
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
