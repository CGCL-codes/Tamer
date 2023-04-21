package server.campaign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;
import megamek.common.AmmoType;
import megamek.common.BattleArmor;
import megamek.common.CriticalSlot;
import megamek.common.Entity;
import megamek.common.EntityListFile;
import megamek.common.Infantry;
import megamek.common.Mech;
import megamek.common.MechFileParser;
import megamek.common.MechSummary;
import megamek.common.MechSummaryCache;
import megamek.common.Mounted;
import megamek.common.Pilot;
import megamek.common.Tank;
import megamek.common.WeaponType;
import megamek.common.options.PilotOptions;
import server.campaign.operations.Operation;
import server.campaign.pilot.SPilot;
import server.campaign.pilot.SPilotSkills;
import server.campaign.pilot.skills.SPilotSkill;
import server.campaign.pilot.skills.WeaponSpecialistSkill;
import server.campaign.util.SerializedMessage;
import server.mwmysql.JDBCConnectionHandler;
import common.CampaignData;
import common.MegaMekPilotOption;
import common.Unit;
import common.campaign.pilot.skills.PilotSkill;
import common.campaign.targetsystems.TargetSystem;
import common.campaign.targetsystems.TargetTypeNotImplementedException;
import common.campaign.targetsystems.TargetTypeOutOfBoundsException;
import common.util.TokenReader;
import common.util.UnitUtils;

/**
 * A class representing an MM.Net Entity
 * 
 * @author Helge Richter (McWizard) Jun 10/04 - Dave Poole added an overloaded
 *         constructor to allow creation of a new SUnit with the same UnitID as
 *         an existing Mech to facilitate repodding
 */
public final class SUnit extends Unit implements Comparable<SUnit> {

    private Integer BV = 0;

    private Integer scrappableFor = new Integer(-1);

    private long passesMaintainanceUntil = 0;

    private boolean pilotIsRepairing = false;

    private Entity unitEntity = null;

    private int lastCombatPilot = -1;

    private boolean is_saving = false;

    private JDBCConnectionHandler ch = new JDBCConnectionHandler();

    /**
     * For Serialization.
     */
    public SUnit() {
        super();
    }

    /**
     * Construct a new unit.
     * 
     * @param p
     *            flavour string (es: Built by Kurita on An-Ting)
     * @param filename
     *            to read this entity from
     */
    public SUnit(String p, String Filename, int weightclass) {
        super();
        int gunnery = 4;
        int piloting = 5;
        SHouse house = CampaignMain.cm.getHouseFromPartialString(p, null);
        setUnitFilename(Filename);
        init();
        if (house != null) {
            setPilot(house.getNewPilot(getType()));
        } else {
            setPilot(new SPilot(SPilot.getRandomPilotName(CampaignMain.cm.getR()), gunnery, piloting));
        }
        setWeightclass(weightclass);
        setProducer(p);
        setId(CampaignMain.cm.getAndUpdateCurrentUnitID());
    }

    /**
     * Constructs a new Unit with the id for an existing unit (repod)
     * 
     * @param p
     *            - flavour string (es: Built by Kurita on An-Ting)
     * @param Filename
     *            - filename to read this entity from
     * @param weightclass
     *            - int defining weightclass
     * @param replaceId
     *            - unitID to assign a new SUnit
     */
    public SUnit(int replaceId, String p, String Filename) {
        super();
        setUnitFilename(Filename);
        Entity ent = SUnit.loadMech(getUnitFilename());
        setEntity(ent);
        init();
        setPilot(new SPilot("Vacant", 99, 99));
        setId(replaceId);
        setProducer(p);
        unitEntity = ent;
    }

    /**
     * Method which checks a unit for illegal ammo and replaces it with default
     * ammo loads. useful for removing faction banned ammo from salvage. Note
     * that this is primarily designed to strip L2 ammo from L2 units (eg -
     * precision AC) and replace it with normal ammo. L3 ammos may lead to some
     * oddities and should be banned or allowed server wide rather than on a
     * house-by-house basis.
     * 
     * @param u
     *            - unit to check
     * @param h
     *            - SHouse unit is joining
     */
    public static void checkAmmoForUnit(SUnit u, SHouse h) {
        Entity en = u.getEntity();
        boolean wasChanged = false;
        for (Mounted mAmmo : en.getAmmo()) {
            AmmoType at = (AmmoType) mAmmo.getType();
            String munition = Long.toString(at.getMunitionType());
            if (at.getAmmoType() == AmmoType.T_ATM) {
                continue;
            }
            if (at.getAmmoType() == AmmoType.T_AC_LBX) {
                continue;
            }
            if (at.getAmmoType() == AmmoType.T_SRM_STREAK) {
                continue;
            }
            if (at.getAmmoType() == AmmoType.T_LRM_STREAK) {
                continue;
            }
            if (at.getAmmoType() == AmmoType.M_STANDARD) {
                continue;
            }
            if (CampaignMain.cm.getData().getServerBannedAmmo().containsKey(munition) || h.getBannedAmmo().containsKey(munition)) {
                Vector<AmmoType> types = AmmoType.getMunitionsFor(at.getAmmoType());
                Enumeration<AmmoType> allTypes = types.elements();
                boolean defaultFound = false;
                while (allTypes.hasMoreElements() && !defaultFound) {
                    AmmoType currType = allTypes.nextElement();
                    if ((currType.getTechLevel() <= en.getTechLevel()) && (currType.getMunitionType() == AmmoType.M_STANDARD) && (currType.getRackSize() == at.getRackSize())) {
                        mAmmo.changeAmmoType(currType);
                        mAmmo.setShotsLeft(at.getShots());
                        defaultFound = true;
                        wasChanged = true;
                    }
                }
            }
        }
        if (wasChanged) {
            u.setEntity(en);
        }
    }

