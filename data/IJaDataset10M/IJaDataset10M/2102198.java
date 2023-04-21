package de.grogra.math;

import de.grogra.graph.*;

public class ProfileSweep extends Sweep {

    BSplineCurve profile;

    BSplineCurve trajectory;

    public static final Type $TYPE;

    public static final Type.Field profile$FIELD;

    public static final Type.Field trajectory$FIELD;

    public static class Type extends Sweep.Type {

        public Type(Class c, de.grogra.persistence.SCOType supertype) {
            super(c, supertype);
        }

        public Type(ProfileSweep representative, de.grogra.persistence.SCOType supertype) {
            super(representative, supertype);
        }

        Type(Class c) {
            super(c, Sweep.$TYPE);
        }

        private static final int SUPER_FIELD_COUNT = Sweep.Type.FIELD_COUNT;

        protected static final int FIELD_COUNT = Sweep.Type.FIELD_COUNT + 2;

        static Field _addManagedField(Type t, String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            return t.addManagedField(name, modifiers, type, componentType, id);
        }

        @Override
        protected void setObject(Object o, int id, Object value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    ((ProfileSweep) o).profile = (BSplineCurve) value;
                    return;
                case Type.SUPER_FIELD_COUNT + 1:
                    ((ProfileSweep) o).trajectory = (BSplineCurve) value;
                    return;
            }
            super.setObject(o, id, value);
        }

        @Override
        protected Object getObject(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    return ((ProfileSweep) o).getProfile();
                case Type.SUPER_FIELD_COUNT + 1:
                    return ((ProfileSweep) o).getTrajectory();
            }
            return super.getObject(o, id);
        }

        @Override
        public Object newInstance() {
            return new ProfileSweep();
        }
    }

    public de.grogra.persistence.ManageableType getManageableType() {
        return $TYPE;
    }

    static {
        $TYPE = new Type(ProfileSweep.class);
        profile$FIELD = Type._addManagedField($TYPE, "profile", 0 | Type.Field.SCO, de.grogra.reflect.ClassAdapter.wrap(BSplineCurve.class), null, Type.SUPER_FIELD_COUNT + 0);
        trajectory$FIELD = Type._addManagedField($TYPE, "trajectory", 0 | Type.Field.SCO, de.grogra.reflect.ClassAdapter.wrap(BSplineCurve.class), null, Type.SUPER_FIELD_COUNT + 1);
        $TYPE.validate();
    }

    public BSplineCurve getProfile() {
        return profile;
    }

    public void setProfile(BSplineCurve value) {
        profile$FIELD.setObject(this, value);
    }

    public BSplineCurve getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(BSplineCurve value) {
        trajectory$FIELD.setObject(this, value);
    }

    public ProfileSweep() {
        super();
    }

    public ProfileSweep(BSplineCurve profile, BSplineCurve trajectory) {
        this.profile = profile;
        this.trajectory = trajectory;
    }

    public boolean dependsOnContext() {
        return profile.dependsOnContext() || trajectory.dependsOnContext();
    }

    @Override
    public void writeStamp(Cache.Entry cache, GraphState gs) {
        super.writeStamp(cache, gs);
        profile.writeStamp(cache, gs);
        trajectory.writeStamp(cache, gs);
    }

    @Override
    protected BSplineCurve getTrajectory(GraphState gs) {
        return trajectory;
    }

    @Override
    protected Object[] initCache(GraphState gs) {
        return new Object[1];
    }

    @Override
    protected int getVertexImpl(float[] out, int curve, int index, Object[] cache, GraphState gs) {
        int n = profile.getVertex(out, index, gs);
        return profile.isRational(gs) ? 1 - n : n;
    }

    public float getKnot(int curve, int index, GraphState gs) {
        return profile.getKnot(0, index, gs);
    }

    public int getSize(int curve, GraphState gs) {
        return profile.getSize(gs);
    }

    public int getDegree(int curve, GraphState gs) {
        return profile.getDegree(gs);
    }
}
