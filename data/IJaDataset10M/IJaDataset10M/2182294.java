package de.jcommandlineparser.helpprinter;

import java.io.OutputStream;
import java.util.Set;
import de.jcommandlineparser.options.AbstractOption;

public interface HelpPrinter {

    /**
	 * <p>
	 * Prints a formatted help text to STDOUT.
	 * </p>
	 */
    void printHelp(Set<AbstractOption<?>> options, String usageString);

    /**
	 * <p>
	 * Prints a formatted help text to <code>out<code>.
	 * </p>
	 * 
	 * @param out
	 *            OutputStream help text is printed to.
	 */
    void printHelp(Set<AbstractOption<?>> options, String usageString, OutputStream out);
}