    /**
     * Method which determines whether or not a given unit may be sold on the
     * black market. Any "false" return prevents house listings as well as
     * player sales.
     */
    public static boolean mayBeSoldOnMarket(SUnit u) {
        if ((u.getType() == Unit.BATTLEARMOR) && !CampaignMain.cm.getBooleanConfig("BAMayBeSoldOnBM")) {
            return false;
        } else if ((u.getType() == Unit.PROTOMEK) && !CampaignMain.cm.getBooleanConfig("ProtosMayBeSoldOnBM")) {
            return false;
        } else if ((u.getType() == Unit.AERO) && !CampaignMain.cm.getBooleanConfig("AerosMayBeSoldOnBM")) {
            return false;
        } else if ((u.getType() == Unit.INFANTRY) && !CampaignMain.cm.getBooleanConfig("InfantryMayBeSoldOnBM")) {
            return false;
        } else if ((u.getType() == Unit.VEHICLE) && !CampaignMain.cm.getBooleanConfig("VehsMayBeSoldOnBM")) {
            return false;
        } else if (((u.getType() == Unit.MEK) || (u.getType() == Unit.QUAD)) && !CampaignMain.cm.getBooleanConfig("MeksMayBeSoldOnBM")) {
            return false;
        }
        return true;
    }

    /**
     * Return the number of techs/bays required for a unit of given size/type.
     */
    public static int getHangarSpaceRequired(int typeid, int weightclass, int baymod, String model, SHouse faction) {
        if (typeid == Unit.PROTOMEK) {
            return 0;
        }
        if ((typeid == Unit.INFANTRY) && CampaignMain.cm.getBooleanConfig("FootInfTakeNoBays")) {
            boolean isFoot = model.startsWith("Foot");
            boolean isAMFoot = model.startsWith("Anti-Mech Foot");
            if (isFoot || isAMFoot) {
                return 0;
            }
        }
        int result = 1;
        String techAmount = "TechsFor" + Unit.getWeightClassDesc(weightclass) + Unit.getTypeClassDesc(typeid);
        if (faction != null) {
            result = faction.getIntegerConfig(techAmount);
        } else {
            result = CampaignMain.cm.getIntegerConfig(techAmount);
        }
        if (!CampaignMain.cm.isUsingAdvanceRepair()) {
            result += baymod;
        }
        if (result < 0) {
            result = 0;
        }
        return result;
    }

    public static int getHangarSpaceRequired(int typeid, int weightclass, int baymod, String model, boolean unitSupported, SHouse faction) {
        if (unitSupported) {
            return SUnit.getHangarSpaceRequired(typeid, weightclass, baymod, model, faction);
        }
        return (int) (SUnit.getHangarSpaceRequired(typeid, weightclass, baymod, model, faction) * CampaignMain.cm.getFloatConfig("NonFactionUnitsIncreasedTechs"));
    }

    /**
     * Pass-through method that gets the number of bays/techs required for a
     * given unit by drawing its characteristics and feeding them to
     * getHangarSpaceRequired(int,int,int,String).
     */
    public static int getHangarSpaceRequired(SUnit u, SHouse faction) {
        return SUnit.getHangarSpaceRequired(u.getType(), u.getWeightclass(), u.getPilot().getBayModifier(), u.getModelName(), faction);
    }

    public static int getHangarSpaceRequired(SUnit u, boolean unitSupported, SHouse faction) {
        if (unitSupported) {
            return SUnit.getHangarSpaceRequired(u.getType(), u.getWeightclass(), u.getPilot().getBayModifier(), u.getModelName(), faction);
        }
        return SUnit.getHangarSpaceRequired(u.getType(), u.getWeightclass(), u.getPilot().getBayModifier(), u.getModelName(), unitSupported, faction);
    }

    /**
     * Simple static method that access configs and returns a unit's influence
     * on map size. Called by ShortOperation when changing status from Waiting
     * -> In_Progress.
     * 
     * @return - configured map weighting
     */
    public static int getMapSizeModification(SUnit u) {
        if (u.getType() == Unit.VEHICLE) {
            return CampaignMain.cm.getIntegerConfig("VehicleMapSizeFactor");
        }
        if (u.getType() == Unit.INFANTRY) {
            return CampaignMain.cm.getIntegerConfig("InfantryMapSizeFactor");
        }
        if (u.getType() == Unit.MEK) {
            return CampaignMain.cm.getIntegerConfig("MekMapSizeFactor");
        }
        if (u.getType() == Unit.BATTLEARMOR) {
            return CampaignMain.cm.getIntegerConfig("BattleArmorMapSizeFactor");
        }
        if (u.getType() == Unit.AERO) {
            return CampaignMain.cm.getIntegerConfig("AeroMapSizeFactor");
        }
        if (u.getType() == Unit.PROTOMEK) {
            return CampaignMain.cm.getIntegerConfig("ProtoMekMapSizeFactor");
        }
        return 0;
    }

    public static double getArmorCost(Entity unit, int location) {
        double cost = 0.0;
        if (CampaignMain.cm.getBooleanConfig("UsePartsRepair")) {
            return 0;
        }
        String armorCost = "CostPoint" + UnitUtils.getArmorShortName(unit, location);
        cost = CampaignMain.cm.getDoubleConfig(armorCost);
        return cost;
    }

    public static double getStructureCost(Entity unit) {
        double cost = 0.0;
        if (CampaignMain.cm.getBooleanConfig("UsePartsRepair")) {
            return 0;
        }
        String armorCost = "CostPoint" + UnitUtils.getInternalShortName(unit) + "IS";
        cost = CampaignMain.cm.getDoubleConfig(armorCost);
        return cost;
    }

