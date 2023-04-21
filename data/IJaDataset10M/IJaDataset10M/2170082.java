package de.grogra.imp3d.objects;

import javax.vecmath.*;
import de.grogra.graph.*;
import de.grogra.imp.PickList;
import de.grogra.imp3d.*;
import de.grogra.math.*;

public class Patch extends ShadedNull implements Pickable, Polygonizable, Polygonization, Renderable {

    protected VertexGrid grid;

    public Patch() {
        super();
        bits |= TRANSFORMING_MASK;
        setLayer(1);
    }

    public Patch(VertexGrid grid) {
        this();
        this.grid = grid;
    }

    public ContextDependent getPolygonizableSource(GraphState gs) {
        return grid;
    }

    public static void pick(VertexGrid grid, Point3d origin, Vector3d direction, PickList list) {
        Sphere.pick(1, origin, direction, list);
    }

    public void pick(Object object, boolean asNode, Point3d origin, Vector3d direction, Matrix4d transformation, PickList list) {
        GraphState gs = list.getGraphState();
        gs.setObjectContext(object, asNode);
        if (object == this) {
            if (gs.getInstancingPathIndex() <= 0) {
                pick(grid, origin, direction, list);
            } else {
                pick((VertexGrid) gs.checkObject(this, true, Attributes.VERTEX_GRID, grid), origin, direction, list);
            }
        } else {
            pick((VertexGrid) gs.getObject(object, asNode, Attributes.VERTEX_GRID), origin, direction, list);
        }
    }

    public Polygonization getPolygonization() {
        return this;
    }

    public void polygonize(ContextDependent source, GraphState gs, PolygonArray out, int flags, float flatness) {
        boolean uv = (flags & COMPUTE_UV) != 0;
        out.init(3);
        out.edgeCount = 4;
        out.planar = false;
        out.closed = false;
        int nu = grid.getUSize(gs), uMax = nu - 1, nv = grid.getVSize(gs), vMax = nv - 1;
        float fu = 1f / uMax, fv = 1f / vMax;
        Pool pool = Pool.get(gs);
        float[] a = pool.getFloatArray(0, 3);
        Point3f p = pool.p3f0;
        Vector3f v0 = pool.v3f0, v1 = pool.v3f1, sum = pool.v3f2;
        for (int v = 0; v <= vMax; v++) {
            for (int u = 0; u <= uMax; u++) {
                int i = grid.getVertex(a, grid.getVertexIndex(u, v, gs), gs);
                p.x = a[0];
                p.y = (i > 1) ? a[1] : 0;
                p.z = (i > 2) ? a[2] : 0;
                out.vertices.push(p.x).push(p.y).push(p.z);
                if (uv) {
                    out.uv.push(u * fu).push(v * fv);
                }
            }
        }
        float[] vs = out.vertices.elements;
        int n = 0;
        for (int v = 0; v <= vMax; v++) {
            for (int u = 0; u <= uMax; u++) {
                int i = 3 * n;
                p.x = vs[i];
                p.y = vs[i + 1];
                p.z = vs[i + 2];
                sum.set(0, 0, 0);
                boolean prev = false;
                for (int j = -4; j <= 0; j++) {
                    int u2 = u, v2 = v;
                    switch(j & 3) {
                        case 0:
                            if (++u2 == nu) {
                                prev = false;
                                continue;
                            }
                            break;
                        case 1:
                            if (++v2 == nv) {
                                prev = false;
                                continue;
                            }
                            break;
                        case 2:
                            if (--u2 < 0) {
                                prev = false;
                                continue;
                            }
                            break;
                        case 3:
                            if (--v2 < 0) {
                                prev = false;
                                continue;
                            }
                            break;
                    }
                    i = 3 * ((v2 * nu) + u2);
                    v1.x = vs[i];
                    v1.y = vs[i + 1];
                    v1.z = vs[i + 2];
                    v1.sub(p);
                    if (prev) {
                        v0.cross(v0, v1);
                        sum.add(v0);
                    }
                    Vector3f t = v0;
                    v0 = v1;
                    v1 = t;
                    prev = true;
                }
                out.setNormal(n, sum.x, sum.y, sum.z);
                if ((v < vMax) && (u < uMax)) {
                    out.polygons.push(n).push(n + 1).push(n + 1 + nu).push(n + nu);
                }
                n++;
            }
        }
    }

    public void draw(Object object, boolean asNode, RenderState rs) {
        rs.drawPolygons(this, object, asNode, null, -1, null);
    }

    public static final NType $TYPE;

    public static final NType.Field grid$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(Patch.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((Patch) o).grid = (VertexGrid) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((Patch) o).getGrid();
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new Patch());
        $TYPE.addManagedField(grid$FIELD = new _Field("grid", _Field.PROTECTED | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(VertexGrid.class), null, 0));
        $TYPE.declareFieldAttribute(grid$FIELD, Attributes.VERTEX_GRID);
        $TYPE.addIdentityAccessor(Attributes.SHAPE);
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Patch();
    }

    public VertexGrid getGrid() {
        return grid;
    }

    public void setGrid(VertexGrid value) {
        grid$FIELD.setObject(this, value);
    }
}
