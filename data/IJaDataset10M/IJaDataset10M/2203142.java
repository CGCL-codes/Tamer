package nl.ou.netlogo.sql.extension;

import nl.ou.netlogo.sql.wrapper.SqlConnection;
import nl.ou.netlogo.sql.wrapper.SqlEnvironment;
import nl.ou.netlogo.sql.wrapper.SqlExtension;
import nl.ou.netlogo.sql.wrapper.SqlStatement;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoList;
import org.nlogo.api.Syntax;
import java.sql.*;

/**
 * Class representing the exec-query command in a NetLogo model from the SQL
 * extension.
 * 
 * @author NetLogo project-team
 * 
 */
public class ExecQuery extends DefaultCommand {

    private final SqlEnvironment sqlenv = SqlExtension.getSqlEnvironment();

    /**
     * Description of the NetLogo syntax of the command.
     * 
     * @return syntax object handle
     */
    public Syntax getSyntax() {
        int[] right = { Syntax.TYPE_STRING, Syntax.TYPE_LIST };
        return Syntax.commandSyntax(right);
    }

    /**
     * Executes parameterized query command from model context.
     * 
     * @param args
     * @param context
     * @throws ExtensionException
     * @throws org.nlogo.api.LogoException
     */
    public void perform(Argument args[], Context context) throws ExtensionException, org.nlogo.api.LogoException {
        SqlConnection sqlc = sqlenv.getActiveSqlConnection(context, true);
        try {
            String query = args[0].getString();
            LogoList parameters = args[1].getList();
            SqlStatement statement = sqlc.createStatement(query, parameters);
            statement.executeQuery();
        } catch (SQLException e) {
            throw new ExtensionException(e);
        }
    }
}
