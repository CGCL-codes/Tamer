package gov.fnal.mcas.admin;

import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.Logger;

public class DataSourceAdminTool {

    public static enum Commands {

        ADD, REPLACE, DELETE, GET, GETALL
    }

    ;

    public static String usageString = "Usage: DataSourceAdmin [--add=<ds-filename> | --replace=<ds-filename> | --delete=<ds-name> | --get-all | --get=<ds-name> ]";

    static Logger logger = Logger.getLogger(DataSourceAdminTool.class);

    static class ArgumentParserState {

        Commands cmd = null;

        String dataSourceInputFilePath = null;

        String dataSourceName = null;
    }

    static void usageExit1() {
        System.err.println(usageString);
        System.exit(1);
    }

    static ArgumentParserState parseArgs(String[] args) {
        int i = 0;
        String arg;
        ArgumentParserState parserState = new ArgumentParserState();
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];
            if (arg.startsWith("--add=")) {
                if (parserState.cmd != null) usageExit1();
                parserState.cmd = Commands.ADD;
                try {
                    parserState.dataSourceInputFilePath = arg.substring("--add=".length());
                    if (parserState.dataSourceInputFilePath.length() == 0) usageExit1();
                } catch (Exception e) {
                    usageExit1();
                }
            } else if (arg.startsWith("--replace=")) {
                if (parserState.cmd != null) usageExit1();
                parserState.cmd = Commands.REPLACE;
                try {
                    parserState.dataSourceInputFilePath = arg.substring("--replace=".length());
                    if (parserState.dataSourceInputFilePath.length() == 0) usageExit1();
                } catch (Exception e) {
                    usageExit1();
                }
            } else if (arg.startsWith("--delete=")) {
                if (parserState.cmd != null) usageExit1();
                parserState.cmd = Commands.DELETE;
                try {
                    parserState.dataSourceName = arg.substring("--delete=".length());
                    if (parserState.dataSourceName.length() == 0) usageExit1();
                } catch (Exception e) {
                    usageExit1();
                }
            } else if (arg.startsWith("--get=")) {
                if (parserState.cmd != null) usageExit1();
                parserState.cmd = Commands.GET;
                try {
                    parserState.dataSourceName = arg.substring("--get=".length());
                    if (parserState.dataSourceName.length() == 0) usageExit1();
                } catch (Exception e) {
                    usageExit1();
                }
            } else if (arg.startsWith("--get-all")) {
                if (parserState.cmd != null) usageExit1();
                parserState.cmd = Commands.GETALL;
            } else usageExit1();
        }
        if (i < args.length || args.length == 0) {
            usageExit1();
        }
        logger.debug("Command parsing: Command=" + parserState.cmd.toString() + "; dataSourceInput=" + parserState.dataSourceInputFilePath + "; dataSourceName=" + parserState.dataSourceName);
        return parserState;
    }

    /**
	 * The command line tool for the Data Source Administrative tool
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        ArgumentParserState argParserState = DataSourceAdminTool.parseArgs(args);
        DataSourceAdmin dataSourceAdmin = new DataSourceAdmin();
        if (argParserState.cmd == Commands.ADD) {
            DataSourceState unvalidatedDataSourceState = dataSourceAdmin.parseInputDataSource(argParserState.dataSourceInputFilePath);
            DataSourceServicesEndpoints endpoints = dataSourceAdmin.addDataSource(unvalidatedDataSourceState);
            System.out.println(endpoints);
        } else if (argParserState.cmd == Commands.REPLACE) {
            DataSourceState unvalidatedDataSourceState = dataSourceAdmin.parseInputDataSource(argParserState.dataSourceInputFilePath);
            DataSourceServicesEndpoints endpoints = dataSourceAdmin.replaceDataSource(unvalidatedDataSourceState);
            System.out.println(endpoints);
        } else if (argParserState.cmd == Commands.GET) {
            DataSource dataSource = dataSourceAdmin.getDataSource(argParserState.dataSourceName);
            System.out.println(dataSource);
        } else if (argParserState.cmd == Commands.GETALL) {
            Vector<DataSource> dataSources = dataSourceAdmin.getAllDataSources();
            Iterator<DataSource> itr = dataSources.iterator();
            System.out.println("<DataSourcesInfo>");
            while (itr.hasNext()) {
                System.out.println(itr.next());
            }
            System.out.println("</DataSourcesInfo>");
        } else if (argParserState.cmd == Commands.DELETE) {
            dataSourceAdmin.deleteDataSource(argParserState.dataSourceName);
        } else throw new Exception("Invalid command: " + argParserState.cmd.toString());
    }
}
