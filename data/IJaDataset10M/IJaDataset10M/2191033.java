package ArianneViewer;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.borland.dbswing.*;
import com.borland.dx.dataset.*;
import com.borland.dx.sql.dataset.*;
import com.borland.jbcl.layout.*;
import javolution.util.FastTable;
import javolution.util.FastComparator;
import java.util.logging.Level;
import ArianneUtil.LogHandler;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * <p>Title: Guide Viewer</p>
 *
 * <p>Description: Visualizzatore per pagine create con Arianne Editor</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Itaco S.r.l.</p>
 *
 * @author Andrea Annibali
 * @version 1.0
 */
class TabularShape extends ViewerShapes implements Fillable {

    Color xorColor = new Color(100, 100, 100);

    private Color textColor = new Color(ArianneUtil.Shapes.NO_COLOR);

    private Color selectedTextColor = Color.red;

    private Color selectedBackColor = Color.red;

    private Color focusTextColor = Color.red;

    private Color focusBackColor = Color.red;

    private int columnWidth = 10;

    private int rowHeight = 20;

    private int rowMargin = 10;

    private int interCellspW = 0;

    private int interCellspH = 0;

    private int borderWidth = -1;

    private int selRow = -1;

    private int selCol = -1;

    private String oddBckTabRowRule = null;

    private String oddFgTabRowRule = null;

    private String evenBckTabRowRule = null;

    private String evenFgTabRowRule = null;

    private int fontSize = 11;

    private Font actFont = new java.awt.Font("Courier New", 0, fontSize);

    private int maxFracDigit = -1;

    private int minFracDigit = -1;

    private Color[] backColColors;

    private Color[] fgColColors;

    private int[] colAlignment;

    private Color gridColor;

    private int mousePosX = -1;

    private int mousePosY = -1;

    private int autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;

    private long elapsed = 0;

    private boolean displayHeader = false, displayRowNum = false;

    private boolean showVerticalGrid = true;

    private boolean showHorizontalGrid = true;

    private boolean refreshEnabled = true;

    private boolean refreshing = false;

    JMenuItem menuItemSysCall;

    FastTable newValues = new FastTable();

    private volatile Thread updaterThread = null;

    private static final int THREAD_SLEEP = 1500;

    private FastTable oldValues = new FastTable();

    private static final int LOAD_OPTION = Load.ASYNCHRONOUS;

    QueryDataSet tabDataSet = new QueryDataSet();

    TableScrollPane tScrollPane = new TableScrollPane();

    JdbTable t = new JdbTable();

    XYLayout xYLayout = new XYLayout();

    double xp_act;

    double yp_act;

    private double oldZoomFactor = 100;

    private static transient FastComparator compare = FastComparator.DEFAULT;

    private Color shapeFillColor = null;

    TabularShape(int elId, Point ePoint, Point sPoint, Color sbc, Color gc, int cW, int rH, int rM, int ispw, int isph, int bW, String obr, String ebr, String ofr, String efr, boolean dh, boolean dr, boolean svg, boolean shg, java.awt.Font f, DrawingPanel p, int ovl, String imgName, boolean poll, int pollMsec, int bckMsec) {
        super("Tabular", 4, elId, p, imgName, ovl, sbc, ePoint, sPoint, poll, pollMsec, bckMsec);
        this.columnWidth = cW;
        this.setRowHeight(rH);
        this.setRowMargin(rM);
        this.setISPW(ispw);
        this.setISPH(isph);
        this.setBorderWidth(bW);
        this.setOddBckTabRule(obr);
        this.setEvenBckTabRule(ebr);
        this.setOddFgTabRule(ofr);
        this.setEvenFgTabRule(efr);
        this.displayHeader = dh;
        this.displayRowNum = dr;
        this.setFont(f);
        this.setVerticalGrid(svg);
        this.setHorizontalGrid(shg);
        setGridColor(gc);
        initTable();
    }

