package net.sf.gham.plugins.panels.player.detail.changes;

import java.util.Collection;
import java.util.List;
import net.sf.gham.core.entity.player.changes.AllPlayerChangesColumns;
import net.sf.gham.core.entity.player.changes.PlayerChange;
import net.sf.gham.core.gui.selectpanel.SingleDateFilter;
import net.sf.gham.core.gui.selectpanel.SkillFilter;
import net.sf.gham.core.util.constants.SkillConstants;
import net.sf.gham.swing.table.column.ColumnSavable;
import net.sf.gham.swing.table.column.ColumnsComboBoxModel;
import net.sf.gham.swing.table.column.ColumnsList;
import net.sf.gham.swing.treetable.model.GhamTreeTableModel;
import net.sf.gham.swing.treetable.model.groupby.Structure;
import net.sf.gham.swing.treetable.model.groupby.StructureFactory;

/**
 *
 * @author Fabio Collini
 */
public class PlayerChangesTableModel extends GhamTreeTableModel<PlayerChange> implements AllPlayerChangesColumns {

    private SkillFilter skillFilter;

    /** Creates a new instance of PlayerChangesTableModel */
    public PlayerChangesTableModel() {
        super("playerChangesTable", createMyColumnModel());
        skillFilter = new SkillFilter();
        addFilterInList(skillFilter);
        addFilterInList(new SingleDateFilter<PlayerChange>(true));
        addFilterInList(new SingleDateFilter<PlayerChange>(false));
    }

    private static ColumnsComboBoxModel<PlayerChange> createMyColumnModel() {
        return new ColumnsComboBoxModel<PlayerChange>(AllPlayerChangesColumns.class, 5) {

            @Override
            public void createDefaultModel() {
                ColumnsList<PlayerChange> l = new ColumnsList<PlayerChange>(null, 0, false);
                l.add(DATE.clone(120));
                l.add(DAY.clone(80));
                l.add(SKILL.clone(120));
                l.add(BEFORE.clone(50));
                l.add(AFTER.clone(50));
                l.add(CHANGE.clone(70));
                addElement(l);
            }

            @Override
            protected ColumnSavable<PlayerChange> getFirstColumn() {
                return DATE;
            }
        };
    }

    protected StructureFactory<PlayerChange> createStructureFactory() {
        return new StructureFactory<PlayerChange>() {

            @Override
            public List<Structure<PlayerChange>> createDefaultStructures() {
                List<Structure<PlayerChange>> l = super.createDefaultStructures();
                l.add(createStructure("Season", SEASON, WEEK));
                l.add(createStructure("Skill", SKILL));
                return l;
            }
        };
    }

    public void enableSkillFilter(Collection<SkillConstants.Skills> enabledSkills) {
        skillFilter.enableFilters(enabledSkills);
    }
}
