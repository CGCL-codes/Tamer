package com.customwars.client.model.gameobject;

import com.customwars.client.model.ArmyBranch;
import com.customwars.client.model.map.Range;
import com.customwars.client.model.map.path.DefaultMoveStrategy;
import com.customwars.client.model.map.path.MoveStrategy;
import com.customwars.client.tools.Args;
import com.customwars.client.tools.StringUtil;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Immutable statistics of a Unit
 */
public class UnitStats implements Serializable {

    private static final Range DEFAULT_SUPPLY_RANGE = new Range(1, 1);

    final int unitID;

    final int imgRowID;

    final String name;

    final String description;

    final int price;

    final int movement;

    final int vision;

    MoveStrategy moveStrategy;

    final int maxExperience;

    final int maxHp;

    Range supplyRange;

    final int maxSupplies;

    final int suppliesPerTurn;

    final int healRate;

    final boolean canCapture;

    final boolean canDive;

    final boolean canJoin;

    final boolean canFlare;

    final boolean canHide;

    final boolean canLaunchUnit;

    private Map<String, String> transformTerrains;

    private Map<String, String> buildCities;

    private List<String> produces;

    private List<String> supplyUnitsInTransport;

    private List<String> supplyUnits;

    final ArmyBranch armyBranch;

    final int movementType;

    private String primaryWeaponName;

    private String secondaryWeaponName;

    private final int suppliesPerTurnWhenHidden;

    final int maxConstructionMaterial;

    private TransportStats transportStats;

    public UnitStats(int unitID, int imgRowID, String name, String description, int price, int movement, int vision, int maxExperience, int maxHp, int maxSupplies, int maxTransportCount, int suppliesPerTurn, boolean canCapture, boolean canDive, boolean canJoin, boolean canFlare, boolean canHide, boolean canLaunchUnit, List<String> transports, List<String> supplyUnitsInTransport, List<String> supplyUnits, ArmyBranch armyBranch, int movementType, Range supplyRange, String primaryWeaponName, String secondaryWeaponName, int healRate, int suppliesPerTurnWhenHidden, int maxConstructionMaterial) {
        this.unitID = unitID;
        this.imgRowID = imgRowID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.movement = movement;
        this.vision = vision;
        this.maxExperience = maxExperience;
        this.maxHp = maxHp;
        this.maxSupplies = maxSupplies;
        this.suppliesPerTurn = suppliesPerTurn;
        this.canCapture = canCapture;
        this.canDive = canDive;
        this.canJoin = canJoin;
        this.canFlare = canFlare;
        this.canHide = canHide;
        this.canLaunchUnit = canLaunchUnit;
        this.transportStats = new TransportStats(maxTransportCount);
        this.transportStats.addAll(transports);
        this.supplyUnitsInTransport = supplyUnitsInTransport;
        this.supplyUnits = supplyUnits;
        this.armyBranch = armyBranch;
        this.movementType = movementType;
        this.supplyRange = supplyRange;
        this.primaryWeaponName = primaryWeaponName;
        this.secondaryWeaponName = secondaryWeaponName;
        this.healRate = healRate;
        this.suppliesPerTurnWhenHidden = suppliesPerTurnWhenHidden;
        this.maxConstructionMaterial = maxConstructionMaterial;
        init();
    }

    public void init() {
        if (transportStats == null) transportStats = new TransportStats();
        transportStats.init(this);
        transformTerrains = Args.createEmptyMapIfNull(transformTerrains);
        buildCities = Args.createEmptyMapIfNull(buildCities);
        produces = Args.createEmptyListIfNull(produces);
        supplyUnitsInTransport = Args.createEmptyListIfNull(supplyUnitsInTransport);
        supplyUnits = Args.createEmptyListIfNull(supplyUnits);
        if (primaryWeaponName == null) primaryWeaponName = "";
        if (secondaryWeaponName == null) secondaryWeaponName = "";
        if (moveStrategy == null) moveStrategy = new DefaultMoveStrategy();
        Args.checkForNull(name, "please provide a name for unitID " + unitID);
        Args.checkForNull(description, "please provide a description for unit " + name);
        Args.validate(suppliesPerTurn < 0, "supplies per turn should be positive");
        Args.validate(suppliesPerTurnWhenHidden < 0, "supplies per turn when hidden should be positive");
        Args.validate(maxConstructionMaterial < 0, "Construction material must be positive");
        initSupplyRange();
    }

    private void initSupplyRange() {
        boolean canSupply = !supplyUnits.isEmpty();
        if (canSupply && supplyRange == null) {
            supplyRange = DEFAULT_SUPPLY_RANGE;
        } else {
            supplyRange = supplyRange == null ? Range.ZERO_RANGE : supplyRange;
        }
    }

    /**
   * Validate ensures that String references to other objects are valid.
   * This check cannot be done in the init() method because all resources may not be loaded yet.
   * Call this method when all objects have been loaded.
   */
    public void validate() {
        transportStats.validate(this);
        for (String unitName : produces) {
            Args.validate(!UnitFactory.hasUnitForName(unitName), "Illegal unit name " + unitName + " in build unit stats");
        }
        for (Map.Entry<String, String> entry : buildCities.entrySet()) {
            String terrainName = entry.getKey();
            String cityName = entry.getValue();
            Args.validate(!TerrainFactory.hasTerrainForName(terrainName), "Illegal terrain " + terrainName + " in build cities stats");
            Args.validate(!CityFactory.hasCityForName(cityName), "Illegal city " + cityName + " in build cities stats");
        }
        for (String unitName : supplyUnits) {
            Args.validate(!UnitFactory.hasUnitForName(unitName), "Illegal unit " + unitName + " in supply units stats");
        }
        for (String unitName : supplyUnitsInTransport) {
            Args.validate(!UnitFactory.hasUnitForName(unitName), "Illegal unit " + unitName + " in supply units in transport stats");
        }
        Args.validate(StringUtil.hasContent(primaryWeaponName) && !WeaponFactory.hasWeapon(primaryWeaponName), "Illegal primary weapon " + primaryWeaponName + " for unit " + name);
        Args.validate(StringUtil.hasContent(secondaryWeaponName) && !WeaponFactory.hasWeapon(secondaryWeaponName), "Illegal secondary weapon " + secondaryWeaponName + " for unit " + name);
    }

