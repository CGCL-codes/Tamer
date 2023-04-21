package at.ac.tuwien.j3dvnaddoncollection.conversion;

import java.util.Date;
import javax.vecmath.Color3f;
import at.ac.tuwien.j3dvn.control.ConversionAddon;
import at.ac.tuwien.j3dvn.control.IProperty;
import at.ac.tuwien.j3dvn.control.Property;

/**
 * 
 */
public class DateToColor implements ConversionAddon<Date, Color3f> {

    private static final String NAME = "Date to Color";

    private Long min = null;

    private Long max;

    private static final String BORDER_PROPERTY = "border";

    private static final String MIN_COLOR_PROPERTY = "min color";

    private static final String MAX_COLOR_PROPERTY = "max color";

    private static final String MED_COLOR_PROPERTY = "med color";

    private Float fBorder = .2f;

    private final IProperty<Float> pBorder = new IProperty<Float>() {

        public Float getValue() {
            return fBorder;
        }

        public void setValue(Float value) {
            if (value == null) fMaxColor.set(1, 1, 1); else fBorder = value;
        }
    };

    @Property(BORDER_PROPERTY)
    public IProperty<Float> border() {
        return pBorder;
    }

    private Color3f fMinColor = new Color3f(0, 0, 0);

    private final IProperty<Color3f> pMinColor = new IProperty<Color3f>() {

        public Color3f getValue() {
            return fMinColor;
        }

        public void setValue(Color3f value) {
            if (value == null) fMinColor.set(0, 0, 0); else fMinColor = value;
        }
    };

    @Property(MIN_COLOR_PROPERTY)
    public IProperty<Color3f> minColor() {
        return pMinColor;
    }

    private Color3f fMaxColor = new Color3f(1, 1, 1);

    private final IProperty<Color3f> pMaxColor = new IProperty<Color3f>() {

        public Color3f getValue() {
            return fMaxColor;
        }

        public void setValue(Color3f value) {
            fMaxColor = value;
        }
    };

    @Property(MAX_COLOR_PROPERTY)
    public IProperty<Color3f> maxColor() {
        return pMaxColor;
    }

    private Color3f fMedColor = new Color3f(.5f, .5f, .5f);

    private final IProperty<Color3f> pMedColor = new IProperty<Color3f>() {

        public Color3f getValue() {
            return fMedColor;
        }

        public void setValue(Color3f value) {
            if (value == null) {
                fMedColor.set((fMaxColor.x + fMinColor.x) / 2, (fMaxColor.y + fMinColor.y) / 2, (fMaxColor.z + fMinColor.z) / 2);
            } else fMedColor = value;
        }
    };

    @Property(MED_COLOR_PROPERTY)
    public IProperty<Color3f> medColor() {
        return pMedColor;
    }

    public String getName() {
        return NAME;
    }

    public Color3f convert(Date inputValue) {
        float floatValue = new Float(inputValue.getTime());
        float normValue;
        if (inputValue == null) normValue = 0f; else if ((min != null) && (max != null)) normValue = (floatValue - min) / (max - min); else normValue = floatValue;
        Color3f result;
        if (normValue < .5) {
            result = getGradientValue(normValue, fMinColor, fMedColor);
        } else {
            result = getGradientValue(normValue, fMedColor, fMaxColor);
        }
        return result;
    }

    private Color3f getGradientValue(float value, Color3f min, Color3f max) {
        return new Color3f(min.x + (max.x - min.x) * value, min.y + (max.y - min.y) * value, min.z + (max.z - min.z) * value);
    }

    public void setMinMax(Date min, Date max) {
        this.min = min.getTime();
        this.max = max.getTime();
    }
}
