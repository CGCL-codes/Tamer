package org.iskar.mapping.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class MapPoint {

    public static final String ENTRANCE = "Entrance";

    public static final String ANTENNA = "Antenna";

    public static final String DEAD_END = "Dead End";

    public static final String MIDDLE_CUT_POINT = "Middle Cut Point";

    public static final String CONTOUR = "Contour";

    private String type;

    private String name;

    private String notes;

    private List<RawVector> startingVectors;

    private List<RawVector> endingVectors;

    private List<MapPoint> neightbours;

    private MapPoint nearestAntennaPoint;

    private MapPoint antennaStartPoint;

    private Point hpPosition;

    private Point vcPosition;

    private Point dcPosition;

    private Double x;

    private Double y;

    private Double z;

    public MapPoint() {
        startingVectors = new ArrayList<RawVector>();
        endingVectors = new ArrayList<RawVector>();
        neightbours = new ArrayList<MapPoint>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<RawVector> getStartingVectors() {
        return startingVectors;
    }

    public void setStartingVectors(List<RawVector> startingVectors) {
        this.startingVectors = startingVectors;
    }

    public List<RawVector> getEndingVectors() {
        return endingVectors;
    }

    public void setEndingVectors(List<RawVector> endingVectors) {
        this.endingVectors = endingVectors;
    }

    public List<MapPoint> getNeightbours() {
        return neightbours;
    }

    public void setNeightbours(List<MapPoint> neightbours) {
        this.neightbours = neightbours;
    }

    public MapPoint getNearestAntennaPoint() {
        return nearestAntennaPoint;
    }

    public void setNearestAntennaPoint(MapPoint nearestAntennaPoint) {
        this.nearestAntennaPoint = nearestAntennaPoint;
    }

    public Point getHpPosition() {
        return hpPosition;
    }

    public void setHpPosition(Point hpPosition) {
        this.hpPosition = hpPosition;
    }

    public Point getVcPosition() {
        return vcPosition;
    }

    public void setVcPosition(Point vcPosition) {
        this.vcPosition = vcPosition;
    }

    public Point getDcPosition() {
        return dcPosition;
    }

    public void setDcPosition(Point dcPosition) {
        this.dcPosition = dcPosition;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public MapPoint getAntennaStartPoint() {
        return antennaStartPoint;
    }

    public void setAntennaStartPoint(MapPoint antennaStartPoint) {
        this.antennaStartPoint = antennaStartPoint;
    }

    public boolean isFromAntenna() {
        return type.equals(ANTENNA) || type.equals(ENTRANCE) || type.equals(DEAD_END);
    }
}
