package org.gocha.gef.glyph;

import java.awt.Graphics2D;
import java.util.ArrayList;
import org.gocha.collection.list.EventListAdapter;
import org.gocha.collection.list.BasicEventListCancel;
import org.gocha.collection.list.EventListArgs;
import org.gocha.collection.list.EventListCancelArgs;
import org.gocha.gef.Dot;
import org.gocha.gef.BodyMoving;
import org.gocha.gef.BorderInfo;
import org.gocha.gef.PropertyChangeListener;
import org.gocha.gef.Line;
import org.gocha.gef.PropertyChangeEvent;
import org.gocha.gef.Rectangle;
import org.gocha.gef.gui.tool.DotsContainer;

/**
 * Глиф - Линия/Полигон
 * @author Камнев Георгий Павлович
 */
public class LineGlyph extends BasicGlyph implements BorderInfo, BodyMoving, DotsContainer {

    /**
	 * Конструктор
	 */
    public LineGlyph() {
        dots = new BasicEventListCancel<Dot>(new ArrayList<Dot>());
        EventListAdapter<Dot> trigger = new EventListAdapter<Dot>() {

            @Override
            protected void inserted(EventListArgs<Dot> evnt) {
                if (evnt == null) return;
                evnt.getItem().addPropertyChangeListener(LineGlyph.this.getDotListener());
                needCalcBorder = true;
                fireBorderChanged();
                fireRepaint();
            }

            @Override
            protected void deleted(EventListArgs<Dot> evnt) {
                if (evnt == null) return;
                evnt.getItem().removePropertyChangeListener(LineGlyph.this.getDotListener());
                needCalcBorder = true;
                fireBorderChanged();
                fireRepaint();
            }

            @Override
            protected void inserting(EventListCancelArgs<Dot> evnt) {
                if (evnt == null) return;
                if (evnt.getItem() == null) {
                    evnt.setCancel(true);
                }
            }
        };
        dots.addEventListCancelListener(trigger);
        dots.addEventListListener(trigger);
        setId("Line");
    }

    private PropertyChangeListener dotListener = null;

    /**
     * Возвращает слушателя точек, который перерисовывает после изменения свойств точки
     * @return Слушатель
     */
    protected PropertyChangeListener getDotListener() {
        if (dotListener == null) {
            dotListener = new PropertyChangeListener() {

                @Override
                public void properyChanged(PropertyChangeEvent e) {
                    needCalcBorder = true;
                    fireBorderChanged();
                    fireRepaint();
                }
            };
        }
        return dotListener;
    }

    /**
     * Возвращает набор точек
     * @return Точки
     */
    @Override
    public java.util.List<Dot> getDots() {
        return dots;
    }

    /**
     * необходимо пересчитывать бордюр
     */
    private boolean needCalcBorder = true;

    /**
     * Точки
     */
    protected BasicEventListCancel<Dot> dots = null;

    /**
     * Линии из точек
     */
    public Line[] getLines() {
        if (getDots().size() < 2) {
            return new Line[] {};
        }
        if (!isClosedLine()) {
            Line[] res = new Line[getDots().size() - 1];
            Dot dStart = getDots().get(0);
            int idx = -1;
            for (Dot d : getDots()) {
                idx++;
                if (idx == 0) {
                    continue;
                }
                Dot dEnd = d;
                res[idx - 1] = new Line(dStart, dEnd);
                dStart = dEnd;
            }
            return res;
        } else {
            return getClosedLines();
        }
    }

    private Line[] getClosedLines() {
        if (getDots().size() < 2) {
            return new Line[] {};
        }
        Line[] res = new Line[getDots().size()];
        Dot dStart = getDots().get(0);
        Dot dEnd = null;
        int idx = -1;
        for (Dot d : getDots()) {
            idx++;
            if (idx == 0) {
                continue;
            }
            dEnd = d;
            res[idx - 1] = new Line(dStart, dEnd);
            dStart = dEnd;
        }
        dStart = getDots().get(0);
        dEnd = getDots().get(getDots().size() - 1);
        res[res.length - 1] = new Line(dStart, dEnd);
        return res;
    }

    private static boolean fillInnerDefault = false;

    /**
     * Возвращает значение по умолчанию - применять заливку
     * @return true - Применять, false - не применять
     */
    public static boolean isFillInnerDefault() {
        return fillInnerDefault;
    }

    /**
     * Устанавливает значение по умолчанию - применять заливку
     * @param fillInnerDefault true - Применять, false - не применять
     */
    public static void setFillInnerDefault(boolean fillInnerDefault) {
        LineGlyph.fillInnerDefault = fillInnerDefault;
    }

    private boolean fillInner = isFillInnerDefault();

    public boolean isFillBody() {
        return fillInner;
    }

    public void setFillBody(boolean fillInner) {
        this.fillInner = fillInner;
        fireRepaint();
    }

    private static boolean closedLineDefault = false;

    /**
     * Возвращает значение по умолчанию - замкнутый полигон
     * @return true - Замнутый, false - не замкнутый
     */
    public static boolean isClosedLineDefault() {
        return closedLineDefault;
    }

    /**
     * Устанавливает значение по умолчанию - замкнутый полигон
     * @param fillInnerDefault true - Замнутый, false - не замкнутый
     */
    public static void setClosedLineDefault(boolean closedLineDefault) {
        LineGlyph.closedLineDefault = closedLineDefault;
    }

    private boolean closedLine = isClosedLineDefault();

    /**
     * Возвращает значение свойства - Замкнутый полигон
     * @return true - Замнутый, false - не замкнутый
     */
    public boolean isClosedLine() {
        return closedLine;
    }

