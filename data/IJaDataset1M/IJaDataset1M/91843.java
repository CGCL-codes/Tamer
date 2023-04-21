package net.sf.doolin.cli;

import javax.annotation.PostConstruct;
import net.sf.doolin.context.AbstractApplication;
import net.sf.doolin.util.Strings;

/**
 * The Class CLIApplication.
 */
public class CLIApplication extends AbstractApplication {

    private final CLIService cliService;

    /**
	 * Instantiates a new CLI application.
	 * 
	 * @param cliService
	 *            the CLI service
	 */
    public CLIApplication(CLIService cliService) {
        this.cliService = cliService;
    }

    /**
	 * Loads the CLI bundles
	 */
    @PostConstruct
    public void postConstruct() {
        Strings.add("net.sf.doolin.cli.CLIBundle");
    }

    /**
	 * Does nothing.
	 * 
	 * {@inheritedDoc}
	 * 
	 * @param arguments
	 *            the arguments
	 */
    @Override
    public void start(String[] arguments) {
        cliService.run(arguments);
    }
}
