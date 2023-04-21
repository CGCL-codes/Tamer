package giis_lab1_rc1.gui;

import giis_lab1_rc1.data.CircleAlgo;
import giis_lab1_rc1.data.Clipping_Cohen_Sutherland;
import giis_lab1_rc1.data.Clipping_Cyrus_Beck;
import giis_lab1_rc1.data.CurveAlgo;
import giis_lab1_rc1.data.ShapeAlgo;
import giis_lab1_rc1.data.LineAlgo;
import giis_lab1_rc1.data.ParametricCurvesBSpline;
import giis_lab1_rc1.data.ParametricCurvesBize;
import giis_lab1_rc1.data.ParametricCurvesHermite;
import giis_lab1_rc1.data.matrix.AffineTransforms;
import giis_lab1_rc1.data.matrix.PointMatrix;
import giis_lab1_rc1.entities.BezierCurve;
import giis_lab1_rc1.util.Matrix;
import giis_lab1_rc1.util.Utils;
import giis_lab1_rc1.util.configs;
import giis_lab1_rc1.util.objects3D.Plane;
import giis_lab1_rc1.util.objects3D.Point3D;
import giis_lab1_rc1.util.voronoi.Pnt;
import giis_lab1_rc1.util.voronoi.Triangle;
import giis_lab1_rc1.util.voronoi.Triangulation;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/**
 * @author Нечипуренко Дмитрий 
 * @author Русецкий Кирилл 
 */
public class DrawField extends JPanel implements IPlotter {

    private Map<Point, Color> pixels = new HashMap<>(0);
    private int cellSize;
    private int gridRows;
    private int gridCols;
    private Color basePlotColor;
    private Color actualPlotColor;
    private LineAlgo lineAlgo;
    private CurveAlgo curveAlgo;
    private boolean gridEnabled;
    private CircleAlgo circleAlgo;
    private ParametricCurvesHermite hermiteAlgo;
    private ParametricCurvesBize bizeAlgo;
    private ParametricCurvesBSpline bSplineAlgo;
    private List<BezierCurve> beziers;
    private ShapeAlgo shapeAlgo;
    
    private Clipping_Cohen_Sutherland clipping;
    private Clipping_Cyrus_Beck clippingKB;

    /**
     * Конструктор поля 
     * @param cellSize размер ячейки
     * @param cols количество столбцов
     * @param rows количество строк
     */
    public DrawField(int cellSize, int cols, int rows) {
        super();
        this.cellSize = cellSize;
        gridRows = rows;
        gridCols = cols;
        setBackground(Color.WHITE);
        adaptSize();
        beziers = new ArrayList<>(0);
        shapeAlgo = new ShapeAlgo(this);
        clipping = new Clipping_Cohen_Sutherland(this);
        clippingKB = new Clipping_Cyrus_Beck(this);
        
        colorTable = new HashMap<>(); // таблица цветов для диаграммы вороного
        tr = new Triangulation(t);
    }

    private void adaptSize() {
        final Dimension d = new Dimension(gridCols * cellSize, gridRows * cellSize);
        setPreferredSize(d);
        setMaximumSize(d);
        setMinimumSize(d);
        setSize(d);
        revalidate();
    }

