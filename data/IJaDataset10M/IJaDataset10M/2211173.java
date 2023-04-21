package com.aneliya.proxy;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.aneliya.command.server.LoginReceivedCommand;
import com.aneliya.command.server.LogoutReceivedCommand;
import com.aneliya.command.server.MessageReceivedCommand;
import com.aneliya.framework.mvc.Facade;
import com.aneliya.framework.mvc.model.Proxy;
import com.aneliya.model.Message;
import com.aneliya.server.ServerListener;
import com.aneliya.server.ServerSender;

public class ServerProxy extends Proxy {

    private static final String CONFIGURATION_FILE_URL = "config.xml";

    private static final String SERVER_LISTENER_BEAN_ID = "serverListener";

    private static final String SERVER_SENDER_BEAN_ID = "serverSender";

    public static final String MESSAGE_RECEIVED_EVENT = "messageReceivedEvent";

    public static final String LOGIN_RECEIVED_EVENT = "loginReceivedEvent";

    public static final String LOGOUT_RECEIVED_EVENT = "logoutReceivedEvent";

    private ServerListener serverListener;

    private ServerSender serverSender;

    public ServerProxy() throws Exception {
        name = "ServerProxy";
        facade = Facade.getInstance();
        FileSystemXmlApplicationContext factory = new FileSystemXmlApplicationContext(CONFIGURATION_FILE_URL);
        serverListener = (ServerListener) factory.getBean(SERVER_LISTENER_BEAN_ID);
        serverSender = (ServerSender) factory.getBean(SERVER_SENDER_BEAN_ID);
        initializeCommands();
    }

    private void initializeCommands() throws Exception {
        facade.registerCommand(MESSAGE_RECEIVED_EVENT, MessageReceivedCommand.class);
        facade.registerCommand(LOGIN_RECEIVED_EVENT, LoginReceivedCommand.class);
        facade.registerCommand(LOGOUT_RECEIVED_EVENT, LogoutReceivedCommand.class);
    }

    public void start() {
        serverSender.start();
        serverListener.start();
    }

    public void sendMessage(Message message) {
        serverSender.sendMessage(message);
    }
}
