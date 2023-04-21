package model;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import mapeditor.VectorPainter.*;

/**
 * @author Rita Kovord�nyi
 * @author Jonas Kvarnstr�m
 */
@SuppressWarnings("serial")
public class BuildingModel extends ShapeModel implements Serializable {

    private HashMap<Point, MultiPath> allPaths;

    /**
	 * Creates new BuildingModel
	 * 
	 * @param x
	 *            position x
	 * @param y
	 *            position y
	 * @param pop
	 *            number of residents
	 * @param lvl
	 *            building level
	 * @param owner
	 *            Player
	 * @param buildType
	 *            Buildings
	 */
    public BuildingModel(int x, int y, int pop, int lvl, Player owner, Buildings buildType) {
        super(x, y, pop, lvl, owner, buildType);
    }

    @Override
    public void addPoint(int x, int y) {
        return;
    }

    @Override
    public boolean removePoint(int x, int y) {
        return false;
    }

    @Override
    public Polygon getPoly() {
        p.reset();
        int r = 75;
        for (double angle = 0; angle < (2 * Math.PI); angle = angle + (2 * Math.PI) / 6) {
            p.addPoint((int) (this.getX() + Math.cos(angle) * r), (int) (this.getY() + Math.sin(angle) * r));
        }
        return p;
    }

    @Override
    public BufferedImage getTexture() {
        return null;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public int getLevel() {
        return super.level;
    }

    @Override
    public int getPopulation() {
        return super.population;
    }

    @Override
    public ShapeModel clone(int x, int y, String image, int pop, int lvl, Player owner, Buildings buildType) {
        return new BuildingModel(x, y, super.population, super.level, super.getOwner(), super.getBuildingType());
    }

    @Override
    public int findPoint(int x2, int y2) {
        return -1;
    }

    @Override
    public void movePoint(int n, int x, int y) {
        return;
    }

    @Override
    public boolean isBuilding() {
        return true;
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public boolean isPolygon() {
        return false;
    }

    @Override
    public MultiPath getPath(Point endPoint) {
        return allPaths.get(endPoint);
    }

    /**
	 * Returns all paths as a MultiPath (contains 8 paths to all other
	 * buildings).
	 */
    public HashMap<Point, MultiPath> getAllPaths() {
        return allPaths;
    }

    @Override
    public Set<Point> getKeySet() {
        return allPaths.keySet();
    }

    @Override
    public void setPaths(HashMap<Point, MultiPath> map) {
        allPaths = map;
    }
}
