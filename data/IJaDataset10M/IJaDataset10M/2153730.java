package net.sf.gham.core.entity.player.changes;

import net.sf.gham.core.entity.player.cellrenderer.SkillNameCellRenderer;
import net.sf.gham.swing.cellrenderer.CenterCellRender;
import net.sf.gham.swing.table.column.ColumnSavable;
import net.sf.gham.swing.table.column.ColumnSavableRef;
import net.sf.gham.swing.treetable.model.groupby.aggregator.TotalIntAggregator;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 * 
 */
public interface AllPlayerChangesColumns {

    ColumnSavable<PlayerChange> DATE = new ColumnSavableRef<PlayerChange>(PlayerChange.class, "Date", CenterCellRender.singleton());

    ColumnSavable<PlayerChange> DAY = new ColumnSavableRef<PlayerChange>(PlayerChange.class, Messages.getString("Day"), "DAY", "HattrickWeek", CenterCellRender.singleton());

    ColumnSavable<PlayerChange> SEASON = new ColumnSavableRef<PlayerChange>(PlayerChange.class, "Season", CenterCellRender.singleton()).setGroupable(true);

    ColumnSavable<PlayerChange> WEEK = new ColumnSavableRef<PlayerChange>(PlayerChange.class, "Week", CenterCellRender.singleton()).setGroupable(true);

    ColumnSavable<PlayerChange> NAME = new ColumnSavableRef<PlayerChange>(PlayerChange.class, Messages.getString("Name"), "NAME", "PlayerName");

    ColumnSavable<PlayerChange> PLAYERID = new ColumnSavableRef<PlayerChange>(PlayerChange.class, Messages.getString("Player"), "PLAYERID", "PlayerName").setGroupByField("PlayerId").setGroupable(true);

    ColumnSavable<PlayerChange> SKILL = new ColumnSavableRef<PlayerChange>(PlayerChange.class, "Skill", new SkillNameCellRenderer()).setGroupable(true);

    ColumnSavable<PlayerChange> BEFORE = new ColumnSavableRef<PlayerChange>(PlayerChange.class, Messages.getString("Before"), "BEFORE", "BeforeValue", CenterCellRender.singleton());

    ColumnSavable<PlayerChange> AFTER = new ColumnSavableRef<PlayerChange>(PlayerChange.class, Messages.getString("After"), "AFTER", "AfterValue", CenterCellRender.singleton());

    ColumnSavable<PlayerChange> CHANGE = new ColumnSavableRef<PlayerChange>(PlayerChange.class, Messages.getString("Change"), "CHANGE", "Change", CenterCellRender.singleton()).setAggregator(TotalIntAggregator.singleton());
}