    public void addBezier(BezierCurve bc) {
        beziers.add(bc);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (gridEnabled) {
            drawGrid(g);
        }
        drawAxis(g);

        for (Point p : pixels.keySet()) {
            g.setColor(pixels.get(p));
            Rectangle r = cellRect(p.x, p.y);
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        for (BezierCurve bc : beziers) {
            bc.draw();
            for (Point p : bc.getPixels()) {
                g.setColor(Color.BLACK);
                Rectangle r = cellRect(p.x, p.y);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
            // отрисовываем базовые точки
            for (Point p : bc.getBasePixels()) {
                g.setColor(Color.red);
                Rectangle r = cellRect(p.x, p.y);
                g.fillRect(r.x, r.y, r.width, r.height);
                g.setColor(Color.BLACK);
            }
        }
        // отрисовка диаграммы вороного
        if (voronoi) {
            drawAllVoronoi(g, voronoiC);
        }
    }

    /**
     * Получает прямоугольник в стандартной системе координат JPanel, 
     * соответствующий точке в СК сетки с заданным размером ячейки
     * @param x абсцисса точки
     * @param y ордината точки
     * @return прямоугольник в системе координат JPanel
     */
    private Rectangle cellRect(int x, int y) {
        if (gridEnabled) {
            return new Rectangle(x * getCellSize() + 1, y * getCellSize() + 1,
                    getCellSize() - 1, getCellSize() - 1);
        } else {
            return new Rectangle(x * getCellSize(), y * getCellSize(),
                    getCellSize(), getCellSize());
        }
    }

    /**
     * Получение точки в новой системе координат
     * @param x значение Х в старой системе
     * @param y значение У в старой системе
     * @return новая точка
     */
    public Point getCellCoordFromPoint(int x, int y) {
        double cellX = (double) x / getCellSize();
        double cellY = (double) y / getCellSize();
        cellX = Math.floor(cellX);
        cellY = Math.floor(cellY);
        int xr = (int) cellX - getGridCols() / 2;
        int yr = getGridRows() / 2 - (int) cellY;
        return new Point(xr, yr);
    }

    private void drawGrid(Graphics g) {
        int dx, dy, x0, y0, xm, ym;
        dx = dy = getCellSize();
        Color c = g.getColor();
        g.setColor(Color.GRAY);
        Rectangle clp = g.getClipBounds();

        if (clp != null) {
            // координата x первой вертикальной линии, попадающей в 
            // прямоугольник перерисовки
            x0 = Utils.ceilToMultiple(clp.x, cellSize);
            // координата x последней вертикальной линии, попадающей в 
            // прямоугольник перерисовки
            xm = Utils.floorToMultiple(clp.x + clp.width, cellSize);

            // аналогично для горизонтальных линий сетки
            y0 = Utils.ceilToMultiple(clp.y, cellSize);
            ym = Utils.floorToMultiple(clp.y + clp.height, cellSize);
        } else {
            // полная перерисовка
            xm = cellSize * gridCols;
            ym = cellSize * gridRows;
            x0 = y0 = 0;
            clp = new Rectangle(xm, ym);
        }

        for (int x = x0; x <= xm; x += dx) {
            g.drawLine(x, clp.y, x, (int) clp.getMaxY());
        }

        for (int y = y0; y <= ym; y += dy) {
            g.drawLine(clp.x, y, (int) clp.getMaxX(), y);
        }

        g.setColor(c);
    }

    private void drawAxis(Graphics g) {

        Color c = g.getColor();

        int xm = getCellSize() * getGridCols();
        int ym = getCellSize() * getGridRows();

        g.setColor(Color.RED);
        int x = xm / 2;
        g.drawLine(x, 0, x, ym);
        g.drawLine(x, 0, x + 3, 5);
        g.drawLine(x, 0, x - 3, 5);

        int y = ym / 2;
        g.drawLine(0, y, xm, y);
        g.drawLine(xm, y, xm - 5, y - 3);
        g.drawLine(xm, y, xm - 5, y + 3);

        g.setColor(c);

    }

    /**
     * Отрисовка пикселя
     * @param x координата Х
     * @param y координата У
     */
    @Override
    public void PlotPixel(int x, int y) {
        int xr = x + getGridCols() / 2;
        int yr = getGridRows() / 2 - y;
        pixels.put(new Point(xr, yr), actualPlotColor);
//        Graphics g = getGraphics();
//        if (g != null){
//            Rectangle r = cellRect(xr,yr);
//            g.setClip(r.x, r.y, r.width, r.height);
//            paint(g);
//        }
        repaint(cellRect(xr, yr));
    }

    /**
     * получение размера ячейки
     * @return размер ячейки
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * установка размера яцейки
     * @param cellSize новый размер ячейки
     */
    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
        adaptSize();
        repaint();
    }

    public int getGridRows() {
        return gridRows;
    }

    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
        adaptSize();
        repaint();
    }

    public int getGridCols() {
        return gridCols;
    }

    public void setGridCols(int gridCols) {
        this.gridCols = gridCols;
        adaptSize();
        repaint();
    }

    /**
     * Получение цвета закрашивания
     * @return цвет закрашивания
     */
    public Color getPlotColor() {
        return basePlotColor;
    }

    /**
     * Установка цвета закрашивания
     * @param plotColor цвет закрашивания
     */
    @Override
    public void setPlotColor(Color plotColor) {
        this.basePlotColor = plotColor;
        actualPlotColor = plotColor;
    }

