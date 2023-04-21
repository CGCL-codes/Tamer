package com.ericdaugherty.mail.server.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.ericdaugherty.mail.server.errors.TooManyErrorsException;
import com.ericdaugherty.mail.server.info.User;
import com.ericdaugherty.mail.server.services.general.StreamHandler;

/**
 * A pop3 persistance processor interface.
 *
 * @author Andreas Kyrmegalos
 */
public interface POP3MessagePersistenceProccessor {

    public void setUser(User user);

    public String[] populatePOP3MessageList();

    public String[] deleteMessages();

    public void retreiveMessage(StreamHandler pop3CH, int messageNumber) throws TooManyErrorsException, FileNotFoundException, IOException;

    public void retreiveMessageTop(StreamHandler pop3CH, int messageNumber, long numLines) throws TooManyErrorsException, FileNotFoundException, IOException;
}
