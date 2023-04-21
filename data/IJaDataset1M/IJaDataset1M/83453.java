package toolers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import toolers.Core.DbResultPanel;

/**
 *
 * @author Praca
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());
        DbResultPanel mainFrame = injector.getInstance(DbResultPanel.class);
        JFrame frame = new JFrame("Tytuł");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.add((JPanel) mainFrame);
        frame.setVisible(true);
    }
}
