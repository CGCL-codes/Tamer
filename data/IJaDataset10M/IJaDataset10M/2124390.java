package edu.ipfw.nitrogo.session;

import java.math.BigDecimal;
import java.util.Random;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("surveyAction")
@Scope(ScopeType.SESSION)
@AutoCreate
public class SurveyAction {

    @Logger
    private Log log;

    private int userCount;

    private int userResponded;

    private float lightTruck;

    private float compactPerformance;

    private float offRoad;

    private float streetPerformance;

    private float racing;

    private float restoration;

    private float restyling;

    private float streetRod;

    Random r;

    public SurveyAction() {
        super();
        r = new Random();
        userCount = 120000;
        userResponded = r.nextInt(120000);
        this.loadData();
    }

    public static void main(String[] args) {
    }

    public void loadData() {
        userResponded = userResponded + (r.nextInt(50));
        this.setCompactPerformance(100 * r.nextFloat());
        this.setLightTruck(100 * r.nextFloat());
        this.setOffRoad(100 * r.nextFloat());
        this.setRacing(100 * r.nextFloat());
        this.setRestoration(100 * r.nextFloat());
        this.setRestyling(100 * r.nextFloat());
        this.setStreetPerformance(100 * r.nextFloat());
        this.setStreetRod(100 * r.nextFloat());
    }

    public float getLightTruck() {
        return lightTruck;
    }

    public void setLightTruck(float lightTruck) {
        this.lightTruck = lightTruck;
    }

    public float getCompactPerformance() {
        return compactPerformance;
    }

    public void setCompactPerformance(float compactPerformance) {
        this.compactPerformance = compactPerformance;
    }

    public float getOffRoad() {
        return offRoad;
    }

    public void setOffRoad(float offRoad) {
        this.offRoad = offRoad;
    }

    public float getStreetPerformance() {
        return streetPerformance;
    }

    public void setStreetPerformance(float streetPerformance) {
        this.streetPerformance = streetPerformance;
    }

    public float getRacing() {
        return racing;
    }

    public void setRacing(float racing) {
        this.racing = racing;
    }

    public float getRestoration() {
        return restoration;
    }

    public void setRestoration(float restoration) {
        this.restoration = restoration;
    }

    public float getRestyling() {
        return restyling;
    }

    public void setRestyling(float restyling) {
        this.restyling = restyling;
    }

    public float getStreetRod() {
        return streetRod;
    }

    public void setStreetRod(float streetRod) {
        this.streetRod = streetRod;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getUserResponded() {
        return userResponded;
    }

    public void setUserResponded(int userResponded) {
        this.userResponded = userResponded;
    }
}
