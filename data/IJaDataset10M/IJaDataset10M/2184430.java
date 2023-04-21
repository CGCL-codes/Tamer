package jscicalc.complex;

import jscicalc.Base;
import jscicalc.complex.Complex;

/**
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class Format {

    /**
     * Constructor. This takes a Complex z and a Base b.
     * @param z The complex number
     * @param b The base
     */
    public Format(Complex z, Base b) {
        this.z = z;
        real = new DoubleFormat(z.real(), b);
        imaginary = new DoubleFormat(Math.abs(z.imaginary()), b);
        abs = new DoubleFormat(z.abs(), b);
        argument = new DoubleFormat(z.arg(), b);
        imaginarySign = z.imaginary() < 0;
        realSign = z.real() < 0;
    }

    /**
     * This is an enumerated type representing the possible
     * notations that we can use.
     *
     * @author J. D. Lamb 
     * @version 1.0
     */
    public enum Notation {

        STANDARD, SCIENTIFIC, SCIENTIFICB, NONE
    }

    /**
     * Used to replace hyphens with true minus signs
     * @param s A String
     * @return The same String with hyphens replaced by minus signs
     */
    private static String correctMinus(String s) {
        return s;
    }

    /**
     * Method that creates a string (ideally in standard notation) representation
     * of a number to sigDigits significant digits. If maxLength is exceeded it
     * resorts to scientific notation.
     * @param base Base: mut be 2, 8, 10 or 16
     * @param sigDigits Desired number of significant digits
     * @param maxLength The maximum number of characters in the string produced
     * @param showComplex Set to true if you want to show imaginary part
     * even when smaller than smallImaginary.
     * @param rectangularComplex Set to true if you want complex numbers in
     * rectangular notation.
     * @return a string representation of d
     */
    public String formatStandard(Base base, int sigDigits, int maxLength, boolean showComplex, boolean rectangularComplex) {
        if (rectangularComplex) {
            int rDigits = sigDigits;
            {
                int p = real.precision(1000);
                if (p < rDigits) rDigits = p;
            }
            int iDigits = sigDigits;
            {
                int p = imaginary.precision(1000);
                if (p < iDigits) iDigits = p;
            }
            real = new DoubleFormat(z.real(), base);
            real.round(rDigits);
            imaginary = new DoubleFormat(Math.abs(z.imaginary()), base);
            imaginary.round(iDigits);
            if (showComplex) {
                int ml = maxLength - 2;
                int il = ml / 2;
                int rl = il;
                if (il + rl < ml && realSign) ++rl;
                Notation ifm = Notation.STANDARD;
                if (imaginary.precision(il) < iDigits) {
                    if (imaginary.scientificPrecision(il) < iDigits) {
                        if (base == Base.BINARY) if (imaginary.scientificPrecision(il) < iDigits) {
                            ifm = Notation.NONE;
                        } else {
                            ifm = Notation.SCIENTIFICB;
                        } else ifm = Notation.NONE;
                    } else ifm = Notation.SCIENTIFIC;
                }
                Notation rfm = Notation.STANDARD;
                if (real.precision(rl) < rDigits) {
                    if (real.scientificPrecision(rl) < rDigits) {
                        if (base == Base.BINARY) if (real.scientificPrecision(rl) < rDigits) {
                            rfm = Notation.NONE;
                        } else {
                            rfm = Notation.SCIENTIFICB;
                        } else rfm = Notation.NONE;
                    } else rfm = Notation.SCIENTIFIC;
                }
                while (rfm == Notation.NONE || ifm == Notation.NONE) {
                    if (rfm == Notation.NONE && ifm == Notation.NONE) {
                        --rDigits;
                        --iDigits;
                        if (base == Base.BINARY) {
                            if (imaginary.scientificBPrecision(il) >= iDigits) ifm = Notation.SCIENTIFICB;
                            if (real.scientificBPrecision(rl) >= rDigits) rfm = Notation.SCIENTIFICB;
                        } else {
                            if (imaginary.scientificPrecision(il) >= iDigits) ifm = Notation.SCIENTIFIC;
                            if (real.scientificPrecision(rl) >= rDigits) rfm = Notation.SCIENTIFIC;
                        }
                    } else if (rfm == Notation.NONE) {
                        if (base == Base.BINARY) {
                            while (imaginary.scientificBPrecision(il - 1) >= iDigits) --il;
                        } else {
                            while (imaginary.scientificPrecision(il - 1) >= iDigits) --il;
                        }
                        if (rl + il < ml) {
                            rl = ml - il;
                            if (base == Base.BINARY) {
                                if (real.scientificBPrecision(rl) >= rDigits) rfm = Notation.SCIENTIFICB;
                            } else {
                                if (real.scientificPrecision(rl) >= rDigits) rfm = Notation.SCIENTIFIC;
                            }
                        } else {
                            if (iDigits == rDigits) {
                                --rDigits;
                            } else {
                                --iDigits;
                            }
                        }
                    } else {
                        if (base == Base.BINARY) {
                            while (real.scientificBPrecision(rl - 1) >= rDigits) --rl;
                        } else {
                            while (real.scientificPrecision(rl - 1) >= rDigits) --rl;
                        }
                        if (rl + il < ml) {
                            il = ml - rl;
                            if (base == Base.BINARY) {
                                if (imaginary.scientificBPrecision(il) >= iDigits) ifm = Notation.SCIENTIFICB;
                            } else {
                                if (imaginary.scientificPrecision(il) >= iDigits) ifm = Notation.SCIENTIFIC;
                            }
                        } else {
                            if (iDigits == rDigits) {
                                --iDigits;
                            } else {
                                --rDigits;
                            }
                        }
                    }
                }
                real.round(rDigits);
                String realString = null;
                switch(rfm) {
                    case STANDARD:
                        realString = real.standard();
                        break;
                    case SCIENTIFIC:
                        realString = real.scientific();
                        break;
                    case SCIENTIFICB:
                        realString = real.scientificB();
                        break;
                    default:
                        realString = "*";
                        break;
                }
                imaginary.round(iDigits);
                String imaginaryString = null;
                switch(ifm) {
                    case STANDARD:
                        imaginaryString = imaginary.standard();
                        break;
                    case SCIENTIFIC:
                        imaginaryString = imaginary.scientific();
                        break;
                    case SCIENTIFICB:
                        imaginaryString = imaginary.scientificB();
                        break;
                    default:
                        imaginaryString = "*";
                        break;
                }
                if (z.imaginary() == 1 || z.imaginary() == -1) imaginaryString = new String();
                if (imaginarySign) {
                    return correctMinus(realString + "-" + imaginaryString + "i");
                } else {
                    return correctMinus(realString + "+" + imaginaryString + "i");
                }
            } else {
            }
        } else {
            abs = new DoubleFormat(z.abs(), base);
            abs.round(sigDigits);
            argument = new DoubleFormat(z.arg(), base);
            argument.round(sigDigits);
        }
        return null;
    }

    /**
     * A calculation (e.g. sin) might create a small imaginary part. If it is smaller
     * than this number in size AND showComplex == false, don&rsquo;t show it.
     */
    private static final double SMALLIMAGINARY = 0.00005;

    /**
     * Test function
     */
    public static void main(String[] args) {
        Complex c = new Complex(4, 5);
        Format f = new Format(c, Base.DECIMAL);
        System.out.println("--------------------");
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(0, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(0, 1);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(1, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(0.002, -1);
        f = new Format(c, Base.DECIMAL);
        System.out.println("--------------------");
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(-1, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(-12, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(123, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(1234, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 5, 20, true, true));
        c = new Complex(12345, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(123456, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(1234567, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(12345678, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(123456789, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(1234567891.0, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(12345678912.0, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(123456789123.0, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(1234567891234.0, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(12345678912345.0, 0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(1234.0, -4321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(1234.0, -4321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(12345.0, -54321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(123456.0, -654321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(1234567.0, -7654321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(12345678.0, -87654321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(123456789.0, -987654321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(234567891.0, -1987654321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(1234567891.0, -987654321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
        c = new Complex(1234567891.0, -1987654321.0);
        f = new Format(c, Base.DECIMAL);
        System.out.println(f.formatStandard(Base.DECIMAL, 9, 20, true, true));
    }

    /**
     * This is where the number is stored
     */
    private final Complex z;

    /**
     * The real part
     */
    private DoubleFormat real;

    /**
     * The imaginary part
     */
    private DoubleFormat imaginary;

    /**
     * The absoulte value
     */
    private DoubleFormat abs;

    /**
     * The argument
     */
    private DoubleFormat argument;

    /**
     * Sign of real part (true for negative)
     */
    boolean realSign;

    /**
     * Sign of imaginary part (true for negative)
     */
    boolean imaginarySign;

    /**
     * A private inner class that is similar in structure to ParseBase.Number.
     * It is used by Format to represent a double in a form from whch
     * the digits can be extracted as characters. 
     * @see ParseBase.Number
     */
    private static class DoubleFormat {

        /**
	 * The constructor takes a double and a base
	 * @param number The double we want to format
	 * @param base The Base
	 */
        DoubleFormat(double number, Base base) {
            digits = new java.util.Vector<Integer>();
            exponentDigits = new java.util.Vector<Integer>();
            infinity = Double.isInfinite(number);
            NaN = Double.isNaN(number);
            zero = number == 0;
            switch(base) {
                case BINARY:
                    this.base = 2;
                    formatBin(number);
                    break;
                case OCTAL:
                    this.base = 8;
                    formatOct(number);
                    break;
                case HEXADECIMAL:
                    this.base = 16;
                    formatHex(number);
                    break;
                default:
                    this.base = 10;
                    formatDec(number);
                    break;
            }
        }

        /**
	 * Rounds to sigDigits significant digits. We round 0.5 (or 0.1, 0.4, 0.8)
	 * up to nearest whole and get an aswer with sigDigits digits in it.
	 * @param sigDigits The desired number of significant digits
	 */
        public void round(final int sigDigits) {
            if (sigDigits >= digits.size()) return;
            boolean carry = false;
            for (int i = digits.size() - 1; i >= 0; --i) {
                if (i > sigDigits) digits.removeElementAt(i); else if (i == sigDigits) {
                    int digit = digits.elementAt(i);
                    digits.removeElementAt(i);
                    if (2 * digit < base) break;
                } else {
                    int digit = digits.elementAt(i);
                    digit = (digit + 1) % base;
                    digits.setElementAt(digit, i);
                    if (digit != 0) break;
                    carry = i == 0;
                }
            }
            if (carry) {
                digits.add(0, 1);
                digits.removeElementAt(sigDigits);
                if (exponentNegative) {
                    for (int i = exponentDigits.size() - 1; i >= 0; --i) {
                        int digit = exponentDigits.elementAt(i);
                        digit = (digit - 1) % base;
                        exponentDigits.setElementAt(digit, i);
                        if (digit == 0) break;
                        carry = i == 0;
                    }
                    if (carry) exponentDigits.removeElementAt(0);
                } else for (int i = exponentDigits.size() - 1; i >= 0; --i) {
                    int digit = exponentDigits.elementAt(i);
                    digit = (digit + 1) % base;
                    exponentDigits.setElementAt(digit, i);
                    if (digit != 0) break;
                    carry = i == 0;
                }
                if (carry) exponentDigits.insertElementAt(1, 0);
            }
        }

        /**
	 * The number of significant digits shown if the string representation is
	 * compressed into the given length and shown in standard notation.
	 * The function returns zero if the string cannot be compressed that much.
	 * @param maxLength The desired maximum length
	 * @return The number of significant digits that would result
	 */
        public int precision(int maxLength) {
            if (NaN) return maxLength > 2 ? 1 : 0;
            if (zero) return maxLength > 0 ? 1 : 0;
            if (infinity) if (negative) return maxLength > 1 ? 1 : 0; else return maxLength > 0 ? 1 : 0;
            int length = digits.size();
            for (int i = digits.size() - 1; digits.elementAt(i) == 0; --i) --length;
            if (exponentNegative) {
                length += exponent + 1;
                if (negative) ++length;
                int result = digits.size() - (length - maxLength);
                if (result > digits.size()) result = digits.size();
                if (result < 0) result = 0;
                return result;
            } else {
                if (length > exponent + 1) {
                    int basedigits = length - (exponent + 1);
                    ++length;
                    if (negative) ++length;
                    if (length <= maxLength) return digits.size(); else if (length - maxLength > basedigits + 1) return 0; else if (length - maxLength == basedigits + 1) return digits.size() - basedigits; else return digits.size() - (length - maxLength);
                } else {
                    length = exponent + 1;
                    if (negative) ++length;
                    if (length <= maxLength) return digits.size(); else return 0;
                }
            }
        }

        /**
	 * The number of significant digits shown if the string representation is
	 * compressed into the given length and shown in scientific notation.
	 * The function returns zero if the string cannot be compressed that much.
	 * @param maxLength The desired maximum length
	 * @return The number of significant digits that would result
	 */
        public int scientificPrecision(int maxLength) {
            if (NaN) return maxLength > 2 ? 1 : 0;
            if (zero) return maxLength > 0 ? 1 : 0;
            if (infinity) if (negative) return maxLength > 1 ? 1 : 0; else return maxLength > 0 ? 1 : 0;
            int z = digits.size();
            for (int i = digits.size() - 1; digits.elementAt(i) == 0; --i) --z;
            int length = scientificLength();
            if (maxLength >= length) return z; else if (length - maxLength < z - 1) return z - (length - maxLength); else if (length - maxLength <= z) return 1; else return 0;
        }

        /**
	 * The number of significant digits shown if the string representation is
	 * compressed into the given length and shown in scientific (binary) notation.
	 * The function returns zero if the string cannot be compressed that much.
	 * @param maxLength The desired maximum length
	 * @return The number of significant digits that would result
	 */
        public int scientificBPrecision(int maxLength) {
            if (NaN) return maxLength > 2 ? 1 : 0;
            if (zero) return maxLength > 0 ? 1 : 0;
            if (infinity) if (negative) return maxLength > 1 ? 1 : 0; else return maxLength > 0 ? 1 : 0;
            int z = digits.size();
            for (int i = digits.size() - 1; digits.elementAt(i) == 0; --i) --z;
            int length = scientificBLength();
            if (maxLength >= length) return z; else if (length - maxLength < z - 1) return z - (length - maxLength); else if (length - maxLength <= z) return 1; else return 0;
        }

        /**
	 * The length of string that would be produced in standard notation.
	 * @return The length of the string that standard() would produce
	 */
        public int length() {
            if (NaN) return 3;
            if (zero) return 1;
            if (infinity) if (negative) return 2; else return 1;
            int length = digits.size();
            for (int i = digits.size() - 1; digits.elementAt(i) == 0; --i) --length;
            if (exponentNegative) {
                length += exponent + 1;
            } else {
                if (length > exponent + 1) ++length; else length = exponent + 1;
            }
            if (negative) ++length;
            return length;
        }

        /**
	 * The length of string that would be produced in scientific notation.
	 * @return The length of the string that scientific() would produce
	 */
        public int scientificLength() {
            if (NaN) return 3;
            if (zero) return 1;
            if (infinity) if (negative) return 2; else return 1;
            int length = 4;
            if (negative) ++length;
            if (exponentNegative) ++length;
            int z = digits.size();
            for (int i = digits.size() - 1; digits.elementAt(i) == 0; --i) --z;
            length += z;
            if (z == 1) --length;
            z = 0;
            for (int i = 0; i < exponentDigits.size() && exponentDigits.elementAt(i) == 0; ++i) ++z;
            length += exponentDigits.size() - z;
            return length;
        }

        /**
	 * Calculate the number of digits in the exponent. This is used to help
	 * decide when to use scientific notation.
	 * @return The number of digits in the exponent
	 */
        public int exponentDigits() {
            int z = 0;
            for (int i = 0; i < exponentDigits.size() && exponentDigits.elementAt(i) == 0; ++i) ++z;
            return exponentDigits.size() - z;
        }

        /**
	 * Produce a string representing the number without using
	 * scientific notation. This can produce very long strings.
	 * @return String representation of number without an exponent
	 */
        public String standard() {
            if (NaN) return "NaN";
            if (zero) return "0";
            if (infinity) if (negative) return "-&#8734;"; else return "&#8734;";
            StringBuilder stringBuffer = new StringBuilder();
            int z = digits.size();
            if (z > 0) for (int i = digits.size() - 1; digits.elementAt(i) == 0; --i) --z;
            if (negative) stringBuffer.append("-");
            if (exponentNegative) {
                stringBuffer.append("0.");
                for (int i = 0; i < exponent - 1; ++i) stringBuffer.append("0");
                for (int i = 0; i < z; ++i) stringBuffer.append(getDigit(digits.elementAt(i)));
            } else {
                for (int i = 0; i < Math.max(exponent, digits.size()); ++i) {
                    if (i >= Math.max(exponent + 1, z)) break;
                    if (i == exponent + 1) stringBuffer.append(".");
                    if (i < digits.size()) stringBuffer.append(getDigit(digits.elementAt(i))); else stringBuffer.append("0");
                }
            }
            return stringBuffer.toString();
        }

        /**
	 * Produce a string representing the number using
	 * scientific notation. All significant digits are used; so use round() to
	 * shorten if necessary.
	 * @return String representation of number with an exponent
	 */
        public String scientific() {
            if (NaN) return "NaN";
            if (zero) return "0&#215;10<sup>0</sup>";
            if (infinity) if (negative) return "-&#8734;"; else return "&#8734;";
            StringBuilder stringBuffer = new StringBuilder();
            int z = digits.size();
            for (int i = digits.size() - 1; digits.elementAt(i) == 0; --i) --z;
            if (negative) stringBuffer.append("-");
            stringBuffer.append(getDigit(digits.elementAt(0)));
            if (z > 1) stringBuffer.append(".");
            for (int i = 1; i < z; ++i) stringBuffer.append(getDigit(digits.elementAt(i)));
            stringBuffer.append("&#215;10<sup>");
            if (exponent == 0) stringBuffer.append("0"); else {
                if (exponentNegative) stringBuffer.append("-");
                z = 0;
                for (int i = 0; exponentDigits.elementAt(i) == 0; ++i) ++z;
                for (int i = z; i < exponentDigits.size(); ++i) stringBuffer.append(getDigit(exponentDigits.elementAt(i)));
            }
            stringBuffer.append("</sup>");
            return stringBuffer.toString();
        }

        /**
	 * Produce a string representing the binary number using
	 * scientific notation. All significant digits are used; so use round() to
	 * shorten if necessary. The exponent is rewritten as x2{decimal integer} if
	 * number is binary
	 * @return String representation of number with an exponent
	 */
        public String scientificB() {
            if (base != 2) return scientific();
            String s = scientific();
            String t = "";
            int i = 0;
            for (; s.charAt(i) != ';'; ++i) t += s.charAt(i);
            t += ";2<sup>";
            ++i;
            ++i;
            ++i;
            ++i;
            ++i;
            ++i;
            ++i;
            if (s.charAt(++i) == '-') ++i;
            int exp = 0;
            for (; s.charAt(i) != '<'; ++i) {
                exp <<= 1;
                if (s.charAt(i) == '1') exp |= 1;
            }
            t += Integer.toString(exp);
            t += "</sup>";
            return t;
        }

        /**
	 * The length of string that would be produced in scientific notation,
	 * changing a binary to 2x{decimal integer}
	 * @return The length of the string that scientific() would produce
	 */
        public int scientificBLength() {
            if (base != 2) return scientificLength();
            int l = scientificLength();
            String s = scientific();
            int b = 0;
            int i = 0;
            for (; s.charAt(i) != ';'; ++i) ;
            ++i;
            ++i;
            ++i;
            ++i;
            ++i;
            ++i;
            ++i;
            if (s.charAt(++i) == '-') ++i;
            int exp = 0;
            for (; s.charAt(i) != '<'; ++i) {
                ++b;
                exp <<= 1;
                if (s.charAt(i) == '1') exp |= 1;
            }
            int d = (int) (Math.floor(Math.log10(exp)));
            return l - b + d - 1;
        }

        /**
	 * This method handles formatting decimals
	 * @param number The double to format
	 */
        private void formatDec(double number) {
            if (Double.isNaN(number)) {
                NaN = true;
                return;
            }
            if (Double.isInfinite(number)) {
                infinity = true;
                long bits = Double.doubleToRawLongBits(number);
                negative = (bits & 0x8000000000000000L) != 0;
                return;
            }
            if (number == 0) {
                zero = true;
                return;
            }
            String s = Double.toString(number);
            boolean readingSignificand = true;
            boolean readPoint = false;
            boolean firstDigitRead = false;
            int leadingDigits = 0;
            exponent = 0;
            exponentNegative = false;
            negative = false;
            for (Character c : s.toCharArray()) {
                if (readingSignificand) {
                    if (c == '-') negative = true; else if (readPoint == false && c == '.') readPoint = true; else if (c == 'E') readingSignificand = false; else {
                        int digit = getInt(c);
                        if (firstDigitRead) {
                            firstDigitRead = true;
                            if (digit != 0) ++leadingDigits;
                        } else {
                            if (!readPoint) ++leadingDigits;
                        }
                        digits.add(digit);
                    }
                } else {
                    if (c == '-') exponentNegative = true; else {
                        int digit = getInt(c);
                        if (true || digit > 0) {
                            exponent *= 10;
                            exponent += digit;
                        }
                    }
                }
            }
            exponent += leadingDigits - 1;
            for (Character c : Integer.toString(exponent).toCharArray()) exponentDigits.add(getInt(c));
        }

        /**
	 * This method handles formatting hexadecimals
	 * @param number The double to format
	 */
        private void formatHex(double number) {
            long bits = Double.doubleToRawLongBits(number);
            negative = (bits & 0x8000000000000000L) != 0;
            long exponent = (bits & 0x7ff0000000000000L) >>> 52;
            long significand = bits & 0x000fffffffffffffL;
            exponent -= BIAS;
            boolean denormalised = exponent == E_MIN - 1;
            exponentNegative = exponent < 0;
            if (exponentNegative) exponent = -exponent;
            if (!denormalised) {
                significand |= 0x0010000000000000L;
            } else {
                significand <<= 1;
                if (significand != 0) while ((significand & 0x0008000000000000L) == 0) {
                    ++exponent;
                    significand <<= 1;
                }
            }
            while (exponent % 4 != 0) {
                significand <<= 1;
                if (exponentNegative) ++exponent; else --exponent;
            }
            exponent >>>= 2;
            exponent = (int) exponent;
            for (int i = 0; i < 14; ++i) {
                int digit = (int) (significand & 0xfL);
                digits.add(0, digit);
                significand >>>= 4;
            }
            for (int i = 0; i < 3; ++i) {
                int digit = (int) (exponent & 0xfL);
                exponentDigits.add(0, digit);
                exponent >>>= 4;
            }
        }

        /**
	 * This method handles formatting octals
	 * @param number The double to format
	 */
        private void formatOct(double number) {
            long bits = Double.doubleToRawLongBits(number);
            negative = (bits & 0x8000000000000000L) != 0;
            long exponent = (bits & 0x7ff0000000000000L) >>> 52;
            long significand = bits & 0x000fffffffffffffL;
            exponent -= BIAS;
            boolean denormalised = exponent == E_MIN - 1;
            exponentNegative = exponent < 0;
            if (exponentNegative) exponent = -exponent;
            if (!denormalised) {
                significand |= 0x0010000000000000L;
            } else {
                significand <<= 1;
                if (significand != 0) while ((significand & 0x0008000000000000L) == 0) {
                    ++exponent;
                    significand <<= 1;
                }
            }
            while (exponent % 3 != 0) {
                significand <<= 1;
                if (exponentNegative) ++exponent; else --exponent;
            }
            exponent /= 3;
            exponent = (int) exponent;
            significand <<= 2;
            for (int i = 0; i < 19; ++i) {
                int digit = (int) (significand & 0x7L);
                digits.add(0, digit);
                significand >>>= 3;
            }
            for (int i = 0; i < 4; ++i) {
                int digit = (int) (exponent & 0x7L);
                exponentDigits.add(0, digit);
                exponent >>>= 3;
            }
        }

        /**
	 * This method handles formatting binary numbers
	 * @param number The double to format
	 */
        private void formatBin(double number) {
            long bits = Double.doubleToRawLongBits(number);
            negative = (bits & 0x8000000000000000L) != 0;
            long exponent = (bits & 0x7ff0000000000000L) >>> 52;
            long significand = bits & 0x000fffffffffffffL;
            exponent -= BIAS;
            boolean denormalised = exponent == E_MIN - 1;
            exponentNegative = exponent < 0;
            if (exponentNegative) exponent = -exponent;
            if (!denormalised) {
                significand |= 0x0010000000000000L;
            } else {
                significand <<= 1;
                if (significand != 0) while ((significand & 0x0008000000000000L) == 0) {
                    ++exponent;
                    significand <<= 1;
                }
            }
            exponent = (int) exponent;
            for (int i = 0; i < 53; ++i) {
                int digit = (int) (significand & 0x1L);
                digits.add(0, digit);
                significand >>>= 1;
            }
            for (int i = 0; i < 9; ++i) {
                int digit = (int) (exponent & 0x1L);
                exponentDigits.add(0, digit);
                exponent >>>= 1;
            }
        }

        /**
	 * This is effectively the reverse of getDigit(). We use it when extracting
	 * digits from Double.toString().
	 * @param c A char corresponding to a digit
	 * @return The corresponding int
	 */
        private static int getInt(char c) {
            switch(c) {
                case '0':
                    return 0;
                case '1':
                    return 1;
                case '2':
                    return 2;
                case '3':
                    return 3;
                case '4':
                    return 4;
                case '5':
                    return 5;
                case '6':
                    return 6;
                case '7':
                    return 7;
                case '8':
                    return 8;
                case '9':
                    return 9;
                default:
                    return -1;
            }
        }

        /**
	 * We need to know how to represent integers up to 15 as char. This function
	 * does it.
	 * @param d An integer (should be in range 0&ndash;15)
	 * @return The corresponding char
	 */
        private static char getDigit(int d) {
            switch(d) {
                case 0:
                    return '0';
                case 1:
                    return '1';
                case 2:
                    return '2';
                case 3:
                    return '3';
                case 4:
                    return '4';
                case 5:
                    return '5';
                case 6:
                    return '6';
                case 7:
                    return '7';
                case 8:
                    return '8';
                case 9:
                    return '9';
                case 10:
                    return 'A';
                case 11:
                    return 'B';
                case 12:
                    return 'C';
                case 13:
                    return 'D';
                case 14:
                    return 'E';
                case 15:
                    return 'F';
                default:
                    return '?';
            }
        }

        /**
	 * Is number negative?
	 */
        public boolean negative;

        /**
	 * Representation of significand digits as integers in correct base.
	 */
        public java.util.Vector<Integer> digits;

        /**
	 * Is exponent negative?
	 */
        public boolean exponentNegative;

        /**
	 * Representation of exponent digits as integers in correct base.
	 */
        public java.util.Vector<Integer> exponentDigits;

        /**
	 * Value of the exponent.
	 */
        public int exponent;

        /**
	 * Is number infinite?
	 */
        public boolean infinity;

        /**
	 * Is number not a number?
	 */
        public boolean NaN;

        /**
	 * Is number zero? Useful so we can avoid overflow in some methods.
	 */
        public boolean zero;

        /**
	 * The base as an integer.
	 */
        public int base;

        /**
	 * Standard constant for IEEE 754 doubles
	 */
        public static final long BIAS = 1023;

        /**
	 * Standard constant for IEEE 754 doubles
	 */
        public static final long E_MAX = 1023;

        /**
	 * Standard constant for IEEE 754 doubles
	 */
        public static final long E_MIN = -1022;
    }
}
