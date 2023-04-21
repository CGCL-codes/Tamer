package org.norecess.nolatte.primitives.string;

import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.support.Mapper;
import org.norecess.nolatte.types.IDataTypeFilter;

public class DowncaseMapper implements Mapper<Datum, Datum> {

    private static final long serialVersionUID = 1L;

    private final IDataTypeFilter myDataTypeFilter;

    public DowncaseMapper(IDataTypeFilter dataTypeFilter) {
        myDataTypeFilter = dataTypeFilter;
    }

    public Datum function(Datum value) {
        return myDataTypeFilter.getText(value).downcase();
    }
}
