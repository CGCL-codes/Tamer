package demo.examples.chat;

import iaik.utils.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sepp.api.Sepp;
import org.sepp.api.datatypes.ErrorType;
import org.sepp.api.datatypes.MessageType;
import org.sepp.api.interfaces.Component;
import org.sepp.config.SeppConfiguration;
import org.sepp.datatypes.Members;
import org.sepp.utils.Constants;

public class ConsoleChat implements Component {

    static final long serialVersionUID = 111116;

    private Log log;

    private List messageTypes = new ArrayList();

    private SeppConfiguration configuration;

    private Sepp sepp;

    private ArrayList peers = new ArrayList();

    public ConsoleChat(String configFile) {
        log = LogFactory.getLog(this.getClass());
        configuration = new SeppConfiguration(configFile);
        Thread.currentThread().setName(configuration.getNodeID());
        sepp = new Sepp(configuration);
        messageTypes.add(new MessageType(MessageType.APPLICATION_GENERAL));
        log.info("SePP Chat Client GUI started.");
        sepp.setComponent(this);
        sepp.join();
        startCommandLineInterface();
    }

    public List getInterestedMessageTypes() {
        return messageTypes;
    }

    public void receiveMessage(byte[] message, String source, MessageType messageType) {
        if (messageType.getType() == MessageType.APPLICATION_GENERAL) printIncommingMessage(source, message);
    }

    public void receiveError(byte[] message, String destination, ErrorType errorType) {
        log.error("Message couldn't be sent or received. Message: " + Util.toASCIIString(message) + "\nError: " + errorType.getDescription());
    }

    public void membersChanged(Members members) {
        ArrayList list = new ArrayList();
        for (int index = 0; index < members.size(); index++) {
            list.add(members.getMember(index));
        }
        peers.clear();
        peers.addAll(list);
    }

    public void memberDisconnected(String peerId) {
        log.info("Member peer " + peerId + " has left the SePP network.");
    }

    public void joined(boolean joined, String peerId) {
        if (joined) {
            log.debug("Peer " + peerId + " has joined the SePP network.");
        } else {
            log.debug("Peer " + peerId + " couldn't join the SePP network.");
        }
    }

    protected void printIncommingMessage(String source, byte[] data) {
        System.out.println(source + "(in) : " + Util.toASCIIString(data));
    }

    protected void printOutgoingMessage(String destination, byte[] data) {
        System.out.println(destination + "(out): " + Util.toASCIIString(data));
    }

    protected void startCommandLineInterface() {
        String input;
        Scanner in = new Scanner(System.in);
        do {
            System.out.print("> ");
            input = in.nextLine();
        } while (parseInput(input));
        System.exit(-1);
    }

    protected boolean parseInput(String input) {
        String[] command = input.split(" ");
        if (command[0].equalsIgnoreCase(Constants.CONSOLE_COMMAND_EXIT)) {
            sepp.leave();
            return false;
        }
        if (command[0].equalsIgnoreCase(Constants.CONSOLE_COMMAND_HELP)) {
            executeHelp();
            return true;
        }
        if (command[0].equalsIgnoreCase(Constants.CONSOLE_COMMAND_SENDMESSAGE)) {
            executeSendMessage(input);
            return true;
        }
        if (command[0].equalsIgnoreCase(Constants.CONSOLE_COMMAND_SHOWPEERLIST)) {
            executeListPeers();
            return true;
        }
        if (command[0].equalsIgnoreCase(Constants.CONSOLE_COMMAND_SHOWPEERNAME)) {
            executePeerName();
            return true;
        }
        System.out.println("Command '" + input + "' unknown! Type 'help' for available commands.");
        return true;
    }

    private void executeHelp() {
        StringBuffer sb = new StringBuffer();
        sb.append("Available Commands: \n");
        sb.append("Help\n");
        sb.append("Send -h <Host1;Host2;...> -m <Message>\n");
        sb.append("List\n");
        sb.append("Name\n");
        System.out.println(sb.toString());
    }

    private void executeSendMessage(String input) {
        String[] hosts;
        String message;
        hosts = (getParameter(input, "-h")).split(";");
        message = getParameter(input, "-m");
        for (int i = 0; i < hosts.length; i++) {
            sendMessageToHost(hosts[i], message);
        }
    }

    private void executeListPeers() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < peers.size(); i++) {
            sb.append(peers.get(i) + "\n");
        }
        System.out.println(sb.toString());
    }

    private void executePeerName() {
        System.out.println(configuration.getNodeID());
    }

    private void sendMessageToHost(String destination, String message) {
        if (destination instanceof String && !destination.equalsIgnoreCase(configuration.getNodeID())) {
            sepp.sendMessage(message.getBytes(), destination, new MessageType(MessageType.APPLICATION_GENERAL));
            printOutgoingMessage(destination, message.getBytes());
        } else {
            log.debug("Can not send message to myself.");
        }
    }

    private String getParameter(String input, String parameter) {
        int pos = input.indexOf(parameter);
        if (input.charAt(pos + parameter.length() + 1) == '"') {
            int firstCommaPos = pos + parameter.length() + 1;
            int secondCommaPos = input.indexOf('"', pos + parameter.length() + 3);
            if (secondCommaPos > firstCommaPos) {
                return input.substring(firstCommaPos + 1, secondCommaPos);
            } else {
                return "";
            }
        }
        if (pos >= 0) {
            return (input.substring(pos + parameter.length() + 1)).split(" ")[0];
        }
        return "";
    }

    /**
	 * @param args
	 */
    public static void main(String[] argv) {
        if (argv.length != 2) {
            System.exit(-1);
        } else {
            new ConsoleChat(argv[1]);
        }
    }
}
