package sears.search.core;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import sears.file.Subtitle;
import sears.gui.MainWindow;
import sears.gui.SubtitleCellComponent;
import sears.gui.SubtitleTableCellRenderer;
import sears.gui.SubtitleTableModel;
import sears.gui.glassPane.ViewportGlassPaneModule;
import sears.gui.search.FindDialog;
import sears.search.data.CharIndexIterator;
import sears.search.gui.JTableManipulation;
import sears.search.gui.SearchViewportGlassPane;

/**
 * BETA CLASS:
 * Find action core, extension of <tt>MainWindow</tt> class
 */
public class FindModule extends ViewportGlassPaneModule {

    private int lastSelectedRow;

    private JTable table;

    private SearchViewportGlassPane svgp;

    private PillManager pm;

    private JTableManipulation jtm;

    private CharIndexIterator charIndexIterator;

    private boolean fireAdjustementValueChangedEnabled = true;

    /**
	 * Constructs a new <code>FindModule</code> object.
	 * @param scrollPane
	 * @throws NullPointerException if scrollPane is <tt>null</tt>
	 * @throws NullPointerException	if its view port is <tt>null</tt>
	 * @throws IllegalArgumentException	if the view of the view port is not a <code>JTable</code> object
	 * 
	 * @see JScrollPane
	 * @see JViewport
	 */
    public FindModule(JScrollPane scrollPane) {
        super(scrollPane);
        if (view instanceof JTable) {
            table = (JTable) view;
        } else {
            throw new IllegalArgumentException("the view is not instance of JTable");
        }
        pm = new PillManager();
        svgp = new SearchViewportGlassPane(viewport, pm);
        svgp.addAsAGlassPane(MainWindow.instance);
        jtm = new JTableManipulation(scrollPane);
        lastSelectedRow = 0;
        super.isViewportChanged = false;
        charIndexIterator = null;
    }

    /**
	 * Tests if the the <code>String</code> object is valid to perform
	 * find action
	 * @param str 	the string to test
	 * @return 		true if the string is valid, false if not
	 */
    private boolean isAValidStringForAFindAction(String str) {
        return (str != null && str.trim().length() > 0);
    }

