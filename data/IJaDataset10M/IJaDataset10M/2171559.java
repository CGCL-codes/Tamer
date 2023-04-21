package openfarm.model.settings;

import java.io.File;
import openfarmtools.interpreter.exceptions.MaterialOsException;
import openfarmtools.repository.filehandling.EOperatingSystemProperties;

public abstract class GeneralProperties implements LoadableUnit {

    protected File binDir;

    protected boolean settingsLoaded;

    protected String rootDirectory;

    protected EOperatingSystemProperties operatingSystem;

    protected String startCommand;

    public GeneralProperties() {
        String os;
        try {
            if ((os = System.getProperty("os.name")) != null) ;
            operatingSystem = EOperatingSystemProperties.getOSFactory(os);
        } catch (MaterialOsException e) {
            e.printStackTrace();
            operatingSystem = EOperatingSystemProperties.OTHER;
        }
    }

    public boolean isSettingsLoaded() {
        return settingsLoaded;
    }

    public void setSettingsLoaded(boolean settingsLoaded) {
        this.settingsLoaded = settingsLoaded;
    }

    public EOperatingSystemProperties getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(EOperatingSystemProperties operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public void setStartCommand(String cmStr) {
        this.startCommand = cmStr;
    }

    public File getBinDir() {
        return binDir;
    }

    public void setBinDir(File binDir) {
        this.binDir = binDir;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public String getRootDirectory() {
        return this.rootDirectory;
    }
}
