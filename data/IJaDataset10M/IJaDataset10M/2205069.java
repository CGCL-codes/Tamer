package org.sqsh.commands;

import static org.sqsh.options.ArgumentRequired.REQUIRED;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.sqsh.Command;
import org.sqsh.DatabaseCommand;
import org.sqsh.Renderer;
import org.sqsh.SQLRenderer;
import org.sqsh.Session;
import org.sqsh.SqshOptions;
import org.sqsh.options.Argv;
import org.sqsh.options.Option;

/**
 * Implements the \procs command.
 */
public class Procs extends Command implements DatabaseCommand {

    private static class Options extends SqshOptions {

        @Option(option = 'p', longOption = "proc-pattern", arg = REQUIRED, argName = "pattern", description = "Provides a pattern to match against procedure names")
        public String procPattern = "%";

        @Option(option = 's', longOption = "schema-pattern", arg = REQUIRED, argName = "pattern", description = "Provides a pattern to match against schema names")
        public String schemaPattern = "%";

        @Argv(program = "\\procs", min = 0, max = 0)
        public List<String> arguments = new ArrayList<String>();
    }

    @Override
    public SqshOptions getOptions() {
        return new Options();
    }

    @Override
    public int execute(Session session, SqshOptions opts) throws Exception {
        Options options = (Options) opts;
        Connection con = session.getConnection();
        ResultSet result = null;
        Renderer renderer = session.getRendererManager().getCommandRenderer(session);
        try {
            DatabaseMetaData meta = con.getMetaData();
            HashSet<Integer> cols = new HashSet<Integer>();
            cols.add(2);
            cols.add(3);
            result = meta.getProcedures(con.getCatalog(), options.schemaPattern, options.procPattern);
            SQLRenderer sqlRenderer = session.getSQLRenderer();
            sqlRenderer.displayResults(renderer, session, result, cols);
        } catch (SQLException e) {
            session.err.println("Failed to retrieve database metadata: " + e.getMessage());
            return 1;
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                }
            }
        }
        return 0;
    }
}
