package phworld;

import java.io.*;

public class TechLevel implements Serializable, Cloneable {

    public static final int MAX_TECH = 8;

    public static final int N_TECH_TYPES = 5;

    private int hulls;

    private int engines;

    private int weapons;

    private int shields;

    private int misc;

    public TechLevel() {
    }

    public int getHullTech() {
        return hulls;
    }

    public int getEngineTech() {
        return engines;
    }

    public int getWeaponTech() {
        return weapons;
    }

    public int getShieldTech() {
        return shields;
    }

    public int getMiscTech() {
        return misc;
    }

    public void setHullTech(int t) {
        if (t < 0 || t > MAX_TECH) return;
        hulls = t;
    }

    public void setEngineTech(int t) {
        if (t < 0 || t > MAX_TECH) return;
        engines = t;
    }

    public void setWeaponTech(int t) {
        if (t < 0 || t > MAX_TECH) return;
        weapons = t;
    }

    public void setShieldTech(int t) {
        if (t < 0 || t > MAX_TECH) return;
        shields = t;
    }

    public void setMiscTech(int t) {
        if (t < 0 || t > MAX_TECH) return;
        misc = t;
    }

    public Object clone() {
        TechLevel t = new TechLevel();
        t.hulls = hulls;
        t.engines = engines;
        t.weapons = weapons;
        t.shields = shields;
        t.misc = misc;
        return t;
    }

    public int getTech(int type) {
        switch(type) {
            case ShipComponent.HULL:
                return hulls;
            case ShipComponent.ENGINE:
                return engines;
            case ShipComponent.WEAPON:
                return weapons;
            case ShipComponent.SHIELD:
                return shields;
            case ShipComponent.MISC:
                return misc;
        }
        return 0;
    }

    public void setTech(int type, int tech) {
        switch(type) {
            case ShipComponent.HULL:
                hulls = tech;
                break;
            case ShipComponent.ENGINE:
                engines = tech;
                break;
            case ShipComponent.WEAPON:
                weapons = tech;
                break;
            case ShipComponent.SHIELD:
                shields = tech;
                break;
            case ShipComponent.MISC:
                misc = tech;
                break;
        }
    }

    public static String getTechName(int type) {
        switch(type) {
            case ShipComponent.HULL:
                return "Hulls";
            case ShipComponent.ENGINE:
                return "Engines";
            case ShipComponent.WEAPON:
                return "Weapons";
            case ShipComponent.SHIELD:
                return "Shields";
            case ShipComponent.MISC:
                return "Misc items";
        }
        return "Unspecified";
    }

    public static String getNounTechName(int type) {
        switch(type) {
            case ShipComponent.HULL:
                return "hull";
            case ShipComponent.ENGINE:
                return "engine";
            case ShipComponent.WEAPON:
                return "weapon";
            case ShipComponent.SHIELD:
                return "shield";
            case ShipComponent.MISC:
                return "misc item";
        }
        return "unspecified";
    }

    public static String getName(int type, int techLevel) {
        ShipComponent sc = null;
        switch(type) {
            case ShipComponent.HULL:
                return ShipHull.getTechName(techLevel);
            case ShipComponent.ENGINE:
                sc = new ShipEngine(techLevel);
                break;
            case ShipComponent.WEAPON:
                sc = new ShipWeapon(techLevel);
                break;
            case ShipComponent.SHIELD:
                sc = new ShipShield(techLevel);
                break;
            case ShipComponent.MISC:
                sc = new ShipMisc(techLevel);
                break;
        }
        if (sc == null) return "Unknown Gadget (tm)"; else return sc.toString();
    }

    public static ShipComponent getComponentInstance(int type, int techLevel) {
        ShipComponent sc = null;
        switch(type) {
            case ShipComponent.HULL:
                sc = new ShipHull(-1, techLevel);
                break;
            case ShipComponent.ENGINE:
                sc = new ShipEngine(techLevel);
                break;
            case ShipComponent.WEAPON:
                sc = new ShipWeapon(techLevel);
                break;
            case ShipComponent.SHIELD:
                sc = new ShipShield(techLevel);
                break;
            case ShipComponent.MISC:
                sc = new ShipMisc(techLevel);
                break;
        }
        return sc;
    }

