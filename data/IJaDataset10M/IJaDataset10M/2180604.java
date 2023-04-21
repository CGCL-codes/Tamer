package de.maramuse.soundcomp.parser;

import de.maramuse.soundcomp.parser.SCParser.ParserVal;

/**
 * this symbol class represents a fractional note length value
 */
public class Fraction extends ParserVal {

    static Number n1 = new Number(1);

    private Number num = null, denom = n1;

    Fraction() {
        super(SCParser.FRACTION);
    }

    Fraction(ParserVal denom) {
        super(SCParser.FRACTION);
        this.denom = (Number) denom;
        num = n1;
    }

    Fraction(ParserVal denom, ParserVal num) {
        super(SCParser.FRACTION);
        this.denom = (Number) denom;
        this.num = (Number) num;
    }

    public double getNum() {
        if (num == null) return Double.NaN;
        return num.getDouble();
    }

    public double getDenom() {
        if (denom == null) return Double.NaN;
        return denom.getDouble();
    }

    public double getLength() {
        if (denom == null || num == null || denom.getDouble() == 0d) return Double.NaN;
        return num.getDouble() / denom.getDouble();
    }

    @Override
    public double asDouble() {
        return getLength();
    }
}