    /**
     * Устанавливает значение свойства - Замкнутый полигон
     * @param closedLine true - Замнутый, false - не замкнутый
     */
    public void setClosedLine(boolean closedLine) {
        this.closedLine = closedLine;
        fireRepaint();
    }

    private static final int firstDotRadius = 6;

    private void drawFill(Graphics2D g) {
        g.setPaint(getFill());
        int dotCo = getDots().size();
        if (dotCo < 2) {
            if (dotCo == 1) {
                Dot d = getDots().get(0);
                g.fillOval((int) d.getX() - firstDotRadius / 2, (int) d.getY() - firstDotRadius / 2, firstDotRadius, firstDotRadius);
            }
            return;
        }
        int[] xdots = new int[getDots().size()];
        int[] ydots = new int[getDots().size()];
        for (int i = 0; i < xdots.length; i++) {
            Dot d = getDots().get(i);
            xdots[i] = (int) d.getX();
            ydots[i] = (int) d.getY();
        }
        g.fillPolygon(xdots, ydots, xdots.length);
    }

    private void drawOutline(Graphics2D g) {
        g.setPaint(getOutlinePaint());
        g.setStroke(getOutlineStroke());
        int dotCo = getDots().size();
        if (dotCo < 2) {
            if (dotCo == 1) {
                Dot d = getDots().get(0);
                g.drawOval((int) d.getX() - firstDotRadius / 2, (int) d.getY() - firstDotRadius / 2, firstDotRadius, firstDotRadius);
            }
            return;
        }
        int idx = -1;
        Line first = null;
        Line last = null;
        for (Line l : getLines()) {
            idx++;
            if (idx == 0) first = l;
            l.draw(g);
            last = l;
        }
        if (isClosedLine()) {
            Line endLine = new Line(first.getBegin(), last.getEnd());
            endLine.draw(g);
        }
    }

    @Override
    protected void paintContent(Graphics2D g) {
        if (getDots().size() < 2) return;
        if (isFillBody()) drawFill(g);
        drawOutline(g);
        if (isSelected()) {
            for (Dot d : getDots()) {
                dot.getTransform().setLeft(d.getX());
                dot.getTransform().setTop(d.getY());
                dot.paint(g);
            }
        }
    }

    private DotGlyph dot = new DotGlyph();

    @Override
    public boolean hitTest(Dot p) {
        return isFillBody() ? hitTestByEvenOdd(p) : hitTestByLinesAndDots(p);
    }

    /**
     * Проверка по правилу попадания в точку или в линию, нахождение точки вутри полигона
     */
    public boolean hitTestByLinesAndDots(Dot p) {
        double err = 5;
        return hitTestByLinesAndDots(p, err);
    }

    /**
     * Проверка по правилу попадания в точку или в линию, нахождение точки вутри полигона
     * @param err - Величина погрешности
     */
    public boolean hitTestByLinesAndDots(Dot p, double err) {
        for (Line l : getLines()) {
            if (l.hitTest(p, err)) {
                return true;
            }
        }
        for (Dot d : getDots()) {
            double r = d.distance(p);
            if (r < err) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверка по правилу Чет/Нечет нахождение точки вутри полигона
     */
    public boolean hitTestByEvenOdd(Dot p) {
        Dot dOutside = new Dot(getLeft() - getWidth() * 2, getTop() - getHeight() * 2);
        Line crossLine = new Line(dOutside, p);
        int countIntersec = 0;
        for (Line polyLine : getClosedLines()) {
            Dot dCross = crossLine.intersection(polyLine);
            if (dCross != null) countIntersec++;
        }
        boolean isOdd = (countIntersec % 2) > 0;
        return isOdd;
    }

    private void calcBorder() {
        if (!needCalcBorder) return;
        double minx = Double.MAX_VALUE;
        double miny = Double.MAX_VALUE;
        double maxx = -(Double.MAX_VALUE);
        double maxy = -(Double.MAX_VALUE);
        for (Dot _dot : this.getDots()) {
            if (_dot.getX() < minx) minx = _dot.getX();
            if (_dot.getX() > maxx) maxx = _dot.getX();
            if (_dot.getY() < miny) miny = _dot.getY();
            if (_dot.getY() > maxy) maxy = _dot.getY();
        }
        left = minx;
        top = miny;
        width = maxx - minx;
        height = maxy - miny;
        needCalcBorder = false;
    }

    private double top = -1;

    /**
	 * @return Верхняя граница объекта
	 */
    private double getTop() {
        calcBorder();
        return top;
    }

    private double left = -1;

    /**
	 * @return Левая граница объекта
	 */
    private double getLeft() {
        calcBorder();
        return left;
    }

    private double width = -1;

    /**
	 * @return Ширина объекта
	 */
    private double getWidth() {
        calcBorder();
        return width;
    }

    private double height = -1;

    /**
	 * @return Высота объекта
	 */
    private double getHeight() {
        calcBorder();
        return height;
    }

    /**
	 * Возвращает центр содержимого
	 * @return Возвращает центр содержимого
	 */
    @Override
    public Dot getCenter() {
        return new Dot(getLeft() + getWidth() / 2, getTop() + getHeight() / 2);
    }

    /**
     * Устанавливает центр содержимого
     * @param dot центр содержимого
     */
    @Override
    public void setCenter(Dot dot) {
        Dot centerOld = getCenter();
        double offX = dot.getX() - centerOld.getX();
        double offY = dot.getY() - centerOld.getY();
        for (int i = 0; i < getDots().size(); i++) {
            Dot d = getDots().get(i);
            d.setX(d.getX() + offX);
            d.setY(d.getY() + offY);
        }
    }

    @Override
    public Rectangle getBorder() {
        return new org.gocha.gef.Rectangle(getLeft(), getTop(), getWidth(), getHeight());
    }
}
