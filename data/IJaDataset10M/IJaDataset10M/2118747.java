package simpleclient.client;

import com.sun.sgs.client.simple.SimpleClient;
import java.util.Properties;
import java.io.*;
import javax.swing.JOptionPane;

public class MySimpleClient {

    public static void main(String[] args) {
        MySimpleClientListener listen = new MySimpleClientListener();
        SimpleClient sc = new SimpleClient(listen);
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("host", args[0]);
        connectionProperties.setProperty("port", args[1]);
        try {
            sc.login(connectionProperties);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        try {
            listen.waitForLogin();
        } catch (InterruptedException ignore) {
        }
        String cmd = null;
        do {
            cmd = JOptionPane.showInputDialog(null, "Please enter a command", "");
            if (cmd != null) {
                try {
                    sc.send(cmd.getBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } while (cmd != null && !"logout".equals(cmd));
        sc.logout(false);
    }
}
