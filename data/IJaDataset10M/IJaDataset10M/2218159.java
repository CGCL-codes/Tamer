package talkclient.langageCommandes;

import lb.edu.isae.commandesTalk.CommandScanner;
import lb.edu.isae.commandesTalk.CommandScanner.Command;

/**
 *
 * @author pfares
 */
public class CommandWho extends CommandClient {

    public static CommandWho parse(CommandScanner cs) {
        System.out.println("Parse de who " + cs.currentCommand);
        if (cs.currentCommand != Command.WHO) return null;
        return new CommandWho();
    }
}
