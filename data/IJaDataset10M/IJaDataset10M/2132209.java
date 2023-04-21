package playground.anhorni.secloc;

import java.util.ArrayList;
import org.matsim.plans.Act;
import org.matsim.plans.Leg;
import org.matsim.plans.Plan;
import org.matsim.plans.Knowledge;
import org.matsim.basic.v01.BasicLink;
import org.matsim.facilities.Activity;
import org.matsim.facilities.Facility;
import org.matsim.gbl.Gbl;

public class RandomLocationSelector implements LocationSelectorI {

    private final int nbrChanges = 1;

    private final String[] types2change = { "leisure" };

    public RandomLocationSelector() {
    }

    public void setLocations(Plan plan) {
        for (int i = 0; i < types2change.length; i++) {
            Knowledge knowledge = plan.getPerson().getKnowledge();
            ArrayList<Activity> fs_array = knowledge.getActivities(types2change[i]);
            for (int j = 0; j < this.nbrChanges; j++) {
                Facility f = fs_array.get(Gbl.random.nextInt(fs_array.size())).getFacility();
                BasicLink linkExchange = (BasicLink) f.getLink();
                System.out.println("link_id" + linkExchange.getId());
                exchangeLink(types2change[i], linkExchange, plan);
            }
        }
    }

    public void exchangeLink(String type, BasicLink link, Plan plan) {
        ArrayList<?> actslegs = plan.getActsLegs();
        for (int j = 0; j < actslegs.size(); j = j + 2) {
            Act act = (Act) actslegs.get(j);
            if (act.getType().equals(type)) {
                act.setLink(link);
            }
        }
        for (int j = 1; j < actslegs.size(); j = j + 2) {
            Leg leg = (Leg) actslegs.get(j);
            leg.setRoute(null);
        }
    }
}
