package giis_lab1_rc1.entities;

import giis_lab1_rc1.data.ParametricCurvesBize;
import giis_lab1_rc1.gui.IPlotter;
import giis_lab1_rc1.util.configs;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Нечипуренко Дмитрий 
 * @author Русецкий Кирилл 
 */
public class BezierCurve implements IPlotter {

    private Point p1;
    private Point p2;
    private Point p3;
    private Point p4;
    private List<Point> pixels;
    private ParametricCurvesBize b;
    // список базовых точек
    private List<Point> basePixels;

    public List<Point> getBasePixels() {
        return basePixels;
    }

    /**
     * Конструктор
     * @param p1 первая точка
     * @param p2 вторая точка
     * @param p3 третья точка
     * @param p4 четвертая точка
     */
    public BezierCurve(Point p1, Point p2, Point p3, Point p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.pixels = new ArrayList<>(0);
        this.b = new ParametricCurvesBize(this);
        // запись базовых точек
        this.basePixels = new ArrayList<>(0);
        this.basePixels.add(getPointInField(p1.x, p1.y));
        this.basePixels.add(getPointInField(p2.x, p2.y));
        this.basePixels.add(getPointInField(p3.x, p3.y));
        this.basePixels.add(getPointInField(p4.x, p4.y));

    }

    /**
     * Отрисовка кривой
     */
    public void draw() {
        getPixels().clear();
        b.drawBize(p1, p2, p3, p4);
    }

    /**
     * @return the p1
     */
    public Point getP1() {
        return p1;
    }

    /**
     * @param p1 the p1 to set
     */
    public void setP1(Point p1) {
        this.p1 = p1;
    }

    /**
     * @return the p2
     */
    public Point getP2() {
        return p2;
    }

    /**
     * @param p2 the p2 to set
     */
    public void setP2(Point p2) {
        this.p2 = p2;
    }

    /**
     * @return the p3
     */
    public Point getP3() {
        return p3;
    }

    /**
     * @param p3 the p3 to set
     */
    public void setP3(Point p3) {
        this.p3 = p3;
    }

    /**
     * @return the p4
     */
    public Point getP4() {
        return p4;
    }

    /**
     * @param p4 the p4 to set
     */
    public void setP4(Point p4) {
        this.p4 = p4;
    }

    @Override
    public void PlotPixel(int x, int y) {
        getPixels().add(getPointInField(x, y));
    }

    // получение точки с новыми координатами на поле
    private Point getPointInField(int x, int y) {
        return new Point(x + configs.sizeX / 2, configs.sizeY / 2 - y);
    }

    @Override
    public void setPlotColor(Color plotColor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPlotLuminosity(float l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the pixels
     */
    public List<Point> getPixels() {
        return pixels;
    }
}
