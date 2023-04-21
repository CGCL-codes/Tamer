package com.softserveinc.edu.training.webserver.requests;

import java.util.Scanner;

/**
 * class for parsing a message from a post method.
 * 
 * @author marko
 * 
 * @version v1.0
 * 
 */
public class StringMessageBodyParser implements MessageBodyParser {

    private MessageBody messageFromClient;

    private Scanner clientInputStream;

    public StringMessageBodyParser() {
    }

    @Override
    public void parse() throws WrongFormatException {
        try {
            messageFromClient = new MessageBody();
            String line = (clientInputStream).nextLine();
            String[] parametersPairs = line.split("&");
            for (int i = 0; i < parametersPairs.length; i++) {
                String[] pair = parametersPairs[i].split("=");
                messageFromClient.addParameter(pair[0], pair[1]);
            }
        } catch (Exception e) {
            throw new WrongFormatException(WrongFormatException.UNKNOWN_FORMAT);
        }
    }

    @Override
    public MessageBody getMessageBody() {
        return this.messageFromClient;
    }

    @Override
    public void setClientInputStream(Scanner clientInputStream) {
        this.clientInputStream = clientInputStream;
    }
}
