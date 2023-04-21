package net.sf.gham.plugins.panels.player.list.player;

import javax.swing.table.DefaultTableCellRenderer;
import net.sf.gham.core.entity.match.EventSide;
import net.sf.gham.core.entity.player.PlayerMyTeam;
import net.sf.gham.core.entity.player.YouthPlayerMyTeam;
import net.sf.gham.plugins.panels.youth.list.AllYouthPlayerMyTeamColumn;
import net.sf.gham.swing.cellrenderer.CenterCellRender;
import net.sf.gham.swing.cellrenderer.Number2CellRenderer;
import net.sf.gham.swing.table.column.ColumnSavableRef;
import net.sf.gham.swing.treetable.model.groupby.aggregator.Aggregator;
import net.sf.gham.swing.treetable.model.groupby.aggregator.AverageAggregator;
import net.sf.gham.swing.treetable.model.groupby.aggregator.TotalIntAggregator;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 *
 */
public class GoalsColumn extends ColumnSavableRef<YouthPlayerMyTeam> {

    private static final String AVERAGE = Messages.getString("Average");

    private static final String AVERAGE_SHORT = Messages.getString("Average_short");

    private static final String ATTEMPTS = Messages.getString("Attempts");

    private static final String ATTEMPTS_SHORT = Messages.getString("Attempts_short");

    private static final String GOALS = Messages.getString("Goals");

    private static final String GOALS_SHORT = Messages.getString("Goals_short");

    public GoalsColumn(String name, String key, String fieldName) {
        this(name, key, fieldName, name, false);
    }

    public GoalsColumn(String name, String key, String fieldName, String tooltip) {
        this(name, key, fieldName, tooltip, false);
    }

    public GoalsColumn(String name, String key, String fieldName, String tooltip, boolean avg) {
        super(PlayerMyTeam.class, Messages.getString(name) + (avg ? AVERAGE_SHORT : ""), key, fieldName, getRenderer(avg));
        setAggregator(getAggregator(avg));
        setColumnCategory(AllYouthPlayerMyTeamColumn.CAT_GOALS);
        setTooltip(Messages.getString(tooltip) + (avg ? " - " + AVERAGE : ""));
    }

    public GoalsColumn(String name, String fieldName) {
        this(name, fieldName.toUpperCase(), fieldName, name);
    }

    public GoalsColumn(String fieldName, EventSide side, boolean goal) {
        this(fieldName, side.getShortName(), side.getName(), goal);
    }

    public GoalsColumn(String fieldName, EventSide side, boolean goal, boolean avg) {
        this(fieldName, side.getShortName(), side.getName(), goal, avg);
    }

    public GoalsColumn(String fieldName, String n, String t, boolean goal) {
        this(fieldName, n, t, goal, false);
    }

    public GoalsColumn(String fieldName, String n, String t, boolean goal, boolean avg) {
        super(PlayerMyTeam.class, "", fieldName.toUpperCase() + (avg ? "AVG" : ""), fieldName + (avg ? "Avg" : ""), getRenderer(avg));
        String name;
        String tooltip;
        if (goal) {
            name = GOALS_SHORT;
            tooltip = GOALS;
        } else {
            name = ATTEMPTS_SHORT;
            tooltip = ATTEMPTS;
        }
        name += n;
        tooltip += " - " + t;
        if (avg) {
            name += AVERAGE_SHORT;
            tooltip += " - " + AVERAGE;
        }
        setName(name);
        setTooltip(tooltip);
        setAggregator(getAggregator(avg));
        setColumnCategory(AllYouthPlayerMyTeamColumn.CAT_GOALS);
    }

    private Aggregator getAggregator(boolean avg) {
        return avg ? AverageAggregator.singleton() : TotalIntAggregator.singleton();
    }

    private static DefaultTableCellRenderer getRenderer(boolean avg) {
        return avg ? Number2CellRenderer.singleton() : CenterCellRender.singleton();
    }
}
