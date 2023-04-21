package eu.actorsproject.xlim.type;

/**
 * The "real" type (64-bit IEEE-754 double precision)
 *
 */
class RealType extends UnparametricType {

    RealType() {
        super("real");
    }

    @Override
    public int getSize() {
        return 64;
    }

    @Override
    public boolean isZero(String s) {
        try {
            return Double.doubleToRawLongBits(Double.valueOf(s)) == 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public String getZero() {
        return "0.0";
    }

    @Override
    public long minValue() {
        return 0xfff0000000000000L;
    }

    @Override
    public long maxValue() {
        return 0x7ff0000000000000L;
    }
}
