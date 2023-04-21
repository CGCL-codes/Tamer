package ArianneEditor;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jfree.chart.*;
import ArianneUtil.*;
import com.borland.dx.dataset.*;
import com.borland.jbcl.layout.*;

/**
 * ChartShape e' la classe derivata da ArianneUtil.ChartShape che implementa le regole di
 * visualizzazione dei grafici
 * <p>
 *
 * @author      Andrea Annibali
 * @version     1.0
 */
public class EditorChartShape extends ArianneEditor.FillableShape {

    private double oldZoomFactor = 100;

    private double minYRange = 0;

    private double maxYRange = 0;

    private float oldAlpha = -1.0f;

    private int mousePosX = -1;

    private int mousePosY = -1;

    private int rectW = 0;

    private int rectH = 0;

    private int historyCount = 1000;

    private boolean insideArea = false;

    private boolean displayShapes;

    private boolean specifyTimeStamp = false, autoScale = true, showLegend = true, vOrientation = false;

    private String chartTitle = "";

    private String yRangeLabel = "Values";

    private String xRangeLabel = "Time";

    private String path = "";

    private String oldPath = "";

    private String chartTitleFontName = "";

    private int chartTitleFontStyle = Font.ITALIC;

    private int chartTitleFontSize = 14;

    private int chartTitleColor = Color.black.getRGB();

    private Font font = JFreeChart.DEFAULT_TITLE_FONT;

    private Color textColor;

    private String[] curveNames = new String[] { "Prima curva", "Seconda curva", "Terza curva" };

    private ImageIcon bckImg = null;

    JMenuItem menuItem2;

    JMenuItem menuItem3;

    JMenuItem menuItem4;

    JMenuItem menuItem5;

    EditorChartShapeBean csb;

    /**
     * Costruttore richiamato quando viene disegnata un nuovo grafico (in particolare nel
     * 'mouser_released')
     * @param elId identificativo univoco dell'oggetto
     * @param ePoint end-point
     * @param sPoint start-point
     * @param cvs Hashtable contenente le curve
     * @param ds stabilisce se i simboli sulla curva sono da visualizzare o meno
     * @param bckCol colore di background
     * @param brdCol colore del bordo
     * @param isOpaque stato di opacita' dell'oggetto
     * @param p il EditorDrawingPanel di appartenenza
     */
    EditorChartShape(int elId, Point ePoint, Point sPoint, Hashtable cvs, boolean ds, int lt, String ls, float a, Color bckCol, Color brdCol, String imgPath, boolean isOpaque, EditorDrawingPanel p, boolean isInBck, int ovl, String imgN, boolean sl, boolean vori) {
        super(imgN, p, elId, "Chart", 4, 1, "Continuous", a, ovl, isOpaque, isInBck, ePoint, sPoint, false);
        this.displayShapes = ds;
        this.setPath(imgPath);
        this.setOpaque(isOpaque);
        rectW = (int) Math.round(this.getMaxX() - this.getMinX());
        rectH = (int) Math.round(this.getMaxY() - this.getMinY());
        setShowLegend(sl);
        setVOrientation(vori);
        initChart();
        setShapeBorderColor(brdCol);
        setShapeFillColor(bckCol);
        if (getPath() != null && !(getPath().equals(""))) {
            bckImg = new ImageIcon(getPath());
        }
    }

    /**
     * Costruttore utilizzato durante la loadChartShape
     * @param elId identificativo univoco dell'oggetto
     * @param xPnt coordinate x del poligono di inscrizione dell'oggetto
     * @param yPnt coordinate y del poligono di inscrizione dell'oggetto
     * @param cvs Hashtable contenente le curve
     * @param ds stabilisce se i simboli sulla curva sono da visualizzare o meno
     * @param bckCol colore di background
     * @param brdCol colore del bordo
     * @param sqlQry Query Associata
     * @param isOpaque stato di opacita' dell'oggetto
     * @param p il EditorDrawingPanel di appartenenza
     * @param ct il titolo da visualizzare nel grafico
     */
    EditorChartShape(int elId, double xPnt[], double yPnt[], Hashtable cvs, boolean ds, Color brdCol, Color bckCol, Font fnt, Color fntColor, String sqlQry, String imgPath, float a, double minYR, double maxYR, boolean isOpaque, EditorDrawingPanel p, boolean isInBck, int ovl, String ct, String yrl, String xrl, String ctfn, int ctsty, int ctsz, int ctc, String[] cn, int hc, boolean st, boolean as, String imgN, boolean sl, boolean vori) {
        super(imgN, p, elId, "Chart", 4, xPnt, yPnt, 1, "Continuous", a, ovl, isOpaque, isInBck, false);
        this.setChartTitle(ct);
        setYRangeLabel(yrl);
        setXRangeLabel(xrl);
        setChartTitleFontName(ctfn);
        setChartTitleFontStyle(ctsty);
        setChartTitleFontSize(ctsz);
        setChartTitleColor(ctc);
        setMinYRange(minYR);
        setMaxYRange(maxYR);
        this.displayShapes = ds;
        this.setSpecifyTimeStamp(st);
        this.setAutoScale(as);
        this.setPath(imgPath);
        this.setOpaque(isOpaque);
        this.setSqlQuery(sqlQry);
        rectW = (int) Math.round(this.getMaxX() - this.getMinX());
        rectH = (int) Math.round(this.getMaxY() - this.getMinY());
        setCurvesName(cn);
        setShowLegend(sl);
        setVOrientation(vori);
        initChart();
        this.setFont(fnt);
        this.setTextColor(fntColor);
        this.setShapeBorderColor(brdCol);
        this.setShapeFillColor(bckCol);
        this.setHistoryCount(hc);
        if (getPath() != null && !(getPath().equals(""))) {
            bckImg = new ImageIcon(getPath());
        }
    }

