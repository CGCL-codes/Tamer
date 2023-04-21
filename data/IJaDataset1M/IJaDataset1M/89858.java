package de.grogra.imp3d.shading;

import de.grogra.imp3d.spectral.BlackbodySpectralCurve;
import de.grogra.imp3d.spectral.SpectralCurve;
import de.grogra.persistence.SCOType;

public class BlackbodySPD extends SPD {

    float temp = 5000;

    public static final Type $TYPE;

    public static final Type.Field temp$FIELD;

    public static class Type extends SCOType {

        public Type(Class c, de.grogra.persistence.SCOType supertype) {
            super(c, supertype);
        }

        public Type(BlackbodySPD representative, de.grogra.persistence.SCOType supertype) {
            super(representative, supertype);
        }

        Type(Class c) {
            super(c, SCOType.$TYPE);
        }

        private static final int SUPER_FIELD_COUNT = SCOType.FIELD_COUNT;

        protected static final int FIELD_COUNT = SCOType.FIELD_COUNT + 1;

        static Field _addManagedField(Type t, String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            return t.addManagedField(name, modifiers, type, componentType, id);
        }

        @Override
        protected void setFloat(Object o, int id, float value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    ((BlackbodySPD) o).temp = (float) value;
                    return;
            }
            super.setFloat(o, id, value);
        }

        @Override
        protected float getFloat(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    return ((BlackbodySPD) o).getTemp();
            }
            return super.getFloat(o, id);
        }

        @Override
        public Object newInstance() {
            return new BlackbodySPD();
        }
    }

    public de.grogra.persistence.ManageableType getManageableType() {
        return $TYPE;
    }

    static {
        $TYPE = new Type(BlackbodySPD.class);
        temp$FIELD = Type._addManagedField($TYPE, "temp", 0 | Type.Field.SCO, de.grogra.reflect.Type.FLOAT, null, Type.SUPER_FIELD_COUNT + 0);
        $TYPE.validate();
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float value) {
        this.temp = (float) value;
    }

    public BlackbodySPD() {
        super();
    }

    public BlackbodySPD(float temp) {
        super();
        this.temp = temp;
    }

    @Override
    public SpectralCurve getSpectralDistribution() {
        return new BlackbodySpectralCurve(temp);
    }
}
