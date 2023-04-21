package com.res.java.util;

import com.res.java.lib.CobolSymbol;
import com.res.java.translation.symbol.SymbolProperties;
import com.res.java.translation.symbol.SymbolUtil;

public class FieldAttributes {

    public static void processDecimal(String pic, CobolSymbol sym) {
        if (pic.charAt(0) == 'S') {
            sym.isSigned = true;
            pic = pic.substring(1);
        } else sym.isSigned = false;
        pic = pic.replace('V', '.').replace('*', '9').replace('Z', '9');
        int j = pic.indexOf('+');
        if (j >= 0) {
            pic = pic.substring(0, j + 1) + pic.substring(j + 1).replace('+', '9').replace('-', '9');
        }
        j = pic.indexOf('-');
        if (j >= 0) {
            pic = pic.substring(0, j + 1) + pic.substring(j + 1).replace('-', '9');
        }
        j = pic.indexOf('$');
        if (j >= 0) {
            pic = pic.substring(0, j + 1) + pic.substring(j + 1).replace('$', '9');
        }
        int i;
        String s;
        i = pic.indexOf('.');
        if (i < 0) {
            i = pic.indexOf('9');
            if (i >= 0) {
                sym.type = CobolSymbol.INTEGER;
                processInteger(pic, sym);
            } else {
                sym.type = CobolSymbol.STRING;
                processAlpha(pic, sym);
            }
            if (pic.indexOf('P') >= 0) {
                processPScaling(pic, sym);
                return;
            }
            return;
        }
        sym.type = CobolSymbol.BIGDECIMAL;
        s = pic.substring(0, i);
        processPartOfDecimal(s, sym, false);
        s = pic.substring(i + 1);
        processPartOfDecimal(s, sym, true);
        return;
    }

    private static void processPScaling(String pic, CobolSymbol sym) {
        int i = 0, l = 0;
        while (i < pic.length()) {
            if (pic.charAt(i) == 'P') {
                if (i + 1 < pic.length() && pic.charAt(i + 1) == '(') {
                    i = i + 2;
                    int k = i + 1;
                    while (pic.charAt(k) != ')' && k < pic.length()) {
                        if (pic.charAt(k) >= '0' && pic.charAt(k) <= '9') ; else {
                            SymbolUtil.getInstance().reportError("Data Name:" + sym.name + " has invalid picture string=>" + pic);
                            System.exit(-1);
                        }
                        k++;
                    }
                    if (k >= pic.length()) {
                        SymbolUtil.getInstance().reportError("Data Name:" + sym.name + " has invalid picture string=>" + pic);
                        System.exit(-1);
                    }
                    l += new Integer(pic.substring(i, k)).intValue();
                    i = ++k;
                } else {
                    ++i;
                    l++;
                }
            } else i++;
        }
        if (l <= 0) return;
        if (pic.charAt(i - 1) == 'P') {
            sym.maxIntLength += l;
        } else {
            sym.type = CobolSymbol.BIGDECIMAL;
            sym.maxFractionLength = (short) (sym.maxIntLength + l);
            sym.maxIntLength = 0;
        }
        return;
    }

    public static void processInteger(String pic, CobolSymbol sym) {
        byte[] intPart = pic.getBytes();
        int j = 0;
        short l = 0;
        while (j < intPart.length) {
            if (intPart[j] == '9' || intPart[j] == 'Z' || intPart[j] == '*') {
                if (j + 1 < intPart.length && intPart[j + 1] == '(') {
                    int k = j + 2;
                    while (intPart[k] != ')' && k < intPart.length) {
                        if (intPart[k] >= '0' && intPart[k] <= '9') ; else {
                            SymbolUtil.getInstance().reportError("Data Name:" + sym.name + " has invalid picture string=>" + pic);
                            System.exit(-1);
                        }
                        k++;
                    }
                    if (k >= intPart.length) {
                        SymbolUtil.getInstance().reportError("Data Name:" + sym.name + " has invalid picture string=>" + pic);
                        System.exit(-1);
                    }
                    l += new Integer(pic.substring(j + 2, k)).intValue();
                    j = ++k;
                } else {
                    ++j;
                    l++;
                }
            } else if (intPart[j] == 'S') {
                sym.isSigned = true;
                l++;
                j++;
            } else if (intPart[j] == '$') {
                sym.isCurrency = true;
                l++;
                j++;
            } else {
                j++;
            }
        }
        sym.type = CobolSymbol.INTEGER;
        sym.maxIntLength = l;
    }

