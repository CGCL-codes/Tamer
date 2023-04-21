package de.huxhorn.sulky.swing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.*;

public class Tables {

    private static final Method CONVERT_ROW_INDEX_TO_MODEL_METHOD;

    private static final Method SET_AUTO_CREATE_ROW_SORTER_METHOD;

    static {
        Method method = null;
        try {
            method = JTable.class.getMethod("setAutoCreateRowSorter", boolean.class);
        } catch (Throwable e) {
        }
        SET_AUTO_CREATE_ROW_SORTER_METHOD = method;
        method = null;
        try {
            method = JTable.class.getMethod("convertRowIndexToModel", int.class);
        } catch (Throwable e) {
        }
        CONVERT_ROW_INDEX_TO_MODEL_METHOD = method;
    }

    public static void setAutoCreateRowSorter(JTable table, boolean auto) {
        if (SET_AUTO_CREATE_ROW_SORTER_METHOD != null) {
            try {
                SET_AUTO_CREATE_ROW_SORTER_METHOD.invoke(table, auto);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    public static int convertRowIndexToModel(JTable table, int row) {
        int result = row;
        if (CONVERT_ROW_INDEX_TO_MODEL_METHOD != null) {
            try {
                result = (Integer) CONVERT_ROW_INDEX_TO_MODEL_METHOD.invoke(table, row);
            } catch (Throwable e) {
            }
        }
        return result;
    }
}
