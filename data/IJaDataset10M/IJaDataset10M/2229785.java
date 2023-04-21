package arcjutils.swing;

import java.util.List;
import java.util.Vector;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.RepaintManager;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import com.jgoodies.forms.layout.*;
import arcjutils.DbUtils;
import tablesort.*;

/**
 * --------------------------------------------------------------------------
 * type: arcjutils.swing.DbGrid.java<br>
 * date: 20.8.2006<br>
 * info: trida predstavujici databazovy grid<br>
 * @author Michal Rost
 * --------------------------------------------------------------------------
 */
public class DbGrid extends JPanel implements Printable {

    private static final long serialVersionUID = -6004067341699047704L;

    private static int DEFAULT_PRINT_SPACE_SIZE = 5;

    private static int DEFAULT_MAX_COLUMN_WIDTH = 50;

    private static int TABLE_TITLE_FONT_SIZE = 10;

    protected SortableTableModel tableModel;

    protected UnEditableSortableTable table;

    protected DefaultTableModel fixedTableModel;

    protected JTable fixedTable;

    protected List idents;

    protected JScrollPane scpFix;

    protected String tableTitle;

    protected boolean sumLineVisible;

    protected int maxColumnWidth;

    protected int tableRealWidth;

    /**
   * ========================================================================
   * DbGrid - konstruktor, inicializace
   * @param dataVector vektor vektoru (radku) tabulky
   * @param idents list id radku tabulky
   * @param colTitles popisky sloupcu tabulky
   * @throws Exception
   * ========================================================================
   */
    public DbGrid(Vector dataVector, List idents, String[] colTitles) throws Exception {
        super();
        init();
        setData(dataVector, idents, colTitles);
    }

    /**
   * ========================================================================
   * DbGrid - konstruktor, inicializace
   * @param rs vysledkova sada
   * @param beanType datovy typ beanu ve vysledkove sade
   * @param idProperty nazev polozky z beanu, obsahujici id
   * @param propertyNames nazev polozek z beanu, ktere budou nahrany do gridu
   * @param colTitles popisky sloupcu
   * @throws Exception
   * ========================================================================
   */
    public DbGrid(ResultSet rs, Class beanType, String idProperty, String[] propertyNames, String[] colTitles) throws Exception {
        this(DbUtils.getDataVectorFromResultSet(rs, beanType, propertyNames), DbUtils.getPropertyListFromBeanList(DbUtils.getBeanListFromResultSet(rs, beanType), "id"), colTitles);
    }

    /**
   * ========================================================================
   * DbGrid - konstruktor, inicializace
   * @param data objekt zapouzdrujici data pro grid
   * @throws Exception
   * ========================================================================
   */
    public DbGrid(DbGridData data) throws Exception {
        this(data.getDataVector(), data.getIdents(), data.getColTitles());
    }

    /**
   * ========================================================================
   * DbGrid - konstruktor, inicializace
   * @param beans list beanu
   * @param objColNames nazvy sloupcu, ktere se maji pouzit z beanu
   * @param idColName nazev sloupce, z nehoz budou ziskana id
   * @param colTitles popisky sloupcu tabulky
   * @throws Exception
   * ========================================================================
   */
    public DbGrid(List beans, String[] objColNames, String idColName, String[] colTitles) throws Exception {
        this(DbUtils.getDataVectorFromBeanList(beans, objColNames), DbUtils.getPropertyListFromBeanList(beans, idColName), colTitles);
    }

    /**
   * ========================================================================
   * setData - nastavi data pro tabulku
   * @param dataVector vektor vektoru (radku) tabulky
   * @param idents list id radku tabulky
   * @param colTitles popisky sloupcu tabulky
   * ========================================================================
   */
    public void setData(Vector dataVector, List idents, String[] colTitles) {
        initTableModel(dataVector, colTitles);
        removeAllRows();
        this.idents = idents;
        for (int i = 0; i < dataVector.size(); i++) tableModel.addRow((Vector) dataVector.get(i));
        fixedTableModel.addRow(calculateSum(dataVector));
        if (!dataVector.isEmpty()) {
            table.getSelectionModel().setSelectionInterval(0, 0);
        }
        initColumnSizes();
    }

