package org.gjt.universe.scheme001;

import org.gjt.universe.*;

class OrderShipBuild_001 extends Order {

    private StationID BID;

    private Station_001 station;

    private ShipDesignID DID;

    private float pointsManufactured;

    private float manufacturingPointsRequired;

    public OrderShipBuild_001(StationID BID, ShipDesignID DID) {
        this.BID = BID;
        station = (Station_001) StationList.get(BID);
        this.DID = DID;
        ShipDesign_001 shipDesign = (ShipDesign_001) ShipDesignList.get(DID);
        pointsManufactured = 0;
        CostID CID = shipDesign.getBuildCost();
        Cost_001 cost = (Cost_001) CostList.get(CID);
        manufacturingPointsRequired = cost.getShipManufacturing();
    }

    StationID getStation() {
        return BID;
    }

    ShipDesignID getShipDesign() {
        return DID;
    }

    public boolean process() {
        float manufacturingThisTurn = station.useShipManufacturing(manufacturingPointsRequired - pointsManufactured);
        if ((pointsManufactured == 0) && (manufacturingThisTurn > 0)) {
            ShipDesign_001 shipdesign = (ShipDesign_001) ShipDesignList.get(DID);
            float populationchange = shipdesign.getPopulationBuildCost();
            float population = station.getPopulation();
            if (population < populationchange) {
                return false;
            }
            station.setPopulation(population - populationchange);
        }
        pointsManufactured += manufacturingThisTurn;
        if (pointsManufactured >= manufacturingPointsRequired) {
            CivID owner = station.getOwner();
            ShipID HID = ShipStd.newShip(DID, owner);
            FleetID FID = FleetStd.newFleet(owner, BID);
            FleetBase fleet = FleetList.get(FID);
            fleet.addShip(HID);
            ResultBuild.newResult(owner, BID, HID, FID);
            return true;
        }
        return false;
    }

    public Index getKey() {
        return BID;
    }

    public String toString() {
        ShipDesignBase shipDesign = ShipDesignList.get(DID);
        return shipDesign.getName();
    }

    public int getEstimatedTimeOfCompletion() {
        return 0;
    }

    public float getPercentageCompletion() {
        if (manufacturingPointsRequired == 0) {
            return 1;
        }
        return pointsManufactured / manufacturingPointsRequired;
    }

    public void atPosition(int i) {
    }

    public void submit() {
        OrderEngine.submit(this);
    }

    public void delete() {
        OrderEngine.delete(this);
    }
}
