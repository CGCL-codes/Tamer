package org.mars_sim.msp.core.structure;

import org.mars_sim.msp.core.*;
import org.mars_sim.msp.core.equipment.Equipment;
import org.mars_sim.msp.core.equipment.EquipmentFactory;
import org.mars_sim.msp.core.events.HistoricalEvent;
import org.mars_sim.msp.core.person.Person;
import org.mars_sim.msp.core.person.PersonConfig;
import org.mars_sim.msp.core.person.ai.social.RelationshipManager;
import org.mars_sim.msp.core.resource.AmountResource;
import org.mars_sim.msp.core.resource.Part;
import org.mars_sim.msp.core.structure.building.Building;
import org.mars_sim.msp.core.structure.building.BuildingConfig;
import org.mars_sim.msp.core.structure.building.BuildingManager;
import org.mars_sim.msp.core.structure.building.function.LifeSupport;
import org.mars_sim.msp.core.time.MarsClock;
import org.mars_sim.msp.core.vehicle.Rover;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Resupply mission from Earth for a settlement.
 */
public class Resupply implements Serializable {

    private String resupplyName;

    private Settlement settlement;

    private MarsClock arrivalDate;

    private boolean isDelivered;

    private List<BuildingTemplate> newBuildings;

    private List<String> newVehicles;

    private Map<String, Integer> newEquipment;

    private int newImmigrantNum;

    private Map<AmountResource, Double> newResources;

    private Map<Part, Integer> newParts;

    /**
	 * Constructor
	 * @param arrivalDate the arrival date of the supplies. 
	 * @param resupplyName the name of the resupply mission.
	 * @param settlement the settlement receiving the supplies.
	 */
    Resupply(MarsClock arrivalDate, String resupplyName, Settlement settlement) {
        this.arrivalDate = arrivalDate;
        this.resupplyName = resupplyName;
        this.settlement = settlement;
        isDelivered = false;
        ResupplyConfig config = SimulationConfig.instance().getResupplyConfiguration();
        newBuildings = config.getResupplyBuildings(resupplyName);
        newVehicles = config.getResupplyVehicleTypes(resupplyName);
        newEquipment = config.getResupplyEquipment(resupplyName);
        newImmigrantNum = config.getNumberOfResupplyImmigrants(resupplyName);
        newResources = config.getResupplyResources(resupplyName);
        newParts = config.getResupplyParts(resupplyName);
    }

    /**
	 * Gets the arrival date of the resupply mission.
	 * @return arrival date as MarsClock instance.
	 */
    public MarsClock getArrivalDate() {
        return (MarsClock) arrivalDate.clone();
    }

    /**
	 * Gets the name of the resupply mission.
	 * @return name
	 */
    public String getResupplyName() {
        return resupplyName;
    }

    /**
	 * Checks if the supplies have been delivered to the settlement.
	 * @return true if delivered
	 */
    public boolean isDelivered() {
        return isDelivered;
    }

    /**
	 * Delivers supplies to the settlement.
	 * @throws Exception if problem delivering supplies.
	 */
    void deliverSupplies() {
        BuildingManager buildingManager = settlement.getBuildingManager();
        Iterator<BuildingTemplate> buildingI = newBuildings.iterator();
        while (buildingI.hasNext()) {
            BuildingTemplate template = buildingI.next();
            BuildingConfig buildingConfig = SimulationConfig.instance().getBuildingConfiguration();
            double width = buildingConfig.getWidth(template.getType());
            double length = buildingConfig.getLength(template.getType());
            if (buildingManager.checkIfNewBuildingLocationOpen(template.getXLoc(), template.getYLoc(), width, length, template.getFacing())) {
                buildingManager.addBuilding(template);
            } else {
                BuildingTemplate positionedTemplate = positionNewResupplyBuilding(template);
                buildingManager.addBuilding(positionedTemplate);
            }
        }
        UnitManager unitManager = Simulation.instance().getUnitManager();
        Iterator<String> vehicleI = newVehicles.iterator();
        while (vehicleI.hasNext()) {
            String vehicleType = vehicleI.next();
            String vehicleName = unitManager.getNewName(UnitManager.VEHICLE, null, null);
            Rover rover = new Rover(vehicleName, vehicleType, settlement);
            unitManager.addUnit(rover);
        }
        Inventory inv = settlement.getInventory();
        Iterator<String> equipmentI = newEquipment.keySet().iterator();
        while (equipmentI.hasNext()) {
            String equipmentType = equipmentI.next();
            int number = newEquipment.get(equipmentType);
            for (int x = 0; x < number; x++) {
                Equipment equipment = EquipmentFactory.getEquipment(equipmentType, settlement.getCoordinates(), false);
                equipment.setName(unitManager.getNewName(UnitManager.EQUIPMENT, equipmentType, null));
                inv.storeUnit(equipment);
            }
        }
        Iterator<AmountResource> resourcesI = newResources.keySet().iterator();
        while (resourcesI.hasNext()) {
            AmountResource resource = resourcesI.next();
            double amount = newResources.get(resource);
            double capacity = inv.getAmountResourceRemainingCapacity(resource, true);
            if (amount > capacity) amount = capacity;
            inv.storeAmountResource(resource, amount, true);
        }
        Iterator<Part> partsI = newParts.keySet().iterator();
        while (partsI.hasNext()) {
            Part part = partsI.next();
            int number = newParts.get(part);
            inv.storeItemResources(part, number);
        }
        Collection<Person> immigrants = new ConcurrentLinkedQueue<Person>();
        RelationshipManager relationshipManager = Simulation.instance().getRelationshipManager();
        for (int x = 0; x < newImmigrantNum; x++) {
            PersonConfig personConfig = SimulationConfig.instance().getPersonConfiguration();
            String gender = Person.FEMALE;
            if (RandomUtil.getRandomDouble(1.0D) <= personConfig.getGenderRatio()) gender = Person.MALE;
            Person immigrant = new Person(unitManager.getNewName(UnitManager.PERSON, null, gender), gender, settlement);
            unitManager.addUnit(immigrant);
            relationshipManager.addNewImmigrant(immigrant, immigrants);
            immigrants.add(immigrant);
        }
        HistoricalEvent newEvent = new ResupplyEvent(settlement, resupplyName);
        Simulation.instance().getEventManager().registerNewEvent(newEvent);
        isDelivered = true;
    }

