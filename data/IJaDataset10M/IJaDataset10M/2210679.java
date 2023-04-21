package org.opengpx.lib;

import java.util.Hashtable;

public class AdvancedSearchData {

    public Hashtable<EnumInterface, Boolean> containerSizeBoxes = new Hashtable<EnumInterface, Boolean>();

    public Hashtable<EnumInterface, Boolean> cacheTypeBoxes = new Hashtable<EnumInterface, Boolean>();

    public String difficultyFrom;

    public String difficultyTo;

    public String terrainFrom;

    public String terrainTo;

    public float maxDistance;

    public RadioOptions isActive;

    public RadioOptions isFound;

    public RadioOptions isIgnored;

    public RadioOptions isOwned;

    public RadioOptions hasWpts;

    public RadioOptions hasAltCoords;

    public RadioOptions hasTBs;

    public enum RadioOptions {

        YES("t"), NO("f"), EITHER("b");

        private String typeCode;

        RadioOptions(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeCode() {
            return typeCode;
        }
    }

    public interface EnumInterface {
    }

    public enum CacheType implements EnumInterface {

        ALL("All"), NONE("None"), TRADITIONAL("t"), CITO("c"), EARTHCACHE("ea"), EVENT("ev"), GPS_ADVENTURES("g"), LETTERBOX("le"), LOCATIONLESS("lo"), MEGA_EVENT("me"), MULTI("mu"), APE("p"), UNKNOWN("u"), VIRTUAL("v"), WEBCAM("we"), WHEREIGO("wh");

        private String typeCode;

        CacheType(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeCode() {
            return typeCode;
        }
    }

    public enum ContainerSize implements EnumInterface {

        ALL("All"), NONE("None"), MICRO("m"), SMALL("s"), REGULAR("r"), LARGE("l"), NOT_CHOSEN("n"), OTHER("o"), VIRTUAL("v");

        private String typeCode;

        ContainerSize(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeCode() {
            return typeCode;
        }
    }

    public String getCacheTypeString() {
        StringBuilder ret = new StringBuilder(255);
        for (EnumInterface key : cacheTypeBoxes.keySet()) {
            CacheType type = (CacheType) key;
            if (cacheTypeBoxes.get(key)) {
                ret.append(type.getTypeCode() + ",");
            }
        }
        return ret.toString();
    }

    public boolean haveCacheTypeFilter() {
        if (cacheTypeBoxes.values().contains(Boolean.valueOf(true))) {
            return true;
        }
        return false;
    }

    public String getContainerSizeString() {
        StringBuilder ret = new StringBuilder(255);
        for (EnumInterface key : containerSizeBoxes.keySet()) {
            ContainerSize type = (ContainerSize) key;
            if (containerSizeBoxes.get(key)) {
                ret.append(type.getTypeCode() + ",");
            }
        }
        return ret.toString();
    }

    public boolean haveContainerSizeFilter() {
        if (containerSizeBoxes.values().contains(Boolean.valueOf(true))) {
            return true;
        }
        return false;
    }
}
