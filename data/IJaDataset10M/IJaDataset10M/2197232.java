package starcraft.gamemodel.races.terrans;

import starcraft.gamemodel.races.CombatCard;
import starcraft.gamemodel.races.Faction;
import starcraft.gamemodel.races.Race;
import starcraft.gamemodel.races.terrans.units.BattleCruiserUnitType;
import starcraft.gamemodel.races.terrans.units.FirebatUnitType;
import starcraft.gamemodel.races.terrans.units.GhostUnitType;
import starcraft.gamemodel.races.terrans.units.GoliathUnitType;
import starcraft.gamemodel.races.terrans.units.MarineUnitType;
import starcraft.gamemodel.races.terrans.units.ScienceVesselUnitType;
import starcraft.gamemodel.races.terrans.units.SiegeTankUnitType;
import starcraft.gamemodel.races.terrans.units.VultureUnitType;
import starcraft.gamemodel.races.terrans.units.WraithUnitType;
import starcraft.gamemodel.references.BuildingType;
import starcraft.gamemodel.references.UnitType;

public class TerranRace implements Race {

    private static final long serialVersionUID = -620533863413983129L;

    public static final TerranRace INSTANCE = new TerranRace();

    private final Faction[] factions = new Faction[] { ArcturusMengskFaction.INSTANCE, JimRaynorFaction.INSTANCE };

    private final UnitType[] unitTypes = new UnitType[] { MarineUnitType.INSTANCE, FirebatUnitType.INSTANCE, GhostUnitType.INSTANCE, VultureUnitType.INSTANCE, GoliathUnitType.INSTANCE, SiegeTankUnitType.INSTANCE, WraithUnitType.INSTANCE, BattleCruiserUnitType.INSTANCE, ScienceVesselUnitType.INSTANCE };

    private final BuildingType[] buildingTypes = new BuildingType[] {};

    private final CombatCard[] initialCombatDeck = new CombatCard[] { TerranCombatCards.Card01, TerranCombatCards.Card02, TerranCombatCards.Card03, TerranCombatCards.Card04, TerranCombatCards.Card05, TerranCombatCards.Card06, TerranCombatCards.Card07, TerranCombatCards.Card08, TerranCombatCards.Card09, TerranCombatCards.Card10, TerranCombatCards.Card11, TerranCombatCards.Card12, TerranCombatCards.Card13, TerranCombatCards.Card14, TerranCombatCards.Card15, TerranCombatCards.Card16, TerranCombatCards.Card17, TerranCombatCards.Card18, TerranCombatCards.Card19, TerranCombatCards.Card20 };

    private TerranRace() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return "Terran";
    }

    @Override
    public String getFileSuffix() {
        return "terran";
    }

    @Override
    public Faction[] getFactions() {
        return this.factions;
    }

    @Override
    public UnitType[] getUnitTypes() {
        return this.unitTypes;
    }

    @Override
    public BuildingType[] getBuildingTypes() {
        return this.buildingTypes;
    }

    @Override
    public CombatCard[] getInitialCombatDeck() {
        return this.initialCombatDeck;
    }

    @Override
    public int getNumberOfAfterBattleCards() {
        return 0;
    }

    @Override
    public int getNumberOfDefenseCards() {
        return 1;
    }

    @Override
    public int getNumberOfHandCards() {
        return 8;
    }

    @Override
    public int getNumberOfOffenseCards() {
        return 3;
    }
}
