package net.sf.mailand.smtp.server;

import java.io.IOException;
import java.util.Map;
import net.sf.mailand.smtp.command.EhloCommand;
import net.sf.mailand.smtp.command.ReturnCodeType;

final class EhloCommandHandler implements SmtpCommandHandler<EhloCommand> {

    public void handle(SmtpSession session, EhloCommand command) throws IOException {
        session.senderDomain(command.senderHost());
        session.reply(ReturnCodeType.OK, session.getSmtpServer().getServerDomain() + " hello", true);
        session.reply(ReturnCodeType.OK, "AUTH " + AuthCommandHandler.supportedMethods(), false);
    }

    public static void register(Map<String, SmtpCommandHandler> dispatcher) {
        dispatcher.put(EhloCommand.TYPE, new EhloCommandHandler());
    }
}
