package megamek.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import megamek.common.weapons.BayWeapon;

/**
 * @author Jay Lawson
 */
public class Jumpship extends Aero {

    /**
     *
     */
    private static final long serialVersionUID = 9154398176617208384L;

    public static final int LOC_FLS = 1;

    public static final int LOC_FRS = 2;

    public static final int LOC_ALS = 4;

    public static final int LOC_ARS = 5;

    private static String[] LOCATION_ABBRS = { "NOS", "FLS", "FRS", "AFT", "ALS", "ARS" };

    private static String[] LOCATION_NAMES = { "Nose", "Left Front Side", "Right Front Side", "Aft", "Aft Left Side", "Aft Right Side" };

    private int kf_integrity = 0;

    private int sail_integrity = 0;

    private int nCrew = 0;

    private int nPassenger = 0;

    private int lifeBoats = 0;

    private int escapePods = 0;

    boolean hasLF = false;

    private boolean hasHPG = false;

    private int gravDeck = 0;

    private int gravDeckLarge = 0;

    private int gravDeckHuge = 0;

    private double stationThrust = 0.2;

    private double accumulatedThrust = 0.0;

    public Jumpship() {
        super();
        damThresh = new int[] { 0, 0, 0, 0, 0, 0 };
    }

    @Override
    public int locations() {
        return 6;
    }

    public int getTotalGravDeck() {
        return (gravDeck + gravDeckLarge + gravDeckHuge);
    }

    public void setGravDeck(int n) {
        gravDeck = n;
    }

    public int getGravDeck() {
        return gravDeck;
    }

    public void setGravDeckLarge(int n) {
        gravDeckLarge = n;
    }

    public int getGravDeckLarge() {
        return gravDeckLarge;
    }

    public void setGravDeckHuge(int n) {
        gravDeckHuge = n;
    }

    public int getGravDeckHuge() {
        return gravDeckHuge;
    }

    public void setHPG(boolean b) {
        hasHPG = b;
    }

    public boolean hasHPG() {
        return hasHPG;
    }

    public void setLF(boolean b) {
        hasLF = b;
    }

    public boolean hasLF() {
        return hasLF;
    }

    public void setEscapePods(int n) {
        escapePods = n;
    }

    public int getEscapePods() {
        return escapePods;
    }

    public void setLifeBoats(int n) {
        lifeBoats = n;
    }

    public int getLifeBoats() {
        return lifeBoats;
    }

    public void setNCrew(int crew) {
        nCrew = crew;
    }

    public void setNPassenger(int pass) {
        nPassenger = pass;
    }

    public int getNCrew() {
        return nCrew;
    }

    public int getNPassenger() {
        return nPassenger;
    }

    @Override
    public String[] getLocationAbbrs() {
        return LOCATION_ABBRS;
    }

    @Override
    public String[] getLocationNames() {
        return LOCATION_NAMES;
    }

    public void setKFIntegrity(int kf) {
        kf_integrity = kf;
    }

    public int getKFIntegrity() {
        return kf_integrity;
    }

    public void setSailIntegrity(int sail) {
        sail_integrity = sail;
    }

    public int getSailIntegrity() {
        return sail_integrity;
    }

    public void initializeSailIntegrity() {
        int integrity = 1 + (int) Math.round((30.0 + weight / 7500.0) / 20.0);
        setSailIntegrity(integrity);
    }

    public void initializeKFIntegrity() {
        int integrity = (int) Math.round(1.2 + (0.95) * weight / 60000.0);
        setKFIntegrity(integrity);
    }

    public boolean canJump() {
        return kf_integrity > 0;
    }

    @Override
    public void setEngine(Engine e) {
        engine = e;
    }