    TabularShape(int elId, double xPnt[], double yPnt[], int sColor, int gColor, int fColor, int tColor, int stColor, int sbColor, int ftColor, int fbColor, int cW, int rH, int rM, int ispw, int isph, int bW, String obr, String ebr, String ofr, String efr, Color[] bcc, Color[] fcc, int[] alignment_, boolean dh, boolean dr, boolean svg, boolean shg, java.awt.Font f, int maxfd, int minfd, String imgN, DrawingPanel p, int arm, int ovl, boolean poll, int pollMsec, int bckMsec) {
        super("Tabular", 4, elId, p, imgN, ovl, new Color(sColor), xPnt, yPnt, poll, pollMsec, bckMsec);
        this.columnWidth = cW;
        this.setRowHeight(rH);
        this.setRowMargin(rM);
        this.setISPW(ispw);
        this.setISPH(isph);
        this.setBorderWidth(bW);
        this.setOddBckTabRule(obr);
        this.setEvenBckTabRule(ebr);
        this.setOddFgTabRule(ofr);
        this.setEvenFgTabRule(efr);
        this.displayHeader = dh;
        this.displayRowNum = dr;
        this.setVerticalGrid(svg);
        this.setHorizontalGrid(shg);
        this.autoResizeMode = arm;
        this.setMaxFracDigit(maxfd);
        this.setMinFracDigit(minfd);
        setGridColor(new Color(gColor));
        setTextColor(new Color(tColor));
        setSelectedTextColor(new Color(stColor));
        setSelectedBackColor(new Color(sbColor));
        setFocusTextColor(new Color(ftColor));
        setFocusBackColor(new Color(fbColor));
        if (fColor == ArianneUtil.Shapes.NO_COLOR) setShapeFillColor(null); else setShapeFillColor(new Color(fColor));
        this.setFont(f);
        this.setBackColColors(bcc);
        this.setFgColColors(fcc);
        this.setColAlignment(alignment_);
        initTable();
    }

    public boolean getDisplayRowNum() {
        return this.displayRowNum;
    }

    public void setDisplayRowNum(boolean dr) {
        this.displayRowNum = dr;
        this.getTable().setRowHeaderVisible(displayRowNum);
        getTable().validate();
    }

    public Color getShapeFillColor() {
        return shapeFillColor;
    }

    public int getShapeFillColorRGB() {
        return shapeFillColor.getRGB();
    }

    public AlphaComposite makeComposite() {
        return null;
    }

    public double getCurVal() {
        return 0;
    }

    public void setTextColor(Color c) {
        textColor = c;
    }

    public void createUpdaterThread(final int msec) {
        if (updaterThread == null) updaterThread = new Thread() {

            public void run() {
                Thread thisThread = Thread.currentThread();
                while (updaterThread == thisThread) {
                    try {
                        Thread.sleep(msec);
                    } catch (InterruptedException e) {
                    }
                    updateVal();
                }
            }
        };
    }

    public void terminateUpdaterThread() {
        if (updaterThread != null) {
            Thread moribund = updaterThread;
            updaterThread = null;
            moribund.interrupt();
            moribund = null;
        }
    }

    public void setColAlignment(int[] a) {
        if (a == null) a = new int[1];
        colAlignment = new int[Math.max(getTableNumCols(), a.length)];
        for (int i = 0; i < a.length && i < colAlignment.length; i++) {
            if (a[i] == -1) colAlignment[i] = DefaultTableCellRenderer.LEFT; else colAlignment[i] = a[i];
        }
    }

    public void setBackColColors(Color[] c) {
        if (c == null) c = new Color[1];
        backColColors = new Color[Math.max(getTableNumCols(), c.length)];
        for (int i = 0; i < c.length && i < backColColors.length; i++) {
            if (c[i] == null) backColColors[i] = getShapeFillColor(); else backColColors[i] = c[i];
        }
    }

    public void setFgColColors(Color[] c) {
        if (c == null) c = new Color[1];
        fgColColors = new Color[Math.max(getTableNumCols(), c.length)];
        for (int i = 0; i < c.length && i < fgColColors.length; i++) {
            if (c[i] == null) fgColColors[i] = getTextColor(); else fgColColors[i] = c[i];
        }
    }

