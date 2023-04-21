package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.quantity.math.Real;

public class RealTypeMapper implements TypeMapper {

    private static final RealTypeMapper ME = new RealTypeMapper();

    public static RealTypeMapper instance() {
        return ME;
    }

    @Override
    public String getMappedClassName() {
        return Real.class.getName();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object read(DataRow row, Object aggregateParent, DataColumnModel... columns) {
        Number number = (Number) row.getColumn(columns[0].getName()).getValue();
        return Real.valueOf(number);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void write(Object object, DataRow row, DataColumnModel... columns) {
        Real number = (Real) object;
        row.getColumn(columns[0].getName()).setValue(number.asNumber());
    }
}
