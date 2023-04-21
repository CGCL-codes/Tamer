package no.telenor.isis.forskningstorget.smssend.lib;

import org.coos.messaging.Exchange;
import org.coos.messaging.ExchangePattern;
import org.coos.messaging.Message;
import org.coos.messaging.impl.DefaultExchange;

public class SMSSendExchange extends DefaultExchange {

    public SMSSendExchange(ExchangePattern pattern) {
        super(pattern);
    }

    public SMSSendExchange() {
    }
}
