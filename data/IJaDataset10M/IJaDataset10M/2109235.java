package org.gocha.gef;

import java.util.ArrayList;

/**
 * Точка в пространстве
 * @author gocha
 */
public class Dot implements PropertyChangeSender {

    /**
     * Конструктор точки
     */
    public Dot() {
    }

    /**
     * Конструктор декартовых координат из полярных
     * @param coord Полярная координата
     */
    public Dot(Coord coord) {
        if (coord == null) {
            throw new IllegalArgumentException("coord == null");
        }
        this.x = Math.cos(coord.getAngle()) * coord.getLength();
        this.y = Math.sin(coord.getAngle()) * coord.getLength();
    }

    /**
     * Конструктор точки
     * @param x Координата x
     * @param y Координата y
     */
    public Dot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Конструктор копирования
     * @param point точка
     */
    public Dot(java.awt.Point point) {
        if (point != null) {
            this.x = point.x;
            this.y = point.y;
        }
    }

    /**
     * Конструктор копирования точки
     * @param src Исходная точка
     */
    public Dot(Dot src) {
        if (src != null) {
            this.x = src.x;
            this.y = src.y;
        }
    }

    private double x = 0;

    private double y = 0;

    private ArrayList<PropertyChangeListener> propListeners = new ArrayList<PropertyChangeListener>();

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        propListeners.add(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propListeners.remove(listener);
    }

    /**
     * Вызывает слушателей свойств
     * @param evt сообщение
     */
    protected void fireChanged(PropertyChangeEvent evt) {
        for (PropertyChangeListener l : propListeners) {
            l.properyChanged(evt);
        }
    }

    /**
     * Возвращает координату x
     * @return Координата x
     */
    public double getX() {
        return x;
    }

    /**
     * Устанавливает координату x
     * @param x Координата x
     */
    public void setX(double x) {
        Object old = this.x;
        this.x = x;
        fireChanged(new PropertyChangeEvent(this, "x", old, x));
    }

    /**
     * Возвращает координату y
     * @return Координата y
     */
    public double getY() {
        return y;
    }

    /**
     * Устанавливает координату y
     * @param y Координата y
     */
    public void setY(double y) {
        Object old = this.y;
        this.y = y;
        fireChanged(new PropertyChangeEvent(this, "y", old, y));
    }

    /**
     * Масштабирование точки
     * @param d Исходная точка
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     * @return Пребразованная точка
     */
    public static Dot processScale(Dot d, Transform g, boolean forward) {
        Dot r = new Dot(d);
        if (forward) {
            r.setX(g.getScaleX() == 0 ? 0 : r.getX() / g.getScaleX());
            r.setY(g.getScaleY() == 0 ? 0 : r.getY() / g.getScaleY());
        } else {
            r.setX(r.getX() * g.getScaleX());
            r.setY(r.getY() * g.getScaleY());
        }
        return r;
    }

    /**
     * Масштабирование точки
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     * @return Пребразованная точка
     */
    public Dot processScale(Transform g, boolean forward) {
        return processScale(this, g, forward);
    }

    /**
     * Масштабирование точки
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     */
    public void applyScale(Transform g, boolean forward) {
        Dot d = processScale(this, g, forward);
        setX(d.getX());
        setY(d.getY());
    }

    /**
     * Вращение точки
     * @param d Исходная точка
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     * @return Пребразованная точка
     */
    public static Dot processRotate(Dot d, Transform g, boolean forward) {
        Dot r = new Dot(d);
        if (forward) {
            double a = -g.getAngle();
            double nx = (r.getX() * Math.cos(a)) - (r.getY() * Math.sin(a));
            double ny = (r.getX() * Math.sin(a)) + (r.getY() * Math.cos(a));
            r.setX(nx);
            r.setY(ny);
        } else {
            double a = g.getAngle();
            double nx = (r.getX() * Math.cos(a)) - (r.getY() * Math.sin(a));
            double ny = (r.getX() * Math.sin(a)) + (r.getY() * Math.cos(a));
            r.setX(nx);
            r.setY(ny);
        }
        return r;
    }

    /**
     * Вращение точки
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     * @return Пребразованная точка
     */
    public Dot processRotate(Transform g, boolean forward) {
        return processRotate(this, g, forward);
    }

    /**
     * Вращение точки
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     */
    public void applyRotate(Transform g, boolean forward) {
        Dot d = processRotate(g, forward);
        setX(d.getX());
        setY(d.getY());
    }

