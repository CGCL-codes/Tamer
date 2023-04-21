package net.sf.hdkp.wow;

import java.io.File;
import net.sf.hdkp.messages.*;

public class GenericGameFinder extends MessageSender implements GameFinder {

    private static final String INTERFACE_FOLDER = "Interface";

    private static final String ADDONS_FOLDER = "AddOns";

    public GenericGameFinder(MessageReceiver receiver) {
        super(receiver);
    }

    @Override
    public synchronized String getGamePath() {
        return null;
    }

    @Override
    public synchronized String getInstallPath() {
        return null;
    }

    @Override
    public String getGamePathForInstallPath(String installPath) {
        return null;
    }

    @Override
    public boolean isValidInstallPath(String installPath) {
        if (installPath == null) {
            return false;
        }
        return getAddonsPath(installPath) != null;
    }

    protected File getSubDirectory(File rootDir, String name) {
        if (rootDir == null || !rootDir.isDirectory()) {
            return null;
        }
        for (File file : rootDir.listFiles()) {
            if (file.isDirectory() && file.getName().equalsIgnoreCase(name)) {
                return file;
            }
        }
        return null;
    }

    protected File getSubDirectory(File rootDir, String... names) {
        File currentDir = rootDir;
        for (String name : names) {
            currentDir = getSubDirectory(currentDir, name);
            if (currentDir == null) {
                return null;
            }
        }
        return currentDir;
    }

    @Override
    public String getAddonsPath(String installPath) {
        if (installPath != null) {
            File addonsDir = getSubDirectory(new File(installPath), INTERFACE_FOLDER, ADDONS_FOLDER);
            if (addonsDir != null) {
                return addonsDir.getAbsolutePath();
            }
        }
        return null;
    }

    @Override
    public boolean isValidGamePath(String gamePath) {
        if (gamePath == null) {
            return false;
        }
        return new File(gamePath).exists();
    }
}