    @Override
    public int getWeaponArc(int wn) {
        final Mounted mounted = getEquipment(wn);
        int arc = Compute.ARC_NOSE;
        switch(mounted.getLocation()) {
            case LOC_NOSE:
                arc = Compute.ARC_NOSE;
                break;
            case LOC_FRS:
                arc = Compute.ARC_RIGHTSIDE_SPHERE;
                break;
            case LOC_FLS:
                arc = Compute.ARC_LEFTSIDE_SPHERE;
                break;
            case LOC_ARS:
                arc = Compute.ARC_RIGHTSIDEA_SPHERE;
                break;
            case LOC_ALS:
                arc = Compute.ARC_LEFTSIDEA_SPHERE;
                break;
            case LOC_AFT:
                arc = Compute.ARC_AFT;
                break;
            default:
                arc = Compute.ARC_360;
        }
        return rollArcs(arc);
    }

    @Override
    public HitData rollHitLocation(int table, int side) {
        int roll = Compute.d6(2);
        if (side == ToHitData.SIDE_FRONT) {
            switch(roll) {
                case 2:
                    setPotCrit(CRIT_LIFE_SUPPORT);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
                case 3:
                    setPotCrit(CRIT_CONTROL);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
                case 4:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_FRS, false, HitData.EFFECT_NONE);
                case 5:
                    setPotCrit(CRIT_RIGHT_THRUSTER);
                    return new HitData(LOC_FRS, false, HitData.EFFECT_NONE);
                case 6:
                    setPotCrit(CRIT_CIC);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
                case 7:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
                case 8:
                    setPotCrit(CRIT_SENSOR);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
                case 9:
                    setPotCrit(CRIT_LEFT_THRUSTER);
                    return new HitData(LOC_FLS, false, HitData.EFFECT_NONE);
                case 10:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_FLS, false, HitData.EFFECT_NONE);
                case 11:
                    setPotCrit(CRIT_CREW);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
                case 12:
                    setPotCrit(CRIT_KF_DRIVE);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
            }
        } else if (side == ToHitData.SIDE_LEFT) {
            switch(roll) {
                case 2:
                    setPotCrit(CRIT_AVIONICS);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
                case 3:
                    setPotCrit(CRIT_SENSOR);
                    return new HitData(LOC_FLS, false, HitData.EFFECT_NONE);
                case 4:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_FLS, false, HitData.EFFECT_NONE);
                case 5:
                    setPotCrit(CRIT_DOCK_COLLAR);
                    return new HitData(LOC_FLS, false, HitData.EFFECT_NONE);
                case 6:
                    setPotCrit(CRIT_KF_DRIVE);
                    return new HitData(LOC_FLS, false, HitData.EFFECT_NONE);
                case 7:
                    setPotCrit(CRIT_WEAPON_BROAD);
                    return new HitData(LOC_ALS, false, HitData.EFFECT_NONE);
                case 8:
                    setPotCrit(CRIT_GRAV_DECK);
                    return new HitData(LOC_ALS, false, HitData.EFFECT_NONE);
                case 9:
                    setPotCrit(CRIT_DOOR);
                    return new HitData(LOC_ALS, false, HitData.EFFECT_NONE);
                case 10:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_ALS, false, HitData.EFFECT_NONE);
                case 11:
                    setPotCrit(CRIT_CARGO);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
                case 12:
                    setPotCrit(CRIT_ENGINE);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
            }
        } else if (side == ToHitData.SIDE_RIGHT) {
            switch(roll) {
                case 2:
                    setPotCrit(CRIT_AVIONICS);
                    return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
                case 3:
                    setPotCrit(CRIT_SENSOR);
                    return new HitData(LOC_FRS, false, HitData.EFFECT_NONE);
                case 4:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_FRS, false, HitData.EFFECT_NONE);
                case 5:
                    setPotCrit(CRIT_DOCK_COLLAR);
                    return new HitData(LOC_FRS, false, HitData.EFFECT_NONE);
                case 6:
                    setPotCrit(CRIT_KF_DRIVE);
                    return new HitData(LOC_FRS, false, HitData.EFFECT_NONE);
                case 7:
                    setPotCrit(CRIT_WEAPON_BROAD);
                    return new HitData(LOC_ARS, false, HitData.EFFECT_NONE);
                case 8:
                    setPotCrit(CRIT_GRAV_DECK);
                    return new HitData(LOC_ARS, false, HitData.EFFECT_NONE);
                case 9:
                    setPotCrit(CRIT_DOOR);
                    return new HitData(LOC_ARS, false, HitData.EFFECT_NONE);
                case 10:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_ARS, false, HitData.EFFECT_NONE);
                case 11:
                    setPotCrit(CRIT_CARGO);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
                case 12:
                    setPotCrit(CRIT_ENGINE);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
            }
        } else if (side == ToHitData.SIDE_REAR) {
            switch(roll) {
                case 2:
                    setPotCrit(CRIT_FUEL_TANK);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
                case 3:
                    setPotCrit(CRIT_AVIONICS);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
                case 4:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_ARS, false, HitData.EFFECT_NONE);
                case 5:
                    setPotCrit(CRIT_RIGHT_THRUSTER);
                    return new HitData(LOC_ARS, false, HitData.EFFECT_NONE);
                case 6:
                    setPotCrit(CRIT_ENGINE);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
                case 7:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
                case 8:
                    setPotCrit(CRIT_ENGINE);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
                case 9:
                    setPotCrit(CRIT_LEFT_THRUSTER);
                    return new HitData(LOC_ALS, false, HitData.EFFECT_NONE);
                case 10:
                    setPotCrit(CRIT_WEAPON);
                    return new HitData(LOC_ALS, false, HitData.EFFECT_NONE);
                case 11:
                    setPotCrit(CRIT_CONTROL);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
                case 12:
                    setPotCrit(CRIT_KF_DRIVE);
                    return new HitData(LOC_AFT, false, HitData.EFFECT_NONE);
            }
        }
        return new HitData(LOC_NOSE, false, HitData.EFFECT_NONE);
    }

    @Override
    public int getMaxEngineHits() {
        return 6;
    }

    @Override
    public int calculateBattleValue(boolean ignoreC3, boolean ignorePilot) {
        if (useManualBV) {
            return manualBV;
        }
        double dbv = 0;
        double obv = 0;
        int modularArmor = 0;
        for (Mounted mounted : getEquipment()) {
            if ((mounted.getType() instanceof MiscType) && mounted.getType().hasFlag(MiscType.F_MODULAR_ARMOR)) {
                modularArmor += mounted.getBaseDamageCapacity() - mounted.getDamageTaken();
            }
        }
        dbv += (getTotalArmor() + modularArmor) * 25.0;
        dbv += getSI() * 20.0;
        double amsBV = 0;
        double amsAmmoBV = 0;
        double screenBV = 0;
        double screenAmmoBV = 0;
        for (Mounted mounted : getEquipment()) {
            EquipmentType etype = mounted.getType();
            if (mounted.isDestroyed()) {
                continue;
            }
            if (((etype instanceof WeaponType) && (etype.hasFlag(WeaponType.F_AMS)))) {
                amsBV += etype.getBV(this);
            } else if ((etype instanceof AmmoType) && (((AmmoType) etype).getAmmoType() == AmmoType.T_AMS)) {
                amsAmmoBV += etype.getBV(this);
            } else if ((etype instanceof AmmoType) && (((AmmoType) etype).getAmmoType() == AmmoType.T_SCREEN_LAUNCHER)) {
                screenAmmoBV += etype.getBV(this);
            } else if ((etype instanceof WeaponType) && (((WeaponType) etype).getAtClass() == WeaponType.CLASS_SCREEN)) {
                screenBV += etype.getBV(this);
            }
        }
        dbv += amsBV;
        dbv += screenBV;
        dbv += Math.min(amsBV, amsAmmoBV);
        dbv += Math.min(screenBV, screenAmmoBV);
        dbv *= getBVTypeModifier();
        int aeroHeatEfficiency = getHeatCapacity();
        TreeMap<String, Double> weaponsForExcessiveAmmo = new TreeMap<String, Double>();
        TreeMap<Integer, Double> arcBVs = new TreeMap<Integer, Double>();
        TreeMap<Integer, Double> arcHeat = new TreeMap<Integer, Double>();
        for (Mounted mounted : getTotalWeaponList()) {
            WeaponType wtype = (WeaponType) mounted.getType();
            double weaponHeat = wtype.getHeat();
            int arc = getWeaponArc(getEquipmentNum(mounted));
            double dBV = wtype.getBV(this);
            if (wtype instanceof BayWeapon) {
                continue;
            }
            if (wtype.hasFlag(WeaponType.F_AMS)) {
                continue;
            }
            if (wtype.getAtClass() == WeaponType.CLASS_SCREEN) {
                continue;
            }
            if (mounted.isMissing() || mounted.isHit() || mounted.isDestroyed() || mounted.isBreached()) {
                continue;
            }
            if ((wtype.getAmmoType() == AmmoType.T_AC_ULTRA) || (wtype.getAmmoType() == AmmoType.T_AC_ULTRA_THB)) {
                weaponHeat *= 2;
            }
            if (wtype.getAmmoType() == AmmoType.T_AC_ROTARY) {
                weaponHeat *= 6;
            }
            if (!((wtype.hasFlag(WeaponType.F_ENERGY) && !(wtype.getAmmoType() == AmmoType.T_PLASMA)) || wtype.hasFlag(WeaponType.F_ONESHOT) || wtype.hasFlag(WeaponType.F_INFANTRY) || (wtype.getAmmoType() == AmmoType.T_NA))) {
                String key = wtype.getAmmoType() + ":" + wtype.getRackSize() + ";" + arc;
                if (!weaponsForExcessiveAmmo.containsKey(key)) {
                    weaponsForExcessiveAmmo.put(key, wtype.getBV(this));
                } else {
                    weaponsForExcessiveAmmo.put(key, wtype.getBV(this) + weaponsForExcessiveAmmo.get(key));
                }
            }
            if (wtype.hasFlag(WeaponType.F_MGA)) {
                double mgaBV = 0;
                for (Mounted possibleMG : getTotalWeaponList()) {
                    if (possibleMG.getType().hasFlag(WeaponType.F_MG) && (possibleMG.getLocation() == mounted.getLocation())) {
                        mgaBV += possibleMG.getType().getBV(this);
                    }
                }
                dBV = mgaBV * 0.67;
            }
            if (wtype.hasFlag(WeaponType.F_DIRECT_FIRE)) {
                if (hasTargComp()) {
                    dBV *= 1.25;
                }
            }
            if (mounted.getLinkedBy() != null) {
                Mounted mLinker = mounted.getLinkedBy();
                if ((mLinker.getType() instanceof MiscType) && mLinker.getType().hasFlag(MiscType.F_ARTEMIS)) {
                    dBV *= 1.2;
                }
                if ((mLinker.getType() instanceof MiscType) && mLinker.getType().hasFlag(MiscType.F_ARTEMIS_V)) {
                    dBV *= 1.3;
                }
                if ((mLinker.getType() instanceof MiscType) && mLinker.getType().hasFlag(MiscType.F_APOLLO)) {
                    dBV *= 1.15;
                }
            }
            double currentArcBV = 0.0;
            double currentArcHeat = 0.0;
            if (null != arcBVs.get(arc)) {
                currentArcBV = arcBVs.get(arc);
            }
            if (null != arcHeat.get(arc)) {
                currentArcHeat = arcHeat.get(arc);
            }
            arcBVs.put(arc, currentArcBV + dBV);
            arcHeat.put(arc, currentArcHeat + weaponHeat);
        }
        double weaponBV = 0.0;
        int highArc = Integer.MIN_VALUE;
        int adjArc = Integer.MIN_VALUE;
        int oppArc = Integer.MIN_VALUE;
        double adjArcMult = 1.0;
        double oppArcMult = 0.5;
        double highBV = 0.0;
        double heatUsed = 0.0;
        Set<Integer> set = arcBVs.keySet();
        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext()) {
            int key = iter.next();
            if ((arcBVs.get(key) > highBV) && ((key == Compute.ARC_NOSE) || (key == Compute.ARC_LEFT_BROADSIDE) || (key == Compute.ARC_RIGHT_BROADSIDE) || (key == Compute.ARC_AFT))) {
                highArc = key;
                highBV = arcBVs.get(key);
            }
        }
        if (highArc > Integer.MIN_VALUE) {
            heatUsed += arcHeat.get(highArc);
            int adjArcCW = getAdjacentArcCW(highArc);
            int adjArcCCW = getAdjacentArcCCW(highArc);
            double adjArcCWBV = 0.0;
            double adjArcCWHeat = 0.0;
            if ((adjArcCW > Integer.MIN_VALUE) && (null != arcBVs.get(adjArcCW))) {
                adjArcCWBV = arcBVs.get(adjArcCW);
                adjArcCWHeat = arcHeat.get(adjArcCW);
            }
            double adjArcCCWBV = 0.0;
            double adjArcCCWHeat = 0.0;
            if ((adjArcCCW > Integer.MIN_VALUE) && (null != arcBVs.get(adjArcCCW))) {
                adjArcCCWBV = arcBVs.get(adjArcCCW);
                adjArcCCWHeat = arcHeat.get(adjArcCCW);
            }
            if (adjArcCWBV > adjArcCCWBV) {
                adjArc = adjArcCW;
                if ((heatUsed + adjArcCWHeat) > aeroHeatEfficiency) {
                    adjArcMult = 0.5;
                }
                heatUsed += adjArcCWHeat;
                oppArc = adjArcCCW;
                if ((heatUsed + adjArcCCWHeat) > aeroHeatEfficiency) {
                    oppArcMult = 0.25;
                }
            } else {
                adjArc = adjArcCCW;
                if ((heatUsed + adjArcCCWHeat) > aeroHeatEfficiency) {
                    adjArcMult = 0.5;
                }
                heatUsed += adjArcCCWHeat;
                oppArc = adjArcCW;
                if ((heatUsed + adjArcCWHeat) > aeroHeatEfficiency) {
                    oppArcMult = 0.25;
                }
            }
        }
        Map<String, Double> ammo = new HashMap<String, Double>();
        ArrayList<String> keys = new ArrayList<String>();
        for (Mounted mounted : getAmmo()) {
            int arc = getWeaponArc(getEquipmentNum(mounted));
            AmmoType atype = (AmmoType) mounted.getType();
            if (mounted.getShotsLeft() == 0) {
                continue;
            }
            if (atype.getAmmoType() == AmmoType.T_AMS) {
                continue;
            }
            if (atype.getAmmoType() == AmmoType.T_SCREEN_LAUNCHER) {
                continue;
            }
            if (mounted.getLocation() == Entity.LOC_NONE) {
                continue;
            }
            String key = atype.getAmmoType() + ":" + atype.getRackSize() + ";" + arc;
            double ammoWeight = mounted.getType().getTonnage(this);
            if (atype.isCapital()) {
                ammoWeight = mounted.getShotsLeft() * atype.getAmmoRatio();
            }
            ammoWeight = Math.ceil(weight);
            if (atype.hasFlag(AmmoType.F_CAP_MISSILE)) {
                ammoWeight = mounted.getShotsLeft();
            }
            if (!keys.contains(key)) {
                keys.add(key);
            }
            if (!ammo.containsKey(key)) {
                ammo.put(key, ammoWeight * atype.getBV(this));
            } else {
                ammo.put(key, ammoWeight * atype.getBV(this) + ammo.get(key));
            }
        }
        for (String key : keys) {
            double ammoBV = 0.0;
            int arc = Integer.parseInt(key.split(";")[1]);
            if (weaponsForExcessiveAmmo.get(key) != null) {
                if (ammo.get(key) > weaponsForExcessiveAmmo.get(key)) {
                    ammoBV += weaponsForExcessiveAmmo.get(key);
                } else {
                    ammoBV += ammo.get(key);
                }
            }
            double currentArcBV = 0.0;
            if (null != arcBVs.get(arc)) {
                currentArcBV = arcBVs.get(arc);
            }
            arcBVs.put(arc, currentArcBV + ammoBV);
        }
        if (highArc > Integer.MIN_VALUE) {
            weaponBV += arcBVs.get(highArc);
            arcBVs.put(highArc, 0.0);
            if ((adjArc > Integer.MIN_VALUE) && (null != arcBVs.get(adjArc))) {
                weaponBV += adjArcMult * arcBVs.get(adjArc);
                arcBVs.put(adjArc, 0.0);
            }
            if ((oppArc > Integer.MIN_VALUE) && (null != arcBVs.get(oppArc))) {
                weaponBV += oppArcMult * arcBVs.get(oppArc);
                arcBVs.put(oppArc, 0.0);
            }
            set = arcBVs.keySet();
            iter = set.iterator();
            while (iter.hasNext()) {
                int key = iter.next();
                weaponBV += (0.25 * arcBVs.get(key));
            }
        }
        double oEquipmentBV = 0;
        for (Mounted mounted : getMisc()) {
            MiscType mtype = (MiscType) mounted.getType();
            if (mounted.isDestroyed()) {
                continue;
            }
            if (mtype.hasFlag(MiscType.F_TARGCOMP)) {
                continue;
            }
            double bv = mtype.getBV(this);
            oEquipmentBV += bv;
        }
        weaponBV += oEquipmentBV;
        int runMp = getRunMP();
        if (!(this instanceof Warship) && !(this instanceof SpaceStation)) {
            runMp = 1;
        }
        double speedFactor = Math.pow(1 + (((double) runMp - 5) / 10), 1.2);
        speedFactor = Math.round(speedFactor * 100) / 100.0;
        obv = weaponBV * speedFactor;
        double finalBV = dbv + obv;
        double xbv = 0.0;
        if (!ignoreC3 && (game != null)) {
            xbv += getExtraC3BV((int) Math.round(finalBV));
        }
        finalBV = Math.round(finalBV + xbv);
        double pilotFactor = 1;
        if (!ignorePilot) {
            pilotFactor = getCrew().getBVSkillMultiplier();
        }
        int retVal = (int) Math.round((finalBV) * pilotFactor);
        return retVal;
    }

    public int getArcswGuns() {
        int nArcs = 0;
        for (int i = 0; i < locations(); i++) {
            if (hasWeaponInArc(i)) {
                nArcs++;
            }
        }
        return nArcs;
    }

    public boolean hasWeaponInArc(int loc) {
        boolean hasWeapons = false;
        for (Mounted weap : getWeaponList()) {
            if (weap.getLocation() == loc) {
                hasWeapons = true;
            }
        }
        return hasWeapons;
    }

    public double getFuelPerTon() {
        double points = 10.0;
        if (weight >= 250000) {
            points = 2.5;
            return points;
        } else if (weight >= 110000) {
            points = 5.0;
            return points;
        }
        return points;
    }

    @Override
    public double getArmorWeight(int loc) {
        double armorPoints = getTotalOArmor();
        armorPoints -= Math.round((get0SI() * loc) / 10.0);
        double baseArmor = 0.8;
        if (isClan()) {
            baseArmor = 1.0;
        }
        if (weight >= 250000) {
            baseArmor = 0.4;
            if (isClan()) {
                baseArmor = 0.5;
            }
        } else if (weight >= 150000) {
            baseArmor = 0.6;
            if (isClan()) {
                baseArmor = 0.7;
            }
        }
        if (armorType[0] == EquipmentType.T_ARMOR_FERRO_IMP) {
            baseArmor += 0.2;
        } else if (armorType[0] == EquipmentType.T_ARMOR_FERRO_CARBIDE) {
            baseArmor += 0.4;
        } else if (armorType[0] == EquipmentType.T_ARMOR_LAMELLOR_FERRO_CARBIDE) {
            baseArmor += 0.6;
        }
        double armorPerTon = baseArmor;
        double armWeight = 0.0;
        for (; (armWeight * armorPerTon) < armorPoints; armWeight += .5) {
        }
        return armWeight;
    }

    @Override
    public double getCost(boolean ignoreAmmo) {
        double cost = 0;
        cost += 200000;
        cost += 200000;
        cost += 5000 * (getNCrew() + getNPassenger());
        cost += 80000;
        cost += 100000;
        cost += 10000 * getArcswGuns();
        cost += 100000 * getSI();
        cost += 25000 + 10 * getWeight();
        cost += 100000 * getDocks();
        double engineWeight = weight * 0.012;
        cost += engineWeight * 1000;
        cost += 500 * getOriginalWalkMP() * weight / 100.0;
        cost += 1000;
        if (hasHPG()) {
            cost += 1000000000;
        }
        cost += 200 * getFuel() / getFuelPerTon();
        cost += getArmorWeight(locations()) * EquipmentType.getArmorCost(armorType[0]);
        int sinkCost = 2000 + 4000 * getHeatType();
        cost += sinkCost * getHeatSinks();
        double driveCost = 0;
        driveCost += 60000000 + (75000000 * getDocks());
        driveCost += 25000000 + (5000000 * getDocks());
        driveCost += 50000000;
        driveCost += 50000 * getKFIntegrity();
        driveCost += 50000 * (30 + (weight / 7500));
        driveCost += 500000 + (200000 * getDocks());
        if (hasLF()) {
            driveCost *= 3;
        }
        cost += driveCost;
        cost += 5000000 * getGravDeck();
        cost += 10000000 * getGravDeckLarge();
        cost += 40000000 * getGravDeckHuge();
        cost += getWeaponsAndEquipmentCost(ignoreAmmo);
        int baydoors = 0;
        int bayCost = 0;
        for (Bay next : getTransportBays()) {
            baydoors += next.getDoors();
            if ((next instanceof MechBay) || (next instanceof ASFBay) || (next instanceof SmallCraftBay)) {
                bayCost += 20000 * next.totalSpace;
            }
            if ((next instanceof LightVehicleBay) || (next instanceof HeavyVehicleBay)) {
                bayCost += 20000 * next.totalSpace;
            }
        }
        cost += bayCost + baydoors * 1000;
        cost += 5000 * (getLifeBoats() + getEscapePods());
        double weightMultiplier = 1.25f;
        return Math.round(cost * weightMultiplier);
    }

    @Override
    public boolean doomedOnGround() {
        return true;
    }

    @Override
    public boolean doomedInAtmosphere() {
        return true;
    }

    @Override
    public boolean doomedInSpace() {
        return false;
    }

    /**
     * need to check bay location before loading ammo
     */
    @Override
    public boolean loadWeapon(Mounted mounted, Mounted mountedAmmo) {
        boolean success = false;
        WeaponType wtype = (WeaponType) mounted.getType();
        AmmoType atype = (AmmoType) mountedAmmo.getType();
        if (mounted.getLocation() != mountedAmmo.getLocation()) {
            return success;
        }
        Mounted bay = whichBay(getEquipmentNum(mounted));
        if ((bay != null) && !bay.ammoInBay(getEquipmentNum(mountedAmmo))) {
            return success;
        }
        if (mountedAmmo.isAmmoUsable() && !wtype.hasFlag(WeaponType.F_ONESHOT) && (atype.getAmmoType() == wtype.getAmmoType()) && (atype.getRackSize() == wtype.getRackSize())) {
            mounted.setLinked(mountedAmmo);
            success = true;
        }
        return success;
    }

    @Override
    public int getHQIniBonus() {
        return 2;
    }

    /**
     * what location is opposite the given one
     */
    @Override
    public int getOppositeLocation(int loc) {
        switch(loc) {
            case LOC_NOSE:
                return LOC_AFT;
            case LOC_FLS:
                return LOC_ARS;
            case LOC_FRS:
                return LOC_ALS;
            case LOC_ALS:
                return LOC_FRS;
            case LOC_ARS:
                return LOC_FLS;
            case LOC_AFT:
                return LOC_NOSE;
            default:
                return LOC_NOSE;
        }
    }

    /**
     * All military jumpships automatically have ECM if in space
     */
    @Override
    public boolean hasActiveECM() {
        if (!game.getOptions().booleanOption("stratops_ecm") || !game.getBoard().inSpace()) {
            return super.hasActiveECM();
        }
        return getECMRange() >= 0;
    }

    /**
     * What's the range of the ECM equipment?
     * 
     * @return the <code>int</code> range of this unit's ECM. This value will be
     *         <code>Entity.NONE</code> if no ECM is active.
     */
    @Override
    public int getECMRange() {
        if (!game.getOptions().booleanOption("stratops_ecm") || !game.getBoard().inSpace()) {
            return super.getECMRange();
        }
        if (!isMilitary()) {
            return Entity.NONE;
        }
        int range = 1;
        range = range - getSensorHits() - getCICHits();
        return range;
    }

    /**
     * @return is the crew of this vessel protected from gravitational effects,
     *         see StratOps, pg. 36
     */
    @Override
    public boolean isCrewProtected() {
        return isMilitary() && (getOriginalWalkMP() > 4);
    }

    public double getAccumulatedThrust() {
        return accumulatedThrust;
    }

    public void setAccumulatedThrust(double d) {
        accumulatedThrust = d;
    }

    public double getStationKeepingThrust() {
        return stationThrust;
    }

    @Override
    public void newRound(int roundNumber) {
        super.newRound(roundNumber);
        if (isDeployed() && (getAccumulatedThrust() < 1.0)) {
            setAccumulatedThrust(getAccumulatedThrust() + stationThrust);
        }
    }

    @Override
    public int getRunMP(boolean gravity, boolean ignoreheat, boolean ignoremodulararmor) {
        if (this instanceof Warship) {
            return super.getRunMP(gravity, ignoreheat, ignoremodulararmor);
        }
        return (int) Math.floor(getAccumulatedThrust());
    }

    /**
     * find the adjacent firing arc on this vessel clockwise
     */
    public int getAdjacentArcCW(int arc) {
        switch(arc) {
            case Compute.ARC_NOSE:
                return Compute.ARC_RIGHTSIDE_SPHERE;
            case Compute.ARC_LEFTSIDE_SPHERE:
                return Compute.ARC_NOSE;
            case Compute.ARC_RIGHTSIDE_SPHERE:
                return Compute.ARC_RIGHTSIDEA_SPHERE;
            case Compute.ARC_LEFTSIDEA_SPHERE:
                return Compute.ARC_LEFTSIDE_SPHERE;
            case Compute.ARC_RIGHTSIDEA_SPHERE:
                return Compute.ARC_AFT;
            case Compute.ARC_AFT:
                return Compute.ARC_LEFTSIDEA_SPHERE;
            default:
                return Integer.MIN_VALUE;
        }
    }

    /**
     * find the adjacent firing arc on this vessel counter-clockwise
     */
    public int getAdjacentArcCCW(int arc) {
        switch(arc) {
            case Compute.ARC_NOSE:
                return Compute.ARC_LEFTSIDE_SPHERE;
            case Compute.ARC_RIGHTSIDE_SPHERE:
                return Compute.ARC_NOSE;
            case Compute.ARC_LEFTSIDE_SPHERE:
                return Compute.ARC_LEFTSIDEA_SPHERE;
            case Compute.ARC_LEFTSIDEA_SPHERE:
                return Compute.ARC_AFT;
            case Compute.ARC_RIGHTSIDEA_SPHERE:
                return Compute.ARC_RIGHTSIDE_SPHERE;
            case Compute.ARC_AFT:
                return Compute.ARC_RIGHTSIDEA_SPHERE;
            default:
                return Integer.MIN_VALUE;
        }
    }

    @Override
    public double getBVTypeModifier() {
        return 0.75;
    }

    @Override
    public boolean usesWeaponBays() {
        return true;
    }

    @Override
    public int getBattleForceSize() {
        if (getWeight() < 100000) {
            return 1;
        }
        if (getWeight() < 300000) {
            return 2;
        }
        return 3;
    }

    @Override
    public int getBattleForceStructurePoints() {
        return 1;
    }
}
