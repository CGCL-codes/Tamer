package simtools.ui;

import java.util.logging.Logger;

/**
 * This class is used to display messages into the command line
 *
 * @see StringsResourceBundle
 * @see BasicMessageWriter
 *
 * @author Claude Cazenave
 *
 * @version 1.0 1999
 */
public class MessageCommandLine extends BasicMessageWriter {

    /**
	 * A looger to dump error or warning messages in a soket, an output stream, a file...
	 */
    static Logger _logger = simtools.util.LogConfigurator.getLogger(MessageCommandLine.class.getName());

    /**
         * Creates it with strings resources
         * @param resources the resources
         */
    public MessageCommandLine(StringsResourceBundle resources) {
        super(resources);
    }

    /**
         * Prints a message without argument
         * @param messageKey the message key
         * @return the message
         */
    public String print0args(String messageKey) {
        String res = super.print0args(messageKey);
        _logger.fine(res);
        return res;
    }

    /**
         * Prints a message with one argument
         * @param messageKey the message key
         * @param arg1 the first argument
         * @return the message
         */
    public String print1args(String messageKey, Object arg1) {
        String res = super.print1args(messageKey, arg1);
        _logger.fine(res);
        return res;
    }

    /**
         * Prints a message with two arguments
         * @param messageKey the message key
         * @param arg1 the first argument
         * @param arg2 the second argument
         * @return the message
         */
    public String print2args(String messageKey, Object arg1, Object arg2) {
        String res = super.print2args(messageKey, arg1, arg2);
        System.out.println(res);
        return res;
    }

    /**
         * Prints a message with n arguments
         * @param messageKey the message key
         * @param args an array of argument
         * @return the message
         */
    public String printNargs(String messageKey, Object[] args) {
        String res = super.printNargs(messageKey, args);
        System.out.println(res);
        return res;
    }
}
