package com.sks.web.formbean.house;

import com.sks.web.formbean.BaseForm;

public class FavoritesForm extends BaseForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8735238061432421509L;

    private Integer buildingId;

    private Integer houseId;

    private Integer userId;

    private String houseName;

    private String buildingName;

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingName() {
        return buildingName;
    }
}
