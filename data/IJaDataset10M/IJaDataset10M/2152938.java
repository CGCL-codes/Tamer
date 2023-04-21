package alice.c4jadex.gridworld.fabio.Eater;

import alice.c4jadex.bridge.*;
import alice.cartago.*;
import alice.cartago.examples.gridworld.*;
import jadex.runtime.*;

/**
 *    GetReosurcePlan
 * @author Michele Piunti
 */
public class EatFoodPlan extends Plan {

    /** Creates a new instance of GetReosurcePlan */
    public EatFoodPlan() {
    }

    public void body() {
        Food fo = (Food) getParameter("res").getValue();
        log("attempt to eat: " + fo);
        String agName = getAgentName();
        location myLoc = (location) getBeliefbase().getBelief("my_loc").getFact();
        String sensorName = "" + getBeliefbase().getBelief("my_sensor").getFact();
        IGoal eat = createGoal("use");
        IGoal sense = createGoal("sense");
        eat.getParameter("artifact_name").setValue("gridworld");
        eat.getParameter("sensor_name").setValue(sensorName);
        Op op = new Op("eatFood", agName, fo);
        eat.getParameter("op").setValue(op);
        dispatchSubgoalAndWait(eat);
        String filter = agName + "_eaten|" + agName + "_fail";
        sense = createGoal("sense");
        sense.getParameter("sensor_name").setValue(sensorName);
        sense.getParameter("filter").setValue(agName + "_eaten");
        dispatchSubgoalAndWait(sense);
        Perception p = (Perception) sense.getParameter("percept").getValue();
        if (p.getLabel().endsWith("eaten")) {
            location loc = (location) p.getContent(0);
            getBeliefbase().getBeliefSet("visited_loc").addFact(loc);
            double gain = fo.getValue();
            log("EAT FOOD !!! Gained: " + gain + " of life !!!!!!");
            IGoal reset = createGoal("reset");
            dispatchTopLevelGoal(reset);
            getBeliefbase().getBeliefSet("kn_resources").removeFact(fo);
        } else fail();
    }

    private void log(String s) {
        System.out.println("[" + getAgentName() + "] " + s);
    }

    public void fail() {
        log("EAT FAILED!");
        IGoal reset = createGoal("reset");
        dispatchTopLevelGoal(reset);
    }
}
