package bzstats.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import bzstats.BzStatsException;
import bzstats.PeriodStats;

/**
 * @author youth
 */
public class JoinEvent extends GameEvent {

    private static final Pattern PATTERN = Pattern.compile("^\\*\\*\\* '(.*)' joined the game as (.*)\\.$");

    private String player;

    private String team;

    /**
	 * @return Returns the player.
	 */
    public String getPlayer() {
        return player;
    }

    /**
	 * @return Returns the team.
	 */
    public String getTeam() {
        return team;
    }

    public boolean parseEvent(String logline) throws BzStatsException {
        final Matcher match = PATTERN.matcher(logline);
        final boolean found = match.matches();
        if (found) {
            player = match.group(1);
            team = match.group(2);
        }
        return found;
    }

    /**
	 * @see bzstats.event.GameEvent#collectStats(bzstats.PeriodStats)
	 */
    public void collectStats(PeriodStats stats) {
        stats.getPlayerStats(player).setLatestTeam(team);
    }
}
