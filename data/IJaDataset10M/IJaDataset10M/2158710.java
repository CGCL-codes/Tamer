package proper.gui.experiment;

import proper.gui.core.frame.ApplicationFrame;
import proper.gui.core.frame.ChildFrame;

/**
* This frame is for Propositionalization with RELAGGS.
*
*
* @author      FracPete
* @version $Revision: 1.2 $
*/
public class Relaggs extends ExperimentFrame {

    /**
   * initializes the object
   */
    public Relaggs(ApplicationFrame parent) {
        super(parent, "RELAGGS");
    }

    /**
   * prints a short description of this class
   */
    public void printDescription() {
        System.out.println("A frontend for Propositionalization with RELAGGS.");
        System.out.println();
    }

    /**
   * starts the application
   */
    public static void main(String[] args) throws Exception {
        ChildFrame frame;
        frame = new Relaggs(null);
        frame.run(args);
    }
}
