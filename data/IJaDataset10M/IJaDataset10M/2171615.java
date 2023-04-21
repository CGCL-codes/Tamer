package net.sf.RecordEditor.utils.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public abstract class ShowFieldsMenu {

    private JMenu menu = new JMenu("Show Column");

    private ArrayList<ShowFieldAction> hiddenColumns = new ArrayList<ShowFieldAction>();

    public abstract boolean showColumn(TableColumn colDef, int originalColumn);

    public void hideColumn(TableColumn colDefinition, int col) {
        int idx;
        String s = colDefinition.getHeaderValue().toString();
        if ((idx = s.indexOf('|')) >= 0) {
            s = s.substring(idx + 1);
        }
        System.out.println("Hide: " + s + " " + col + " " + colDefinition.getModelIndex());
        new ShowFieldAction(s, colDefinition, col);
    }

    public void removeAll() {
        menu.removeAll();
        hiddenColumns.clear();
    }

    /**
	 * @return the menu
	 */
    public final JMenu getMenu() {
        return menu;
    }

    public void addjustHiddenColumns(int tblPos, int amount, int origColumn) {
        for (ShowFieldAction action : hiddenColumns) {
            System.out.print(" ==> " + action.insertColumn + " " + tblPos + " ~ " + amount);
            if (action.insertColumn > tblPos || (action.insertColumn == tblPos && action.colDef.getModelIndex() > origColumn)) {
                action.insertColumn += amount;
            }
            System.out.println(" ==> " + action.insertColumn + " " + action.colDef.getHeaderValue() + " %% " + action.colDef.getModelIndex() + " > " + origColumn);
        }
    }

    public int doShowColumn(TableColumn column) {
        int col = -1;
        for (int i = 0; i < hiddenColumns.size(); i++) {
            if (hiddenColumns.get(i).equals(column)) {
                col = hiddenColumns.get(i).insertColumn;
                hiddenColumns.get(i).show();
                break;
            }
        }
        return col;
    }

    public boolean[] getFieldVisibility(int length, int colsToSkip) {
        boolean[] ret = new boolean[length - colsToSkip];
        int i;
        for (i = 0; i < ret.length; i++) {
            ret[i] = true;
        }
        for (i = 0; i < hiddenColumns.size(); i++) {
            ret[hiddenColumns.get(i).colDef.getModelIndex() - colsToSkip] = false;
        }
        return ret;
    }

    public void setFieldVisibility(TableColumnModel mdl, int colsToSkip, int colAdjust, boolean[] fieldVisibility) {
        int i, j, idx;
        for (i = hiddenColumns.size() - 1; i >= 0; i--) {
            idx = hiddenColumns.get(i).colDef.getModelIndex() - colsToSkip;
            if (fieldVisibility[idx]) {
                hiddenColumns.get(i).show();
            }
        }
        int hiddenNum = 0;
        for (i = 0; i < fieldVisibility.length; i++) {
            if (!fieldVisibility[i]) {
                for (j = 0; j < mdl.getColumnCount(); j++) {
                    if (mdl.getColumn(j).getModelIndex() == i + colsToSkip) {
                        hideColumn(mdl.getColumn(j), i - hiddenNum + colAdjust);
                        mdl.removeColumn(mdl.getColumn(j));
                        break;
                    }
                }
                hiddenNum += 1;
            }
        }
    }

    @SuppressWarnings("serial")
    private class ShowFieldAction extends JMenuItem implements ActionListener {

        public TableColumn colDef;

        public int insertColumn;

        public ShowFieldAction(String name, TableColumn colDefinition, int col) {
            super(name);
            colDef = colDefinition;
            insertColumn = col;
            super.addActionListener(this);
            addjustHiddenColumns(insertColumn, -1, colDef.getModelIndex());
            menu.add(this);
            hiddenColumns.add(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            show();
        }

        public void show() {
            if (showColumn(colDef, insertColumn)) {
                menu.remove(this);
                hiddenColumns.remove(this);
                addjustHiddenColumns(insertColumn, 1, colDef.getModelIndex());
            }
        }
    }
}
