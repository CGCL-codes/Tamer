package com.bhms.module.resources.village.mapper;

import java.math.BigDecimal;
import java.util.Date;

public class Estates {

    private Integer housingprojectid;

    private String projectcode;

    private String projectname;

    private String projectalias;

    private String projectpinyin;

    private String projectjianpin;

    private String address;

    private String firstletter;

    private Integer hpexpertagentid;

    private String fullviewpath;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Integer city_id;

    private Integer districtid;

    private Integer businesszoneid;

    private Integer circlelineid;

    private Integer circledirectionid;

    private String propcompanyname;

    private BigDecimal propertyfee;

    private BigDecimal maxpropertyfee;

    private Short heatingtype;

    private BigDecimal heatingfee;

    private Integer plotnum;

    private Integer plotprice;

    private Integer ugplotnum;

    private Integer ugplotprice;

    private String devcompanyname;

    private Short completionyear;

    private Integer totalarea;

    private BigDecimal floorarearatio;

    private BigDecimal greenarearatio;

    private Short status;

    private Integer createaccid;

    private Date createtime;

    private Integer updateaccid;

    private Date updatetime;

    private Integer approvalaccid;

    private Date approvaltime;

    private Short projectstatus;

    private BigDecimal unitprice;

    private Double priceamplitude;

    private String bus_code;

    private Integer subway_id;

    private String subway_name;

    private Integer station_id;

    private String station_name;

    public Integer getHousingprojectid() {
        return housingprojectid;
    }

    public void setHousingprojectid(Integer housingprojectid) {
        this.housingprojectid = housingprojectid;
    }

