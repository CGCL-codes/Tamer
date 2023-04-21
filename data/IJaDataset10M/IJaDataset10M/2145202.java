package com.googlecode.jsendnsca.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.ArrayUtils;
import com.googlecode.jsendnsca.core.MessagePayload;
import com.googlecode.jsendnsca.core.NagiosSettings;

/**
 * Used to parse the command line options
 * 
 * @author Raj.Patel
 * @version 1.0
 */
@SuppressWarnings("static-access")
public class OptionsParser {

    private static final String TIMEOUT_OPTION = "timeout";

    private static final String PASSWORD_OPTION = "password";

    private static final String PORT_OPTION = "port";

    private static final String ALERTINGHOST_OPTION = "alertinghost";

    private static final String NAGIOSHOST_OPTION = "nagioshost";

    private static final String[] LEVELS = new String[] { "OK", "WARNING", "CRITICAL" };

    private Options options;

    private CommandLine commandLine;

    private String[] messageArgs;

    /**
     * Construct a new {@link OptionsParser} using the provided command line
     * arguments
     * 
     * @param args
     *            the command line arguments
     * @throws UsageException
     *             thrown on invalid or missing arguments
     */
    public OptionsParser(String[] args) throws UsageException {
        options = buildNagiosOptions();
        try {
            commandLine = new GnuParser().parse(options, args);
            messageArgs = commandLine.getArgs();
            if (messageArgs.length < 3 || !ArrayUtils.contains(LEVELS, messageArgs[0])) {
                throw new UsageException(options);
            }
        } catch (ParseException e) {
            throw new UsageException(options);
        }
    }

    private Options buildNagiosOptions() {
        final Options options = new Options();
        options.addOption(getOption(NAGIOSHOST_OPTION, "nagios host", "the host where nagios is running, defaults to localhost"));
        options.addOption(getOption(ALERTINGHOST_OPTION, "alerting host", "the host sending the passive check, defaults to using the hostname of the machine"));
        options.addOption(getOption(PORT_OPTION, PORT_OPTION, "the port on which NSCA is running, defaults to 5667"));
        options.addOption(getOption(PASSWORD_OPTION, "nsca password", "the password configured in NSCA, defaults to password"));
        options.addOption(getOption(TIMEOUT_OPTION, "send timeout", "the timeout to use when sending the passive check in ms, defaults to 10000"));
        return options;
    }

    private Option getOption(String longName, String argName, String description) {
        final Option option = OptionBuilder.withLongOpt(longName).withArgName(argName).hasArg().withDescription(description).isRequired(false).create();
        return option;
    }

    /**
     * Get {@link NagiosSettings} from the provided command line
     * 
     * @return the {@link NagiosSettings} to use to send the passive check
     */
    public NagiosSettings getNagiosSettings() {
        final NagiosSettings nagiosSettings = new NagiosSettings();
        nagiosSettings.setNagiosHost(commandLine.getOptionValue(NAGIOSHOST_OPTION, nagiosSettings.getNagiosHost()));
        nagiosSettings.setPassword(commandLine.getOptionValue(PASSWORD_OPTION, nagiosSettings.getPassword()));
        nagiosSettings.setPort(getIntegerOptionValue(commandLine, PORT_OPTION, nagiosSettings.getPort()));
        nagiosSettings.setTimeout(getIntegerOptionValue(commandLine, TIMEOUT_OPTION, nagiosSettings.getTimeout()));
        return nagiosSettings;
    }

    /**
     * Get the {@link MessagePayload} of the passive check as specified on the
     * command line
     * 
     * @return the {@link MessagePayload}
     */
    public MessagePayload getMessagePayload() {
        final MessagePayload messagePayload = new MessagePayload();
        messagePayload.setHostname(commandLine.getOptionValue(ALERTINGHOST_OPTION, "localhost"));
        messagePayload.setLevel(messageArgs[0]);
        messagePayload.setServiceName(messageArgs[1]);
        messagePayload.setMessage(messageArgs[2]);
        return messagePayload;
    }

    private int getIntegerOptionValue(CommandLine commandLine, String option, int defaultValue) {
        return Integer.parseInt(commandLine.getOptionValue(option, String.valueOf(defaultValue)));
    }
}
