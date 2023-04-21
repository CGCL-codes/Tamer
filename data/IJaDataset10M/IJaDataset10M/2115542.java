package pcgen.gui2.tabs.skill;

import java.util.Arrays;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import pcgen.cdom.enumeration.SkillCost;
import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.CharacterLevelFacade;
import pcgen.core.facade.CharacterLevelsFacade;
import pcgen.core.facade.SkillFacade;
import pcgen.core.facade.util.DefaultListFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.gui2.filter2.FilteredTreeViewTable;
import pcgen.gui2.util.treeview.DataView;
import pcgen.gui2.util.treeview.DataViewColumn;
import pcgen.gui2.util.treeview.DefaultDataViewColumn;
import pcgen.gui2.util.treeview.TreeView;
import pcgen.gui2.util.treeview.TreeViewModel;
import pcgen.gui2.util.treeview.TreeViewPath;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class SkillTreeViewModel implements TreeViewModel<SkillFacade>, DataView<SkillFacade>, ListSelectionListener {

    private static final List<? extends DataViewColumn> columns = Arrays.asList(new DefaultDataViewColumn("Total", Integer.class, true), new DefaultDataViewColumn("Modifier", Integer.class, true), new DefaultDataViewColumn("Ranks", Float.class, true, true), new DefaultDataViewColumn("Skill Cost", SkillCost.class, true), new DefaultDataViewColumn("Source", String.class));

    private final ListFacade<? extends TreeView<SkillFacade>> treeviews;

    private final CharacterFacade character;

    private final CharacterLevelsFacade levels;

    private final ListSelectionModel selectionModel;

    private FilteredTreeViewTable table;

    public SkillTreeViewModel(CharacterFacade character, ListSelectionModel selectionModel) {
        this.character = character;
        this.levels = character.getCharacterLevelsFacade();
        this.selectionModel = selectionModel;
        List<TreeView<SkillFacade>> views = Arrays.asList(SkillTreeView.NAME, SkillTreeView.TYPE_NAME, SkillTreeView.KEYSTAT_NAME, SkillTreeView.KEYSTAT_TYPE_NAME, COST_NAME, COST_TYPE_NAME);
        treeviews = new DefaultListFacade<TreeView<SkillFacade>>(views);
        selectionModel.addListSelectionListener(this);
    }

    public void install(FilteredTreeViewTable table) {
        if (table.getSelectionModel() != selectionModel) {
            throw new IllegalArgumentException();
        }
        this.table = table;
    }

    public void uninstall() {
        table = null;
    }

    public ListFacade<? extends TreeView<SkillFacade>> getTreeViews() {
        return treeviews;
    }

    public int getDefaultTreeViewIndex() {
        return 0;
    }

    public DataView<SkillFacade> getDataView() {
        return this;
    }

    public ListFacade<SkillFacade> getDataModel() {
        return character.getDataSet().getSkills();
    }

    public List<?> getData(SkillFacade obj) {
        CharacterLevelFacade level = levels.getElementAt(selectionModel.getMinSelectionIndex());
        return Arrays.asList(levels.getSkillTotal(level, obj), levels.getSkillModifier(level, obj), levels.getSkillRanks(level, obj), levels.getSkillCost(level, obj), obj.getSource());
    }

    public List<? extends DataViewColumn> getDataColumns() {
        return columns;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() && table != null) {
            table.refreshModelData();
        }
    }

    private enum SkillTreeView implements TreeView<SkillFacade> {

        NAME("Name"), TYPE_NAME("Type/Name"), KEYSTAT_NAME("Key Stat/Name"), KEYSTAT_TYPE_NAME("Key Stat/Type/Name");

        private String name;

        private SkillTreeView(String name) {
            this.name = name;
        }

        public String getViewName() {
            return name;
        }

        @SuppressWarnings("unchecked")
        public List<TreeViewPath<SkillFacade>> getPaths(SkillFacade pobj) {
            TreeViewPath<SkillFacade> path;
            switch(this) {
                case NAME:
                    path = new TreeViewPath<SkillFacade>(pobj);
                    break;
                case TYPE_NAME:
                    path = new TreeViewPath<SkillFacade>(pobj, pobj.getType());
                    break;
                case KEYSTAT_NAME:
                    path = new TreeViewPath<SkillFacade>(pobj, pobj.getKeyStat());
                    break;
                case KEYSTAT_TYPE_NAME:
                    path = new TreeViewPath<SkillFacade>(pobj, pobj.getKeyStat(), pobj.getType());
                    break;
                default:
                    throw new InternalError();
            }
            return Arrays.asList(path);
        }
    }

    private final TreeView<SkillFacade> COST_NAME = new TreeView<SkillFacade>() {

        public String getViewName() {
            return "Cost/Name";
        }

        @SuppressWarnings("unchecked")
        public List<TreeViewPath<SkillFacade>> getPaths(SkillFacade pobj) {
            CharacterLevelFacade level = levels.getElementAt(selectionModel.getMinSelectionIndex());
            return Arrays.asList(new TreeViewPath<SkillFacade>(pobj, levels.getSkillCost(level, pobj)));
        }
    };

    private final TreeView<SkillFacade> COST_TYPE_NAME = new TreeView<SkillFacade>() {

        public String getViewName() {
            return "Cost/Type/Name";
        }

        @SuppressWarnings("unchecked")
        public List<TreeViewPath<SkillFacade>> getPaths(SkillFacade pobj) {
            CharacterLevelFacade level = levels.getElementAt(selectionModel.getMinSelectionIndex());
            return Arrays.asList(new TreeViewPath<SkillFacade>(pobj, levels.getSkillCost(level, pobj), pobj.getType()));
        }
    };
}