    /**
     * Costruttore invocato alla pressione del tasto di inserimento nuovo grafico del PictureEditor
     * @param c colore del bordo
     * @param p il EditorDrawingPanel di appartenenza
     */
    EditorChartShape(int elId, Color c, EditorDrawingPanel p, int ovl) {
        super("", p, elId, "Chart", 4, new double[4], new double[4], 1, "Continuous", 1.0f, ovl, true, false, false);
        displayShapes = false;
        setOpaque(true);
        setShowLegend(true);
        setVOrientation(false);
        setXPoints(new double[getNumVertex()]);
        setYPoints(new double[getNumVertex()]);
        for (int i = 0; i < getNumVertex(); i++) {
            setXPoint(i, 1000);
            setYPoint(i, 1000);
        }
        setShapeBorderColor(Color.black);
        setShapeFillColor(c);
        rectW = (int) Math.round(this.getMaxX() - this.getMinX());
        rectH = (int) Math.round(this.getMaxY() - this.getMinY());
    }

    /**
     * Inizializza il grafico
     */
    public void initChart() {
        LogHandler.log("Inizializzazione del grafico", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        addToPanel(getFatherPanel());
        LogHandler.log("Fine inizializzazione del grafico", Level.FINEST, "LOG_MSG", isLoggingEnabled());
    }

    public void setShowLegend(boolean b) {
        showLegend = b;
    }

    public boolean getShowLegend() {
        return showLegend;
    }

    public void setMinYRange(double m) {
        minYRange = m;
        if (getChartShapeBean() != null) getChartShapeBean().setMinYRange(minYRange);
    }

    public double getMinYRange() {
        return minYRange;
    }

    public void setMaxYRange(double m) {
        maxYRange = m;
        if (getChartShapeBean() != null) getChartShapeBean().setMaxYRange(maxYRange);
    }

    public double getMaxYRange() {
        return maxYRange;
    }

    /**
     * Restituisce il titolo del grafico
     * @return un oggetto di tipo String che contiene il titolo del grafico
     */
    public String getChartTitle() {
        setChartTitle(csb.getChartTitle());
        return chartTitle;
    }

    /**
     * Imposta il titolo del grafico
     * @param s il titolo da impostare
     */
    public void setChartTitle(String s) {
        if (csb != null) csb.setChartTitle(s);
        chartTitle = s;
    }

    public String getChartTitleFontName() {
        return chartTitleFontName;
    }

    public int getChartTitleFontStyle() {
        return chartTitleFontStyle;
    }

    public int getChartTitleFontSize() {
        return chartTitleFontSize;
    }

    public void setChartTitleFontName(String fn) {
        chartTitleFontName = fn;
        if (csb != null) {
            csb.setChartTitleFontName(fn);
        }
    }

    public void setChartTitleFontSize(int sz) {
        chartTitleFontSize = sz;
        if (csb != null) {
            csb.setChartTitleSize(sz);
        }
    }

    public void setChartTitleFontStyle(int fs) {
        chartTitleFontStyle = fs;
        if (csb != null) {
            csb.setChartTitleStyle(fs);
        }
    }

    public void setSpecifyTimeStamp(boolean st) {
        specifyTimeStamp = st;
        if (getCharShapeBean() != null) getCharShapeBean().setSpecifyTimeStamp(specifyTimeStamp);
    }

    public boolean getSpecifyTimeStamp() {
        return specifyTimeStamp;
    }

    public void setAutoScale(boolean as) {
        autoScale = as;
        if (getCharShapeBean() != null) getCharShapeBean().setAutoScale(autoScale);
    }

    public boolean getAutoScale() {
        return autoScale;
    }

    /**
     * Metodo invocato quando il cursore entra nell'area del ChartShapeBean
     * @param e l'evento del mouse (il click) che ha causato l'invocazione del metodo
     */
    public void this_mouseEntered(MouseEvent e) {
        insideArea = true;
    }

    /**
     * Metodo invocato quando il cursore esce dall'area del ChartShapeBean
     * @param e l'evento del mouse (il click) che ha causato l'invocazione del metodo
     */
    public void this_mouseExited(MouseEvent e) {
        insideArea = false;
    }

    /**
     * Metodo invocato quando viene eseguito un click all'interno del grafico
     * @param e l'evento del mouse (il click) che ha causato l'invocazione del metodo
     */
    public ChartShapeBean getChartShapeBean() {
        return csb;
    }

    public void this_mouseClicked(MouseEvent e) {
        getFatherPanel().this_mouseClicked(e);
        if (e.getModifiers() == Event.META_MASK) {
            popMenu(e, getCharShapeBean());
        }
    }

    /**
     * Imposta le coordinate dei vertici del poligono di inscrizione dell'oggetto grafico
     * @param ePoint punto di fine-dragging del mouse
     * @param sPoint punto di inizio-dragging del mouse
     */
    public void inscribePoints(Point ePoint, Point sPoint) {
        int startX = (int) Math.min(ePoint.getX(), sPoint.getX());
        int endX = (int) Math.max(ePoint.getX(), sPoint.getX());
        int startY = (int) Math.min(ePoint.getY(), sPoint.getY());
        int endY = (int) Math.max(ePoint.getY(), sPoint.getY());
        setXPoint(0, startX);
        setYPoint(0, startY);
        setXPoint(1, endX);
        setYPoint(1, startY);
        setXPoint(2, endX);
        setYPoint(2, endY);
        setXPoint(3, startX);
        setYPoint(3, endY);
        setIntCoord();
    }

    /**
     * Restituisce il bean
     * @return ChartShapeBean
     */
    public ArianneUtil.ChartShapeBean getCharShapeBean() {
        return csb;
    }

    public void addToPanel(EditorDrawingPanel p) {
        setIntCoord();
        csb = new EditorChartShapeBean(this, new Hashtable(), null, getXCoordinates()[0], getYCoordinates()[0], getMinYRange(), getMaxYRange(), new Dimension(rectW, rectH), new Dimension(rectW, rectH), getShapeBorderColor(), shapeFillColor, chartTitle, getYRangeLabel(), getXRangeLabel(), chartTitleFontName, chartTitleFontStyle, chartTitleFontSize, chartTitleColor, font, textColor, 1000, getSpecifyTimeStamp(), getAutoScale(), popup, getPollInterval(), bckImg, getAlpha(), getShowLegend(), getVOrientation());
        if (csb != null) {
            p.add(csb, new XYConstraints(getXCoordinates()[0], getYCoordinates()[0], getIntWidth(), getIntHeight()), 0);
            csb.validate();
        } else {
            LogHandler.log("Il grafico non poteva essere aggiunto al pannello (era null)", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        }
    }

    public void setYRangeLabel(String yrl) {
        yRangeLabel = yrl;
        if (csb != null) {
            csb.setYRangeLabel(yrl);
        }
    }

    public String getYRangeLabel() {
        return yRangeLabel;
    }

    public void setXRangeLabel(String xrl) {
        xRangeLabel = xrl;
        if (csb != null) {
            csb.setXRangeLabel(xrl);
        }
    }

    public String getXRangeLabel() {
        return xRangeLabel;
    }

    /**
     * Restituisce la coordinata X pi� in alto a sinistra dell'oggetto
     * @return int
     */
    public int getChartPosX() {
        return (int) Math.round(getXPoints()[0]);
    }

    /**
     * Restituisce la coordinata Y pi� in alto a sinistra dell'oggetto
     * @return int
     */
    public int getChartPosY() {
        return (int) Math.round(getYPoints()[0]);
    }

    /**
     * Inizializza i menu di pop-up che si attivano con il tasto destro
     * del mouse
     */
    public void initMenu() {
        super.initMenu();
        menuItem2 = new JMenuItem("Define curves");
        menuItem2.addActionListener(new Chart_menuItem2_actionAdapter(this));
        popup.add(menuItem2);
        menuItem3 = new JMenuItem("Presentation Dialog");
        menuItem3.addActionListener(new Chart_menuItem3_actionAdapter(this));
        popup.add(menuItem3);
        menuItem4 = new JMenuItem("Call Up Dialog");
        menuItem4.addActionListener(new Chart_menuItem4_actionAdapter(this));
        popup.add(menuItem4);
        menuItem5 = new JMenuItem("Background image");
        menuItem5.addActionListener(new Chart_menuItem5_actionAdapter(this));
        popup.add(menuItem5);
        LogHandler.log("Menu inizializzati ", Level.FINEST, "LOG_MSG", isLoggingEnabled());
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

    public void fill(Color c) {
        shapeFillColor = c;
    }

    /**
     * Esegue il parsing della SELECT, controllandone la sintassi e restituendo
     * un messaggio di errore nel caso non sia sintatticamente corretta.
     * @param query String
     * @return String
     */
    private String parseSelect(String query) {
        String res = "";
        LogHandler.log("Effettuo il parsing della select ", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        if (!query.toUpperCase().startsWith("SELECT")) {
            LogHandler.log("Si � verificato un errore nel parsing ", Level.FINEST, "LOG_MSG", isLoggingEnabled());
            return "Query syntax error";
        }
        query = query.toUpperCase().replaceFirst("SELECT", "");
        res = query.toUpperCase().substring(0, query.indexOf("FROM")).replaceAll(",", "   ");
        LogHandler.log("Parsing terminato con successo", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        return res;
    }

    /**
     * Restituisce il formatter impostato per l'oggetto testuale corrente
     * (non implementato)
     * @return la stringa con cui � stato costruito il formatter
     */
    public String getFormatter() {
        return null;
    }

    public void setImg() {
        JFileChooser jFileChooser1 = new JFileChooser();
        String separator = "";
        if (JFileChooser.APPROVE_OPTION == jFileChooser1.showOpenDialog(this.getFatherFrame())) {
            setPath(jFileChooser1.getSelectedFile().getPath());
            separator = jFileChooser1.getSelectedFile().separator;
            File dirImg = new File("." + separator + "images");
            if (!dirImg.exists()) {
                dirImg.mkdir();
            }
            int index = getPath().lastIndexOf(separator);
            String imgName = getPath().substring(index);
            String newPath = dirImg + imgName;
            try {
                File inputFile = new File(getPath());
                File outputFile = new File(newPath);
                if (!inputFile.getCanonicalPath().equals(outputFile.getCanonicalPath())) {
                    FileInputStream in = new FileInputStream(inputFile);
                    FileOutputStream out = new FileOutputStream(outputFile);
                    int c;
                    while ((c = in.read()) != -1) out.write(c);
                    in.close();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
                JOptionPane.showMessageDialog(null, ex.getMessage().substring(0, Math.min(ex.getMessage().length(), getFatherPanel().MAX_DIALOG_MSG_SZ)) + "-" + getClass(), "Set image", JOptionPane.ERROR_MESSAGE);
            }
            setPath(newPath);
            bckImg = new ImageIcon(getPath());
        }
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String pth) {
        path = pth;
    }

    /**
     * Imposta il formatter della stringa di visualizzazione (non implementato)
     * @param form la stringa con cui costruire il formatter (ad es. "####.##,##")
     */
    public void setFormatter(String form) {
    }

    /**
     * Restituisce la posizione X del mouse
     * @return int
     */
    public int getMousePosX() {
        return this.mousePosX;
    }

    /**
     * Restituisce la posizione Y del mouse
     * @return int
     */
    public int getMousePosY() {
        return this.mousePosY;
    }

    /**
     * Imposta la posizione X del mouse
     * @param xPos int
     */
    public void setMousePosX(int xPos) {
        this.mousePosX = xPos;
    }

    /**
     * Imposta la posizione Y del mouse
     * @param yPos int
     */
    public void setMousePosY(int yPos) {
        this.mousePosY = yPos;
    }

    public double getOldZoomFactor() {
        return oldZoomFactor;
    }

    public void setOldZoomFactor(double v) {
        oldZoomFactor = v;
    }

    public int getRectW() {
        return getIntMaxX() - getIntMinX();
    }

    public int getRectH() {
        return getIntMaxY() - getIntMinY();
    }

    public void setVOrientation(boolean b) {
        vOrientation = b;
    }

    public boolean getVOrientation() {
        return vOrientation;
    }

    /**
     * Visualizza l'oggetto implementandone le regole di rappresentazione
     * @param g l'oggetto graphics su cui disegnare l'oggetto tabellare
     */
    public void draw(Graphics2D g, JPanel p, boolean toDraw) {
        if (toDraw) {
            if (p != null) {
                if (p instanceof EditorDrawingPanel && ((EditorDrawingPanel) p).getZoomFactor() != getOldZoomFactor()) {
                    Dimension d = new Dimension(getRectW(), getRectH());
                    if (csb != null) {
                        csb.setPreferredSize(d);
                        csb.setMinimumSize(d);
                        csb.setMaximumSize(d);
                        csb.setSize(d);
                        csb.validate();
                    }
                    if (csb != null && !csb.isRunning()) {
                        csb.start();
                        if (getConnection() != null) enableDrawing();
                    }
                    setOldZoomFactor(getZoomFactor());
                }
                setMousePosX(getFatherPanel().getMousePosX() - getChartPosX());
                setMousePosY(getFatherPanel().getMousePosY() - getChartPosY());
            }
            setVisible(isVisible() & ((p == null && g != null) || isInOverlay()));
            if (isVisible()) {
                LogHandler.log("Connessioni del grafico al DB riorganizzate ", Level.FINEST, "LOG_MSG", isLoggingEnabled());
                long elapsed = Calendar.getInstance().getTimeInMillis() - lastReadMsecElapsed;
                Stroke oldStroke = g.getStroke();
                g.setStroke(getStroke());
                if (getFatherPanel() != null && elapsed > getFatherPanel().getRefreshPeriod() && getFatherPanel().getTimerToggle()) {
                    lastReadMsecElapsed += elapsed;
                    if (csb != null) {
                        try {
                            if (!csb.isRunning()) {
                                csb.start();
                                enableDrawing();
                            }
                        } catch (DataSetException ex) {
                            this.setSqlQuery("");
                            String msgString = "Attenzione: ricontrollare la query di reperimento dati. \n" + "Si e' verificato il seguente errore: \n" + "DataSetException: " + ex.getMessage() + "\n" + "VendorError: " + ex.getErrorCode();
                            JOptionPane.showMessageDialog(null, msgString.substring(0, Math.min(msgString.length(), getFatherPanel().MAX_DIALOG_MSG_SZ)), "Esecuzione query" + "-" + getClass(), JOptionPane.ERROR_MESSAGE);
                            getFatherEditor().stopTimerToggle();
                        }
                    } else {
                        if (csb.isRunning()) {
                            csb.stop();
                        }
                    }
                }
                if ((p != null && g != null) || (p != null)) {
                    if (getPath() != null && !getPath().equals(oldPath) && !getPath().equals("")) {
                        oldPath = getPath();
                        getCharShapeBean().setBckImg(bckImg);
                    }
                    if (csb != null) if (csb.getLocation().getX() != getXCoordinates()[0] || csb.getLocation().getY() != getYCoordinates()[0] || csb.getSize().getWidth() != (int) Math.round(getWidth()) || csb.getSize().getHeight() != (int) Math.round(getHeight())) {
                        csb.setBounds(getXCoordinates()[0], getYCoordinates()[0], (int) Math.round(getWidth()), (int) Math.round(getHeight()));
                        csb.validate();
                    }
                    if (getAlpha() != oldAlpha) {
                        oldAlpha = getAlpha();
                        if (getCharShapeBean() != null) getCharShapeBean().setAlpha(getAlpha());
                    }
                    if (csb != null && csb.getBackground() != getShapeBorderColor()) {
                        csb.setBackground(getShapeBorderColor());
                        LogHandler.log("Grafico " + csb + ": colore impostato a " + getShapeBorderColor() + "", Level.FINEST, "LOG_MSG", isLoggingEnabled());
                    }
                    if (csb != null) {
                        boolean toBeRepainted = false;
                        if (csb.getDisplayShapes() != displayShapes) {
                            csb.setDisplayShapes(displayShapes);
                            toBeRepainted = true;
                            LogHandler.log("Grafico " + csb + ": displayShapes impostato a " + displayShapes, Level.FINEST, "LOG_MSG", isLoggingEnabled());
                        }
                        if (csb.getSpecifyTimeStamp() != specifyTimeStamp) {
                            csb.setDisplayShapes(specifyTimeStamp);
                            toBeRepainted = true;
                        }
                        if (csb.getBorderColor() != getShapeBorderColor()) {
                            csb.setBorderColor(getShapeBorderColor());
                            toBeRepainted = true;
                            LogHandler.log("Grafico " + csb + ": colore del bordo impostato a " + getShapeBorderColor(), Level.FINEST, "LOG_MSG", isLoggingEnabled());
                        }
                        if (csb.getFillColor() != shapeFillColor) {
                            csb.setFillColor(shapeFillColor);
                            toBeRepainted = true;
                            LogHandler.log("Grafico " + csb + ": colore di riempimento impostato a " + shapeFillColor, Level.FINEST, "LOG_MSG", isLoggingEnabled());
                        }
                        csb.validate();
                        if (toBeRepainted) csb.repaint();
                        csb.getChart().setAntiAlias(true);
                    }
                }
                g.drawRect(getXCoordinates()[0], getYCoordinates()[0], getXCoordinates()[2] - getXCoordinates()[0], getYCoordinates()[2] - getYCoordinates()[0]);
                g.setStroke(oldStroke);
                if (isSelected()) {
                    int selWidth = ra * 2;
                    int selHeight = ra * 2;
                    int cX = ra;
                    int cY = ra;
                    g.setColor(Color.black);
                    for (int i = 0; i < getNumVertex(); i++) {
                        g.drawOval(getXCoordinates()[i] - cX, getYCoordinates()[i] - cY, selWidth, selHeight);
                    }
                    for (int i = 0; i < getNumVertex(); i++) {
                        double midX = (getXPoints()[i] + getXPoints()[(i + 1) % getNumVertex()]) / 2;
                        double midY = (getYPoints()[i] + getYPoints()[(i + 1) % getNumVertex()]) / 2;
                        g.drawRect((int) Math.round(midX) - ra, (int) Math.round(midY) - ra, selWidth, selHeight);
                    }
                }
                if (getSquareDrawActive()) {
                    Stroke drawingStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
                    g.setStroke(drawingStroke);
                    g.drawRect(getXCoordinates()[0], getYCoordinates()[0], getXCoordinates()[2] - getXCoordinates()[0], getYCoordinates()[2] - getYCoordinates()[0]);
                }
            }
            if (p != null && getFatherPanel().getTimerToggle()) {
                if (getCharShapeBean() != null && !getCharShapeBean().isRunning()) enableDrawing();
            } else {
                if (csb != null) csb.stop();
            }
        }
    }

    public boolean getDisplayShapes() {
        return displayShapes;
    }

    public void setDisplayShapes(boolean ds) {
        displayShapes = ds;
    }

    /**
     * Dati il punto di fine dragging del mouse e il vertice che sta subendo lo stretching,
     * effettua la deformazione secondo l'incremento determinato dai parametri incX e incY.
     * @param ePoint Point punto finale del dragging
     * @param size int vertice su cui viene effettuato il dragging
     * @param incX double incremento lungo l'asse X
     * @param incY double incremento lungo l'asse Y
     * @param minXVal double coordinata X minima del poligono entro cui � inscritto l'oggetto
     * @param minYVal double coordinata Y minima del poligono entro cui � inscritto l'oggetto
     * @param maxXVal double coordinata X massima del poligono entro cui � inscritto l'oggetto
     * @param maxYVal double coordinata Y massima del poligono entro cui � inscritto l'oggetto
     * @param XoExtend double coordinata X del punto di riferimento in base al quale effettuare la
     * deformazione
     * @param YoExtend double coordinata Y del punto di riferimento in base al quale effettuare la
     * deformazione
     */
    public void extend(Point ePoint, int size, double incX, double incY, double minXVal, double minYVal, double maxXVal, double maxYVal, double XoExtend, double YoExtend) {
        double xp[] = new double[getNumVertex()];
        double yp[] = new double[getNumVertex()];
        int jX[] = new int[getNumVertex()];
        int jY[] = new int[getNumVertex()];
        boolean trovato_x = false;
        boolean trovato_y = false;
        double hAss, wAss;
        final double ALFA = 0.0250;
        final double BETA = 0.0175;
        for (int i = 0; i < getNumVertex(); i++) {
            xp[i] = getXPoints()[getNumVertex() - i - 1];
            yp[i] = getYPoints()[getNumVertex() - i - 1];
            jX[i] = -1;
            jY[i] = -1;
        }
        hAss = maxYVal - minYVal;
        wAss = maxXVal - minXVal;
        for (int i = 0; i < getNumVertex(); i++) {
            if (incX != 0) {
                if (xp[i] == Math.round(XoExtend) - incX) {
                    trovato_x = true;
                } else if (xp[i] == Math.round(XoExtend)) {
                    jX[i] = i;
                }
                if (trovato_x) {
                    if (size == 2) {
                        incX += -ALFA;
                    } else if (size == 0) {
                        incX += ALFA;
                    }
                }
            }
            if (incY != 0) {
                if (yp[i] == Math.round(YoExtend) - incY) {
                    trovato_y = true;
                } else if (yp[i] == Math.round(YoExtend)) {
                    jY[i] = i;
                }
                if (trovato_y) {
                    if (size == 3) {
                        incY += ALFA;
                    } else if (size == 1) {
                        incY += -ALFA;
                    }
                }
            }
        }
        for (int i = 0; i < getNumVertex(); i++) {
            if (wAss > BETA && i != jX[i] && (size == 0 || size == 2)) {
                xp[i] += incX * Math.abs(Math.round(XoExtend) - xp[i]) / wAss;
            }
            if (hAss > BETA && i != jY[i] && (size == 1 || size == 3)) {
                yp[i] += incY * Math.abs(Math.round(YoExtend) - yp[i]) / hAss;
            }
        }
        for (int i = 0; i < getNumVertex(); i++) {
            setXPoint(i, xp[getNumVertex() - i - 1]);
            setYPoint(i, yp[getNumVertex() - i - 1]);
        }
        setIntCoord();
    }

    public void prepareCurves() {
        Hashtable curves = new Hashtable();
        for (int i = 0; i < sqlElementQuery.length; i++) {
            if (sqlElementQuery[i] != null) {
                Vector columns1 = new Vector();
                if (curveNames[i] == null) curveNames[i] = "Curva " + (i + 1);
                columns1.addElement(curveNames[i]);
                ArianneUtil.QueryPlot qp1 = new ArianneUtil.QueryPlot(i, columns1, sqlElementQuery[i]);
                curves.put(new Integer(qp1.getId()), qp1);
            }
        }
        getCharShapeBean().setCurves(curves);
        csb.init();
    }

    public void enableDrawing() {
        if (getRemoteDb() != null) {
            getCharShapeBean().setConnection(getRemoteDb().getJdbcConnection());
            prepareCurves();
            if (!getChartShapeBean().isRunning()) getCharShapeBean().start();
        }
    }

    public void setQueryList(String[] qL, String[] curveNames) {
        sqlElementQuery = qL;
        Hashtable curves = new Hashtable();
        if (getRemoteDb() == null) {
            String msgString = "Attenzione: non risulta specificata alcuna connessione a DB";
            JOptionPane.showMessageDialog(null, msgString.substring(0, Math.min(msgString.length(), getFatherPanel().MAX_DIALOG_MSG_SZ)) + "-" + getClass(), "Impostazione della connessione", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                if (getRemoteDb() != null) {
                    getCharShapeBean().setConnection(getRemoteDb().getJdbcConnection());
                }
            } catch (com.borland.dx.dataset.DataSetException ex) {
                ex.printStackTrace();
                getFatherEditor().stopTimerToggle();
                JOptionPane.showMessageDialog(null, "Errore nell'impostazione della connessione: e' stata definita correttamente? " + ex.getMessage().substring(0, Math.min(ex.getMessage().length(), getFatherPanel().MAX_DIALOG_MSG_SZ)) + "-" + getClass(), "Attenzione!", JOptionPane.ERROR_MESSAGE);
            }
        }
        for (int i = 0; i < qL.length; i++) {
            if (qL[i] != null) {
                Vector columns1 = new Vector();
                columns1.addElement(curveNames[i]);
                ArianneUtil.QueryPlot qp1 = new ArianneUtil.QueryPlot(i, columns1, qL[i]);
                curves.put(new Integer(qp1.getId()), qp1);
            }
        }
        getCharShapeBean().setCurves(curves);
    }

    /**
     * Richiama la dialog di impostazione delle query che definiscono le
     * curve da visualizzare
     * @param e l'evento che ha scatenato il richiamo di questo metodo
     */
    void ChartmenuItem2_actionPerformed(ActionEvent e) {
        this.getFatherPanel().disableKeyListening();
        ChartCurvesDialog ccd = new ChartCurvesDialog(this.getFatherFrame(), "Chart curves definition", true, isLoggingEnabled());
        ccd.init(this.sqlElementQuery, getCurveNames());
        ccd.setVisible(true);
        setCurvesName(ccd.getCurvesName());
        this.setQueryList(ccd.sqlElementQuery, getCurveNames());
        this.getFatherPanel().enableKeyListening();
    }

    public void setCurvesName(String[] cn) {
        curveNames = cn;
    }

    public String[] getCurvesName() {
        return curveNames;
    }

    public String[] getCurveNames() {
        return curveNames;
    }

    public Color getShapeBorderColor() {
        if (getChartShapeBean() != null) return getCharShapeBean().getShapeBorderColor(); else return null;
    }

    public int getShapeBorderColorRGB() {
        if (getCharShapeBean() != null) return getCharShapeBean().getShapeBorderColor().getRGB(); else return Color.white.getRGB();
    }

    public Color getShapeFillColor() {
        if (getCharShapeBean() != null) return getCharShapeBean().getShapeFillColor(); else return null;
    }

    public int getShapeFillColorRGB() {
        if (getCharShapeBean() != null && getCharShapeBean().getShapeFillColor() != null) return getCharShapeBean().getShapeFillColor().getRGB(); else return Color.white.getRGB();
    }

    public void setShapeBorderColor(Color c) {
        if (c != null) {
            super.setShapeBorderColor(c);
            if (getCharShapeBean() != null) {
                getCharShapeBean().setBorderColor(c);
            }
        }
    }

    public void setShapeFillColor(Color c) {
        super.setShapeFillColor(c);
        if (getCharShapeBean() != null) getCharShapeBean().setFillColor(c);
    }

    /**
     * Richiama la dialog di impostazione i comandi esterni da associare alla shape
     * @param e l'evento (selezione della terza voce del menu di pop up)
     * che ha causato l'invocazione di questo metodo
     */
    void ChartmenuItem3_actionPerformed(ActionEvent e) {
        getFatherPanel().disableKeyListening();
        ChartPropertiesDialog cpd = new ChartPropertiesDialog(getFatherFrame(), "Chart properties", true, isLoggingEnabled());
        cpd.init(csb, this.getTextColor(), this.getShapeBorderColor(), new Color(this.getShapeFillColorRGB()), this.getChartTitleFontName(), this.getChartTitleFontStyle(), this.getChartTitleFontSize(), this.getChartTitleColorRGB(), this.displayShapes, this.getFont(), this.getFormatter(), historyCount, getMinYRange(), getMaxYRange(), this.getSpecifyTimeStamp(), getAutoScale(), bckImg, getShowLegend(), getVOrientation());
        getFatherPanel().centerFrame(cpd);
        cpd.setVisible(true);
        if (cpd.isConfirmed()) {
            this.setShapeBorderColor(cpd.getShapeBorderColor());
            this.setShapeFillColor(cpd.getFillColor());
            this.setDisplayShapes(cpd.getDisplayShapes());
            this.setSpecifyTimeStamp(cpd.getSpecifyTimeStamp());
            this.setFont(cpd.getFont());
            this.setTextColor(cpd.getTextColor());
            this.setChartTitle(cpd.getChartTitle());
            this.setYRangeLabel(cpd.getChartYRLabel());
            this.setXRangeLabel(cpd.getChartXRLabel());
            this.setAutoScale(cpd.getAutoScale());
            this.setShowLegend(cpd.getShowLegend());
            this.setVOrientation(cpd.getVOrientation());
            if (cpd.getChartTitleFont() != null) this.setChartTitleFontName(cpd.getChartTitleFont().getFontName());
            if (cpd.getChartTitleFont() != null) this.setChartTitleFontStyle(cpd.getChartTitleFont().getStyle());
            if (cpd.getChartTitleFont() != null) this.setChartTitleFontSize(cpd.getChartTitleFont().getSize());
            if (cpd.getChartTitleColor() != null) this.setChartTitleColor(cpd.getChartTitleColor().getRGB());
            this.setHistoryCount(cpd.getHistoryCount());
            this.setMinYRange(cpd.getMinYRange());
            this.setMaxYRange(cpd.getMaxYRange());
            this.getFatherPanel().enableKeyListening();
        }
    }

    public int getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(int nsec) {
        historyCount = nsec;
        csb.modifyHistoryCount(historyCount);
    }

    public int getRGBTextColor() {
        return getTextColor().getRGB();
    }

    public Font getFont() {
        this.setFont(csb.getChartFont());
        return this.font;
    }

    public void setFont(Font f) {
        this.font = f;
        if (csb != null && font != null) csb.setChartFont(font);
    }

    public int getChartTitleColorRGB() {
        setChartTitleColor(csb.getChartTitleColor());
        return chartTitleColor;
    }

    public void setChartTitleColor(int c) {
        if (csb != null) csb.setChartTitleColor(c);
        chartTitleColor = c;
    }

    public void setTextColor(Color c) {
        this.textColor = c;
        if (csb != null && font != null) csb.setFontColor(textColor);
    }

    public Color getTextColor() {
        if (csb != null) {
            textColor = csb.getFontColor();
        }
        return textColor;
    }

    public Font getChartFont() {
        if (csb != null) {
            return csb.getChartFont();
        }
        return null;
    }

    /**
     * Richiama la dialog di impostazione i comandi esterni da associare alla shape
     * @param e l'evento (selezione della terza voce del menu di pop up)
     * che ha causato l'invocazione di questo metodo
     */
    void ChartmenuItem4_actionPerformed(ActionEvent e) {
        this.getFatherPanel().disableKeyListening();
        callUpDialog();
        this.getFatherPanel().enableKeyListening();
    }

    void ChartmenuItem5_actionPerformed(ActionEvent e) {
        setImg();
    }
}

class Chart_menuItem2_actionAdapter implements java.awt.event.ActionListener {

    EditorChartShape adaptee;

    Chart_menuItem2_actionAdapter(EditorChartShape adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ChartmenuItem2_actionPerformed(e);
    }
}

class Chart_menuItem3_actionAdapter implements java.awt.event.ActionListener {

    EditorChartShape adaptee;

    Chart_menuItem3_actionAdapter(EditorChartShape adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ChartmenuItem3_actionPerformed(e);
    }
}

class Chart_menuItem4_actionAdapter implements java.awt.event.ActionListener {

    EditorChartShape adaptee;

    Chart_menuItem4_actionAdapter(EditorChartShape adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ChartmenuItem4_actionPerformed(e);
    }
}

class Chart_menuItem5_actionAdapter implements java.awt.event.ActionListener {

    EditorChartShape adaptee;

    Chart_menuItem5_actionAdapter(EditorChartShape adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ChartmenuItem5_actionPerformed(e);
    }
}

class Chart_this_mouseAdapter extends java.awt.event.MouseAdapter {

    EditorChartShape adaptee;

    Chart_this_mouseAdapter(EditorChartShape adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseEntered(MouseEvent e) {
        adaptee.this_mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        adaptee.this_mouseExited(e);
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.this_mouseClicked(e);
    }
}
