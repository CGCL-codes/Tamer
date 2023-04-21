package org.mars_sim.msp.core.person.ai.task;

import org.mars_sim.msp.core.Inventory;
import org.mars_sim.msp.core.Simulation;
import org.mars_sim.msp.core.Unit;
import org.mars_sim.msp.core.equipment.Bag;
import org.mars_sim.msp.core.mars.SurfaceFeatures;
import org.mars_sim.msp.core.person.NaturalAttributeManager;
import org.mars_sim.msp.core.person.Person;
import org.mars_sim.msp.core.person.ai.Skill;
import org.mars_sim.msp.core.person.ai.SkillManager;
import org.mars_sim.msp.core.person.ai.mission.Mining;
import org.mars_sim.msp.core.resource.AmountResource;
import org.mars_sim.msp.core.vehicle.Rover;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Task for collecting minerals that have been mined at a site.
 */
public class CollectMinedMinerals extends EVAOperation implements Serializable {

    private static final String COLLECT_MINERALS = "Collecting Minerals";

    private static final double MINERAL_COLLECTION_RATE = 10D;

    private Rover rover;

    protected AmountResource mineralType;

    /**
	 * Constructor
	 * @param person the person performing the task.
	 * @param rover the rover used for the EVA operation.
	 * @param mineralType the type of mineral to collect.
	 * @throws Exception if error creating task.
	 */
    public CollectMinedMinerals(Person person, Rover rover, AmountResource mineralType) {
        super("Collect Minerals", person);
        this.rover = rover;
        this.mineralType = mineralType;
        addPhase(COLLECT_MINERALS);
    }

    /**
	 * Perform the exit rover phase of the task.
	 * @param time the time to perform this phase (in millisols)
	 * @return the time remaining after performing this phase (in millisols)
	 * @throws Exception if error exiting rover.
	 */
    private double exitRover(double time) {
        try {
            time = exitAirlock(time, rover.getAirlock());
            addExperience(time);
        } catch (Exception e) {
            endTask();
        }
        if (exitedAirlock) {
            if (!hasBags()) takeBag();
            if (hasBags()) setPhase(COLLECT_MINERALS); else {
                setPhase(ENTER_AIRLOCK);
            }
        }
        return time;
    }

    /**
	 * Checks if the person is carrying any bags.
	 * @return true if carrying bags.
	 */
    private boolean hasBags() {
        return person.getInventory().containsUnitClass(Bag.class);
    }

    /**
	 * Takes the most full bag from the rover.
	 * @throws Exception if error taking bag.
	 */
    private void takeBag() {
        Bag bag = findMostFullBag(rover.getInventory(), mineralType);
        if (bag != null) {
            if (person.getInventory().canStoreUnit(bag)) {
                rover.getInventory().retrieveUnit(bag);
                person.getInventory().storeUnit(bag);
            }
        }
    }

    /**
	 * Gets the most but not completely full bag of the resource in the rover.
	 * @param inv the inventory to look in.
	 * @param resourceType the resource for capacity.
	 * @return container.
	 */
    private static Bag findMostFullBag(Inventory inv, AmountResource resource) {
        Bag result = null;
        double leastCapacity = Double.MAX_VALUE;
        Iterator<Unit> i = inv.findAllUnitsOfClass(Bag.class).iterator();
        while (i.hasNext()) {
            Bag bag = (Bag) i.next();
            double remainingCapacity = bag.getInventory().getAmountResourceRemainingCapacity(resource, true);
            if ((remainingCapacity > 0D) && (remainingCapacity < leastCapacity)) {
                result = bag;
                leastCapacity = remainingCapacity;
            }
        }
        return result;
    }

    /**
	 * Perform the collect minerals phase of the task.
	 * @param time the time to perform this phase (in millisols)
	 * @return the time remaining after performing this phase (in millisols)
	 * @throws Exception if error collecting minerals.
	 */
    private double collectMinerals(double time) {
        checkForAccident(time);
        if (shouldEndEVAOperation()) {
            setPhase(EVAOperation.ENTER_AIRLOCK);
            return time;
        }
        Mining mission = (Mining) person.getMind().getMission();
        double mineralsExcavated = mission.getMineralExcavationAmount(mineralType);
        double remainingPersonCapacity = person.getInventory().getAmountResourceRemainingCapacity(mineralType, true);
        double mineralsCollected = time * MINERAL_COLLECTION_RATE;
        int areologySkill = person.getMind().getSkillManager().getEffectiveSkillLevel(Skill.AREOLOGY);
        if (areologySkill == 0) mineralsCollected /= 2D;
        if (areologySkill > 1) mineralsCollected += mineralsCollected * (.2D * areologySkill);
        if (mineralsCollected > remainingPersonCapacity) mineralsCollected = remainingPersonCapacity;
        if (mineralsCollected > mineralsExcavated) mineralsCollected = mineralsExcavated;
        addExperience(time);
        person.getInventory().storeAmountResource(mineralType, mineralsCollected, true);
        mission.collectMineral(mineralType, mineralsCollected);
        if (((mineralsExcavated - mineralsCollected) <= 0D) || (mineralsCollected >= remainingPersonCapacity)) setPhase(ENTER_AIRLOCK);
        return 0D;
    }

