package org.snova.gae.client.admin.handler;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.snova.gae.client.admin.GAEAdmin;
import org.snova.gae.client.connection.ProxyConnection;
import org.snova.gae.common.auth.Group;
import org.snova.gae.common.auth.Operation;
import org.snova.gae.common.event.GroupOperationEvent;

/**
 *
 */
public class AddGroup implements CommandHandler {

    public static final String COMMAND = "groupadd";

    private Options options = new Options();

    private ProxyConnection connection;

    public AddGroup(ProxyConnection connection) {
        this.connection = connection;
        options.addOption("h", "help", false, "print this message.");
    }

    @Override
    public void execute(String[] args) {
        CommandLineParser parser = new PosixParser();
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("h")) {
                printHelp();
            } else {
                String[] groupnameargs = line.getArgs();
                if (groupnameargs != null && groupnameargs.length != 1) {
                    GAEAdmin.outputln("Argument groupname required!");
                }
                GroupOperationEvent event = new GroupOperationEvent();
                event.opr = Operation.ADD;
                event.grp = new Group();
                event.grp.setName(groupnameargs[0]);
                AdminResponseEventHandler.syncSendEvent(connection, event);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            System.out.println("Error:" + exp.getMessage());
        }
    }

    @Override
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(COMMAND + " groupname", options);
    }
}