    public static double getCritCost(Entity unit, CriticalSlot crit) {
        double cost = 0.0;
        if (CampaignMain.cm.getBooleanConfig("UsePartsRepair")) {
            return 0;
        }
        if (crit == null) {
            return 0;
        }
        if (crit.isBreached() && !crit.isDamaged()) {
            return 0;
        }
        if (UnitUtils.isEngineCrit(crit)) {
            cost = CampaignMain.cm.getDoubleConfig("EngineCritRepairCost");
        } else if (crit.getType() == CriticalSlot.TYPE_SYSTEM) {
            if (crit.isMissing()) {
                cost = CampaignMain.cm.getDoubleConfig("SystemCritReplaceCost");
            } else {
                cost = CampaignMain.cm.getDoubleConfig("SystemCritRepairCost");
            }
        } else {
            Mounted mounted = unit.getEquipment(crit.getIndex());
            if (mounted.getType() instanceof WeaponType) {
                WeaponType weapon = (WeaponType) mounted.getType();
                if (weapon.hasFlag(WeaponType.F_ENERGY)) {
                    if (crit.isMissing()) {
                        cost = CampaignMain.cm.getDoubleConfig("EnergyWeaponCritReplaceCost");
                    } else {
                        cost = CampaignMain.cm.getDoubleConfig("EnergyWeaponCritRepairCost");
                    }
                } else if (weapon.hasFlag(WeaponType.F_BALLISTIC)) {
                    if (crit.isMissing()) {
                        cost = CampaignMain.cm.getDoubleConfig("BallisticCritReplaceCost");
                    } else {
                        cost = CampaignMain.cm.getDoubleConfig("BallisticCritRepairCost");
                    }
                } else if (weapon.hasFlag(WeaponType.F_MISSILE)) {
                    if (crit.isMissing()) {
                        cost = CampaignMain.cm.getDoubleConfig("MissileCritReplaceCost");
                    } else {
                        cost = CampaignMain.cm.getDoubleConfig("MissileCritRepairCost");
                    }
                } else if (crit.isMissing()) {
                    cost = CampaignMain.cm.getDoubleConfig("EquipmentCritReplaceCost");
                } else {
                    cost = CampaignMain.cm.getDoubleConfig("EquipmentCritRepairCost");
                }
            } else if (crit.isMissing()) {
                cost = CampaignMain.cm.getDoubleConfig("EquipmentCritReplaceCost");
            } else {
                cost = CampaignMain.cm.getDoubleConfig("EquipmentCritRepairCost");
            }
        }
        cost = Math.max(cost, 1);
        return cost;
    }

    /**
     * @return the Serialized Version of this entity
     */
    public String toString(boolean toPlayer) {
        SerializedMessage msg = new SerializedMessage("$");
        if (toPlayer) {
            setBV(0);
            getBV();
        }
        msg.append("CM");
        msg.append(getUnitFilename());
        msg.append(getPosId());
        msg.append(getStatus());
        msg.append(getProducer());
        msg.append(((SPilot) getPilot()).toFileFormat("#", toPlayer));
        if (toPlayer) {
            LinkedList<MegaMekPilotOption> mmoptions = getPilot().getMegamekOptions();
            msg.append(mmoptions.size());
            Iterator<MegaMekPilotOption> i = mmoptions.iterator();
            while (i.hasNext()) {
                MegaMekPilotOption mmo = i.next();
                msg.append(mmo.getMmname());
                msg.append(mmo.isValue());
            }
            msg.append(getType());
            msg.append(getBV());
        }
        msg.append(getWeightclass());
        msg.append(getId());
        if (getModelName().equals("OMG-UR-FD")) {
            return msg.getMessage();
        }
        if (getEntity() instanceof Mech) {
            unitEntity = getEntity();
            msg.append(((Mech) unitEntity).isAutoEject());
        }
        ArrayList<Mounted> en_Ammo = unitEntity.getAmmo();
        msg.append(en_Ammo.size());
        for (Mounted mAmmo : en_Ammo) {
            boolean hotloaded = mAmmo.isHotLoaded();
            if (!CampaignMain.cm.getMegaMekClient().game.getOptions().booleanOption("tacops_hotload")) {
                hotloaded = false;
            }
            AmmoType at = (AmmoType) mAmmo.getType();
            msg.append(at.getAmmoType());
            msg.append(at.getInternalName());
            msg.append(mAmmo.getShotsLeft());
            msg.append(hotloaded);
        }
        if ((unitEntity instanceof Mech) || (unitEntity instanceof Tank)) {
            int mgCount = CampaignMain.cm.getMachineGunCount(unitEntity.getWeaponList());
            msg.append(mgCount);
            if (mgCount > 0) {
                for (int location = 0; location < unitEntity.locations(); location++) {
                    for (int slot = 0; slot < unitEntity.getNumberOfCriticals(location); slot++) {
                        CriticalSlot crit = unitEntity.getCritical(location, slot);
                        if ((crit == null) || (crit.getType() != CriticalSlot.TYPE_EQUIPMENT)) {
                            continue;
                        }
                        Mounted m = unitEntity.getEquipment(crit.getIndex());
                        if ((m == null) || !(m.getType() instanceof WeaponType)) {
                            continue;
                        }
                        WeaponType wt = (WeaponType) m.getType();
                        if (!wt.hasFlag(WeaponType.F_MG)) {
                            continue;
                        }
                        msg.append(location);
                        msg.append(slot);
                        msg.append(m.isRapidfire());
                    }
                }
            }
        } else {
            msg.append(0);
        }
        msg.append(0);
        msg.append(targetSystem.getCurrentType());
        msg.append(isSupportUnit() ? "1" : "0");
        msg.append(getScrappableFor());
        if (CampaignMain.cm.isUsingAdvanceRepair()) {
            msg.append(UnitUtils.unitBattleDamage(getEntity(), false));
        } else {
            msg.append("%%-%%-%%-");
        }
        if (toPlayer) {
            msg.append(getPilotIsReparing());
        }
        if (!toPlayer) {
            msg.append(getLastCombatPilot());
        }
        msg.append(getCurrentRepairCost());
        msg.append(getLifeTimeRepairCost());
        if (CampaignMain.cm.isUsingMySQL() && !toPlayer) {
            msg.append(getDBId());
        }
        return msg.getMessage();
    }

