package visual3d.expression.parser;

public class Operator {

    private Zahlenwert lValue;

    private Zahlenwert rValue;

    private Zahlenwert next;

    private Operator prevOp;

    private Operator nextOp;

    private char value;

    private int praezedenz;

    private Double leer[];

    private double xWerte[];

    public Operator() {
        leer = new Double[1];
        lValue = rValue = next = null;
        prevOp = nextOp = null;
        leer[0] = null;
        xWerte = null;
    }

    public void setLval(Zahlenwert l) {
        lValue = l;
    }

    public int getPrec() {
        return praezedenz;
    }

    private void calcOp(double a, double b) {
        switch(value) {
            case 45:
                a -= b;
                break;
            case 43:
                a += b;
                break;
            case 47:
                a /= b;
                break;
            case 42:
                a *= b;
                break;
            case 37:
                a %= b;
                break;
            case 94:
                a = Math.pow(a, b);
                break;
        }
        lValue.setValue(new Double(a));
    }

    private void calcOp(Double a[], Double b[]) {
        boolean aBelegt = a[0] != null;
        boolean bBelegt = b[0] != null;
        if (!aBelegt) a = new Double[xWerte.length];
        switch(value) {
            default:
                break;
            case 45:
                if (!aBelegt && !bBelegt) {
                    for (int i = 0; i < a.length; i++) lValue.setValue(new Double(0.0D));
                    return;
                }
                if (aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() - b[i].doubleValue());
                } else if (aBelegt && !bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() - xWerte[i]);
                } else {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] - b[i].doubleValue());
                }
                break;
            case 43:
                if (aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() + b[i].doubleValue());
                    break;
                }
                if (aBelegt && !bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() + xWerte[i]);
                    break;
                }
                if (!aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] + b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] + xWerte[i]);
                break;
            case 47:
                if (aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() / b[i].doubleValue());
                    break;
                }
                if (aBelegt && !bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() / xWerte[i]);
                    break;
                }
                if (!aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] / b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] != 0.0D ? 1.0D : a[i].doubleValue() / xWerte[i]);
                break;
            case 42:
                if (aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() * b[i].doubleValue());
                    break;
                }
                if (aBelegt && !bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() * xWerte[i]);
                    break;
                }
                if (!aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] * b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] * xWerte[i]);
                break;
            case 37:
                if (aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() % b[i].doubleValue());
                    break;
                }
                if (aBelegt && !bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() % xWerte[i]);
                    break;
                }
                if (!aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] % b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] != 0.0D ? 0.0D : a[i].doubleValue() % xWerte[i]);
                break;
            case 94:
                if (aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(Math.pow(a[i].doubleValue(), b[i].doubleValue()));
                    break;
                }
                if (aBelegt && !bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(Math.pow(a[i].doubleValue(), xWerte[i]));
                    break;
                }
                if (!aBelegt && bBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(Math.pow(xWerte[i], b[i].doubleValue()));
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(Math.pow(xWerte[i], xWerte[i]));
                break;
        }
        lValue.setValue(a);
    }

    private void calcOp(double a, Double b[]) {
        if (Double.isInfinite(a) || Double.isNaN(a)) {
            lValue.setValue(new Double(a));
            return;
        }
        boolean bBelegt = b[0] != null;
        if (!bBelegt) b = new Double[xWerte.length];
        switch(value) {
            default:
                break;
            case 45:
                if (bBelegt) {
                    for (int i = 0; i < b.length; i++) b[i] = new Double(a - b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < b.length; i++) b[i] = new Double(a - xWerte[i]);
                break;
            case 43:
                if (a == (double) 0) {
                    lValue.setValue(bBelegt ? ((Object) (b)) : ((Object) (leer)));
                    return;
                }
                if (bBelegt) {
                    for (int i = 0; i < b.length; i++) b[i] = new Double(a + b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < b.length; i++) b[i] = new Double(a + xWerte[i]);
                break;
            case 47:
                if (bBelegt) {
                    for (int i = 0; i < b.length; i++) b[i] = new Double(a / b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < b.length; i++) b[i] = new Double(a / xWerte[i]);
                break;
            case 42:
                if (a == (double) 0) {
                    for (int i = 0; i < b.length; i++) b[i] = new Double(!Double.isNaN(b[i].doubleValue()) && !Double.isInfinite(b[i].doubleValue()) ? 0.0D : b[i].doubleValue());
                } else if (a == (double) 1) {
                    lValue.setValue(bBelegt ? ((Object) (b)) : ((Object) (leer)));
                    return;
                }
                if (bBelegt) {
                    for (int i = 0; i < b.length; i++) b[i] = new Double(a * b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < b.length; i++) b[i] = new Double(a * xWerte[i]);
                break;
            case 37:
                if (bBelegt) {
                    for (int i = 0; i < b.length; i++) b[i] = new Double(a % b[i].doubleValue());
                    break;
                }
                for (int i = 0; i < b.length; i++) b[i] = new Double(a % xWerte[i]);
                break;
            case 94:
                if (a == (double) 0) {
                    for (int i = 0; i < b.length; i++) b[i] = new Double(!Double.isNaN(b[i].doubleValue()) && !Double.isInfinite(b[i].doubleValue()) ? 0.0D : b[i].doubleValue());
                    break;
                }
                if (bBelegt) {
                    for (int i = 0; i < b.length; i++) b[i] = new Double(Math.pow(a, b[i].doubleValue()));
                    break;
                }
                for (int i = 0; i < b.length; i++) b[i] = new Double(Math.pow(a, xWerte[i]));
                break;
        }
        lValue.setValue(b);
    }

    private void calcOp(Double a[], double b) {
        if (Double.isInfinite(b) || Double.isNaN(b)) {
            lValue.setValue(new Double(b));
            return;
        }
        boolean aBelegt = a[0] != null;
        if (!aBelegt) a = new Double[xWerte.length];
        switch(value) {
            default:
                break;
            case 45:
                if (b == (double) 0) {
                    lValue.setValue(aBelegt ? ((Object) (a)) : ((Object) (leer)));
                    return;
                }
                if (aBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() - b);
                } else {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] - b);
                }
                break;
            case 43:
                if (b == (double) 0) {
                    lValue.setValue(aBelegt ? ((Object) (a)) : ((Object) (leer)));
                    return;
                }
                if (aBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() + b);
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] + b);
                break;
            case 47:
                if (aBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() / b);
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] / b);
                break;
            case 42:
                if (b == (double) 0) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(!Double.isNaN(a[i].doubleValue()) && !Double.isInfinite(a[i].doubleValue()) ? 0.0D : a[i].doubleValue());
                } else if (b == (double) 1) {
                    lValue.setValue(aBelegt ? ((Object) (a)) : ((Object) (leer)));
                    return;
                }
                if (aBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() * b);
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] * b);
                break;
            case 37:
                if (aBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(a[i].doubleValue() % b);
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(xWerte[i] % b);
                break;
            case 94:
                if (b == (double) 0) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(!Double.isNaN(a[i].doubleValue()) && !Double.isInfinite(a[i].doubleValue()) ? 1.0D : a[i].doubleValue());
                } else if (b == (double) 1) {
                    lValue.setValue(aBelegt ? ((Object) (a)) : ((Object) (leer)));
                    return;
                }
                if (aBelegt) {
                    for (int i = 0; i < a.length; i++) a[i] = new Double(Math.pow(a[i].doubleValue(), b));
                    break;
                }
                for (int i = 0; i < a.length; i++) a[i] = new Double(Math.pow(xWerte[i], b));
                break;
        }
        lValue.setValue(a);
    }

    public void calc() {
        for (; nextOp != null && nextOp.getPrec() < praezedenz; nextOp.calc()) ;
        if ((lValue.getValue() instanceof Double) && (rValue.getValue() instanceof Double)) calcOp(((Double) lValue.getValue()).doubleValue(), ((Double) rValue.getValue()).doubleValue()); else if ((lValue.getValue() instanceof Double[]) && (rValue.getValue() instanceof Double[])) calcOp((Double[]) lValue.getValue(), (Double[]) rValue.getValue()); else if ((lValue.getValue() instanceof Double) && (rValue.getValue() instanceof Double[])) calcOp(((Double) lValue.getValue()).doubleValue(), (Double[]) rValue.getValue()); else if ((lValue.getValue() instanceof Double[]) && (rValue.getValue() instanceof Double)) calcOp((Double[]) lValue.getValue(), ((Double) rValue.getValue()).doubleValue());
        lValue.setNextOp(nextOp);
        if (prevOp != null) prevOp.setNextOp(nextOp);
        if (nextOp != null) {
            nextOp.setPrevOp(prevOp);
            nextOp.setLval(lValue);
        }
    }

    public void setNextOp(Operator no) {
        nextOp = no;
    }

    public void setPrevOp(Operator po) {
        prevOp = po;
    }

    public void init(Operator lOp, Zahlenwert lVal, double x[]) {
        lValue = lVal;
        rValue = next;
        prevOp = lOp;
        nextOp = next.getNext();
        xWerte = x;
        next.init(this, next, x);
    }

    public String parse(String str) {
        char c = str.charAt(0);
        if (c == '-' || c == '+' || c == '*' || c == '/' || c == '^' || c == '%') {
            value = c;
            str = str.substring(1);
        } else {
            return "Unbekannter Operator:".concat(String.valueOf(str));
        }
        if (str.length() == 0) return "Bin\344rer Operator: rechter Wert fehlt!";
        switch(c) {
            case 43:
            case 45:
                praezedenz = 50;
                break;
            case 94:
                praezedenz = 30;
                break;
            default:
                praezedenz = 40;
                break;
        }
        next = new Zahlenwert();
        return next.parse(str);
    }
}
