package de.intarsys.pdf.cos;

/**
 * Represents floating point numbers in pdf.
 */
public class COSFixed extends COSNumber {

    public static final int DEFAULT_PRECISION = 5;

    public static COSFixed create(byte[] bytes, int start, int length) {
        long result = 0;
        long decimal = 1;
        int end = start + length;
        boolean negative = false;
        boolean point = false;
        int precision = 0;
        int i = start;
        byte prefix = bytes[i];
        if (prefix == '+') {
            i++;
        } else if (prefix == '-') {
            negative = true;
            i++;
        }
        for (; i < end; i++) {
            byte digit = bytes[i];
            if (digit == '.') {
                point = true;
            } else {
                result = ((result * 10) + digit) - '0';
                if (point) {
                    decimal = decimal * 10;
                    precision++;
                }
            }
        }
        if (negative) {
            return new COSFixed(-(float) ((double) result / (double) decimal), precision);
        }
        return new COSFixed((float) ((double) result / (double) decimal), precision);
    }

    public static COSFixed create(double value) {
        return new COSFixed((float) value, DEFAULT_PRECISION);
    }

    public static COSFixed create(double value, int precision) {
        return new COSFixed((float) value, precision);
    }

    public static COSFixed create(float value) {
        return new COSFixed(value, DEFAULT_PRECISION);
    }

    public static COSFixed create(float value, int precision) {
        return new COSFixed(value, precision);
    }

    private final float floatValue;

    private int precision;

    protected COSFixed(float value, int precision) {
        this.floatValue = value;
        this.precision = precision;
    }

    @Override
    public java.lang.Object accept(ICOSObjectVisitor visitor) throws COSVisitorException {
        return visitor.visitFromFixed(this);
    }

    @Override
    public COSFixed asFixed() {
        return this;
    }

    @Override
    protected String basicToString() {
        return String.valueOf(floatValue);
    }

    @Override
    protected COSObject copyBasic() {
        return new COSFixed(floatValue, precision);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof COSFixed)) {
            return false;
        }
        return floatValue == ((COSFixed) o).floatValue;
    }

    @Override
    public float floatValue() {
        return floatValue;
    }

    /**
	 * The precision (digits after period) for this.
	 * 
	 * @return The precision (digits after period) for this.
	 */
    public int getPrecision() {
        return precision;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(floatValue);
    }

    @Override
    public int intValue() {
        return (int) floatValue;
    }

    @Override
    public void restoreState(Object object) {
        super.restoreState(object);
        COSFixed fixed = (COSFixed) object;
        this.precision = fixed.precision;
    }

    public Object saveState() {
        COSFixed result = new COSFixed(floatValue, precision);
        result.container = this.container.saveStateContainer();
        return result;
    }

    /**
	 * Assign the precision for this.
	 * 
	 * @param precision
	 *            The new precision.
	 */
    public void setPrecision(int precision) {
        this.precision = precision;
    }
}
