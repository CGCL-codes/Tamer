package javax.sound.midi.util;

public class TimeSignature extends SemanticMetaMessage {

    public static final int TYPE = 88;

    protected byte numerator;

    protected byte denominatorNegatedExponent;

    @Override
    protected int getType() {
        return TYPE;
    }

    @Override
    protected byte[] getData() {
        return new byte[] { numerator, denominatorNegatedExponent, 24, 8 };
    }

    public byte getNumerator() {
        return numerator;
    }

    public void setNumerator(byte numerator) {
        this.numerator = numerator;
    }

    public byte getDenominatorNegatedExponent() {
        return denominatorNegatedExponent;
    }

    public void setDenominatorNegatedExponent(byte denominatorNegatedExponent) {
        this.denominatorNegatedExponent = denominatorNegatedExponent;
    }
}
