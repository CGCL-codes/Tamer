package com.mockturtlesolutions.snifflib.stats;

import com.mockturtlesolutions.snifflib.datatypes.DataSet;
import java.util.Vector;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

/**
Sorts rows of a DataSet with respect to one or more variables 
in the DataSet.
If multiple variables are used to sort, the sorting is done in
the sequence in which those variables are entered into the 
"by-variables".

If response variables are supplied then only those variables
are sorted and returned in the result.
*/
public class Sort {

    private DataSet dataSet;

    private DataSet result;

    private Vector byVariables;

    private Vector responseVariables;

    private boolean ignoreNaN;

    private int[] datacols;

    public Sort(DataSet S) {
        this.dataSet = S;
        this.byVariables = new Vector();
        this.responseVariables = new Vector();
        this.ignoreNaN = true;
    }

    /**
	Data is sorted with respect to the By-variables in the
	order in which the by-variables are entered.
	*/
    public void addByVariable(String varname) {
        this.byVariables.add(varname);
    }

    public void addResponseVariable(String varname) {
        this.responseVariables.add(varname);
    }

    /**
	Removes the variable as a "by-variable."
	*/
    public void removeByVariable(String varname) {
        this.byVariables.remove(varname);
    }

    /**
	Removes the variable as a response variable.
	*/
    public void removeResponseVariable(String varname) {
        this.responseVariables.remove(varname);
    }

    public Vector getByVariables() {
        return (this.byVariables);
    }

    public Vector getResponseVariables() {
        return (this.responseVariables);
    }

    /**
	Return sorted data set.
	*/
    public DataSet getResult() {
        return (this.result);
    }

    /**
	The mean over each unique combination of the "by-variables".
	A separate column is produced for each response variable and
	a separate row for each unique combination of the by-variables.
	
	While the by-variables may be numeric or categorical, the response
	variables must implement the 
	*/
    public void run() {
        Vector index = new Vector();
        this.datacols = new int[this.responseVariables.size()];
        for (int j = 0; j < this.responseVariables.size(); j++) {
            this.datacols[j] = this.dataSet.getColumnForName((String) this.responseVariables.get(j));
        }
        for (int j = 0; j < dataSet.getRowCount(); j++) {
            index.add(new DefaultDataSetComparator(j));
        }
        Collections.sort(index, new DefaultDataSetComparator(0));
        Vector outclass = new Vector();
        for (int j = 0; j < outclass.size(); j++) {
            outclass.add(this.dataSet.getColumnClass(j));
        }
        this.result = new DataSet(this.responseVariables, outclass);
        int idx = 0;
        Object val = null;
        int currentrow = 0;
        for (int i = 0; i < index.size(); i++) {
            idx = ((DefaultDataSetComparator) index.get(i)).intValue();
            for (int j = 0; j < this.responseVariables.size(); j++) {
                val = this.dataSet.getValueAt(currentrow, this.datacols[j]);
                this.result.setValueAt(val, idx, j);
            }
        }
    }

    public class DefaultDataSetComparator implements Comparator {

        private Integer value;

        public DefaultDataSetComparator(int j) {
            this.value = new Integer(j);
        }

        public int intValue() {
            return (this.value.intValue());
        }

        public int compare(Object t, Object v) {
            int a = ((DefaultDataSetComparator) t).intValue();
            int b = ((DefaultDataSetComparator) v).intValue();
            int out = 0;
            String byvar;
            int col;
            Comparable A;
            Object B;
            Class Aclass;
            for (int crit = 0; crit < byVariables.size(); crit++) {
                col = datacols[crit];
                A = (Comparable) dataSet.getValueAt(a, col);
                B = dataSet.getValueAt(b, col);
                if (A instanceof String) {
                    out = A.compareTo(B);
                }
                if (out != 0) {
                    break;
                }
            }
            return (out);
        }

        public boolean equals(Object v) {
            int a = ((DefaultDataSetComparator) this).intValue();
            int b = ((DefaultDataSetComparator) v).intValue();
            boolean out = true;
            String byvar;
            int col;
            Comparator A;
            Object B;
            for (int crit = 0; crit < byVariables.size(); crit++) {
                col = datacols[crit];
                A = (Comparator) dataSet.getValueAt(a, col);
                B = dataSet.getValueAt(b, col);
                out = A.equals(B);
                if (out == false) {
                    break;
                }
            }
            return (out);
        }
    }
}
