package nl.ou.netlogo.sql.extension;

import java.util.Map;
import nl.ou.netlogo.sql.wrapper.SqlConfiguration;
import nl.ou.netlogo.sql.wrapper.SqlEnvironment;
import nl.ou.netlogo.sql.wrapper.SqlExtension;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.Syntax;

/**
 * Configure implements the sql:configure command
 * 
 * @author NetLogo project-team
 * 
 */
public class Configure extends DefaultCommand {

    private final SqlEnvironment sqlenv = SqlExtension.getSqlEnvironment();

    /**
     * Checks syntax of the sql:configure command.
     * 
     * @return syntax object handle
     */
    public Syntax getSyntax() {
        return Syntax.commandSyntax(new int[] { Syntax.TYPE_STRING, Syntax.TYPE_LIST });
    }

    /**
     * Executes sql:configure command.
     * 
     * @param args
     *            <dl>
     *            <dt>args[0]</dt>
     *            <dd>is the name of the item to configure</dd>
     *            <dt>args[1]</dt>
     *            <dd>is the list of key/value pairs to configure</dd>
     *            </dl>
     * @param context
     * @throws ExtensionException
     * @throws org.nlogo.api.LogoException
     */
    public void perform(Argument args[], Context context) throws ExtensionException, org.nlogo.api.LogoException {
        String name = args[0].getString();
        Map<String, String> kvPairs = SqlConfiguration.parseSettingList(name, args[1].getList());
        sqlenv.getConfiguration().setConfiguration(name, kvPairs, context);
    }
}
