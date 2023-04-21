package com.mia.sct.data.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * MapLocationModel.java
 * 
 * Model for map location objects
 * 
 * @author Devon Bryant
 * @since Nov 24, 2007
 */
public class MapLocationModel implements Cloneable {

    /** Shape types */
    public static final String SHAPE_RECT_TYPE = "RECT";

    public static final String SHAPE_CIRCLE_TYPE = "CIRCLE";

    public static final String SHAPE_ROUNDRECT_TYPE = "ROUNDRECT";

    private String groupRefID = null;

    private LocationTypeEnum locationType = null;

    private String locationID = null;

    private int shapeWidth = 0;

    private int shapeHeight = 0;

    private int arcWidth = 0;

    private int arcHeight = 0;

    private String shapeType = null;

    private List<PointModel> points = new ArrayList<PointModel>();

    /**
	 * Get the group reference id (tuning group reference)
	 * @return the groupRefID
	 */
    public String getGroupRefID() {
        return groupRefID;
    }

    /**
	 * Set the group reference id (tuning group reference)
	 * @param inGroupRefID the groupRefID to set
	 */
    public void setGroupRefID(String inGroupRefID) {
        groupRefID = inGroupRefID;
    }

    /**
	 * Get the location type (Shape, ComplexShape)
	 * @return the locationType
	 */
    public LocationTypeEnum getLocationType() {
        return locationType;
    }

    /**
	 * Set the location type (Point, Shape, ComplexShape)
	 * @param inLocationType the locationType to set
	 */
    public void setLocationType(LocationTypeEnum inLocationType) {
        locationType = inLocationType;
    }

    /**
	 * Get the location id
	 * @return the locationID
	 */
    public String getLocationID() {
        return locationID;
    }

    /**
	 * Set the location id
	 * @param inLocationID the locationID to set
	 */
    public void setLocationID(String inLocationID) {
        locationID = inLocationID;
    }

    /**
	 * Set the location width (only applies to basic shape types)
	 * @param inWidth the shape width
	 */
    public void setWidth(int inWidth) {
        shapeWidth = inWidth;
    }

    /**
	 * Get the shapes width
	 * @return Width int
	 */
    public int getWidth() {
        return shapeWidth;
    }

    /**
	 * Set the location height (only applies to basic shape types)
	 * @param inHeight the shape height
	 */
    public void setHeight(int inHeight) {
        shapeHeight = inHeight;
    }

    /**
	 * Get the shapes height
	 * @return Height int
	 */
    public int getHeight() {
        return shapeHeight;
    }

    /**
	 * Get the arc width (used for round rectangles)
	 * @return the arc width
	 */
    public int getArcWidth() {
        return arcWidth;
    }

    /**
	 * Set the arc width (used for round rectangles)
	 * @param inArcWidth the arc width to set
	 */
    public void setArcWidth(int inArcWidth) {
        arcWidth = inArcWidth;
    }

    /**
	 * Get the arc height (used for round rectangles)
	 * @return the arc height
	 */
    public int getArcHeight() {
        return arcHeight;
    }

    /**
	 * Set the arc height (used for round rectangles)
	 * @param inArcHeight the arc height to set
	 */
    public void setArcHeight(int inArcHeight) {
        arcHeight = inArcHeight;
    }

    /**
	 * Add a point model to the list of points
	 * Only complex shapes have multiple points
	 * @param inPointModel
	 */
    public void addPoint(PointModel inPointModel) {
        points.add(inPointModel);
    }

    /**
	 * Get the list of point models for this location
	 * @return list of point models
	 */
    public List<PointModel> getPoints() {
        return points;
    }

    /**
	 * Get the shape type (RECT, CIRCLE) for basic shapes only
	 * @return the shapeType
	 */
    public String getShapeType() {
        return shapeType;
    }

    /**
	 * Set the shape type (RECT, CIRCLE) for basic shapes only
	 * @param inShapeType the shapeType to set
	 */
    public void setShapeType(String inShapeType) {
        shapeType = inShapeType;
    }

    public Object clone() {
        MapLocationModel result = null;
        String clnGroupRefID = null;
        String clnLocationID = null;
        String clnShapeType = null;
        result = new MapLocationModel();
        if (groupRefID != null) {
            clnGroupRefID = new String(groupRefID);
        }
        result.setGroupRefID(clnGroupRefID);
        result.setLocationType(locationType);
        if (locationID != null) {
            clnLocationID = new String(locationID);
        }
        result.setLocationID(clnLocationID);
        result.setWidth(shapeWidth);
        result.setHeight(shapeHeight);
        result.setArcWidth(arcWidth);
        result.setArcHeight(arcHeight);
        if (shapeType != null) {
            clnShapeType = new String(shapeType);
        }
        result.setShapeType(clnShapeType);
        for (PointModel pointModel : points) {
            result.addPoint((PointModel) pointModel.clone());
        }
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
