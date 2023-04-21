package de.uni_leipzig.lots.server.services;

import de.uni_leipzig.lots.webfrontend.app.AbstractBootstrapManager;
import de.uni_leipzig.lots.webfrontend.app.FullBootstrapListener;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kiel
 * @version $Id: TestBootstrapManager.java,v 1.6 2007/07/14 20:08:33 mai99bxd Exp $
 */
public class TestBootstrapManager extends AbstractBootstrapManager {

    @NotNull
    public String getVersion() {
        return "1.1";
    }

    public int getDataVersion() {
        return 7;
    }

    public boolean isBootstrapped() {
        return true;
    }

    public boolean isDatabaseActive() {
        return true;
    }

    public boolean isSetupComplete() {
        return false;
    }

    public void bootstrapFullApplication() {
    }

    public void forceBootstrapFullApplication() {
    }

    public boolean isInstallKeyValid(@NotNull String key) {
        return false;
    }

    public void addFullBootstrapListener(@NotNull FullBootstrapListener listener) {
    }

    public void removeFullBootstrapListener(@NotNull FullBootstrapListener listener) {
    }
}
