package gestalt.extension.quadline;

import gestalt.shape.AbstractShape;
import gestalt.shape.Color;
import mathematik.Vector3f;

public abstract class TubeLine extends AbstractShape {

    public Vector3f[] points;

    public Color[] colors;

    public float[] linewidthset;

    public float linewidth;

    public int steps;

    protected TubeLineProducer mProducer;

    protected boolean mAutoUpdate;

    private QuadFragment[][] mFragments;

    public TubeLine() {
        mProducer = new TubeLineProducer();
        linewidth = 5;
        steps = 3;
        mAutoUpdate = false;
    }

    public void propagateUpVector(boolean theFlag) {
        mProducer.UPVECTOR_PROPAGATION = theFlag;
    }

    public void autoupdate(boolean theAutoUpdate) {
        mAutoUpdate = theAutoUpdate;
    }

    public Vector3f upvector() {
        return mProducer.upvector();
    }

    public void update() {
        if (points != null) {
            mFragments = mProducer.produce(points, colors, linewidthset, linewidth, steps);
        }
    }

    public TubeLineProducer producer() {
        return mProducer;
    }

    public QuadFragment[][] fragments() {
        return mFragments;
    }

    public float getSortValue() {
        return _mySortValue;
    }

    public float[] getSortData() {
        return position().toArray();
    }

    public void setSortValue(float theSortValue) {
        _mySortValue = theSortValue;
    }
}