    /**
     * Установка инетенсивности цвета
     * @param l
     */
    @Override
    public void setPlotLuminosity(float l) {
        actualPlotColor = adjustLuminosity(basePlotColor, l);
    }

    private Color adjustLuminosity(Color c, float l) {
        int r = (int) ((255 - c.getRed()) * (1.0 - l));
        int g = (int) ((255 - c.getGreen()) * (1.0 - l));
        int b = (int) ((255 - c.getBlue()) * (1.0 - l));
        return new Color(r, g, b);
    }

    /**
     * Получения алгоритма для линий
     * @return линейный алгоритма
     */
    public LineAlgo getLineAlgo() {
        return lineAlgo;
    }

    /**
     * Установка алгоритма для линий
     * @param lineAlgo
     */
    public void setLineAlgo(LineAlgo lineAlgo) {
        this.lineAlgo = lineAlgo;
    }

    /**
     * Функция отрисовки линии
     * @param p1 точка начала линии 
     * @param p2 точка окончания линии
     */
    public void drawLine(Point p1, Point p2) {
        lineAlgo.drawLine(p1, p2);
    }

    /**
     * Отрисовка окружности
     * @param p
     */
    public void drawCircle(Point p, int r) {
        circleAlgo.drawCircle(p, r);
    }

    public CircleAlgo getCircleAlgo() {
        return circleAlgo;
    }

    public void setCircleAlgo(CircleAlgo circleAlgo) {
        this.circleAlgo = circleAlgo;
    }

    /**
     * @return the gridEnabled
     */
    public boolean isGridEnabled() {
        return gridEnabled;
    }

    /**
     * @param gridEnabled the gridEnabled to set
     */
    public void setGridEnabled(boolean gridEnabled) {
        this.gridEnabled = gridEnabled;
        repaint();
    }

    public CurveAlgo getCurveAlgo() {
        return curveAlgo;
    }

    public void setCurveAlgo(CurveAlgo curveAlgo) {
        this.curveAlgo = curveAlgo;
    }

    public void drawCurve(Point p, int a, int b) {
        curveAlgo.drawCurve(p, a, b);
    }

    public void setHermiteAlgo(ParametricCurvesHermite hermiteAlgo) {
        this.hermiteAlgo = hermiteAlgo;
    }

    public ParametricCurvesHermite getHermiteAlgo() {
        return hermiteAlgo;
    }

    public void drawHermite(Point p1, Point p4, Dimension r1, Dimension r4) {
        hermiteAlgo.drawHermite(p1, p4, r1, r4);
    }

    public void clearField() {
        pixels.clear();     //  очистка списка пикселей
        beziers.clear();    //  очистка бизье
        clearVoronoi();     //  очистка вороного
        Utils.logClear();   //  очистка лога
        adaptSize();
        repaint();
    }

    public void clearPixel(ArrayList lst) {
        int xr = 0;
        int yr = 0;
        Point p = null;
        for (int i = 0; i < lst.size(); i++) {
            p = (Point) lst.get(i);
            xr = p.x + getGridCols() / 2;
            yr = getGridRows() / 2 - p.y;
            pixels.remove(new Point(xr, yr));
        }
        adaptSize();
        repaint();
    }

    public ParametricCurvesBize getBizeAlgo() {
        return bizeAlgo;
    }

    public void setBizeAlgo(ParametricCurvesBize bizeAlgo) {
        this.bizeAlgo = bizeAlgo;
    }

    public void drawBize(Point p1, Point p2, Point p3, Point p4) {
        bizeAlgo.drawBize(p1, p2, p3, p4);
    }

    public ParametricCurvesBSpline getbSplineAlgo() {
        return bSplineAlgo;
    }

    public void setbSplineAlgo(ParametricCurvesBSpline bSplineAlgo) {
        this.bSplineAlgo = bSplineAlgo;
    }

    /**
     * Отрисовываем Б-сплайн
     * @param p множество точек
     */
    public void drawBSpline(ArrayList<Point> p) {
        bSplineAlgo.drawBSpline(p);
    }

    /**
     * Получение базовых точек кривой Безье
     * @param bc - идентификатор кривой
     * @return список базовых точек
     */
    public ArrayList<Point> getBezierBasePixels(int bc) {
        return (ArrayList<Point>) beziers.get(bc).getBasePixels();
    }

