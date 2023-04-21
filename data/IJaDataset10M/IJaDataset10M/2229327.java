package alice.c4jadex.gridworld.fabio;

import alice.c4jadex.bridge.*;
import alice.cartago.*;
import alice.cartago.examples.gridworld.location;
import jadex.runtime.*;

/**
 *    observeTarget
 * @author Michele Piunti
 */
public class observeTargetPlan extends Plan {

    String sensorName, agName;

    /** Creates a new instance of observeTarget */
    public observeTargetPlan() {
        agName = getAgentName();
        sensorName = "" + getBeliefbase().getBelief("my_sensor").getFact();
    }

    public void body() {
        String agToObserve = "Eater";
        IGoal sense;
        IGoal observe = createGoal("focus");
        observe.getParameter("artifact_name").setValue("gridworld");
        observe.getParameter("sensor_name").setValue(sensorName);
        dispatchSubgoalAndWait(observe);
        boolean observed = false;
        while (!observed) {
            sense = createGoal("sense");
            sense.getParameter("sensor_name").setValue(sensorName);
            sense.getParameter("filter").setValue("dest_" + agToObserve);
            dispatchSubgoalAndWait(sense);
            Perception p = (Perception) sense.getParameter("percept").getValue();
            location target_o = (location) p.getContent(0);
            if (target_o != null) {
                observed = true;
                location myL = (location) getBeliefbase().getBelief("my_loc").getFact();
                location target_my = new location(target_o.x, myL.y);
                log(" listened eater trg:  " + target_o);
                getBeliefbase().getBelief("target_loc").setFact(target_my);
            }
        }
    }

    private void log(String s) {
        System.out.println("[" + getAgentName() + "] " + s);
    }
}
