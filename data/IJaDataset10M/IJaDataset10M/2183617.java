package jaguar;

/**
 * @author peter
 *
 */
public class JaguarOperatingSystem {

    private boolean msWindows = false;

    /**
	 * 
	 */
    public JaguarOperatingSystem() {
        msWindows = getName().toLowerCase().indexOf("windows") >= 0;
    }

    public String getArch() {
        return System.getProperty("os.arch", "Ghost");
    }

    public String getName() {
        return System.getProperty("os.name", "GhostOs");
    }

    public String getVersion() {
        return System.getProperty("os.version", "Alpha");
    }

    public String getAvailableProcessors() {
        return "" + Runtime.getRuntime().availableProcessors();
    }

    public boolean isMsWindows() {
        return msWindows;
    }
}
