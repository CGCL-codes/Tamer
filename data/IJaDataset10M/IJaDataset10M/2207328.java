package de.grogra.imp3d.shading;

import de.grogra.graph.impl.Node.NType;
import de.grogra.math.Channel;
import de.grogra.math.ChannelData;
import de.grogra.math.ChannelMap;
import de.grogra.math.ColorMap;
import de.grogra.math.Graytone;
import de.grogra.vecmath.Math2;

public class Checker extends SurfaceMap {

    protected static final ColorMap DEFAULT_COLOR_1 = new Graytone(1);

    protected static final ColorMap DEFAULT_COLOR_2 = new Graytone(0);

    ChannelMap color1 = null;

    ChannelMap color2 = null;

    public static final NType $TYPE;

    public static final NType.Field color1$FIELD;

    public static final NType.Field color2$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(Checker.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((Checker) o).color1 = (ChannelMap) value;
                    return;
                case 1:
                    ((Checker) o).color2 = (ChannelMap) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((Checker) o).color1;
                case 1:
                    return ((Checker) o).color2;
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new Checker());
        $TYPE.addManagedField(color1$FIELD = new _Field("color1", 0 | _Field.FCO, de.grogra.reflect.ClassAdapter.wrap(ChannelMap.class), null, 0));
        $TYPE.addManagedField(color2$FIELD = new _Field("color2", 0 | _Field.FCO, de.grogra.reflect.ClassAdapter.wrap(ChannelMap.class), null, 1));
        $TYPE.setSpecialEdgeField(color1$FIELD, COLOR);
        $TYPE.setSpecialEdgeField(color2$FIELD, COLOR_2);
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Checker();
    }

    public int getAverageColor() {
        int c1 = ((color1 instanceof ColorMap) ? (ColorMap) color1 : DEFAULT_COLOR_1).getAverageColor();
        int c2 = ((color2 instanceof ColorMap) ? (ColorMap) color2 : DEFAULT_COLOR_2).getAverageColor();
        return ((c1 & 0xfffefefe) + (c2 & 0xfffefefe)) >> 1;
    }

    public ChannelMap getColor1() {
        return (color1 instanceof ColorMap) ? color1 : DEFAULT_COLOR_1;
    }

    public ChannelMap getColor2() {
        return (color2 instanceof ColorMap) ? color2 : DEFAULT_COLOR_2;
    }

    @Override
    public float getFloatValue(ChannelData data, int channel) {
        ChannelData in = getInputData(data);
        float u = in.getFloatValue(data, Channel.U);
        float v = in.getFloatValue(data, Channel.V);
        boolean b = ((Math2.floor(2 * u) ^ Math2.floor(2 * v)) & 1) == 0;
        ChannelMap c1 = (color1 != null) ? color1 : DEFAULT_COLOR_1;
        ChannelMap c2 = (color2 != null) ? color2 : DEFAULT_COLOR_2;
        ChannelData cc = data.getData(b ? c2 : c1);
        boolean ddu;
        if ((ddu = (channel >= Channel.DPXDU) && (channel <= Channel.DPZDU)) || ((channel >= Channel.DPXDV) && (channel <= Channel.DPZDV))) {
            float d = 0.05f;
            if (ddu) {
                u += d;
            } else {
                v += d;
            }
            if ((((Math2.floor(2 * u) ^ Math2.floor(2 * v)) & 1) == 0) != b) {
                u = cc.getFloatValue(data, channel & 3);
                u = (data.getData(b ? c1 : c2).getFloatValue(data, channel & 3) - u) / d;
            } else {
                u = cc.getFloatValue(data, channel);
            }
        } else {
            u = cc.getFloatValue(data, channel);
        }
        return u;
    }
}
