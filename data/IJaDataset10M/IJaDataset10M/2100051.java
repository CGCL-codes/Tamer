package com.novasurv.turtle.frontend.swing.grades;

import com.novasurv.turtle.backend.project.GradeAssertion;

/**
 * Wrapper on top of GradeItem to carry success/failure information to each cell, used
 * for the row background coloring.
 *
 * @author Jason Dobies
 */
public class GradeCellValue {

    private Object value;

    private boolean isTotal;

    /**
     * Hook to the entire item being rendered by this holder.
     */
    private GradeAssertion item;

    /**
     * Creates a new cell value holder for displaying a grade item.
     *
     * @param item  grade item being displayed
     * @param value indicates the specific value to render in this cell
     */
    public GradeCellValue(GradeAssertion item, Object value) {
        this.item = item;
        this.value = value;
    }

    /**
     * Creates a new cell value holder for non-grade item row, such as a summary
     * row. This will currently cause {#isTotal()} to return <code>true</code> as that
     * is the only non-grade row supported, however in the future this may be expanded if
     * necessary to indicate a more fine grained row type.
     *
     * @param value value to display in the cell
     */
    public GradeCellValue(Object value) {
        this.value = value;
        this.isTotal = true;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isTotal() {
        return isTotal;
    }

    public void setTotal(boolean total) {
        isTotal = total;
    }

    public GradeAssertion getItem() {
        return item;
    }

    public void setItem(GradeAssertion item) {
        this.item = item;
    }

    public String toString() {
        return "";
    }
}
