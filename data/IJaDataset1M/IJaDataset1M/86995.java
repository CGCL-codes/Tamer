package org.gocha.miner.client;

import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.List;

/**
 * Координаты
 * @author gocha
 */
public class Coord {

    private int x;

    private int y;

    /**
     * Координаты
     * @param x Координата x
     * @param y Координата y
     */
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Координата x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Координата y
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Складывает координаты
     * @param c Координата
     * @return Новая координата
     */
    public Coord add(Coord c) {
        if (c == null) return this;
        return new Coord(x + c.x, y + c.y);
    }

    /**
     * Проверяет открытая ли карта
     * @param m Карта
     * @return true - Открыта
     */
    public boolean isOpen(Map m) {
        if (m == null) return false;
        return m.isOpen(x, y);
    }

    /**
     * Проверяет закрыта ли карта
     * @param m Карта
     * @return true - Закрыта
     */
    public boolean isClosed(Map m) {
        if (m == null) return false;
        return m.isClosed(x, y);
    }

    /**
     * Проверяет наличее флага
     * @param m Карта
     * @return true - Флаг установлен
     */
    public boolean hasFlag(Map m) {
        return m.hasFlag(x, y);
    }

    /**
     * Проверяет свободна ли (нет флага и закрыта) ячейка
     * @param m Карта
     * @return true - свободна
     */
    public boolean isFree(Map m) {
        return !hasFlag(m) && isClosed(m);
    }

    /**
     * Возвращает клетки вокруг координаты
     * @param m Карта
     * @return Клетки
     */
    public List<Coord> around(Map m) {
        ArrayList<Coord> l = new ArrayList<Coord>();
        if (m == null) return l;
        int w = m.getWidth();
        int h = m.getHeight();
        for (int iy = -1; iy <= 1; iy++) {
            for (int ix = -1; ix <= 1; ix++) {
                int nx = x + ix;
                int ny = y + iy;
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                l.add(new Coord(nx, ny));
            }
        }
        return l;
    }

    /**
     * Возвращает свободные клеки вокруг
     * @param m Карта
     * @return Свободные клетки
     */
    public List<Coord> freeAround(Map m) {
        ArrayList<Coord> l = new ArrayList<Coord>();
        if (m == null) return l;
        int w = m.getWidth();
        int h = m.getHeight();
        for (int iy = -1; iy <= 1; iy++) {
            for (int ix = -1; ix <= 1; ix++) {
                int nx = x + ix;
                int ny = y + iy;
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                boolean free = !m.hasFlag(nx, ny) && m.isClosed(nx, ny);
                if (free) l.add(new Coord(nx, ny));
            }
        }
        return l;
    }
}
