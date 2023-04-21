package org.ldaptive.cli;

import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.ldaptive.Connection;
import org.ldaptive.ConnectionConfig;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.DeleteOperation;
import org.ldaptive.DeleteRequest;
import org.ldaptive.props.ConnectionConfigPropertySource;

/**
 * Command line interface for {@link DeleteOperation}.
 *
 * @author  Middleware Services
 * @version  $Revision: 2290 $ $Date: 2012-02-27 17:13:40 -0500 (Mon, 27 Feb 2012) $
 */
public class DeleteOperationCli extends AbstractCli {

    /** option for LDAP DN. */
    private static final String OPT_DN = "dn";

    /** name of operation provided by this class. */
    private static final String COMMAND_NAME = "ldapdelete";

    /**
   * CLI entry point method.
   *
   * @param  args  command line arguments.
   */
    public static void main(final String[] args) {
        new DeleteOperationCli().performAction(args);
    }

    /** {@inheritDoc} */
    @Override
    protected void initOptions() {
        options.addOption(new Option(OPT_DN, true, "entry DN"));
        final Map<String, String> desc = getArgDesc(ConnectionConfig.class);
        for (String s : ConnectionConfigPropertySource.getProperties()) {
            options.addOption(new Option(s, true, desc.get(s)));
        }
        super.initOptions();
    }

    /** {@inheritDoc} */
    @Override
    protected void dispatch(final CommandLine line) throws Exception {
        if (line.hasOption(OPT_HELP)) {
            printHelp();
        } else {
            delete(initConnectionFactory(line), line.getOptionValues(OPT_DN));
        }
    }

    /**
   * Executes the ldap delete operation.
   *
   * @param  cf  connection factory.
   * @param  entryDns  to delete
   *
   * @throws  Exception  on any LDAP search error
   */
    protected void delete(final ConnectionFactory cf, final String[] entryDns) throws Exception {
        final Connection conn = cf.getConnection();
        conn.open();
        for (String dn : entryDns) {
            final DeleteOperation op = new DeleteOperation(conn);
            op.execute(new DeleteRequest(dn));
            System.out.println(String.format("Deleted entry: %s", dn));
        }
        conn.close();
    }

    /** {@inheritDoc} */
    @Override
    protected String getCommandName() {
        return COMMAND_NAME;
    }
}
