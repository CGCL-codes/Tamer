package net.openchrom.support.settings;

public class OperatingSystemUtils implements IOperatingSystemUtils {

    @Override
    public String getLineDelimiter() {
        if (isWindows()) {
            return IOperatingSystemUtils.END_OF_LINE_WINDOWS;
        } else if (isMac()) {
            return IOperatingSystemUtils.END_OF_LINE_MAC;
        } else if (isLinux()) {
            return IOperatingSystemUtils.END_OF_LINE_LINUX;
        } else if (isUnix()) {
            return IOperatingSystemUtils.END_OF_LINE_UNIX;
        } else {
            return IOperatingSystemUtils.END_OF_LINE_DEFAULT;
        }
    }

    @Override
    public boolean isWindows() {
        return (getOperatingSystem().indexOf("win") >= 0);
    }

    @Override
    public boolean isLinux() {
        return (getOperatingSystem().indexOf("linux") >= 0);
    }

    @Override
    public boolean isMac() {
        return (getOperatingSystem().indexOf("mac") >= 0);
    }

    @Override
    public boolean isUnix() {
        return (getOperatingSystem().indexOf("unix") >= 0);
    }

    private String getOperatingSystem() {
        return System.getProperty("os.name").toLowerCase();
    }
}
