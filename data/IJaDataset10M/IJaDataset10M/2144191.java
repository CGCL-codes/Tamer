package pop3server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import static pop3server.ServerState.*;

class POP3ServerThread extends Thread {

    private int name;

    private Socket socket;

    private Map<String, String> passwd = new HashMap<String, String>();

    private ServerState state = AUTHORIZATION;

    private Maildrop maildrop;

    public POP3ServerThread(int num, Socket sock, Maildrop md) {
        this.name = num;
        this.socket = sock;
        this.passwd.put("till", "geheim");
        this.maildrop = md;
    }

    public void run() {
        BufferedReader inFromClient;
        DataOutputStream outToClient;
        boolean running = true;
        String user = null;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new DataOutputStream(socket.getOutputStream());
            outToClient.writeBytes("+OK POP3 server ready\n");
            while (running) {
                String[] clientSeq = inFromClient.readLine().split(" ");
                switch(state) {
                    case AUTHORIZATION:
                        if (clientSeq[0].equalsIgnoreCase("QUIT")) {
                            running = false;
                            outToClient.writeBytes("QUIT recieved. Adios!\n");
                        } else if (clientSeq[0].equalsIgnoreCase("USER")) {
                            if (clientSeq.length < 2) {
                                outToClient.writeBytes("-ERR username is missing\n");
                                user = null;
                            } else {
                                outToClient.writeBytes("+OK password please\n");
                                user = clientSeq[1];
                            }
                        } else if (clientSeq[0].equalsIgnoreCase("PASS") && user != null) {
                            if (clientSeq.length < 2) {
                                outToClient.writeBytes("-ERR password is missing\n");
                            } else if (passwd.containsKey(user) && passwd.get(user).equals(clientSeq[1])) {
                                outToClient.writeBytes("+OK login succesfull\n");
                                state = TRANSACTION;
                            } else outToClient.writeBytes("-ERR wrong username or password\n");
                        } else outToClient.writeBytes("-ERR unknown command\n");
                        break;
                    case TRANSACTION:
                        if (!(maildrop instanceof Maildrop)) {
                            outToClient.writeBytes("-ERR unhandable error. Please log in angain\n");
                            state = AUTHORIZATION;
                            user = null;
                        } else if (clientSeq[0].equalsIgnoreCase("QUIT")) {
                            outToClient.writeBytes("+OK QUIT recieved. Cleaning up...\n");
                            state = UPDATE;
                        } else if (clientSeq[0].equalsIgnoreCase("STAT")) {
                            outToClient.writeBytes("+OK " + maildrop.size() + " " + maildrop.bytes() + "\n");
                        } else if (clientSeq[0].equalsIgnoreCase("LIST")) {
                            outToClient.writeBytes("+OK " + maildrop.size() + " messages (" + maildrop.bytes() + " bytes)\n");
                            for (String line : maildrop.getMailStats()) outToClient.writeBytes(line + "\n");
                            outToClient.writeBytes(".\n");
                        } else if (clientSeq[0].equalsIgnoreCase("RETR")) {
                            if (clientSeq.length < 2) {
                                outToClient.writeBytes("-ERR message id is missing\n");
                            } else if (!maildrop.hasEmail(Integer.parseInt(clientSeq[1]))) {
                                outToClient.writeBytes("-ERR no such message, only " + maildrop.size() + " messages in maildrop\n");
                            } else {
                                Email m = maildrop.getEmail(Integer.parseInt(clientSeq[1]));
                                outToClient.writeBytes("+OK " + m.size() + " bytes\n");
                                for (String line : m.getLines()) outToClient.writeBytes(line + "\n");
                                outToClient.writeBytes(".\n");
                            }
                        } else if (clientSeq[0].equalsIgnoreCase("DELE")) {
                            if (clientSeq.length < 2) {
                                outToClient.writeBytes("-ERR message id is missing\n");
                            } else if (!maildrop.hasEmail(Integer.parseInt(clientSeq[1]))) {
                                outToClient.writeBytes("-ERR no such message, only " + maildrop.size() + " messages in maildrop\n");
                            } else {
                                maildrop.deleteEmail(Integer.parseInt(clientSeq[1]));
                                outToClient.writeBytes("+OK message " + clientSeq[1] + " deleted\n");
                            }
                        } else if (clientSeq[0].equalsIgnoreCase("NOOP")) {
                            outToClient.writeBytes("+OK\n");
                        } else if (clientSeq[0].equalsIgnoreCase("RSET")) {
                            maildrop.reset();
                            outToClient.writeBytes("+OK maildrop has " + maildrop.size() + " messages (" + maildrop.bytes() + " bytes)" + "\n");
                        } else if (clientSeq[0].equalsIgnoreCase("UIDL")) {
                            outToClient.writeBytes("+OK\n");
                            for (String line : maildrop.getMailUIDL()) outToClient.writeBytes(line + "\n");
                            outToClient.writeBytes(".\n");
                        } else outToClient.writeBytes("-ERR unknown command\n");
                        break;
                    case UPDATE:
                        if (clientSeq[0].equalsIgnoreCase("QUIT")) {
                            running = false;
                            maildrop.cleanUp();
                            outToClient.writeBytes("+OK closing connection. Bye bye " + user + "\n");
                        } else outToClient.writeBytes("-ERR unknown command\n");
                        break;
                    default:
                        System.exit(1);
                }
            }
            socket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
        System.out.println("TCP Server Thread " + name + " stopped!");
    }
}
