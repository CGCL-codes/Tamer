package alice.c4jadex.gridworld.fabio.Influencer;

import alice.cartago.Tuple;
import alice.cartago.examples.gridworld.location;
import jadex.runtime.IEvent;
import jadex.runtime.Plan;

public class ChoseTargetPlan extends Plan {

    @Override
    public void body() {
        IEvent event = getInitialEvent();
        String label = event.getParameter("label").getValue().toString();
        Tuple tuple = (Tuple) event.getParameter("content").getValue();
        Object X = tuple.getContent(0);
        int locX = Integer.parseInt(X.toString());
        int locY = ((location) getBeliefbase().getBelief("my_loc").getFact()).getY();
        location new_target_my = new location(locX, locY);
        location new_target_eat = new location(locX, locY);
        startAtomic();
        getBeliefbase().getBelief("my_target_loc").setFact(new_target_my);
        getBeliefbase().getBelief("eat_target_loc").setFact(new_target_eat);
        endAtomic();
        log(" perceived event: " + label + " - Eater's target is: " + X + " my new TARGET: " + new_target_my);
    }

    private void log(String s) {
        System.out.println("[" + getAgentName() + "] " + s);
    }
}
