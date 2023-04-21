package org.rip.smtp.client;

import java.util.List;
import org.rip.smtp.SMTP;
import org.rip.smtp.smtpReply;
import org.rip.smtp.smtpResponse;

public class quitCommand extends smtpCommand {

    public String syntax() {
        return "QUIT CRLF";
    }

    @Override
    public boolean parseArgs(String[] a) {
        return true;
    }

    @Override
    public boolean execute(SMTPClient client) {
        String cmd = SMTP.QUIT + SMTP.CRLF;
        List<smtpReply> replies = client.sendCommand(cmd);
        smtpReply reply = last(replies);
        if (reply.response == smtpResponse.Closing) {
            client.displayMessage("ok");
            client.quit();
            return true;
        }
        return true;
    }
}
