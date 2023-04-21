package scrabblehelper;

import gui.ErrorWindow;
import gui.LoadingWindow;
import gui.ScrabbleWindow;
import gui.mainwindow.MainWindow;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import scrabbletools.IntHashDictionary;

/**
 *
 * @author Nick
 */
public class Startup {

    public static final String DICTIONARIES_FOLDER = "dictionaries";

    public Startup() {
    }

    public static void startUp() {
        try {
            loadResources();
        } catch (Exception ex) {
            new ErrorWindow(ex);
        }
        try {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new MainWindow().setVisible(true);
                }
            });
        } catch (Exception ex) {
            new ErrorWindow(ex);
        }
    }

    public static void loadResources() {
        try {
            try {
                URL url = ClassLoader.getSystemClassLoader().getResource("dictionaries/TWL06.txt");
                LoadingWindow lw = new LoadingWindow();
                lw.setVisible(true);
                IntHashDictionary d = new IntHashDictionary(url);
                lw.dispose();
                StaticFields.setDictionary(d);
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                new ErrorWindow(sw.toString());
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            new ErrorWindow(sw.toString());
        }
    }
}
