package eric;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class JUniqueInstance {

    private final int port;

    private final String message;

    public JUniqueInstance(final int port, final String message) {
        assert port > 0 && port < 1 << 16 : "Le port doit être entre 1 et 65535";
        this.port = port;
        this.message = message;
    }

    public JUniqueInstance(final int port) {
        this(port, null);
    }

    public boolean launch() {
        boolean unique;
        try {
            final ServerSocket server = new ServerSocket(port);
            unique = true;
            final Thread portListenerThread = new Thread() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            final Socket socket = server.accept();
                            new Thread() {

                                @Override
                                public void run() {
                                    receive(socket);
                                }
                            }.start();
                        } catch (final IOException e) {
                            Logger.getLogger("UniqueProgInstance").warning("Attente de connexion échouée.");
                        }
                    }
                }
            };
            portListenerThread.setDaemon(true);
            portListenerThread.start();
        } catch (final IOException e) {
            unique = false;
            send();
        }
        return unique;
    }

    public void send() {
        PrintWriter pw = null;
        try {
            final Socket socket = new Socket("localhost", port);
            pw = new PrintWriter(socket.getOutputStream());
            pw.write(message);
        } catch (final IOException e) {
            Logger.getLogger("UniqueProgInstance").warning("Écriture de sortie échoué.");
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public synchronized void launchFiles(final String f) {
        JMacrosTools.busy = true;
        JMacrosTools.removeListeners();
        if (!f.equals("")) {
            if (JMacrosTools.CurrentJZF != null) {
                JMacrosTools.CurrentJZF.toFront();
            }
            final String[] files = f.split(System.getProperty("path.separator"));
            for (final String filename : files) {
                if (JMacrosTools.isStartup) {
                    JMacrosTools.StartupFiles.add(filename);
                } else {
                    if ((filename.endsWith(".mcr"))) {
                        JMacrosTools.OpenMacro(filename);
                    } else {
                        JMacrosTools.OpenFile(filename, null, false);
                    }
                }
            }
            if (!JMacrosTools.isStartup) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        JMacrosTools.RefreshDisplay();
                        JMacrosTools.busy = false;
                        JMacrosTools.addListeners();
                    }
                });
            }
        }
    }

    private synchronized void receive(final Socket socket) {
        Scanner sc = null;
        try {
            socket.setSoTimeout(5000);
            sc = new Scanner(socket.getInputStream());
            final String filename = sc.nextLine();
            launchFiles(filename);
        } catch (final Exception e) {
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }
}
