package fr.imag.adele.escoffier.script.command;

import java.io.PrintStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.shell.Command;
import org.apache.felix.shell.ShellService;

/**
 * <p>
 * Instances of this component are command aliases in the felix shell. When
 * executed, they delegate to another command.
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: christian $<br/> $Date: 2008-02-13 10:41:02 +0100 (Mi, 13 Feb 2008) $<br/> $Revision: 1286 $
 * </p>
 * 
 * @author Christian Schollum
 */
@Component(factory = true, immediate = true)
@Provides
public class Alias implements Command {

    @Requires
    private ShellService shellService;

    @Property
    private String alias;

    @Property
    private String command;

    public Alias() {
    }

    public String getName() {
        return alias;
    }

    public String getUsage() {
        return substitute(command, alias, shellService.getCommandUsage(command));
    }

    /**
     * @param command2
     * @param alias2
     * @param commandUsage
     * @return
     */
    private String substitute(String name1, String name2, String commandStr) {
        return commandStr.replaceFirst(name1, name2);
    }

    public String getShortDescription() {
        return "execute command '" + command + "'";
    }

    public void execute(final String cmdStr, final PrintStream out, final PrintStream err) {
        try {
            shellService.executeCommand(substitute(alias, command, cmdStr), out, err);
        } catch (Exception e) {
            e.printStackTrace(err);
            err.flush();
        }
    }
}
