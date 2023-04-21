package ArianneViewer;

import java.text.DecimalFormat;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import javolution.text.Text;
import javax.swing.JPanel;

/**
 * <p>Title: Guide Viewer</p>
 *
 * <p>Description: Visualizzatore per pagine create con Arianne Editor</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Itaco S.r.l.</p>
 *
 * @author not attributable
 * @version 1.0
 */
class SliderShape extends FillableShape {

    private double valActSlider = this.getMinSlider();

    private double minSlider = 0;

    private double maxSlider = 100;

    private Font scaleFont;

    private Color scaleFontColor;

    private String valueActual = "50", sliderType = "Normal";

    private int xPSlider[];

    private int yPSlider[];

    private int nTicks = 10;

    long elapsed = -1;

    private boolean showScaleUp = true, showSubScaleUp = true, showValUp = true, showScaleDown = true, showSubScaleDown = true, showValDown = true;

    DecimalFormat stringFormatter = new DecimalFormat("##.####");

    SliderShape(int elId, String st, double xPnt[], double yPnt[], int numV, int lt, String ls, float alpha, int sColor, int fColor, double cMin, double cMax, int nT, Font scFont, int scColor, boolean cyl, boolean showScUp, boolean showSubScUp, boolean showValUp, boolean showScDown, boolean showSubScDown, boolean showValDown, DrawingPanel p, boolean isOp, boolean bck, int ovl, String imgName, boolean polling, int pollMsec, int bckMsec) {
        super("Slider", numV, elId, p, imgName, isOp, bck, ovl, lt, ls, alpha, new Color(sColor), xPnt, yPnt, polling, pollMsec, bckMsec, (fColor == -1 ? null : new Color(fColor)), cyl);
        this.setMinSlider(cMin);
        this.setMaxSlider(cMax);
        this.setNTicks(nT);
        this.setSliderType(st);
        this.setScaleFont(scFont);
        this.setScaleFontColor(new Color(scColor));
        setShowScaleUp(showScUp);
        setShowSubScaleUp(showSubScUp);
        setShowValUp(showValUp);
        setShowScaleDown(showScDown);
        setShowSubScaleDown(showSubScDown);
        setShowValDown(showValDown);
        xPSlider = new int[getNumVertex()];
        yPSlider = new int[getNumVertex()];
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

    void shape_menuItem_actionPerformed(ActionEvent e) {
    }

    /**
       * Restituisce il valore corrente
       * @return il valore corrente
       */
    public double getCurVal() {
        return 0;
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

    public void setSliderType(String st) {
        sliderType = st;
    }

    public String getSliderType() {
        return sliderType;
    }

    public void setNTicks(int n) {
        nTicks = n;
    }

    public int getNTicks() {
        return nTicks;
    }

    public void setScaleFont(Font f) {
        scaleFont = f;
    }

    public Font getScaleFont() {
        Font fnt = new Font(scaleFont.getName(), scaleFont.getStyle(), (int) (scaleFont.getSize() * getZoomFactor()));
        return fnt;
    }

    public Color getScaleFontColor() {
        return scaleFontColor;
    }

    public void setScaleFontColor(Color c) {
        scaleFontColor = c;
    }

    public boolean getShowScaleUp() {
        return showScaleUp;
    }

    public void setShowScaleUp(boolean v) {
        showScaleUp = v;
    }

    public boolean getShowSubScaleUp() {
        return showSubScaleUp;
    }

    public void setShowSubScaleUp(boolean v) {
        showSubScaleUp = v;
    }

    public void setShowValUp(boolean v) {
        showValUp = v;
    }

    public boolean getShowValUp() {
        return showValUp;
    }

    public boolean getShowScaleDown() {
        return showScaleDown;
    }

    public void setShowScaleDown(boolean v) {
        showScaleDown = v;
    }

    public boolean getShowSubScaleDown() {
        return showSubScaleDown;
    }

    public void setShowSubScaleDown(boolean v) {
        showSubScaleDown = v;
    }

    public void setShowValDown(boolean v) {
        showValDown = v;
    }

    public boolean getShowValDown() {
        return showValDown;
    }

    public void setMinSlider(double minSlider) {
        this.minSlider = minSlider;
    }

    public double getMinSlider() {
        return this.minSlider;
    }

    public double getMaxSlider() {
        return this.maxSlider;
    }

    public void setMaxSlider(double maxSlider) {
        this.maxSlider = maxSlider;
    }

    /**
       * Esegue l'aggiornamento del valore corrente dello slider in base al risultato della query associata
       * @return l'oggetto risultato dell'aggiornamento
       */
    public Object updateVal() {
        if (getDataProvider() != null) {
            if (!getDataProvider().t.isRunning()) getDataProvider().startDataRetrieval();
            Object res = getDataProvider().getVal(getQueryId(), ArianneUtil.Util.SLIDER_VAL_POSITION);
            if (res != null) {
                if (valueActual.contains(".")) {
                    if (Float.parseFloat(valueActual) != ((Float) res).floatValue()) setChanges(true);
                } else {
                    if (Integer.parseInt(valueActual) != ((Integer) res).intValue()) setChanges(true);
                }
                valueActual = "" + res;
            }
            return res;
        }
        return null;
    }

    public void drawShape(Graphics2D g, int[] xc, int[] yc, int nV, boolean toDraw) {
        if (((((ArianneViewer.DrawingPanel) getFatherPanel()).getFather()).getOverlayMask() & getOverlay()) > 0) {
            if (getSliderType() != null && getSliderType().equals("Thermometer")) {
                drawPolygon(g, xc, yc, nV, toDraw);
                int d = Math.abs(2 * (yc[0] - yc[3]));
                int xd = (int) Math.round((xc[0] + xc[3]) / 2 - d / 2);
                int yd = (int) Math.round((yc[0] + yc[3]) / 2 - d / 2);
                double alfaR = Math.asin(Math.abs(2.0 * (double) (yd - yc[0]) / d));
                int alfa = (int) Math.round(Math.toDegrees(alfaR));
                drawArc(g, (int) Math.round((double) xd - (double) d / 2.0 * Math.cos(alfaR)), yd, d, d, alfa, 360 - 2 * alfa, toDraw);
            } else {
                drawPolygon(g, xc, yc, nV, toDraw);
            }
        }
    }

    public boolean thereAreChanges() {
        setChanges(false);
        Text resolvedVisExpr = resolve(getVisualizationExpression());
        if (isVisualizationRuleValid(resolvedVisExpr)) verifiedVisualizationRule(resolvedVisExpr);
        if (getChanges()) {
            setLastChangeDrawn(false);
            return true;
        }
        if (getConnection() != null && getSqlQuery() != null && !getSqlQuery().equals("") && !getSqlQuery().equals("null")) {
            updateVal();
        }
        if (getChanges()) {
            setLastChangeDrawn(false);
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g, JPanel p, boolean toDraw) {
        if (((ArianneViewer.DrawingPanel) p).getTimerButton()) {
            elapsed = ((ArianneViewer.DrawingPanel) p).getTimeInMsec() - lastReadMsecElapsed;
            if (elapsed > this.getFatherPanel().getRefreshPeriod()) {
                lastReadMsecElapsed += elapsed;
                Text resolvedVisExpr = resolve(getVisualizationExpression());
                if (isVisualizationRuleValid(resolvedVisExpr)) setVisible(verifiedVisualizationRule(resolvedVisExpr));
                setShapeBorderColor(resultOfBorderColouringRule());
                setShapeFillColor(resultOfFillColouringRule());
                g.setColor(this.getShapeBorderColor());
                if (getConnection() != null && getSqlQuery() != null && !getSqlQuery().equals("") && !getSqlQuery().equals("null")) {
                    updateVal();
                }
                if (!isVisible()) return;
            }
        }
        if (isVisible()) {
            g.setStroke(getCurStroke());
            double valActSlider;
            if (this.valueActual == null) valActSlider = this.getMinSlider(); else valActSlider = Double.parseDouble(this.valueActual.replaceAll(",", "."));
            if (valActSlider > this.maxSlider) valActSlider = this.maxSlider; else if (valActSlider < this.minSlider) valActSlider = this.minSlider;
            double rappSlider = (valActSlider - this.getMinSlider()) / (this.getMaxSlider() - this.getMinSlider());
            setIntCoord();
            for (int i = 0; i < getNumVertex(); i++) {
                xPSlider[i] = getXCoordinates()[i];
                yPSlider[i] = getYCoordinates()[i];
            }
            if (valActSlider <= this.getMinSlider()) {
                xPSlider[1] = getXCoordinates()[0];
                xPSlider[2] = getXCoordinates()[3];
                yPSlider[1] = getYCoordinates()[0];
                yPSlider[2] = getYCoordinates()[3];
            } else if (valActSlider >= this.getMaxSlider()) {
                xPSlider[1] = getXCoordinates()[1];
                xPSlider[2] = getXCoordinates()[2];
                yPSlider[1] = getYCoordinates()[1];
                yPSlider[2] = getYCoordinates()[2];
            } else {
                if (getXCoordinates()[1] > getXCoordinates()[0]) xPSlider[1] = getXCoordinates()[0] + (int) (((getXCoordinates()[1] - getXCoordinates()[0]) * rappSlider) + 1); else xPSlider[1] = getXCoordinates()[0] + (int) ((getXCoordinates()[1] - getXCoordinates()[0]) * rappSlider);
                if (getXCoordinates()[2] > getXCoordinates()[3]) xPSlider[2] = getXCoordinates()[3] + (int) (((getXCoordinates()[2] - getXCoordinates()[3]) * rappSlider) + 1); else xPSlider[2] = getXCoordinates()[3] + (int) ((getXCoordinates()[2] - getXCoordinates()[3]) * rappSlider);
                if (getYCoordinates()[1] > getYCoordinates()[0]) yPSlider[1] = getYCoordinates()[0] + (int) (((getYCoordinates()[1] - getYCoordinates()[0]) * rappSlider) + 1); else yPSlider[1] = getYCoordinates()[0] + (int) ((getYCoordinates()[1] - getYCoordinates()[0]) * rappSlider);
                if (getYCoordinates()[2] > getYCoordinates()[3]) yPSlider[2] = getYCoordinates()[3] + (int) (((getYCoordinates()[2] - getYCoordinates()[3]) * rappSlider) + 1); else yPSlider[2] = getYCoordinates()[3] + (int) ((getYCoordinates()[2] - getYCoordinates()[3]) * rappSlider);
            }
            if (getShapeFillColor() == null) {
                drawPolygon(g, getXCoordinates(), getYCoordinates(), getNumVertex(), toDraw);
            } else {
                g.setColor(getShapeFillColor());
                saveOldComposite(g.getComposite());
                g.setComposite(getCurComposite());
                ArianneUtil.Util.fillShape(getFatherPanel(), g, getShapeFillColor(), getXCoordinates(), getYCoordinates(), xPSlider, yPSlider, getNumVertex(), toDraw, get3D(), isInOverlay());
                g.setComposite(getOldComposite());
                g.setColor(this.getShapeBorderColor());
                drawShape(g, getXCoordinates(), getYCoordinates(), getNumVertex(), toDraw);
                if (isInOverlay()) {
                    int deltaYup = 0;
                    int deltaYdown = 0;
                    int deltaXup = 0;
                    int deltaXdown = 0;
                    ArianneUtil.Util.handleScale(g, getXCoordinates(), getYCoordinates(), getMinSlider(), getMaxSlider(), getNTicks(), getScaleFont(), getScaleFontColor(), getShowScaleUp(), getShowSubScaleUp(), getShowValUp(), getShowScaleDown(), getShowSubScaleDown(), getShowValDown(), deltaXup, deltaYup, deltaXdown, deltaYdown, toDraw);
                }
            }
            setLastChangeDrawn(true);
        }
    }

    public boolean isInsideArea(Point p) {
        return false;
    }

    public boolean isInSelectArea(Point p) {
        Point sPoint;
        Point ePoint;
        for (int i = 0; i < getNumVertex(); i++) {
            setIntCoord();
            sPoint = new Point(getXCoordinates()[i], getYCoordinates()[i]);
            ePoint = new Point(getXCoordinates()[(i + 1) % getNumVertex()], getYCoordinates()[(i + 1) % getNumVertex()]);
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
    }
}
