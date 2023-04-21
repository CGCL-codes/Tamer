package com.catarak.uwhscoretime.model;

/**
 *
 * @author bouchard
 */
public class Penalty {

    public Penalty(Team.Player player, PenaltyType duration) {
        setPlayer(player);
        setDuration(duration);
    }

    public Penalty(Time gameTime) {
        setGameTime(gameTime);
    }

    public PenaltyType getDuration() {
        return duration;
    }

    public void setDuration(PenaltyType duration) {
        this.duration = duration;
    }

    public Team.Player getPlayer() {
        return player;
    }

    public void setPlayer(Team.Player player) {
        this.player = player;
    }

    public Time getGameTime() {
        return gameTime;
    }

    public void setGameTime(Time gameTime) {
        this.gameTime = gameTime;
    }

    private Team.Player player;

    private PenaltyType duration;

    private Time gameTime;

    private transient TimeSlice timeSlice;

    TimeSlice getTimeSlice() {
        if (timeSlice == null) {
            timeSlice = new TimeSlice(duration.getPenaltyDuration());
        }
        return timeSlice;
    }

    public Time getRemainingTime() {
        return getTimeSlice().getTime();
    }

    public void setCountDownRunning(boolean run) {
        getTimeSlice().setRunning(run);
    }

    public boolean isCountDownRunning() {
        return getTimeSlice().isRunning();
    }

    public boolean isFinished() {
        return getTimeSlice().isOver();
    }
}
