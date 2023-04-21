package alice.c4jadex.gridworld.fabio.Eater;

import alice.c4jadex.bridge.*;
import alice.cartago.*;
import alice.cartago.examples.gridworld.*;
import jadex.runtime.*;

/**
 *    SenseInfluencerPlan
 * @author Michele Piunti
 */
public class SenseEaterPlan extends Plan {

    /** Creates a new instance of SenseInfluencerPlan */
    public SenseEaterPlan() {
    }

    public void body() {
        location myLoc = (location) getBeliefbase().getBelief("my_loc").getFact();
        String agName = getAgentName();
        String sensorName = "" + getBeliefbase().getBelief("my_sensor").getFact();
        int range_vision = ((Integer) getBeliefbase().getBelief("range_vision").getFact()).intValue();
        IGoal getVision = createGoal("use");
        IGoal sense = createGoal("sense");
        getVision.getParameter("artifact_name").setValue("gridworld");
        getVision.getParameter("sensor_name").setValue(sensorName);
        Op op = new Op("getVision", agName, myLoc, range_vision);
        getVision.getParameter("op").setValue(op);
        sense = createGoal("sense");
        sense.getParameter("sensor_name").setValue(sensorName);
        sense.getParameter("filter").setValue(agName + "_vision");
        sense.getParameter("timeout").setValue(3000);
        dispatchSubgoalAndWait(getVision);
        dispatchSubgoalAndWait(sense);
        Perception p = (Perception) sense.getParameter("percept").getValue();
        Vision V = (Vision) p.getContent(0);
        startAtomic();
        WorldObject[] obsn = V.getObstacles();
        WorldObject[] resn = V.getResources();
        Creature[] crn = V.getCreatures();
        getBeliefbase().getBeliefSet("kn_resources").removeFacts();
        getBeliefbase().getBeliefSet("kn_obstacles").removeFacts();
        if (resn.length > 0) addBel("kn_resources", resn);
        if (obsn.length > 0) addBel("kn_obstacles", obsn);
        endAtomic();
    }

    private void removeBel(String bel, location l, WorldObject[] wos, int range) {
        for (int i = 0; i < wos.length; i++) {
            if (wos[i].getLocation().getDistance(l) <= range) getBeliefbase().getBeliefSet(bel).removeFact(wos[i]);
        }
    }

    private void addBel(String bel, WorldObject[] wos) {
        for (int i = 0; i < wos.length; i++) {
            getBeliefbase().getBeliefSet(bel).addFact(wos[i]);
        }
    }

    private void log(String s) {
        System.out.println("[" + getAgentName() + "] " + s);
    }
}
