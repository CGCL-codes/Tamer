package instead.launcher.interp;

import instead.DownloadException;
import instead.UpdateInstallException;

/**
 * @author 7ectant
 */
public abstract class Distribution {

    protected String version = "0";

    public final String getVersion() {
        return version;
    }

    public abstract void install() throws DownloadException, UpdateInstallException;

    public abstract boolean isInstallationComplete();
}
