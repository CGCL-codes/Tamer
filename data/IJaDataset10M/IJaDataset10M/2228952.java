package net.sourceforge.ant4hg.parameters;

import net.sourceforge.ant4hg.Logger;
import net.sourceforge.ant4hg.HgTask.ArgumentFriender;

/**
 * Defines addremove parameter.
 * 
 * @author Benjamin de Dardel <ant4hg[at]free.fr>
 * @since V0.4
 */
public class AddRemoveParameter extends Parameter {

    /**
     * Defines commands that use this parameter.
     */
    public static final String[] AUTHORIZED_COMMANDS = new String[] { "commit" };

    /**
     * Defines required value for this parameter.
     */
    public static final String[] REQUIRED_VALUES = new String[] { "true", "false", "yes", "no", "on", "off" };

    /**
     * Constructor
     * 
     */
    public AddRemoveParameter() {
        super("addremove", AUTHORIZED_COMMANDS, REQUIRED_VALUES);
    }

    /**
     * Constructor
     * 
     * @param value
     *            , the parameter value.
     */
    public AddRemoveParameter(String value) {
        this();
        setValue(value);
    }

    @Override
    public void initArgument(ArgumentFriender friend) {
        if (!isSet()) {
            Logger.error("PARAMETER addremove NOT NULL, BUT NOT SET");
            return;
        }
        if (isTrue()) {
            friend.setArgument("--addremove");
        }
    }
}
