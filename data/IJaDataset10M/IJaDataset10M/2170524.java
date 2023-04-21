package com.vitria.test.client.worker.jms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.vitria.test.client.framework.Workable;
import com.vitria.test.client.worker.common.ConfigurationInvalidException;
import com.vitria.test.commandline.CommandParser;
import com.vitria.test.commandline.CommandParserFactory;
import com.vitria.test.commandline.api.CommandException;
import com.vitria.test.commandline.api.CommandOption;
import com.vitria.test.commandline.api.CommandOptionValueType;
import com.vitria.test.common.jms.ConnectionInfo;

public class MessagePublisherClient extends MessageClientBase {

    public static void main(String[] args) {
        MessageClientBase msc = new MessagePublisherClient(Arrays.asList(args));
        msc.run();
    }

    public MessagePublisherClient(List<String> args_) {
        super(args_);
    }

    protected Workable createWorker(Map<String, Object> config, List<String> extraCmds) throws Exception {
        ConnectionInfo info = getConnectionInfo(config, false);
        int sleepSeconds = getSleepSeconds(config);
        Policy policy = getPolicy(config, info);
        log(info.toString());
        log(Constants.SLEEP_SECONDS + ": " + sleepSeconds);
        if (policy != null) {
            log("publish policy: " + policy);
        }
        log("");
        MessagePublisherWorker mpw = new MessagePublisherWorker(info, policy, sleepSeconds);
        return mpw;
    }

    @Override
    protected Map<String, Object> getParameters(Map<String, Object> cmdOpts) throws ConfigurationInvalidException {
        Map<String, Object> params = super.getParameters(cmdOpts);
        if (cmdOpts.containsKey(Constants.LIMIT)) {
            params.put(Constants.PUB_POLICY, Constants.PUB_POLICY_COUNT_MONITOR);
        }
        if (!params.containsKey(Constants.SLEEP_SECONDS)) {
            params.put(Constants.SLEEP_SECONDS, 10);
        }
        return params;
    }

    protected String getDefaultConfigurationPath() {
        return Constants.DEFAULT_PUB_CONFIGURATION_PATH;
    }

    protected CommandParser createCommandParser() {
        try {
            ArrayList<CommandOption> cmdOpts = new ArrayList<CommandOption>();
            CommandOption co = CommandParserFactory.createCommandOption("limit", false, Constants.LIMIT, "message count limit in the destination before stopping publish");
            co.setValueType(CommandOptionValueType.INTEGER);
            cmdOpts.add(co);
            co = CommandParserFactory.createCommandOption("sleep", false, Constants.SLEEP_SECONDS, "sleep seconds between publishing each event or sleep before re-check the message count in destination");
            co.setValueType(CommandOptionValueType.INTEGER);
            cmdOpts.add(co);
            co = CommandParserFactory.createCommandOption("prefix", false, Constants.CONFIGURATION_PREFIX, "configuration file prefix e.g. a-message-publisher.properties");
            cmdOpts.add(co);
            return CommandParserFactory.createCommandParser("mpc", cmdOpts);
        } catch (CommandException ce) {
            Logger.error("", ce);
        }
        return null;
    }
}
