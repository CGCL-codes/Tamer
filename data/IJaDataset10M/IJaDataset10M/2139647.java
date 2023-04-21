package de.oststadt.shavengods;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.oststadt.shavengods.util.Coordinate;
import de.oststadt.shavengods.exceptions.LandTileOccupiedException;
import de.oststadt.shavengods.exceptions.WorldIsTooCrowdedException;
import de.oststadt.shavengods.util.SatNavSystem;

public class WorldMap {

    private LandTile[][] map;

    private int height;

    private int width;

    private Map<ShavenGod, Coordinate> currentLocations;

    WorldMap(int x, int y) {
        this.map = new LandTile[x][y];
        this.currentLocations = new HashMap<ShavenGod, Coordinate>();
        this.width = x;
        this.height = y;
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.map[i][j] = new LandTile();
            }
        }
    }

    public LandTile[][] getAllMapTiles() {
        return map;
    }

    public LandTile getSingleTile(Coordinate c) {
        return this.map[c.getX()][c.getY()];
    }

    public List<Coordinate> getAdjactentCoordinates(Coordinate c) {
        List<Coordinate> result = new LinkedList<Coordinate>();
        if (c.getX() - 1 > 0) {
            result.add(new Coordinate(c.getX() - 1, c.getY()));
        }
        if (c.getY() - 1 > 0) {
            result.add(new Coordinate(c.getX(), c.getY() - 1));
        }
        if (c.getX() + 1 < this.width) {
            result.add(new Coordinate(c.getX() + 1, c.getY()));
        }
        if (c.getY() + 1 < this.height) {
            result.add(new Coordinate(c.getX(), c.getY() + 1));
        }
        return result;
    }

    public boolean occupyTile(Coordinate c, ShavenGod i) {
        try {
            this.map[c.getX()][c.getY()].setOccupant(i);
            this.currentLocations.put(i, c);
            return true;
        } catch (LandTileOccupiedException ex) {
            return false;
        }
    }

    public Coordinate getInhabitantsCurrentLocation(ShavenGod inhabitant) {
        return this.currentLocations.get(inhabitant);
    }

    public Collection<ShavenGod> getAllInhabitants() {
        return this.currentLocations.keySet();
    }

    public Coordinate getAdjactentEmptyCoordinate(Coordinate origin) throws WorldIsTooCrowdedException {
        for (Coordinate c : this.getAdjactentCoordinates(origin)) {
            if (!this.getSingleTile(c).isOccupied()) {
                return c;
            }
        }
        throw new WorldIsTooCrowdedException();
    }

    /**
     * moves god to desired location (if possible) and frees up currentLocation
     * if desired location is occupied, move is gracefully ignored.
     * @param god
     * @param moveToCoordinate
     */
    public void move(ShavenGod god, Coordinate moveToCoordinate) {
        Coordinate currentLocation = this.getInhabitantsCurrentLocation(god);
        if (this.occupyTile(moveToCoordinate, god)) {
            this.occupyTile(currentLocation, null);
        }
    }
}
