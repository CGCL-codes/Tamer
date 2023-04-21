package org.inigma.utopia;

import java.util.Calendar;
import java.util.UUID;
import org.inigma.utopia.utils.CalendarUtils;

public class Army {

    private String id;

    private Military military;

    private int generals;

    private int soldiers;

    private int offspecs;

    private int defspecs;

    private int elites;

    private int horses;

    private int spoils;

    private Calendar returnTime;

    public Army() {
        this.id = UUID.randomUUID().toString();
        this.returnTime = CalendarUtils.getCalendar();
    }

    public Army(Military military) {
        this();
        this.military = military;
    }

    public int getDefspecs() {
        return defspecs;
    }

    public int getElites() {
        return elites;
    }

    public int getGenerals() {
        return generals;
    }

    public int getHorses() {
        return horses;
    }

    public String getId() {
        return id;
    }

    public Military getMilitary() {
        return military;
    }

    public int getOffspecs() {
        return offspecs;
    }

    public Calendar getReturnTime() {
        return returnTime;
    }

    public int getSoldiers() {
        return soldiers;
    }

    public int getSpoils() {
        return spoils;
    }

    public void setDefspecs(int defspecs) {
        this.defspecs = defspecs;
    }

    public void setElites(int elites) {
        this.elites = elites;
    }

    public void setGenerals(int generals) {
        this.generals = generals;
    }

    public void setHorses(int horses) {
        this.horses = horses;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMilitary(Military military) {
        this.military = military;
    }

    public void setOffspecs(int offspecs) {
        this.offspecs = offspecs;
    }

    public void setReturnTime(Calendar returnTime) {
        this.returnTime = returnTime;
    }

    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
    }

    public void setSpoils(int spoils) {
        this.spoils = spoils;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Army ");
        sb.append("soldiers=").append(soldiers);
        sb.append(", offspecs=").append(offspecs);
        sb.append(", defspecs=").append(defspecs);
        sb.append(", elites=").append(elites);
        sb.append(", horses=").append(horses);
        sb.append(", spoils=").append(spoils);
        sb.append(", eta=").append(returnTime.getTime());
        sb.append("]");
        return sb.toString();
    }
}