    public String getProjectcode() {
        return projectcode;
    }

    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode == null ? null : projectcode.trim();
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname == null ? null : projectname.trim();
    }

    public String getProjectalias() {
        return projectalias;
    }

    public void setProjectalias(String projectalias) {
        this.projectalias = projectalias == null ? null : projectalias.trim();
    }

    public String getProjectpinyin() {
        return projectpinyin;
    }

    public void setProjectpinyin(String projectpinyin) {
        this.projectpinyin = projectpinyin == null ? null : projectpinyin.trim();
    }

    public String getProjectjianpin() {
        return projectjianpin;
    }

    public void setProjectjianpin(String projectjianpin) {
        this.projectjianpin = projectjianpin == null ? null : projectjianpin.trim();
    }

    public String getFirstletter() {
        return firstletter;
    }

    public void setFirstletter(String firstletter) {
        this.firstletter = firstletter == null ? null : firstletter.trim();
    }

    public Integer getHpexpertagentid() {
        return hpexpertagentid;
    }

    public void setHpexpertagentid(Integer hpexpertagentid) {
        this.hpexpertagentid = hpexpertagentid;
    }

    public String getFullviewpath() {
        return fullviewpath;
    }

    public void setFullviewpath(String fullviewpath) {
        this.fullviewpath = fullviewpath == null ? null : fullviewpath.trim();
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public Integer getDistrictid() {
        return districtid;
    }

    public void setDistrictid(Integer districtid) {
        this.districtid = districtid;
    }

    public Integer getBusinesszoneid() {
        return businesszoneid;
    }

    public void setBusinesszoneid(Integer businesszoneid) {
        this.businesszoneid = businesszoneid;
    }

    public Integer getCirclelineid() {
        return circlelineid;
    }

    public void setCirclelineid(Integer circlelineid) {
        this.circlelineid = circlelineid;
    }

    public Integer getCircledirectionid() {
        return circledirectionid;
    }

    public void setCircledirectionid(Integer circledirectionid) {
        this.circledirectionid = circledirectionid;
    }

    public String getPropcompanyname() {
        return propcompanyname;
    }

    public void setPropcompanyname(String propcompanyname) {
        this.propcompanyname = propcompanyname == null ? null : propcompanyname.trim();
    }

    public BigDecimal getPropertyfee() {
        return propertyfee;
    }

    public void setPropertyfee(BigDecimal propertyfee) {
        this.propertyfee = propertyfee;
    }

    public BigDecimal getMaxpropertyfee() {
        return maxpropertyfee;
    }

    public void setMaxpropertyfee(BigDecimal maxpropertyfee) {
        this.maxpropertyfee = maxpropertyfee;
    }

    public Short getHeatingtype() {
        return heatingtype;
    }

    public void setHeatingtype(Short heatingtype) {
        this.heatingtype = heatingtype;
    }

    public BigDecimal getHeatingfee() {
        return heatingfee;
    }

    public void setHeatingfee(BigDecimal heatingfee) {
        this.heatingfee = heatingfee;
    }

    public Integer getPlotnum() {
        return plotnum;
    }

    public void setPlotnum(Integer plotnum) {
        this.plotnum = plotnum;
    }

    public Integer getPlotprice() {
        return plotprice;
    }

    public void setPlotprice(Integer plotprice) {
        this.plotprice = plotprice;
    }

    public Integer getUgplotnum() {
        return ugplotnum;
    }

    public void setUgplotnum(Integer ugplotnum) {
        this.ugplotnum = ugplotnum;
    }

    public Integer getUgplotprice() {
        return ugplotprice;
    }

    public void setUgplotprice(Integer ugplotprice) {
        this.ugplotprice = ugplotprice;
    }

    public String getDevcompanyname() {
        return devcompanyname;
    }

    public void setDevcompanyname(String devcompanyname) {
        this.devcompanyname = devcompanyname == null ? null : devcompanyname.trim();
    }

    public Short getCompletionyear() {
        return completionyear;
    }

    public void setCompletionyear(Short completionyear) {
        this.completionyear = completionyear;
    }

    public Integer getTotalarea() {
        return totalarea;
    }

    public void setTotalarea(Integer totalarea) {
        this.totalarea = totalarea;
    }

    public BigDecimal getFloorarearatio() {
        return floorarearatio;
    }

    public void setFloorarearatio(BigDecimal floorarearatio) {
        this.floorarearatio = floorarearatio;
    }

    public BigDecimal getGreenarearatio() {
        return greenarearatio;
    }

    public void setGreenarearatio(BigDecimal greenarearatio) {
        this.greenarearatio = greenarearatio;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getCreateaccid() {
        return createaccid;
    }

    public void setCreateaccid(Integer createaccid) {
        this.createaccid = createaccid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getUpdateaccid() {
        return updateaccid;
    }

    public void setUpdateaccid(Integer updateaccid) {
        this.updateaccid = updateaccid;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getApprovalaccid() {
        return approvalaccid;
    }

    public void setApprovalaccid(Integer approvalaccid) {
        this.approvalaccid = approvalaccid;
    }

    public Date getApprovaltime() {
        return approvaltime;
    }

    public void setApprovaltime(Date approvaltime) {
        this.approvaltime = approvaltime;
    }

    public Short getProjectstatus() {
        return projectstatus;
    }

    public void setProjectstatus(Short projectstatus) {
        this.projectstatus = projectstatus;
    }

    public BigDecimal getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(BigDecimal unitprice) {
        this.unitprice = unitprice;
    }

    public Double getPriceamplitude() {
        return priceamplitude;
    }

    public void setPriceamplitude(Double priceamplitude) {
        this.priceamplitude = priceamplitude;
    }

    public String getBus_code() {
        return bus_code;
    }

    public void setBus_code(String bus_code) {
        this.bus_code = bus_code == null ? null : bus_code.trim();
    }

    public Integer getSubway_id() {
        return subway_id;
    }

    public void setSubway_id(Integer subway_id) {
        this.subway_id = subway_id;
    }

    public String getSubway_name() {
        return subway_name;
    }

    public void setSubway_name(String subway_name) {
        this.subway_name = subway_name == null ? null : subway_name.trim();
    }

    public Integer getStation_id() {
        return station_id;
    }

    public void setStation_id(Integer station_id) {
        this.station_id = station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name == null ? null : station_name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}
