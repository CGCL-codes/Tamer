package net.sf.gham.core.entity.player.aggregator;

import java.util.List;
import net.sf.gham.core.entity.player.skill.PlayerSkill;
import net.sf.gham.core.util.FixedSkill;
import net.sf.gham.swing.table.column.ColumnSavable;
import net.sf.gham.swing.treetable.model.groupby.InternalNode;
import net.sf.gham.swing.treetable.model.groupby.aggregator.Aggregator;

/**
 * @author fabio
 *
 */
public class SkillTotalAggregator implements Aggregator {

    private static SkillTotalAggregator singleton;

    public static SkillTotalAggregator singleton() {
        if (singleton == null) {
            singleton = new SkillTotalAggregator();
        }
        return singleton;
    }

    private SkillTotalAggregator() {
    }

    public <T> Object getValue(InternalNode<T> node, ColumnSavable<T> column, Object param) {
        List<T> leafs = node.getAllLeafs();
        if (!leafs.isEmpty()) {
            double tot = 0;
            for (T obj : leafs) {
                tot += ((PlayerSkill) column.getValueAt(obj, param)).getValue();
            }
            return new FixedSkill(tot);
        } else {
            return null;
        }
    }
}
