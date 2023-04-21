package org.gjt.universe.scheme001;

import org.gjt.universe.*;

public class Module_001_Station extends Module_001 {

    private boolean enabled;

    private float economics;

    private boolean economicsLock;

    private StationID BID;

    public static ModuleID newModule(StationID BID, ModuleDesignID EID) {
        ModuleDesign_001_Station moduleDesign = (ModuleDesign_001_Station) ModuleDesignList.get(EID);
        ModuleDesign_001_StationType moduleType = moduleDesign.getType();
        Module_001_Station newModule;
        Station_001 station = (Station_001) StationList.get(BID);
        if (moduleType == ModuleDesign_001_StationType.Power) {
            newModule = new Module_001_Station_Power(BID, EID);
            station.addModuleToPowerGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.Shield) {
            newModule = new Module_001_Station_Shield(BID, EID);
            station.addModuleToShieldGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.Attack) {
            newModule = new Module_001_Station_Attack(BID, EID);
            station.addModuleToAttackGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.StationManufacturing) {
            newModule = new Module_001_Station_StationManufacturing(BID, EID);
            station.addModuleToStationManufacturingGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.ShipManufacturing) {
            newModule = new Module_001_Station_ShipManufacturing(BID, EID);
            station.addModuleToShipManufacturingGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.Environmental) {
            newModule = new Module_001_Station_Environmental(BID, EID);
            station.addModuleToEnvironmentalGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.Scanner) {
            newModule = new Module_001_Station_Scanner(BID, EID);
            station.addModuleToScannerGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.Research) {
            newModule = new Module_001_Station_Research(BID, EID);
            station.addModuleToResearchGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.Engineering) {
            newModule = new Module_001_Station_Engineering(BID, EID);
            station.addModuleToEngineeringGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else if (moduleType == ModuleDesign_001_StationType.Storage) {
            newModule = new Module_001_Station_Storage(BID, EID);
        } else if (moduleType == ModuleDesign_001_StationType.Mining) {
            newModule = new Module_001_Station_Mining(BID, EID);
            station.addModuleToMiningGroup(newModule);
            CivList.get(station.getOwner()).addToGlobalResourceGroup(newModule);
        } else {
            newModule = new Module_001_Station(BID, EID);
        }
        ModuleList.add(newModule);
        ModuleID MID = newModule.getID();
        station.addModule(MID);
        return MID;
    }

    Module_001_Station(ModuleDesignID EID) {
        super(EID);
    }

    Module_001_Station(StationID BID, ModuleDesignID EID) {
        super(EID);
        this.BID = BID;
        enabled = true;
        economics = (float) 0.0;
        ModuleDesign_001_Station moduleDesign = (ModuleDesign_001_Station) ModuleDesignList.get(EID);
        if (moduleDesign.getType() == ModuleDesign_001_StationType.Power) {
            economicsLock = true;
            CostID CID = moduleDesign.getMaintenanceCost();
            Cost_001 cost = (Cost_001) CostList.get(CID);
            economics = cost.getEconomic();
        }
        if (moduleDesign.getType() == ModuleDesign_001_StationType.Environmental) {
            economicsLock = true;
        }
        if (moduleDesign.getType() == ModuleDesign_001_StationType.Storage) {
            economicsLock = true;
        }
    }

    void setEconomics(float val) {
        economics = val;
    }

    float getEconomics() {
        ModuleDesign_001_Station moduledesign = (ModuleDesign_001_Station) ModuleDesignList.get(getDesign());
        ModuleDesign_001_StationType type = moduledesign.getType();
        CostID CID = moduledesign.getMaintenanceCost();
        Cost_001 cost = (Cost_001) CostList.get(CID);
        float maxEcon = cost.getEconomic();
        if (type == ModuleDesign_001_StationType.Power) {
            economics = maxEcon;
        }
        if (type == ModuleDesign_001_StationType.Shield) {
            economics = maxEcon;
        }
        if (type == ModuleDesign_001_StationType.Attack) {
            economics = maxEcon;
        }
        if (type == ModuleDesign_001_StationType.Scanner) {
            economics = maxEcon;
        }
        if (!isOperating()) {
            economics = 0;
        }
        return economics;
    }

    void setEconomicsLock(boolean val) {
        economicsLock = val;
    }

    boolean getEconomicsLock() {
        return economicsLock;
    }

    float getResourcesConsumed() {
        ModuleDesign_001_Station moduledesign = (ModuleDesign_001_Station) ModuleDesignList.get(getDesign());
        CostID CID = moduledesign.getMaintenanceCost();
        Cost_001 cost = (Cost_001) CostList.get(CID);
        float maxEcon = cost.getEconomic();
        float economicRatio = 0;
        if ((cost.getEconomic() != 0) && getEnabled()) {
            economicRatio = economics / maxEcon;
        }
        return economicRatio * cost.getResource();
    }

    boolean getEnabled() {
        return enabled;
    }

    void setEnabled(boolean val) {
        enabled = val;
    }

    /** This method returns true if the module is operating.  A module
	is usually capable of operating unless there is a production
	queue associated with the module and that queue is empty. */
    boolean isOperating() {
        ModuleDesign_001_Station moduledesign = (ModuleDesign_001_Station) ModuleDesignList.get(getDesign());
        ModuleDesign_001_StationType type = moduledesign.getType();
        Station_001 station = (Station_001) StationList.get(BID);
        if (type == ModuleDesign_001_StationType.StationManufacturing) {
            OrderQueueSetIndex_001 set = OrderQueueSetIndex_001.StationManufacturing;
            OrderQueue queue = OrderEngine.getOrderQueue(set, station.getID());
            if (queue == null || queue.isEmpty()) {
                return false;
            }
        }
        if (type == ModuleDesign_001_StationType.ShipManufacturing) {
            OrderQueueSetIndex_001 set = OrderQueueSetIndex_001.ShipManufacturing;
            OrderQueue queue = OrderEngine.getOrderQueue(set, station.getID());
            if (queue == null || queue.isEmpty()) {
                return false;
            }
        }
        if (type == ModuleDesign_001_StationType.Research) {
            OrderQueueSetIndex_001 set = OrderQueueSetIndex_001.Research;
            OrderQueue queue = OrderEngine.getOrderQueue(set, station.getID());
            if (queue == null || queue.isEmpty()) {
                return false;
            }
        }
        if (type == ModuleDesign_001_StationType.Engineering) {
            OrderQueueSetIndex_001 set = OrderQueueSetIndex_001.Engineering;
            OrderQueue queue = OrderEngine.getOrderQueue(set, station.getID());
            if (queue == null || queue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public VectorDisplayReturn specificDisplayDebug() {
        VectorDisplayReturn returnList = new VectorDisplayReturn();
        returnList.add(new DisplayReturn("Type: 001_Station"));
        returnList.add(new DisplayReturn("Enabled: " + getEnabled()));
        returnList.add(new DisplayReturn("Economics: " + economics));
        returnList.add(new DisplayReturn("Economics Lock: " + economicsLock));
        returnList.add(new DisplayReturn("Owner: " + getOwner(), getOwner()));
        returnList.add(new DisplayReturn("Location: " + getLocation()));
        returnList.add(new DisplayReturn("Station: " + BID));
        returnList.add(new DisplayReturn("Design: " + getDesign(), getDesign()));
        return returnList;
    }
}
