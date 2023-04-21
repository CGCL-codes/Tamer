package powermock.examples.apachecli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * Class that verifies that the
 * http://code.google.com/p/powermock/issues/detail?id=38 is fixed.
 */
public class OptionUser {

    private Options options;

    /**
	 * {@inheritDoc}
	 */
    public void printOptionDescription() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("caption", options);
    }
}
