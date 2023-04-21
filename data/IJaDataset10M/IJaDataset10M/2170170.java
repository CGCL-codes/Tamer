package playground.anhorni.locationchoice.preprocess.plans.modifications;

import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.api.experimental.facilities.ActivityFacility;
import org.matsim.core.facilities.ActivityFacilitiesImpl;
import org.matsim.core.facilities.FacilitiesReaderMatsimV1;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.population.ActivityImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.utils.collections.QuadTree;
import org.matsim.locationchoice.utils.QuadTreeRing;
import playground.anhorni.locationchoice.preprocess.facilities.FacilityQuadTreeBuilder;

public class ActivityDifferentiationShop {

    private static final Logger log = Logger.getLogger(ActivityDifferentiationShop.class);

    private final double groceryShare = 0.66;

    private int numberOfShopActs;

    private final ScenarioImpl scenario;

    private ActivityFacilitiesImpl facilitiesActDiff;

    private final String facilitiesActDifffilePath = "input/facilities/facilitiesActDiff.xml.gz";

    public ActivityDifferentiationShop(ScenarioImpl scenario) {
        this.scenario = scenario;
    }

    public void run() {
        this.init();
        this.assignGroceryAndNonGrocery();
        this.assignFacility();
    }

    private void init() {
        this.numberOfShopActs = this.getNumberOfShopActs();
        log.info("reading facilitiesActDiff ...");
        this.facilitiesActDiff = this.scenario.getActivityFacilities();
        new FacilitiesReaderMatsimV1(this.scenario).readFile(facilitiesActDifffilePath);
    }

    private int getNumberOfShopActs() {
        int numberOfShopActs = 0;
        for (Person person : this.scenario.getPopulation().getPersons().values()) {
            if (person.getPlans().size() > 1) {
                log.error("More than one plan for person: " + person.getId());
            }
            PlanImpl selectedPlan = (PlanImpl) person.getSelectedPlan();
            final List<? extends PlanElement> actslegs = selectedPlan.getPlanElements();
            for (int j = 0; j < actslegs.size(); j = j + 2) {
                final ActivityImpl act = (ActivityImpl) actslegs.get(j);
                if (act.getType().startsWith("shop")) {
                    numberOfShopActs++;
                }
            }
        }
        return numberOfShopActs;
    }

    private void assignGroceryAndNonGrocery() {
        int assignedNumberOf_GroceryActs = 0;
        int assignedNumberOf_NonGroceryActs = 0;
        for (Person person : this.scenario.getPopulation().getPersons().values()) {
            if (person.getPlans().size() > 1) {
                log.error("More than one plan for person: " + person.getId());
            }
            PlanImpl selectedPlan = (PlanImpl) person.getSelectedPlan();
            final List<? extends PlanElement> actslegs = selectedPlan.getPlanElements();
            for (int j = 0; j < actslegs.size(); j = j + 2) {
                final ActivityImpl act = (ActivityImpl) actslegs.get(j);
                if (act.getType().startsWith("shop")) {
                    double random = MatsimRandom.getRandom().nextDouble();
                    if (random <= 0.66) {
                        if (assignedNumberOf_GroceryActs < groceryShare * numberOfShopActs) {
                            act.setType("shop_grocery");
                            assignedNumberOf_GroceryActs++;
                        } else {
                            act.setType("shop_nongrocery");
                            assignedNumberOf_NonGroceryActs++;
                        }
                    } else {
                        if (assignedNumberOf_NonGroceryActs < (1.0 - groceryShare) * numberOfShopActs) {
                            act.setType("shop_nongrocery");
                            act.setFacilityId(null);
                            assignedNumberOf_NonGroceryActs++;
                        } else {
                            act.setType("shop_grocery");
                            act.setFacilityId(null);
                            assignedNumberOf_GroceryActs++;
                        }
                    }
                }
            }
        }
        log.info("Number of shopping activities:\t" + this.numberOfShopActs);
        log.info("Number of grocery shopping acts:\t" + assignedNumberOf_GroceryActs);
        log.info("Number of non-grocery acts:\t" + assignedNumberOf_NonGroceryActs);
        log.info("Share:\t" + (100.0 * assignedNumberOf_GroceryActs / this.numberOfShopActs));
    }

    private void assignFacility() {
        NOGATypes nogatypes = new NOGATypes();
        List<ActivityFacility> groceryFacilities = new Vector<ActivityFacility>();
        for (int i = 0; i < nogatypes.shopGrocery.length; i++) {
            groceryFacilities.addAll(this.facilitiesActDiff.getFacilitiesForActivityType(nogatypes.shopGrocery[i]).values());
        }
        List<ActivityFacility> nonGroceryFacilities = new Vector<ActivityFacility>();
        for (int i = 0; i < nogatypes.shopNonGrocery.length; i++) {
            nonGroceryFacilities.addAll(this.facilitiesActDiff.getFacilitiesForActivityType(nogatypes.shopNonGrocery[i]).values());
        }
        FacilityQuadTreeBuilder builder = new FacilityQuadTreeBuilder();
        QuadTree<ActivityFacility> groceryTree = builder.buildFacilityQuadTree("shop_grocery", groceryFacilities);
        QuadTree<ActivityFacility> nongroceryTree = builder.buildFacilityQuadTree("shop_nongrocery", nonGroceryFacilities);
        AssignLocations assignShops = new AssignLocations((QuadTreeRing<ActivityFacility>) nongroceryTree, this.facilitiesActDiff);
        assignShops.run(this.scenario.getPopulation(), "shop_nongrocery");
        assignShops = new AssignLocations((QuadTreeRing<ActivityFacility>) groceryTree, this.facilitiesActDiff);
        assignShops.run(this.scenario.getPopulation(), "shop_grocery");
    }
}
