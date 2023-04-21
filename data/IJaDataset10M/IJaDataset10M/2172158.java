package net.spc.entity;

import java.util.ArrayList;

/**
 * @author Luthen
 *	the entity of a chart
 * date 2009-03-31
 */
public class SpcChart {

    private String title;

    private String id;

    private int size;

    private double center;

    private double ucl;

    private double lcl;

    private ArrayList<ChartData> datalist;

    public double getCenter() {
        return center;
    }

    public void setCenter(double cx) {
        this.center = cx;
    }

    public double getUcl() {
        return ucl;
    }

    public void setUcl(double uclr) {
        this.ucl = uclr;
    }

    public double getLcl() {
        return lcl;
    }

    public void setLcl(double lclr) {
        this.lcl = lclr;
    }

    public ArrayList<ChartData> getDatalist() {
        return datalist;
    }

    public void setDatalist(ArrayList<ChartData> datalist) {
        this.datalist = datalist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
