package org.rip.smtp.client;

public class helpCommand extends smtpCommand {

    public String syntax() {
        return "HELP [ SP String ] CRLF";
    }
}
