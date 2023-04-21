package proper.gui.experiment;

import proper.gui.core.frame.ApplicationFrame;
import proper.gui.core.frame.ChildFrame;

/**
* This frame is for Propositionalization with REMILK.
*
*
* @author      FracPete
* @version $Revision: 1.2 $
*/
public class ReMilk extends ExperimentFrame {

    /**
   * initializes the object
   */
    public ReMilk(ApplicationFrame parent) {
        super(parent, "REMILK");
    }

    /**
   * prints a short description of this class
   */
    public void printDescription() {
        System.out.println("A frontend for Propositionalization with REMILK - a combination of RELAGGS");
        System.out.println("and MILK.");
        System.out.println();
    }

    /**
   * starts the application
   */
    public static void main(String[] args) throws Exception {
        ChildFrame frame;
        frame = new ReMilk(null);
        frame.run(args);
    }
}
