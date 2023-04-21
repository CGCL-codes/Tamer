package de.grogra.imp3d.objects;

import java.util.ArrayList;
import javax.vecmath.*;
import de.grogra.graph.*;
import de.grogra.util.*;
import de.grogra.math.*;
import de.grogra.xl.util.FloatList;

public class SweepSequence extends ExtendedSweep {

    String name;

    boolean hermite = true;

    float tangentLength = 1.2f;

    int path = Sequence.DOWNWARD;

    public static final Type $TYPE;

    public static final Type.Field name$FIELD;

    public static final Type.Field hermite$FIELD;

    public static final Type.Field tangentLength$FIELD;

    public static final Type.Field path$FIELD;

    public static class Type extends ExtendedSweep.Type {

        public Type(Class c, de.grogra.persistence.SCOType supertype) {
            super(c, supertype);
        }

        public Type(SweepSequence representative, de.grogra.persistence.SCOType supertype) {
            super(representative, supertype);
        }

        Type(Class c) {
            super(c, ExtendedSweep.$TYPE);
        }

        private static final int SUPER_FIELD_COUNT = ExtendedSweep.Type.FIELD_COUNT;

        protected static final int FIELD_COUNT = ExtendedSweep.Type.FIELD_COUNT + 4;

        static Field _addManagedField(Type t, String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            return t.addManagedField(name, modifiers, type, componentType, id);
        }

        @Override
        protected void setBoolean(Object o, int id, boolean value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 1:
                    ((SweepSequence) o).hermite = (boolean) value;
                    return;
            }
            super.setBoolean(o, id, value);
        }

