package net.sf.gham.core.entity.match.matchtablecolumn;

import net.sf.gham.core.entity.match.EventSide;
import net.sf.gham.swing.cellrenderer.CenterCellRender;
import net.sf.gham.swing.treetable.model.groupby.aggregator.TotalIntAggregator;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 *
 */
public class TeamGoalOrAttemptsTableColumn extends MatchTeamTableColumn {

    private static final String goalsCategory = Messages.getString("Goals");

    private static final String attemptsCategory = Messages.getString("Attempts");

    private static final String teamShort = "";

    private static final String team = "";

    public TeamGoalOrAttemptsTableColumn(String key, EventSide side, boolean isTeam, boolean isGoal) {
        super(getPrefiShort(isTeam, isGoal) + side.getShortName(), key, isGoal ? "Goals" : "Attempts", CenterCellRender.singleton(), new Class[] { EventSide.class }, new Object[] { side }, isTeam);
        setName(getPrefiShort(isTeam, isGoal) + side.getShortName());
        setTooltip(getPrefix(isTeam, isGoal) + side.getName());
        if (isGoal) setColumnCategory(goalsCategory); else setColumnCategory(attemptsCategory);
        setAggregator(TotalIntAggregator.singleton());
    }

    private String getPrefix(boolean isTeam, boolean isGoal) {
        StringBuffer b = new StringBuffer(5);
        if (isTeam) {
            b.append(team);
        } else {
            b.append(opponent);
        }
        if (b.length() > 0) b.append(" - ");
        if (isGoal) {
            b.append(Messages.getString("Goals"));
        } else {
            b.append(Messages.getString("Attempts"));
        }
        b.append(" - ");
        return b.toString();
    }

    private static String getPrefiShort(boolean isTeam, boolean isGoal) {
        StringBuffer b = new StringBuffer(5);
        if (isTeam) {
            b.append(teamShort);
        } else {
            b.append(opponentShort);
        }
        if (isGoal) {
            b.append(Messages.getString("Goals_short"));
        } else {
            b.append(Messages.getString("Attempts_short"));
        }
        return b.toString();
    }
}
