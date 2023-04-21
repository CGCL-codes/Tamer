package model.util;

import java.util.List;
import model.gl.knowledge.GLObject;

public class Neighborhood extends City {

    private final float X_GAP = 2.0f;

    private final float Y_GAP = 2.0f;

    private float width;

    private float depth;

    private String name;

    private List<GLObject> flats;

    public Neighborhood(List<GLObject> flats) {
        this("", flats);
    }

    public Neighborhood(String name, List<GLObject> flats) {
        this.name = name;
        this.flats = flats;
        this.cols = 0;
        this.rows = 0;
        this.width = 0.0f;
        this.depth = 0.0f;
        if (flats != null) {
            if (flats.size() > 0) {
                this.cols = super.calculateCols(flats.size());
                if (flats.size() % this.cols == 0) this.rows = flats.size() / this.cols; else this.rows = (flats.size() / this.cols + 1);
            }
        }
    }

    protected Vector2f doLayout(Vector2f point) {
        float x = point.getX();
        float y = point.getY();
        GLObject obj = null;
        boolean done = false;
        for (int i = 0; i < this.rows && !done; i++) {
            for (int j = 0; j < this.cols && !done; j++) {
                int index = i * this.cols + j;
                if (flats.size() > index) {
                    obj = flats.get(index);
                    obj.setPositionX(x);
                    obj.setPositionZ(y);
                    x += obj.getMaxWidth() + this.X_GAP;
                } else {
                    done = true;
                }
            }
            if (!done) {
                x = point.getX();
                if (obj != null) {
                    y += obj.getMaxDepth() + this.Y_GAP;
                }
            }
        }
        this.width = this.cols * obj.getMaxWidth() + (this.cols) * this.X_GAP;
        this.depth = this.rows * obj.getMaxDepth() + (this.rows) * this.Y_GAP;
        return new Vector2f(this.width, this.depth);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GLObject> getFlats() {
        return flats;
    }

    public void setFlats(List<GLObject> flats) {
        this.flats = flats;
    }

    public float getWidth() {
        return width;
    }

    public float getDepth() {
        return depth;
    }
}
