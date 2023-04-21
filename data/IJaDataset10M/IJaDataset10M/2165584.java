package TestClickeKey;

import engine.Projector;
import engine.lib.Painter;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author keyron
 */
public class TestAnimation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("Try");
        f.setBounds(100, 100, 100, 100);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container r = f.getContentPane();
        JPanel p = new JPanel();
        r.add(p);
        Fileld asd = new Fileld();
        Projector j = new Projector(p, asd, new Painter());
        try {
            j.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        j.setVisible(true);
        f.setVisible(true);
    }
}
