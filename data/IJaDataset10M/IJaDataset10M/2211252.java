package timba;

import timba.view.MainJFrame;
import timba.util.Debug;
import timba.util.LookAndFeelChooser;

/**
 * Open Aplication
 * @author JuanBC - JMGuzman
 * @version 1.0
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main.argsProc(args);
        try {
            LookAndFeelChooser.setSystemLookAndFeel(LookAndFeelChooser.METAL_LNF, LookAndFeelChooser.GTK_LNF);
        } catch (Exception e) {
            String error = "Failure in charge system Look and Feel";
            Debug.err(error);
        }
        MainJFrame.open();
    }

    private static void argsProc(String[] args) {
        boolean b = false;
        for (int i = 0; i < args.length; i++) {
            String str = args[i].trim().toLowerCase();
            if (str.equals("-d")) b = true;
        }
        Debug.setEnabled(b);
    }
}
