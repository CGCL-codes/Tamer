package seevolution;

import java.io.File;
import seevolution.gui.SeevolutionView;

/**
 * The class used to load the application, creates a Seevolution object and starts the execution.<br>
 * In order to adhere to the new stardards and make use of javax.swing.SwingUtilities.invokeLater(Runnable r)
 * to start the application, the class itself is Runnable. This was necessary because otherwise the contents
 * of the inner class were not modifiable, and the prefs file could not be set via the command line.
 *
 * @author Andres Esteban Marcos
 * @version 1.0
 */
public class Seevolution implements Runnable {

    private String prefsFile, inputFile;

    /**
	 * Sole constructor, called from main
	 * @param prefsFile The name of the file that stores the prefs in XML format
	 */
    private Seevolution(String prefsFile, String inputFile) {
        this.prefsFile = prefsFile;
        this.inputFile = inputFile;
    }

    /**
	 * The initial method, execution starts here.
	 * @param args Accepts the name of the prefs file as an optional command line argument
	 */
    public static void main(String args[]) {
        String prefsFile = "evol.prefs";
        String inputFile = null;
        System.out.println("There are " + args.length + " args");
        if (args.length > 0 && args[0].equals("-open")) {
            inputFile = args[1];
        } else {
            for (int i = 0; i < args.length; i++) {
                System.out.println("arg " + i + " is " + args[i]);
                String parts[] = args[i].split("=");
                if (parts.length == 2) {
                    String name = parts[0];
                    String value = parts[1];
                    if (name.equals("-p") || name.equals("-prefs")) prefsFile = value;
                } else if (parts.length == 1) inputFile = args[i];
            }
        }
        javax.swing.SwingUtilities.invokeLater(new Seevolution(prefsFile, inputFile));
    }

    /**
	 * Required by interface Runnable.<br>
	 * Starts up the GUI.
	 */
    public void run() {
        try {
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
        }
        SeevolutionView view = new SeevolutionView(prefsFile);
        if (inputFile != null) view.setMutationsFile(inputFile);
    }
}
