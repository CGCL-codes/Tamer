package alice.c4jadex.examples;

import alice.c4jadex.bridge.*;
import alice.cartago.*;
import jadex.runtime.*;

/**
 *    helloPlan
 * @author Michele Piunti
 */
public class helloPlan extends Plan {

    /** Creates a new instance of helloPlan */
    public helloPlan() {
    }

    public void body() {
        try {
            String aName = "console";
            IGoal lookup = createGoal("lookup");
            lookup.getParameter("artifact_name").setValue(aName);
            dispatchSubgoalAndWait(lookup);
            ArtifactId aid = (ArtifactId) lookup.getParameter("aid").getValue();
            if (aid != null) System.out.println("lookup succeed: retrieved arifact" + aName + " aId: " + aid);
            IGoal use = createGoal("use");
            use.getParameter("artifact_name").setValue("console");
            Op op = new Op("println", "ciao, this message is created by: " + getAgentName());
            use.getParameter("op").setValue(op);
            String sensorName = getAgentName() + "Sensor";
            dispatchSubgoalAndWait(use);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