    public Color[] getBackColColors() {
        return backColColors;
    }

    public Color[] getFgColColors() {
        return fgColColors;
    }

    public int getISPW() {
        return this.interCellspW;
    }

    public int getISPH() {
        return this.interCellspH;
    }

    public void setISPW(int ispw) {
        if (ispw >= 0) {
            this.interCellspW = ispw;
        } else {
            JOptionPane.showMessageDialog(null, "Intercell spacing width value less than '0' is invalid. Setting to '0'.", "Invalid table intercell spacing", JOptionPane.ERROR_MESSAGE);
            this.interCellspW = 0;
        }
        this.getTable().setIntercellSpacing(new Dimension(interCellspW, getISPH()));
    }

    public void setISPH(int isph) {
        if (isph >= 0) {
            this.interCellspH = isph;
        } else {
            JOptionPane.showMessageDialog(null, "Intercell spacing height value less than '0' is invalid. Setting to '0'.", "Invalid table intercell spacing", JOptionPane.ERROR_MESSAGE);
            this.interCellspH = 0;
        }
        this.getTable().setIntercellSpacing(new Dimension(getISPW(), interCellspH));
    }

    public String getOddBckTabRule() {
        return oddBckTabRowRule;
    }

    public void setOddBckTabRule(String obr) {
        oddBckTabRowRule = obr;
    }

    public String getOddFgTabRule() {
        return oddFgTabRowRule;
    }

    public void setOddFgTabRule(String ofr) {
        oddFgTabRowRule = ofr;
    }

    public String getEvenBckTabRule() {
        return evenBckTabRowRule;
    }

    public void setEvenBckTabRule(String ebr) {
        evenBckTabRowRule = ebr;
    }

    public String getEvenFgTabRule() {
        return evenFgTabRowRule;
    }

    public void setEvenFgTabRule(String efr) {
        evenFgTabRowRule = efr;
    }

    public void setSelectedBackColor(Color c) {
        selectedBackColor = c;
    }

    public Color getSelectedBackColor() {
        return selectedBackColor;
    }

    public void setFocusBackColor(Color c) {
        focusBackColor = c;
    }

    public Color getFocusBackColor() {
        return focusBackColor;
    }

    public int getSelectedBackColorRGB() {
        return selectedBackColor.getRGB();
    }

    public int getFocusBackColorRGB() {
        return focusBackColor.getRGB();
    }

