package com.hardcode.gdbms.engine.data.indexes;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.instruction.IncompatibleTypesException;
import com.hardcode.gdbms.engine.values.BooleanValue;
import com.hardcode.gdbms.engine.values.Value;
import java.io.IOException;
import java.util.Stack;

/**
 * DOCUMENT ME!
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class QuickSort {

    private FixedIndexSet ret;

    private int fieldId;

    private DataSource dataSource;

    /**
	 * DOCUMENT ME!
	 *
	 * @param ini DOCUMENT ME!
	 * @param fin DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 * @throws ReadDriverException TODO
	 * @throws IOException 
	 */
    private long partition(long ini, long fin) throws ReadDriverException, IOException {
        long first = ini;
        long last = fin;
        long pivotIndex = first;
        Object pivot = dataSource.getFieldValue(ret.getIndex(pivotIndex), fieldId);
        long up = first;
        long down = last;
        while (up < down) {
            while (compare(pivot, dataSource.getFieldValue(ret.getIndex(up), fieldId)) <= 0) {
                up++;
                if (up > fin) {
                    break;
                }
            }
            while (compare(pivot, dataSource.getFieldValue(ret.getIndex(down), fieldId)) > 0) {
                down--;
                if (down < ini) {
                    break;
                }
            }
            if (up < down) {
                long aux = ret.getIndex(up);
                ret.setIndex(up, ret.getIndex(down));
                ret.setIndex(down, aux);
            }
        }
        long aux = ret.getIndex(pivotIndex);
        ret.setIndex(pivotIndex, ret.getIndex(down));
        ret.setIndex(down, aux);
        return up;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param o1 DOCUMENT ME!
	 * @param o2 DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws RuntimeException DOCUMENT ME!
	 */
    private int compare(Object o1, Object o2) {
        Value v1 = (Value) o1;
        Value v2 = (Value) o2;
        try {
            if (((BooleanValue) v1.less(v2)).getValue()) {
                return -1;
            } else if (((BooleanValue) v2.less(v1)).getValue()) {
                return 1;
            } else {
                return 0;
            }
        } catch (IncompatibleTypesException e) {
            throw new RuntimeException("Como incompatibles si se indexa sobre la misma columna?");
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param v DOCUMENT ME!
	 * @param fieldId DOCUMENT ME!
	 * @param low DOCUMENT ME!
	 * @param high DOCUMENT ME!
	 * @throws IOException
	 * @throws ReadDriverException TODO
	 */
    public void quickSort(DataSource v, int fieldId, long low, long high) throws IOException, ReadDriverException {
        dataSource = v;
        this.fieldId = fieldId;
        ret = IndexFactory.createFixedIndex(high - low + 1);
        for (int i = 0; i < ret.getIndexCount(); i++) {
            ret.setIndex(i, i);
        }
        Stack intervalos = new Stack();
        Intervalo inicial = new Intervalo(low, high);
        intervalos.push(inicial);
        while (!intervalos.empty()) {
            Intervalo i = (Intervalo) intervalos.pop();
            long pivote = partition(i.ini, i.fin);
            if (i.ini < (pivote - 1)) {
                intervalos.push(new Intervalo(i.ini, pivote - 2));
            }
            if ((pivote + 1) < i.fin) {
                intervalos.push(new Intervalo(pivote, i.fin));
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return Returns the indexes.
	 */
    public FixedIndexSet getIndexes() {
        return ret;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @author Fernando Gonz�lez Cort�s
	 */
    public class Intervalo {

        long ini;

        long fin;

        /**
		 * Crea un nuevo Intervalo.
		 *
		 * @param ini DOCUMENT ME!
		 * @param fin DOCUMENT ME!
		 */
        public Intervalo(long ini, long fin) {
            this.ini = ini;
            this.fin = fin;
        }
    }
}
