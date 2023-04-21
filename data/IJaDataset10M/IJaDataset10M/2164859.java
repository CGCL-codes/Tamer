package org.gjt.universe.scheme001;

import java.util.Vector;
import java.util.Enumeration;
import java.io.IOException;
import org.gjt.universe.*;

public final class Scheme_001 extends SchemeBase {

    public OrderQueueSet getOrderQueueSet() {
        OrderQueueSetIndex startStatic = OrderQueueSetIndex_001.Station1;
        Vector vec = new Vector();
        for (Enumeration e = OrderQueueSetIndex.elements(); e.hasMoreElements(); ) {
            OrderQueueSetIndex_001 index = (OrderQueueSetIndex_001) e.nextElement();
            vec.addElement(index.toString());
        }
        return new OrderQueueSet(vec);
    }

    public void initialize(String filename) throws IOException {
        ReadThemeFile_001.initialize(filename);
    }

    public void initialize() throws IOException {
        Log.debug("Scheme initialization begins...");
        ReadThemeFile_001.initialize();
        Log.debug("Scheme initialization completed.");
    }

    public void orderEngineSetup() {
        (new OrderStation1_001()).submit();
        (new OrderStation2_001()).submit();
    }

    public OrderQueueSetIndex whichQueueSet(Order order) {
        if (order instanceof OrderStation1_001) {
            return OrderQueueSetIndex_001.Station1;
        }
        if (order instanceof OrderMove) {
            return OrderQueueSetIndex_001.FleetMove;
        }
        if (order instanceof OrderTransitWormhole) {
            return OrderQueueSetIndex_001.FleetMove;
        }
        if (order instanceof OrderColonize) {
            return OrderQueueSetIndex_001.FleetMove;
        }
        if (order instanceof OrderStationBuild_001) {
            return OrderQueueSetIndex_001.StationManufacturing;
        }
        if (order instanceof OrderShipBuild_001) {
            return OrderQueueSetIndex_001.ShipManufacturing;
        }
        if (order instanceof OrderResearch_001) {
            return OrderQueueSetIndex_001.Research;
        }
        if (order instanceof OrderEngineering_001) {
            return OrderQueueSetIndex_001.Engineering;
        }
        if (order instanceof OrderStation2_001) {
            return OrderQueueSetIndex_001.Station2;
        }
        if (order instanceof OrderTransfer) {
            ProtoObject project = ((OrderTransfer) order).getProject();
            if (project instanceof ProtoModule) {
                return OrderQueueSetIndex_001.StationManufacturing;
            }
            if (project instanceof ProtoShip) {
                return OrderQueueSetIndex_001.ShipManufacturing;
            }
            if (project instanceof ProtoTech) {
                return OrderQueueSetIndex_001.Research;
            }
        }
        return null;
    }

    public void determineSpaceBattles() {
        VectorSystemID battleLocations = new VectorSystemID();
        for (Enumeration e1 = FleetList.elements(); e1.hasMoreElements(); ) {
            FleetBase fleet1 = (FleetBase) e1.nextElement();
            FleetID FID1 = fleet1.getID();
            Location loc1 = fleet1.getLocation();
            CivID owner1 = fleet1.getOwner();
            for (Enumeration e2 = FleetList.elements(); e2.hasMoreElements(); ) {
                FleetBase fleet2 = (FleetBase) e2.nextElement();
                FleetID FID2 = fleet2.getID();
                Location loc2 = fleet2.getLocation();
                CivID owner2 = fleet2.getOwner();
                if (!owner1.equals(owner2)) {
                    if (loc1.getSystem().equals(loc2.getSystem())) {
                        SystemID SID = loc1.getSystem();
                        int size = battleLocations.size();
                        boolean notInVector = true;
                        for (int cnt = 0; cnt < size; cnt++) {
                            SystemID SID2 = battleLocations.get(cnt);
                            if (SID.equals(SID2)) {
                                notInVector = false;
                            }
                        }
                        if (notInVector) {
                            battleLocations.add(SID);
                        }
                    }
                }
            }
            int size = StationList.size();
            for (int cnt = 1; cnt < size; cnt++) {
                StationID BID = new StationID(cnt);
                StationBase station = StationList.get(BID);
                Location loc2 = station.getLocation();
                CivID owner2 = station.getOwner();
                if (!owner1.equals(owner2)) {
                    if (loc1.getSystem().equals(loc2.getSystem())) {
                        SystemID SID = loc1.getSystem();
                        int size2 = battleLocations.size();
                        boolean notInVector = true;
                        for (int cnt2 = 0; cnt2 < size2; cnt2++) {
                            SystemID SID2 = battleLocations.get(cnt2);
                            if (SID.equals(SID2)) {
                                notInVector = false;
                            }
                        }
                        if (notInVector) {
                            battleLocations.add(SID);
                        }
                    }
                }
            }
        }
        int size = StationList.size();
        for (int cnt = 0; cnt < size; cnt++) {
        }
        size = battleLocations.size();
        for (int cnt = 0; cnt < size; cnt++) {
            Location loc = new Location(battleLocations.get(cnt));
            SpaceBattle_001.performBattle(loc);
        }
    }