    public void setGridColor(Color gc) {
        gridColor = gc;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setBorderWidth(int w) {
        borderWidth = w;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setMaxFracDigit(int n) {
        maxFracDigit = n;
    }

    public int getMaxFracDigit() {
        return maxFracDigit;
    }

    public void setMinFracDigit(int n) {
        minFracDigit = n;
    }

    public int getMinFracDigit() {
        return minFracDigit;
    }

    public void setRenderer() {
        this.getTabContainer().setBorder(BorderFactory.createEmptyBorder());
        if (getTable().getColumnCount() > 0) {
            for (int i = 0; i < getTableNumCols(); i++) {
                getTable().setGridColor(getGridColor());
                getTable().setBorder(new LineBorder(getShapeBorderColor(), getBorderWidth()));
                getTable().getColumnModel().getColumn(i).setCellRenderer(new ArianneUtil.CustomTableCellRenderer(getColumnWidth(), getMaxFracDigit(), getMinFracDigit(), getTextColor(), getShapeFillColor(), getSelectedTextColor(), getSelectedBackColor(), getFocusTextColor(), getFocusBackColor(), backColColors, fgColColors, colAlignment, getOddBckTabRule(), getEvenBckTabRule(), getOddFgTabRule(), getEvenFgTabRule()));
            }
        }
    }

    public void setSelectedTextColor(Color c) {
        selectedTextColor = c;
    }

    public void setFocusTextColor(Color c) {
        focusTextColor = c;
    }

    public Color getSelectedTextColor() {
        return selectedTextColor;
    }

    public Color getFocusTextColor() {
        return focusTextColor;
    }

    public int getSelectedTextColorRGB() {
        return selectedTextColor.getRGB();
    }

    public int getFocusTextColorRGB() {
        return focusTextColor.getRGB();
    }

    public JdbTable getDbTable() {
        return t;
    }

    public double getOldZoomFactor() {
        return oldZoomFactor;
    }

    public void setOldZoomFactor(double v) {
        oldZoomFactor = v;
    }

    /**
   * Imposta la visualizzazione delle linee verticali della griglia
   * della tabella
   * @param vg se true visualizza le linee verticali
   */
    public void setVerticalGrid(boolean vg) {
        this.showVerticalGrid = vg;
        this.getDbTable().setShowVerticalLines(showVerticalGrid);
    }

    public boolean showVerticalGrid() {
        return showVerticalGrid;
    }

    public boolean showHorizontalGrid() {
        return showHorizontalGrid;
    }

    /**
   * Imposta la visualizzazione delle linee orizzontali della griglia
   * della tabella
   * @param vg se true visualizza le linee orizzontali
   */
    public void setHorizontalGrid(boolean vg) {
        this.showHorizontalGrid = vg;
        this.getDbTable().setShowHorizontalLines(showHorizontalGrid);
    }

    public void doWhenSelected(Graphics g, boolean toDraw) {
    }

    public void enqueueTabularCommands(Hashtable commandHashTable, Hashtable commandsOrder) {
    }

    public void enqueueButtonCommands(Hashtable commandHashTable, Hashtable commandsOrder) {
    }

    public void enqueueNShapesCommands(Hashtable commandHashTable, Hashtable commandsOrder) {
    }

    public void enqueueSShapesCommands(Hashtable commandHashTable, Hashtable commandsOrder) {
    }

    public void this_mouseClicked(MouseEvent e) {
        if (e.getModifiers() == Event.META_MASK && getPopUp().getComponentCount() > 0) {
            int xViewPort = tScrollPane.getViewport().getViewPosition().x;
            int yViewPort = tScrollPane.getViewport().getViewPosition().y;
            this.popMenuSysCall(e, ((ArianneViewer.DrawingPanel) getFatherPanel()), (int) Math.round(getMinX() - xViewPort), (int) Math.round(getMinY()) - yViewPort);
        }
    }

    public void initTable() {
        getTable().setDataSet(tabDataSet);
        getTable().addMouseListener(new Tabular_this_mouseAdapter(this));
        tScrollPane.getViewport().add(t, null);
        getFatherPanel().add(tScrollPane, new XYConstraints(getIntMinX(), getIntMinY(), getIntWidth(), getIntHeight()));
        setBaseProperties(t);
        if (updaterThread != null) updaterThread = null;
        if (updaterThread == null) this.createUpdaterThread(THREAD_SLEEP); else {
            this.terminateUpdaterThread();
            updaterThread = null;
            createUpdaterThread(THREAD_SLEEP);
        }
    }

    public void setBaseProperties(JdbTable tab) {
        getTable().setEditable(false);
        getTable().setColumnHeaderVisible(displayHeader);
        getTable().setRowHeaderVisible(displayRowNum);
        getTable().setFont(getFont());
        getTable().getTableHeader().setFont(getFont());
        getTable().setAutoResizeMode(autoResizeMode);
        tab.setShowHorizontalLines(showHorizontalGrid);
        tab.setShowVerticalLines(showVerticalGrid);
        setRenderer();
        getTable().setBorder(BorderFactory.createLineBorder(getShapeBorderColor(), 1));
    }

    /**
   * Restituisce l'oggetto swing TableScrollPane che contiene la JdbTable associata
   * all'oggetto
   * @return il TableScrollPane contenente la JdbTable
   */
    public TableScrollPane getTabContainer() {
        return tScrollPane;
    }

    public void setTabContainer(TableScrollPane ts) {
        tScrollPane = ts;
    }

    public void setDisplayHeader(boolean dh) {
        this.displayHeader = dh;
    }

    public boolean getDisplayHeader() {
        return this.displayHeader;
    }

    /**
   * Restituisce l'oggetto swing JdbTable associato all'oggetto tabellare
   * @return la JdbTable associata all'oggetto tabellare
   */
    public JdbTable getTable() {
        return t;
    }

    public boolean thereAreChanges(String q) {
        newValues = new FastTable();
        Object curVal = null;
        Statement s = null;
        ResultSet r = null;
        if (q != null && !q.equals("")) try {
            s = getRemoteDb().createStatement();
            r = s.executeQuery(q);
            int nRow = 0;
            while (r.next()) {
                nRow++;
                for (int n = 0; n < r.getMetaData().getColumnCount(); n++) {
                    curVal = r.getObject(n + 1);
                    newValues.add(curVal);
                    if (oldValues.size() > 0 && newValues.size() - 1 < oldValues.size() && !(compare.areEqual(curVal, oldValues.get(newValues.size() - 1)))) {
                        oldValues = newValues;
                        setLastChangeDrawn(false);
                        return true;
                    }
                }
            }
            if (nRow != t.getRowCount()) {
                setLastChangeDrawn(false);
                return true;
            }
            oldValues = newValues;
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally {
            try {
                r.close();
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            r = null;
            s = null;
        }
        return false;
    }

    public void selectRow() {
        try {
            if (t.getRowCount() > 0 && t.getRowCount() > selRow && selRow > -1 && t.getSelectedRow() != selRow) {
                t.setRowSelectionInterval(selRow, selRow);
            } else if (t.getSelectedRow() == -1 && selRow > -1 && t.getRowCount() > 0) {
                int i = 0;
                while (t.getSelectedRow() == -1 && i < 100) try {
                    i++;
                    t.setRowSelectionInterval(selRow, selRow);
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHandler.log("Selezione della riga corrente (" + selRow + ") della tabella fallita - \n" + ex.getMessage(), Level.INFO, "ERR_MSG", isLoggingEnabled());
        }
    }

    public synchronized void handleTabDataSet(boolean rfrsh) {
        refreshing = true;
        if (rfrsh) {
            t.setDataSet(null);
            tabDataSet.refresh();
            t.setDataSet(tabDataSet);
            setRenderer();
            selectRow();
            setLastChangeDrawn(true);
        } else {
            t.setDataSet(null);
            tabDataSet.open();
            t.setDataSet(tabDataSet);
            setRenderer();
        }
        refreshing = false;
    }

    public boolean isRefreshing() {
        return refreshing;
    }

    public void updateVal() {
        try {
            if (tabDataSet.getQuery() == null) {
                tabDataSet.close();
                tabDataSet.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(getRemoteDb(), getSqlQuery(), null, true, LOAD_OPTION));
                tabDataSet.setReadOnly(true);
                tabDataSet.setMetaDataUpdate(MetaDataUpdate.NONE);
                tabDataSet.getQuery().setLoadOption(Load.AS_NEEDED);
                handleTabDataSet(false);
            } else {
                if (!tabDataSet.isOpen()) {
                    tabDataSet.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(getRemoteDb(), getSqlQuery(), null, true, LOAD_OPTION));
                    tabDataSet.setReadOnly(true);
                    tabDataSet.setMetaDataUpdate(MetaDataUpdate.NONE);
                    tabDataSet.getQuery().setLoadOption(Load.AS_NEEDED);
                    handleTabDataSet(false);
                } else {
                    if (thereAreChanges(getSqlQuery())) {
                        setChanges(true);
                        setLastChangeDrawn(false);
                        forceRefresh();
                    }
                }
            }
        } catch (DataSetException ex) {
            ex.printStackTrace();
        }
    }

    public void forceRefresh() {
        refreshEnabled = true;
    }

    public boolean getRefresh() {
        return refreshEnabled;
    }

    public void unForceRefresh() {
        refreshEnabled = false;
    }

    public boolean thereAreChanges() {
        return thereAreChanges(getSqlQuery());
    }

    public void setTableVisibility() {
        boolean vis = isInOverlay();
        if (t.isVisible() != vis) {
            t.setVisible(vis);
            setBaseProperties(getTable());
        }
    }

    public void draw(Graphics2D g, JPanel p, boolean toDraw) {
        if (p != null) {
            if (((ArianneViewer.DrawingPanel) p).getZoomFactor() != getOldZoomFactor()) {
                getFatherPanel().remove(tScrollPane);
                getFatherPanel().add(tScrollPane, new XYConstraints(getIntMinX(), getIntMinY(), getIntWidth(), getIntHeight()));
                tScrollPane.setLocation(getXCoordinates()[0], getYCoordinates()[0]);
                tScrollPane.setSize(getIntWidth(), getIntHeight());
                tScrollPane.setPreferredSize(new Dimension(getIntWidth(), getIntHeight()));
                getTable().setFont(getFont());
                getTable().getTableHeader().setFont(getFont());
                setOldZoomFactor(getZoomFactor());
            }
            setMousePosX(((ArianneViewer.DrawingPanel) p).getMousePosX() - getTabPosX());
            setMousePosY(((ArianneViewer.DrawingPanel) p).getMousePosY() - getTabPosY());
        }
        setTableVisibility();
        if (isVisible()) {
            elapsed = ((ArianneViewer.DrawingPanel) p).getTimeInMsec() - lastReadMsecElapsed;
            if (getConnection() != null && getSqlQuery() != null && !getSqlQuery().equals("") && !getSqlQuery().equals("null")) {
                if (((ArianneViewer.DrawingPanel) p).getTimerButton()) {
                    if (elapsed > ((ArianneViewer.DrawingPanel) p).getRefreshPeriod()) {
                        lastReadMsecElapsed += elapsed;
                        if (!isInOverlay()) return;
                        if (t.getSelectedRow() != -1) selRow = t.getSelectedRow();
                        if (t.getSelectedColumn() != -1) selCol = t.getSelectedColumn();
                        if (getRefresh() || !isLastChangeDrawn()) {
                            if (tabDataSet.isOpen() && tabDataSet.getQuery() != null && t.getSelectedRow() != -1) {
                                handleTabDataSet(true);
                            }
                            unForceRefresh();
                        }
                        selectRow();
                        try {
                            if (t.getColumnCount() >= selCol && selCol > -1) {
                                t.setColumnSelectionInterval(selCol, selCol);
                            }
                        } catch (Exception ex1) {
                            ex1.printStackTrace();
                        }
                        if (updaterThread == null) {
                            createUpdaterThread(THREAD_SLEEP);
                            if (!updaterThread.isAlive()) updaterThread.start();
                        } else {
                            if (!updaterThread.isAlive()) {
                                updaterThread.start();
                            }
                        }
                    } else {
                        this.terminateUpdaterThread();
                        updaterThread = null;
                    }
                }
                if (tabDataSet.getQuery() == null) {
                    tabDataSet.close();
                    tabDataSet.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(getRemoteDb(), getSqlQuery(), null, true, LOAD_OPTION));
                    handleTabDataSet(false);
                }
            }
        }
    }

    public int getColumnWidth() {
        return this.columnWidth;
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    public void setRowHeight(int rh) {
        if (rh > 0) {
            this.rowHeight = rh;
            this.getTable().setRowHeight(rh);
        } else {
            JOptionPane.showMessageDialog(null, "Row height value less than '1' is invalid. Setting to '10'.", "Invalid table row height", JOptionPane.ERROR_MESSAGE);
            this.rowHeight = 10;
            this.getTable().setRowHeight(rowHeight);
        }
    }

    public int getRowMargin() {
        return this.rowMargin;
    }

    public void setRowMargin(int rm) {
        if (rm > 0) {
            this.rowMargin = rm;
            this.getTable().setRowMargin(rm);
        } else {
            JOptionPane.showMessageDialog(null, "Row margin value less than '1' is invalid. Setting to '10'.", "Invalid table row margin", JOptionPane.ERROR_MESSAGE);
            this.rowMargin = 10;
            this.getTable().setRowMargin(rowMargin);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public int getRGBTextColor() {
        return textColor.getRGB();
    }

    public Font getFont() {
        Font fnt = new Font(actFont.getName(), actFont.getStyle(), (int) Math.round(actFont.getSize() * getZoomFactor()));
        return fnt;
    }

    public void setFont(Font fnt) {
        actFont = fnt;
    }

    public void setShapeFillColor(Color c) {
        shapeFillColor = c;
        setRenderer();
    }

    public int getTableNumCols() {
        int res = -1;
        if (getTable() != null) res = getTable().getColumnCount();
        return res;
    }

    public int getTableNumRows() {
        int res = -1;
        if (getTable() != null) res = getTable().getRowCount();
        return res;
    }

    String getRowString(QueryDataSet qds) {
        String res = "";
        DataRow d = new DataRow(qds);
        qds.getDataRow(d);
        for (int i = 0; i < qds.getColumnCount(); i++) {
            Column c = d.getColumn(i);
            int colType = c.getDataType();
            String val = "";
            switch(colType) {
                case com.borland.dx.dataset.Variant.FLOAT:
                    val = "" + d.getFloat(i);
                    break;
                case com.borland.dx.dataset.Variant.DOUBLE:
                    val = "" + d.getDouble(i);
                    break;
                case com.borland.dx.dataset.Variant.INT:
                    val = "" + d.getInt(i);
                    break;
                case com.borland.dx.dataset.Variant.STRING:
                    val = "" + d.getString(i);
                    break;
                case com.borland.dx.dataset.Variant.DATE:
                    val = "" + d.getDate(i);
                    break;
                case com.borland.dx.dataset.Variant.BOOLEAN:
                    val = "" + d.getBoolean(i);
                    break;
            }
            for (int j = 0; j < this.getColumnWidth(); j++) val = val + " ";
            res = res + val.substring(0, Math.min(this.columnWidth, val.length())) + " ";
        }
        return res;
    }

    private String parseSelect(String query) {
        String res = "";
        if (!query.toUpperCase().startsWith("SELECT")) return "Query syntax error";
        query = query.toUpperCase().replaceFirst("SELECT", "");
        res = query.toUpperCase().substring(0, query.indexOf("FROM")).replaceAll(",", "   ");
        return res;
    }

    void shape_menuItem_actionPerformed(ActionEvent e) {
        Database localDb = this.getFatherPanel().getLocalDb();
        QueryDataSet queryMenuSQL = new QueryDataSet();
        try {
            String query = "SELECT SYS_CALL_NAME FROM SYSCALL " + "WHERE DESCR = '" + e.getActionCommand() + "'";
            queryMenuSQL.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(localDb, query, null, true, Load.ALL));
            queryMenuSQL.open();
            String command = queryMenuSQL.getString("SYS_CALL_NAME");
            String[] result = command.split("\\s");
            for (int k = 0; k < result.length; k++) {
                if (result[k].startsWith("%")) {
                    String sqlColVal = "";
                    if (result[k].startsWith("%row")) {
                        sqlColVal = "" + t.getSelectedRow();
                        command = command.replaceAll("%row", sqlColVal);
                    } else {
                        int numCol = Integer.parseInt(result[k].substring(1, result[k].length()));
                        sqlColVal = "" + t.getValueAt(t.getSelectedRow(), numCol);
                        command = command.replaceAll(result[k], sqlColVal);
                    }
                }
            }
            Runtime.getRuntime().exec(command);
            queryMenuSQL.close();
            queryMenuSQL = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getMousePosX() {
        return this.mousePosX;
    }

    public int getMousePosY() {
        return this.mousePosY;
    }

    public void setMousePosX(int xPos) {
        this.mousePosX = xPos;
    }

    public void setMousePosY(int yPos) {
        this.mousePosY = yPos;
    }

    public int getTabPosX() {
        return (int) Math.round(getXPoints()[0]);
    }

    public int getTabPosY() {
        return (int) Math.round(getYPoints()[0]);
    }

    public int getActPointedRow() {
        return t.getSelectedRow();
    }

    public boolean isInsideArea(Point p) {
        setIntCoord();
        int counter = 0;
        double xinters;
        Point p1, p2;
        p1 = new Point(getXCoordinates()[0], getYCoordinates()[0]);
        for (int i = 1; i <= getNumVertex(); i++) {
            p2 = new Point(getXCoordinates()[i % getNumVertex()], getYCoordinates()[i % getNumVertex()]);
            if (p.y > Math.min(p1.y, p2.y)) {
                if (p.y <= Math.max(p1.y, p2.y)) {
                    if (p.x <= Math.max(p1.x, p2.x)) {
                        if (p1.y != p2.y) {
                            xinters = (p.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
                            if (p1.x == p2.x || p.x <= xinters) counter++;
                        }
                    }
                }
            }
            p1 = p2;
        }
        if (counter % 2 == 0) return (false); else return (true);
    }

    public boolean isInSelectArea(Point p) {
        Point sPoint;
        Point ePoint;
        for (int i = 0; i < this.getNumVertex(); i++) {
            for (int k = 0; k < getNumVertex(); k++) {
                setXCoord(k, (int) Math.round(getXPoints()[k]));
                setYCoord(k, (int) Math.round(getYPoints()[k]));
            }
            sPoint = new Point(getXCoordinates()[i], getYCoordinates()[i]);
            ePoint = new Point(getXCoordinates()[(i + 1) % this.getNumVertex()], getYCoordinates()[(i + 1) % this.getNumVertex()]);
            int h = ePoint.y - sPoint.y;
            int k = ePoint.x - sPoint.x;
            if (h >= 0) {
                if ((p.y - sPoint.y) * k >= (p.x - sPoint.x - epsilon) * h && (p.y - sPoint.y) * k <= (p.x - sPoint.x + epsilon) * h && (p.y < ePoint.y && p.y > sPoint.y)) {
                    return true;
                }
            } else {
                if ((p.y - sPoint.y) * k <= (p.x - sPoint.x - epsilon) * h && (p.y - sPoint.y) * k >= (p.x - sPoint.x + epsilon) * h && (p.y < sPoint.y && p.y > ePoint.y)) {
                    return true;
                }
            }
            if (k >= 0) {
                if ((p.y - sPoint.y - epsilon) * k <= (p.x - sPoint.x) * h && (p.y - sPoint.y + epsilon) * k >= (p.x - sPoint.x) * h && (p.x < ePoint.x && p.x > sPoint.x)) {
                    return true;
                }
            } else {
                if ((p.y - sPoint.y - epsilon) * k >= (p.x - sPoint.x) * h && (p.y - sPoint.y + epsilon) * k <= (p.x - sPoint.x) * h && (p.x < sPoint.x && p.x > ePoint.x)) return true;
            }
        }
        return false;
    }

    public void inscribePoints(Point ePoint, Point sPoint) {
        int startX = (int) Math.min(ePoint.getX(), sPoint.getX());
        int endX = (int) Math.max(ePoint.getX(), sPoint.getX());
        int startY = (int) Math.min(ePoint.getY(), sPoint.getY());
        int endY = (int) Math.max(ePoint.getY(), sPoint.getY());
        getXPoints()[0] = startX;
        getYPoints()[0] = startY;
        getXPoints()[1] = endX;
        getYPoints()[1] = startY;
        getXPoints()[2] = endX;
        getYPoints()[2] = endY;
        getXPoints()[3] = startX;
        getYPoints()[3] = endY;
        setIntCoord();
    }
}

class Tabular_this_mouseAdapter extends java.awt.event.MouseAdapter {

    TabularShape adaptee;

    Tabular_this_mouseAdapter(TabularShape adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.this_mouseClicked(e);
    }
}
