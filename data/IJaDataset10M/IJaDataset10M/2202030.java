package org.matsim.socialnetworks.replanning;

import java.util.ArrayList;
import java.util.List;
import org.matsim.basic.v01.BasicPlanImpl.ActIterator;
import org.matsim.gbl.Gbl;
import org.matsim.gbl.MatsimRandom;
import org.matsim.interfaces.basic.v01.population.BasicPlanElement;
import org.matsim.interfaces.core.v01.Activity;
import org.matsim.interfaces.core.v01.ActivityOption;
import org.matsim.interfaces.core.v01.Facility;
import org.matsim.interfaces.core.v01.Leg;
import org.matsim.interfaces.core.v01.Person;
import org.matsim.interfaces.core.v01.Plan;
import org.matsim.network.NetworkLayer;
import org.matsim.population.Knowledge;
import org.matsim.population.algorithms.PersonPrepareForSim;
import org.matsim.population.algorithms.PlanAlgorithm;
import org.matsim.router.PlansCalcRoute;
import org.matsim.router.util.TravelCost;
import org.matsim.router.util.TravelTime;

/**
 * Sample replanning strategy to change activity location:
 * uses agent knowledge
 *  
 * @author jhackney
 *
 */
public class RandomChangeLocationK implements PlanAlgorithm {

    private final String weights;

    private double[] cum_p_factype;

    private NetworkLayer network;

    private TravelCost tcost;

    private TravelTime ttime;

    private String[] factypes;

    public RandomChangeLocationK(String[] factypes, NetworkLayer network, TravelCost tcost, TravelTime ttime) {
        weights = Gbl.getConfig().socnetmodule().getSWeights();
        cum_p_factype = getCumFacWeights(weights);
        this.network = network;
        this.tcost = tcost;
        this.ttime = ttime;
        this.factypes = factypes;
    }

    public void run(Plan plan) {
        replaceRandomFacility(plan);
    }

    private void replaceRandomFacility(Plan plan) {
        String factype = null;
        Person person = plan.getPerson();
        Plan newPlan = person.copySelectedPlan();
        boolean changed = false;
        double rand = MatsimRandom.random.nextDouble();
        if (rand < cum_p_factype[0]) {
            factype = factypes[0];
        } else if (cum_p_factype[0] <= rand && rand < cum_p_factype[1]) {
            factype = factypes[1];
        } else if (cum_p_factype[1] <= rand && rand < cum_p_factype[2]) {
            factype = factypes[2];
        } else if (cum_p_factype[2] <= rand && rand < cum_p_factype[3]) {
            factype = factypes[3];
        } else {
            factype = factypes[4];
        }
        ActIterator planIter = newPlan.getIteratorAct();
        ArrayList<Activity> actsOfFacType = new ArrayList<Activity>();
        while (planIter.hasNext()) {
            Activity nextAct = (Activity) planIter.next();
            if (nextAct.getType() == factype) {
                actsOfFacType.add(nextAct);
            }
        }
        if (actsOfFacType.size() < 1) {
            person.setSelectedPlan(plan);
            person.getPlans().remove(newPlan);
            return;
        } else {
            Activity newAct = (Activity) (actsOfFacType.get(MatsimRandom.random.nextInt(actsOfFacType.size())));
            Knowledge k = person.getKnowledge();
            List<ActivityOption> actList = k.getActivities(factype);
            if (actList.size() > 0) {
                Facility fFromKnowledge = actList.get(MatsimRandom.random.nextInt(actList.size())).getFacility();
                if (newAct.getLinkId() != fFromKnowledge.getLink().getId()) {
                    if (newAct.getType() == plan.getFirstActivity().getType() && newAct.getLink() == plan.getFirstActivity().getLink()) {
                        Activity lastAct = (Activity) newPlan.getPlanElements().get(newPlan.getPlanElements().size() - 1);
                        lastAct.setLink(fFromKnowledge.getLink());
                        lastAct.setCoord(fFromKnowledge.getCoord());
                        lastAct.setFacility(fFromKnowledge);
                    }
                    if (newAct.getType() == ((Activity) plan.getPlanElements().get(plan.getPlanElements().size() - 1)).getType() && newAct.getLink() == ((Activity) plan.getPlanElements().get(plan.getPlanElements().size() - 1)).getLink()) {
                        Activity firstAct = (Activity) newPlan.getFirstActivity();
                        firstAct.setLink(fFromKnowledge.getLink());
                        firstAct.setCoord(fFromKnowledge.getCoord());
                        firstAct.setFacility(fFromKnowledge);
                    }
                    newAct.setLink(fFromKnowledge.getLink());
                    newAct.setCoord(fFromKnowledge.getCoord());
                    newAct.setFacility(fFromKnowledge);
                    changed = true;
                }
            }
            if (changed) {
                List<? extends BasicPlanElement> bestactslegs = newPlan.getPlanElements();
                for (int j = 1; j < bestactslegs.size(); j = j + 2) {
                    Leg leg = (Leg) bestactslegs.get(j);
                    leg.setRoute(null);
                }
                newPlan.setScore(Plan.UNDEF_SCORE);
                new PersonPrepareForSim(new PlansCalcRoute(network, tcost, ttime), network).run(newPlan.getPerson());
                person.setSelectedPlan(newPlan);
            } else {
                person.getPlans().remove(newPlan);
                person.setSelectedPlan(plan);
            }
        }
    }

    private double[] getCumFacWeights(String longString) {
        String patternStr = ",";
        String[] s;
        s = longString.split(patternStr);
        double[] w = new double[s.length];
        w[0] = Double.parseDouble(s[0]);
        double sum = w[0];
        for (int i = 1; i < s.length; i++) {
            w[i] = Double.parseDouble(s[i]) + w[i - 1];
            sum = sum + Double.parseDouble(s[i]);
        }
        if (sum > 0) {
            for (int i = 0; i < s.length; i++) {
                w[i] = w[i] / sum;
            }
        } else if (sum < 0) {
            Gbl.errorMsg("At least one weight for the type of information exchange or meeting place must be > 0, check config file.");
        }
        return w;
    }
}