    /**
     * Determines and sets the position of a new resupply building.
     * @param template the building template.
     * @return the repositioned building template.
     */
    private BuildingTemplate positionNewResupplyBuilding(BuildingTemplate template) {
        BuildingTemplate newPosition = null;
        boolean hasLifeSupport = SimulationConfig.instance().getBuildingConfiguration().hasLifeSupport(template.getType());
        if (hasLifeSupport) {
            List<Building> inhabitableBuildings = settlement.getBuildingManager().getBuildings(LifeSupport.NAME);
            Collections.shuffle(inhabitableBuildings);
            Iterator<Building> i = inhabitableBuildings.iterator();
            while (i.hasNext()) {
                newPosition = positionNextToBuilding(template, i.next(), 0D);
                if (newPosition != null) break;
            }
        } else {
            List<Building> sameBuildings = settlement.getBuildingManager().getBuildingsOfName(template.getType());
            Collections.shuffle(sameBuildings);
            Iterator<Building> j = sameBuildings.iterator();
            while (j.hasNext()) {
                newPosition = positionNextToBuilding(template, j.next(), 0D);
                if (newPosition != null) break;
            }
        }
        if (newPosition == null) {
            BuildingManager buildingManager = settlement.getBuildingManager();
            if (buildingManager.getBuildingNum() > 0) {
                for (int x = 10; newPosition == null; x += 10) {
                    List<Building> allBuildings = buildingManager.getBuildings();
                    Collections.shuffle(allBuildings);
                    Iterator<Building> i = allBuildings.iterator();
                    while (i.hasNext()) {
                        newPosition = positionNextToBuilding(template, i.next(), (double) x);
                        if (newPosition != null) break;
                    }
                }
            } else {
                newPosition = new BuildingTemplate(template.getType(), 0D, 0D, RandomUtil.getRandomDouble(360D));
            }
        }
        return newPosition;
    }

    /**
     * Positions a new construction site near an existing building.
     * @param template the new building template.
     * @param building the existing building.
     * @param separationDistance the separation distance (meters) from the building.
     * @return new building template with determined position, or null if none found.
     */
    private BuildingTemplate positionNextToBuilding(BuildingTemplate template, Building building, double separationDistance) {
        BuildingTemplate newPosition = null;
        double width = SimulationConfig.instance().getBuildingConfiguration().getWidth(template.getType());
        double length = SimulationConfig.instance().getBuildingConfiguration().getLength(template.getType());
        final int front = 0;
        final int back = 1;
        final int right = 2;
        final int left = 3;
        List<Integer> directions = new ArrayList<Integer>(4);
        directions.add(front);
        directions.add(back);
        directions.add(right);
        directions.add(left);
        Collections.shuffle(directions);
        double direction = 0D;
        double structureDistance = 0D;
        for (int x = 0; x < directions.size(); x++) {
            switch(directions.get(x)) {
                case front:
                    direction = building.getFacing();
                    structureDistance = (building.getLength() / 2D) + (length / 2D);
                    break;
                case back:
                    direction = building.getFacing() + 180D;
                    structureDistance = (building.getLength() / 2D) + (length / 2D);
                    break;
                case right:
                    direction = building.getFacing() + 90D;
                    structureDistance = (building.getWidth() / 2D) + (width / 2D);
                    break;
                case left:
                    direction = building.getFacing() + 270D;
                    structureDistance = (building.getWidth() / 2D) + (width / 2D);
            }
            double distance = structureDistance + separationDistance;
            double radianDirection = Math.PI * direction / 180D;
            double rectCenterX = building.getXLocation() + (distance * Math.sin(radianDirection));
            double rectCenterY = building.getYLocation() + (distance * Math.cos(radianDirection));
            double rectRotation = building.getFacing();
            if (settlement.getBuildingManager().checkIfNewBuildingLocationOpen(rectCenterX, rectCenterY, width, length, rectRotation)) {
                newPosition = new BuildingTemplate(template.getType(), rectCenterX, rectCenterY, building.getFacing());
                break;
            }
        }
        return newPosition;
    }

    /**
     * Prepare object for garbage collection.
     */
    public void destroy() {
        resupplyName = null;
        settlement = null;
        arrivalDate = null;
        newBuildings.clear();
        newBuildings = null;
        newVehicles.clear();
        newVehicles = null;
        newEquipment.clear();
        newEquipment = null;
        newResources.clear();
        newResources = null;
        newParts.clear();
        newParts = null;
    }
}
