package org.rjam.admin.command;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.rjam.base.BaseLogging;
import org.rjam.base.Logger;
import org.rjam.net.command.server.api.ICommand;
import org.rjam.net.command.server.api.ICommandProcessor;
import org.rjam.net.command.server.api.IRequestContext;

public class SetLog extends BaseLogging implements ICommand {

    private static final long serialVersionUID = 1L;

    public void execute(ICommandProcessor processor, IRequestContext context) throws IOException {
        if (!context.hasNext()) {
            processor.reply(REPLY_500_ERROR, "Logger name is required.");
            return;
        }
        String name = context.getNextToken();
        if (name.equals("all")) {
            name = ".*";
        }
        if (!context.hasNext()) {
            processor.reply(REPLY_500_ERROR, "Logger level is required.");
            return;
        }
        String tmp = context.getNextToken();
        int level = Logger.getLevel(tmp);
        Map<String, Logger> map = Logger.getLoggers();
        processor.reply(REPLY_300_TEMPOARY_OK, "Matching loggers follows");
        int cnt = 0;
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
            String nm = (String) it.next();
            if (nm.matches(name)) {
                Logger logger = (Logger) map.get(nm);
                logger.setLevel(level);
                processor.reply(nm);
                cnt++;
            }
        }
        processor.reply(REPLY_200_OK, "" + cnt + " loggers set to " + Logger.getLevelName(level));
    }

    public String getName() {
        return "SetLog";
    }

    public boolean isAutherized(ICommandProcessor processor, IRequestContext context) {
        return true;
    }
}
