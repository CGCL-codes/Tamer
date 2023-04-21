package org.datanucleus.store.mapped.mapping;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.time.calendar.LocalDateTime;
import org.datanucleus.ClassNameConstants;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.metadata.ColumnMetaData;
import org.datanucleus.metadata.MetaDataUtils;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.types.converters.TypeConverter;

/**
 * SCO Mapping for javax.time.calendar.LocalDateTime type.
 * Can be persisted using either
 * <ul>
 * <li>Single column using a String mapping.</li>
 * <li>Single column using TIMESTAMP mapping.</li>
 * </ul>
 */
public class LocalDateTimeMapping extends TemporalMapping {

    public Class getJavaType() {
        return LocalDateTime.class;
    }

    /**
     * Accessor for the name of the java-type actually used when mapping the particular datastore
     * field. This java-type must have an entry in the datastore mappings.
     * @param index requested datastore field index.
     * @return the name of java-type for the requested datastore field.
     */
    public String getJavaTypeForDatastoreMapping(int index) {
        if (datastoreMappings == null || datastoreMappings.length == 0) {
            ColumnMetaData[] colmds = getColumnMetaDataForMember(mmd, roleForMember);
            boolean useString = false;
            if (colmds != null && colmds.length > 0 && colmds[0].getJdbcType() != null) {
                if (MetaDataUtils.isJdbcTypeString(colmds[0].getJdbcType())) {
                    useString = true;
                }
            }
            return (useString ? ClassNameConstants.JAVA_LANG_STRING : ClassNameConstants.JAVA_SQL_TIMESTAMP);
        } else if (datastoreMappings != null && datastoreMappings.length > 0 && datastoreMappings[0].isStringBased()) {
            return ClassNameConstants.JAVA_LANG_STRING;
        } else {
            return ClassNameConstants.JAVA_SQL_TIMESTAMP;
        }
    }

    protected int getDefaultLengthAsString() {
        return 10;
    }

    public void setObject(ExecutionContext ec, Object preparedStatement, int[] exprIndex, Object value) {
        if (value == null) {
            getDatastoreMapping(0).setObject(preparedStatement, exprIndex[0], null);
        } else if (datastoreMappings != null && datastoreMappings.length > 0 && datastoreMappings[0].isStringBased()) {
            TypeConverter conv = ec.getNucleusContext().getTypeManager().getTypeConverterForType(LocalDateTime.class, String.class);
            if (conv != null) {
                Object obj = conv.toDatastoreType(value);
                getDatastoreMapping(0).setObject(preparedStatement, exprIndex[0], obj);
            } else {
                throw new NucleusUserException("This type doesn't support persistence as a String");
            }
        } else {
            LocalDateTime localDate = (LocalDateTime) value;
            Calendar cal = Calendar.getInstance();
            cal.set(localDate.getYear(), localDate.getMonthOfYear().ordinal(), localDate.getDayOfMonth(), localDate.getHourOfDay(), localDate.getMinuteOfHour(), localDate.getSecondOfMinute());
            getDatastoreMapping(0).setObject(preparedStatement, exprIndex[0], cal);
        }
    }

    public Object getObject(ExecutionContext ec, Object resultSet, int[] exprIndex) {
        if (exprIndex == null) {
            return null;
        }
        Object datastoreValue = getDatastoreMapping(0).getObject(resultSet, exprIndex[0]);
        if (datastoreValue == null) {
            return null;
        }
        if (datastoreValue instanceof String) {
            TypeConverter conv = ec.getNucleusContext().getTypeManager().getTypeConverterForType(LocalDateTime.class, String.class);
            if (conv != null) {
                return conv.toMemberType(datastoreValue);
            } else {
                throw new NucleusUserException("This type doesn't support persistence as a String");
            }
        } else if (datastoreValue instanceof Date) {
            Date date = (Date) datastoreValue;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            LocalDateTime localDateTime = LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND) * 1000000);
            return localDateTime;
        } else if (datastoreValue instanceof Timestamp) {
            Timestamp ts = (Timestamp) datastoreValue;
            Calendar cal = Calendar.getInstance();
            cal.setTime(ts);
            LocalDateTime localDateTime = LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND) * 1000000);
            return localDateTime;
        } else {
            return null;
        }
    }
}
