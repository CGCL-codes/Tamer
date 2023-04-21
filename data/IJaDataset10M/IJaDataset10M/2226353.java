package gameobjects.map;

import gameobjects.GameMessage;
import gui.GameFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import main.MainRoutine;
import utils.Pathfinder;

/**
 *
 * @author babene
 */
public abstract class AbstractMap {

    public static final int MAP_WIDTH = 40;

    public static final int MAP_HEIGHT = 20;

    public static final int SQUARE_SIZE = 20;

    public static Dimension mapDim = new Dimension(MAP_WIDTH * SQUARE_SIZE, MAP_HEIGHT * SQUARE_SIZE);

    protected MapSquare[][] mapSquares;

    protected MapSquare startSquare, goalSquare;

    protected List<MapSquare> blockList;

    protected boolean showGrid;

    public AbstractMap() {
        blockList = Collections.synchronizedList(new LinkedList());
        showGrid = false;
        mapSquares = new MapSquare[MAP_WIDTH][MAP_HEIGHT];
        insertSquares();
        setNaturalFlags();
    }

    public void insertSquares() {
        for (int i = 0; i < mapSquares.length; i++) {
            for (int j = 0; j < mapSquares[i].length; j++) {
                mapSquares[i][j] = new MapSquare(i, j);
            }
        }
        setNeighbors();
    }

    private void setNeighbors() {
        for (int i = 0; i < mapSquares.length; i++) {
            for (int j = 0; j < mapSquares[i].length; j++) {
                if (i > 0 && i < mapSquares.length - 1 && j > 0 && j < mapSquares[i].length - 1) {
                    mapSquares[i][j].setNeighbor(mapSquares[i - 1][j - 1], MapSquare.NEIGHBOR_UPPER_LEFT);
                    mapSquares[i][j].setNeighbor(mapSquares[i][j - 1], MapSquare.NEIGHBOR_UPPER);
                    mapSquares[i][j].setNeighbor(mapSquares[i + 1][j - 1], MapSquare.NEIGHBOR_UPPER_RIGHT);
                    mapSquares[i][j].setNeighbor(mapSquares[i - 1][j], MapSquare.NEIGHBOR_LEFT);
                    mapSquares[i][j].setNeighbor(mapSquares[i + 1][j], MapSquare.NEIGHBOR_RIGHT);
                    mapSquares[i][j].setNeighbor(mapSquares[i - 1][j + 1], MapSquare.NEIGHBOR_LOWER_LEFT);
                    mapSquares[i][j].setNeighbor(mapSquares[i][j + 1], MapSquare.NEIGHBOR_LOWER);
                    mapSquares[i][j].setNeighbor(mapSquares[i + 1][j + 1], MapSquare.NEIGHBOR_LOWER_RIGHT);
                } else if (i == 0) {
                    mapSquares[i][j].setNeighbor(mapSquares[i + 1][j], MapSquare.NEIGHBOR_RIGHT);
                    if (j != mapSquares[i].length - 1) {
                        mapSquares[i][j].setNeighbor(mapSquares[i][j + 1], MapSquare.NEIGHBOR_LOWER);
                        mapSquares[i][j].setNeighbor(mapSquares[i + 1][j + 1], MapSquare.NEIGHBOR_LOWER_RIGHT);
                    }
                    if (j != 0) {
                        mapSquares[i][j].setNeighbor(mapSquares[i][j - 1], MapSquare.NEIGHBOR_UPPER);
                        mapSquares[i][j].setNeighbor(mapSquares[i + 1][j - 1], MapSquare.NEIGHBOR_UPPER_RIGHT);
                    }
                } else if (i == mapSquares.length - 1) {
                    mapSquares[i][j].setNeighbor(mapSquares[i - 1][j], MapSquare.NEIGHBOR_LEFT);
                    if (j != mapSquares[i].length - 1) {
                        mapSquares[i][j].setNeighbor(mapSquares[i][j + 1], MapSquare.NEIGHBOR_LOWER);
                        mapSquares[i][j].setNeighbor(mapSquares[i - 1][j + 1], MapSquare.NEIGHBOR_LOWER_LEFT);
                    }
                    if (j != 0) {
                        mapSquares[i][j].setNeighbor(mapSquares[i][j - 1], MapSquare.NEIGHBOR_UPPER);
                        mapSquares[i][j].setNeighbor(mapSquares[i - 1][j - 1], MapSquare.NEIGHBOR_UPPER_LEFT);
                    }
                } else if (j == 0) {
                    mapSquares[i][j].setNeighbor(mapSquares[i][j + 1], MapSquare.NEIGHBOR_LOWER);
                    if (i != mapSquares.length - 1) {
                        mapSquares[i][j].setNeighbor(mapSquares[i + 1][j], MapSquare.NEIGHBOR_RIGHT);
                        mapSquares[i][j].setNeighbor(mapSquares[i + 1][j + 1], MapSquare.NEIGHBOR_LOWER_RIGHT);
                    }
                    if (i != 0) {
                        mapSquares[i][j].setNeighbor(mapSquares[i - 1][j], MapSquare.NEIGHBOR_LEFT);
                        mapSquares[i][j].setNeighbor(mapSquares[i - 1][j + 1], MapSquare.NEIGHBOR_LOWER_LEFT);
                    }
                } else if (j == mapSquares[i].length - 1) {
                    mapSquares[i][j].setNeighbor(mapSquares[i][j - 1], MapSquare.NEIGHBOR_UPPER);
                    if (i != mapSquares.length - 1) {
                        mapSquares[i][j].setNeighbor(mapSquares[i + 1][j], MapSquare.NEIGHBOR_RIGHT);
                        mapSquares[i][j].setNeighbor(mapSquares[i + 1][j - 1], MapSquare.NEIGHBOR_UPPER_RIGHT);
                    }
                    if (i != 0) {
                        mapSquares[i][j].setNeighbor(mapSquares[i - 1][j], MapSquare.NEIGHBOR_LEFT);
                        mapSquares[i][j].setNeighbor(mapSquares[i - 1][j - 1], MapSquare.NEIGHBOR_UPPER_LEFT);
                    }
                }
            }
        }
    }

