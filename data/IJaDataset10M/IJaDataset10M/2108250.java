package playground.christoph.evacuation.vehicles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.events.PersonEntersVehicleEvent;
import org.matsim.core.events.PersonLeavesVehicleEvent;
import org.matsim.core.events.handler.PersonEntersVehicleEventHandler;
import org.matsim.core.events.handler.PersonLeavesVehicleEventHandler;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.households.Household;
import org.matsim.households.Households;

/**
 * Checks how many vehicles are used by one household at a time and creates a warn
 * message if the number of available vehicles is exceeded.
 * 
 * @author cdobler
 */
public class HouseholdVehiclesTracker implements PersonEntersVehicleEventHandler, PersonLeavesVehicleEventHandler {

    private static final Logger log = Logger.getLogger(HouseholdVehiclesTracker.class);

    private final Scenario scenario;

    private final Households households;

    private final Map<Id, Id> personHousehold;

    private final Map<Id, HouseholdVehiclesInfo> householdVehicles;

    private final Map<Id, AtomicInteger> activeHouseHoldVehicles;

    private final Map<Id, AtomicInteger> vehicleUsage;

    private final Set<Id> activeVehicles;

    private final Set<Id> exceededHouseholds;

    public HouseholdVehiclesTracker(Scenario scenario, Map<Id, HouseholdVehiclesInfo> householdVehicles) {
        this.scenario = scenario;
        this.householdVehicles = householdVehicles;
        this.households = ((ScenarioImpl) scenario).getHouseholds();
        personHousehold = new HashMap<Id, Id>();
        vehicleUsage = new HashMap<Id, AtomicInteger>();
        activeHouseHoldVehicles = new HashMap<Id, AtomicInteger>();
        activeVehicles = new HashSet<Id>();
        exceededHouseholds = new HashSet<Id>();
    }

    public void printClosingStatistics() {
        log.info("Number of household that used more vehicles than available: " + exceededHouseholds.size());
    }

    @Override
    public void handleEvent(PersonLeavesVehicleEvent event) {
        int usage = vehicleUsage.get(event.getVehicleId()).decrementAndGet();
        if (usage == 0) {
            activeVehicles.remove(event.getVehicleId());
            activeHouseHoldVehicles.get(personHousehold.get(event.getPersonId())).decrementAndGet();
        }
    }

    @Override
    public void handleEvent(PersonEntersVehicleEvent event) {
        int usage = vehicleUsage.get(event.getVehicleId()).incrementAndGet();
        if (usage == 1) {
            activeVehicles.add(event.getVehicleId());
            Id householdId = personHousehold.get(event.getPersonId());
            int householdActive = activeHouseHoldVehicles.get(householdId).incrementAndGet();
            int maxVehicles = householdVehicles.get(householdId).getNumVehicles();
            if (householdActive > maxVehicles) {
                exceededHouseholds.add(householdId);
                log.warn("Too many vehicles active in household " + householdId.toString() + ". " + householdActive + " (active) vs " + maxVehicles + " (max).");
            }
        }
    }

    @Override
    public void reset(int iteration) {
        vehicleUsage.clear();
        activeVehicles.clear();
        personHousehold.clear();
        exceededHouseholds.clear();
        activeHouseHoldVehicles.clear();
        for (Household household : households.getHouseholds().values()) {
            for (Id personId : household.getMemberIds()) personHousehold.put(personId, household.getId());
            activeHouseHoldVehicles.put(household.getId(), new AtomicInteger(0));
        }
        for (Person person : scenario.getPopulation().getPersons().values()) {
            vehicleUsage.put(person.getId(), new AtomicInteger(0));
        }
    }
}
