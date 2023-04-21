package net.engine.links;

import net.engine.Renderable;
import net.engine.SafeArrayList;
import net.engine.math.Float2;

public class Links {

    public Renderable sprite;

    public SafeArrayList<Float2> linkPoints;

    public Links(Renderable sprite) {
        this.sprite = sprite;
        linkPoints = null;
    }

    private void ensureArrayExists() {
        if (linkPoints == null) {
            linkPoints = new SafeArrayList<Float2>();
        }
    }

    public int addLinkPoint(float x, float y) {
        ensureArrayExists();
        Float2 linkPoint = new Float2(x, y);
        linkPoints.add(linkPoint);
        return linkPoints.size() - 1;
    }

    public void setLinkPoint(int index, float x, float y) {
        ensureArrayExists();
        Float2 linkPoint = new Float2(x, y);
        linkPoints.set(index, linkPoint);
    }

    public Float2 getLinkPoint(int index) {
        if (linkPoints == null) {
            return null;
        }
        return linkPoints.get(index);
    }
}
