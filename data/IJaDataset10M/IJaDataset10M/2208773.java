package PC_control;

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

/**
 *
 * @author kurttie
 */
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
        while (Main.is_connected_to_server() == 1) {
            if (socket.isClosed()) {
                Main.disconnect_from_server(0);
            }
            System.out.println("we Are Connected");
            if (socket != null && output != null && input != null) {
                try {
                    output.println("Tank ID " + Main.get_tank_id());
                    output.flush();
                    String responseLine;
                    output.println("Look");
                    output.flush();
                    while (Main.is_connected_to_server() == 1) {
                        responseLine = input.readLine();
                        if (responseLine == null) {
                            responseLine = "";
                        }
                        int size = Main.command_queue.size();
                        for (int i = 0; i < size; i++) {
                            String command = "";
                            command = Main.command_queue.remove(0);
                            this.output.println(command);
                            this.output.flush();
                        }
                        if (responseLine.startsWith("Start Tank")) {
                            Main.init_tank(responseLine);
                        }
                        if (responseLine.startsWith("Bullet")) {
                            update_bullet_list(responseLine);
                            update_tank_list(responseLine);
                            output.println("Look");
                            output.flush();
                        }
                        if (responseLine.startsWith("Move Ok")) {
                            Main.update_tank_loc(responseLine);
                        }
                        if (responseLine.startsWith("Tank Dir")) {
                            Main.update_tank_dir(responseLine);
                        }
                        if (responseLine.startsWith("Score:")) {
                            Main.update_tank_score(responseLine);
                        }
                        if (responseLine.startsWith("Dead")) {
                            Main.dead();
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

    private void update_bullet_list(String arg) {
        Main.bullet_draw.clear();
        int bullet_list_start = 0;
        int bullet_list_end = 0;
        String bullet_string = "";
        bullet_list_start = (arg.indexOf("Bullet:")) + 7;
        bullet_list_end = arg.indexOf("Tanks:");
        bullet_string = arg.substring(bullet_list_start, bullet_list_end);
        if (bullet_string.length() > 0) {
            String[] bullet_list = new String[0];
            bullet_list = bullet_string.split("\\|");
            for (String items : bullet_list) {
                String[] bullet_loc = new String[0];
                bullet_loc = items.split(",");
                Main.bullet_draw.add(bullet_loc);
            }
        }
    }

    private void update_tank_list(String arg) {
        Main.tank_draw.clear();
        int tank_list_start = 0;
        int tank_list_end = 0;
        String tank_string = "";
        tank_list_start = (arg.indexOf("Tanks:") + 6);
        tank_list_end = arg.indexOf("Obstacle:");
        tank_string = arg.substring(tank_list_start, tank_list_end);
        if (tank_string.length() > 0) {
            String[] tank_list = new String[0];
            tank_list = tank_string.split("\\|");
            for (String items : tank_list) {
                String[] tank_loc = new String[0];
                tank_loc = items.split(",");
                Main.tank_draw.add(tank_loc);
            }
        }
    }
}
