package org.databene.dbsanity.model.data;

import org.databene.commons.ArrayFormat;
import org.databene.commons.bean.ArrayWithIdentity;
import org.databene.commons.converter.ToStringConverter;

/**
 * Represents a data row of a testee to be verified against a set of data rows from a reference data set.<br/><br/>
 * Created: 25.02.2012 11:34:35
 * @since 0.9.4
 * @author Volker Bergmann
 */
public class TesteeSample {

    private Object[] cells;

    private Testee testee;

    public TesteeSample(Object[] cells, Testee testee) {
        this.cells = cells;
        this.testee = testee;
    }

    public Object[] extractCheckedElements() {
        int[] checkedColumnIndexes = testee.getCheckedColumnIndexes();
        if (checkedColumnIndexes == null) return cells;
        Object[] result = new Object[checkedColumnIndexes.length];
        for (int i = 0; i < checkedColumnIndexes.length; i++) result[i] = ToStringConverter.convert(cells[checkedColumnIndexes[i]], null);
        return result;
    }

    public ArrayWithIdentity getId() {
        return IdentityUtil.identity(extractCheckedElements(), testee.getIdentityColumnIndexes());
    }

    public Object[] getCells() {
        return cells;
    }

    public int getHeadingUncheckedColumnCount() {
        return testee.getCheckedColumnIndexes()[0];
    }

    @Override
    public String toString() {
        return ArrayFormat.format(cells);
    }
}
