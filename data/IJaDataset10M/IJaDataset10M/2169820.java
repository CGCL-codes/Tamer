package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author quincey
 */
public class Timeslot {

    private Date start;

    private int duration;

    private int slots;

    private List<Team> teams;

    private static int MAX_SLOTS = 4;

    public Timeslot() {
        this.slots = MAX_SLOTS;
        this.teams = new ArrayList<Team>();
    }

    public Timeslot(Date d, int du) {
        setDate(d);
        setDuration(du);
        this.slots = MAX_SLOTS;
        this.teams = new ArrayList<Team>();
    }

    public Date getDate() {
        return this.start;
    }

    public final void setDate(Date d) {
        Date ref = new Date();
        if (ref.before(d)) {
            this.start = d;
        } else {
            return;
        }
    }

    public int getDuration() {
        return this.duration;
    }

    public final void setDuration(int d) {
        this.duration = d;
    }

    public int getSlots() {
        return this.slots;
    }

    public void setSlots(int s) {
        this.slots = s;
    }

    public ListIterator<Team> getTeams() {
        return this.teams.listIterator();
    }

    public void addTeam(Team t) {
        int freeSlots = getSlots();
        int neededSlots = t.getSlots();
        if (freeSlots - neededSlots >= 0) {
            this.teams.add(t);
        } else {
            return;
        }
    }

    public void removeTeam(Team t) {
        this.teams.remove(t);
    }
}
