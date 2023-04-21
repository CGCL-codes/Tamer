package net.sf.gham.plugins.panels.youth.list;

import net.sf.gham.core.entity.player.YouthPlayerMyTeam;
import net.sf.gham.core.entity.player.rolecalcolator.Roles.Position;
import net.sf.gham.swing.cellrenderer.Number2NotNegativeCellRenderer;
import net.sf.gham.swing.table.column.ColumnSavableRef;
import net.sf.gham.swing.treetable.model.groupby.aggregator.AverageAggregator;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 *
 */
public class AverageRoleColumn extends ColumnSavableRef<YouthPlayerMyTeam> {

    public AverageRoleColumn(Position position) {
        super(YouthPlayerMyTeam.class, Messages.getString("Average_short") + " " + Messages.getString(position.getKeyLabel() + "_short"), ("Average" + position.getKeyLabel().substring(0, position.getKeyLabel().length() - 5).replace("_", "")).toUpperCase(), "Average" + position.getKeyLabel().substring(0, position.getKeyLabel().length() - 5).replace("_", ""), Number2NotNegativeCellRenderer.singleton());
        setGroupable(true);
        setAggregator(AverageAggregator.singleton());
        setColumnCategory(AllYouthPlayerMyTeamColumn.CAT_ROLE_STARS);
        setTooltip(Messages.getString("Average") + " - " + position.toString());
    }
}
