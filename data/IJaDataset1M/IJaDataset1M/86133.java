package net.slashie.expedition.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.slashie.expedition.action.BuildBuildings;
import net.slashie.expedition.action.Hibernate;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.item.ItemFactory;
import net.slashie.expedition.item.StorageType;
import net.slashie.expedition.town.Building;
import net.slashie.expedition.town.BuildingFactory;
import net.slashie.expedition.town.BuildingTeam;
import net.slashie.expedition.town.Farm;
import net.slashie.expedition.ui.ExpeditionUserInterface;
import net.slashie.expedition.world.ExpeditionMacroLevel;
import net.slashie.expedition.world.FoodConsumerDelegate;
import net.slashie.expedition.world.Forest;
import net.slashie.expedition.world.OverworldExpeditionCell;
import net.slashie.expedition.world.agents.DayShiftAgent;
import net.slashie.lang.Percentage;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.baseDomain.AbstractItem;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.ui.ActionCancelException;
import net.slashie.serf.ui.UserInterface;
import net.slashie.util.Pair;
import net.slashie.utils.OutParameter;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

/**
 * Represents a settlement founded by the Expedition.
 * 
 * The base inventory of the GoodsCache represents units and goods
 * placed temporary by the expedition on the town, as well as goods
 * available for the expeditionary to transfer into self.
 * 
 * @author Slash
 *
 */
@SuppressWarnings("serial")
public class Town extends GoodsCache implements BuildingTeam {

    private static final String[] TOWN_ACTIONS = new String[] { "Transfer equipment and people", "Construct building on settlement", "Inhabit settlement", "Pass through the settlement", "Do nothing" };

    private String name;

    protected Expedition founderExpedition;

    protected Date foundedIn;

    /**
	 * Represents the accumulated surplus from resource gathering and 
	 * production
	 */
    private int economicWelfare;

    /**
	 * Represents the buildings constructed on the settlement
	 */
    private List<Building> buildings = new ArrayList<Building>();

    /**
	 * Represents how much can the founding expedition use the local
	 * resources and fetch the town production
	 * 
	 * Influences lodging available for temporary expedition units
	 * and the quantity of goods placed on the fetchable inventory
	 * over the production phase.
	 */
    private Percentage governance;

    private Calendar delayedCalendar;

    private Action delayedAction;

    public Town(ExpeditionGame game) {
        super(game, "TOWN");
        founderExpedition = game.getExpedition();
        foundedIn = game.getGameTime().getTime();
        governance = founderExpedition.getBaseGovernance();
    }

    /**
	 * Determines how many expedition members is the settlement
	 * willing to host, based on the governance
	 * @return
	 */
    public int getLodgingCapacity() {
        return governance.transformInt(getPopulationCapacity());
    }

    /**
	 * Represents the total population the settlement may host
	 * @return
	 */
    public int getPopulationCapacity() {
        int ret = 0;
        for (Building building : buildings) {
            ret += building.getPopulationCapacity();
        }
        return ret;
    }

