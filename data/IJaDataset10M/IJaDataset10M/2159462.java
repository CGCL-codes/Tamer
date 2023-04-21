package server.campaign.commands;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import server.campaign.BuildTable;
import server.campaign.CampaignMain;
import server.campaign.NewbieHouse;
import server.campaign.SHouse;
import server.campaign.SPlanet;
import server.campaign.SPlayer;
import server.campaign.SUnit;
import server.campaign.SUnitFactory;
import server.campaign.pilot.SPilot;
import server.mwmysql.HistoryHandler;
import common.CampaignData;
import common.Unit;

public class RequestCommand implements Command {

    int accessLevel = 0;

    String syntax = "";

    public int getExecutionLevel() {
        return accessLevel;
    }

    public void setExecutionLevel(int i) {
        accessLevel = i;
    }

    public String getSyntax() {
        return syntax;
    }

    public void process(StringTokenizer command, String Username) {
        if (accessLevel != 0) {
            int userLevel = CampaignMain.cm.getServer().getUserLevel(Username);
            if (userLevel < getExecutionLevel()) {
                CampaignMain.cm.toUser("AM:Insufficient access level for command. Level: " + userLevel + ". Required: " + accessLevel + ".", Username, true);
                return;
            }
        }
        SPlayer p = CampaignMain.cm.getPlayer(Username);
        SPlanet planet = null;
        SUnitFactory factory = null;
        boolean needsMoreTechs = false;
        String result = "";
        String weightstring = command.nextToken();
        if (p.getMyHouse().isNewbieHouse()) {
            if (weightstring.equals("resetunits")) {
                NewbieHouse nh = (NewbieHouse) p.getMyHouse();
                CampaignMain.cm.toUser(nh.requestNewMech(p, false, null), Username, true);
                return;
            }
            result = CampaignMain.cm.getConfig("NewbieHouseName") + " players may not purchase new units; however, they may reset their units.";
            result += "<br><a href=\"MEKWARS/c request#resetunits\">Click here to request a reset of your units.</a>";
            CampaignMain.cm.toUser(result, Username, true);
            return;
        }
        if (p.hasUnmaintainedUnit()) {
            CampaignMain.cm.toUser("AM:Your faction refuses to assign new units to you force while units in your hangar are unmaintained!", Username, true);
            return;
        }
        if (p.mayAcquireWelfareUnits()) {
            SHouse house = p.getMyHouse();
            SUnit unit = buildWelfareMek(house.getName());
            SPilot pilot = house.getNewPilot(unit.getType());
            unit.setPilot(pilot);
            p.addUnit(unit, true);
            CampaignMain.cm.toUser("AM:High Command has given you a new unit from its welfare rolls to help you get back on your feet!", Username, true);
            if (CampaignMain.cm.isKeepingUnitHistory()) {
                CampaignMain.cm.MySQL.addHistoryEntry(HistoryHandler.HISTORY_TYPE_UNIT, unit.getDBId(), HistoryHandler.UNIT_CREATED, unit.getProducer());
                CampaignMain.cm.MySQL.addHistoryEntry(HistoryHandler.HISTORY_TYPE_UNIT, unit.getDBId(), HistoryHandler.UNIT_PRODUCED_BY_WELFARE, "Activated from garrison duty and assigned to combat duty.");
                CampaignMain.cm.MySQL.addHistoryEntry(HistoryHandler.HISTORY_TYPE_PILOT, pilot.getDBId(), HistoryHandler.PILOT_ASSIGNED, "Assigned to " + unit.getModelName());
                CampaignMain.cm.MySQL.addHistoryEntry(HistoryHandler.HISTORY_TYPE_UNIT, unit.getDBId(), HistoryHandler.PILOT_ASSIGNED, pilot.getName() + " assigned to unit");
            }
            return;
        }
        int weightclass;
        try {
            weightclass = Integer.parseInt(weightstring);
        } catch (Exception ex) {
            weightclass = Unit.getWeightIDForName(weightstring.toUpperCase());
        }
        int type_id = Unit.MEK;
        String typestring = command.nextToken();
        try {
            type_id = Integer.parseInt(typestring);
        } catch (Exception ex) {
            type_id = Unit.getTypeIDForName(typestring);
        }
        if (!p.mayUse(weightclass)) {
            CampaignMain.cm.toUser("AM:You are not experienced enough to use " + Unit.getWeightClassDesc(weightclass) + " units.", Username, true);
            return;
        }
        if (!Boolean.parseBoolean(p.getSubFaction().getConfig("CanBuyNew" + Unit.getWeightClassDesc(weightclass) + Unit.getTypeClassDesc(type_id)))) {
            CampaignMain.cm.toUser("AM:Sorry as a member of " + p.getSubFactionName() + " you are unable to purchase this unit.", Username);
            return;
        }
        if (!p.hasRoomForUnit(type_id, weightclass)) {
            CampaignMain.cm.toUser("AM:Sorry, you already have the maximum number of " + Unit.getWeightClassDesc(weightclass) + " " + Unit.getTypeClassDesc(type_id) + "s", Username);
            return;
        }
        String planetName = "";
        String factoryName = "";
        if (command.hasMoreElements()) {
            planetName = command.nextToken();
            planet = (SPlanet) CampaignMain.cm.getData().getPlanetByName(planetName);
            if (planet == null) {
                CampaignMain.cm.toUser("AM:Could not find planet: " + planetName + ".", Username, true);
                return;
            }
            if (!planet.getOwner().equals(p.getMyHouse())) {
                CampaignMain.cm.toUser("AM:Your faction does not control " + planetName + ".", Username, true);
                return;
            }
            if (planet.getFactoriesOfWeighclass(weightclass).size() == 0) {
                CampaignMain.cm.toUser(planetName + " does not produce units of the weight class specified.", Username, true);
                return;
            }
            try {
                factoryName = command.nextToken();
            } catch (NoSuchElementException e) {
                CampaignMain.cm.toUser("AM:You requested a unit from " + planetName + ", but did not specifiy which factory to use.", Username, true);
                return;
            }
            Vector<SUnitFactory> namedFactories = planet.getFactoriesByName(factoryName);
            if (namedFactories.size() == 0) {
                CampaignMain.cm.toUser("AM:There is no " + factoryName + " on " + planetName + ".", Username, true);
                return;
            }
            for (SUnitFactory currFac : namedFactories) {
                if (currFac.getWeightclass() == weightclass) {
                    factory = currFac;
                    break;
                }
            }
            if (factory == null) {
                CampaignMain.cm.toUser(factoryName + " on " + planetName + " does not produce units of the requested weightclass.", Username, true);
                return;
            }
            if (factory.getAccessLevel() > p.getSubFactionAccess()) {
                CampaignMain.cm.toUser("You do not have sufficient rank to purchase a unit from " + factoryName + " on " + planetName + ".", Username);
                return;
            }
            if (!factory.canProduce(type_id)) {
                CampaignMain.cm.toUser(factoryName + " on " + planetName + " does not produce units of the requested type.", Username, true);
                return;
            }
            if (factory.getTicksUntilRefresh() > 0) {
                CampaignMain.cm.toUser(factoryName + " is currently refreshing. " + factory.getTicksUntilRefresh() + " miniticks remaining.", Username, true);
                return;
            }
        } else {
            factory = p.getMyHouse().getNativeFactoryForProduction(type_id, weightclass, true);
            if (factory != null) {
                planet = factory.getPlanet();
            }
        }
        if (planet == null || factory == null) {
            CampaignMain.cm.toUser("AM:No " + p.getMyHouse().getName() + " factory is available to fill your order at this time (Click on icon in House Status to use captured factories).", Username, true);
            return;
        }
        int mechCbills = factory.getPriceForUnit(weightclass, type_id);
        int mechInfluence = factory.getInfluenceForUnit(weightclass, type_id);
        int mechPP = factory.getPPCost(weightclass, type_id);
        SHouse playerHouse = p.getMyHouse();
        if (!factory.getFounder().equalsIgnoreCase(playerHouse.getName())) {
            mechCbills = Math.round(mechCbills * CampaignMain.cm.getFloatConfig("NonOriginalCBillMultiplier"));
            mechInfluence = Math.round(mechInfluence * CampaignMain.cm.getFloatConfig("NonOriginalInfluenceMultiplier"));
            mechPP = Math.round(mechPP * CampaignMain.cm.getFloatConfig("NonOriginalComponentMultiplier"));
        }
        if (p.willHaveHangarPenalty(type_id, weightclass)) {
            int costPenalty = p.calculateHangarPenaltyForNextPurchase(type_id, weightclass);
            mechCbills += costPenalty;
        }
        if (mechInfluence > CampaignMain.cm.getIntegerConfig("InfluenceCeiling")) {
            mechInfluence = CampaignMain.cm.getIntegerConfig("InfluenceCeiling");
        }
        boolean hasEnoughMoney = false;
        boolean hasEnoughInfluence = false;
        boolean factionHasEnoughPP = false;
        if (p.getMoney() >= mechCbills) {
            hasEnoughMoney = true;
        }
        if (p.getInfluence() >= mechInfluence) {
            hasEnoughInfluence = true;
        }
        if (playerHouse.getPP(weightclass, type_id) >= mechPP) {
            factionHasEnoughPP = true;
        }
        if (hasEnoughMoney && hasEnoughInfluence && factionHasEnoughPP) {
            int spaceTaken = SUnit.getHangarSpaceRequired(type_id, weightclass, 0, "null", p.getMyHouse());
            if (spaceTaken > p.getFreeBays()) {
                needsMoreTechs = true;
            }
            boolean useBays = CampaignMain.cm.isUsingAdvanceRepair();
            if (needsMoreTechs) {
                int techCost = p.getTechHiringFee();
                if (useBays) {
                    techCost = CampaignMain.cm.getIntegerConfig("CostToBuyNewBay");
                }
                int numTechs = spaceTaken - p.getFreeBays();
                techCost = techCost * numTechs;
                int totalCost = techCost + mechCbills;
                StringBuilder toSend = new StringBuilder();
                if (totalCost > p.getMoney()) {
                    toSend.append("AM:Command will not release a new unit to you unless support is in place; however, you cannot afford to buy the unit *and* ");
                    if (useBays) {
                        toSend.append(" purchase the necessary bayspace");
                    } else {
                        toSend.append(" hire technicians");
                    }
                    toSend.append(". The total cost would be " + CampaignMain.cm.moneyOrFluMessage(true, true, totalCost) + ", but you only have " + CampaignMain.cm.moneyOrFluMessage(true, true, p.getMoney()) + ".");
                    CampaignMain.cm.toUser(toSend.toString(), Username, true);
                    return;
                }
                toSend.append("AM:Quartermaster command will not send a new unit to your force until support resources are in place. You will need to ");
                if (useBays) {
                    toSend.append("purchase " + numTechs + " more bays");
                } else {
                    toSend.append("hire " + numTechs + " more technicians");
                }
                toSend.append(" at a cost of " + CampaignMain.cm.moneyOrFluMessage(true, true, techCost) + ". Combined cost of the new unit and necessary ");
                if (useBays) {
                    toSend.append("bays");
                } else {
                    toSend.append("techs");
                }
                toSend.append(" is " + CampaignMain.cm.moneyOrFluMessage(true, true, (mechCbills + techCost)) + " and " + CampaignMain.cm.moneyOrFluMessage(false, true, mechInfluence) + ".");
                toSend.append("<br><a href=\"MEKWARS/c hireandrequestnew#" + numTechs + "#" + Unit.getWeightClassDesc(weightclass) + "#" + type_id + "#" + planet.getName() + "#" + factory.getName() + "\">Click here to purchase both the unit and the needed support.</a>");
                CampaignMain.cm.toUser(toSend.toString(), Username, true);
                return;
            }
            SPilot pilot = playerHouse.getNewPilot(type_id);
            CampaignData.mwlog.dbLog("Pulled pilot: " + pilot.toFileFormat("$", false) + " (DBId: " + pilot.getDBId() + ")");
            if (CampaignMain.cm.isUsingMySQL()) {
                pilot.toDB(type_id, -1);
            }
            CampaignData.mwlog.dbLog("Pilot saved: " + pilot.toFileFormat("$", false) + " (DBId: " + pilot.getDBId() + ")");
            Vector<SUnit> mechs = factory.getMechProduced(type_id, pilot);
            StringBuffer results = new StringBuffer();
            if (playerHouse.getBooleanConfig("UseCalculatedCosts")) {
                mechCbills = 0;
            }
            for (SUnit mech : mechs) {
                if (playerHouse.getBooleanConfig("UseCalculatedCosts")) {
                    double unitCost = mech.getEntity().getCost(false);
                    if (unitCost < 1) {
                        unitCost = playerHouse.getPriceForUnit(mech.getWeightclass(), mech.getType());
                    }
                    double costMod = playerHouse.getDoubleConfig("CostModifier");
                    mechCbills += (int) Math.round(unitCost * costMod);
                    if (mechCbills > p.getMoney()) {
                        CampaignMain.cm.toUser("You could not afford the selected unit. Please try again", Username);
                        return;
                    }
                }
                if (CampaignMain.cm.getBooleanConfig("AllowPersonalPilotQueues") && mech.isSinglePilotUnit()) {
                    SPilot pilot1 = (SPilot) mech.getPilot();
                    SPilot pilot2 = new SPilot("Vacant", 99, 99);
                    mech.setPilot(pilot2);
                    if (!pilot1.getName().equalsIgnoreCase("Vacant")) {
                        playerHouse.getPilotQueues().addPilot(mech.getType(), pilot1, true);
                    }
                }
                p.addUnit(mech, true);
                results.append(mech.getModelName());
                results.append(", ");
                if (CampaignMain.cm.isKeepingUnitHistory()) {
                    CampaignMain.cm.MySQL.addHistoryEntry(HistoryHandler.HISTORY_TYPE_UNIT, mech.getDBId(), HistoryHandler.UNIT_CREATED, mech.getProducer());
                    CampaignMain.cm.MySQL.addHistoryEntry(HistoryHandler.HISTORY_TYPE_UNIT, mech.getDBId(), HistoryHandler.UNIT_BOUGHT_FROM_FACTORY, "Purchased from " + factory.getName() + " on " + factory.getPlanet().getName());
                }
            }
            results.delete(results.length() - 2, results.length());
            p.addMoney(-mechCbills);
            p.addInfluence(-mechInfluence);
            StringBuilder hsUpdates = new StringBuilder();
            hsUpdates.append(factory.addRefresh((CampaignMain.cm.getIntegerConfig(Unit.getWeightClassDesc(factory.getWeightclass()) + "Refresh") * 100) / factory.getRefreshSpeed(), false));
            hsUpdates.append(playerHouse.addPP(weightclass, type_id, -mechPP, false));
            result = "AM:You've been granted the following " + results.toString() + ". (-";
            result += CampaignMain.cm.moneyOrFluMessage(true, false, mechCbills) + " / -" + CampaignMain.cm.moneyOrFluMessage(false, true, mechInfluence) + ")";
            CampaignData.mwlog.mainLog(p.getName() + " bought the following " + results.toString() + " from " + factory.getName() + " on " + planet.getName());
            CampaignMain.cm.toUser(result, Username, true);
            CampaignMain.cm.doSendHouseMail(playerHouse, "NOTE", p.getName() + " bought the following " + results.toString() + " from " + factory.getName() + " on " + planet.getName() + "!");
            CampaignMain.cm.doSendToAllOnlinePlayers(playerHouse, "HS|" + hsUpdates.toString(), false);
            return;
        } else if (!hasEnoughMoney || !hasEnoughInfluence) {
            result = "AM:You need at least " + CampaignMain.cm.moneyOrFluMessage(true, false, mechCbills) + " and " + CampaignMain.cm.moneyOrFluMessage(false, true, mechInfluence) + " to request a " + Unit.getTypeClassDesc(type_id) + " of this weight class from a factory.";
            CampaignMain.cm.toUser(result, Username, true);
            return;
        } else if (!factionHasEnoughPP) {
            result = "AM:Your faction does not have the components needed to produce such a unit at this time. Wait for your faction to gather more resources.";
            CampaignMain.cm.toUser(result, Username, true);
            return;
        }
    }

    /**
     * Private method which builds a welfare unit. Duplicated in RequestCommand.
     * Kept private in these classes in order to ensure that ONLY requests
     * generate welfare units (had previously been a public call in
     * CampaignMain).
     */
    private SUnit buildWelfareMek(String producer) {
        String Filename = "./data/buildtables/standard/" + producer + "_Welfare.txt";
        if (!(new File(Filename).exists())) {
            Filename = "./data/buildtables/standard/Welfare.txt";
        }
        String unitFileName = BuildTable.getUnitFilename(Filename);
        SUnit cm = new SUnit(producer, unitFileName, Unit.LIGHT);
        return cm;
    }
}
