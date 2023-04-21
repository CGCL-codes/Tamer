package org.wuhsin.canon.commandline;

import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuhsin.canon.commandline.annotations.CmdLine;

/**
 * A argument parser that takes the expected syntax and the list 
 * of arguments and the populates itself with a set of <code>Arguments</code>.
 *  
 * @author jmochel
 *
 */
public class Arguments extends LinkedList<Argument> {

    /**
     *  Serialization UID for versioning.
     */
    private static final long serialVersionUID = 1286549155392320106L;

    /**
     * Logger for reporting
     */
    private static Logger logger = LoggerFactory.getLogger(Arguments.class.getSimpleName());

    /**
     * The constructor.
     * 
     * @param syntax A description of what arguments are expectde and what they should look like. 
     * @param args The command line arguments as passed in to the application. 
     */
    public Arguments(final CmdLine syntax, final String[] args) {
        super();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith(syntax.prefix())) {
                final int startOfNomen = syntax.prefix().length();
                int endOfNomen = args[i].indexOf(syntax.leadin());
                if (endOfNomen == -1) {
                    endOfNomen = args[i].length() - 1;
                }
                final String nomen = args[i].substring(startOfNomen, endOfNomen);
                String detail = null;
                if (args[i].indexOf(syntax.leadin()) != -1) {
                    final int startOfDetail = args[i].indexOf(syntax.leadin()) + 1;
                    final int endOfDetail = args[i].length();
                    detail = args[i].substring(startOfDetail, endOfDetail);
                }
                super.add(new Argument(nomen, detail));
                logger.debug("Found configuration for the following option: " + nomen);
            } else {
                throw new IllegalArgumentException("An illegal argument was given for parsing:" + args[i]);
            }
        }
    }
}
