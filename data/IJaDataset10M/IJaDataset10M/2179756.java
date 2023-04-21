package ch.intertec.storybook.view.modify;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.model.DbTable;
import ch.intertec.storybook.model.Location;
import ch.intertec.storybook.model.SbCharacter;
import ch.intertec.storybook.model.Scene;
import ch.intertec.storybook.toolkit.swing.DbTableCheckBox;

public abstract class AbstractLinksFactory {

    private List<Object> linksList;

    private DbTable table;

    public AbstractLinksFactory(List<Object> linksList, DbTable table) {
        this.linksList = linksList;
        this.table = table;
    }

    public List<? extends DbTable> doSelectByCharacter(SbCharacter character) {
        return null;
    }

    public List<? extends DbTable> doSelectByLocation(Location location) {
        return null;
    }

    public List<? extends DbTable> doSelectByScene(Scene scene) {
        return null;
    }

    public abstract List<? extends DbTable> doSelectAll();

    public abstract List<? extends DbTable> doSelect(int id1, int id2);

    public JPanel createPanel() {
        try {
            MigLayout layout = new MigLayout("wrap 4,insets 2");
            JPanel panel = new JPanel(layout);
            panel.setOpaque(false);
            List<? extends DbTable> tableList = doSelectAll();
            for (DbTable t : tableList) {
                DbTableCheckBox<DbTable> tcb = new DbTableCheckBox<DbTable>(t);
                if (table != null) {
                    int tableId = table.getId();
                    List<? extends DbTable> list = doSelect(tableId, t.getId());
                    if (!list.isEmpty()) {
                        tcb.setSelected(true);
                    }
                }
                linksList.add((Object) tcb);
                panel.add(tcb);
            }
            return panel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JPanel();
    }

    public JList createList(SbCharacter character, boolean isNew) {
        return createList(character, isNew, 280);
    }

    public JList createList(SbCharacter character, boolean isNew, int width) {
        try {
            List<? extends DbTable> tableList = new ArrayList<DbTable>();
            if (character != null) {
                tableList = doSelectByCharacter(character);
            } else if (isNew == false) {
                tableList = doSelectAll();
            }
            return this.getJList(tableList, isNew, width);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JList();
    }

    public JList createList(Location location, boolean isNew) {
        return createList(location, isNew, 280);
    }

    public JList createList(Location location, boolean isNew, int width) {
        try {
            List<? extends DbTable> tableList = new ArrayList<DbTable>();
            if (location != null) {
                tableList = doSelectByLocation(location);
            } else if (isNew == false) {
                tableList = doSelectAll();
            }
            return this.getJList(tableList, isNew, width);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JList();
    }

    public JList createList() {
        return createList((Scene) null, false);
    }

    public JList createList(Scene scene, boolean isNew) {
        return createList(scene, isNew, 280);
    }

    public JList createList(Scene scene, boolean isNew, int width) {
        try {
            List<? extends DbTable> tableList = new ArrayList<DbTable>();
            if (scene != null) {
                tableList = doSelectByScene(scene);
            } else if (isNew == false) {
                tableList = doSelectAll();
            }
            return this.getJList(tableList, isNew, width);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JList();
    }

    private JList getJList(List<? extends DbTable> tableList, boolean isNew, int width) {
        List<Integer> indexList = new ArrayList<Integer>();
        DefaultListModel model = new DefaultListModel();
        int index = 0;
        for (DbTable t : tableList) {
            t.setToStringUsedForList(true);
            model.addElement(t);
            ++index;
        }
        JList list = new JList(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(-1);
        list.setFixedCellWidth(width);
        if (isNew) {
            return list;
        }
        int[] indices = new int[indexList.size()];
        for (int i = 0; i < indexList.size(); ++i) {
            indices[i] = indexList.get(i).intValue();
        }
        list.setSelectedIndices(indices);
        return list;
    }
}
