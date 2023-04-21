package civquest.map;

import civquest.core.Game;
import civquest.io.ConfigurationException;
import civquest.io.Messages;
import civquest.map.FieldDistanceInfo;
import civquest.parser.ruleset.Registry;
import civquest.parser.ruleset.Ruleset;
import civquest.parser.ruleset.Section;
import civquest.parser.ruleset.exception.RulesetException;
import civquest.util.Coordinate;
import civquest.util.Rectangle;
import java.util.Random;

public class FlatMapData extends MapData {

    private Random randomgen = new Random();

    private int diagonalDistance;

    private int straightDistance;

    public FlatMapData(Game game, int width, int height, boolean nflatearth, int nminbigriverwf, Registry registry) throws ConfigurationException {
        super(game, width, height, nflatearth, nminbigriverwf, registry);
        fields = new Field[mapWidth][mapHeight];
        loadConfiguration(registry);
    }

    public void initFieldArray() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                fields[x][y] = new Field(x, y);
            }
        }
    }

    private void loadConfiguration(Registry registry) throws RulesetException {
        Registry mapRegistry = registry.getSubRegistry("map");
        Ruleset flatRuleset = mapRegistry.getRuleset("flat");
        Section distSection = flatRuleset.getSection("distances");
        diagonalDistance = distSection.getField("diagonal").getIntValue();
        straightDistance = distSection.getField("straight").getIntValue();
    }

    public int getArrayWidth() {
        return mapWidth;
    }

    public int getArrayHeight() {
        return mapHeight;
    }

    public Coordinate adjustToMapSize(Coordinate coord) {
        if ((coord.y < 0) || (coord.y >= mapHeight)) return null;
        if (isFlatEarth()) {
            if ((coord.x < 0) || (coord.x >= mapWidth)) return null;
        } else {
            while (coord.x < 0) coord.x += mapWidth;
            while (coord.x >= mapWidth) coord.x -= mapWidth;
        }
        return coord;
    }

    public boolean isArrayCoordOnMap(Coordinate coord) {
        return isOnMap(coord.x, coord.y);
    }

    public boolean isArrayCoordOnMap(int x, int y) {
        return isOnMap(x, y);
    }

    public boolean isOnMap(Coordinate coord) {
        return isOnMap(coord.x, coord.y);
    }

    public boolean isOnMap(int x, int y) {
        return ((x >= 0) && (x < mapWidth) && (y >= 0) && (y < mapHeight));
    }

    public boolean isOnNorthEdge(Coordinate coord) {
        return (coord.y == 0);
    }

    public boolean isOnEastEdge(Coordinate coord) {
        if (!isFlatEarth()) return false; else return (coord.x == mapWidth - 1);
    }

    public boolean isOnSouthEdge(Coordinate coord) {
        return (coord.y == mapHeight - 1);
    }

    public boolean isOnWestEdge(Coordinate coord) {
        if (!isFlatEarth()) return false; else return (coord.x == 0);
    }

    public Coordinate getAreaCoord(Rectangle rect, Coordinate coord) {
        coord = (Coordinate) (coord.clone());
        if (coord.x < rect.getX1()) {
            do {
                coord.x += getMapWidth();
            } while (coord.x < rect.getX1());
        } else if (coord.x > rect.getX2()) {
            do {
                coord.x -= getMapWidth();
            } while (coord.x > rect.getX2());
        }
        if ((coord.x >= rect.getX1()) && (coord.y >= rect.getY1()) && (coord.x <= rect.getX2()) && (coord.y <= rect.getY2())) {
            return coord;
        } else {
            return null;
        }
    }

    public Coordinate getRandomCoordinate() {
        return new Coordinate(randomgen.nextInt(mapWidth), randomgen.nextInt(mapHeight));
    }

    public Coordinate mapCoordinate(Coordinate coord) {
        return coord;
    }

    /** Returns if the two given Coordinates are neighbor-Coordinates */
    public boolean areNeighborCoords(Coordinate coord1, Coordinate coord2) {
        if (Math.abs(coord1.y - coord2.y) > 1) {
            return false;
        } else if (Math.abs(coord1.x - coord2.x) <= 1) {
            return true;
        } else if ((!isFlatEarth()) && (((coord1.x == 0) && (coord2.x == mapWidth - 1)) || ((coord1.x == mapWidth - 1) && (coord2.x == 0)))) {
            return true;
        } else {
            return false;
        }
    }

    public int getNeighborFieldDist(Coordinate pos1, Coordinate pos2) {
        if ((pos1.x == pos2.x) || (pos1.y == pos2.y)) {
            return straightDistance;
        } else {
            return diagonalDistance;
        }
    }

    public Field[] getNeighborFields(Field field) {
        Coordinate[] neighborCoords = field.getPosition().getNeighborCoords();
        Field[] neighborFields = new Field[8];
        for (int n = 0; n < 8; n++) {
            if (isOnMap(neighborCoords[n])) {
                neighborFields[n] = getField(neighborCoords[n]);
            } else {
                neighborFields[n] = null;
            }
        }
        return neighborFields;
    }

    public FieldDistanceInfo[] getNeighborFieldDists(Field field) {
        FieldDistanceInfo[] retValue = new FieldDistanceInfo[8];
        Coordinate pos = field.getPosition();
        Coordinate[] neighborCoords = pos.getNeighborCoords();
        for (int n = 0; n < retValue.length; n++) {
            if (isOnMap(neighborCoords[n])) {
                int distance = 0;
                if (neighborCoords[n].x == pos.x || neighborCoords[n].y == pos.y) {
                    distance = straightDistance;
                } else {
                    distance = diagonalDistance;
                }
                retValue[n] = new FieldDistanceInfo(getField(neighborCoords[n]), distance);
            } else {
                Coordinate adjustedCoord = adjustToMapSize(neighborCoords[n]);
                if (adjustedCoord == null) {
                    retValue[n] = null;
                } else {
                    int distance = 0;
                    if (adjustedCoord.y == pos.y) {
                        distance = straightDistance;
                    } else {
                        distance = diagonalDistance;
                    }
                    retValue[n] = new FieldDistanceInfo(getField(adjustedCoord), distance);
                }
            }
        }
        return retValue;
    }
}