    public static int getTechNumber(String type) {
        if (type.startsWith("Hull")) return ShipComponent.HULL;
        if (type.startsWith("Engine")) return ShipComponent.ENGINE;
        if (type.startsWith("Weapon")) return ShipComponent.WEAPON;
        if (type.startsWith("Shield")) return ShipComponent.SHIELD;
        if (type.startsWith("Misc")) return ShipComponent.MISC;
        throw new IllegalArgumentException("Argument " + type + " is not name of any tech.");
    }

    public String toString() {
        return ("H:" + hulls + " E:" + engines + " W:" + weapons + " S:" + shields + " M:" + misc);
    }

    public static int getTechTime(int type, int tech) {
        if (tech < 1 || tech > MAX_TECH) return 99999;
        switch(type) {
            case ShipComponent.HULL:
                return ShipHull.getTechTime(tech);
            case ShipComponent.ENGINE:
                return ShipEngine.getTechTime(tech);
            case ShipComponent.WEAPON:
                return ShipWeapon.getTechTime(tech);
            case ShipComponent.SHIELD:
                return ShipShield.getTechTime(tech);
            case ShipComponent.MISC:
                return ShipMisc.getTechTime(tech);
        }
        return 0;
    }

    public static int getBuildTime(int type, int tech) {
        if (tech == 0) return 0;
        if (tech < 1 || tech > MAX_TECH) return 99999;
        switch(type) {
            case ShipComponent.HULL:
                return ShipHull.getBuildTime(tech);
            case ShipComponent.ENGINE:
                return ShipEngine.getBuildTime(tech);
            case ShipComponent.WEAPON:
                return ShipWeapon.getBuildTime(tech);
            case ShipComponent.SHIELD:
                return ShipShield.getBuildTime(tech);
            case ShipComponent.MISC:
                return ShipMisc.getBuildTime(tech);
        }
        return 0;
    }

    public static MaterialBundle getBuildMaterials(int type, int tech) {
        if (tech < 1 || tech > MAX_TECH) return new MaterialBundle(MaterialBundle.UNLIMITED);
        switch(type) {
            case ShipComponent.HULL:
                return ShipHull.getBuildMaterials(tech);
            case ShipComponent.ENGINE:
                return ShipEngine.getBuildMaterials(tech);
            case ShipComponent.WEAPON:
                return ShipWeapon.getBuildMaterials(tech);
            case ShipComponent.SHIELD:
                return ShipShield.getBuildMaterials(tech);
            case ShipComponent.MISC:
                return ShipMisc.getBuildMaterials(tech);
        }
        return null;
    }

    public static String getShortDescription(int type, int tech) {
        if (tech <= 0 || tech > MAX_TECH) return "";
        String filename = "NoSuchTechError";
        switch(type) {
            case ShipComponent.HULL:
                filename = "hulls";
                break;
            case ShipComponent.ENGINE:
                filename = "engines";
                break;
            case ShipComponent.WEAPON:
                filename = "weapons";
                break;
            case ShipComponent.SHIELD:
                filename = "shields";
                break;
            case ShipComponent.MISC:
                filename = "misc";
                break;
        }
        filename = "data/tech" + File.separator + filename + File.separator + tech + ".short";
        java.io.BufferedReader in = null;
        try {
            java.io.File f = new java.io.File(filename);
            in = new java.io.BufferedReader(new java.io.FileReader(f));
        } catch (Exception e) {
            System.err.println("Error Loading " + filename);
            System.exit(1);
        }
        String descriptionText = "";
        String s;
        try {
            do {
                s = in.readLine();
                if (s != null) descriptionText = descriptionText + s + "\n";
            } while (s != null);
        } catch (IOException e) {
        }
        try {
            in.close();
        } catch (IOException e) {
        }
        return descriptionText;
    }