    /**
	 * Base name of the settlement
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * Allows changing the name of the settlement
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }

    public String getLongDescription() {
        return "The " + getTitle() + " of " + getName();
    }

    protected void townAction(int switchChat, Expedition expedition) {
        switch(switchChat) {
            case 0:
                ((ExpeditionUserInterface) UserInterface.getUI()).transferFromExpedition(this);
                break;
            case 1:
                BuildBuildings buildAction = new BuildBuildings();
                buildAction.setTown(this);
                expedition.setNextAction(buildAction);
                break;
            case 2:
                if (getPopulation() + expedition.getTotalUnits() + 1 <= getLodgingCapacity()) {
                    Hibernate hibernate = new Hibernate(7, true);
                    expedition.setPosition(getPosition().x(), getPosition().y(), getPosition().z());
                    expedition.setNextAction(hibernate);
                } else {
                    expedition.getLevel().addMessage(getDescription() + " can't host all of your expedition.");
                }
            case 3:
                expedition.setPosition(getPosition());
                break;
            case 4:
                break;
        }
    }

    public void showBlockingMessage(String message) {
        ((ExpeditionUserInterface) UserInterface.getUI()).showBlockingMessage(message);
    }

    protected String[] getTownActions() {
        return TOWN_ACTIONS;
    }

    public int getSize() {
        return (getPopulation() / 1000) + 1;
    }

    public int getPopulation() {
        return getTotalUnits();
    }

    public boolean isTown() {
        return getSize() > 5 && getSize() < 20;
    }

    public boolean isCity() {
        return getSize() > 20;
    }

    public String getTitle() {
        if (isCity()) return "city";
        if (isTown()) return "town";
        return "village";
    }

    public void addBuilding(Building building) {
        buildings.add(building);
        if (building instanceof Farm) {
            ((Farm) building).plant(ExpeditionGame.getCurrentGame().getGameTime());
        }
    }

    public Expedition getFounderExpedition() {
        return founderExpedition;
    }

    public Date getFoundedIn() {
        return foundedIn;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    /**
	 * Gather resources around the settlement, based on
	 *  * Workforce and Specialists
	 *  * Tiles around, and their resources value
	 *  * Storage capacity
	 */
    public void gatherResources() {
        int settledUnits = getTotalSettledUnits();
        int externalWorkforce = getTotalUnits() - settledUnits;
        List<Pair<Position, OverworldExpeditionCell>> cellsAround = ((ExpeditionMacroLevel) getLevel()).getMapCellsAndPositionsAround(getPosition());
        for (Pair<Position, OverworldExpeditionCell> cell : cellsAround) {
            for (Pair<String, Integer> resource : cell.getB().getDailyResources()) {
                townLog(">>> Trying to fetch " + resource.getA());
                int maxStorage = getCarryable(ItemFactory.createItem(resource.getA()));
                if (maxStorage == 0) {
                    townLog("Can't store " + resource.getA() + " in town");
                    continue;
                }
                Integer maxAvailable = null;
                if (resource.getA().equals("WOOD")) {
                    Forest f = ((ExpeditionMacroLevel) getLevel()).getOrCreateForest(cell.getA());
                    maxAvailable = f.getAvailableWood();
                }
                townLog("There is " + maxAvailable + " on land");
                int internalGatherQuantity = settledUnits * resource.getB();
                int externalGatherQuantity = externalWorkforce * resource.getB();
                int gatherQuantity = internalGatherQuantity + externalGatherQuantity;
                if (maxAvailable != null && gatherQuantity > maxAvailable) {
                    gatherQuantity = maxAvailable;
                    if (externalGatherQuantity > maxAvailable) {
                        externalGatherQuantity = maxAvailable;
                        internalGatherQuantity = 0;
                    } else {
                        internalGatherQuantity = maxAvailable - externalGatherQuantity;
                    }
                }
                townLog("Our staff can gather " + externalGatherQuantity);
                townLog("Settled units can gather " + internalGatherQuantity);
                townLog("In total, we can gather " + gatherQuantity);
                if (gatherQuantity == 0) continue;
                townLog("We can store " + maxStorage);
                if (gatherQuantity > maxStorage) {
                    gatherQuantity = maxStorage;
                    if (externalGatherQuantity > maxStorage) {
                        externalGatherQuantity = maxStorage;
                        internalGatherQuantity = 0;
                    } else {
                        internalGatherQuantity = maxStorage - externalGatherQuantity;
                    }
                }
                townLog("in the end, expedition staff gathered " + externalGatherQuantity + " all tributed");
                townLog("in the end, settled units gathered " + internalGatherQuantity);
                addItem(resource.getA(), externalGatherQuantity);
                int feudal = governance.transformInt(internalGatherQuantity);
                if (feudal > 0) addItem(resource.getA(), feudal);
                townLog("Settled units tributed " + feudal);
                if (internalGatherQuantity - feudal > 0) increaseEconomicWelfare(resource.getA(), internalGatherQuantity - feudal);
                if (resource.getA().equals("WOOD")) {
                    Forest f = ((ExpeditionMacroLevel) getLevel()).getOrCreateForest(cell.getA());
                    f.substractWood(gatherQuantity);
                }
            }
        }
    }

    private void townLog(String event) {
        System.out.println("Town " + getName() + ": " + event);
    }

    private int getTotalSettledUnits() {
        int totalUnits = 0;
        List<Equipment> inventory = getItems();
        for (Equipment equipment : inventory) {
            if (equipment.getItem() instanceof ExpeditionUnit && ((ExpeditionUnit) equipment.getItem()).isSettled()) {
                totalUnits += equipment.getQuantity();
            }
        }
        return totalUnits;
    }

    private void increaseEconomicWelfare(String itemId, int surplus) {
        ExpeditionItem item = ItemFactory.createItem(itemId);
        int welfareContribution = item.getBaseTradingValue() * surplus;
        townLog("Welfare increase by " + welfareContribution + " based on a " + surplus + " surplus of " + itemId);
        economicWelfare += welfareContribution;
    }

    private int getMaxStorage(StorageType storageType) {
        int acum = 0;
        for (Building building : buildings) {
            acum += building.getStorageCapacity(storageType);
        }
        return acum;
    }

    private int getCurrentLocalStorage(StorageType storageType) {
        int acum = 0;
        for (Equipment e : getInventory()) {
            if (((ExpeditionItem) e.getItem()).getStorageType() == storageType) {
                acum += e.getQuantity();
            }
        }
        return acum;
    }