    public static void processPartOfDecimal(String pic, CobolSymbol sym, boolean fraction) {
        byte[] intPart = pic.getBytes();
        int j = 0;
        short l = 0;
        while (j < intPart.length) {
            if (intPart[j] == '9' || intPart[j] == 'Z' || intPart[j] == '*') {
                if (j + 1 < intPart.length && intPart[j + 1] == '(') {
                    int k = j + 2;
                    while (intPart[k] != ')' && k < intPart.length) {
                        if (intPart[k] >= '0' && intPart[k] <= '9') ; else {
                            SymbolUtil.getInstance().reportError("Data Name:" + sym.name + " has invalid picture string=>" + pic);
                            System.exit(-1);
                        }
                        k++;
                    }
                    if (k >= intPart.length) {
                        SymbolUtil.getInstance().reportError("Data Name:" + sym.name + " has invalid picture string=>" + pic);
                        System.exit(-1);
                    }
                    l += new Integer(pic.substring(j + 2, k)).intValue();
                    j = ++k;
                } else {
                    ++j;
                    l++;
                }
            } else if (intPart[j] == 'S') {
                sym.isSigned = true;
                l++;
                j++;
            } else if (intPart[j] == '$') {
                sym.isCurrency = true;
                l++;
                j++;
            } else {
                j++;
            }
        }
        if (sym.type == CobolSymbol.BIGDECIMAL) if (fraction) {
            sym.maxFractionLength = l;
        } else {
            sym.maxIntLength = l;
        } else ;
    }

    public static void processAlpha(String pic, CobolSymbol sym) {
        byte[] partsOfPic = pic.getBytes();
        int j = 0;
        int len = 0;
        while (j < partsOfPic.length) {
            if (partsOfPic[j] == '(') {
                int k = j + 1;
                while (partsOfPic[k] != ')' && k < partsOfPic.length) {
                    if (partsOfPic[k] >= '0' && partsOfPic[k] <= '9') ; else {
                        SymbolUtil.getInstance().reportError("Data Name:" + sym.name + " has invalid picture string=>" + pic);
                        System.exit(-1);
                    }
                    ++k;
                }
                if (k >= partsOfPic.length) {
                    SymbolUtil.getInstance().reportError("Data Name:" + sym.name + " has invalid picture string=>" + pic);
                    System.exit(-1);
                }
                len += new Integer(pic.substring(j + 1, k)).intValue() - 1;
                j = ++k;
            } else if (partsOfPic[j] == '$') {
                sym.isCurrency = true;
                len++;
                j++;
            } else {
                len++;
                j++;
            }
        }
        sym.type = CobolSymbol.STRING;
        sym.maxStringLength = len;
    }

    public static int calculateLength(SymbolProperties props) {
        int len = 0;
        boolean isNumber = false;
        CobolSymbol sym = props.getJavaType();
        switch(sym.type) {
            case CobolSymbol.FLOAT:
            case CobolSymbol.DOUBLE:
            case CobolSymbol.BIGDECIMAL:
                len = sym.maxIntLength + sym.maxFractionLength;
                isNumber = true;
                break;
            case CobolSymbol.LONG:
            case CobolSymbol.INTEGER:
            case CobolSymbol.SHORT:
                len = sym.maxIntLength;
                isNumber = true;
                break;
            case CobolSymbol.STRING:
                len = sym.maxStringLength;
                break;
        }
        if (isNumber) {
            switch(sym.usage) {
                case CobolSymbol.BINARY:
                    props.setAdjustedLength(len);
                    if (len >= 1 && len <= 4) {
                        len = 2;
                    } else if (len >= 5 && len <= 9) {
                        len = 4;
                    } else if (len >= 10) {
                        len = 8;
                        if (sym.type == CobolSymbol.INTEGER) sym.type = CobolSymbol.LONG;
                    } else len = 0;
                    break;
                case CobolSymbol.PACKED_DECIMAL:
                    props.setAdjustedLength(len);
                    int len2 = len;
                    len = len2 / 2;
                    if (len2 % 2 > 0) len++;
                    if (len >= 1 && len <= 4) {
                    } else if (len >= 5 && len <= 10) {
                        if (sym.type == CobolSymbol.INTEGER) sym.type = CobolSymbol.LONG;
                    } else len = 0;
                    break;
                case CobolSymbol.DISPLAY:
                    props.setAdjustedLength(len);
                    if (sym.isSigned) len++;
                    if (len >= 1 && len <= 4) {
                    } else if (len >= 5 && len <= 9) {
                    } else if (len >= 10) {
                        if (sym.type == CobolSymbol.INTEGER) sym.type = CobolSymbol.LONG;
                    } else len = 0;
                    break;
                default:
                    props.setAdjustedLength(len);
            }
        } else if (sym.maxStringLength == 1) sym.type = CobolSymbol.CHAR;
        return len;
    }
}
