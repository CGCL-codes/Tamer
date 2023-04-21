package org.jcvi.tasker.table;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * A <code>TableCell</code> is a generic cell within a {@link SampleTablePanel}.
 *
 * @author jsitz@jcvi.org
 */
class TableCell extends JLabel {

    /** The Serial Version UID. */
    private static final long serialVersionUID = -1596662717293122405L;

    /** The color of the borders to use for cells. */
    private static final Color BORDER_COLOR = Color.BLACK;

    /** The standard cell padding border. */
    private static final Border CELLPAD_BORDER = BorderFactory.createEmptyBorder(4, 8, 4, 8);

    /** The cell padding border for LEFT cells in cell blocks. */
    private static final Border CELLPAD_LEFT_BORDER = BorderFactory.createEmptyBorder(4, 8, 4, 0);

    /** The cell padding border for LEFT cells in cell blocks. */
    private static final Border CELLPAD_MIDDLE_BORDER = BorderFactory.createEmptyBorder(4, 0, 4, 0);

    /** The cell padding border for LEFT cells in cell blocks. */
    private static final Border CELLPAD_RIGHT_BORDER = BorderFactory.createEmptyBorder(4, 0, 4, 8);

    /** A full border on all sides of a cell. */
    private static final Border FULL_BORDER = BorderFactory.createLineBorder(TableCell.BORDER_COLOR);

    /** A border on all but the right side of the cell. */
    private static final Border LEFT_BORDER = BorderFactory.createMatteBorder(1, 1, 1, 0, TableCell.BORDER_COLOR);

    /** A border on all but the left side of the cell. */
    private static final Border RIGHT_BORDER = BorderFactory.createMatteBorder(1, 0, 1, 1, TableCell.BORDER_COLOR);

    /** A border on only the top and bottom of a cell. */
    private static final Border MIDDLE_BORDER = BorderFactory.createMatteBorder(1, 0, 1, 0, TableCell.BORDER_COLOR);

    /**
     * Looks up the line {@link Border} to use on a cell based on the type requested.
     *
     * @param borderType The border to use.
     * @return The <code>Border</code> to use.
     */
    protected static Border getBorder(int borderType) {
        switch(borderType) {
            case SwingConstants.LEFT:
                return TableCell.LEFT_BORDER;
            case SwingConstants.RIGHT:
                return TableCell.RIGHT_BORDER;
            case SwingConstants.CENTER:
                return TableCell.MIDDLE_BORDER;
            default:
                return TableCell.FULL_BORDER;
        }
    }

    /**
     * Looks up the {@link Border} to use for padding on a cell based on the type requested.
     *
     * @param borderType The padding border to use.
     * @return The <code>Border</code> to use.
     */
    protected static Border getPadding(int borderType) {
        switch(borderType) {
            case SwingConstants.LEFT:
                return TableCell.CELLPAD_LEFT_BORDER;
            case SwingConstants.RIGHT:
                return TableCell.CELLPAD_RIGHT_BORDER;
            case SwingConstants.CENTER:
                return TableCell.CELLPAD_MIDDLE_BORDER;
            default:
                return TableCell.CELLPAD_BORDER;
        }
    }

    /**
     * Constructs a new <code>SampleTablePanel.TableLabel</code>.
     *
     * @param icon The {@link Icon} to display.
     * @param contents The text contents of the label.
     * @param alignment The alignment of the contents of the cell.
     * @param borderType The border type to draw around the cell.
     */
    public TableCell(Icon icon, String contents, int alignment, int borderType) {
        super(contents, icon, alignment);
        this.setBorder(this.getCellBorder(borderType));
        this.init();
    }

    /**
     * Constructs a new <code>SampleTablePanel.TableLabel</code>.
     *
     * @param icon The {@link Icon} to display.
     * @param contents The text contents of the label.
     * @param alignment The alignment of the contents of the cell.
     */
    public TableCell(Icon icon, String contents, int alignment) {
        this(icon, contents, alignment, -1);
    }

    /**
     * Constructs a new <code>SampleTablePanel.TableLabel</code>.
     *
     * @param icon The {@link Icon} to display.
     * @param contents The text contents of the label.
     */
    public TableCell(Icon icon, String contents) {
        this(icon, contents, SwingConstants.LEFT);
    }

    /**
     * Constructs a new <code>SampleTablePanel.TableLabel</code>.
     *
     * @param contents The text contents of the label.
     */
    public TableCell(String contents) {
        this(null, contents, SwingConstants.LEFT);
    }

    /**
     * Constructs a new <code>SampleTablePanel.TableLabel</code>.
     *
     * @param icon The {@link Icon} to display.
     */
    public TableCell(Icon icon) {
        this(icon, "", SwingConstants.CENTER);
    }

    /**
     * Constructs a new <code>SampleTablePanel.TableLabel</code>.
     *
     * @param contents The text contents of the label.
     * @param alignment The alignment of the contents of the cell.
     * @param borderType The border type to draw around the cell.
     */
    public TableCell(String contents, int alignment, int borderType) {
        this(null, contents, alignment, borderType);
    }

    /**
     * Constructs a new <code>SampleTablePanel.TableLabel</code>.
     *
     * @param contents The text contents of the label.
     * @param alignment The alignment of the contents of the cell.
     */
    public TableCell(String contents, int alignment) {
        this(null, contents, alignment, -1);
    }

    /**
     * Fetches the standard border used to apply padding to the cell.
     *
     * @param borderType The requested border type.
     * @return A {@link Border}.
     */
    protected Border getPaddingBorder(int borderType) {
        return TableCell.getPadding(borderType);
    }

    /**
     * Creates a compound border suitable for this cell.
     *
     * @param borderType The requested border type.
     * @return A new {@link Border}.
     */
    protected Border getCellBorder(int borderType) {
        return BorderFactory.createCompoundBorder(TableCell.getBorder(borderType), this.getPaddingBorder(borderType));
    }

    /**
     * Performs any extra initialization for this cell.
     */
    protected void init() {
        this.setOpaque(true);
    }
}
