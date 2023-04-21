package com.kescom.matrix.core.user;

import java.text.ParseException;
import java.util.Date;
import com.kescom.matrix.core.db.IndexBase;
import com.kescom.matrix.core.directory.ISeriesTemplate;
import com.kescom.matrix.core.series.IDataPointType;
import com.kescom.matrix.core.series.IPrivacyLevel;

public abstract class ReportingEntityBase extends IndexBase implements IReportingEntity {

    public String formatField(int index) {
        Object value = getField(index);
        if (value != null) return getTemplate().getSchema().getFields().get(index).getType().getFormat().format(value); else return null;
    }

    public void parseField(int index, String text) throws ParseException {
        if (text != null) setField(index, getTemplate().getSchema().getFields().get(index).getType().getFormat().parseObject(text)); else setField(index, null);
    }

    protected Object fromText(IDataPointType type, String text) {
        String dataType = type.getDataType();
        try {
            if (dataType.equals(IDataPointType.DT_BOOLEAN)) return Boolean.parseBoolean(text); else if (dataType.equals(IDataPointType.DT_DOUBLE)) return Double.parseDouble(text); else if (dataType.equals(IDataPointType.DT_LONG)) return Long.parseLong(text); else if (dataType.equals(IDataPointType.DT_TIME)) return new Date(Long.parseLong(text)); else return text;
        } catch (Exception e) {
            if (dataType.equals(IDataPointType.DT_BOOLEAN)) return false; else if (dataType.equals(IDataPointType.DT_DOUBLE)) return new Double(0.0); else if (dataType.equals(IDataPointType.DT_LONG)) return new Long(0); else if (dataType.equals(IDataPointType.DT_TIME)) return new Date(0); else return text;
        }
    }

    protected String toText(IDataPointType type, Object obj) {
        if (obj instanceof Date) return Long.toString(((Date) obj).getTime()); else return obj.toString();
    }

    public String getCalculatedSummary() {
        return getTemplate().getName();
    }

    public String getCalculatedTitle() {
        return getUser().getNickname() + "/" + getName();
    }
}
