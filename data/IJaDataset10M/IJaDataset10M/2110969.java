package edu.buaa.top.tools.runner.all.actions;

import java.util.Vector;
import edu.buaa.top.tools.runner.all.preferences.util.RunnableRow;
import edu.buaa.top.tools.runner.all.preferences.util.TopRunnerTable;
import edu.buaa.top.tools.runner.all.preferences.util.TypeEnum;

public class TopRunnerList extends TopRunnerTable {

    int[] start = new int[TypeEnum.TYPE.length];

    public TopRunnerList(TopRunnerTable f) {
        Vector<RunnableRow> v = new Vector<RunnableRow>();
        RunnableRow[] r = f.getRows();
        int i = 0, j = 0;
        for (j = 0; j < TypeEnum.TYPE.length; j++) {
            start[j] = v.size();
            for (i = 0; i < r.length; i++) {
                if (r[i].getType() == j) {
                    v.add(r[i]);
                }
            }
        }
        this.setRows(v.toArray(new RunnableRow[0]));
    }

    public RunnableRow[] getRowsInType(int j) {
        int start = this.start[j];
        int length = 0;
        if (j != TypeEnum.TYPE.length - 1) {
            length = this.start[j + 1] - start;
        } else {
            length = this.getRows().length - start;
        }
        int i = 0;
        RunnableRow[] result = new RunnableRow[length];
        for (i = 0; i < length; i++) {
            result[i] = this.getRows()[start + i];
        }
        return result;
    }

    public String[] getNames(RunnableRow[] a) {
        String[] result = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i].getName();
        }
        return result;
    }

    public String[] getValues(RunnableRow[] a) {
        String[] result = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i].getValue();
        }
        return result;
    }

    public int[] getTypes(RunnableRow[] a) {
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i].getType();
        }
        return result;
    }

    public String[] getServices() {
        return this.getValues(this.getRowsInType(0));
    }

    public String[] getFolders() {
        return this.getValues(this.getRowsInType(1));
    }

    public String[] getOthers() {
        return this.getValues(this.getRowsInType(2));
    }

    public String[] getPairs() {
        return this.getValues(this.getRowsInType(3));
    }

    public String[] getServiceNames() {
        return this.getNames(this.getRowsInType(0));
    }

    public String[] getFolderNames() {
        return this.getNames(this.getRowsInType(1));
    }

    public String[] getOtherNames() {
        return this.getNames(this.getRowsInType(2));
    }

    public String[] getPairNames() {
        return this.getNames(this.getRowsInType(3));
    }
}
