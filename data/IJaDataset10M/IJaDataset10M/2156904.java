package org.robinfinch.clasj.competition;

import java.util.Arrays;
import java.util.Collection;
import org.robinfinch.clasj.universals.HourGlass;
import org.robinfinch.clasj.universals.TimeLineException;

/**
 * An event in which {@link org.robinfinch.clasj.competition.Competitor competitors} compete.
 * @author Mark Hoogenboom
 */
public class Competition implements HourGlass.Listener {

    private final HourGlass hourGlass;

    private final Championship championship;

    private final Competitor[] competitors;

    private Phase phase;

    private int round;

    private final Match[][] matches;

    private final Result[] results;

    Competition(Championship championship, HourGlass hourGlass) {
        this.hourGlass = hourGlass;
        this.championship = championship;
        this.competitors = new Competitor[championship.getNumberOfEntryRules()];
        this.matches = new Match[competitors.length][competitors.length];
        this.results = new Result[competitors.length];
    }

    void plan() throws TimeLineException {
        phase = Phase.PLANNED;
        hourGlass.addListener(hourGlass.getTime() + championship.getStart(), this);
    }

    public int getCurrentRound() {
        return round;
    }

    public Result[] getCurrentResults() {
        return results;
    }

    public boolean isFinished() {
        return phase == Phase.FINISHED;
    }

    public void onTime() throws TimeLineException {
        if (phase == Phase.PLANNED) {
            enterCompetitors();
        }
        if (phase == Phase.STARTED) {
            playRound();
        }
        if (phase == Phase.FINISHED) {
        }
    }

    private void enterCompetitors() throws TimeLineException {
        for (int i = 0; i < competitors.length; i++) {
            EntryRule rule = championship.getEntryRule(i);
            if (rule != null) {
                competitors[i] = rule.getCompetitor(Arrays.asList(competitors));
            }
        }
        phase = Phase.STARTED;
        round = 1;
        for (int i = 0; i < competitors.length; i++) {
            results[i] = new Result();
        }
    }

    private void playRound() throws TimeLineException {
        CompetitionSchedule schedule = championship.getCompetitionSchedule();
        for (int match = 1; match <= schedule.getNumberOfMatches(round); match++) {
            playMatch(schedule.getHomeCompetitor(round, match) - 1, schedule.getAwayCompetitor(round, match) - 1);
        }
        computeResults();
        if (round < schedule.getNumberOfRounds()) {
            round++;
            hourGlass.addListener(hourGlass.getTime() + championship.getFrequency(), this);
        } else {
            phase = Phase.FINISHED;
        }
    }

    private void playMatch(int home, int away) throws TimeLineException {
        if ((competitors[home] != null) && (competitors[away] != null)) {
            matches[home][away] = competitors[home].challenge(this, competitors[away]);
            matches[home][away].play();
        }
    }

    private void computeResults() {
        for (int i = 0; i < competitors.length; i++) {
            results[i].setCompetitor(competitors[i]);
            results[i].reset();
            for (int j = 0; j < competitors.length; j++) {
                if (matches[i][j] != null) {
                    results[i].addHome(matches[i][j]);
                }
                if (matches[j][i] != null) {
                    results[i].addAway(matches[j][i]);
                }
            }
        }
        for (int i = 0; i < competitors.length; i++) {
            for (int j = 0; j < competitors.length; j++) {
                if (matches[i][j] != null) {
                    results[i].addHome(matches[i][j], results[j]);
                }
                if (matches[j][i] != null) {
                    results[i].addAway(matches[j][i], results[j]);
                }
            }
        }
        Arrays.sort(results);
    }

    Competitor getChampion(int rank, Collection<Competitor> excluded) throws TimeLineException {
        if (!isFinished()) {
            throw new TimeLineException("No champion is known for " + championship.getName() + " because the competition has not finished yet.");
        }
        Competitor champion = null;
        for (int i = rank - 1; (champion == null) && (i < results.length); i++) {
            Competitor competitor = results[i].getCompetitor();
            if (!excluded.contains(competitor)) {
                champion = competitor;
            }
        }
        return champion;
    }

    private static enum Phase {

        PLANNED, STARTED, FINISHED
    }
}
