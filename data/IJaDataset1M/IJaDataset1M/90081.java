package com.cosmos.impl.acacia.converter;

import com.cosmos.acacia.converter.annotation.DataColumn;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;

/**
 *
 * @author Miro
 */
public class LineProcessor<T> {

    private Class<T> dataClass;

    private List<Field> dataFields;

    public LineProcessor(Class<T> dataClass) {
        this.dataClass = dataClass;
        init();
    }

    private void init() {
        dataFields = new ArrayList<Field>();
        for (Field field : dataClass.getDeclaredFields()) {
            if (field.getAnnotation(DataColumn.class) == null) {
                continue;
            }
            dataFields.add(field);
        }
        if (dataFields.size() == 0) {
            throw new IllegalStateException("This data class doesn't contains DataColumn annotations.");
        }
    }

    public T processLine(String line) {
        T dataUnit;
        try {
            dataUnit = dataClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        int lineLength = line.length();
        int lastColumnPosition = 0;
        for (Field field : dataFields) {
            DataColumn dataColumn = field.getAnnotation(DataColumn.class);
            int columnPosition;
            if (dataColumn.relativeColumnPosition()) {
                columnPosition = lastColumnPosition;
            } else {
                columnPosition = dataColumn.columnPosition();
            }
            int length = dataColumn.length();
            int endIndex = columnPosition + length;
            if (columnPosition >= lineLength) {
                break;
            } else if (endIndex >= lineLength) {
                endIndex = lineLength - 1;
            }
            String data = line.substring(columnPosition, endIndex);
            setProperty(dataUnit, field.getName(), data);
            lastColumnPosition = columnPosition + length + dataColumn.columnSeparator().length();
        }
        return dataUnit;
    }

    protected void setProperty(Object bean, String name, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                BeanUtils.setProperty(bean, name, value);
            } catch (ConversionException ex) {
                String message;
                if ((message = ex.getMessage()) != null && message.contains("BigDecimal") && value.contains(",")) {
                    value = value.replace(",", "");
                    try {
                        BeanUtils.setProperty(bean, name, value);
                    } catch (Exception ex1) {
                        throw new RuntimeException(ex1);
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
