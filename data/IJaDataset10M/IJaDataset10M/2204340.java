package de.fraunhofer.ipsi.xpathDatatypes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

public class XS_Double extends XS_AnyAtomicType implements Numeric {

    public static final QName TYPENAME = new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "double", "xs");

    /**
	 * Method typeName
	 *
	 * @return   a QName
	 *
	 */
    public QName typeName() {
        return TYPENAME;
    }

    public static final XS_Double ONE = new XS_Double(1d);

    public static final XS_Double ZERO = new XS_Double(0d);

    public static final XS_Double NaN = new XS_Double(Double.NaN);

    private static final DecimalFormat formatter = new DecimalFormat("0.0E0");

    static {
        formatter.setMaximumFractionDigits(309);
        DecimalFormatSymbols sym = formatter.getDecimalFormatSymbols();
        sym.setDecimalSeparator('.');
        sym.setInfinity("INF");
        sym.setNaN("NaN");
        formatter.setDecimalFormatSymbols(sym);
    }

    private final double value;

    /**
	 * Constructor
	 *
	 * @param    value               a  double
	 *
	 */
    protected XS_Double(double value) {
        this.value = value;
    }

    /**
	 * Constructor
	 *
	 * @param    value               a  String
	 *
	 */
    private XS_Double(String value) {
        double d;
        try {
            d = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            try {
                d = formatter.parse(value).doubleValue();
            } catch (ParseException ex) {
                throw e;
            }
        }
        this.value = d;
    }

    /**
	 * Method doubleValue
	 *
	 * @return   a double
	 *
	 */
    public double doubleValue() {
        return value;
    }

    /**
	 * Method compareTo
	 *
	 * @param    o                   an Object
	 *
	 * @return   an int
	 *
	 */
    public int compareTo(Numeric num) {
        Double thisDouble = new Double(value);
        Double otherDouble = new Double(valueOf((XS_AnyAtomicType) num).value);
        if ((Double.compare(thisDouble.doubleValue(), NaN.doubleValue()) == 0) && Double.compare(otherDouble.doubleValue(), NaN.doubleValue()) == 0) return -1; else return new Double(value).compareTo(new Double(valueOf((XS_AnyAtomicType) num).value));
    }

    /**
	 * Method add
	 *
	 * @param    b                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    public Numeric add(Numeric b) {
        return new XS_Double(value + valueOf((XS_AnyAtomicType) b).value);
    }

    /**
	 * Method subtract
	 *
	 * @param    b                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    public Numeric subtract(Numeric b) {
        return new XS_Double(value - valueOf((XS_AnyAtomicType) b).value);
    }

    /**
	 * Method multiply
	 *
	 * @param    b                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    public Numeric multiply(Numeric b) {
        return new XS_Double(value * valueOf((XS_AnyAtomicType) b).value);
    }

    /**
	 * Method divide
	 *
	 * @param    b                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    public Numeric divide(Numeric b) {
        return new XS_Double(value / valueOf((XS_AnyAtomicType) b).value);
    }

    /**
	 * Method idivide
	 *
	 * @param    b                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    public Numeric idivide(Numeric b) {
        return new XS_Integer((new BigDecimal(value / valueOf((XS_AnyAtomicType) b).value)).toBigInteger());
    }

    /**
	 * Method mod
	 *
	 * @param    b                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    public Numeric mod(Numeric b) {
        return new XS_Double(value % valueOf((XS_AnyAtomicType) b).value);
    }

    /**
	 * Method negate
	 *
	 * @return   a Numeric
	 *
	 */
    public Numeric negate() {
        return new XS_Double(-value);
    }

    /**
	 * Method equals
	 *
	 * @param    o                   an Object
	 *
	 * @return   a boolean
	 *
	 */
    public boolean equals(Object o) {
        return o instanceof Numeric && this.compareTo((Numeric) o) == 0;
    }

    /**
	 * Method abs
	 *
	 * @return   a Numeric
	 *
	 */
    public Numeric abs() {
        return new XS_Double(Math.abs(value));
    }

    /**
	 *
	 * Method floor
	 *
	 * @return    a Numeric
	 *
	 */
    public Numeric floor() {
        return new XS_Double(Math.floor(value));
    }

    /**
	 *
	 * Method ceiling
	 *
	 * @return    a Numeric
	 *
	 */
    public Numeric ceiling() {
        return new XS_Double(Math.ceil(value));
    }

    /**
	 *
	 * Method round
	 *
	 * @return    a Numeric
	 *
	 */
    public Numeric round() {
        return new XS_Double(Math.floor(value + 0.5f));
    }

    /**
	 *
	 * Method roundHalfEven
	 *
	 * @return  a Numeric
	 *
	 */
    public Numeric roundHalfToEven(int n) {
        return new XS_Double(BigDecimal.valueOf(value).setScale(n, BigDecimal.ROUND_HALF_EVEN).doubleValue());
    }

    /**
	 * Method toString
	 *
	 * @return   a String
	 *
	 */
    public String toString() {
        String result = null;
        double absValue = value;
        if (absValue < 0) absValue *= -1;
        if (absValue >= 0.000001 && absValue < 1000000) {
            result = new XS_Decimal(BigDecimal.valueOf(value)).toString();
        } else if (absValue == 0.0) {
            if (1 / value < 0.0) result = "-0"; else result = "0";
        } else if (Double.isInfinite(value)) {
            if (value < 0) result = "-INF"; else result = "INF";
        } else if (Double.isNaN(value)) {
            result = "NaN";
        } else {
            result = formatter.format(value);
        }
        return result;
    }

    /**
	 * Method valueOf
	 *
	 * @param    d                   a  XS_AnyAtomicType
	 *
	 * @throws IllegalArgumentException
	 *
	 * @return   a XS_Double
	 *
	 */
    public static XS_Double valueOf(XS_AnyAtomicType d) {
        if (d instanceof XS_Double) {
            return new XS_Double(((XS_Double) d).value);
        } else if (d instanceof XS_Float) {
            return new XS_Double((double) ((XS_Float) d).floatValue());
        } else if (d instanceof XS_Boolean) {
            return ((XS_Boolean) d).booleanValue() ? ONE : ZERO;
        } else if (d instanceof XS_Decimal || d instanceof XS_String || d instanceof XS_UntypedAtomic) {
            return new XS_Double(d.toString());
        } else throw new IllegalArgumentException();
    }

    /**
	 * Method valueOf
	 *
	 * @param    s                   a  String
	 *
	 * @return   a XS_Double
	 *
	 */
    public static XS_Double valueOf(String s) {
        return new XS_Double(s);
    }

    /**
	 * Method valueOf
	 *
	 * @param    d                   a  double
	 *
	 * @return   a XS_Double
	 *
	 */
    public static XS_Double valueOf(double d) {
        return new XS_Double(d);
    }
}