        @Override
        protected boolean getBoolean(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 1:
                    return ((SweepSequence) o).isHermite();
            }
            return super.getBoolean(o, id);
        }

        @Override
        protected void setInt(Object o, int id, int value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 3:
                    ((SweepSequence) o).path = (int) value;
                    return;
            }
            super.setInt(o, id, value);
        }

        @Override
        protected int getInt(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 3:
                    return ((SweepSequence) o).getPath();
            }
            return super.getInt(o, id);
        }

        @Override
        protected void setFloat(Object o, int id, float value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 2:
                    ((SweepSequence) o).tangentLength = (float) value;
                    return;
            }
            super.setFloat(o, id, value);
        }

        @Override
        protected float getFloat(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 2:
                    return ((SweepSequence) o).getTangentLength();
            }
            return super.getFloat(o, id);
        }

        @Override
        protected void setObject(Object o, int id, Object value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    ((SweepSequence) o).name = (String) value;
                    return;
            }
            super.setObject(o, id, value);
        }

        @Override
        protected Object getObject(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    return ((SweepSequence) o).getName();
            }
            return super.getObject(o, id);
        }

        @Override
        public Object newInstance() {
            return new SweepSequence();
        }
    }

    public de.grogra.persistence.ManageableType getManageableType() {
        return $TYPE;
    }

    static {
        $TYPE = new Type(SweepSequence.class);
        name$FIELD = Type._addManagedField($TYPE, "name", 0 | Type.Field.SCO, de.grogra.reflect.ClassAdapter.wrap(String.class), null, Type.SUPER_FIELD_COUNT + 0);
        hermite$FIELD = Type._addManagedField($TYPE, "hermite", 0 | Type.Field.SCO, de.grogra.reflect.Type.BOOLEAN, null, Type.SUPER_FIELD_COUNT + 1);
        tangentLength$FIELD = Type._addManagedField($TYPE, "tangentLength", 0 | Type.Field.SCO, de.grogra.reflect.Type.FLOAT, null, Type.SUPER_FIELD_COUNT + 2);
        path$FIELD = Type._addManagedField($TYPE, "path", 0 | Type.Field.SCO, Sequence.PATH_TYPE, null, Type.SUPER_FIELD_COUNT + 3);
        $TYPE.validate();
    }

    public boolean isHermite() {
        return hermite;
    }

    public void setHermite(boolean value) {
        this.hermite = (boolean) value;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int value) {
        this.path = (int) value;
    }

    public float getTangentLength() {
        return tangentLength;
    }

    public void setTangentLength(float value) {
        this.tangentLength = (float) value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name$FIELD.setObject(this, value);
    }

    public SweepSequence() {
    }

    public SweepSequence(String name) {
        this.name = name;
    }

    public SweepSequence(String name, boolean useRail) {
        this.name = name;
        setUseRail(useRail);
    }

    public boolean dependsOnContext() {
        return true;
    }

    @Override
    public void writeStamp(Cache.Entry cache, GraphState gs) {
        super.writeStamp(cache, gs);
        Object[] c = getCacheImpl(gs);
        cache.write(((VertexListImpl) ((BSplineOfVertices) c[1]).getVertices()).getData());
        Int2ObjectMap profiles = (Int2ObjectMap) c[2];
        Object[] list = (Object[]) profiles.get(Integer.MAX_VALUE);
        cache.write(profiles.size());
        GraphState.ObjectContext os = gs.getObjectContext();
        for (int i = profiles.size() - 2; i >= 0; i--) {
            int j = ((Integer) profiles.getValueAt(i)).intValue();
            gs.setObjectContext((GraphState.ObjectContext) list[j]);
            ((BSplineCurve) list[j + 1]).writeStamp(cache, gs);
        }
        gs.setObjectContext(os);
    }

    private final transient VertexSequence seq = new VertexSequence() {

        @Override
        public boolean dependsOnContext() {
            return false;
        }

        @Override
        public String getName() {
            return SweepSequence.this.getName();
        }

        @Override
        boolean collectCurves() {
            return true;
        }

        @Override
        protected Object calculateCache(Matrix4d inv, ArrayList list, GraphState gs, Object info) {
            float[] vertices = (float[]) super.calculateCache(inv, list, gs, info);
            VertexList v = new VertexListImpl(vertices, 7);
            return new BSplineOfVertices(v, 3, false, hermite && (v.getSize(null) > 2));
        }
    };

    @Override
    protected BSplineCurve init(final Int2ObjectMap profiles, GraphState gs) {
        seq.path = this.path;
        seq.hermite = this.hermite;
        seq.tangentLength = this.tangentLength;
        return (BSplineCurve) seq.calculateCache(gs, profiles);
    }

    @Override
    protected Object[] getCache(GraphState state) {
        Object[] cache = getCacheImpl(state);
        if (cache[0] == null) {
            cache[0] = computeTrajectory(cache, state);
        }
        final Int2ObjectMap profiles = (Int2ObjectMap) cache[2];
        final Object[] list = (Object[]) profiles.remove(Integer.MAX_VALUE);
        if (list != null) {
            BSplineCurveList curves = new BSplineCurveList() {

                public int getSize(GraphState gs) {
                    return profiles.size();
                }

                private int getIndex(int curve) {
                    curve = ((Number) profiles.getValueAt(curve)).intValue();
                    return (curve < 0) ? curve & 255 : curve;
                }

                public int getDimension(int curve, GraphState gs) {
                    curve = getIndex(curve);
                    gs.setObjectContext((GraphState.ObjectContext) list[curve]);
                    return ((BSplineCurve) list[curve + 1]).getDimension(gs);
                }

                public int getVertex(float[] out, int curve, int index, GraphState gs) {
                    curve = getIndex(curve);
                    gs.setObjectContext((GraphState.ObjectContext) list[curve]);
                    return ((BSplineCurve) list[curve + 1]).getVertex(out, index, gs);
                }

                public int getSize(int curve, GraphState gs) {
                    curve = getIndex(curve);
                    gs.setObjectContext((GraphState.ObjectContext) list[curve]);
                    return ((BSplineCurve) list[curve + 1]).getSize(gs);
                }

                public float getKnot(int curve, int index, GraphState gs) {
                    curve = getIndex(curve);
                    gs.setObjectContext((GraphState.ObjectContext) list[curve]);
                    return ((BSplineCurve) list[curve + 1]).getKnot(0, index, gs);
                }

                public int getDegree(int curve, GraphState gs) {
                    curve = getIndex(curve);
                    gs.setObjectContext((GraphState.ObjectContext) list[curve]);
                    return ((BSplineCurve) list[curve + 1]).getDegree(gs);
                }

                public boolean isRational(int curve, GraphState gs) {
                    curve = getIndex(curve);
                    gs.setObjectContext((GraphState.ObjectContext) list[curve]);
                    return ((BSplineCurve) list[curve + 1]).isRational(gs);
                }

                public boolean areCurvesCompatible(GraphState gs) {
                    return false;
                }

                public boolean dependsOnContext() {
                    return false;
                }

                public void writeStamp(Cache.Entry cache, GraphState gs) {
                }
            };
            GraphState.ObjectContext os = state.getObjectContext();
            FloatList out = new FloatList();
            int[] npv = BSpline.makeCompatible(out, curves, 1e-3f, 0, false, state);
            final int vc = npv[0];
            final int knotIndex = out.size() - vc - npv[1] - 1;
            int vertexIndex = 0;
            for (int j = 0; j < profiles.size(); j++) {
                int d = curves.getDimension(j, state);
                final int vi = vertexIndex;
                profiles.setValueAt(j, new BSplineCurveImpl(out.elements, d, npv[1], false, curves.isRational(j, state)) {

                    @Override
                    protected int getCount() {
                        return vc;
                    }

                    @Override
                    protected int getVertexIndex(int index) {
                        return vi + index * this.dimension;
                    }

                    @Override
                    protected int getKnotIndex(int knot) {
                        return knotIndex + knot;
                    }
                });
                vertexIndex += d * vc;
            }
            state.setObjectContext(os);
        }
        return cache;
    }
}