    /**
   * ========================================================================
   * setData - nastavi data pro tabulku
   * @param data objekt zapouzdrujici data pro grid
   * ========================================================================
   */
    public void setData(DbGridData data) {
        setData(data.getDataVector(), data.getIdents(), data.getColTitles());
    }

    /**
   * ========================================================================
   * setData - nastavi data pro tabulku
   * @param beans list beanu
   * @param objColNames nazvy sloupcu, ktere se maji pouzit z beanu
   * @param idColName nazev sloupce, z nehoz budou ziskana id
   * @param colTitles popisky sloupcu tabulky
   * @throws Exception
   * ========================================================================
   */
    public void setData(List beans, String[] objColNames, String idColName, String[] colTitles) throws Exception {
        setData(DbUtils.getDataVectorFromBeanList(beans, objColNames), DbUtils.getPropertyListFromBeanList(beans, idColName), colTitles);
    }

    /**
   * ========================================================================
   * setColumnWidth - nastavi sirku sloupce
   * @param index
   * @param width
   * ========================================================================
   */
    public void setColumnWidth(int index, int width) {
        table.setColumnWidth(index, width);
    }

    /**
   * ========================================================================
   * setColumnWidths - nastavi sirky sloupcu
   * @param widths
   * ========================================================================
   */
    public void setColumnWidths(int[] widths) {
        table.setColumnWidths(widths);
    }

    /**
   * ========================================================================
   * getSelectedRow - ziska vybrany radek
   * @return vybrany radek
   * ========================================================================
   */
    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    /**
   * ========================================================================
   * getSelectedRowId - vrati id polozky vybraneho radku
   * @return id polozky vybraneho radku
   * ========================================================================
   */
    public Object getSelectedRowId() {
        return table.getSelectedRow() == -1 ? null : idents.get(table.getSelectedRow());
    }

    /**
   * ========================================================================
   * getSelectedColumn - ziska vybrany sloupec
   * @return vybrany sloupec
   * ========================================================================
   */
    public int getSelectedColumn() {
        return table.getSelectedColumn();
    }

    /**
   * ========================================================================
   * getTable - ziska tabulku
   * @return tabulka
   * ========================================================================
   */
    public SortableTable getTable() {
        return table;
    }

    /**
   * ========================================================================
   * getTableModel - ziska model
   * @return model
   * ========================================================================
   */
    public TableModel getTableModel() {
        return tableModel;
    }

    /**
   * ========================================================================
   * getFixedTableModel - ziska model fixovaneho radku
   * @return model
   * ========================================================================
   */
    public TableModel getFixedTableModel() {
        return fixedTableModel;
    }

    /**
   * ========================================================================
   * removeRow - odstrani radek
   * @param index
   * ========================================================================
   */
    public void removeRow(int index) {
        tableModel.removeRow(index);
        idents.remove(index);
    }

    /**
   * ========================================================================
   * removeAllRows - odstrani vsechny radky
   * ========================================================================
   */
    public void removeAllRows() {
        while (tableModel.getRowCount() > 0) {
            removeRow(0);
        }
        while (fixedTableModel.getRowCount() > 0) {
            fixedTableModel.removeRow(0);
        }
    }