    public static MaterialBundle getTechMaterials(int type, int tech, int existingN) {
        if (tech < 1 || tech > MAX_TECH) return new MaterialBundle(MaterialBundle.UNLIMITED);
        int scale = calcTechUpgradePricePercentage(existingN);
        MaterialBundle mb = null;
        switch(type) {
            case ShipComponent.HULL:
                mb = ShipHull.getTechMaterials(tech);
                break;
            case ShipComponent.ENGINE:
                mb = ShipEngine.getTechMaterials(tech);
                break;
            case ShipComponent.WEAPON:
                mb = ShipWeapon.getTechMaterials(tech);
                break;
            case ShipComponent.SHIELD:
                mb = ShipShield.getTechMaterials(tech);
                break;
            case ShipComponent.MISC:
                mb = ShipMisc.getTechMaterials(tech);
                break;
        }
        if (mb != null) {
            mb.scaleBundle(scale);
            return mb;
        }
        return null;
    }

    public static MaterialBundle getFirstRoundBuildMaterials(int type, int tech) {
        MaterialBundle mb = new MaterialBundle(MaterialBundle.UNLIMITED);
        MaterialBundle buildm = getBuildMaterials(type, tech);
        int time = getBuildTime(type, tech);
        mb.putFuel(buildm.getFuel() / time + buildm.getFuel() % time);
        mb.putDodechadrium(buildm.getDodechadrium() / time + buildm.getDodechadrium() % time);
        mb.putMetals(buildm.getMetals() / time + buildm.getMetals() % time);
        mb.putMinerals(buildm.getMinerals() / time + buildm.getMinerals() % time);
        mb.putOil(buildm.getOil() / time + buildm.getOil() % time);
        mb.putTroops(buildm.getTroops() / time + buildm.getTroops() % time);
        return mb;
    }

    public static MaterialBundle getRestRoundsBuildMaterials(int type, int tech) {
        MaterialBundle mb = new MaterialBundle(MaterialBundle.UNLIMITED);
        MaterialBundle buildm = getBuildMaterials(type, tech);
        int time = getBuildTime(type, tech);
        mb.putFuel(buildm.getFuel() / time);
        mb.putDodechadrium(buildm.getDodechadrium() / time);
        mb.putMetals(buildm.getMetals() / time);
        mb.putMinerals(buildm.getMinerals() / time);
        mb.putOil(buildm.getOil() / time);
        mb.putTroops(buildm.getTroops() / time);
        return mb;
    }

    private static int calcTechUpgradePricePercentage(int existingN) {
        return (100 / (1 + existingN));
    }

    public static MaterialBundle getFirstRoundTechMaterials(int type, int tech, int existingN) {
        MaterialBundle mb = new MaterialBundle(MaterialBundle.UNLIMITED);
        MaterialBundle buildm = getTechMaterials(type, tech, existingN);
        int time = getTechTime(type, tech);
        mb.putFuel(buildm.getFuel() / time + buildm.getFuel() % time);
        mb.putDodechadrium(buildm.getDodechadrium() / time + buildm.getDodechadrium() % time);
        mb.putMetals(buildm.getMetals() / time + buildm.getMetals() % time);
        mb.putMinerals(buildm.getMinerals() / time + buildm.getMinerals() % time);
        mb.putOil(buildm.getOil() / time + buildm.getOil() % time);
        mb.putTroops(buildm.getTroops() / time + buildm.getTroops() % time);
        return mb;
    }

    public static MaterialBundle getRestRoundsTechMaterials(int type, int tech, int existingN) {
        MaterialBundle mb = new MaterialBundle(MaterialBundle.UNLIMITED);
        MaterialBundle buildm = getTechMaterials(type, tech, existingN);
        int time = getTechTime(type, tech);
        mb.putFuel(buildm.getFuel() / time);
        mb.putDodechadrium(buildm.getDodechadrium() / time);
        mb.putMetals(buildm.getMetals() / time);
        mb.putMinerals(buildm.getMinerals() / time);
        mb.putOil(buildm.getOil() / time);
        mb.putTroops(buildm.getTroops() / time);
        return mb;
    }
}
