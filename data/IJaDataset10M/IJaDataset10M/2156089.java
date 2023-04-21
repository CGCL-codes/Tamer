package net.sourceforge.javagg.island3d;

import java.util.*;
import java.awt.*;

/**
 * Takes a blank map surface and generates elevation data by picking a 
 * random path from a high elevation to a low elevation then picking new random 
 * paths around the first and second and so on. Uses recursive construction to 
 * achive this path generation. Once done you have something like the top down 
 * view of a tree. When projected on a 3d surface it resembles a volcanic island. 
 * I am particularly proud of this class though it is a simple concept.
 *
 * @author Larry Gray
 * @version 1.1
 */
public class RandomTurtle {

    /** Starting elevation or top elevation. */
    private static int highestElevation = 50;

    /** Lenght of map in number of points.*/
    private static int mapLength = 50;

    /** Width of map in number of points.*/
    private static int mapWidth = 50;

    /** Rectangular area of map elevation values. */
    public static int[][] map = new int[mapWidth][mapLength];

    /** Maximum number of branches for a given level. */
    private static int numberBranches = 3;

    /**
     * Inializes the map.
     */
    public static void clearMap() {
        RandomTurtle.map = new int[mapWidth][mapLength];
    }

    /**
	 * Gets the map data.
	 * @return int[][] map data.
	 */
    public static int[][] getMap() {
        return map;
    }

    /** Current number of branches used. Usually 1 to 3. */
    private int branches;

    /** An elevation color model set. */
    private ElevationColorModel[] colorModel = new ElevationColorModel[] { new ElevationColorModel(0, 3, Color.blue), new ElevationColorModel(4, 8, Color.cyan), new ElevationColorModel(9, 30, Color.green), new ElevationColorModel(31, 35, Color.darkGray), new ElevationColorModel(36, 40, Color.lightGray), new ElevationColorModel(41, 50, Color.white), new ElevationColorModel(51, 55, Color.red), new ElevationColorModel(56, 60, Color.yellow) };

    /** Number of elevation ranges.*/
    private int numberElevations = 8;

    /**
	 * Builds a RandomTurle using a map point and a grahics context.
	 */
    public RandomTurtle(MapPoint mapPoint, Graphics g) {
        int x = mapPoint.getX();
        int y = mapPoint.getY();
        int elevation = mapPoint.getElevation();
        branches = 0;
        ArrayList mapPoints;
        int count = 0;
        while (branches < RandomTurtle.numberBranches) {
            count++;
            int[] xPoints = { x * 5, x * 5 + 5, x * 5 + 5, x * 5 };
            int[] yPoints = { y * 5, y * 5, y * 5 + 5, y * 5 + 5 };
            g.setColor(this.getColor(elevation));
            g.fillPolygon(xPoints, yPoints, 4);
            mapPoints = new ArrayList();
            if (RandomTurtle.map[x - 1][y] == 0) mapPoints.add(new MapPoint(x - 1, y, 0));
            if (RandomTurtle.map[x - 1][y - 1] == 0) mapPoints.add(new MapPoint(x - 1, y - 1, 0));
            if (RandomTurtle.map[x][y - 1] == 0) mapPoints.add(new MapPoint(x, y - 1, 0));
            if (RandomTurtle.map[x + 1][y - 1] == 0) mapPoints.add(new MapPoint(x + 1, y - 1, 0));
            if (RandomTurtle.map[x + 1][y] == 0) mapPoints.add(new MapPoint(x + 1, y, 0));
            if (RandomTurtle.map[x + 1][y + 1] == 0) mapPoints.add(new MapPoint(x + 1, y + 1, 0));
            if (RandomTurtle.map[x][y + 1] == 0) mapPoints.add(new MapPoint(x, y + 1, 0));
            if (RandomTurtle.map[x - 1][y + 1] == 0) mapPoints.add(new MapPoint(x - 1, y + 1, 0));
            if (elevation > 0) {
                if (mapPoints.size() > 1) {
                    MapPoint newPoint = (MapPoint) mapPoints.get((int) (Math.random() * mapPoints.size()));
                    newPoint.setElevation(elevation - 1);
                    map[x][y] = elevation;
                    RandomTurtle randomTurtle = new RandomTurtle(newPoint, g);
                    this.branches++;
                    continue;
                } else if (mapPoints.size() == 1) {
                    MapPoint newPoint = (MapPoint) mapPoints.get(0);
                    newPoint.setElevation(elevation - 1);
                    map[x][y] = elevation;
                    RandomTurtle randomTurtle = new RandomTurtle(newPoint, g);
                    this.branches++;
                    continue;
                } else break;
            } else break;
        }
    }

    /**
	 *  Gets the color for a given elevation.
	 *  @return Color a color of the elevation.
	 */
    public Color getColor(int elevation) {
        for (int i = 0; i < this.numberElevations; i++) {
            Color tempColor;
            tempColor = colorModel[i].getColor(elevation);
            if (tempColor != null) return tempColor;
        }
        return Color.white;
    }

    /**
	 * @return Returns the highestElevation.
	 */
    public static int getHighestElevation() {
        return highestElevation;
    }

    /**
	 * @param highestElevation The highestElevation to set.
	 */
    public static void setHighestElevation(int highestElevation) {
        RandomTurtle.highestElevation = highestElevation;
    }

    /**
	 * @return Returns the mapLength.
	 */
    public static int getMapLength() {
        return mapLength;
    }

    /**
	 * @param mapLength The mapLength to set.
	 */
    public static void setMapLength(int mapLength) {
        RandomTurtle.mapLength = mapLength;
    }

    /**
	 * @return Returns the mapWidth.
	 */
    public static int getMapWidth() {
        return mapWidth;
    }

    /**
	 * @param mapWidth The mapWidth to set.
	 */
    public static void setMapWidth(int mapWidth) {
        RandomTurtle.mapWidth = mapWidth;
    }

    /**
	 * @return Returns the numberBranches.
	 */
    public static int getNumberBranches() {
        return numberBranches;
    }

    /**
	 * @param numberBranches The numberBranches to set.
	 */
    public static void setNumberBranches(int numberBranches) {
        RandomTurtle.numberBranches = numberBranches;
    }

    /**
	 * @return Returns the branches.
	 */
    public int getBranches() {
        return branches;
    }

    /**
	 * @param branches The branches to set.
	 */
    public void setBranches(int branches) {
        this.branches = branches;
    }

    /**
	 * @return Returns the colorModel.
	 */
    public ElevationColorModel[] getColorModel() {
        return colorModel;
    }

    /**
	 * @param colorModel The colorModel to set.
	 */
    public void setColorModel(ElevationColorModel[] colorModel) {
        this.colorModel = colorModel;
    }

    /**
	 * @return Returns the numberElevations.
	 */
    public int getNumberElevations() {
        return numberElevations;
    }

    /**
	 * @param numberElevations The numberElevations to set.
	 */
    public void setNumberElevations(int numberElevations) {
        this.numberElevations = numberElevations;
    }

    /**
	 * @param map The map to set.
	 */
    public static void setMap(int[][] map) {
        RandomTurtle.map = map;
    }
}
