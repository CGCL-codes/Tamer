package com.dfruits.convs.oracle;

import java.util.Map;
import com.dfruits.dto.datatypes.RawDataType;
import com.dfruits.queries.utils.sql.ISQLDataConverter;

public class RawDataToSQLStringConverter implements ISQLDataConverter {

    public boolean canConvert(Object value, Map<String, Object> props) {
        return value != null && value instanceof RawDataType;
    }

    public String convert(Object value, Map<String, Object> props) {
        return String.format("hextoraw( '%s' )", value.toString());
    }
}
