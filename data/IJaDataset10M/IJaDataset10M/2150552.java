package org.wings.table;

import javax.swing.ListSelectionModel;
import org.wings.SComponent;
import org.wings.STable;
import org.wings.STable;
import org.wings.SLabel;
import org.wings.SIcon;
import org.wings.SResourceIcon;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 1759 $
 */
public class SDefaultTableRowSelectionRenderer extends SLabel implements STableCellRenderer {

    public static final SResourceIcon DEFAULT_MULTI_SELECTION_ICON = new SResourceIcon("org/wings/icons/SelectedCheckBox.gif");

    public static final SResourceIcon DEFAULT_MULTI_NOT_SELECTION_ICON = new SResourceIcon("org/wings/icons/NotSelectedCheckBox.gif");

    public static final SResourceIcon DEFAULT_SINGLE_SELECTION_ICON = new SResourceIcon("org/wings/icons/SelectedRadioButton.gif");

    public static final SResourceIcon DEFAULT_SINGLE_NOT_SELECTION_ICON = new SResourceIcon("org/wings/icons/NotSelectedRadioButton.gif");

    /** Style to use for the foreground for non-selected nodes. */
    protected String nonSelectionStyle;

    /** Style to use for the foreground for non-selected nodes. */
    protected String selectionStyle;

    protected SResourceIcon multiSelectionIcon = DEFAULT_MULTI_SELECTION_ICON;

    protected SResourceIcon multiNotSelectionIcon = DEFAULT_MULTI_NOT_SELECTION_ICON;

    protected SResourceIcon singleSelectionIcon = DEFAULT_SINGLE_SELECTION_ICON;

    protected SResourceIcon singleNotSelectionIcon = DEFAULT_SINGLE_NOT_SELECTION_ICON;

    /**
     * TODO: documentation
     *
     */
    public SDefaultTableRowSelectionRenderer() {
    }

    public SComponent getTableCellRendererComponent(STable table, Object value, boolean selected, int row, int col) {
        switch(table.getSelectionMode()) {
            case ListSelectionModel.SINGLE_SELECTION:
                setIcon(selected ? singleSelectionIcon : singleNotSelectionIcon);
                break;
            default:
                setIcon(selected ? multiSelectionIcon : multiNotSelectionIcon);
                break;
        }
        if (selected) {
            setStyle(selectionStyle);
        } else {
            setStyle(nonSelectionStyle);
        }
        return this;
    }

    /**
     * Sets the style the cell is drawn with when the cell isn't selected.
     *
     * @param newStyle
     */
    public void setNonSelectionStyle(String newStyle) {
        nonSelectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell isn't selected.
     *
     * @return
     */
    public String getNonSelectionStyle() {
        return nonSelectionStyle;
    }

    /**
     * Sets the style the cell is drawn with when the cell isn't selected.
     *
     * @param newStyle
     */
    public void setSelectionStyle(String newStyle) {
        selectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell isn't selected.
     *
     * @return
     */
    public String getSelectionStyle() {
        return selectionStyle;
    }
}
