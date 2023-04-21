package de.searchworkorange.lib.commandclient.configuration;

import de.searchworkorange.lib.configuration.ConfigFileNotFoundException;
import de.searchworkorange.lib.configuration.MyConfiguration;
import de.searchworkorange.lib.logger.LoggerCollection;
import de.searchworkorange.lib.logger.LoggerConfiguration;
import de.searchworkorange.lib.timer.TimerConfiguration;
import java.io.IOException;

/**
 *
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class ConfigurationCollection extends MyConfiguration {

    private LoggerConfiguration loggerConfig = null;

    private LoggerCollection loggerCol = null;

    private CommandClientConfiguration commandConfig = null;

    private TimerConfiguration timerConfig = null;

    /**
     * 
     * @param configFileName
     * @throws ConfigFileNotFoundException
     */
    public ConfigurationCollection(String configFileName) throws ConfigFileNotFoundException {
        super(configFileName);
        loggerConfig = new LoggerConfiguration(configFileName);
        loggerCol = new LoggerCollection(loggerConfig);
        commandConfig = new CommandClientConfiguration(loggerCol, configFileName);
        timerConfig = new TimerConfiguration(configFileName);
    }

    /**
     *
     * @return String
     */
    public String getCommandServerHost() {
        return commandConfig.getCommandServerHost();
    }

    /**
     *
     * @return int
     */
    public int getCommandServerPort() {
        return commandConfig.getCommandServerPort();
    }

    /**
     * 
     * @return LoggerCollection
     */
    public LoggerCollection getLoggerCol() {
        return loggerCol;
    }

    /**
     *
     * @return TimerConfiguration
     */
    public TimerConfiguration getTimerConfig() {
        return timerConfig;
    }

    /**
     *
     * @throws IOException
     */
    public void reloadConfigFile() throws IOException {
        loggerConfig.reloadConfigFile();
        commandConfig.reloadConfigFile();
        timerConfig.reloadConfigFile();
    }
}
