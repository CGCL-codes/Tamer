package bntu.iss.diplom.creator.gui.tablemodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import bntu.iss.diplom.creator.objects.DFTSubBean;
import bntu.iss.diplom.creator.objects.SimpleDFTOption;

public class DFTOptionsTableModel extends AbstractTableModel {

    private Map<String, List<SimpleDFTOption>> beansMap = new HashMap<String, List<SimpleDFTOption>>();

    private final String[] COLUMNS = new String[] { "subdirective", "option", "param value" };

    private List<DFTRow> rows = new ArrayList<DFTRow>();

    public DFTOptionsTableModel() {
    }

    public int getColumnCount() {
        return 3;
    }

    public void addBean(DFTSubBean task) {
        if (getBeansMap().get(task.getName()) != null) {
            getBeansMap().get(task.getName()).addAll(task.getOptionsList());
        } else {
            getBeansMap().put(task.getName(), task.getOptionsList());
        }
        refreshRows();
    }

    private void refreshRows() {
        rows.clear();
        for (String key : getBeansMap().keySet()) {
            String beanName = key;
            for (SimpleDFTOption opt : getBeansMap().get(key)) {
                rows.add(new DFTRow(beanName, opt.getName(), opt.getStringPrefactor()));
                beanName = "";
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rows.get(rowIndex).getName();
            case 1:
                return rows.get(rowIndex).getOption();
            case 2:
                return rows.get(rowIndex).getPrefactor();
        }
        return "";
    }

    public Map<String, List<SimpleDFTOption>> getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map<String, List<SimpleDFTOption>> beansMap) {
        this.beansMap = beansMap;
        refreshRows();
    }
}
