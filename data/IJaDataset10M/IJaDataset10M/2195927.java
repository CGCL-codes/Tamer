package genj.app;

import genj.util.Registry;

/**
 * An abstract interface to a HelpBridge allowing the opening of a Help Window
 */
public interface HelpBridge {

    /**
   * Closes Help
   */
    public void close(Registry registry);

    /**
   * Opens Help
   */
    public void open(Registry registry);
}
