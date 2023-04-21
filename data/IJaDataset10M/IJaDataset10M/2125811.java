package hokutonorogue.level;

import java.io.FileNotFoundException;
import java.net.*;
import java.util.*;
import com.golden.gamedev.util.*;
import hokutonorogue.character.*;
import hokutonorogue.level.tile.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public class MazeLevelModel extends AbstractIndoorLevelModel {

    protected int randomness = 10;

    protected int sparseness = 10;

    protected int deadendRemoval = 75;

    protected int roomsNumber = 10;

    protected int minWidth = 2;

    protected int maxWidht = 4;

    protected int minHeight = 2;

    protected int maxHeight = 3;

    protected List<Room> rooms = new ArrayList<Room>();

    public MazeLevelModel() {
    }

    @Override
    public void _initialize() throws MalformedURLException, FileNotFoundException, Exception {
    }

    protected void placeRandomRoomScenery() {
        for (Room room : rooms) {
            for (int i = 0; i < Utility.getRandom(0, 3); i++) {
                Tile t = room.getRandomFreeBorderTile();
                if (t != null) {
                    SceneryObjectsContainer oc = new SceneryObjectsContainer();
                    oc.setIndex(Utility.getRandom(16, 17));
                    oc.randomizeObjects();
                    addScenery(t, oc);
                }
            }
        }
    }

    public void randomize(int exitDirection, AbstractOutdoorLevelModel outdoorLevelModel) throws MalformedURLException {
        int x = width;
        int y = height;
        tiles = new ArrayList<ArrayList<Tile>>();
        ArrayList<Tile> unvisited = new ArrayList<Tile>();
        ArrayList<Tile> unvisitedSecond = new ArrayList<Tile>();
        ArrayList<Tile> visited = new ArrayList<Tile>();
        initializeLevel(x, y, unvisited, unvisitedSecond);
        buildMaze(randomness, unvisited, visited);
        applySparseness(sparseness);
        removeDeadEnds(deadendRemoval, unvisitedSecond);
        if (exitDirection == SOUTH) {
            buildSouthExit(x, y, outdoorLevelModel);
        } else if (exitDirection == EAST) {
            buildEastExit(x, y, outdoorLevelModel);
        }
        placeRooms(roomsNumber, minWidth, maxWidht, minHeight, maxHeight, unvisitedSecond);
        finalizeLevel();
        placeRandomRoomScenery();
        placeRandomCharacters();
    }

    protected void initializeLevel(int x, int y, ArrayList<Tile> unvisited, ArrayList<Tile> unvisitedSecond) {
        for (int i = 0; i < y; i++) {
            ArrayList<Tile> row = new ArrayList<Tile>();
            for (int j = 0; j < x; j++) {
                Tile tile = new Tile();
                tile.setWall(new Wall());
                tile.setX(j);
                tile.setY(i);
                tile.setLevel(this);
                row.add(tile);
                if (i % 2 != 0 && j % 2 != 0) {
                    unvisited.add(tile);
                    unvisitedSecond.add(tile);
                }
            }
            tiles.add(row);
        }
    }

    protected void placeRooms(int roomsCount, int minWidth, int maxWidht, int minHeight, int maxHeight, ArrayList<Tile> unvisitedSecond) {
        System.out.print("Start placing " + roomsCount + " rooms...");
        for (int i = 0; i < roomsCount; i++) {
            int roomWidth = Utility.getRandom(minWidth, maxWidht) * 2 + 1;
            int roomHeight = Utility.getRandom(minHeight, maxHeight) * 2 + 1;
            int best = Integer.MAX_VALUE;
            int score = 0;
            Room room = new Room();
            room.setId(i);
            Coordinates bestCoordinates = new Coordinates();
            for (Tile Tile : unvisitedSecond) {
                try {
                    Coordinates currentCoordinates = Tile.getCoordinates();
                    boolean noAdiacentCorrodors = true;
                    for (int currY = currentCoordinates.y; currY < currentCoordinates.y + roomHeight; currY++) {
                        for (int currX = currentCoordinates.x; currX < currentCoordinates.x + roomWidth; currX++) {
                            Tile t = (Tile) getTileAt(currX, currY);
                            for (Tile elem : t.getFreeNeighborsNoDiagonals()) {
                                if (!elem.isObstacle() && elem.getRoom() == null) {
                                    score += 1;
                                    noAdiacentCorrodors = false;
                                }
                            }
                            if (!t.isObstacle() && t.getRoom() == null) {
                                score += 3;
                            }
                            if (t.getRoom() != null) {
                                score += 100;
                            }
                        }
                    }
                    if (noAdiacentCorrodors) {
                        score = Integer.MAX_VALUE;
                    }
                    if (score < best) {
                        best = score;
                        bestCoordinates.x = currentCoordinates.x;
                        bestCoordinates.y = currentCoordinates.y;
                    }
                } catch (Exception e) {
                }
                score = 0;
            }
            for (int currY = bestCoordinates.y; currY < bestCoordinates.y + roomHeight; currY++) {
                ArrayList<Tile> roomRow = new ArrayList<Tile>();
                for (int currX = bestCoordinates.x; currX < bestCoordinates.x + roomWidth; currX++) {
                    Tile t = (Tile) getTileAt(currX, currY);
                    t.setWall(null);
                    roomRow.add(t);
                    t.setRoom(room);
                }
                room.getTiles().add(roomRow);
            }
            rooms.add(room);
            boolean isolated = true;
            for (Tile tl : room.borders()) {
                if (!tl.isObstacle()) {
                    if (tl.getRoom() == null) {
                        Door door = new Door();
                        if (getTileAt(tl.getCoordinates().downCoordinates()).getWall() == null) {
                            door.setIndex(door.getIndex() + 2);
                        }
                        addScenery(tl, door);
                    }
                    isolated = false;
                }
            }
        }
        System.out.println("Done.");
    }

    protected void removeDeadEnds(int deadendRemoval, ArrayList<Tile> unvisitedSecond) {
        System.out.print("Start removing deadends with deadendRemoval: " + deadendRemoval + "%...");
        for (Tile Tile : unvisitedSecond) {
            Tile current = (Tile) Tile;
            int random = Utility.getRandom(0, 100);
            if (random <= deadendRemoval) {
                List<Tile> tiles = current.getCandidateTiles();
                boolean corridorFound = false;
                while (tiles.size() > 0 && !corridorFound) {
                    if (!tiles.get(1).isObstacle()) {
                        corridorFound = true;
                        tiles.get(0).setWall(null);
                    } else {
                        tiles.get(1).setWall(null);
                        tiles.get(0).setWall(null);
                        tiles = tiles.get(1).getCandidateTiles();
                    }
                }
            }
        }
        System.out.println("Done.");
    }

    protected void applySparseness(int sparseness) {
        System.out.print("Start applying sparseness: " + sparseness + "...");
        for (int i = 0; i < sparseness; i++) {
            for (ArrayList<Tile> list : tiles) {
                for (Tile tile : list) {
                    if (tile.isFree()) {
                        Tile freeTile = tile.getDeadEndFreeTile();
                        if (freeTile != null) {
                            tile.setWall(new Wall());
                            freeTile.setWall(new Wall());
                        }
                    }
                }
            }
        }
        System.out.println("Done.");
    }

    protected void buildMaze(int randomness, ArrayList<Tile> unvisited, ArrayList<Tile> visited) {
        System.out.print("Start building maze with randomness: " + randomness + "%...");
        Tile currentTile = (Tile) unvisited.get(Utility.getRandom(0, unvisited.size() - 1));
        unvisited.remove(currentTile);
        visited.add(currentTile);
        int direction = Utility.getRandom(0, 3);
        while (unvisited.size() > 0) {
            List<Integer> directions = new ArrayList<Integer>();
            for (int i = 0; i < 4; i++) {
                directions.add(new Integer(i));
            }
            boolean invalid = true;
            Tile newTile = null;
            while (invalid && directions.size() > 0) {
                int random = Utility.getRandom(0, 100);
                if (random < randomness) {
                    direction = directions.remove(Utility.getRandom(0, directions.size() - 1));
                }
                newTile = (Tile) getTileAt(currentTile.getCoordinates().getCoordinatesAt(direction).getCoordinatesAt(direction));
                if (newTile != null && !visited.contains(newTile)) {
                    invalid = false;
                } else if (directions.size() > 0) {
                    direction = directions.remove(Utility.getRandom(0, directions.size() - 1));
                }
            }
            if (!invalid) {
                currentTile = makeCorridor(unvisited, visited, currentTile, direction, newTile);
            } else {
                currentTile = (Tile) visited.get(Utility.getRandom(0, visited.size() - 1));
            }
        }
        System.out.println("Done.");
    }

    protected Tile makeCorridor(ArrayList<Tile> unvisited, ArrayList<Tile> visited, Tile currentTile, int direction, Tile newTile) {
        currentTile.setWall(null);
        newTile.setWall(null);
        Tile midTile = (Tile) getTileAt(currentTile.getCoordinates().getCoordinatesAt(direction));
        midTile.setWall(null);
        unvisited.remove(newTile);
        visited.add(newTile);
        currentTile = newTile;
        return currentTile;
    }

    protected void placeRandomCharacters() throws MalformedURLException {
        int n = (getWidth() * getHeight()) / 350;
        for (int i = 0; i < 3 * n; i++) {
            CustomCharacter punk = CustomCharacter.createPunk();
            addCharacter(punk, randomFreeTile());
        }
    }

    public int getDeadendRemoval() {
        return deadendRemoval;
    }

    public void setDeadendRemoval(int deadendRemoval) {
        this.deadendRemoval = deadendRemoval;
    }

    public int getRandomness() {
        return randomness;
    }

    public void setRandomness(int randomness) {
        this.randomness = randomness;
    }

    public int getSparseness() {
        return sparseness;
    }

    public void setSparseness(int sparseness) {
        this.sparseness = sparseness;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxWidht() {
        return maxWidht;
    }

    public void setMaxWidht(int maxWidht) {
        this.maxWidht = maxWidht;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getRoomsNumber() {
        return roomsNumber;
    }

    public void setRoomsNumber(int roomsNumber) {
        this.roomsNumber = roomsNumber;
    }
}
