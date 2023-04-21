package intranetchatv3.core;

import intranetchatv3.display.MainDisplay;
import intranetchatv3.df.MessageTypes;
import intranetchatv3.df.NetworkObj;
import intranetchatv3.df.VariableStore;
import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;

/**
 * This is the tread which is tasked with dealing with the incoming messages and
 * informing all required observers that there is a message waiting
 * @author Philip White
 */
public class NetworkListener implements Runnable {

    private NetworkInterface network;

    private MainDisplay display;

    private VariableStore store;

    private UserCollection users;

    private PrivateChatController controller;

    /**
     * creates an instance of a networkInterface and then is set into a constant
     * loop to listen for messages
     */
    public void run() {
        display = MainDisplay.getInstance();
        store = VariableStore.getInstance();
        network = NetworkInterface.getInstance();
        users = UserCollection.getInstance();
        controller = PrivateChatController.getInstance();
        while (true) {
            NetworkObj s = network.recieveMulticast();
            assigning(s);
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void assigning(NetworkObj o) {
        String mess;
        switch(o.getType()) {
            case MessageTypes.PUBLIC_MESSAGE:
                Color text;
                if (o.getSourceID().compareTo(network.getID()) == 0) {
                    text = store.out;
                } else {
                    text = store.in;
                }
                mess = getTime() + " " + o.getSource() + " : " + o.getMessage();
                display.addContents(mess, text);
                break;
            case MessageTypes.PRIVATE_MESSAGE:
                controller.incoming(o);
                break;
            case MessageTypes.USER_JOINED:
                users.addUser(o);
                mess = getTime() + " " + o.getSource() + " has joined";
                display.addContents(mess, store.system);
                display.userList();
                if (o.getSourceID().compareTo(store.networkID) != 0) {
                    NetworkObj net = new NetworkObj();
                    net.setSource(store.username);
                    net.setSourceID(store.networkID);
                    net.setType(MessageTypes.USER_BROADCAST);
                    try {
                        network.sendMulticast(net);
                    } catch (IOException ex) {
                    }
                }
                break;
            case MessageTypes.USER_BROADCAST:
                users.addUser(o);
                display.userList();
                break;
            case MessageTypes.USER_LEFT:
                users.removeUser(o);
                mess = getTime() + " " + o.getSource() + " has left";
                display.addContents(mess, store.system);
                display.userList();
                break;
            case MessageTypes.RETRANSMIT:
                try {
                    network.sendMulticast(network.lastSent);
                } catch (IOException ex) {
                }
                break;
            case MessageTypes.FILE_TRANSFER:
                break;
            case MessageTypes.SHUTDOWN:
                if (o.containsID(network.getID())) {
                    network.leaveMulticastGroup();
                }
                System.exit(0);
                break;
            case MessageTypes.USERNAME_CHANGE:
                String pre = users.updateUser(o);
                mess = getTime() + " " + pre + " changed to " + o.getSource();
                display.addContents(mess, store.system);
                break;
            case MessageTypes.PRIVATE_USERADD:
                controller.incoming(o);
                break;
            case MessageTypes.PRIVATE_USERLEFT:
                controller.incoming(o);
            default:
                System.out.println("Illegal Type");
        }
    }

    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        String minute = calendar.get(Calendar.MINUTE) + "";
        String second = calendar.get(Calendar.SECOND) + "";
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            hour = hour + 12;
        }
        String h = hour + "";
        if (h.length() < 2) {
            h = "0" + h;
        }
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        if (second.length() < 2) {
            second = "0" + second;
        }
        return "[" + h + ":" + minute + ":" + second + "]";
    }
}
