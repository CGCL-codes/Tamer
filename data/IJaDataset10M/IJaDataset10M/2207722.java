package btthud.data;

import java.awt.*;

public class MUHex {

    public static final int UNKNOWN = 0;

    public static final int PLAIN = 1;

    public static final int WATER = 2;

    public static final int LIGHT_FOREST = 3;

    public static final int HEAVY_FOREST = 4;

    public static final int MOUNTAIN = 5;

    public static final int ROUGH = 6;

    public static final int BUILDING = 7;

    public static final int ROAD = 8;

    public static final int BRIDGE = 9;

    public static final int FIRE = 10;

    public static final int WALL = 11;

    public static final int SMOKE = 12;

    public static final int ICE = 13;

    public static final int SMOKE_OVER_WATER = 14;

    public static final int TOTAL_TERRAIN = 15;

    public static final char terrainTypes[] = { '?', '.', '~', '`', '"', '^', '%', '@', '#', '/', '&', '=', ':', '-', '+' };

    int terrain;

    int elevation;

    public MUHex() {
        terrain = UNKNOWN;
        elevation = 0;
    }

    public MUHex(int terrain, int elevation) {
        this.terrain = terrain;
        this.elevation = elevation;
    }

    public int terrain() {
        return terrain;
    }

    public int elevation() {
        return elevation;
    }

    public void setTerrain(char t) {
        terrain = idForTerrain(t);
    }

    public void setElevation(int e) {
        elevation = e;
    }

    public static Color colorForElevation(Color ic, int e, float elevationColorMultiplier) {
        float[] comp = ic.getRGBColorComponents(null);
        float mod = elevationColorMultiplier * e;
        float[] newComp = { comp[0], comp[1], comp[2] };
        for (int i = 0; i < 3; i++) {
            newComp[i] -= mod;
            if (newComp[i] < 0.0f) newComp[i] = 0.0f;
        }
        return new Color(newComp[0], newComp[1], newComp[2]);
    }

    public static int idForTerrain(char terr) {
        switch(terr) {
            case '.':
                return PLAIN;
            case '~':
                return WATER;
            case '`':
                return LIGHT_FOREST;
            case '"':
                return HEAVY_FOREST;
            case '^':
                return MOUNTAIN;
            case '%':
                return ROUGH;
            case '@':
                return BUILDING;
            case '#':
                return ROAD;
            case '/':
                return BRIDGE;
            case '&':
                return FIRE;
            case '=':
                return WALL;
            case ':':
                return SMOKE;
            case '-':
                return ICE;
            case '+':
                return SMOKE_OVER_WATER;
            case '?':
                return UNKNOWN;
            default:
                return UNKNOWN;
        }
    }

    public static String nameForId(int id) {
        switch(id) {
            case PLAIN:
                return "Plain";
            case WATER:
                return "Water";
            case LIGHT_FOREST:
                return "Light Forest";
            case HEAVY_FOREST:
                return "Heavy Forest";
            case MOUNTAIN:
                return "Mountain";
            case ROUGH:
                return "Rough";
            case BUILDING:
                return "Building";
            case ROAD:
                return "Road";
            case BRIDGE:
                return "Bridge";
            case FIRE:
                return "Fire";
            case WALL:
                return "Wall";
            case SMOKE:
                return "Smoke";
            case ICE:
                return "Ice";
            case SMOKE_OVER_WATER:
                return "Smoke on Water";
            case UNKNOWN:
                return "Unknown";
            default:
                return "Unknown";
        }
    }

    public static char terrainForId(int id) {
        if (id < 0 || id >= TOTAL_TERRAIN) return '?'; else return terrainTypes[id];
    }
}
