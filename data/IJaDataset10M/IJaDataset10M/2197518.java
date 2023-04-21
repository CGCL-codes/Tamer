package org.tolven.legacypostgresql.db;

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.tolven.plugin.TolvenCommandPlugin;
import org.tolven.tools.ant.TolvenSQL;

/**
 * This plugin outputs an legacypostgresql database indexes file
 * 
 * @author Joseph Isaac
 *
 */
public class LegacyPostgresqlDBPlugin extends TolvenCommandPlugin {

    public static final String CMD_SCHEMAS = "schemas";

    public static final String CMD_INDEXES = "indexes";

    public static final String CMD_DBURL = "dbUrl";

    public static final String CMD_DRIVERCLASS = "driverClass";

    public static final String CMD_DRIVERCLASSPATH = "driverClasspath";

    public static final String CMD_USER = "user";

    public static final String CMD_PASSWORD = "password";

    private Logger logger = Logger.getLogger(LegacyPostgresqlDBPlugin.class);

    @Override
    protected void doStart() throws Exception {
        logger.info("*** start ***");
    }

    @Override
    public void execute(String[] args) {
        logger.info("*** execute ***");
        CommandLine commandLine = getCommandLine(args);
        String url = commandLine.getOptionValue(CMD_DBURL);
        String driverClass = commandLine.getOptionValue(CMD_DRIVERCLASS);
        String driverClasspath = commandLine.getOptionValue(CMD_DRIVERCLASSPATH);
        String user = commandLine.getOptionValue(CMD_USER);
        char[] password = commandLine.getOptionValue(CMD_PASSWORD).toCharArray();
        String zipSqlFilename = null;
        if (commandLine.hasOption(CMD_SCHEMAS)) {
            zipSqlFilename = "schemas.sql";
        } else {
            zipSqlFilename = "indexes.sql";
        }
        File sqlFile = getFilePath(zipSqlFilename);
        logger.info("Execute SQL file " + getPluginZip().getPath() + "!/" + zipSqlFilename + " with URL: " + url);
        TolvenSQL.sql(sqlFile, url, driverClass, user, password, driverClasspath);
    }

    private CommandLine getCommandLine(String[] args) {
        GnuParser parser = new GnuParser();
        try {
            return parser.parse(getCommandOptions(), args, true);
        } catch (ParseException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(getClass().getName(), getCommandOptions());
            throw new RuntimeException("Could not parse command line for: " + getClass().getName(), ex);
        }
    }

    private Options getCommandOptions() {
        Options cmdLineOptions = new Options();
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option(CMD_SCHEMAS, CMD_SCHEMAS, false, "\"schemas\""));
        optionGroup.addOption(new Option(CMD_INDEXES, CMD_INDEXES, false, "\"indexes\""));
        optionGroup.setRequired(true);
        cmdLineOptions.addOptionGroup(optionGroup);
        Option dbUrlOption = new Option(CMD_DBURL, CMD_DBURL, true, "\"dbUrl\"");
        dbUrlOption.setRequired(true);
        cmdLineOptions.addOption(dbUrlOption);
        Option driverClassOption = new Option(CMD_DRIVERCLASS, CMD_DRIVERCLASS, true, "\"driverClass\"");
        driverClassOption.setRequired(true);
        cmdLineOptions.addOption(driverClassOption);
        Option userOption = new Option(CMD_USER, CMD_USER, true, "\"user\"");
        userOption.setRequired(true);
        cmdLineOptions.addOption(userOption);
        Option passwordOption = new Option(CMD_PASSWORD, CMD_PASSWORD, true, "\"password\"");
        passwordOption.setRequired(true);
        cmdLineOptions.addOption(passwordOption);
        Option driverClasspathOption = new Option(CMD_DRIVERCLASSPATH, CMD_DRIVERCLASSPATH, true, "\"driverClasspath\"");
        cmdLineOptions.addOption(driverClasspathOption);
        return cmdLineOptions;
    }

    @Override
    protected void doStop() throws Exception {
        logger.info("*** stop ***");
    }
}
