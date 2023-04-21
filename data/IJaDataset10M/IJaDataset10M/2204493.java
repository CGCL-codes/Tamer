package dk.fuddi;

import javax.swing.*;
import java.awt.*;

/**
 * User: jen
 * Date: May 25, 2007
 * Time: 11:49:57 PM
 */
public class EnDecodeMain extends JFrame {

    public static Dimension dimension = new Dimension(800, 600);

    public EnDecodeMain() throws HeadlessException {
        super("Developer tool");
        add(new ConversionPanel(dimension));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(dimension);
        setVisible(true);
    }

    public static void main(String[] args) {
        new dk.fuddi.EnDecodeMain();
    }
}