    public void toDB() {
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer();
        ResultSet rs = null;
        if (is_saving) return;
        is_saving = true;
        Connection c = ch.getConnection();
        try {
            if (getDBId() == 0) {
                sql.setLength(0);
                sql.append("INSERT into units set MWID=?, uFileName=?, uWeightClass=?, uType=?");
                ps = c.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, getId());
                ps.setString(2, getUnitFilename());
                ps.setInt(3, getWeightclass());
                ps.setInt(4, getType());
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                rs.next();
                setDBId(rs.getInt(1));
                rs.close();
            } else {
                sql.setLength(0);
                sql.append("UPDATE units set uFileName=?, uWeightClass=?, uType = ?, MWID=? WHERE ID = ?");
                ps = c.prepareStatement(sql.toString());
                ps.setString(1, getUnitFilename());
                ps.setInt(2, getWeightclass());
                ps.setInt(3, getType());
                ps.setInt(4, getId());
                ps.setInt(5, getDBId());
                ps.executeUpdate();
            }
            ps.close();
        } catch (SQLException e) {
            CampaignData.mwlog.dbLog("SQL Exception in SUnit.toDB: " + e.getMessage());
            CampaignData.mwlog.dbLog(e);
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException ex) {
            }
        } finally {
            is_saving = false;
            ch.returnConnection(c);
        }
    }

    /**
     * Reads a Entity from a String
     * 
     * @param s
     *            A string to read from
     * @return the remaining String
     */
    public String fromString(String s) {
        try {
            s = s.substring(3);
            StringTokenizer ST = new StringTokenizer(s, "$");
            setUnitFilename(TokenReader.readString(ST));
            setPosId(TokenReader.readInt(ST));
            int newstate = TokenReader.readInt(ST);
            setProducer(TokenReader.readString(ST));
            SPilot p = new SPilot();
            p.fromFileFormat(TokenReader.readString(ST), "#");
            setWeightclass(TokenReader.readInt(ST));
            setId(TokenReader.readInt(ST));
            if (CampaignMain.cm.getCurrentUnitID() <= getId()) {
                CampaignMain.cm.setCurrentUnitID(getId() + 1);
            }
            if (getId() == 0) {
                setId(CampaignMain.cm.getAndUpdateCurrentUnitID());
            }
            if ((newstate == STATUS_FORSALE) && (CampaignMain.cm.getMarket().getListingForUnit(getId()) == null)) {
                setStatus(STATUS_OK);
            } else if (CampaignMain.cm.isUsingAdvanceRepair()) {
                setStatus(STATUS_OK);
            } else {
                setStatus(newstate);
            }
            unitEntity = SUnit.loadMech(getUnitFilename());
            setEntity(unitEntity);
            init();
            this.setPilot(p);
            if (getModelName().equals("OMG-UR-FD")) {
                return s;
            }
            if (unitEntity instanceof Mech) {
                ((Mech) unitEntity).setAutoEject(TokenReader.readBoolean(ST));
            }
            String defaultField = "0";
            if (ST.hasMoreElements()) {
                Entity en = getEntity();
                int maxCrits = TokenReader.readInt(ST);
                defaultField = TokenReader.readString(ST);
                ArrayList<Mounted> e = en.getAmmo();
                for (int count = 0; count < maxCrits; count++) {
                    int weaponType = Integer.parseInt(defaultField);
                    String ammoName = TokenReader.readString(ST);
                    int shots = TokenReader.readInt(ST);
                    boolean hotloaded = false;
                    try {
                        defaultField = TokenReader.readString(ST);
                        hotloaded = Boolean.parseBoolean(defaultField);
                        defaultField = TokenReader.readString(ST);
                    } catch (Exception ex) {
                        hotloaded = false;
                    }
                    if (!CampaignMain.cm.getMegaMekClient().game.getOptions().booleanOption("tacops_hotload")) {
                        hotloaded = false;
                    }
                    Mounted mWeapon = e.get(count);
                    AmmoType at = getEntityAmmo(weaponType, ammoName);
                    if (at == null) {
                        continue;
                    }
                    String munition = Long.toString(at.getMunitionType());
                    if (CampaignMain.cm.getData().getServerBannedAmmo().get(munition) != null) {
                        continue;
                    }
                    mWeapon.changeAmmoType(at);
                    mWeapon.setShotsLeft(shots);
                    mWeapon.setHotLoad(hotloaded);
                }
                setEntity(en);
            }
            int maxMachineGuns = Integer.parseInt(defaultField);
            Entity en = getEntity();
            for (int count = 0; count < maxMachineGuns; count++) {
                int location = TokenReader.readInt(ST);
                int slot = TokenReader.readInt(ST);
                boolean selection = TokenReader.readBoolean(ST);
                try {
                    CriticalSlot cs = en.getCritical(location, slot);
                    Mounted m = en.getEquipment(cs.getIndex());
                    m.setRapidfire(selection);
                } catch (Exception ex) {
                }
            }
            setEntity(en);
            targetSystem.setEntity(en);
            TokenReader.readString(ST);
            int tsType = TokenReader.readInt(ST);
            if (tsType != TargetSystem.TS_TYPE_STANDARD && CampaignData.cd.targetSystemIsBanned(tsType)) {
                tsType = TargetSystem.TS_TYPE_STANDARD;
            }
            targetSystem.setTargetSystem(tsType);
            TokenReader.readInt(ST);
            if (CampaignMain.cm.getSupportUnits().contains(getUnitFilename().trim().toLowerCase())) {
                setSupportUnit(true);
            } else {
                setSupportUnit(false);
            }
            setScrappableFor(TokenReader.readInt(ST));
            if (CampaignMain.cm.isUsingAdvanceRepair() && ((unitEntity instanceof Mech) || (unitEntity instanceof Tank))) {
                UnitUtils.applyBattleDamage(unitEntity, TokenReader.readString(ST), ((CampaignMain.cm.getRTT() != null) && (CampaignMain.cm.getRTT().unitRepairTimes(getId()) != null)));
            } else {
                TokenReader.readString(ST);
            }
            setLastCombatPilot(TokenReader.readInt(ST));
            setRepairCosts(TokenReader.readInt(ST), TokenReader.readInt(ST));
            if (CampaignMain.cm.isUsingMySQL() && ST.hasMoreTokens()) {
                setDBId(TokenReader.readInt(ST));
            }
            return s;
        } catch (Exception ex) {
            CampaignData.mwlog.errLog(ex);
            CampaignData.mwlog.errLog("Unable to Load SUnit: " + s);
            return s;
        }
    }

    public void fromDB(int unitID) {
        ResultSet rs = null;
        ResultSet ammoRS = null;
        ResultSet mgRS = null;
        Statement ammoStmt = null;
        Statement mgStmt = null;
        Statement stmt = null;
        Connection c = ch.getConnection();
        try {
            ammoStmt = c.createStatement();
            mgStmt = c.createStatement();
            stmt = c.createStatement();
            ammoRS = ammoStmt.executeQuery("SELECT * from unit_ammo WHERE unitID = " + unitID + " ORDER BY ammoLocation");
            mgRS = mgStmt.executeQuery("SELECT * from unit_mgs WHERE unitID = " + unitID + " ORDER BY mgLocation");
            rs = stmt.executeQuery("SELECT * from units WHERE ID = " + unitID);
            if (rs.next()) {
                setUnitFilename(rs.getString("uFileName"));
                setPosId(rs.getInt("uPosID"));
                int newstate = rs.getInt("uStatus");
                setProducer(rs.getString("uProducer"));
                setWeightclass(rs.getInt("uWeightClass"));
                setId(rs.getInt("MWID"));
                setDBId(unitID);
                if (CampaignMain.cm.getCurrentUnitID() <= getId()) CampaignMain.cm.setCurrentUnitID(getId() + 1);
                if (getId() == 0) setId(CampaignMain.cm.getAndUpdateCurrentUnitID());
                if (newstate == Unit.STATUS_FORSALE && CampaignMain.cm.getMarket().getListingForUnit(getId()) == null) setStatus(Unit.STATUS_OK); else if (CampaignMain.cm.isUsingAdvanceRepair()) setStatus(Unit.STATUS_OK); else setStatus(newstate);
                setScrappableFor(rs.getInt("uScrappableFor"));
                setRepairCosts(rs.getInt("uCurrentRepairCost"), rs.getInt("uLifetimeRepairCost"));
                Entity uEntity = loadMech(getUnitFilename());
                if (uEntity instanceof Mech) ((Mech) uEntity).setAutoEject(rs.getBoolean("uAutoEject"));
                uEntity.setSpotlight(rs.getBoolean("uHasSpotlight"));
                uEntity.setSpotlightState(rs.getBoolean("uIsUsingSpotlight"));
                if (CampaignMain.cm.isUsingAdvanceRepair() && (uEntity instanceof Mech || uEntity instanceof Tank)) UnitUtils.applyBattleDamage(uEntity, rs.getString("uBattleDamage"), (CampaignMain.cm.getRTT() != null & CampaignMain.cm.getRTT().unitRepairTimes(getId()) != null));
                setEntity(uEntity);
                SPilot p = CampaignMain.cm.MySQL.loadUnitPilot(unitID);
                setPilot(p);
                setLastCombatPilot(p.getPilotId());
                init();
                unitEntity = getEntity();
                while (ammoRS.next()) {
                    int weaponType = ammoRS.getInt("ammoType");
                    String ammoName = ammoRS.getString("ammoInternalName");
                    int shots = ammoRS.getInt("ammoShotsLeft");
                    int AmmoLoc = ammoRS.getInt("ammoLocation");
                    boolean hotloaded = Boolean.parseBoolean(ammoRS.getString("ammoHotLoaded"));
                    if (!CampaignMain.cm.getMegaMekClient().game.getOptions().booleanOption("tacops_hotload")) hotloaded = false;
                    AmmoType at = getEntityAmmo(weaponType, ammoName);
                    String munition = Long.toString(at.getMunitionType());
                    if (CampaignMain.cm.getData().getServerBannedAmmo().get(munition) != null) continue;
                    try {
                        unitEntity.getAmmo().get(AmmoLoc).changeAmmoType(at);
                        unitEntity.getAmmo().get(AmmoLoc).setShotsLeft(shots);
                        unitEntity.getAmmo().get(AmmoLoc).setHotLoad(hotloaded);
                    } catch (Exception ex) {
                        CampaignData.mwlog.dbLog("Exception: " + ex.toString());
                        CampaignData.mwlog.dbLog(ex.getStackTrace().toString());
                    }
                }
                setEntity(unitEntity);
                Entity en = this.getEntity();
                while (mgRS.next()) {
                    int location = mgRS.getInt("mgLocation");
                    int slot = mgRS.getInt("mgSlot");
                    boolean selection = mgRS.getBoolean("mgRapidFire");
                    try {
                        CriticalSlot cs = en.getCritical(location, slot);
                        Mounted m = en.getEquipment(cs.getIndex());
                        m.setRapidfire(selection);
                    } catch (Exception ex) {
                    }
                }
                setEntity(unitEntity);
            }
            rs.close();
            mgRS.close();
            ammoRS.close();
            stmt.close();
            mgStmt.close();
            ammoStmt.close();
        } catch (SQLException e) {
            CampaignData.mwlog.dbLog("SQL Error in SUnit.fromDB: " + e.getMessage());
            CampaignData.mwlog.dbLog(e);
            try {
                if (rs != null) rs.close();
                if (mgRS != null) mgRS.close();
                if (ammoRS != null) ammoRS.close();
                if (stmt != null) stmt.close();
                if (mgStmt != null) mgStmt.close();
                if (ammoStmt != null) ammoStmt.close();
            } catch (SQLException ex) {
            }
        } finally {
            ch.returnConnection(c);
        }
    }

    /**
     * @return a description of the entity including pilot
     */
    public String getDescription(boolean showLink) {
        String status = "";
        if (CampaignMain.cm.isUsingAdvanceRepair()) {
            if (UnitUtils.hasCriticalDamage(getEntity())) {
                status = "Is Critically Damaged";
            } else if (UnitUtils.hasArmorDamage(getEntity())) {
                status = "Has Minor Armor Damage";
            } else if (UnitUtils.isRepairing(getEntity())) {
                status = "Is Currently Under Going Repairs";
            } else {
                status = "Is Fully Functional";
            }
        } else {
            if (getStatus() == Unit.STATUS_UNMAINTAINED) {
                status = "Unmaintained" + " (" + getMaintainanceLevel() + "%)";
            } else {
                status = "Maintained" + " (" + getMaintainanceLevel() + "%)";
            }
        }
        String idToShow = "";
        if (showLink) {
            idToShow = "<a href=\"MEKWARS/c sth#u#" + getId() + "\">#" + getId() + "</a>";
        } else {
            idToShow = "#" + getId();
        }
        String dialogBox = "<a href=\"MEKINFO" + getEntity().getChassis() + " " + getEntity().getModel().replace("\"", "%22") + "#" + getBVForMatch() + "#" + getPilot().getGunnery() + "#" + getPilot().getPiloting() + "\">" + getModelName() + "</a>";
        if ((getType() == Unit.MEK) || (getType() == Unit.VEHICLE)) {
            return idToShow + " " + dialogBox + " (" + getPilot().getGunnery() + "/" + getPilot().getPiloting() + ") [" + getPilot().getExperience() + " EXP " + getPilot().getSkillString(false) + "] Kills: " + getPilot().getKills() + " " + getProducer() + ". BV: " + getBVForMatch() + " " + status;
        }
        if ((getType() == Unit.INFANTRY) || (getType() == Unit.BATTLEARMOR)) {
            if (((Infantry) getEntity()).isAntiMek()) {
                return idToShow + " " + dialogBox + " (" + getPilot().getGunnery() + "/" + getPilot().getPiloting() + ") [" + getPilot().getExperience() + " EXP " + getPilot().getSkillString(false) + "] Kills: " + getPilot().getKills() + " " + getProducer() + ". BV: " + getBVForMatch() + " " + status;
            }
        }
        return idToShow + " " + dialogBox + " (" + getPilot().getGunnery() + ") [" + getPilot().getExperience() + " EXP " + getPilot().getSkillString(false) + "] Kills: " + getPilot().getKills() + " " + getProducer() + ". BV: " + getBVForMatch() + " " + status;
    }

    /**
     * @return a smaller description
     */
    public String getSmallDescription() {
        String result;
        if ((getType() == Unit.MEK) || (getType() == Unit.VEHICLE) || (getType() == Unit.AERO)) {
            result = getModelName() + " [" + getPilot().getGunnery() + "/" + getPilot().getPiloting();
        } else if ((getType() == Unit.INFANTRY) || (getType() == Unit.BATTLEARMOR)) {
            if (((Infantry) getEntity()).isAntiMek()) {
                result = getModelName() + " [" + getPilot().getGunnery() + "/" + getPilot().getPiloting();
            } else {
                result = getModelName() + " [" + getPilot().getGunnery();
            }
        } else {
            result = getModelName() + " [" + getPilot().getGunnery();
        }
        if (!getPilot().getSkillString(true).equals(" ")) {
            result += getPilot().getSkillString(true);
        }
        result += "]";
        return result;
    }

    /**
     * Returns the Modelname for this Unit
     * 
     * @return the Modelname
     */
    public String getModelName() {
        if (checkModelName() == null) {
            unitEntity = SUnit.loadMech(getUnitFilename());
            init();
        }
        return checkModelName();
    }

    public String getVerboseModelName() {
        if ((getType() == Unit.MEK) || (getType() == Unit.VEHICLE) || (getType() == Unit.AERO)) {
            return getModelName() + " (" + getPilot().getGunnery() + "/" + getPilot().getPiloting() + ")";
        }
        if ((getType() == Unit.INFANTRY) || (getType() == Unit.BATTLEARMOR)) {
            if (((Infantry) getEntity()).isAntiMek()) {
                return getModelName() + " (" + getPilot().getGunnery() + "/" + getPilot().getPiloting() + ")";
            }
        }
        return getModelName() + " (" + getPilot().getGunnery() + ")";
    }

    /**
     * @return the BV of this entity including all modifications
     */
    public int calcBV() {
        try {
            if (hasVacantPilot()) {
                getEntity().getCrew().setGunnery(4);
                getEntity().getCrew().setPiloting(5);
            } else {
                getEntity().setCrew(UnitUtils.createEntityPilot(this));
            }
            int calcedBV = getEntity().calculateBattleValue();
            int FastHoverBVMod = CampaignMain.cm.getIntegerConfig("FastHoverBVMod");
            if ((FastHoverBVMod > 0) && (getType() == Unit.VEHICLE) && (getEntity().getMovementMode() == megamek.common.EntityMovementMode.HOVER)) {
                if (getEntity().getWalkMP() >= 8) {
                    calcedBV += FastHoverBVMod;
                }
            }
            if (CampaignMain.cm.getBooleanConfig("ElitePilotsBVMod")) {
                if (getPilot().getGunnery() < 3) {
                    calcedBV = (int) Math.round(calcedBV * 1.05);
                } else if (getPilot().getPiloting() < 3) {
                    calcedBV = (int) Math.round(calcedBV * 1.05);
                }
            }
            calcedBV += getPilotSkillBV();
            if (hasVacantPilot()) {
                getEntity().getCrew().setGunnery(99);
                getEntity().getCrew().setPiloting(99);
            }
            return calcedBV;
        } catch (Exception ex) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean equals(Object o) {
        SUnit m = null;
        try {
            m = (SUnit) o;
        } catch (ClassCastException e) {
            return false;
        }
        if (m == null) {
            return false;
        }
        if ((m.getId() == getId()) && m.getUnitFilename().equals(getUnitFilename()) && (m.getPilot().getGunnery() == getPilot().getGunnery()) && (m.getPilot().getPiloting() == getPilot().getPiloting())) {
            return true;
        }
        return false;
    }

    /**
     * Sets the Pilot of this entity
     * 
     * @param p
     *            A pilot
     */
    public void setPilot(SPilot p) {
        setBV(0);
        if (p == null) {
            return;
        }
        Pilot mPilot = new Pilot(p.getName(), p.getGunnery(), p.getPiloting());
        Entity entity = getEntity();
        if ((getEntity() instanceof BattleArmor) && !((BattleArmor) getEntity()).isAntiMek() && !hasVacantPilot()) {
            mPilot.setPiloting(5);
        }
        entity.setCrew(mPilot);
        setEntity(entity);
        if (p.getSkills().has(PilotSkill.WeaponSpecialistSkillID)) {
            Iterator<PilotSkill> ski = p.getSkills().getSkillIterator();
            while (ski.hasNext()) {
                SPilotSkill skill = (SPilotSkill) ski.next();
                if (skill.getName().equals("Weapon Specialist") && p.getWeapon().equals("Default")) {
                    p.getSkills().remove(skill);
                    ((WeaponSpecialistSkill) skill).assignWeapon(getEntity(), p);
                    skill.addToPilot(p);
                    skill.modifyPilot(p);
                    break;
                }
            }
        }
        p.setUnitType(getType());
        super.setPilot(p);
        if (CampaignMain.cm.isUsingMySQL()) {
            toDB();
        }
    }

    public void init() {
        setType(Unit.getEntityType(getEntity()));
        if ((getType() != Unit.MEK) || getEntity().isOmni()) {
            setModelname(new String(unitEntity.getChassis() + " " + unitEntity.getModel()).trim());
        } else {
            if (unitEntity.getModel().trim().length() > 0) {
                setModelname(unitEntity.getModel().trim());
            } else {
                setModelname(unitEntity.getChassis().trim());
            }
        }
        getC3Type(unitEntity);
        if (getModelName().equals("OMG-UR-FD")) {
            setProducer("Error loading unit. Tried to build from " + getUnitFilename());
            setWeightclass(Unit.LIGHT);
        }
    }

    /**
     * Sets status to unmaintained. Factors out repetetive code checking
     * maintainance status and decreasing as unit is moved to unmaintained.
     * Called from both Player and SetUnmaintainedCommand. It would possible to
     * bypass this code and set a unit as unmaintained without incurring any
     * maintainance penalty w/ Unit.setStatus(STATUS_UNMAINTAINED).
     * 
     * @urgru 8/4/04
     */
    public void setUnmaintainedStatus() {
        if (CampaignMain.cm.isUsingAdvanceRepair()) {
            setStatus(STATUS_OK);
            return;
        }
        int baseUnmaintained = CampaignMain.cm.getIntegerConfig("BaseUnmaintainedLevel");
        int unmaintPenalty = CampaignMain.cm.getIntegerConfig("UnmaintainedPenalty");
        setStatus(STATUS_UNMAINTAINED);
        if (getMaintainanceLevel() >= baseUnmaintained + unmaintPenalty) {
            setMaintainanceLevel(baseUnmaintained);
        } else {
            addToMaintainanceLevel(-unmaintPenalty);
        }
    }

    public int getBVForMatch() {
        if (CampaignMain.cm.getBooleanConfig("UseBaseBVForMatching")) {
            return getBaseBV();
        }
        return getBV();
    }

    public int getBV() {
        int toReturn = 0;
        if (BV <= 0) {
            toReturn = calcBV();
            BV = toReturn;
        } else {
            toReturn = BV;
        }
        return (toReturn < 0) ? 0 : toReturn;
    }

    public void setBV(Integer i) {
        if (i < 0) {
            BV = 0;
        }
        BV = i;
    }

    /**
     * @return the megamek.common.entity this Unit represents
     */
    public Entity getEntity() {
        if (unitEntity != null) {
            return unitEntity;
        }
        unitEntity = SUnit.loadMech(getUnitFilename());
        return unitEntity;
    }

    public void setEntity(Entity unitEntity) {
        this.unitEntity = unitEntity;
    }

    public static Entity loadMech(String Filename) {
        if (Filename == null) {
            return null;
        }
        Entity ent = null;
        if (new File("./data/mechfiles").exists()) {
            try {
                MechSummary ms = MechSummaryCache.getInstance().getMech(Filename.trim());
                if (ms == null) {
                    MechSummary[] units = MechSummaryCache.getInstance().getAllMechs();
                    for (MechSummary unit : units) {
                        if (unit.getEntryName().equalsIgnoreCase(Filename) || unit.getModel().trim().equalsIgnoreCase(Filename.trim()) || unit.getChassis().trim().equalsIgnoreCase(Filename.trim())) {
                            ms = unit;
                            break;
                        }
                    }
                }
                if (ms != null) {
                    ent = new MechFileParser(ms.getSourceFile(), ms.getEntryName()).getEntity();
                }
            } catch (Exception exep) {
                ent = null;
            }
        }
        if (ent != null) {
            return ent;
        }
        try {
            ent = new MechFileParser(new File("./data/unitfiles/Meks.zip"), Filename).getEntity();
        } catch (Exception ex) {
            try {
                ent = new MechFileParser(new File("./data/unitfiles/Vehicles.zip"), Filename).getEntity();
            } catch (Exception exe) {
                try {
                    ent = new MechFileParser(new File("./data/unitfiles/Infantry.zip"), Filename).getEntity();
                } catch (Exception exei) {
                    CampaignData.mwlog.errLog("Error loading: " + Filename);
                    try {
                        ent = UnitUtils.createOMG();
                    } catch (Exception exep) {
                        CampaignData.mwlog.errLog("Unable to find default unit file. Server Exiting");
                        CampaignData.mwlog.errLog(exep);
                        System.exit(1);
                    }
                }
            }
        }
        return ent;
    }

    public void setPassesMaintainanceUntil(long l) {
        passesMaintainanceUntil = l;
    }

    public long getPassesMaintainanceUntil() {
        return passesMaintainanceUntil;
    }

    public int getScrappableFor() {
        return scrappableFor;
    }

    public void setScrappableFor(int i) {
        scrappableFor = i;
    }

    /**
     * @return the amount of EXP the pilot has
     */
    public int getExperience() {
        return getPilot().getExperience();
    }

    /**
     * @param experience
     *            the experience to set the pilot to
     */
    public void setExperience(Integer experience) {
        getPilot().setExperience(experience.intValue());
    }

    public boolean isOmni() {
        boolean isOmni = getEntity().isOmni();
        String targetChassis = getEntity().getChassis();
        if ((getType() == Unit.VEHICLE) && !isOmni) {
            try {
                FileInputStream fis = new FileInputStream("./data/buildtables/omnivehiclelist.txt");
                BufferedReader dis = new BufferedReader(new InputStreamReader(fis));
                while (dis.ready()) {
                    String chassie = dis.readLine();
                    if (targetChassis.equalsIgnoreCase(chassie)) {
                        dis.close();
                        fis.close();
                        return true;
                    }
                }
                dis.close();
                fis.close();
            } catch (Exception ex) {
            }
        }
        return isOmni;
    }

    public boolean hasTAG() {
        return getEntity().hasTAG();
    }

    public boolean hasHoming() {
        for (Mounted ammo : getEntity().getAmmo()) {
            if (((AmmoType) ammo.getType()).getMunitionType() == AmmoType.M_HOMING) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSemiGuided() {
        for (Mounted ammo : getEntity().getAmmo()) {
            if (((AmmoType) ammo.getType()).getMunitionType() == AmmoType.M_SEMIGUIDED) {
                return true;
            }
        }
        return false;
    }

    public int getBaseBV() {
        return getEntity().calculateBattleValue(false, true);
    }

    public int getPilotSkillBV() {
        int skillBV = 0;
        Iterator<PilotSkill> pilotSkills = getPilot().getSkills().getSkillIterator();
        while (pilotSkills.hasNext()) {
            SPilotSkill skill = (SPilotSkill) pilotSkills.next();
            skillBV += skill.getBVMod(getEntity(), (SPilot) getPilot());
        }
        return skillBV;
    }

    public void setPilotIsRepairing(boolean repair) {
        pilotIsRepairing = repair;
    }

    public boolean getPilotIsReparing() {
        return pilotIsRepairing;
    }

    public int getLastCombatPilot() {
        return lastCombatPilot;
    }

    public void setLastCombatPilot(int pilot) {
        lastCombatPilot = pilot;
    }

    @Override
    public void setWeightclass(int i) {
        if ((i > Unit.ASSAULT) || (i < Unit.LIGHT)) {
            i = Unit.getEntityWeight(getEntity());
        }
        super.setWeightclass(i);
    }

    public static Vector<SUnit> createMULUnits(String filename) {
        return SUnit.createMULUnits(filename, "autoassigned unit");
    }

    public static Vector<SUnit> createMULUnits(String filename, String fluff) {
        Vector<SUnit> mulUnits = new Vector<SUnit>(1, 1);
        Vector<Entity> loadedUnits = null;
        File entityFile = new File("data/armies/" + filename);
        try {
            loadedUnits = EntityListFile.loadFrom(entityFile);
            loadedUnits.trimToSize();
        } catch (Exception ex) {
            CampaignData.mwlog.errLog("Unable to load file " + entityFile.getName());
            CampaignData.mwlog.errLog(ex);
            return mulUnits;
        }
        for (Entity en : loadedUnits) {
            SUnit cm = new SUnit();
            cm.setEntity(en);
            cm.setUnitFilename(UnitUtils.getEntityFileName(en));
            cm.setId(CampaignMain.cm.getAndUpdateCurrentUnitID());
            cm.init();
            cm.setProducer(fluff);
            SPilot pilot = null;
            pilot = new SPilot(en.getCrew().getName(), en.getCrew().getGunnery(), en.getCrew().getPiloting());
            if (pilot.getName().equalsIgnoreCase("Unnamed") || pilot.getName().equalsIgnoreCase("vacant")) {
                pilot.setName(SPilot.getRandomPilotName(CampaignMain.cm.getR()));
            }
            pilot.setCurrentFaction("Common");
            StringTokenizer skillList = new StringTokenizer(en.getCrew().getOptionList(",", PilotOptions.LVL3_ADVANTAGES), ",");
            while (skillList.hasMoreTokens()) {
                String skill = skillList.nextToken();
                if (skill.toLowerCase().startsWith("weapon_specialist")) {
                    pilot.addMegamekOption(new MegaMekPilotOption("weapon_specialist", true));
                    pilot.getSkills().add(SPilotSkills.getPilotSkill(PilotSkill.WeaponSpecialistSkillID));
                    pilot.setWeapon(skill.substring("weapon_specialist".length()).trim());
                } else if (skill.toLowerCase().startsWith("edge ")) {
                    pilot.addMegamekOption(new MegaMekPilotOption("edge", true));
                    pilot.getSkills().add(SPilotSkills.getPilotSkill(PilotSkill.EdgeSkillID));
                    try {
                        pilot.getSkills().getPilotSkill(PilotSkill.EdgeSkillID).setLevel(Integer.parseInt(skill.substring("edge ".length()).trim()));
                    } catch (Exception ex) {
                        pilot.getSkills().getPilotSkill(PilotSkill.EdgeSkillID).setLevel(1);
                    }
                } else if (skill.toLowerCase().equals("edge_when_headhit")) {
                    pilot.setHeadHit(true);
                } else if (skill.toLowerCase().equals("edge_when_tac")) {
                    pilot.setTac(true);
                } else if (skill.toLowerCase().equals("edge_when_ko")) {
                    pilot.setKO(true);
                } else if (skill.toLowerCase().equals("edge_when_explosion")) {
                    pilot.setExplosion(true);
                } else {
                    pilot.getSkills().add(SPilotSkills.getPilotSkill(PilotSkill.getMMSkillID(skill)));
                    pilot.addMegamekOption(new MegaMekPilotOption(skill, true));
                }
            }
            skillList = new StringTokenizer(en.getCrew().getOptionList(",", PilotOptions.MD_ADVANTAGES), ",");
            while (skillList.hasMoreTokens()) {
                String skill = skillList.nextToken();
                pilot.getSkills().add(SPilotSkills.getPilotSkill(PilotSkill.getMMSkillID(skill)));
                pilot.addMegamekOption(new MegaMekPilotOption(skill, true));
            }
            cm.setPilot(pilot);
            cm.setWeightclass(99);
            mulUnits.add(cm);
        }
        return mulUnits;
    }

    /**
     * Compares SUnit IDs to support sorting of collections
     * @author Spork
     */
    public int compareTo(SUnit u) {
        return Integer.valueOf(this.getId()).compareTo(Integer.valueOf(u.getId()));
    }

    public void setTargetSystem(int type) {
        if (type != TargetSystem.TS_TYPE_STANDARD && CampaignData.cd.targetSystemIsBanned(type)) {
            setTargetSystem(TargetSystem.TS_TYPE_STANDARD);
        }
        try {
            targetSystem.setTargetSystem(type);
        } catch (TargetTypeOutOfBoundsException e) {
            e.printStackTrace();
        } catch (TargetTypeNotImplementedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSupportUnit() {
        return CampaignMain.cm.getSupportUnits().contains(getUnitFilename().toLowerCase());
    }

    public void reportStateToPlayer(SPlayer player) {
        CampaignMain.cm.toUser("PL|UU|" + getId() + "|" + toString(true), player.getName(), false);
    }

    public boolean isOMGUnit() {
        return getModelName().equals("OMG-UR-FD");
    }

    public boolean canBeCapturedInOperation(Operation o) {
        switch(getType()) {
            case Unit.MEK:
                return o.getBooleanValue("ForceProduceAndCaptureMeks");
            case Unit.VEHICLE:
                return o.getBooleanValue("ForceProduceAndCaptureVees");
            case Unit.INFANTRY:
                return o.getBooleanValue("ForceProduceAndCaptureInfs");
            case Unit.PROTOMEK:
                return o.getBooleanValue("ForceProduceAndCaptureProtos");
            case Unit.BATTLEARMOR:
                return o.getBooleanValue("ForceProduceAndCaptureBAs");
            case Unit.AERO:
                return o.getBooleanValue("ForceProduceAndCaptureAeros");
        }
        return false;
    }
}
