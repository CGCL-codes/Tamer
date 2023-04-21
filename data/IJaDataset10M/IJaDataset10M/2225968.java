package org.ala.layers.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Objects {

    private String id;

    private String pid;

    private String description;

    private String name;

    private String fid;

    private String fieldname;

    private String geometry;

    private int name_id;

    private String bbox;

    private Double area_km;

    private Double degrees;

    private Double distance;

    private String wmsurl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getName_id() {
        return name_id;
    }

    public void setName_id(int nameId) {
        this.name_id = nameId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBbox() {
        return bbox;
    }

    public void setBbox(String bbox) {
        this.bbox = bbox;
    }

    public Double getArea_km() {
        return area_km;
    }

    public void setArea_km(Double area_km) {
        this.area_km = area_km;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDegrees() {
        return degrees;
    }

    public void setDegrees(Double degrees) {
        this.degrees = degrees;
    }

    public void setWmsurl(String wmsurl) {
        this.wmsurl = wmsurl;
    }

    public String getWmsurl() {
        return wmsurl;
    }
}
