package ttt.client;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import com.sun.org.apache.bcel.internal.generic.CPInstruction;
import ttt.core.Command;
import ttt.core.CommandListener;
import ttt.core.Desk;
import ttt.core.Status;
import ttt.core.Turn;
import ttt.core.XStreamCommand;

public class GameClient {

    private class ClientCommandListener implements CommandListener {

        public void readCallback(String cmd) {
            readCallback((Command) xstream.fromXML(cmd));
        }

        public void readCallback(Command cmd) {
            System.out.println(cmd.getName());
            String cmdName = cmd.getName();
            try {
                if (cmdName.equals("status")) {
                    Status s = (Status) cmd.getData();
                    if (s.getStatus() == Status.FAIL) {
                        connectionEnd("Bad status ... " + s.getMessage());
                    }
                    if (s.getStatus() == Status.OK) {
                        System.out.println("Ok ... " + s.getMessage());
                    }
                } else if (cmdName.equals("desk")) {
                    Desk desk = (Desk) cmd.getData();
                    System.out.println(desk.toString());
                    sendCommand(new Command("turn", inputTurn()));
                }
            } catch (ClassCastException e) {
                System.err.println("Invalid data for command '" + cmdName + "'!");
            }
        }
    }

    private class ClientThread extends Thread {

        private CommandListener commandListener;

        public ClientThread(CommandListener commandListener) {
            super();
            this.commandListener = commandListener;
        }

        public void run() {
            super.run();
            yield();
            StringBuffer sb = new StringBuffer();
            while (true) {
                try {
                    int c = in.read();
                    if (c == -1) {
                        connectionEnd("Conection lost...");
                    }
                    if (c == 0) {
                        commandListener.readCallback(sb.toString());
                        sb = new StringBuffer();
                    } else {
                        sb.append((char) c);
                    }
                } catch (SocketException e) {
                    connectionEnd("Conection lost...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public CommandListener getCommandListener() {
            return commandListener;
        }

        public void setCommandListener(CommandListener commandListener) {
            this.commandListener = commandListener;
        }
    }

    private Socket socket;

    private PrintWriter out;

    private BufferedReader in;

    private XStreamCommand xstream = new XStreamCommand();

    public GameClient() {
        connect();
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendCommand(new Command("hello", null));
        ClientThread ct = new ClientThread(new ClientCommandListener());
        ct.start();
        while (true) {
        }
    }

    private void sendCommand(Command command) {
        String msg = xstream.toXML(command);
        out.println(msg + (char) 0);
        out.flush();
    }

    private void connect() {
        try {
            socket = new Socket("a.thr.cz", 6553);
        } catch (UnknownHostException e) {
            connectionEnd("Host is unknown ...");
        } catch (IOException e) {
            connectionEnd("Could not connect ...");
        }
    }

    private void connectionEnd(String ann) {
        out.close();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(ann);
        System.exit(1);
    }

    private Turn inputTurn() {
        Scanner sc = new Scanner(System.in);
        int x, y;
        System.out.println("X: ");
        x = sc.nextInt();
        System.out.println("Y: ");
        y = sc.nextInt();
        return (new Turn(y - 1, x - 1));
    }
}