    /**
     * Получение количества кривых Безье
     * @return количество кривых
     */
    public int getBeziersSize() {
        return beziers.size();
    }

    /**
     * Получение новой точки ??
     * @param x - координата х
     * @param y - координата у
     * @return новая точка
     */
    public Point getPointInField(int x, int y) {
        return new Point(x + configs.sizeX / 2, configs.sizeY / 2 - y);
    }

    /**
     * Получение точки на сетке (обратные координаты)
     * @param p точка в экранном режиме
     * @return точка на сетке
     */
    public Point getInvertPointOnField(Point p) {
        int x = p.x;
        int y = p.y;
        int x1 = x - getGridCols() / 2;
        int y1 = configs.sizeY / 2 - y;
        return new Point(x1, y1);
    }

    /**
     * Удаление кривой Безье
     * @param bc идентификатор кривой
     */
    public void deleteBezierCurve(int bc) {
        beziers.remove(bc);
        adaptSize();
        repaint();
    }

    /**
     * Функция обновление кривой Безье
     * @param id идентификатор кривой которую заменяем
     * @param bc новая кривая
     */
    public void updateBezierCurve(int id, BezierCurve bc) {
        beziers.set(id, bc);
        adaptSize();
        repaint();
    }

    /**
     * Функция отрисовки 3Д фигуры
     * @param shape 
     */
    public void drawShapeAlgo(giis_lab1_rc1.util.Shape shape, boolean debug) {
        getShapeAlgo().setDebug(debug);
        getShapeAlgo().drawShape(shape);
        
//        Point3D pl = new Point3D(shapeAlgo.getS().getPoints().get(1));
//        Point3D pn = new Point3D(shapeAlgo.getS().getPoints().get(7));
//        
//        Point3D pp1 = new Point3D(-7.0F, 5.0F, 2.0F);
//        Point3D pp2 = new Point3D(7.0F, 5.0F, 2.0F);
        
        //getClipping().draw(pl, pn, pp1, pp2);
    }

    public void drawShapeAlgo(giis_lab1_rc1.util.Shape shape) {
        getShapeAlgo().drawShape(shape);
    }
    
    public void drawShapeAlgo(giis_lab1_rc1.util.Shape shape, boolean debug, boolean hide){
        getShapeAlgo().setDebug(debug);
        getShapeAlgo().drawShape(shape, hide);
    }

    /**
     * @return the shapeAlgo
     */
    public ShapeAlgo getShapeAlgo() {
        return shapeAlgo;
    }

    /**
     * @param shapeAlgo the shapeAlgo to set
     */
    public void setShapeAlgo(ShapeAlgo shapeAlgo) {
        this.shapeAlgo = shapeAlgo;
    }

    public List<Point3D> lastGoodPoint = new ArrayList<>();
    public boolean needGoodPoint = false;
    
