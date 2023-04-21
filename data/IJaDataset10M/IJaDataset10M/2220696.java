package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the action command used for each Column Delete action within the
 * Grid menu. It deletes the currently-selected column only if the new column
 * count does not reach zero. It is appropriate to the Layout Outline page and
 * the Layout Graphical Editor page. It is enabled for a single grid cell
 * format selection (where a grid cell format is a format that is an immediate
 * child of a grid row).
 */
public class ColumnDeleteActionCommand extends LayoutActionCommand {

    /**
     * An element filter used to filter out all non element nodes.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

    /**
     * Initializes the new instance using the given parameters.
     * @param selectionManager selection manager associated with the ODOM that
     * this command modifies. Cannot be null.
     */
    public ColumnDeleteActionCommand(ODOMSelectionManager selectionManager) {
        super(selectionManager);
    }

    /**
     * The action is only valid when there is a single grid cell selected, and
     * when there is more than one column in the grid.
     * @return true if enabled; false otherwise.
     */
    public boolean enable(ODOMActionDetails details) {
        return ((details.getNumberOfElements() == 1) && selectionIsAppropriate(details.getElement(0)));
    }

    /**
     * Supporting method that is used to make sure that the selection is
     * appropriate for this action command.
     *
     * @param selection the single selected element
     * @return true if the element's grandparent is a grid or segment grid.
     */
    private boolean selectionIsAppropriate(Element selection) {
        boolean isAppropriate = false;
        Element parent = selection.getParent();
        if (parent != null) {
            parent = parent.getParent();
        }
        if (parent != null) {
            String grandParentName = parent.getName();
            if (grandParentName.equals(FormatType.GRID.getElementName()) || grandParentName.equals(FormatType.SEGMENT_GRID.getElementName())) {
                try {
                    int columnCount = parent.getAttribute("columns").getIntValue();
                    isAppropriate = (columnCount > 1);
                } catch (DataConversionException e) {
                }
            }
        }
        return isAppropriate;
    }

    /**
     * Runs the DeleteColumnActionCommand with the supplied details.
     * @param details the details needed for running the command
     */
    public void run(ODOMActionDetails details) {
        if (details.getNumberOfElements() != 1) {
            throw new IllegalStateException("Only a single selection should be available when the " + "action is run (" + details.getNumberOfElements() + " elements selected)");
        } else {
            List selection = deleteColumn(details.getElement(0));
            if (selectionManager != null) {
                selectionManager.setSelection(selection);
            }
        }
    }

    /**
     * Deletes the column of the currently-selected grid cell from the grid.
     * <strong>
     * {@link com.volantis.mcs.eclipse.ab.editors.layout.states.ColumnChangeState}
     * is tightly-coupled with this method's implementation.
     * </strong>
     * @param gridCell the grid cell whose column is to be deleted
     * @return a list of the cells to select, post-deletion
     * @throws IllegalArgumentException if deletion cannot be performed for
     *         gridCell
     */
    private List deleteColumn(Element gridCell) {
        List selectedElements = new ArrayList(1);
        Element grid = gridCell.getParent().getParent();
        int columnCount = 0;
        int rowCount = 0;
        try {
            columnCount = grid.getAttribute("columns").getIntValue();
            rowCount = grid.getAttribute("rows").getIntValue();
        } catch (DataConversionException e) {
            throw new UndeclaredThrowableException(e);
        }
        List gridContent = grid.getContent(ELEMENT_FILTER);
        Element selectedRow = gridCell.getParent();
        int colPos = selectedRow.getContent(ELEMENT_FILTER).indexOf(gridCell);
        grid.setAttribute("columns", String.valueOf(--columnCount));
        ODOMElement cols = (ODOMElement) gridContent.get(0);
        ODOMElement column = (ODOMElement) cols.getContent(ELEMENT_FILTER).get(colPos);
        column.detach();
        for (int row = 1; row <= rowCount; row++) {
            ODOMElement rowElement = (ODOMElement) gridContent.get(row);
            List rowContent = new ArrayList(rowElement.getContent(ELEMENT_FILTER));
            ODOMElement colCell = (ODOMElement) rowContent.get(colPos);
            colCell.detach();
        }
        if (colPos == columnCount) {
            colPos--;
        }
        Element selectedCell = (Element) selectedRow.getContent(ELEMENT_FILTER).get(colPos);
        selectedElements.add(selectedCell);
        return selectedElements;
    }
}