    /**
	 * Principal method
	 * @param dialog		the dialog which calls the find action
	 * @param subtitleList	the subtitle list where search occurs
	 * @param backward		direction of search
	 */
    public void fire(FindDialog dialog, ArrayList<Subtitle> subtitleList, boolean backward) {
        fireAdjustementValueChangedEnabled = false;
        if (dialog == null) {
            throw new NullPointerException("Dialog object is null");
        }
        sears.search.data.SubtitleFile sf = null;
        String str = dialog.getText();
        if (isAValidStringForAFindAction(str)) {
            sf = new sears.search.data.SubtitleFile(subtitleList);
            setCountOfOccurrences(dialog, sf, str);
            int rowAndChar[] = getRowAndCharIndex(str, subtitleList, backward);
            if (rowAndChar != null) {
                int row = rowAndChar[0];
                SubtitleCellComponent cellComponent = getSubtitleCellAtRow(row);
                if (cellComponent != null) {
                    svgp.setVisible(true);
                    highlightSubString(str, table);
                    Rectangle cell = table.getCellRect(row, SubtitleTableModel.SUBTITLE_COLUMN, false);
                    jtm.scrollCellToVisible(row, SubtitleTableModel.SUBTITLE_COLUMN, backward);
                    Point location = this.getPillLocation(cell);
                    try {
                        pm.firePillDataMustBeUpdated(str, cellComponent, rowAndChar[1], location);
                        svgp.updatePill();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    cancel();
                    sf = null;
                }
            } else {
                cancel();
                sf = null;
            }
        } else {
            cancel();
            sf = null;
        }
        if (sf == null) {
            try {
                setCountOfOccurrences(dialog, null, null);
            } catch (NullPointerException e) {
            }
        }
        fireAdjustementValueChangedEnabled = true;
    }

    /**
	 * Update text of the find dialog
	 * Developer: <tt>NullPointerException</tt> is throw if <tt>dialog</tt> is null
	 * @param dialog		the find dialog
	 * @param subtitleFile	the subtitle file needed to get back the count of occurrences of <tt>str</tt> string
	 * @param str			the string to search
	 */
    private void setCountOfOccurrences(FindDialog dialog, sears.search.data.SubtitleFile subtitleFile, String str) {
        final FindDialog df = dialog;
        if (subtitleFile == null) {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    df.setOccurencesCount(-1);
                }
            });
        } else {
            final String text = str;
            final sears.search.data.SubtitleFile sf = subtitleFile;
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new Thread(new Runnable() {

                        public void run() {
                            df.setOccurencesCount(sf.getCountOfOccurrencesOfText(text));
                        }
                    }).start();
                }
            });
        }
    }

    /**
	 * Cancel the action
	 */
    public void cancel() {
        highlightSubString(null, table);
        svgp.setVisible(false);
    }

    /**
	 * Gets the <tt>SubtitleCellComponent</tt> object at the specified row
	 * @param row	the specified row
	 * @return		the <tt>SubtitleCellComponent</tt> object or null if there's no <tt>SubtitleCellComponent</tt> object at this row
	 */
    private SubtitleCellComponent getSubtitleCellAtRow(int row) {
        SubtitleCellComponent cell = null;
        Component component = table.prepareRenderer(table.getCellRenderer(row, SubtitleTableModel.SUBTITLE_COLUMN), row, SubtitleTableModel.SUBTITLE_COLUMN);
        if (component instanceof SubtitleCellComponent) {
            cell = (SubtitleCellComponent) component;
        }
        return cell;
    }

    /**
	 * @param cell	the cell 
	 * @return		the point in context or null if <tt>cell/tt> is null
	 */
    public Point getPillLocation(Rectangle cell) {
        Point location = null;
        if (cell != null) {
            cell = SwingUtilities.convertRectangle(viewport.getView(), cell.getBounds(), svgp);
            location = cell.getLocation();
        }
        return location;
    }

    public void fireViewChange() {
        super.fireViewChange();
        charIndexIterator = null;
    }

    /**
	 * Initializes variables, depending to the search state.
	 * 
	 * @param str			the string to find (and search)
	 * @param subtitleList	the subtitle list on which the search operation is perform
	 * @param table			the <code>JTable</code> object ...
	 * @return				an array of two int, one it's the row and the other the first char index of the <tt>str</tt> occurrence
	 * 						<br><tt>null</tt> is returned if there's no occurrence of <tt>str</tt> in subtitle
	 */
    private int[] getRowAndCharIndex(String str, ArrayList<Subtitle> subtitleList, boolean backward) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            selectedRow = 0;
        }
        boolean isTextChanged = false;
        if (charIndexIterator == null || super.isViewportChanged) {
            isViewportChanged = false;
            charIndexIterator = new CharIndexIterator(subtitleList, str);
        } else {
            isTextChanged = charIndexIterator.setANewTextForTheSearch(str);
        }
        int rowAndCharIndex[] = null;
        if (lastSelectedRow == selectedRow && !isTextChanged) {
            if (!backward) {
                rowAndCharIndex = charIndexIterator.getNextRowAndCharIndex();
            } else {
                rowAndCharIndex = charIndexIterator.getPreviousRowAndCharIndex();
            }
        } else {
            if (!backward) {
                rowAndCharIndex = charIndexIterator.getNextRowAndCharIndexBeginAtRow(selectedRow);
            } else {
                rowAndCharIndex = charIndexIterator.getPreviousRowAndCharIndexBeginAtRow(selectedRow);
            }
        }
        lastSelectedRow = selectedRow;
        return rowAndCharIndex;
    }

    /**
	 * Tells <code>SubtitleTableCellRenderer</code> to highlight all accurences of visible string
	 * in <code>JTable</code> instance
	 * @param str		the string to highlight
	 * @param table		the <code>JTable</code> instance
	 */
    private void highlightSubString(String str, JTable table) {
        TableColumn tableColumn = table.getColumnModel().getColumn(SubtitleTableModel.SUBTITLE_COLUMN);
        TableCellRenderer renderer = tableColumn.getCellRenderer();
        if (renderer instanceof SubtitleTableCellRenderer) {
            ((SubtitleTableCellRenderer) renderer).highlightString(str);
        }
    }

    /**
	 * Alerts that the word is not found in subtitle
	 */
    public void fireWordNotFound() {
        cancel();
    }

    public void fireAdjustmentValueChanged(AdjustmentEvent e) {
        if (fireAdjustementValueChangedEnabled == true) {
            svgp.setVisible(false);
        }
    }
}
