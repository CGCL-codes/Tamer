package net.sf.mailand.smtp.server;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.mailand.ParseException;
import net.sf.mailand.smtp.command.DataCommand;
import net.sf.mailand.smtp.command.ReturnCodeType;

final class DataCommandHandler implements SmtpCommandHandler<DataCommand> {

    private static Logger logger = Logger.getLogger(DataCommandHandler.class.getName());

    private DataCommandHandler() {
    }

    public void handle(SmtpSession session, DataCommand command) throws IOException {
        if (session.forwardPath().size() == 0) {
            session.reply(ReturnCodeType.BAD_SEQUENCE);
            return;
        }
        session.reply(ReturnCodeType.GO_AHEAD);
        String data;
        try {
            data = command.readData();
        } catch (ParseException e) {
            logger.info("Mail to large!");
            return;
        }
        assert session.reversePath() != null && session.forwardPath().size() > 0;
        session.getSmtpServer().deliver(session.getUsername(), session.reversePath(), session.forwardPath(), data);
        session.reply(ReturnCodeType.OK);
    }

    public static void register(Map<String, SmtpCommandHandler> dispatcher) {
        dispatcher.put(DataCommand.TYPE, new DataCommandHandler());
    }
}