    public boolean check_Z(List<Point3D> points){
        for(Point3D p : points){
            if(p.getZ() <= 0.0F){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Функция выполняющая трансформацию 3Д объекта
     * @param move
     * @param scale 
     * @param rotate
     */
    public void ShapeTransform(Point3D move, Point3D scale, Point3D rotate,
            boolean debug,
            boolean perspective,
            boolean homogenous,
            boolean hide,
            Point3D pline1,
            Point3D pline2,
            boolean line) {

        //Матрица перемещения
        Matrix moveM = AffineTransforms.move(move);
        //Матрица скалирования
        Matrix scaleM = AffineTransforms.scale3d(scale.getX(), scale.getY(), scale.getZ());
        Matrix scaleMS = AffineTransforms.scale3d(scale.getX());
        //Матрица поворота
        Matrix rollM = AffineTransforms.rotate(rotate.getX(), rotate.getY(), rotate.getZ());
        //получение точек фигуры
        List<Point3D> points = shapeAlgo.getS().getPoints();
        //плоскость фигуры
        List<Plane> plane = shapeAlgo.getS().getPlane();
        
        if(lastGoodPoint.isEmpty()){
            for (int i = 0; i < points.size(); i++) {
                Point3D point = new Point3D(points.get(i));
                lastGoodPoint.add(i, point);
            }
        }

        /*
         * Данные для определения центра масс фигуры
         */
        float cx = 0.0F;
        float cy = 0.0F;
        float cz = 0.0F;
        float n = points.size();

        for (int i = 0; i < n; i++) {
            cx += points.get(i).getX();
            cy += points.get(i).getY();
            cz += points.get(i).getZ();
        }
        // точка центр масс
        Point3D center = new Point3D(cx / n, cy / n, cz / n);
        
        /**
         * Цикл трансформации
         */
        for (int i = 0; i < points.size(); i++) {
            // получение точки фигуры
            Point3D point = points.get(i);
            // центрирование полученной точки
            point.minus(center);
            // матрица строка точки
            PointMatrix p = new PointMatrix(point);
            // умножение матрицы строки на матрицу скалирования
            Matrix temp;
            if(homogenous){
                temp  = Matrix.multiply(p, scaleMS);
            }
            else{
               temp  = Matrix.multiply(p, scaleM);
            }
            // умножение на матрицу поворота
            temp = Matrix.multiply(temp, rollM);
            // умножение на матрицу перемещения
            temp = Matrix.multiply(temp, moveM);
            // матрица строка полученных точки
            PointMatrix res = new PointMatrix(temp);
            // получаем точку с учетом маштабного коэффициента
            point = new Point3D(res.getPoint());
            // получаем первоначальное положение точки
            point.plus(center);
            // обновляем список точек фигуры
            points.get(i).replace(point);
        }
        
        // подсчет параметров фигуры
        for(int i = 0; i < plane.size(); i++){
            plane.get(i).calcParams(shapeAlgo.getS());
        }
        
        if(!check_Z(points)){
            for (int i = 0; i < points.size(); i++) {
                Point3D point = new Point3D(points.get(i));
                lastGoodPoint.get(i).replace(point);
            }
        }
        else{
            needGoodPoint = true;
        }
        
        giis_lab1_rc1.util.Shape shape = new giis_lab1_rc1.util.Shape(points, shapeAlgo.getS().getArcs(), plane);
        
        clearField();
        
        if (!perspective) {
            // прячем грани
            if(hide){
                drawShapeAlgo(shape, debug, hide);
            }
            else{
                Point3D pl = new Point3D(points.get(1));
                Point3D pn = new Point3D(points.get(7));
                if(line){
//                    drawClippingAlgo(pl,pn,pline1,pline2);
                    drawClippingAlgoKB(pline1, pline2);
                }
                drawShapeAlgo(shape, debug);
            }
        } 
        else {
            if(needGoodPoint){
                ShapePerspective(lastGoodPoint); 
            }
            else{
                ShapePerspective(points); 
            }
        }
    }

    /**
     * Пока тестово и вроде работает
     */
    public void ShapePerspective(List<Point3D> points) {
        Matrix per = AffineTransforms.perspective(5.0F);
        //Matrix per = AffineTransforms.perspectiveAngle(1.0F, 45.0F);  // проекции Кавалье l = 1, поэтому угол b = 45°
        //Matrix per = AffineTransforms.perspectiveAngle(0.5F, 63.4F);  //проекции Кабине l=½, а b = arctg(2) = 63,4°
        
        List<Point3D> pointsp = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            Point3D point = points.get(i);
            PointMatrix p = new PointMatrix(point);
            Matrix temp = Matrix.multiply(p, per);
            PointMatrix res = new PointMatrix(temp);
            point = new Point3D(res.getPoint());
            pointsp.add(i, point);
        }

        clearField();

        for (int i = 0; i < shapeAlgo.getS().getArcs().size(); i++) {
            this.drawLine(
                    new Point(
                    (int) pointsp.get(shapeAlgo.getS().getArcs().get(i).getFirst() - 1).getX(),
                    (int) pointsp.get(shapeAlgo.getS().getArcs().get(i).getFirst() - 1).getY()),
                    new Point(
                    (int) pointsp.get(shapeAlgo.getS().getArcs().get(i).getSecond() - 1).getX(),
                    (int) pointsp.get(shapeAlgo.getS().getArcs().get(i).getSecond() - 1).getY()));
        }
    }
///////////////////////////////////////////////////////////////////////////////////
    Triangle t = new Triangle(
            new Pnt(-10000, -10000),
            new Pnt(10000, -10000),
            new Pnt(0, 10000));
    Triangulation tr;
    boolean voronoi = false;    // разрешение на отрисовку диаграммы Вороного
    boolean voronoiC = false;   // разрешение на использование цветовой закраски
    private Random random = new Random();   
    private Map<Object, Color> colorTable; //карта для хранения цвета 

    /**
     * Добавляем точку к диаграмме Вороного
     * @param point точка
     */
    public void addPoint(Pnt point) {
        tr.delaunayPlace(point);
    }
    /**
     * Очистка диаграммы Вороного;
     * создаем новую 
     */
    public void clearVoronoi() {
        tr = new Triangulation(t);
    }

    /**
     * Непосредственная отрисовка элементов диаграммы
     * @param g
     * @param polygon точки описывающие полигон
     * @param fillColor цвет полигона
     */
    public void drawVoronoi(Graphics g, Pnt[] polygon, Color fillColor) {
        int[] x = new int[polygon.length];
        int[] y = new int[polygon.length];
        for (int i = 0; i < polygon.length; i++) {
            x[i] = (int) polygon[i].coord(0);
            y[i] = (int) polygon[i].coord(1);
        }
        if (fillColor != null) {
            Color temp = g.getColor();
            g.setColor(fillColor);
            g.fillPolygon(x, y, polygon.length);
            g.setColor(temp);
        }
        g.drawPolygon(x, y, polygon.length);
    }

    /**
     * Отрисовка точки диаграммы Вороного
     * @param g
     * @param point точка
     */
    private void drawVoronoiPoint(Graphics g, Pnt point) {
        int r = 3;
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.fillOval(x-r, y-r, r+r, r+r);
    }

    /**
     * Нарисовать диаграмму Вороного
     * @param g 
     * @param color разрешение раскрашивать
     */
    private void drawAllVoronoi(Graphics g, boolean color) {
        HashSet<Pnt> done = new HashSet<>(t);
        for (Triangle triangle : tr) {
            for (Pnt site : triangle) {
                if (done.contains(site)) {
                    continue;
                }
                done.add(site);

                List<Triangle> list = tr.surroundingTriangles(site, triangle);
                Pnt[] vertices = new Pnt[list.size()];
                int i = 0;

                for (Triangle tri : list) {
                    vertices[i++] = tri.getCircumcenter();
                }

                drawVoronoi(g, vertices, color ? getColor(site) : null);
                drawVoronoiPoint(g, site);
            }
        }
    }
    
    /**
     * Получение цвета многоугольника для диаграммы Вороного
     * @param item объект
     * @return цвет многоугольника
     */
    private Color getColor(Object item) {
        if (colorTable.containsKey(item)) {
            return colorTable.get(item);
        }
        Color color = new Color(Color.HSBtoRGB(random.nextFloat(), 1.0f, 1.0f));
        colorTable.put(item, color);
        return color;
    }

    /**
     * Разрешение для отрисовки/перерисовки диаграммы Вороного
     * Цветное или бесцветное
     * @param color разрешить цвет
     */
    public void enableDrawVoronoi(boolean color) {
        this.voronoi = true;
        this.voronoiC = color;
    }

    /**
     * Запрет отрисовки/перерисовки диаграммы Вороного
     */
    public void disableDrawVoronoi() {
        this.voronoi = false;
    }

    public Clipping_Cohen_Sutherland getClipping() {
        return clipping;
    }

    public void setClipping(Clipping_Cohen_Sutherland clipping) {
        this.clipping = clipping;
    }
    
    public void drawClippingAlgo(Point3D p1, Point3D p2, Point3D pp1, Point3D pp2){
        clearField();
        getClipping().draw(p1, p2, pp1, pp2);
    }
    
    public void drawClippingAlgo(Point3D pp1, Point3D pp2){
        clearField();
        Point3D p1 = new Point3D(shapeAlgo.getS().getPoints().get(1));
        Point3D p2 = new Point3D(shapeAlgo.getS().getPoints().get(7));
        getClipping().draw(p1, p2, pp1, pp2);
        drawShapeAlgo(shapeAlgo.getS());
    }

    public Clipping_Cyrus_Beck getClippingKB() {
        return clippingKB;
    }

    public void setClippingKB(Clipping_Cyrus_Beck clippingKB) {
        this.clippingKB = clippingKB;
    }
    
    /**
     * Отрисовка используя алгоритм Кируса-Бека
     * @param p1 точка начала линии
     * @param p2 точка окончания линии
     */
    public void drawClippingAlgoKB(Point3D p1, Point3D p2){
        getClippingKB().draw(p1, p2, shapeAlgo.getS());
    }
    
}