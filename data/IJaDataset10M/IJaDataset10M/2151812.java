package de.develop.server;

import de.develop.enums.DebugTypes;
import de.develop.gui.ServerAdminGUI;

/**
 * Main is used to start the servers and handle the debug messages.
 *
 * @author Nuno Freitas (nunofreitas@gmail.com)
 */
public class Main {

    public static GameServer gameServer;

    public static ServerAdminGUI serverAdminGUI;

    public static PolicyServer policyServer;

    public static GameScheduler gameManager;

    /**
     *  If debug is enabled writes the message through the GUI.
     *
     * @param label the label of the message to write
     * @param msg the message to write
     */
    public static void debug(DebugTypes debugType, String label, String msg) {
        if (Main.serverAdminGUI != null) {
            Main.serverAdminGUI.write(debugType, label + ": " + msg);
        }
    }

    /**
     * Starts the chat server, the policy server and the GUI for debug messages.
     *
     * @param args the command line arguments (first is the chat server port and second is the policy server port)
     */
    public static void main(String[] args) {
        try {
            int chatPort = 5555;
            int policyPort = chatPort + 1;
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    chatPort = Integer.parseInt(args[i]);
                }
                if (i == 1) {
                    policyPort = Integer.parseInt(args[i]);
                }
            }
            PolicyServer policyServer = new PolicyServer(policyPort);
            policyServer.start();
            GameServer chatServer = new GameServer(chatPort);
            chatServer.start();
            GameScheduler gameManager = new GameScheduler(chatServer);
            gameManager.start();
            ServerAdminGUI gui = new ServerAdminGUI(chatServer);
            gui.setTitle("ChatServer");
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);
            Main.gameServer = chatServer;
            Main.serverAdminGUI = gui;
            Main.policyServer = policyServer;
            Main.gameManager = gameManager;
        } catch (Exception e) {
            debug(DebugTypes.MAIN, "Main", "Exception (main)" + e.getMessage());
        }
    }
}
