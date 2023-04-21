package net.grade.averagegrade.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Locale;
import javax.swing.GroupLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import net.grade.averagegrade.event.LocaleChangeListener;
import net.grade.averagegrade.parse.CourseEntriesParser;
import net.grade.averagegrade.parse.CourseEntry;
import net.grade.averagegrade.parse.university.LinkopingUniversity;
import net.grade.averagegrade.parse.university.University;
import net.grade.averagegrade.resource.Messages;
import net.grade.averagegrade.swing.editor.DateEditor;
import net.grade.averagegrade.swing.editor.DoubleEditor;
import net.grade.averagegrade.swing.editor.IntegerEditor;
import net.grade.averagegrade.swing.table.CoursesTableModel;
import net.grade.averagegrade.swing.table.DateTableCellRenderer;
import net.grade.averagegrade.swing.table.DecimalTableCellRenderer;
import net.grade.averagegrade.util.Utilities;

/**
 * AverageGrade is the main frame of the AverageGrade application,
 * which also contains much of the required code for operation.
 */
public class AverageGrade extends JFrame {

    /** A list containing an instance of every available <code>CourseEntriesParser</code>. */
    private final CourseEntriesParser[] courseEntriesParsers = new CourseEntriesParser[] { new LinkopingUniversity() };

    /** The <code>JTable</code> table used for displaying/editing imported courses. */
    private JTable coursesTable;

    /** The table model used for the <code>coursesTable</code> table. */
    private CoursesTableModel coursesTableModel;

    /** The size of the margin on table columns or zero if not yet set. */
    private int tableMargin;

    /**
     * Saved averages, used for comparison calculations when the user decides to
     * change values in the <code>coursesTable</code> table. The array is divided so:
     * <blockquote>
     * [0] = weighted average grade; [1] = unweighted average grade
     * </blockquote>
     */
    private double[] compareAverages = new double[2];

    /**
     * Temporarily disables the next automatic reading of the clipboard when the
     * window gets activated. The value will reset to <tt>true</tt> on the next
     * window activation.
     */
    private boolean isWindowActivationEnabled = true;

    /**
     * The short name of the university for which courses are loaded. If
     * <code>coursesTable.isEmpty</code> is set, the value will be
     * <code>null</code>.
     */
    private String universityShortName = null;

