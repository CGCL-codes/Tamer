package com.mia.sct.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClickMapModel.java
 * 
 * The model for a click map object
 * 
 * @author Devon Bryant
 * @since Nov 24, 2007
 */
public class ClickMapModel implements Cloneable {

    private String clickMapID = null;

    private List<ActionModel> actionList = new ArrayList<ActionModel>();

    private Map<String, MapLocationModel> mapLocationsMap = new HashMap<String, MapLocationModel>();

    /**
	 * Get the click map id
	 * @return the clickMapID
	 */
    public String getClickMapID() {
        return clickMapID;
    }

    /**
	 * Set the click map id
	 * @param inClickMapID the clickMapID to set
	 */
    public void setClickMapID(String inClickMapID) {
        clickMapID = inClickMapID;
    }

    /**
	 * Add a new action model to the list of actions
	 * @param inActionModel
	 */
    public void addActionModel(ActionModel inActionModel) {
        if (actionList.contains(inActionModel)) {
            actionList.remove(inActionModel);
        }
        actionList.add(inActionModel);
    }

    /**
	 * Get the list of action models
	 * @return List of action models
	 */
    public List<ActionModel> getActionModels() {
        return actionList;
    }

    /**
	 * Add a new map location
	 * @param inLocationID the location id
	 * @param inMapLocation the map location model
	 */
    public void addMapLocation(String inLocationID, MapLocationModel inMapLocation) {
        mapLocationsMap.put(inLocationID, inMapLocation);
    }

    /**
	 * Get a map location by its id
	 * @param inLocationID the map location id
	 * @return
	 */
    public MapLocationModel getMapLocation(String inLocationID) {
        return mapLocationsMap.get(inLocationID);
    }

    /**
	 * Get the map location map
	 * @return map location map
	 */
    public Map<String, MapLocationModel> getMapLocationMap() {
        return mapLocationsMap;
    }

    public Object clone() {
        ClickMapModel result = null;
        String clnClickMapID = null;
        MapLocationModel clnMapLocationModel = null;
        result = new ClickMapModel();
        if (clickMapID != null) {
            clnClickMapID = new String(clickMapID);
        }
        result.setClickMapID(clnClickMapID);
        for (MapLocationModel mapLocationModel : mapLocationsMap.values()) {
            clnMapLocationModel = (MapLocationModel) mapLocationModel.clone();
            result.addMapLocation(clnMapLocationModel.getLocationID(), clnMapLocationModel);
        }
        for (ActionModel actionModel : actionList) {
            result.addActionModel((ActionModel) actionModel.clone());
        }
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