    /**
   * ========================================================================
   * initColumnSizes - upravi sirky sloupcu podle jejich realnych sirek
   * ========================================================================
   */
    public void initColumnSizes() {
        tableRealWidth = 0;
        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
            int colWidth = getColumnRealWidth(i);
            table.getColumnModel().getColumn(i).setPreferredWidth(colWidth);
            tableRealWidth += colWidth;
        }
    }

    /**
   * ========================================================================
   * getColumnRealWidth - ziska realnou sirku sloupce (zaplnenou)
   * @param col
   * @return skutecna sirka sloupce
   * ========================================================================
   */
    public int getColumnRealWidth(int col) {
        TableCellRenderer hr = table.getTableHeader().getDefaultRenderer();
        TableColumn tmpCol = table.getColumnModel().getColumn(col);
        int colWidth = hr.getTableCellRendererComponent(null, tmpCol.getHeaderValue(), false, false, 0, 0).getPreferredSize().width;
        for (int i = 0; i < table.getRowCount(); i++) {
            colWidth = Math.max(colWidth, getCellRealWidth(i, col));
        }
        return Math.min(colWidth + DEFAULT_PRINT_SPACE_SIZE, maxColumnWidth);
    }

    /**
   * ========================================================================
   * getCellRealWidth - ziska realnou sirku bunky (sirku zaplnenou informaci)
   * @param row
   * @param col
   * @return skutecna sirka bunky
   * ========================================================================
   */
    public int getCellRealWidth(int row, int col) {
        Component pCellComponent = table.getDefaultRenderer(table.getColumnClass(col)).getTableCellRendererComponent(table, table.getModel().getValueAt(row, col), false, false, row, col);
        return pCellComponent.getPreferredSize().width;
    }

    /**
   * ========================================================================
   * initTableModel - inicializace table model
   * @param dataVector
   * @param colTitles
   * ========================================================================
   */
    protected void initTableModel(Vector dataVector, String[] colTitles) {
        tableModel = new SortableTableModel();
        fixedTableModel = new DefaultTableModel();
        if (!dataVector.isEmpty()) {
            Vector tmp = (Vector) dataVector.get(0);
            Class[] colClass = new Class[tmp.size()];
            for (int i = 0; i < tmp.size(); i++) colClass[i] = tmp.get(i).getClass();
            tableModel.setColumnIdentifiers(colTitles);
            tableModel.setColumnClasses(colClass);
            fixedTableModel.setColumnIdentifiers(colTitles);
        }
        table.setTableModel(tableModel);
        fixedTable.setModel(fixedTableModel);
    }

    /**
   * ========================================================================
   * init - inicializace
   * ========================================================================
   */
    protected void init() {
        FormLayout tmpLayout = new FormLayout();
        tmpLayout.appendColumn(new ColumnSpec(ColumnSpec.FILL, Sizes.PREFERRED, ColumnSpec.DEFAULT_GROW));
        tmpLayout.appendRow(new RowSpec(RowSpec.FILL, Sizes.MINIMUM, RowSpec.DEFAULT_GROW));
        tmpLayout.appendRow(new RowSpec(RowSpec.FILL, Sizes.MINIMUM, RowSpec.NO_GROW));
        CellConstraints cc = new CellConstraints();
        table = new UnEditableSortableTable();
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        fixedTable = new JTable(fixedTableModel);
        fixedTable.setShowVerticalLines(true);
        fixedTable.setShowHorizontalLines(true);
        fixedTable.setEnabled(false);
        sumLineVisible = true;
        maxColumnWidth = DEFAULT_MAX_COLUMN_WIDTH;
        tableTitle = "";
        JScrollPane scpTmp = new JScrollPane(table);
        scpFix = new HeaderlessScrollPane(fixedTable);
        scpTmp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scpFix.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scpTmp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scpFix.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scpFix.setMinimumSize(new java.awt.Dimension(0, fixedTable.getRowHeight()));
        JScrollBar bar = new InvisibleScrollBar();
        bar.setPreferredSize(scpFix.getVerticalScrollBar().getPreferredSize());
        scpFix.setVerticalScrollBar(bar);
        table.getColumnModel().addColumnModelListener(new ColumnModelListener());
        this.setLayout(tmpLayout);
        this.add(scpTmp, cc.xy(1, 1));
        this.add(scpFix, cc.xy(1, 2));
    }

    /**
   * ========================================================================
   * calculateSum - spocita sumy ciselnych sloupcu data vektoru
   * @param dataVector
   * @return vektor obsahujici sumy z ciselnych sloupcu data vektoru
   * ========================================================================
   */
    protected Vector calculateSum(Vector dataVector) {
        Vector<Object> sumV = new Vector<Object>(dataVector.size());
        for (int i = 0; i < dataVector.size(); i++) {
            Vector tmpV = (Vector) dataVector.get(i);
            for (int j = 0; j < tmpV.size(); j++) {
                if (i == 0) {
                    if (tmpV.get(j) instanceof Number) sumV.add(new BigDecimal(((Number) tmpV.get(j)).doubleValue())); else sumV.add(new String(""));
                } else {
                    if (tmpV.get(j) instanceof Number) {
                        double value = ((Number) tmpV.get(j)).doubleValue();
                        BigDecimal bdValue = BigDecimal.valueOf(value);
                        sumV.set(j, ((BigDecimal) sumV.get(j)).add(bdValue));
                    }
                }
            }
        }
        return sumV;
    }

    /**
   * ========================================================================
   * isSumLineVisible - vrati stav zobrazeni sumacniho radku
   * @return true pokud je zobrazovan sumacni radek
   * ========================================================================
   */
    public boolean isSumLineVisible() {
        return sumLineVisible;
    }

    /**
   * ========================================================================
   * setSumLineVisible - nastavi zobrazovani sumacniho radku
   * @param sumLineVisible
   * ========================================================================
   */
    public void setSumLineVisible(boolean sumLineVisible) {
        if (this.sumLineVisible && !sumLineVisible) {
            this.remove(scpFix);
        }
        if (!this.sumLineVisible && sumLineVisible) {
            this.add(scpFix);
        }
        this.sumLineVisible = sumLineVisible;
    }

    /**
   * ========================================================================
   * getTableTitle - ziska titulek tabulky
   * @return titulek
   * ========================================================================
   */
    public String getTableTitle() {
        return tableTitle;
    }

    /**
   * ========================================================================
   * setTableTitle - nastavi titulek tabulky (pro tisk)
   * @param tableTitle
   * ========================================================================
   */
    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
    }

    /**
   * ========================================================================
   * getMaxColumnWidth - vrati maximalni zobrazovanou sirku sloupcu
   * @return maximalni sirka sloupcu
   * ========================================================================
   */
    public int getMaxColumnWidth() {
        return maxColumnWidth;
    }

    /**
   * ========================================================================
   * setMaxColumnWidth - nastavi maximalni zobrazovanou sirku sloupcu
   * @param maxColumnWidth
   * ========================================================================
   */
    public void setMaxColumnWidth(int maxColumnWidth) {
        this.maxColumnWidth = maxColumnWidth;
    }

    /**
   * ========================================================================
   * setMetalLookCellRenderer - nastavi metal look cell renderer
   * ========================================================================
   */
    public void setMetalLookCellRenderer() {
        MetalLookCellRenderer r = new MetalLookCellRenderer();
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            table.setDefaultRenderer(tableModel.getColumnClass(i), r);
        }
    }

    /**
   * ========================================================================
   * setDefaultRenderer - nastavi cell renderer pro bunky typu c
   * @param c
   * @param r
   * ========================================================================
   */
    public void setDefaultRenderer(Class c, DefaultTableCellRenderer r) {
        table.setDefaultRenderer(c, r);
    }

    /**
   * ========================================================================
   * getDefaultRenderer - vrati cell renderer bunek typu c
   * @param c
   * @return renderer
   * ========================================================================
   */
    public TableCellRenderer getDefaultRenderer(Class c) {
        return table.getDefaultRenderer(c);
    }

    /**
   * ========================================================================
   * print - vytiskne grid
   * @param graphics
   * @param pageFormat
   * @param pageIndex
   * @return stav
   * ========================================================================
   */
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        final Font tableTitleFont = new Font("Dialog", 1, TABLE_TITLE_FONT_SIZE);
        final Font colTitleFont = new Font("Dialog", Font.BOLD, 7);
        final Font cellFont = new Font("Dialog", 0, 6);
        Graphics2D g = (Graphics2D) graphics;
        RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);
        g.setColor(Color.black);
        int pageHeight = (int) pageFormat.getImageableHeight();
        int pageWidth = (int) pageFormat.getImageableWidth();
        int tableWidth = tableRealWidth;
        double scale = (double) pageWidth / (double) tableWidth;
        int rowHeight = table.getRowHeight();
        int titleHeight = TABLE_TITLE_FONT_SIZE;
        int rowsPerPage = (int) ((pageHeight - titleHeight * scale) / Math.ceil(rowHeight * scale) - 1);
        int numOfRows = sumLineVisible ? table.getRowCount() + 2 : table.getRowCount();
        int numOfPages = ((numOfRows - 1) / rowsPerPage) + 1;
        if (pageIndex >= numOfPages) return NO_SUCH_PAGE;
        g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g.scale(scale, scale);
        g.setFont(tableTitleFont);
        g.drawString(tableTitle, 0, 0);
        g.drawLine(0, 0, tableWidth, 0);
        int tableHeight = rowHeight * rowsPerPage;
        g.translate(0f, titleHeight);
        g.translate(0f, -pageIndex * tableHeight);
        g.setClip(0, pageIndex * tableHeight, tableWidth, tableHeight);
        int thisPageRows = Math.min(table.getRowCount() - (pageIndex * rowsPerPage), rowsPerPage);
        TableColumnModel colModel = table.getColumnModel();
        int numOfCols = table.getColumnCount();
        g.setFont(colTitleFont);
        FontMetrics fm = g.getFontMetrics();
        int y = fm.getAscent();
        int x[] = new int[numOfCols];
        x[0] = 0;
        for (int i = 0; i < numOfCols; i++) {
            TableColumn tk = colModel.getColumn(i);
            int width = getColumnRealWidth(i);
            if (i + 1 < numOfCols) x[i + 1] = x[i] + width;
            String title = (String) tk.getIdentifier();
            g.drawString(title, x[i], y);
        }
        g.setFont(cellFont);
        fm = graphics.getFontMetrics();
        int h = fm.getHeight();
        int iniRow = pageIndex * thisPageRows;
        int endRow = Math.min(table.getRowCount(), iniRow + thisPageRows);
        y = printTableRows(g, table, tableModel, fm, iniRow, endRow, x, y);
        if (sumLineVisible && pageIndex == numOfPages - 1) {
            y += h;
            g.drawLine(0, y, tableWidth, y);
            y = printTableRows(g, fixedTable, fixedTableModel, fm, 0, fixedTable.getRowCount(), x, y);
        }
        RepaintManager.currentManager(this).setDoubleBufferingEnabled(true);
        return Printable.PAGE_EXISTS;
    }

    /**
   * ========================================================================
   * printTableRows - vytiskne radky zvolene JTable
   * @param g
   * @param tab
   * @param model
   * @param fm
   * @param iniRow
   * @param endRow
   * @param x
   * @param y
   * @return y
   * ========================================================================
   */
    protected int printTableRows(Graphics2D g, JTable tab, DefaultTableModel model, FontMetrics fm, int iniRow, int endRow, int[] x, int y) {
        for (int i = iniRow; i < endRow; i++) {
            y += fm.getHeight();
            for (int j = 0; j < table.getColumnCount(); j++) {
                int tmpCol = tab.getColumnModel().getColumn(j).getModelIndex();
                Object obj = model.getValueAt(i, tmpCol);
                String str = obj.toString();
                while (SwingUtilities.computeStringWidth(fm, str) >= maxColumnWidth) {
                    str = str.substring(0, str.length() - 1);
                }
                str += obj.toString().length() > str.length() ? "..." : "";
                g.drawString(str, x[j], y);
            }
        }
        return y;
    }

    protected class ColumnModelListener implements TableColumnModelListener {

        public void columnMarginChanged(ChangeEvent e) {
            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                TableColumn col1 = table.getColumnModel().getColumn(i);
                TableColumn col2 = fixedTable.getColumnModel().getColumn(i);
                col2.setPreferredWidth(col1.getWidth());
            }
        }

        public void columnSelectionChanged(ListSelectionEvent e) {
            fixedTable.getColumnModel().getSelectionModel().setSelectionInterval(e.getFirstIndex(), e.getLastIndex());
        }

        public void columnAdded(TableColumnModelEvent e) {
        }

        public void columnMoved(TableColumnModelEvent e) {
            fixedTable.getColumnModel().moveColumn(e.getFromIndex(), e.getToIndex());
        }

        public void columnRemoved(TableColumnModelEvent e) {
        }
    }
}
