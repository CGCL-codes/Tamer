package org.ourgrid.aggregator.ui.sync;

import org.ourgrid.common.command.UIMessages;

public class AggregatorUIMessages {

    public static final String STARTED = "OurGrid Aggregator was successfully started";

    private static final String AGGREGATOR_COMMAND_PREFIX = "aggregator";

    public static String getSuccessMessage(String commandName) {
        return UIMessages.getSuccessMessage(AGGREGATOR_COMMAND_PREFIX, commandName);
    }

    public static String getWrongUsageMessage() {
        return "Usage: aggregator {start|stop|status}";
    }
}
