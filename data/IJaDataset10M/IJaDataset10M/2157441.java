package com.jme3.system;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.SoftTextDialogInput;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

/**
 *
 * @author Kirill Vainer, normenhansen
 */
public abstract class JmeSystemDelegate {

    protected final Logger logger = Logger.getLogger(JmeSystem.class.getName());

    protected boolean initialized = false;

    protected boolean lowPermissions = false;

    protected File storageFolder = null;

    protected SoftTextDialogInput softTextDialogInput = null;

    public synchronized File getStorageFolder() {
        if (lowPermissions) {
            throw new UnsupportedOperationException("File system access restricted");
        }
        if (storageFolder == null) {
            storageFolder = new File(System.getProperty("user.home"), ".jme3");
            if (!storageFolder.exists()) {
                storageFolder.mkdir();
            }
        }
        return storageFolder;
    }

    public String getFullName() {
        return JmeVersion.FULL_NAME;
    }

    public InputStream getResourceAsStream(String name) {
        return this.getClass().getResourceAsStream(name);
    }

    public URL getResource(String name) {
        return this.getClass().getResource(name);
    }

    public boolean trackDirectMemory() {
        return false;
    }

    public void setLowPermissions(boolean lowPerm) {
        lowPermissions = lowPerm;
    }

    public boolean isLowPermissions() {
        return lowPermissions;
    }

    public void setSoftTextDialogInput(SoftTextDialogInput input) {
        softTextDialogInput = input;
    }

    public SoftTextDialogInput getSoftTextDialogInput() {
        return softTextDialogInput;
    }

    public abstract AssetManager newAssetManager(URL configFile);

    public abstract AssetManager newAssetManager();

    public abstract boolean showSettingsDialog(AppSettings sourceSettings, boolean loadFromRegistry);

    private boolean is64Bit(String arch) {
        if (arch.equals("x86")) {
            return false;
        } else if (arch.equals("amd64")) {
            return true;
        } else if (arch.equals("x86_64")) {
            return true;
        } else if (arch.equals("ppc") || arch.equals("PowerPC")) {
            return false;
        } else if (arch.equals("ppc64")) {
            return true;
        } else if (arch.equals("i386") || arch.equals("i686")) {
            return false;
        } else if (arch.equals("universal")) {
            return false;
        } else {
            throw new UnsupportedOperationException("Unsupported architecture: " + arch);
        }
    }

    public Platform getPlatform() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        boolean is64 = is64Bit(arch);
        if (os.contains("windows")) {
            return is64 ? Platform.Windows64 : Platform.Windows32;
        } else if (os.contains("linux") || os.contains("freebsd") || os.contains("sunos")) {
            return is64 ? Platform.Linux64 : Platform.Linux32;
        } else if (os.contains("mac os x") || os.contains("darwin")) {
            if (arch.startsWith("ppc")) {
                return is64 ? Platform.MacOSX_PPC64 : Platform.MacOSX_PPC32;
            } else {
                return is64 ? Platform.MacOSX64 : Platform.MacOSX32;
            }
        } else {
            throw new UnsupportedOperationException("The specified platform: " + os + " is not supported.");
        }
    }

    public abstract JmeContext newContext(AppSettings settings, JmeContext.Type contextType);

    public abstract AudioRenderer newAudioRenderer(AppSettings settings);

    public abstract void initialize(AppSettings settings);
}
