package core.net;

import core.exceptions.WrongPackageTypeException;
import utils.OOUtil;
import core.net.protocols.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient extends Thread {

    private String nick;

    private TCPClient client;

    private ChatProtocol protocol = new ChatProtocol();

    private int id = -1;

    public ChatClient() {
        this.nick = OOUtil.readString("Nick: ");
        this.client = new TCPClient(OOUtil.readString("Hostname: "), OOUtil.readInt("Port: "), protocol);
        new Thread(this).start();
        new UpdaterThread();
    }

    @Override
    public void run() {
        String inp = "";
        while (!inp.equals("/quit")) {
            TCPPackage pkg = protocol.createPackage(id, nick);
            pkg.addLine("MSG=" + nick + ": " + OOUtil.readString(""));
            try {
                client.sendPackage(pkg);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrongPackageTypeException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                update();
            } catch (WrongPackageTypeException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void update() throws WrongPackageTypeException {
        TCPPackage pkg = client.getRecievedPackage();
        String[] msg = protocol.getMessages(pkg);
        if (id == -1) {
            id = protocol.getClientID(pkg);
        }
        for (int i = 0; i < msg.length; i++) {
            System.out.println(msg[i]);
        }
    }

    public static void main(String[] args) {
        new ChatClient();
    }

    private class UpdaterThread extends Thread {

        public UpdaterThread() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    client.sendPackage(protocol.createPackage(id, nick, ""));
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WrongPackageTypeException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    update();
                } catch (WrongPackageTypeException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    this.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
