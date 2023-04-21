package com.store;

import java.sql.Date;
import com.jedi.BaseObj;

/**
 * @author wevjoso
 *
 */
public class Tasktimev extends BaseObj {

    private String engineerid = "";

    private String engname = "";

    private String prjname = "";

    private String customer = "";

    private String salename = "";

    private String matterinfo = "";

    private int comid = 0;

    private String productdir = "";

    private Date abegindate = null;

    private Date aenddate = null;

    private int normaltime = 0;

    private int worktime = 0;

    private int acttime = 0;

    private int totdays = 0;

    private double tripfee = 0.00;

    private String taskaddress = "";

    private String triptype = "";

    private double totlemoney = 0.00;

    private int totaldays = 0;

    public int getTotaldays() {
        return totaldays;
    }

    public void setTotaldays(int totaldays) {
        this.totaldays = totaldays;
    }

    public double getTotlemoney() {
        return totlemoney;
    }

    public void setTotlemoney(double totlemoney) {
        this.totlemoney = totlemoney;
    }

    public Date getAbegindate() {
        return abegindate;
    }

    public void setAbegindate(Date abegindate) {
        this.abegindate = abegindate;
    }

    public int getActtime() {
        return acttime;
    }

    public void setActtime(int acttime) {
        this.acttime = acttime;
    }

    public Date getAenddate() {
        return aenddate;
    }

    public void setAenddate(Date aenddate) {
        this.aenddate = aenddate;
    }

    public int getComid() {
        return comid;
    }

    public void setComid(int comid) {
        this.comid = comid;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEngineerid() {
        return engineerid;
    }

    public void setEngineerid(String engineerid) {
        this.engineerid = engineerid;
    }

    public String getEngname() {
        return engname;
    }

    public void setEngname(String engname) {
        this.engname = engname;
    }

    public String getMatterinfo() {
        return matterinfo;
    }

    public void setMatterinfo(String matterinfo) {
        this.matterinfo = matterinfo;
    }

    public int getNormaltime() {
        return normaltime;
    }

    public void setNormaltime(int normaltime) {
        this.normaltime = normaltime;
    }

    public String getPrjname() {
        return prjname;
    }

    public void setPrjname(String prjname) {
        this.prjname = prjname;
    }

    public String getProductdir() {
        return productdir;
    }

    public void setProductdir(String productdir) {
        this.productdir = productdir;
    }

    public String getSalename() {
        return salename;
    }

    public void setSalename(String salename) {
        this.salename = salename;
    }

    public String getTaskaddress() {
        return taskaddress;
    }

    public void setTaskaddress(String taskaddress) {
        this.taskaddress = taskaddress;
    }

    public int getTotdays() {
        return totdays;
    }

    public void setTotdays(int totdays) {
        this.totdays = totdays;
    }

    public double getTripfee() {
        return tripfee;
    }

    public void setTripfee(double tripfee) {
        this.tripfee = tripfee;
    }

    public String getTriptype() {
        return triptype;
    }

    public void setTriptype(String triptype) {
        this.triptype = triptype;
    }

    public int getWorktime() {
        return worktime;
    }

    public void setWorktime(int worktime) {
        this.worktime = worktime;
    }
}
