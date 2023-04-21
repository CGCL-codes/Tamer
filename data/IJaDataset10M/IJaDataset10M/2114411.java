package com.genia.toolbox.utils.manager.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockTransport extends Transport {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockTransport.class);

    public static final Map<String, List<Message>> MESSAGES = new HashMap<String, List<Message>>();

    public MockTransport(Session session, URLName urlName) {
        super(session, urlName);
    }

    @Override
    public void connect() throws MessagingException {
        LOGGER.info("Connecting to MockTransport:connect()");
    }

    @Override
    public void connect(String host, int port, String username, String password) throws MessagingException {
        LOGGER.info("Connecting to MockTransport:connect(String " + host + ", int " + port + ", String " + username + ", String " + password + ")");
    }

    @Override
    public void connect(String host, String username, String password) throws MessagingException {
        LOGGER.info("Connecting to MockTransport:connect(String " + host + ", String " + username + ", String " + password + ")");
    }

    @Override
    public void sendMessage(Message message, Address[] addresses) throws MessagingException {
        LOGGER.info("Sending message '" + message.getSubject() + "'");
        for (Address address : addresses) {
            List<Message> messages = MESSAGES.get(address.toString());
            if (messages == null) {
                messages = new ArrayList<Message>();
                MESSAGES.put(address.toString(), messages);
            }
            messages.add(message);
        }
    }

    @Override
    public void close() {
        LOGGER.info("Closing MockTransport:close()");
    }
}
