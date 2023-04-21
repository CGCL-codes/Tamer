package com.peterhi.player.actions;

import java.awt.event.ActionEvent;
import com.peterhi.net.messages.PublicTextMessage;
import com.peterhi.client.SocketClient;
import com.peterhi.player.Window;
import com.peterhi.player.Console;

public class SendAction extends BaseAction {

    private static final SendAction instance = new SendAction();

    public static SendAction getInstance() {
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PublicTextMessage message = new PublicTextMessage();
        message.sender = Window.getWindow().id;
        message.text = Console.getConsole().getText();
        try {
            SocketClient.getInstance().send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Console.getConsole().flush();
    }
}
