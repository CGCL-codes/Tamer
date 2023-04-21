package net.sourceforge.cruisecontrol.launch;

/**
 * Interface used to bridge to the actual Main class without any messy
 * reflection
 */
public interface CruiseControlMain {

    /**
     * Start CruiseControl
     *
     * @param args
     *            Command line arguments to be handled directly by CruiseControl
     *            (passed through the launcher without processing)
     */
    public boolean start(String[] args);
}
