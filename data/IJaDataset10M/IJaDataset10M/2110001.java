package org.gwt.advanced.client.datamodel;

/**
 * This is a hierarchical grid data model interface.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface Hierarchical extends Editable {

    /**
     * This method adds a new submodel into the cell.
     *
     * @param rowNumber    is a row number.
     * @param columnNumber is a column number.
     * @param model        is a submodel to be added, can be <code>null</code>. If so it removes any model
     *                     from the specified cell if it's added.
     * @throws IllegalArgumentException if row or column numebr is out of range.
     */
    void addSubgridModel(int rowNumber, int columnNumber, GridDataModel model) throws IllegalArgumentException;

    /**
     * This method gets a submodel beloging to the specified cell.<p> It returns <code>null</code>
     * if there is no such submodel.
     *
     * @param rowNumber    is a row number.
     * @param columnNumber is a column number.
     * @return a submodel.
     * @throws IllegalArgumentException if row or column numebr is out of range.
     */
    GridDataModel getSubgridModel(int rowNumber, int columnNumber) throws IllegalArgumentException;

    /**
     * This method sets expanded flag for the specified cell.
     *
     * @param row      is a row number.
     * @param column   is a column number.
     * @param expanded is an expanded flag value.
     */
    void setExpanded(int row, int column, boolean expanded);

    /**
     * This method checks whether the specified cell is expanded.
     *
     * @param row    is a row number.
     * @param column is a column number.
     * @return an expanded falg value.
     */
    boolean isExpanded(int row, int column);
}
