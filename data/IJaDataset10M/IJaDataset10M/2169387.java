package ubermunchkin.client;

import java.io.File;

public class CommandLine {

    private String installPath;

    private boolean installPathValid;

    private int argIndex;

    public CommandLine(String[] args) {
        argIndex = 0;
        checkForInstallPath(args);
    }

    private void checkForInstallPath(String[] args) {
        File dir;
        if (args.length > argIndex) {
            dir = new File(args[argIndex]);
            if (dir.isDirectory()) {
                installPath = args[argIndex];
                if (installPath.endsWith(File.separator)) installPath += "ubermunchkin" + File.separator; else installPath += File.separator + "ubermunchkin" + File.separator;
                dir = new File(installPath);
                if (dir.isDirectory()) installPathValid = true;
            }
        }
    }

    public boolean isInstallPathValid() {
        return installPathValid;
    }

    public String getInstallPath() {
        return installPath;
    }
}
