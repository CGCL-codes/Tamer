package schemacrawler.main;

import javax.sql.DataSource;
import schemacrawler.main.dbconnector.BundledDriverDatabaseConnector;
import schemacrawler.main.dbconnector.DatabaseConnector;
import schemacrawler.main.dbconnector.DatabaseConnectorException;
import schemacrawler.main.dbconnector.PropertiesDataSourceDatabaseConnector;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.tools.Commands;
import schemacrawler.tools.OutputOptions;
import sf.util.Utilities;

/**
 * Utility for parsing the SchemaCrawler command line.
 * 
 * @author Sualeh Fatehi
 */
public class SchemaCrawlerCommandLine {

    private final Commands commands;

    private final Config config;

    private final SchemaCrawlerOptions schemaCrawlerOptions;

    private final OutputOptions outputOptions;

    private final DatabaseConnector databaseConnector;

    public SchemaCrawlerCommandLine(final Commands commands, final Config config, final DatabaseConnector databaseConnector, final OutputOptions outputOptions) {
        this.commands = commands;
        this.config = config;
        this.databaseConnector = databaseConnector;
        this.outputOptions = outputOptions;
        schemaCrawlerOptions = new SchemaCrawlerOptions();
    }

    /**
   * Loads objects from command line options.
   * 
   * @param args
   *        Command line arguments.
   * @throws SchemaCrawlerException
   *         On an exception
   */
    public SchemaCrawlerCommandLine(final String[] args, final HelpOptions helpOptions) throws SchemaCrawlerException {
        this(args, helpOptions, null);
    }

    /**
   * Loads objects from command line options. Optionally loads the
   * config from the classpath.
   * 
   * @param args
   *        Command line arguments.
   * @param configResource
   *        Config resource.
   * @throws SchemaCrawlerException
   *         On an exception
   */
    public SchemaCrawlerCommandLine(final String[] args, final HelpOptions helpOptions, final String configResource) throws SchemaCrawlerException {
        final ApplicationOptions applicationOptions;
        if (args != null && args.length > 0) {
            applicationOptions = new ApplicationOptionsParser(args).getOptions();
            commands = new CommandParser(args).getOptions();
            outputOptions = new OutputOptionsParser(args).getOptions();
        } else {
            applicationOptions = new ApplicationOptions();
            commands = new Commands();
            outputOptions = new OutputOptions();
        }
        if (applicationOptions.isShowHelp()) {
            helpOptions.showHelp();
            System.exit(0);
        }
        applicationOptions.applyApplicationLogLevel();
        try {
            if (!Utilities.isBlank(configResource)) {
                config = Config.load(SchemaCrawlerCommandLine.class.getResourceAsStream(configResource));
                databaseConnector = new BundledDriverDatabaseConnector(args, config);
            } else {
                if (args != null && args.length > 0) {
                    config = new ConfigParser(args).getOptions();
                } else {
                    config = new Config();
                }
                databaseConnector = new PropertiesDataSourceDatabaseConnector(args, config);
            }
        } catch (final DatabaseConnectorException e) {
            throw new SchemaCrawlerException("Cannot create a database connector - re-run with -help for help", e);
        }
        schemaCrawlerOptions = new SchemaCrawlerOptionsParser(args, config, databaseConnector.getDataSourceName()).getOptions();
    }

    /**
   * Creates the datasource.
   * 
   * @return Datasource
   * @throws DatabaseConnectorException
   *         On an exception
   */
    public DataSource createDataSource() throws DatabaseConnectorException {
        return databaseConnector.createDataSource();
    }

    /**
   * Gets the commands.
   * 
   * @return Commands.
   */
    public Commands getCommands() {
        return commands;
    }

    /**
   * Gets the config.
   * 
   * @return Config.
   */
    public Config getConfig() {
        return new Config(config);
    }

    /**
   * Gets the output options.
   * 
   * @return Output options.
   */
    public OutputOptions getOutputOptions() {
        return outputOptions.duplicate();
    }

    /**
   * Gets the partition in the config.
   * 
   * @return Partition in the config
   */
    public String getPartition() {
        return databaseConnector.getDataSourceName();
    }

    public SchemaCrawlerOptions getSchemaCrawlerOptions() {
        return schemaCrawlerOptions;
    }
}
