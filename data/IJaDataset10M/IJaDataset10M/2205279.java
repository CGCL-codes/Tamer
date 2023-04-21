package de.grogra.imp3d.objects;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;
import de.grogra.imp.PickList;
import de.grogra.imp3d.Pickable;
import de.grogra.imp3d.RenderState;
import de.grogra.imp3d.Renderable;

/**
 * This class represents a point cloud.
 * For now, only point locations and a color (for all points the same)
 * can be set, but no normals. The parameter pointSize controls how big
 * points are displayed on the screen.
 * 
 * @author Reinhard Hemmerling
 *
 * TODO implement selection
 */
public class PointCloud extends ColoredNull implements Renderable, Pickable {

    float pointSize = 3.0f;

    float[] points;

    private transient double minx, miny, minz, maxx, maxy, maxz;

    /**
	 * Create a new empty point cloud.
	 */
    public PointCloud() {
        setPoints(new float[0]);
    }

    /**
	 * Create a new point cloud and set the points to the provided array.
	 * @param points the coordinates of the points
	 */
    public PointCloud(float[] points) {
        setPoints(points);
    }

    /**
	 * Create a new point cloud and set the points to the provided array.
	 * @param points the coordinates of the points
	 * @param pointSize size of a point on screen
	 */
    public PointCloud(float[] points, float pointSize) {
        setPoints(points);
    }

    /**
	 * Make a copy of the points and return them.
	 * @return the points
	 */
    public float[] getPoints() {
        float[] result = new float[points.length];
        System.arraycopy(points, 0, result, 0, points.length);
        return result;
    }

    /**
	 * Make a copy of the points and store it.
	 * Points are given by a triple of floats for (x,y,z).
	 * The array thus stores points in the form [x0,y0,z0,x1,y1,z1,...].
	 * @param points the points to set
	 */
    public void setPoints(float[] points) {
        assert points.length % 3 == 0;
        this.points = new float[points.length];
        System.arraycopy(points, 0, this.points, 0, points.length);
        final int N = this.points.length / 3;
        minx = miny = minz = Double.POSITIVE_INFINITY;
        maxx = maxy = maxz = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < N; i++) {
            minx = min(minx, this.points[3 * i + 0]);
            miny = min(miny, this.points[3 * i + 1]);
            minz = min(minz, this.points[3 * i + 2]);
            maxx = max(maxx, this.points[3 * i + 0]);
            maxy = max(maxy, this.points[3 * i + 1]);
            maxz = max(maxz, this.points[3 * i + 2]);
        }
    }

    public void draw(Object object, boolean asNode, RenderState rs) {
        rs.drawPointCloud(points, pointSize, color, RenderState.CURRENT_HIGHLIGHT, null);
    }

    public void pick(Object object, boolean asNode, Point3d origin, Vector3d direction, Matrix4d transformation, PickList list) {
        double tmin, tmax, tymin, tymax, tzmin, tzmax;
        if (direction.x >= 0) {
            tmin = (minx - origin.x) / direction.x;
            tmax = (maxx - origin.x) / direction.x;
        } else {
            tmin = (maxx - origin.x) / direction.x;
            tmax = (minx - origin.x) / direction.x;
        }
        if (direction.y >= 0) {
            tymin = (miny - origin.y) / direction.y;
            tymax = (maxy - origin.y) / direction.y;
        } else {
            tymin = (maxy - origin.y) / direction.y;
            tymax = (miny - origin.y) / direction.y;
        }
        if (tmin > tymax || tymin > tmax) return;
        if (tymin > tmin) tmin = tymin;
        if (tymax < tmax) tmax = tymax;
        if (direction.z >= 0) {
            tzmin = (minz - origin.z) / direction.z;
            tzmax = (maxz - origin.z) / direction.z;
        } else {
            tzmin = (maxz - origin.z) / direction.z;
            tzmax = (minz - origin.z) / direction.z;
        }
        if (tmin > tzmax || tzmin > tmax) return;
        if (tzmin > tmin) tmin = tzmin;
        if (tzmax < tmax) tmax = tzmax;
        list.add(tmin);
    }

    public static final NType $TYPE;

    public static final NType.Field pointSize$FIELD;

    public static final NType.Field points$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(PointCloud.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        public void setFloat(Object o, float value) {
            switch(id) {
                case 0:
                    ((PointCloud) o).pointSize = (float) value;
                    return;
            }
            super.setFloat(o, value);
        }

        @Override
        public float getFloat(Object o) {
            switch(id) {
                case 0:
                    return ((PointCloud) o).getPointSize();
            }
            return super.getFloat(o);
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 1:
                    ((PointCloud) o).setPoints((float[]) value);
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 1:
                    return ((PointCloud) o).getPoints();
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new PointCloud());
        $TYPE.addManagedField(pointSize$FIELD = new _Field("pointSize", 0 | _Field.SCO, de.grogra.reflect.Type.FLOAT, null, 0));
        $TYPE.addManagedField(points$FIELD = new _Field("points", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(float[].class), null, 1));
        $TYPE.addIdentityAccessor(Attributes.SHAPE);
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new PointCloud();
    }

    public float getPointSize() {
        return pointSize;
    }

    public void setPointSize(float value) {
        this.pointSize = (float) value;
    }
}