    /**
     * Перемещение точки
     * @param d Исходная точка
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     * @return Пребразованная точка
     */
    public static Dot processTranslate(Dot d, Transform g, boolean forward) {
        Dot r = new Dot(d);
        if (forward) {
            r.setX(r.getX() - g.getLeft());
            r.setY(r.getY() - g.getTop());
        } else {
            r.setX(r.getX() + g.getLeft());
            r.setY(r.getY() + g.getTop());
        }
        return r;
    }

    /**
     * Перемещение точки
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     * @return Пребразованная точка
     */
    public Dot processTranslate(Transform g, boolean forward) {
        return processTranslate(this, g, forward);
    }

    /**
     * Перемещение точки
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     */
    public void applyTranslate(Transform g, boolean forward) {
        Dot p = processTranslate(g, forward);
        setX(p.getX());
        setY(p.getY());
    }

    /**
     * Применеие преобразований точки (Масштабирование, Вращения, Перемещения)
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     */
    public void applyTransform(Transform g, boolean forward) {
        Dot p = processTransform(g, forward);
        setX(p.getX());
        setY(p.getY());
    }

    /**
     * Применеие преобразований точки (Масштабирование, Вращения, Перемещения)
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     * @return Пребразованная точка
     */
    public Dot processTransform(Transform g, boolean forward) {
        return processTransform(this, g, forward);
    }

    /**
     * Применеие преобразований точки (Масштабирование, Вращения, Перемещения)
     * @param p Исходная точка
     * @param g Преобразования
     * @param forward Прямое (true) преобразование или обратное (false)
     * @return Пребразованная точка
     */
    public static Dot processTransform(Dot p, Transform g, boolean forward) {
        if (p == null || g == null) {
            return null;
        }
        Dot r = new Dot(p);
        if (forward) {
            for (int i = 0; i < g.getListTransform().length; i++) {
                switch(g.getListTransform()[i]) {
                    case translate:
                        r.setX(r.getX() - g.getLeft());
                        r.setY(r.getY() - g.getTop());
                        break;
                    case rotate:
                        double a = -g.getAngle();
                        double nx = (r.getX() * Math.cos(a)) - (r.getY() * Math.sin(a));
                        double ny = (r.getX() * Math.sin(a)) + (r.getY() * Math.cos(a));
                        r.setX(nx);
                        r.setY(ny);
                        break;
                    case scale:
                        r.setX(g.getScaleX() == 0 ? 0 : r.getX() / g.getScaleX());
                        r.setY(g.getScaleY() == 0 ? 0 : r.getY() / g.getScaleY());
                        break;
                }
            }
        } else {
            for (int i = g.getListTransform().length - 1; i >= 0; i--) {
                switch(g.getListTransform()[i]) {
                    case translate:
                        r.setX(r.getX() + g.getLeft());
                        r.setY(r.getY() + g.getTop());
                        break;
                    case rotate:
                        double a = g.getAngle();
                        double nx = (r.getX() * Math.cos(a)) - (r.getY() * Math.sin(a));
                        double ny = (r.getX() * Math.sin(a)) + (r.getY() * Math.cos(a));
                        r.setX(nx);
                        r.setY(ny);
                        break;
                    case scale:
                        r.setX(r.getX() * g.getScaleX());
                        r.setY(r.getY() * g.getScaleY());
                        break;
                }
            }
        }
        return r;
    }

    /**
     * Вовщащает текстовое представление точки
     * @return Текстовое представление
     */
    @Override
    public String toString() {
        return String.format("<X=%1$.3f;Y=%2$.3f>", x, y);
    }

    /**
     * Добавляет смещение заданное другой точкой
     * @param dot Точка
     * @return Новая точка
     */
    public Dot add(Dot dot) {
        if (dot == null) {
            throw new IllegalArgumentException("dot==null");
        }
        return new Dot(this.x + dot.x, this.y + dot.y);
    }

    /**
     * Добавляет смещение заданное другой точкой
     * @param dot Точка
     * @return Новая точка
     */
    public Dot sub(Dot dot) {
        if (dot == null) {
            throw new IllegalArgumentException("dot==null");
        }
        return new Dot(this.x - dot.x, this.y - dot.y);
    }

    /**
     * Инвертирует координаты
     * @return Инвертированная точка
     */
    public Dot invert() {
        return new Dot(-x, -y);
    }

