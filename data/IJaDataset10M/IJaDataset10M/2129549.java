package com.rlsoftwares.virtualdeck;

import com.rlsoftwares.games.card.Orientation;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author rdcl
 */
public class CoordenateManager {

    private static int V_UNDER = 0;

    private static int V_RIGHT = 1;

    private static int V_ABOVE = 2;

    private static int V_LEFT = 3;

    private static int V_UP = 2;

    private static int V_DOWN = 0;

    private ArrayList<TableCorner> tableCorners = new ArrayList();

    private ArrayList<Orientation> handlersOrientation = new ArrayList();

    private int width;

    private int height;

    private Point center;

    public CoordenateManager(int planWidth, int planHeigth) {
        this.setCenter(new Point((int) (planWidth / 2), (int) (planHeigth / 2)));
        this.setWidth(planWidth);
        this.setHeight(planHeigth);
        tableCorners.add(V_UNDER, TableCorner.UNDER);
        tableCorners.add(V_RIGHT, TableCorner.RIGHT);
        tableCorners.add(V_ABOVE, TableCorner.ABOVE);
        tableCorners.add(V_LEFT, TableCorner.LEFT);
        handlersOrientation.add(V_DOWN, Orientation.DOWN);
        handlersOrientation.add(V_RIGHT, Orientation.RIGHT);
        handlersOrientation.add(V_UP, Orientation.UP);
        handlersOrientation.add(V_LEFT, Orientation.LEFT);
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public Point getCenter() {
        return center;
    }

    private void setCenter(Point center) {
        this.center = center;
    }

    private Point getRelativePoint(int x, int y) {
        return new Point(x - this.getCenter().x, y - this.getCenter().y);
    }

    private Orientation getHandlerOrientation(int value) {
        return this.getHandlersOrientation().get(value);
    }

    private TableCorner getTableCorner(int value) {
        return this.getTableCorners().get(value);
    }

    private int getTableCornerValue(TableCorner p) {
        return this.getTableCorners().indexOf(p);
    }

    private int getHandlerOrientationValue(Orientation o) {
        return this.getHandlersOrientation().indexOf(o);
    }

    public Orientation getRelativeHandlerOrientation(TableCorner local, TableCorner remoteTableCorner, Orientation remoteOrientation) {
        int[][][] relativeMatrix = { { { V_DOWN, V_RIGHT, V_UP, V_LEFT }, { V_RIGHT, V_UP, V_LEFT, V_DOWN }, { V_UP, V_LEFT, V_DOWN, V_RIGHT }, { V_LEFT, V_DOWN, V_RIGHT, V_UP } }, { { V_LEFT, V_DOWN, V_RIGHT, V_UP }, { V_DOWN, V_RIGHT, V_UP, V_LEFT }, { V_RIGHT, V_UP, V_LEFT, V_DOWN }, { V_UP, V_LEFT, V_DOWN, V_RIGHT } }, { { V_UP, V_LEFT, V_DOWN, V_RIGHT }, { V_LEFT, V_DOWN, V_RIGHT, V_UP }, { V_DOWN, V_RIGHT, V_UP, V_LEFT }, { V_RIGHT, V_UP, V_LEFT, V_DOWN } }, { { V_RIGHT, V_UP, V_LEFT, V_DOWN }, { V_UP, V_LEFT, V_DOWN, V_RIGHT }, { V_LEFT, V_DOWN, V_RIGHT, V_UP }, { V_DOWN, V_RIGHT, V_UP, V_LEFT } } };
        return getHandlerOrientation(relativeMatrix[getTableCornerValue(local)][getTableCornerValue(remoteTableCorner)][getHandlerOrientationValue(remoteOrientation)]);
    }

    public TableCorner getRelativeTableCorner(TableCorner local, TableCorner remote) {
        int[][] relativeMatrix = { { V_UNDER, V_RIGHT, V_ABOVE, V_LEFT }, { V_LEFT, V_UNDER, V_RIGHT, V_ABOVE }, { V_ABOVE, V_LEFT, V_UNDER, V_RIGHT }, { V_RIGHT, V_ABOVE, V_LEFT, V_UNDER } };
        return getTableCorner(relativeMatrix[getTableCornerValue(local)][getTableCornerValue(remote)]);
    }

    public Point getCreationPoint(TableCorner local, TableCorner remote, Point remotePoint, Dimension componentSize) {
        Point creationPoint = this.getLocalPoint(local, remote, remotePoint);
        switch(this.getRelativeTableCorner(local, remote)) {
            case RIGHT:
                creationPoint.translate(0, -(int) componentSize.getHeight());
                break;
            case LEFT:
                creationPoint.translate(-(int) componentSize.getHeight(), 0);
                break;
            case ABOVE:
                creationPoint.translate(-(int) componentSize.getHeight(), -(int) componentSize.getHeight());
                break;
        }
        return creationPoint;
    }

    /**
     * Get local point based on my relative position from remote position
     * Ex.: if my table corner is under e remote is right, my relative is left!
     */
    public Point getLocalPoint(TableCorner local, TableCorner remote, Point remotePoint) {
        Point remoteRelativePoint = getRelativePoint(remotePoint.x, remotePoint.y);
        Point localPoint = new Point(remoteRelativePoint);
        int remoteX = remoteRelativePoint.x;
        int remoteY = remoteRelativePoint.y;
        int x = remoteRelativePoint.x;
        int y = remoteRelativePoint.y;
        switch(this.getRelativeTableCorner(local, remote)) {
            case RIGHT:
                x = remoteY;
                y = -remoteX;
                break;
            case LEFT:
                x = -remoteY;
                y = remoteX;
                break;
            case UNDER:
                x = remoteX;
                y = remoteY;
                break;
            case ABOVE:
                x = -remoteX;
                y = -remoteY;
                break;
        }
        localPoint.move(x, y);
        localPoint.translate(this.getCenter().x, this.getCenter().y);
        return localPoint;
    }

    private ArrayList<TableCorner> getTableCorners() {
        return tableCorners;
    }

    public ArrayList<Orientation> getHandlersOrientation() {
        return handlersOrientation;
    }

    public void setHandlersOrientation(ArrayList<Orientation> handlersOrientation) {
        this.handlersOrientation = handlersOrientation;
    }
}
