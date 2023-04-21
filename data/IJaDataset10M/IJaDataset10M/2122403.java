package org.inigma.utopia;

import java.util.Calendar;
import java.util.UUID;
import org.inigma.utopia.utils.CalendarUtils;

public class Survey {

    private String id;

    private Province province;

    private int barren;

    private int homes;

    private int farms;

    private int mills;

    private int banks;

    private int trainingGrounds;

    private int armories;

    private int barracks;

    private int forts;

    private int guardStations;

    private int hospitals;

    private int guilds;

    private int towers;

    private int thievesDens;

    private int watchtowers;

    private int libraries;

    private int schools;

    private int stables;

    private int dungeons;

    private Calendar lastUpdate;

    private float efficiency;

    public Survey() {
        this.id = UUID.randomUUID().toString();
        this.lastUpdate = CalendarUtils.getCalendar();
        this.lastUpdate.setTimeInMillis(0);
    }

    public Survey(Province province) {
        this();
        this.province = province;
    }

    public void copy(Survey survey) {
        this.armories = survey.armories;
        this.banks = survey.banks;
        this.barracks = survey.barracks;
        this.barren = survey.barren;
        this.dungeons = survey.dungeons;
        this.efficiency = survey.efficiency;
        this.farms = survey.farms;
        this.forts = survey.forts;
        this.guardStations = survey.guardStations;
        this.guilds = survey.guilds;
        this.homes = survey.homes;
        this.hospitals = survey.hospitals;
        this.lastUpdate = survey.lastUpdate;
        this.libraries = survey.libraries;
        this.mills = survey.mills;
        this.schools = survey.schools;
        this.stables = survey.stables;
        this.thievesDens = survey.thievesDens;
        this.towers = survey.towers;
        this.trainingGrounds = survey.trainingGrounds;
        this.watchtowers = survey.watchtowers;
    }

    public int getArmories() {
        return armories;
    }

    public int getBanks() {
        return banks;
    }

    public int getBarracks() {
        return barracks;
    }

    public int getBarren() {
        return barren;
    }

    public int getDungeons() {
        return dungeons;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public int getFarms() {
        return farms;
    }

    public int getForts() {
        return forts;
    }

    public int getGuardStations() {
        return guardStations;
    }

    public int getGuilds() {
        return guilds;
    }

    public int getHomes() {
        return homes;
    }

    public int getHospitals() {
        return hospitals;
    }

    public String getId() {
        return id;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public int getLibraries() {
        return libraries;
    }

    public int getMills() {
        return mills;
    }

    public Province getProvince() {
        return province;
    }

    public int getSchools() {
        return schools;
    }

    public int getStables() {
        return stables;
    }

    public int getThievesDens() {
        return thievesDens;
    }

    public int getTotalAcres() {
        return barren + homes + farms + mills + banks + trainingGrounds + barracks + armories + forts + guardStations + hospitals + guilds + towers + thievesDens + watchtowers + libraries + schools + stables + dungeons;
    }

    public int getTowers() {
        return towers;
    }

    public int getTrainingGrounds() {
        return trainingGrounds;
    }

    public int getWatchtowers() {
        return watchtowers;
    }

    public void setArmories(int armories) {
        this.armories = armories;
    }

    public void setBanks(int banks) {
        this.banks = banks;
    }

    public void setBarracks(int barracks) {
        this.barracks = barracks;
    }

    public void setBarren(int barren) {
        this.barren = barren;
    }

    public void setDungeons(int dungeons) {
        this.dungeons = dungeons;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public void setFarms(int farms) {
        this.farms = farms;
    }

    public void setForts(int forts) {
        this.forts = forts;
    }

    public void setGuardStations(int guardStations) {
        this.guardStations = guardStations;
    }

    public void setGuilds(int guilds) {
        this.guilds = guilds;
    }

    public void setHomes(int homes) {
        this.homes = homes;
    }

    public void setHospitals(int hospitals) {
        this.hospitals = hospitals;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = CalendarUtils.getCalendar(lastUpdate);
    }

    public void setLibraries(int libraries) {
        this.libraries = libraries;
    }

    public void setMills(int mills) {
        this.mills = mills;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public void setSchools(int schools) {
        this.schools = schools;
    }

    public void setStables(int stables) {
        this.stables = stables;
    }

    public void setThievesDens(int thievesDens) {
        this.thievesDens = thievesDens;
    }

    public void setTowers(int towers) {
        this.towers = towers;
    }

    public void setTrainingGrounds(int trainingGrounds) {
        this.trainingGrounds = trainingGrounds;
    }

    public void setWatchtowers(int watchtowers) {
        this.watchtowers = watchtowers;
    }
}
