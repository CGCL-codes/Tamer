package com.gregor.smtp.smtpkill;

import com.gregor.smtp.SMTPMessage;

public interface MessageCreator {

    public abstract SMTPMessage createTestMessage(long time);
}
