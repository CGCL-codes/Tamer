package de.tfh.pdvl.hp.serialServer;

/**
 * @author s717689
 *
 */
public class ValueCommand extends SCPICommand {

    private double parameter;

    public ValueCommand() {
    }

    public ValueCommand(String aCmdStr) {
        commandString = aCmdStr;
    }

    /**
     * @return Returns the parameter.
     */
    public double getParameter() {
        return parameter;
    }

    /**
     * @param parameter The parameter to set.
     */
    public void setParameter(double parameter) {
        this.parameter = parameter;
    }

    public String toString() {
        if (commandType == SCPICommandType.QUERY) {
            return commandString + "?\n";
        }
        if (commandType == SCPICommandType.QUERY_MIN) {
            return commandString + "? Min\n";
        }
        if (commandType == SCPICommandType.QUERY_MAX) {
            return commandString + "? Max\n";
        }
        if (commandType == SCPICommandType.SET) {
            return commandString + " " + parameter + "\n";
        }
        return null;
    }
}
