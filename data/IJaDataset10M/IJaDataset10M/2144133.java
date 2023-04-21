package jp.ne.nifty.iga.midori.shell.cmd.internal;

import jp.ne.nifty.iga.midori.shell.eng.*;
import java.io.*;
import java.util.*;

/**
 * source command for parsing shell script.
 * <br>
 * $Revision: 1.3 $
 */
public class source implements MdShellCommand {

    String name;

    /**
	 * Constructor
	 */
    public source() {
        name = "source";
    }

    /**
	 * Setting command name
	 *
	 * @param name command name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Get command name
	 *
	 * @return command name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Get command description
	 *
	 * @return command description
	 */
    public String getDescription() {
        return "source: source command for parsing script file.\n\n" + "    usage: source [-h] script-file\n" + "      parse script-file in this context.\n" + "      With -h, commands are placed on the history.\n";
    }

    /**
	 * Command execution engine
	 *
	 * @param args command arguments
	 * @param commanThread command thread object
	 * @return result code
	 */
    public int execute(String args[], MdShellCommandThread commandThread) {
        MdShellEnv env = commandThread.getEnv();
        if (args != null) {
            if (args[0].equals("-h")) {
                if (args.length > 1) {
                    MdShellParser.script(args[1], env, true);
                } else {
                    commandThread.setException(new MdShellCancelException());
                }
            } else {
                MdShellParser.script(args[0], env, false);
            }
        } else {
            commandThread.setException(new MdShellCancelException());
        }
        return 0;
    }
}
