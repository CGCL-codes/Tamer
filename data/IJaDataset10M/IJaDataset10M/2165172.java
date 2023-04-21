package net.dawnmist.sims2tracker.views;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.dawnmist.sims2tracker.Sims2Tracker;
import net.dawnmist.sims2tracker.data.Skill;
import net.dawnmist.sims2tracker.data.SkillType;
import net.dawnmist.sims2tracker.table.editors.ComboBoxCellEditor;
import net.dawnmist.sims2tracker.table.renderers.MultilineStringCellRenderer;

/**
 *
 * @author Janeene Beeforth <sourceforge@dawnmist.net>
 */
public class SkillTablePanelView extends GenericDataObjectTablePanelView<Skill, SkillType> {

    private ComboBoxCellEditor<SkillType> mSkillTypeEditor;

    public SkillTablePanelView() {
        super(Sims2Tracker.getDataModel().getSkillModel());
        mSkillTypeEditor = new ComboBoxCellEditor<SkillType>(SkillType.getValidTypes());
        JTable table = getTable();
        table.setDefaultEditor(SkillType.class, mSkillTypeEditor);
        table.setDefaultRenderer(SkillType.class, new MultilineStringCellRenderer(5));
        Integer[] levels = new Integer[] { -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        ComboBoxCellEditor<Integer> levelEditor = new ComboBoxCellEditor<Integer>(levels);
        table.setDefaultEditor(Integer.class, levelEditor);
    }

    @Override
    public Skill createNewItem() {
        Skill skill = null;
        NewSkillView skillView = new NewSkillView();
        int result = JOptionPane.showConfirmDialog(this, skillView, "New Skill", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            skill = skillView.getSkill();
        }
        return skill;
    }

    @Override
    protected String getToolbarName() {
        return "Skill Edit Options:";
    }

    @Override
    protected String getAddButtonName() {
        return "Add Skill";
    }

    @Override
    protected String getDeleteButtonName() {
        return "Delete Skill";
    }
}
