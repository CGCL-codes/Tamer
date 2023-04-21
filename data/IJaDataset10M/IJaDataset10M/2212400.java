package map.items;

import java.util.concurrent.locks.ReentrantLock;

/**
 * MapObject; Oberklasse fuer alle Arten von Objekten die sich auf der Karte
 * befinden.
 * 
 * @author ivan__g__s
 */
public abstract class MapObject {

    protected ReentrantLock lock = new ReentrantLock();

    protected int x, y;

    protected boolean visible;

    protected MapObject(int x, int y) {
        this.x = x;
        this.y = y;
        visible = true;
    }

    protected void lock() {
        lock.lock();
    }

    protected void unlock() {
        lock.unlock();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
