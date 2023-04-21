package advancedlommeregner;

import lommeregner.controller.Controller;
import lommeregner.view.View;

/**
 *
 * @author kaspergn
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Controller control = new Controller();
        new View(control).setVisible(true);
    }
}
