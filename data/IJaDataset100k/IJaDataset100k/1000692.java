package org.blue.star.agent.config.utils;

import java.io.File;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.blue.star.common.agent.exceptions.AgentConfigurationException;

/**
 * This class is used to provide Utility Methods to the Configuration class associated with
 * configuring the Blue Agent.
 * 
 * @author Rob.Blake@arjuna.com
 * @version 0.1
 *
 */
public class Utils {

    private static Logger logger = LogManager.getLogger("org.blue.star.registry.agent.config.utils.Utils");

    private static String cn = "org.blue.star.visualisation.registry.agent.config.utils.Utils";

    /**
	 * This method is used to check to see if the given filename actually
	 * exists as a file.
	 * @param fileName - The name of the file to check
	 * @return - true if the file exists.
	 */
    public static boolean checkIsFile(String fileName) {
        return new File(fileName).exists();
    }

    /**
	 * This method is used to set the registry port of the Agent.
	 * @param value - The String value of the registry port.
	 * @return - The Integer value of the Registry Port
	 * @throws AgentConfigurationException - Thrown if there is an issue converting between the two formats.
	 */
    public static int setRegistryPort(String value) throws AgentConfigurationException {
        try {
            return getIntegerValueOfString(value);
        } catch (AgentConfigurationException e) {
            logger.debug(cn + ".setRegistryPort() - NumberFormatException Parsing Registry Port");
            throw new AgentConfigurationException("Registry Port Must be a Valid Integer");
        }
    }

    /**
	 * This method is used to convert a String into its integer value.
	 * @param value - The String to convert into a string
	 * @return - The Integer value of the String.
	 * @throws AgentConfigurationException - Thrown if there is an issue retrieving the Integer value.
	 */
    public static int getIntegerValueOfString(String value) throws AgentConfigurationException {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            logger.debug(cn + ".getIntegerValueOfString() - NumberFormatException");
            throw new AgentConfigurationException(e);
        }
    }

    /**
	 * This method is used to set the timeout value of the Agent.
	 * @param value - The timeout value
	 * @return - The timeout value in integer format.
	 * @throws AgentConfigurationException - Thrown if there is an issue setting the timeout value.
	 */
    public static int setTimeOut(String value) throws AgentConfigurationException {
        try {
            return getIntegerValueOfString(value);
        } catch (AgentConfigurationException e) {
            logger.debug(cn + ".setTimeOut() - NumberFormatException Parsing TimeOut value");
            throw new AgentConfigurationException("Timeout Value must be a Valid Integer");
        }
    }

    /**
	 * This method is used to set whether or not the agent supports Command Arguments.
	 * @param value - The String value of the variable
	 * @return - The boolean value of the variable.
	 * @throws AgentConfigurationException - Thrown if there is any issue converting between the two.
	 */
    public static boolean setAllowCommandArguments(String value) throws AgentConfigurationException {
        try {
            return convertToBooleanValue(value);
        } catch (IllegalArgumentException e) {
            logger.debug(cn + ".setAllowCommandArguments() - Invalid Boolean Value passed");
            throw new AgentConfigurationException("allow_command_arguments Must Be Either 0 or 1");
        }
    }

    /**
	 * This method is used to convert a String value to it's boolean eqivalent.
	 * @param value - The value to convert to a boolean.
	 * @return - The boolean value of the String.
	 * @throws IllegalArgumentException - Thrown if the String value is not boolean.
	 */
    public static boolean convertToBooleanValue(String value) throws IllegalArgumentException {
        if (value.equals("1")) return true; else if (value.equals("0")) return false; else throw new IllegalArgumentException("Non-valid Boolean value");
    }

    /**
	 * This method is used to retrieve the name of a command from a user specified configuration
	 * parameter.
	 * 
	 * commands are typically defined in the format command[name]=command_line
	 * @param value - The String containing the commmand name
	 * @return - The command name itself.
	 */
    public static String getCommandName(String value) throws AgentConfigurationException {
        int bindex = value.indexOf("[");
        int eindex = value.indexOf("]");
        if (bindex == -1 || eindex == -1) throw new AgentConfigurationException("Invalid Command Definition in definition '" + value + "'");
        return value.substring(bindex + 1, eindex);
    }
}