    /**
	 * Perform the enter rover phase of the task.
	 * @param time the time to perform this phase (in millisols)
	 * @return the time remaining after performing this phase (in millisols)
	 * @throws Exception if error entering rover.
	 */
    private double enterRover(double time) {
        time = enterAirlock(time, rover.getAirlock());
        addExperience(time);
        if (enteredAirlock) {
            Inventory pInv = person.getInventory();
            if (pInv.containsUnitClass(Bag.class)) {
                Iterator<Unit> i = pInv.findAllUnitsOfClass(Bag.class).iterator();
                while (i.hasNext()) {
                    Bag bag = (Bag) i.next();
                    pInv.retrieveUnit(bag);
                    rover.getInventory().storeUnit(bag);
                }
            } else {
                endTask();
                return time;
            }
        }
        return 0D;
    }

    /**
	 * Checks if a person can perform a CollectMinedMinerals task.
	 * @param person the person to perform the task
	 * @param rover the rover the person will EVA from
	 * @param mineralType the resource to collect.
	 * @return true if person can perform the task.
	 */
    public static boolean canCollectMinerals(Person person, Rover rover, AmountResource mineralType) {
        boolean exitable = ExitAirlock.canExitAirlock(person, rover.getAirlock());
        SurfaceFeatures surface = Simulation.instance().getMars().getSurfaceFeatures();
        boolean sunlight = surface.getSurfaceSunlight(rover.getCoordinates()) > 0;
        boolean darkRegion = surface.inDarkPolarRegion(rover.getCoordinates());
        boolean medical = person.getPerformanceRating() < .5D;
        boolean bagsAvailable = (findMostFullBag(rover.getInventory(), mineralType) != null);
        return (exitable && (sunlight || darkRegion) && !medical && bagsAvailable);
    }

    @Override
    protected void addExperience(double time) {
        double evaExperience = time / 100D;
        NaturalAttributeManager nManager = person.getNaturalAttributeManager();
        int experienceAptitude = nManager.getAttribute(NaturalAttributeManager.EXPERIENCE_APTITUDE);
        double experienceAptitudeModifier = (((double) experienceAptitude) - 50D) / 100D;
        evaExperience += evaExperience * experienceAptitudeModifier;
        evaExperience *= getTeachingExperienceModifier();
        person.getMind().getSkillManager().addExperience(Skill.EVA_OPERATIONS, evaExperience);
        if (COLLECT_MINERALS.equals(getPhase())) {
            double areologyExperience = time / 10D;
            areologyExperience += areologyExperience * experienceAptitudeModifier;
            person.getMind().getSkillManager().addExperience(Skill.AREOLOGY, areologyExperience);
        }
    }

    @Override
    public List<String> getAssociatedSkills() {
        List<String> results = new ArrayList<String>(2);
        results.add(Skill.EVA_OPERATIONS);
        results.add(Skill.AREOLOGY);
        return results;
    }

    @Override
    public int getEffectiveSkillLevel() {
        SkillManager manager = person.getMind().getSkillManager();
        int EVAOperationsSkill = manager.getEffectiveSkillLevel(Skill.EVA_OPERATIONS);
        int areologySkill = manager.getEffectiveSkillLevel(Skill.AREOLOGY);
        return (int) Math.round((double) (EVAOperationsSkill + areologySkill) / 2D);
    }

    @Override
    protected double performMappedPhase(double time) {
        if (getPhase() == null) throw new IllegalArgumentException("Task phase is null");
        if (EVAOperation.EXIT_AIRLOCK.equals(getPhase())) return exitRover(time);
        if (COLLECT_MINERALS.equals(getPhase())) return collectMinerals(time);
        if (EVAOperation.ENTER_AIRLOCK.equals(getPhase())) return enterRover(time); else return time;
    }

    @Override
    public void destroy() {
        super.destroy();
        rover = null;
        mineralType = null;
    }
}
