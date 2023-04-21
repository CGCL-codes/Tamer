package com.devutil.plugins.youtrack.views;

import com.devutil.plugins.youtrack.util.Texts;
import com.intellij.tasks.Task;

/**
 * Created by IntelliJ IDEA.
 * User: Venom
 * Date: 1 avr. 2010
 * Time: 22:26:26
 */
public class IssueTableConstants {

    static ColumnDef[] COLUMNS_DEFINITIONS = new ColumnDef[] { new ColumnDef(Texts.ISSUE_COLUMN_NB, Integer.class, new ValueGetter() {

        Object getValue(Task task, int rowIdx) {
            return rowIdx;
        }
    }), new ColumnDef(Texts.ISSUE_COLUMN_ID, String.class, new ValueGetter() {

        Object getValue(Task task, int rowIdx) {
            return task.getId();
        }
    }), new ColumnDef(Texts.ISSUE_COLUMN_SUMMARY, String.class, new ValueGetter() {

        Object getValue(Task task, int rowIdx) {
            return task.getSummary();
        }
    }) };

    static class ColumnDef {

        String title;

        Class<?> refClass;

        ValueGetter valueGetter;

        public ColumnDef(final String title, final Class<?> refClass, final ValueGetter valueGetter) {
            this.title = title;
            this.refClass = refClass;
            this.valueGetter = valueGetter;
        }
    }

    abstract static class ValueGetter {

        abstract Object getValue(final Task task, final int rowIdx);
    }
}
