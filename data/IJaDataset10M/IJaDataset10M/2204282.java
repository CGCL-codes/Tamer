package geopms;

import java.io.*;
import java.util.logging.*;

/**
 *
 * @author Marcus Landschulze
 */
public class Main {

    private static Logger logger = Logger.getLogger("com.geores.geopms");

    /** Creates a new instance of Main */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-log")) {
                System.out.println("Start logging! See gds.log.xml in your main directory.");
                try {
                    Handler hd = new FileHandler("%h/gds.log.xml", 0, 1);
                    logger.addHandler(hd);
                } catch (IOException e) {
                    logger.severe("Can't create Log-Filehandler :" + e.getMessage());
                }
            }
        } else {
            logger.setLevel(Level.OFF);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new GeoPMSFrame().setVisible(true);
            }
        });
    }
}