    public void checkCrops() {
        for (Building building : buildings) {
            if (building instanceof Farm) ((Farm) building).checkCrop(ExpeditionGame.getCurrentGame().getGameTime(), this);
        }
    }

    public int getGrowthRate() {
        return (int) Math.round(getPopulation() * ((double) Util.rand(1, 5) / 100.0d));
    }

    public void tryGrowing() {
        int growth = getGrowthRate();
        if (getPopulation() == getPopulationCapacity()) {
            Building houseBuilding = BuildingFactory.createBuilding("HOUSE");
            int requiredHouses = (int) Math.ceil((double) growth / (double) houseBuilding.getPopulationCapacity());
            int availableWood = getItemCountBasic("WOOD");
            int possibleHouses = (int) Math.ceil((double) availableWood / (double) houseBuilding.getWoodCost());
            int buildHouses = requiredHouses;
            if (buildHouses > possibleHouses) buildHouses = possibleHouses;
            final int housesToBuild = buildHouses;
            List<Building> buildingPlan = new ArrayList<Building>();
            buildingPlan.add(houseBuilding);
            OutParameter woodCost = new OutParameter();
            OutParameter netTimeCostObj = new OutParameter();
            try {
                BuildingFactory.getPlanCost(buildingPlan, this, netTimeCostObj, woodCost);
            } catch (ActionCancelException e) {
                return;
            }
            int netTimeCost = netTimeCostObj.getIntValue();
            int daysCost = (int) Math.ceil((double) netTimeCost / (double) DayShiftAgent.TICKS_PER_DAY);
            delayedCalendar.setTime(ExpeditionGame.getCurrentGame().getGameTime().getTime());
            delayedCalendar.roll(Calendar.DATE, daysCost);
            delayedAction = new Action() {

                @Override
                public String getID() {
                    return null;
                }

                @Override
                public void execute() {
                    for (int i = 0; i < housesToBuild; i++) {
                        Building houseBuilding = BuildingFactory.createBuilding("HOUSE");
                        addBuilding(houseBuilding);
                    }
                }
            };
        } else {
            if (Util.chance(95)) {
                if (growth > 0) {
                    if (getPopulation() + growth > getPopulationCapacity()) {
                        growth = getPopulationCapacity() - getPopulation();
                    }
                    addItem(ItemFactory.createItem("CHILD"), growth);
                }
            }
        }
    }

    @Override
    public boolean requiresUnitsToContainItems() {
        return false;
    }

    @Override
    public String getTypeDescription() {
        if (isCity()) return "City";
        if (isTown()) return "Town";
        return "Village";
    }

    @Override
    public boolean destroyOnEmpty() {
        return false;
    }

    @Override
    public int getCarryable(ExpeditionItem item) {
        if (item instanceof ExpeditionUnit) {
            return getLodgingCapacity() - getTotalUnits();
        } else {
            StorageType storageType = item.getStorageType();
            int maxStorage = getMaxStorage(storageType);
            int currentLocalStorage = getCurrentLocalStorage(storageType);
            return maxStorage - currentLocalStorage;
        }
    }

    /**
	 * Determines if the town can carry an item from the expedition
	 */
    public boolean canCarry(ExpeditionItem item, int quantity) {
        if (item instanceof ExpeditionUnit) {
            int currentUnits = getTotalUnits();
            if (currentUnits + quantity > getLodgingCapacity()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isInfiniteCapacity() {
        return false;
    }

    @Override
    public String getDescription() {
        return getName();
    }

    @Override
    public String getClassifierID() {
        return "Town" + name;
    }

    @Override
    public void onStep(Actor a) {
        if (a != ExpeditionGame.getCurrentGame().getExpedition()) {
            return;
        }
        if (!ExpeditionGame.getCurrentGame().getExpedition().getMovementMode().isLandMovement()) {
            return;
        }
        ((ExpeditionUserInterface) UserInterface.getUI()).showCityInfo(this);
        townAction(UserInterface.getUI().switchChat(getLongDescription(), "What do you want to do", getTownActions()), (Expedition) a);
        ((ExpeditionUserInterface) UserInterface.getUI()).afterTownAction();
    }

    @Override
    public int getBuildingCapacity() {
        int power = 0;
        List<Equipment> inventory = getInventory();
        for (Equipment equipment : inventory) {
            if (equipment.getItem() instanceof ExpeditionUnit) {
                int multiplier = ((ExpeditionUnit) equipment.getItem()).getBaseID().equals("CARPENTER") ? 2 : 1;
                power += equipment.getQuantity() * multiplier;
            }
        }
        return power;
    }

    @Override
    public void reduceQuantityOf(AbstractItem item, int quantity) {
        super.reduceQuantityOf(item, quantity);
    }
}
