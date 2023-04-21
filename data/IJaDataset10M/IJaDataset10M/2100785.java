package net.sf.ictalive.wc3;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class GameObject implements Comparable<GameObject> {

    protected long id;

    protected float x;

    protected float y;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int compareTo(GameObject go) {
        int ret;
        ret = (int) (go.getId() - this.getId());
        return ret;
    }
}
