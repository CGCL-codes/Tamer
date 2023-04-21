package org.robinfinch.clasj.competition;

import org.robinfinch.clasj.universals.HourGlass;
import org.robinfinch.clasj.universals.TimeLineException;

/**
 * A match between two {@link org.robinfinch.clasj.competition.Competitor competitors}.
 * @author Mark Hoogenboom
 */
public abstract class Match<T extends Competitor> {

    protected final HourGlass hourGlass;

    protected final Competition competition;

    private final T home;

    protected int homeScore;

    private final T away;

    protected int awayScore;

    protected int time;

    protected Match(Competition competition, T home, T away, HourGlass hourGlass) {
        this.hourGlass = hourGlass;
        this.competition = competition;
        this.home = home;
        this.away = away;
    }

    public abstract void play() throws TimeLineException;

    public T getHome() {
        return home;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public T getAway() {
        return away;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public int getTime() {
        return time;
    }
}