    /**
     * Возвращает дистанцию до точки
     * @param d Точка
     * @return Растояние
     */
    public double distance(Dot d) {
        if (d == null) {
            throw new IllegalArgumentException("d==null");
        }
        double dx = x - d.x;
        double dy = y - d.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Возвращает расстояние от начала координат до точки
     * @return Расстояние
     */
    public double distance() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Преобразовывает точку из глобальных координат в локальную для взятого глифа
     * @param glyph Глиф
     * @return Преобразованная точка
     */
    public Dot toLocal(Glyph glyph) {
        if (glyph == null) throw new IllegalArgumentException("glyph == null");
        Dot dotLocal = this;
        for (Glyph gpath : glyph.getDeepPath()) {
            dotLocal = dotLocal.processTransform(gpath.getTransform(), true);
        }
        return dotLocal;
    }

    /**
     * Преобразовывает точку из локальных координат в глобальную для взятого глифа
     * @param glyph Глиф
     * @return Преобразованная точка
     */
    public Dot toGlobal(Glyph glyph) {
        if (glyph == null) throw new IllegalArgumentException("glyph == null");
        Dot dotGlobal = this;
        for (Glyph gpath : new org.gocha.collection.iterators.ReverseInterable<Glyph>(glyph.getDeepPath())) {
            dotGlobal = dotGlobal.processTransform(gpath.getTransform(), false);
        }
        return dotGlobal;
    }

    /**
     * Угол между точкой и началом координат
     * @return Угол
     */
    public double angle() {
        return angle(x, y);
    }

    /**
     * Угол между данной и указанной точкой
     * @param d Указанная точка
     * @return Угол
     */
    public double angle(Dot d) {
        if (d == null) {
            throw new IllegalArgumentException("d == null");
        }
        return angle(d.getX() - getX(), d.getY() - getY());
    }

    protected static double angle(double x, double y) {
        if (x == 0 && y == 0) return 0;
        int p = 0;
        if (x >= 0 && y >= 0) p = 0;
        if (x < 0 && y >= 0) p = 1;
        if (x < 0 && y < 0) p = 2;
        if (x >= 0 && y < 0) p = 3;
        switch(p) {
            case 0:
                return x == 0 ? Math.PI / 2 : Math.atan(y / x);
            case 1:
                return Math.PI + Math.atan(y / x);
            case 2:
                return Math.PI + Math.atan(y / x);
            case 3:
                return x == 0 ? Math.PI * 1.5 : Math.atan(y / x);
        }
        return 0;
    }

    /**
     * Вычисляет угол между прямыми a и b, пересекающиеся в данной точке
     * @param dA Точка прямой A
     * @param dB Точка прямой Б
     * @return Угол
     */
    public double angle(Dot dA, Dot dB) {
        if (dA == null) throw new IllegalArgumentException("dA == null");
        if (dB == null) throw new IllegalArgumentException("dB == null");
        dA = dA.sub(this);
        dB = dB.sub(this);
        double aA = dA.angle();
        double aB = dB.angle();
        return aA - aB;
    }

    /**
     * Создает точку из параметров переноса трансформации объекта
     * @param transform Параметры трансформации
     * @return Точка
     */
    public static Dot fromTranslate(Transform transform) {
        if (transform == null) {
            throw new IllegalArgumentException("transform == null");
        }
        return new Dot(transform.getLeft(), transform.getTop());
    }

    /**
     * Создает точку из параметров переноса трансформации объекта
     * @param glyph Объект
     * @return Точка
     */
    public static Dot fromTranslate(Glyph glyph) {
        if (glyph == null) {
            throw new IllegalArgumentException("glyph == null");
        }
        return fromTranslate(glyph.getTransform());
    }

    /**
     * Создает точку из объекта java.awt.Point
     * @param point Объект java.awt.Point
     * @return Точка
     */
    public static Dot fromPoint(java.awt.Point point) {
        return new Dot(point);
    }

    /**
     * Создает точку из объекта полярных координат
     * @param coord Объект
     * @return Точка
     */
    public static Dot fromCoord(Coord coord) {
        return new Dot(coord);
    }

    /**
     * Копирует точку из указаной
     * @param dot Образец
     * @return Точка
     */
    public static Dot fromDot(Dot dot) {
        return new Dot(dot);
    }

    /**
     * Копирует координаты X,Y из указанной точки
     * @param src Точка образец
     */
    public void copyFrom(Dot src) {
        if (src != null) {
            setX(src.getX());
            setY(src.getY());
        }
    }

    /**
     * Устаналивает параметры переноса в соответствии с точкой
     * @param transform Параметры
     */
    public void setTranslate(Transform transform) {
        if (transform == null) {
            throw new IllegalArgumentException("transform == null");
        }
        transform.setLeft(x);
        transform.setTop(y);
    }

    /**
     * Устаналивает параметры переноса объекта в соответствии с точкой
     * @param glyph Объект
     */
    public void setTranslate(Glyph glyph) {
        if (glyph == null) {
            throw new IllegalArgumentException("glyph == null");
        }
        setTranslate(glyph.getTransform());
    }
}
