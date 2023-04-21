package nl.ou.netlogo.sql.extension;

import nl.ou.netlogo.sql.wrapper.SqlConnection;
import nl.ou.netlogo.sql.wrapper.SqlEnvironment;
import nl.ou.netlogo.sql.wrapper.SqlExtension;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.Syntax;

/**
 * Class associated with the enable auto commit command in a NetLogo model from
 * the SQL extension. The Autocommit command deals with the AutoCommit
 * functionality as used in relational databases.
 * 
 * @author NetLogo project-team
 * 
 */
public class AutoCommitEnabled extends DefaultReporter {

    private final SqlEnvironment sqlenv = SqlExtension.getSqlEnvironment();

    /**
     * Checks syntax of the sql:autocommit-enabled? command.
     * 
     * @return syntax object handle
     */
    public Syntax getSyntax() {
        return Syntax.reporterSyntax(Syntax.TYPE_BOOLEAN);
    }

    /**
     * Executes sql:autocommit-enabled? command from model context.
     * 
     * @param args
     * @param context
     * @throws ExtensionException
     * @throws org.nlogo.api.LogoException
     */
    public Object report(Argument args[], Context context) throws ExtensionException, org.nlogo.api.LogoException {
        SqlConnection sqlc = sqlenv.getActiveSqlConnection(context, true);
        return sqlc.autoCommitEnabled();
    }
}
