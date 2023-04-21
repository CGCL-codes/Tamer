package org.sqsh;

import static org.sqsh.options.ArgumentRequired.NONE;
import org.sqsh.options.Option;

/**
 * This object represents the command line options that are accepted by
 * a jsqsh command. All jsqsh commands accept at least the following
 * options due to inheritence from this class:
 * 
 * <pre>
 *    -g  Send output to a popup graphical window
 * </pre>
 * 
 * Commands should avoid utilizing these command line switches.
 */
public class SqshOptions {

    @Option(option = 'h', longOption = "help", arg = NONE, description = "Display help for command line usage")
    public boolean doHelp = false;

    @Option(option = 'g', longOption = "gui", arg = NONE, description = "Send all command output to a graphical window")
    public boolean isGraphical = false;
}
