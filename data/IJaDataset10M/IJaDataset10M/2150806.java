package desview.application;

import desview.scheduler.Reader;
import desview.util.Message;
import desview.view.MainWindow;
import desview.view.components.Login;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jdesktop.swingx.JXFrame;

/**
 * Desview start application class.
 * @author Diones Rossetto.
 * @author Luiz Mello.
 * @version 1.0
 * @since 11/04/2010.
 * <br>
 * Version 1.0 Built in July, 10, 2010.
 */
public class Application {

    private static boolean beta = false;

    private static boolean login = true;

    private static boolean closeWindow = false;

    /**
     * <i>Default</i> contructor.
     */
    private Application() {
    }

    /**
     * Main method of the application.
     * @param args the command line arguments.
     */
    public static void main(String... args) {
        if (!login) {
            open();
        } else {
            login();
        }
    }

    private static void open() {
        try {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    MainWindow janela = new MainWindow();
                    janela.setVisible(true);
                    janela.pack();
                    if (!closeWindow) {
                        janela.setDefaultCloseOperation(JXFrame.DO_NOTHING_ON_CLOSE);
                        janela.addWindowListener(new WindowAdapter() {

                            @Override
                            public void windowClosing(WindowEvent e) {
                                if (Message.question(null, "Exit", "Want to close and exit?")) {
                                    System.exit(0);
                                }
                            }
                        });
                    } else {
                        janela.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
                    }
                }
            });
            Reader schedulerThread = new Reader();
            schedulerThread.start();
        } catch (Exception ex) {
        }
    }

    private static void login() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Login l = new Login(beta);
                l.setVisible(true);
                l.pack();
            }
        });
    }
}
