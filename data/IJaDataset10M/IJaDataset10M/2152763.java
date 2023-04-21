package calchelper.gui;

import javax.swing.JFrame;

/**
 * 
 * 
 * @author Ben Decato
 * @author Patrick MacArthur
 */
public class CalcApp {

    public static final String VERSION = "svn-trunk";

    public static final String APP_NAME = "CalcHelper";

    public static void main(String[] args) {
        JFrame frame = new JFrame(APP_NAME + " " + VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalcGUI _calcGUI = new CalcGUI(frame);
        frame.add(_calcGUI);
        frame.pack();
        frame.setVisible(true);
    }
}