    /**
     * Creates a new instance of <code>AverageGrade</code>, using the
     * default look and feel of the system.
     */
    public AverageGrade() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception ignored) {
        }
        initComponents();
    }

    /**
     * Handles initialization of the components in the frame. This method
     * should only be called once, preferably within the frames constructor.
     */
    private void initComponents() {
        final JScrollPane coursesScrollPane = new JScrollPane();
        final JMenuBar mainMenuBar = new JMenuBar();
        final JMenu fileMenu = new JMenu();
        final JMenuItem exitMenuItem = new JMenuItem();
        final JMenu editMenu = new JMenu();
        final JMenuItem addMenuItem = new JMenuItem();
        final JMenuItem deleteMenuItem = new JMenuItem();
        final JMenuItem pasteMenuItem = new JMenuItem();
        final JMenuItem clearMenuItem = new JMenuItem();
        final JMenu languageMenu = new JMenu();
        final JMenu helpMenu = new JMenu();
        final JMenuItem aboutMenuItem = new JMenuItem();
        setTitle(Messages.get("Name"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowActivated(final WindowEvent evt) {
                if (isWindowActivationEnabled && coursesTableModel.hasNormalRows()) pasteMenuItem.doClick(); else isWindowActivationEnabled = true;
            }
        });
        Messages.addLocaleChangeListener(new LocaleChangeListener() {

            public void onLocaleChanged(final Locale locale) {
                updateTitle();
            }
        });
        coursesTable = new JTable();
        coursesTable.setAutoCreateRowSorter(true);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesTable.setModel(coursesTableModel = new CoursesTableModel());
        coursesTable.addKeyListener(new KeyListener() {

            public void keyPressed(final KeyEvent e) {
                if (e.getModifiers() == KeyEvent.CTRL_MASK) {
                    if (e.getKeyCode() == KeyEvent.VK_V) pasteMenuItem.doClick(); else if (e.getKeyCode() == KeyEvent.VK_A) addMenuItem.doClick(); else return;
                    e.consume();
                } else if (e.getModifiers() == 0) {
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        final int selectedRow = coursesTable.getSelectedRow();
                        if (selectedRow == -1) deleteMenuItem.setEnabled(false); else {
                            deleteMenuItem.setEnabled(coursesTableModel.isEditableRow(coursesTable.convertRowIndexToModel(selectedRow)));
                            deleteMenuItem.doClick();
                        }
                        e.consume();
                    }
                }
            }

            public void keyTyped(final KeyEvent e) {
            }

            public void keyReleased(final KeyEvent e) {
            }
        });
        coursesTableModel.addTableModelListener(new TableModelListener() {

            public void tableChanged(final TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    if (coursesTableModel.hasNormalRows()) coursesTableModel.setModified(true);
                    if (e.getColumn() == 1) resizeTable();
                    updateTitle();
                }
            }
        });
        Messages.addLocaleChangeListener(new LocaleChangeListener() {

            private void updateColumn(final int index, final String text) {
                coursesTable.getColumnModel().getColumn(index).setHeaderValue(text);
            }

            public void onLocaleChanged(final Locale locale) {
                updateColumn(1, Messages.get("CourseCode"));
                updateColumn(2, Messages.get("CourseName"));
                updateColumn(3, Messages.get("Points"));
                updateColumn(4, Messages.get("Grade"));
                updateColumn(5, Messages.get("Date"));
                coursesTable.repaint();
                resizeTable();
            }
        });
        coursesTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(final JTable table, Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                if (value != null && value instanceof String && coursesTableModel.hasNormalRows() && coursesTableModel.isEditableRow(coursesTable.convertRowIndexToModel(row))) {
                    value = "<html><b>" + value.toString() + "</b></html>";
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        coursesTable.getColumnModel().getColumn(3).setCellRenderer(new DecimalTableCellRenderer());
        coursesTable.getColumnModel().getColumn(3).setCellEditor(new DoubleEditor(0.5, 99));
        final DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        coursesTable.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        coursesTable.getColumnModel().getColumn(4).setCellEditor(new IntegerEditor(3, 5));
        coursesTable.getColumnModel().getColumn(5).setCellRenderer(new DateTableCellRenderer());
        coursesTable.getColumnModel().getColumn(5).setCellEditor(new DateEditor(Utilities.getDate(1990, 0, 0), Utilities.getToday()));
        for (int i = 0; i < coursesTable.getColumnCount(); i++) coursesTable.getColumnModel().getColumn(i).setResizable(false);
        coursesTable.getTableHeader().setReorderingAllowed(false);
        coursesScrollPane.setViewportView(coursesTable);
        coursesTable.getRowSorter().toggleSortOrder(1);
        coursesTable.getColumnModel().getColumn(0).setMaxWidth(20);
        coursesTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        resizeTable();
        fileMenu.setName("File");
        fileMenu.setText(Messages.get(fileMenu.getName()));
        exitMenuItem.setName("Exit");
        exitMenuItem.setText(Messages.get(exitMenuItem.getName()));
        exitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                processWindowEvent(new WindowEvent(AverageGrade.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        fileMenu.add(exitMenuItem);
        mainMenuBar.add(fileMenu);
        editMenu.setName("Edit");
        editMenu.setText(Messages.get(editMenu.getName()));
        addMenuItem.setName("Add");
        addMenuItem.setText(Messages.get(addMenuItem.getName()));
        addMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        addMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (coursesTableModel.isEmpty()) coursesTableModel.setRowCount(0);
                coursesTableModel.addEditableRow(new Object[] { true, "", "", 1.0D, 3, Utilities.getToday() });
                coursesTableModel.setEmpty(false);
                if (coursesTableModel.hasNormalRows()) coursesTableModel.setModified(true);
                resizeTable();
                updateTitle();
            }
        });
        editMenu.add(addMenuItem);
        deleteMenuItem.setName("Delete");
        deleteMenuItem.setText(Messages.get(deleteMenuItem.getName()));
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                coursesTableModel.removeRow(coursesTable.convertRowIndexToModel(coursesTable.getSelectedRow()));
                if (coursesTableModel.getRowCount() == 0) clearTable(); else updateTitle();
            }
        });
        editMenu.add(deleteMenuItem);
        editMenu.addSeparator();
        pasteMenuItem.setName("Paste");
        pasteMenuItem.setText(Messages.get(pasteMenuItem.getName()));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        pasteMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (coursesTableModel.hasEditableRows()) {
                    if (!coursesTableModel.hasNormalRows()) parseClipboard();
                } else if (coursesTableModel.isEmpty()) parseClipboard();
            }
        });
        editMenu.add(pasteMenuItem);
        clearMenuItem.setName("Clear");
        clearMenuItem.setText(Messages.get(clearMenuItem.getName()));
        clearMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                clearTable();
            }
        });
        editMenu.add(clearMenuItem);
        editMenu.addMenuListener(new MenuListener() {

            public void menuSelected(final MenuEvent e) {
                clearMenuItem.setEnabled(!coursesTableModel.isEmpty());
                final int selectedRow = coursesTable.getSelectedRow();
                if (selectedRow == -1) deleteMenuItem.setEnabled(false); else deleteMenuItem.setEnabled(coursesTableModel.isEditableRow(coursesTable.convertRowIndexToModel(selectedRow)));
            }

            public void menuDeselected(final MenuEvent e) {
            }

            public void menuCanceled(final MenuEvent e) {
            }
        });
        mainMenuBar.add(editMenu);
        languageMenu.setName("Language");
        languageMenu.setText(Messages.get(languageMenu.getName()));
        final LocaleChangeListener languageMenuListener = new LocaleChangeListener() {

            public void onLocaleChanged(final Locale locale) {
                languageMenu.removeAll();
                boolean isItemSelected = false;
                JCheckBoxMenuItem englishMenuItem = null;
                for (final Locale currentLocale : new Locale[] { Locale.ENGLISH, new Locale("sv") }) {
                    final JCheckBoxMenuItem localeMenuItem = new JCheckBoxMenuItem();
                    final StringBuilder sb = new StringBuilder(currentLocale.getDisplayName(Messages.getLocale()).toLowerCase(Messages.getLocale()));
                    sb.replace(0, 1, sb.substring(0, 1).toUpperCase(Messages.getLocale()));
                    localeMenuItem.setText(sb.toString());
                    if (currentLocale.getLanguage().equals(Messages.getLocale().getLanguage())) localeMenuItem.setSelected(isItemSelected = true);
                    localeMenuItem.addActionListener(new ActionListener() {

                        public void actionPerformed(final ActionEvent e) {
                            if (!localeMenuItem.isSelected()) {
                                localeMenuItem.setSelected(true);
                                return;
                            }
                            Messages.setLocale(currentLocale);
                        }
                    });
                    languageMenu.add(localeMenuItem);
                    if (currentLocale.equals(Locale.ENGLISH)) englishMenuItem = localeMenuItem;
                }
                if (!isItemSelected && englishMenuItem != null) englishMenuItem.setSelected(true);
            }
        };
        languageMenuListener.onLocaleChanged(Messages.getLocale());
        Messages.addLocaleChangeListener(languageMenuListener);
        mainMenuBar.add(languageMenu);
        helpMenu.setName("Help");
        helpMenu.setText(Messages.get(helpMenu.getName()));
        aboutMenuItem.setName("About");
        aboutMenuItem.setText(Messages.get(aboutMenuItem.getName()));
        aboutMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                isWindowActivationEnabled = false;
                JOptionPane.showMessageDialog(AverageGrade.this, Messages.get("Name") + " " + Messages.get("AboutText") + "http://code.google.com/p/average-grade/", Messages.get("About") + ": " + Messages.get("Name"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutMenuItem);
        mainMenuBar.add(helpMenu);
        setJMenuBar(mainMenuBar);
        Messages.addLocaleChangeListener(new LocaleChangeListener() {

            public void onLocaleChanged(final Locale locale) {
                fileMenu.setText(Messages.get(fileMenu.getName()));
                exitMenuItem.setText(Messages.get(exitMenuItem.getName()));
                editMenu.setText(Messages.get(editMenu.getName()));
                addMenuItem.setText(Messages.get(addMenuItem.getName()));
                deleteMenuItem.setText(Messages.get(deleteMenuItem.getName()));
                pasteMenuItem.setText(Messages.get(pasteMenuItem.getName()));
                clearMenuItem.setText(Messages.get(clearMenuItem.getName()));
                languageMenu.setText(Messages.get(languageMenu.getName()));
                helpMenu.setText(Messages.get(helpMenu.getName()));
                aboutMenuItem.setText(Messages.get(aboutMenuItem.getName()));
            }
        });
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(coursesScrollPane, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(coursesScrollPane, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    /**
     * Scans the clipboard for any entries and automatically loads them
     * (if any) to the <code>coursesTable</code> table.
     */
    private void parseClipboard() {
        final String clipboardContents = Utilities.getClipboardContents();
        if (clipboardContents != null) {
            for (final CourseEntriesParser courseEntriesParser : courseEntriesParsers) {
                if (courseEntriesParser.containsEntries(clipboardContents)) {
                    loadEntries(courseEntriesParser, clipboardContents);
                    break;
                }
            }
        }
    }

    /**
     * Loads all found entries from the specified text to the <code>coursesTable</code>.
     * The specified text should be pre-verified to contain entries.
     *
     * @param parser The parser which have found entries in the specified text.
     * @param text   The text from where entries should be loaded into the table.
     */
    private void loadEntries(final CourseEntriesParser parser, final String text) {
        final CourseEntry[] courseEntries = parser.extractEntries(text);
        if (courseEntries.length == 0) return;
        if (coursesTableModel.isEmpty()) coursesTableModel.setRowCount(0);
        for (final CourseEntry courseEntry : courseEntries) {
            coursesTableModel.addRow(new Object[] { true, courseEntry.getCode(), courseEntry.getName(), courseEntry.getPoints(), courseEntry.getGrade().getValue(), courseEntry.getDate() });
        }
        universityShortName = ((University) parser).getShortName();
        compareAverages = calculateAverageGrades(false);
        coursesTableModel.setEmpty(false);
        if (coursesTableModel.hasEditableRows()) coursesTableModel.setModified(true);
        resizeTable();
        updateTitle();
    }

    /**
     * Calculates the weighted and unweighted average grade of all selected entries
     * in the <code>coursesTable</code> table. If no entries exists or are selected,
     * zeros will be returned instead. The returned value is a <code>double</code>
     * array with a length of 2, so that:
     * <blockquote>
     * [0] = weighted average grade; [1] = unweighted average grade
     * </blockquote>
     * The only parameter specifies whether or not to include rows that are marked as
     * editable. Setting this to <tt>true</tt> means to include any editable rows.
     *
     * @param isIncludingEditable If set to <tt>true</tt>, editable rows will be taken
     *                            into account when calculating the average grades.
     * @return The weighted and unweighted average grade of the selected entries in
     *         the <code>coursesTable</code> table, as a <code>double</code> array.
     */
    private double[] calculateAverageGrades(final boolean isIncludingEditable) {
        double weightedGrades = 0, sumPoints = 0, unweightedGrades = 0, gradeCount = 0;
        for (int i = 0; i < coursesTableModel.getRowCount(); i++) {
            if (isIncludingEditable || !coursesTableModel.isEditableRow(i)) {
                if (Boolean.valueOf(coursesTableModel.getValueAt(i, 0).toString())) {
                    final double coursePoints = Double.parseDouble(coursesTableModel.getValueAt(i, 3).toString()), gradePoints = Double.parseDouble(coursesTableModel.getValueAt(i, 4).toString());
                    weightedGrades += coursePoints * gradePoints;
                    sumPoints += coursePoints;
                    unweightedGrades += gradePoints;
                    gradeCount++;
                }
            }
        }
        return new double[] { (sumPoints == 0) ? 0 : weightedGrades / sumPoints, (gradeCount == 0) ? 0 : unweightedGrades / gradeCount };
    }

    /**
     * Updates the title of the window. If the <code>coursesTable</code> table model is marked
     * as non-empty (i.e. <code>isEmpty</code> evaluates to <tt>false</tt>), the average grade will
     * be included in the window title. If <code>isModified</code> is set to <tt>true</tt>,
     * the difference between the actual average and the new average will also be included.
     */
    private void updateTitle() {
        final StringBuilder title = new StringBuilder(Messages.get("Name"));
        if (!coursesTableModel.isEmpty()) {
            if (coursesTableModel.hasNormalRows()) title.append(" | ").append(universityShortName);
            final double[] averages = calculateAverageGrades(true);
            title.append(" / ").append(Messages.get("Average")).append(": ").append(Utilities.round(averages[0], 2)).append(" (").append(Utilities.round(averages[1], 2)).append(")");
            if (coursesTableModel.isModified()) {
                final double differenceWeighted = Utilities.round(averages[0] - compareAverages[0], 2), differenceUnweighted = Utilities.round(averages[1] - compareAverages[1], 2);
                title.append(" / ").append(Messages.get("Difference")).append(": ").append((differenceWeighted > 0) ? "+" : "").append(differenceWeighted);
                title.append(" (").append((differenceUnweighted > 0) ? "+" : "").append(differenceUnweighted).append(")");
            }
        }
        setTitle(title.toString());
    }

    /**
     * Clears all entries in the <code>coursesTable</code> table. Also updates
     * the title of the window and resets related properties.
     */
    private void clearTable() {
        compareAverages[0] = compareAverages[1] = 0;
        coursesTableModel.setRowCount(0);
        coursesTableModel.setRowCount(1);
        universityShortName = null;
        coursesTableModel.setEmpty(true);
        coursesTableModel.setModified(false);
        updateTitle();
    }

    /**
     * Gets the margin to use for table column widths, depending on the
     * operating system the application is running on. Also sets the
     * <code>tableMargin</code> property if not already set. If it has
     * been set, <code>tableMargin</code> is returned instead.
     *
     * @return The margin to use for table column widths.
     */
    private int getTableMargin() {
        if (tableMargin > 0) return tableMargin;
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux")) return (tableMargin = 20); else if (os.contains("mac")) return (tableMargin = 25);
        return (tableMargin = 10);
    }

    /**
     * Resizes the width of the columns in <code>coursesTable</code> table to fit
     * the respective headers and content. Does not modify the "Name" column, since
     * it should have the remaining width, nor the "Selected" column.
     */
    private void resizeTable() {
        final int margin = getTableMargin();
        final DefaultTableColumnModel columnModel = (DefaultTableColumnModel) coursesTable.getColumnModel();
        for (int c = 1; c < coursesTable.getColumnCount(); c++) {
            if (c == 2) continue;
            final TableColumn column = columnModel.getColumn(c);
            TableCellRenderer renderer = column.getHeaderRenderer();
            if (renderer == null) renderer = coursesTable.getTableHeader().getDefaultRenderer();
            Component component = renderer.getTableCellRendererComponent(coursesTable, column.getHeaderValue(), false, false, 0, 0);
            int width = component.getPreferredSize().width;
            for (int r = 0; r < coursesTable.getRowCount(); r++) {
                renderer = coursesTable.getCellRenderer(r, c);
                component = renderer.getTableCellRendererComponent(coursesTable, coursesTable.getValueAt(r, c), false, false, r, c);
                width = Math.max(width, component.getPreferredSize().width);
            }
            width += margin;
            column.setMaxWidth(width);
            column.setPreferredWidth(width);
        }
    }
}
