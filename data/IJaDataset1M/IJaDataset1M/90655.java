package com.sun.perseus.parser;

/**
 * Parser for SVG Length values
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: LengthParser.java,v 1.2 2006/04/21 06:40:30 st125089 Exp $
 */
public class LengthParser extends NumberParser {

    /**
     * Parses a length value. This method throws an
     * <code>IllegalArgumentException</code> if the input argument's
     * syntax does not conform to that of a length value, as defined
     * by the CSS 2 specification.
     *
     * @param s the value to convert to a <code>Length</code> object.
     * @return <code>Length</code> corresponding to the input argument.
     */
    public Length parseLength(final String s) {
        setString(s);
        int mant = 0;
        int mantDig = 0;
        boolean mantPos = true;
        boolean mantRead = false;
        int exp = 0;
        int expDig = 0;
        int expAdj = 0;
        boolean expPos = true;
        int unitState = 0;
        current = read();
        Length val = new Length();
        switch(current) {
            case '-':
                mantPos = false;
            case '+':
                current = read();
            default:
        }
        m1: switch(current) {
            default:
                throw new IllegalArgumentException();
            case '.':
                break;
            case '0':
                mantRead = true;
                l: for (; ; ) {
                    current = read();
                    switch(current) {
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            break l;
                        default:
                            break m1;
                        case '0':
                    }
                }
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                mantRead = true;
                l: for (; ; ) {
                    if (mantDig < 9) {
                        mantDig++;
                        mant = mant * 10 + (current - '0');
                    } else {
                        expAdj++;
                    }
                    current = read();
                    switch(current) {
                        default:
                            break l;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                    }
                }
        }
        if (current == '.') {
            current = read();
            m2: switch(current) {
                default:
                case 'e':
                case 'E':
                    if (!mantRead) {
                        throw new IllegalArgumentException();
                    }
                    break;
                case '0':
                    if (mantDig == 0) {
                        l: for (; ; ) {
                            current = read();
                            expAdj--;
                            switch(current) {
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    break l;
                                default:
                                    break m2;
                                case '0':
                            }
                        }
                    }
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    l: for (; ; ) {
                        if (mantDig < 9) {
                            mantDig++;
                            mant = mant * 10 + (current - '0');
                            expAdj--;
                        }
                        current = read();
                        switch(current) {
                            default:
                                break l;
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                        }
                    }
            }
        }
        boolean le = false;
        es: switch(current) {
            case 'e':
                le = true;
            case 'E':
                current = read();
                switch(current) {
                    default:
                        throw new IllegalArgumentException();
                    case '-':
                        expPos = false;
                    case '+':
                        current = read();
                        switch(current) {
                            default:
                                throw new IllegalArgumentException();
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                        }
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                }
                en: switch(current) {
                    case '0':
                        l: for (; ; ) {
                            current = read();
                            switch(current) {
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    break l;
                                default:
                                    break en;
                                case '0':
                            }
                        }
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        l: for (; ; ) {
                            if (expDig < 3) {
                                expDig++;
                                exp = exp * 10 + (current - '0');
                            }
                            current = read();
                            switch(current) {
                                default:
                                    break l;
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                            }
                        }
                }
            default:
        }
        if (!expPos) {
            exp = -exp;
        }
        exp += expAdj;
        if (!mantPos) {
            mant = -mant;
        }
        val.value = NumberParser.buildFloat(mant, exp);
        switch(current) {
            case 'p':
                current = read();
                switch(current) {
                    case 'c':
                        val.unit = val.SVG_LENGTHTYPE_PC;
                        current = read();
                        break;
                    case 't':
                        val.unit = val.SVG_LENGTHTYPE_PT;
                        current = read();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                break;
            case 'i':
                current = read();
                if (current != 'n') {
                    throw new IllegalArgumentException();
                }
                val.unit = val.SVG_LENGTHTYPE_IN;
                current = read();
                break;
            case 'c':
                current = read();
                if (current != 'm') {
                    throw new IllegalArgumentException();
                }
                val.unit = val.SVG_LENGTHTYPE_CM;
                current = read();
                break;
            case 'm':
                current = read();
                if (current != 'm') {
                    throw new IllegalArgumentException();
                }
                val.unit = val.SVG_LENGTHTYPE_MM;
                current = read();
                break;
            case '%':
                val.unit = val.SVG_LENGTHTYPE_PERCENTAGE;
                current = read();
                break;
            case -1:
                val.unit = val.SVG_LENGTHTYPE_NUMBER;
                current = read();
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (current != -1) {
            throw new IllegalArgumentException();
        }
        return val;
    }
}
