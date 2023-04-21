package com.novusradix.JavaPop.Client;

import com.novusradix.JavaPop.Math.Matrix4;
import com.novusradix.JavaPop.Math.MutableFloat;
import com.novusradix.JavaPop.Math.Vector3;
import java.awt.Dimension;
import java.awt.Point;
import javax.media.opengl.GL;
import com.novusradix.JavaPop.Messaging.HeightMapUpdate;
import com.novusradix.JavaPop.Tile;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.*;

/**
 * This class is the client side map. It specifies creation, response to server update messages, and rendering.
 * Other client classes can also query points on the map.
 * @author gef
 */
public class HeightMap extends com.novusradix.JavaPop.HeightMap implements GLObject {

    HeightMapImpl implementation;

    GLObject renderer;

    private final Map<Point, MutableFloat> rocks;

    private Model rock, tree;

    private float lastTime;

    public EarthquakeRenderer earthquakeRenderer;

    public HeightMap(Dimension mapSize, Game g) {
        super(mapSize);
        try {
            rock = new Model(ModelData.fromURL(getClass().getResource("/com/novusradix/JavaPop/models/rock.model")), getClass().getResource("/com/novusradix/JavaPop/textures/marble.png"));
            tree = new Model(ModelData.fromURL(getClass().getResource("/com/novusradix/JavaPop/models/tree.model")), getClass().getResource("/com/novusradix/JavaPop/textures/tree.png"));
        } catch (IOException ex) {
            Logger.getLogger(HeightMapGLSL.class.getName()).log(Level.SEVERE, null, ex);
        }
        earthquakeRenderer = new EarthquakeRenderer(g, this);
        HeightMapGLSL h = new HeightMapGLSL();
        implementation = h;
        renderer = h;
        implementation.initialise(this);
        rocks = new HashMap<Point, MutableFloat>();
    }

    public void display(GL gl, float time) {
        stepRocks(time - lastTime);
        renderRocks(gl);
        earthquakeRenderer.display(gl, time);
        lastTime = time;
        renderer.display(gl, time);
    }

    public void init(GL gl) {
        tree.init(gl);
        rock.init(gl);
        earthquakeRenderer.init(gl);
        renderer.init(gl);
    }

    @Override
    public void applyUpdate(HeightMapUpdate u) {
        implementation.applyUpdate(u);
    }

    @Override
    public byte getHeight(int x, int y) {
        return implementation.getHeight(x, y);
    }

    @Override
    protected void setHeight(int x, int y, byte b) {
        implementation.setHeight(x, y, b);
    }

    public void setTile(int x, int y, byte t) {
        implementation.setTile(x, y, t);
        if (t != Tile.EARTHQUAKE.ordinal()) {
            earthquakeRenderer.removeTile(x, y);
        }
    }

    public void addRocks(Set<Point> r) {
        synchronized (rocks) {
            for (Point tempPoint : r) {
                rocks.put(tempPoint, new MutableFloat(0));
            }
        }
    }

    public void removeRocks(Set<Point> r) {
        synchronized (rocks) {
            for (Point tempPoint : r) {
                rocks.remove(tempPoint);
            }
        }
    }

    private void stepRocks(float elapsedTime) {
        Entry<Point, MutableFloat> e;
        MutableFloat f;
        Point tempPoint;
        Boolean sea;
        Iterator<Entry<Point, MutableFloat>> i = rocks.entrySet().iterator();
        while (i.hasNext()) {
            e = i.next();
            f = e.getValue();
            tempPoint = e.getKey();
            sea = isSeaLevel(tempPoint.x, tempPoint.y);
            if (sea) {
                f.f = Math.max(0.0f, f.f - elapsedTime / 2.0f);
            } else {
                f.f = Math.min(1.0f, f.f + elapsedTime / 2.0f);
            }
        }
    }

    Vector3 p = new Vector3();

    private void renderRocks(GL gl) {
        int x, y;
        rock.prepare(gl);
        gl.glUseProgram(0);
        gl.glColor4f(1, 1, 1, 1);
        synchronized (rocks) {
            for (Entry<Point, MutableFloat> e : rocks.entrySet()) {
                x = e.getKey().x;
                y = e.getKey().y;
                p.set(x, y, e.getValue().f + getHeight(x + 0.5f, y + 0.5f) - 1.0f);
                rock.display(p, Matrix4.identity, gl);
            }
        }
    }
}