    /** Factory method for creating this scheme's version of a Station **/
    public StationID newStation(CivID owner, PlanetID PID) {
        return Station_001.newStation(owner, PID);
    }

    /** Factory method for creating this scheme's version of a Planet **/
    public PlanetID newPlanet(SystemID SID, int star_index, String in_name) {
        return Planet_001.newPlanet(SID, star_index, in_name);
    }

    public VectorDisplayReturn specificDisplayDebug() {
        VectorDisplayReturn returnList = new VectorDisplayReturn();
        returnList.add(new DisplayReturn("Scheme 001"));
        returnList.add(new DisplayReturn("Race Attack Weight: " + Race_001.getAttackWeight()));
        returnList.add(new DisplayReturn("Race Defense Weight: " + Race_001.getDefenseWeight()));
        returnList.add(new DisplayReturn("Race Mining Weight: " + Race_001.getMiningWeight()));
        returnList.add(new DisplayReturn("Race Population Growth Weight: " + Race_001.getPopulationGrowthWeight()));
        returnList.add(new DisplayReturn("Race Manufacturing Weight: " + Race_001.getManufacturingWeight()));
        returnList.add(new DisplayReturn("Race Economic Weight: " + Race_001.getEconomicWeight()));
        returnList.add(new DisplayReturn("Race Research Point Production Weight: " + Race_001.getResearchPointProductionWeight()));
        returnList.add(new DisplayReturn("Race Engineering Point Production Weight: " + Race_001.getEngineeringPointProductionWeight()));
        returnList.add(new DisplayReturn("Theme Battle Factor: " + SpaceBattle_001.getBattleFactor()));
        returnList.add(new DisplayReturn("Theme Damage Factor: " + SpaceBattle_001.getDamageFactor()));
        returnList.add(new DisplayReturn("Theme Shield Regeneration Factor: " + SpaceBattle_001.getShieldRegenerationFactor()));
        returnList.add(new DisplayReturn("Theme Station Growth Factor: " + Station_001.getThemeGrowthFactor()));
        returnList.add(new DisplayReturn("Theme Station Economic Factor: " + Station_001.getThemeEconomicFactor()));
        returnList.add(new DisplayReturn("Theme Station Mining Linear Factor: " + Station_001.getThemeMiningLinearFactor()));
        returnList.add(new DisplayReturn("Theme Station Mining Exponential Factor: " + Station_001.getThemeMiningExponentialFactor()));
        returnList.add(new DisplayReturn("Theme Station Environment Factor: " + Station_001.getThemeEnvironmentFactor()));
        returnList.add(new DisplayReturn("Theme Station Manufacturing Factor: " + Station_001.getThemeManufacturingFactor()));
        returnList.add(new DisplayReturn("Theme Station Research Factor: " + Station_001.getThemeResearchFactor()));
        returnList.add(new DisplayReturn("Theme Station Engineering Factor: " + Station_001.getThemeEngineeringFactor()));
        return returnList;
    }
}
