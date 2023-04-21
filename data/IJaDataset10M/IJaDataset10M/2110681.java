package org.jcrpg.ui.window.layout;

import java.util.ArrayList;
import org.jcrpg.ui.window.element.input.InputBase;

/**
 * Simple UI layout, which uses columns to arrange elements.
 * Example:
 * <pre>
 *   SimpleLayout layout = new SimpleLayout(0.2f, 0.2f, 0.3f, 0.07f ,2);
 *   layout.addToColumn(1, element, 0.35f, 0.5f);
 * </pre>
 *
 * @author goq669
 */
public class SimpleLayout {

    private float xPosRatio;

    private float yPosRatio;

    private float colWidthRatio;

    private float rowHeightRatio;

    private int columns;

    private int[] columnRows;

    private ArrayList<InputBase> addedElements;

    /**
     * New Simple Layout
     * 
     * @param xPosRatio relative x position ratio
     * @param yPosRatio relative y position ratio
     * @param colWidthRatio column width ratio
     * @param rowHeightRatio row height ratio
     * @param columns columns number
     */
    public SimpleLayout(float xPosRatio, float yPosRatio, float colWidthRatio, float rowHeightRatio, int columns) {
        this.xPosRatio = xPosRatio;
        this.yPosRatio = yPosRatio;
        this.colWidthRatio = colWidthRatio;
        this.rowHeightRatio = rowHeightRatio;
        this.columns = columns;
        addedElements = new ArrayList<InputBase>();
        columnRows = new int[columns];
        for (int i = 0; i < columnRows.length; i++) {
            columnRows[i] = 0;
        }
    }

    /**
     * Adds an element to a column, with element dimensions ratio 1.
     * @param column column index, the first column index is 0
     * @param inputElement element to add
     */
    public void addToColumn(int column, InputBase inputElement) {
        addToColumn(column, inputElement, 1, 1);
    }

    /**
     * Adds an element to a column.
     * @param column column index, the first column index is 0
     * @param inputElement element to add
     * @param elementWidthRatio element width ratio within the cell
     * @param elementHeightRatio element height ratio within the cell
     */
    public void addToColumn(int column, InputBase inputElement, float elementWidthRatio, float elementHeightRatio) {
        addToColumn(column, inputElement, elementWidthRatio, elementHeightRatio, 1);
    }

    /**
     * Adds an element to a column.
     * @param column column index, the first column index is 0
     * @param inputElement element to add
     * @param rowSpan row span amount for the element, the element will be aligned on top
     */
    public void addToColumn(int column, InputBase inputElement, int rowSpan) {
        addToColumn(column, inputElement, 1, 1, rowSpan);
    }

    /**
     * Adds an element to a column.
     * @param column column index, the first column index is 0
     * @param inputElement element to add
     * @param elementWidthRatio element width ratio within the cell
     * @param elementHeightRatio element height ratio within the cell
     * @param rowSpan row span amount for the element, the element will be aligned on top
     */
    public void addToColumn(int column, InputBase inputElement, float elementWidthRatio, float elementHeightRatio, int rowSpan) {
        if (column > this.columns) {
            throw new IllegalArgumentException("The given column index (" + column + ") is bigger then maximum (" + this.columns + ")");
        }
        float elemX = this.xPosRatio + this.colWidthRatio * column;
        float elemY = this.yPosRatio + this.rowHeightRatio * columnRows[column];
        float elemWidth = this.colWidthRatio * elementWidthRatio;
        float elemHeight = this.rowHeightRatio * elementHeightRatio;
        inputElement.init(elemX, elemY, elemWidth, elemHeight);
        addedElements.add(inputElement);
        columnRows[column] = columnRows[column] + rowSpan;
    }
}
