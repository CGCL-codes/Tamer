package smoothmetal.demo;

import smooth.util.SmoothUtilities;
import smooth.SmoothLookAndFeelFactory;
import smooth.demo.Main;
import smooth.basic.SmoothTitledBorder;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Locale;

/**
 * Test class for the Smooth Metal look. This basically is a frame with
 * a lot of different non-functional GUI components inside.
 *
 * <ul>
 *  <li>0.4 - moved to smooth.demo.Main</li>
 * </ul>
 *  
 * @author Marcel Offermans
 * @author James Shiell
 * @version 1.1
 *
 * @deprecated use smooth.demo.Main
 */
public class DemoFrame {

    public static void main(String[] args) {
        new Main().start();
    }
}
