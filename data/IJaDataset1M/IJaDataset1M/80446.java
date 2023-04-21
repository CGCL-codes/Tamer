package megamek.common.loaders;

import megamek.common.Engine;
import megamek.common.Entity;
import megamek.common.EntityMovementMode;
import megamek.common.EquipmentType;
import megamek.common.LocationFullException;
import megamek.common.Protomech;
import megamek.common.TechConstants;
import megamek.common.util.BuildingBlock;

public class BLKProtoFile extends BLKFile implements IMechLoader {

    public BLKProtoFile(BuildingBlock bb) {
        dataFile = bb;
    }

    public Entity getEntity() throws EntityLoadingException {
        Protomech t = new Protomech();
        if (!dataFile.exists("name")) {
            throw new EntityLoadingException("Could not find name block.");
        }
        t.setChassis(dataFile.getDataAsString("Name")[0]);
        if (dataFile.exists("Model") && (dataFile.getDataAsString("Model")[0] != null)) {
            t.setModel(dataFile.getDataAsString("Model")[0]);
        } else {
            t.setModel("");
        }
        if (dataFile.exists("quad") && dataFile.getDataAsString("quad")[0].equalsIgnoreCase("true")) {
            t.setIsQuad(true);
        }
        if (dataFile.exists("source")) {
            t.setSource(dataFile.getDataAsString("source")[0]);
        }
        if (!dataFile.exists("year")) {
            throw new EntityLoadingException("Could not find year block.");
        }
        t.setYear(dataFile.getDataAsInt("year")[0]);
        setTechLevel(t);
        setFluff(t);
        checkManualBV(t);
        if (!dataFile.exists("tonnage")) {
            throw new EntityLoadingException("Could not find weight block.");
        }
        t.setWeight(dataFile.getDataAsFloat("tonnage")[0]);
        String sMotion = dataFile.getDataAsString("motion_type")[0];
        EntityMovementMode nMotion = EntityMovementMode.getMode(sMotion);
        if (nMotion == EntityMovementMode.NONE) {
            throw new EntityLoadingException("Invalid movement type: " + sMotion);
        }
        t.setMovementMode(nMotion);
        if (!dataFile.exists("cruiseMP")) {
            throw new EntityLoadingException("Could not find cruiseMP block.");
        }
        t.setOriginalWalkMP(dataFile.getDataAsInt("cruiseMP")[0]);
        int engineCode = BLKFile.FUSION;
        int engineFlags = Engine.NORMAL_ENGINE;
        engineFlags |= Engine.CLAN_ENGINE;
        int engineRating = (int) Math.round(dataFile.getDataAsInt("cruiseMP")[0] * 1.5) * (int) t.getWeight();
        t.setEngine(new Engine(engineRating, BLKFile.translateEngineCode(engineCode), engineFlags));
        if (dataFile.exists("jumpingMP")) {
            t.setOriginalJumpMP(dataFile.getDataAsInt("jumpingMP")[0]);
        }
        if (!dataFile.exists("armor")) {
            throw new EntityLoadingException("Could not find armor block.");
        }
        int[] armor = dataFile.getDataAsInt("armor");
        boolean hasMainGun = false;
        if (Protomech.NUM_PMECH_LOCATIONS == armor.length) {
            hasMainGun = true;
        } else if ((Protomech.NUM_PMECH_LOCATIONS - 1) == armor.length) {
            hasMainGun = false;
        } else {
            throw new EntityLoadingException("Incorrect armor array length");
        }
        t.setHasMainGun(hasMainGun);
        for (int x = 0; x < armor.length; x++) {
            t.initializeArmor(armor[x], x);
        }
        t.autoSetInternal();
        String[] abbrs = t.getLocationNames();
        for (int loop = 0; loop < t.locations(); loop++) {
            loadEquipment(t, abbrs[loop], loop);
        }
        return t;
    }

    private void loadEquipment(Protomech t, String sName, int nLoc) throws EntityLoadingException {
        String[] saEquip = dataFile.getDataAsString(sName + " Equipment");
        if (saEquip == null) {
            return;
        }
        String prefix;
        if (t.getTechLevel() == TechConstants.T_CLAN_TW) {
            prefix = "Clan ";
        } else {
            prefix = "IS ";
        }
        for (String element : saEquip) {
            String equipName = element.trim();
            int ammoIndex = equipName.indexOf("Ammo (");
            int shotsCount = 0;
            if (ammoIndex > 0) {
                try {
                    String shots = equipName.substring(ammoIndex + 6, equipName.length() - 1);
                    shotsCount = Integer.parseInt(shots);
                    if (shotsCount < 0) {
                        throw new EntityLoadingException("Invalid number of shots in: " + equipName + ".");
                    }
                } catch (NumberFormatException badShots) {
                    throw new EntityLoadingException("Could not determine the number of shots in: " + equipName + ".");
                }
                equipName = equipName.substring(0, ammoIndex + 4);
            }
            EquipmentType etype = EquipmentType.get(equipName);
            if (etype == null) {
                etype = EquipmentType.get(prefix + equipName);
            }
            if (etype != null) {
                try {
                    if (ammoIndex > 0) {
                        t.addEquipment(etype, nLoc, false, shotsCount);
                    } else {
                        t.addEquipment(etype, nLoc);
                    }
                } catch (LocationFullException ex) {
                    throw new EntityLoadingException(ex.getMessage());
                }
            }
        }
    }
}