    public boolean setSquareFlag(double x, double y, int flag) {
        MapSquare currentSquare = getSquareAt(x, y);
        if (currentSquare.getFlag() != MapSquare.FLAG_START && currentSquare.getFlag() != MapSquare.FLAG_FINISH) {
            if (flag == MapSquare.FLAG_BLOCKED && currentSquare.getFlag() != MapSquare.FLAG_BLOCKED) {
                if (currentSquare.getFlag() == MapSquare.FLAG_PATH || ((currentSquare.getNeighbor(1) != null && currentSquare.getNeighbor(1).getFlag() == MapSquare.FLAG_PATH) || (currentSquare.getNeighbor(3) != null && currentSquare.getNeighbor(3).getFlag() == MapSquare.FLAG_PATH) || (currentSquare.getNeighbor(4) != null && currentSquare.getNeighbor(4).getFlag() == MapSquare.FLAG_PATH) || (currentSquare.getNeighbor(6) != null && currentSquare.getNeighbor(6).getFlag() == MapSquare.FLAG_PATH))) {
                    Pathfinder.resetPath();
                    currentSquare.setFlag(flag);
                    if (MainRoutine.getGameRoutine().calcPath()) {
                        return true;
                    } else {
                        currentSquare.setFlag(MapSquare.FLAG_FREE);
                        MainRoutine.getGameRoutine().calcPath();
                        return false;
                    }
                } else {
                    currentSquare.setFlag(flag);
                    return true;
                }
            } else if (flag == MapSquare.FLAG_FREE && currentSquare.getFlag() == MapSquare.FLAG_BLOCKED) {
                currentSquare.setFlag(flag);
                Pathfinder.resetPath();
                MainRoutine.getGameRoutine().calcPath();
                return true;
            } else if (flag == MapSquare.FLAG_BLOCKED && currentSquare.getFlag() == MapSquare.FLAG_BLOCKED) {
                MainRoutine.getGameRoutine().getMessageManager().addMessage("Square blocked", GameMessage.MESSAGE_TYPE_ERROR);
                return false;
            } else {
                getSquareAt(x, y).setFlag(flag);
                return true;
            }
        } else {
            MainRoutine.getGameRoutine().getMessageManager().addMessage("Error: You can not build on start or finish Square", GameMessage.MESSAGE_TYPE_ERROR);
            return false;
        }
    }

    public void paint(Graphics2D g2) {
        if (startSquare != null && goalSquare != null) {
            startSquare.paint(g2);
            goalSquare.paint(g2);
        }
        for (Iterator<MapSquare> blockSquares = blockList.iterator(); blockSquares.hasNext(); ) {
            blockSquares.next().paint(g2);
        }
    }

    public MapSquare[][] getMapSquares() {
        return mapSquares;
    }

    public MapSquare getSquareAt(double x, double y) {
        return mapSquares[(int) x / SQUARE_SIZE][(int) y / SQUARE_SIZE];
    }

    protected abstract void setNaturalFlags();

    public void showGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }
}