    public ArmyBranch getArmyBranch() {
        return armyBranch;
    }

    public int getID() {
        return unitID;
    }

    public String getName() {
        return name;
    }

    public int getImgRowID() {
        return imgRowID;
    }

    public Range getSupplyRange() {
        return supplyRange;
    }

    public int getVision() {
        return vision;
    }

    public boolean canJoin() {
        return canJoin;
    }

    public boolean canFlare() {
        return canFlare;
    }

    public int getPrice() {
        return price;
    }

    public int getMaxSupplies() {
        return maxSupplies;
    }

    public String getDescription() {
        return description;
    }

    public int getMovement() {
        return movement;
    }

    public int getMaxExperience() {
        return maxExperience;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getMaxTransportCount() {
        return transportStats.maxTransportCount;
    }

    public int getSuppliesPerTurn() {
        return suppliesPerTurn;
    }

    public boolean canDive() {
        return canDive;
    }

    public int getMovementType() {
        return movementType;
    }

    public boolean canHide() {
        return canHide;
    }

    public boolean canLaunchUnit() {
        return canLaunchUnit;
    }

    public boolean canCapture() {
        return canCapture;
    }

    public boolean canTransformTerrain(Terrain terrain) {
        return transformTerrains.containsKey(terrain.getName());
    }

    public String getTransformTerrainFor(Terrain terrain) {
        return transformTerrains.get(terrain.getName());
    }

    public boolean canTransport() {
        return transportStats.canTransport();
    }

    public boolean canTransport(String unitID) {
        return transportStats.canTransport(unitID);
    }

    public List<String> getTransports() {
        return transportStats.getTransports();
    }

    public boolean canBuildCity(String id) {
        return buildCities.containsKey(id);
    }

    public boolean canBuildCityOn(Terrain terrain) {
        return canBuildCity(terrain.getName());
    }

    public String getCityToBuildOnTerrain(Terrain terrain) {
        return buildCities.get(terrain.getName());
    }

    public boolean canSupplyUnitInTransport(Unit unit) {
        return canSupplyUnitInTransport(unit.getStats().name);
    }

    public boolean canSupplyUnitInTransport(String unitID) {
        return supplyUnitsInTransport.contains(unitID);
    }

    public boolean canSupplyUnitAroundTransport(Unit unit) {
        return canSupplyUnitAroundTransport(unit.getStats().name);
    }

    public boolean canSupplyUnitAroundTransport(String unitID) {
        return supplyUnits.contains(unitID);
    }

    public boolean canProduceUnit(String unitID) {
        return produces.contains(unitID);
    }

    public Iterable<String> getUnitsThatCanBeProduced() {
        return Collections.unmodifiableList(produces);
    }

    public boolean canProduceUnits() {
        return !produces.isEmpty();
    }

    public String getSecondaryWeaponName() {
        return secondaryWeaponName;
    }

    public String getPrimaryWeaponName() {
        return primaryWeaponName;
    }

    public int getSuppliesPerTurnWhenHidden() {
        return suppliesPerTurnWhenHidden;
    }

    public int getMaxConstructionMaterial() {
        return maxConstructionMaterial;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(100);
        sb.append("UnitStats");
        sb.append("{unitID=").append(unitID);
        sb.append(", imgRowID=").append(imgRowID);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", movement=").append(movement);
        sb.append(", vision=").append(vision);
        sb.append(", moveStrategy=").append(moveStrategy);
        sb.append(", maxExperience=").append(maxExperience);
        sb.append(", maxHp=").append(maxHp);
        sb.append(", supplyRange=").append(supplyRange);
        sb.append(", maxSupplies=").append(maxSupplies);
        sb.append(", suppliesPerTurn=").append(suppliesPerTurn);
        sb.append(", healRate=").append(healRate);
        sb.append(", canCapture=").append(canCapture);
        sb.append(", canDive=").append(canDive);
        sb.append(", canJoin=").append(canJoin);
        sb.append(", canFlare=").append(canFlare);
        sb.append(", canHide=").append(canHide);
        sb.append(", canLaunchUnit=").append(canLaunchUnit);
        sb.append(", transformTerrains=").append(transformTerrains);
        sb.append(", buildCities=").append(buildCities);
        sb.append(", buildUnits=").append(produces);
        sb.append(", supplyUnitsInTransport=").append(supplyUnitsInTransport);
        sb.append(", supplyUnits=").append(supplyUnits);
        sb.append(", armyBranch=").append(armyBranch);
        sb.append(", movementType=").append(movementType);
        sb.append(", primaryWeaponName='").append(primaryWeaponName).append('\'');
        sb.append(", secondaryWeaponName='").append(secondaryWeaponName).append('\'');
        sb.append(", suppliesPerTurnWhenHidden=").append(suppliesPerTurnWhenHidden);
        sb.append(", maxConstructionMaterial=").append(maxConstructionMaterial);
        sb.append(", transportStats=").append(transportStats);
        sb.append('}');
        return sb.toString();
    }
}
