package cu.ftpd.modules.zipscript.internal;

import cu.ftpd.logging.Formatter;
import cu.ftpd.logging.Logging;
import cu.ftpd.user.User;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Locale;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @since 2007-maj-22 : 19:25:38
 * @version $Id: RaceLog.java 258 2008-10-26 12:47:23Z jevring $
 */
public class RaceLog {

    protected static DateFormat time = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy ", Locale.ENGLISH);

    protected static final MessageFormat sfv = new MessageFormat("SFV: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\" \"{5}\" \"{6}\"");

    protected static final MessageFormat delfile = new MessageFormat("DELETE: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\"");

    protected static final MessageFormat leader = new MessageFormat("NEWLEADER: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\" \"{5}\" \"{6}\" \"{7}\" \"{8}\"");

    protected static final MessageFormat racefmt = new MessageFormat("RACE: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\" \"{5}\"");

    protected static final MessageFormat halfway = new MessageFormat("HALFWAY: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\" \"{5}\" \"{6}\" \"{7}\" \"{8}\" \"{9}\" \"{10}\"");

    protected static final MessageFormat update = new MessageFormat("UPDATE: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\" \"{5}\"");

    protected static final MessageFormat complete = new MessageFormat("COMPLETE: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\" \"{5}\" \"{6}\" \"{7}\" \"{8}\" \"{9}\" \"{10}\" \"{11}\" \"{12}\" \"{13}\"");

    protected static final MessageFormat ustats = new MessageFormat("USERSTATS: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\" \"{5}\" \"{6}\" \"{7}\"");

    protected static final MessageFormat gstats = new MessageFormat("GROUPSTATS: \"{0}\" \"{1}\" \"{2}\" \"{3}\" \"{4}\" \"{5}\" \"{6}\"");

    protected static final MessageFormat incomplete = new MessageFormat("INCOMPLETE: \"{0}\" \"{1}\" \"{2}\"");

    public void raceComplete(Race race) {
        LinkedList<Racer> racers = race.getRacersInWinningOrder();
        Racer winner = racers.getFirst();
        Racer looser = racers.getLast();
        Logging.getEventLog().log(complete.format(new Object[] { race.getName(), race.getSectionName(), race.getNumberOfExpectedFiles(), String.valueOf(race.getSize()), Formatter.speedFromKBps(race.getFinalRaceSpeed()), Formatter.shortDuration((race.getEndtime() - race.getStarttime()) / 1000), race.getNumberOfRacers(), race.getGroups().size(), winner.getUsername(), winner.getGroup(), Formatter.speedFromKBps(winner.getSpeed()), looser.getUsername(), looser.getGroup(), Formatter.speedFromKBps(looser.getSpeed()) }));
    }

    public void userStats(Race race) {
        int i = 0;
        for (Racer racer : race.getRacersInWinningOrder()) {
            Logging.getEventLog().log(ustats.format(new Object[] { race.getName(), race.getSectionName(), i++, racer.getUsername(), racer.getGroup(), Formatter.size(racer.getBytesRaced()), racer.getNumberOfFiles(), Formatter.speedFromKBps(racer.getSpeed()) }));
        }
    }

    public void groupStats(Race race) {
        int i = 0;
        for (RaceGroup group : race.getRaceGroupsInWinningOrder()) {
            Logging.getEventLog().log(gstats.format(new Object[] { race.getName(), race.getSectionName(), i++, group.getName(), Formatter.size(group.getBytesTransferred()), group.getNumberOfFiles(), Formatter.speedFromKBps(group.getSpeed()) }));
        }
    }

    public void fileDeleted(String path, User user) {
        Logging.getEventLog().log(delfile.format(new Object[] { path, user.getUsername(), user.getPrimaryGroup(), user.getTagline() }));
    }

    public void firstRacer(Race race, User user) {
        Logging.getEventLog().log(update.format(new Object[] { race.getName(), race.getSectionName(), user.getUsername(), user.getPrimaryGroup(), user.getTagline(), Formatter.speedFromKBps(race.getRacer(user.getUsername()).getSpeed()) }));
    }

    public void dirHalfway(Race race) {
        Racer raceLeader = race.getRacer(race.getLeader());
        Logging.getEventLog().log(halfway.format(new Object[] { race.getName(), race.getSectionName(), raceLeader.getUsername(), raceLeader.getGroup(), Formatter.size(raceLeader.getBytesRaced()), raceLeader.getNumberOfFiles(), Formatter.speedFromKBps(raceLeader.getSpeed()), race.getLeadingGroup().getName(), Formatter.size(race.getLeadingGroup().getBytesTransferred()), race.getLeadingGroup().getNumberOfFiles(), Formatter.speedFromKBps(race.getLeadingGroup().getSpeed()) }));
    }

    public void newRacer(Race race, User user) {
        Logging.getEventLog().log(racefmt.format(new Object[] { race.getName(), race.getSectionName(), user.getUsername(), user.getPrimaryGroup(), user.getTagline(), Formatter.speedFromKBps(race.getRacer(user.getUsername()).getSpeed()) }));
    }

    public void newLeader(Race race, String oldLeader, String newLeader) {
        Racer oldLeaderRacer = race.getRacer(oldLeader);
        Racer newLeaderRacer = race.getRacer(newLeader);
        Logging.getEventLog().log(leader.format(new Object[] { race.getName(), race.getSectionName(), oldLeaderRacer.getUsername(), oldLeaderRacer.getGroup(), newLeaderRacer.getUsername(), newLeaderRacer.getGroup(), Formatter.size(newLeaderRacer.getBytesRaced()), newLeaderRacer.getNumberOfFiles(), Formatter.speedFromKBps(newLeaderRacer.getSpeed()) }));
    }

    public void newSfv(String filename, Race race, User user) {
        Logging.getEventLog().log(sfv.format(new Object[] { filename, race.getName(), race.getNumberOfExpectedFiles(), race.getEstimatedSize(), race.getSectionName(), user.getUsername(), user.getPrimaryGroup() }));
    }

    public void raceIncomplete(Race race, User user) {
        Logging.getEventLog().log(incomplete.format(new Object[] { user.getUsername(), user.getPrimaryGroup(), race.getName() }));
    }
}
