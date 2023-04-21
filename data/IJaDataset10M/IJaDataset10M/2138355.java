package backbone;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ListIterator;
import java.util.Vector;
import java.lang.Math;
import java.net.UnknownHostException;

public class ConnectionHandler extends Thread {

    private PrintWriter output = null;

    private BufferedReader input = null;

    private Socket socket = null;

    public ConnectionHandler(String server_name, int port_num) {
        try {
            this.socket = new Socket(server_name, port_num);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    @Override
    public void run() {
        while (Main.is_connected_to_server()) {
            if (socket.isClosed()) {
                Main.disconnect_from_server();
            }
            System.out.println("we Are Connected");
            if (socket != null && output != null && input != null) {
                try {
                    output.println("Tank ID " + Main.get_tank_id());
                    output.flush();
                    String responseLine;
                    output.println("Look");
                    output.flush();
                    while (Main.is_connected_to_server()) {
                        responseLine = input.readLine();
                        if (!(responseLine == null)) {
                            if (responseLine.startsWith("Start Tank")) {
                                Main.init_tank(responseLine);
                            } else if (responseLine.startsWith("Move Ok")) {
                                Main.update_tank_loc(responseLine);
                            } else if (responseLine.startsWith("Tank Dir")) {
                                Main.update_tank_dir(responseLine);
                            } else if (responseLine.startsWith("Dead")) {
                                Main.dead();
                            } else {
                                Main.incoming_message_queue.add(responseLine);
                            }
                            if (responseLine.indexOf("/quit") != -1) {
                                break;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ie) {
                                System.out.println(ie);
                            }
                        }
                        int size = 0;
                        size = Main.outgoing_message_queue.size();
                        for (int i = 0; i < size; i++) {
                            String command = "";
                            command = Main.outgoing_message_queue.remove(0);
                            this.output.println(command);
                            this.output.flush();
                        }
                    }
                } catch (UnknownHostException e) {
                    System.err.println("Trying to connect to unknown host: " + e);
                } catch (IOException e) {
                    System.err.println("IOException:  " + e);
                }
            }
            try {
                output.close();
                input.close();
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
