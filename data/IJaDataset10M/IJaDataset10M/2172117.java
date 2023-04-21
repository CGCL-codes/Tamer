package progcalc;

/**
 * Esta clase mantiene el estado de la calculadora
 * @author Esteban Rodríguez Betancourt <erbldev at gmail dot com>
 */
public class calculator {

    private Number valorActual;

    private boolean varFloating;

    private int numBits;

    public calculator() {
        valorActual = new Integer(0);
        setFloating(false);
        setBitsNumber(32);
    }

    public int getBitsNumber() {
        return numBits;
    }

    public void setBitsNumber(int numBits) {
        switch(numBits) {
            case 8:
                setValue(getValue().byteValue());
                this.numBits = 8;
                break;
            case 16:
                setValue(getValue().shortValue());
                this.numBits = 16;
                break;
            case 32:
                if (isFloating()) {
                    setValue(getValue().floatValue());
                } else {
                    setValue(getValue().intValue());
                }
                this.numBits = 32;
                break;
            case 64:
            default:
                if (isFloating()) {
                    setValue(getValue().doubleValue());
                } else {
                    setValue(getValue().longValue());
                }
                this.numBits = 64;
                break;
        }
    }

    public Number getValue() {
        return valorActual;
    }

    public void setValue(Number valorActual) {
        this.valorActual = valorActual;
    }

    public boolean isFloating() {
        return varFloating;
    }

    public void setFloating(boolean varFloating) {
        if (this.varFloating != varFloating) {
            if (varFloating) {
                Number temp = getValue();
                Number newval = new Double(temp.doubleValue());
                setBitsNumber(64);
                setValue(newval);
            } else {
                Number temp = getValue();
                Number newval = new Double(temp.longValue());
                setBitsNumber(64);
                setValue(newval);
            }
            setFloating(varFloating);
        }
    }

    public long getLongRepresentation() {
        if (isFloating()) {
            Double temp = getValue().doubleValue();
            long valret = Double.doubleToRawLongBits(temp);
            return valret;
        } else {
            return getValue().longValue();
        }
    }

    public int getShortRepresentation() {
        if (isFloating()) {
            Float temp = getValue().floatValue();
            return Float.floatToRawIntBits(temp);
        } else {
            return getValue().intValue();
        }
    }

    public String toBinary() {
        StringBuilder salida = new StringBuilder();
        long valLong;
        if (isFloating()) {
            valLong = Double.doubleToRawLongBits(getValue().doubleValue());
        } else {
            valLong = getValue().longValue();
        }
        return Long.toBinaryString(valLong);
    }

    public String toHexadecimal() {
        long valLong;
        if (isFloating()) {
            valLong = Double.doubleToRawLongBits(getValue().doubleValue());
        } else {
            valLong = getValue().longValue();
        }
        return Long.toHexString(valLong);
    }

    public void cmdAdd(calculator numA) {
        numA.setBitsNumber(getBitsNumber());
        numA.setFloating(isFloating());
        if (isFloating()) {
            switch(getBitsNumber()) {
                case 32:
                    valorActual = valorActual.floatValue() + numA.getValue().floatValue();
                    break;
                default:
                    valorActual = valorActual.doubleValue() + numA.getValue().doubleValue();
                    break;
            }
        } else {
            switch(getBitsNumber()) {
                case 8:
                    valorActual = valorActual.byteValue() + numA.getValue().byteValue();
                    break;
                case 16:
                    valorActual = valorActual.shortValue() + numA.getValue().shortValue();
                    break;
                case 32:
                    valorActual = valorActual.intValue() + numA.getValue().intValue();
                    break;
                default:
                    valorActual = valorActual.longValue() + numA.getValue().longValue();
                    break;
            }
        }
    }

    public void cmdSub(calculator numA) {
        numA.setBitsNumber(getBitsNumber());
        numA.setFloating(isFloating());
        if (isFloating()) {
            switch(getBitsNumber()) {
                case 32:
                    valorActual = numA.getValue().floatValue() - valorActual.floatValue();
                    break;
                default:
                    valorActual = numA.getValue().doubleValue() - valorActual.doubleValue();
                    break;
            }
        } else {
            switch(getBitsNumber()) {
                case 8:
                    valorActual = numA.getValue().byteValue() - valorActual.byteValue();
                    break;
                case 16:
                    valorActual = numA.getValue().shortValue() - valorActual.shortValue();
                    break;
                case 32:
                    valorActual = numA.getValue().intValue() - valorActual.intValue();
                    break;
                default:
                    valorActual = numA.getValue().longValue() - valorActual.longValue();
                    break;
            }
        }
    }

    public void cmdMult(calculator numA) {
        numA.setBitsNumber(getBitsNumber());
        numA.setFloating(isFloating());
        if (isFloating()) {
            switch(getBitsNumber()) {
                case 32:
                    valorActual = valorActual.floatValue() * numA.getValue().floatValue();
                    break;
                default:
                    valorActual = valorActual.doubleValue() * numA.getValue().doubleValue();
                    break;
            }
        } else {
            switch(getBitsNumber()) {
                case 8:
                    valorActual = valorActual.byteValue() * numA.getValue().byteValue();
                    break;
                case 16:
                    valorActual = valorActual.shortValue() * numA.getValue().shortValue();
                    break;
                case 32:
                    valorActual = valorActual.intValue() * numA.getValue().intValue();
                    break;
                default:
                    valorActual = valorActual.longValue() * numA.getValue().longValue();
                    break;
            }
        }
    }

    public void cmdDiv(calculator numA) {
        numA.setBitsNumber(getBitsNumber());
        numA.setFloating(isFloating());
        if (isFloating()) {
            switch(getBitsNumber()) {
                case 32:
                    valorActual = numA.getValue().floatValue() / valorActual.floatValue();
                    break;
                default:
                    valorActual = numA.getValue().doubleValue() / valorActual.doubleValue();
                    break;
            }
        } else {
            switch(getBitsNumber()) {
                case 8:
                    valorActual = numA.getValue().byteValue() / valorActual.byteValue();
                    break;
                case 16:
                    valorActual = numA.getValue().shortValue() / valorActual.shortValue();
                    break;
                case 32:
                    valorActual = numA.getValue().intValue() / valorActual.intValue();
                    break;
                default:
                    valorActual = numA.getValue().longValue() / valorActual.longValue();
                    break;
            }
        }
    }

    public void cmdMod(calculator numA) {
        numA.setBitsNumber(getBitsNumber());
        numA.setFloating(isFloating());
        if (isFloating()) {
            switch(getBitsNumber()) {
                case 32:
                    valorActual = numA.getValue().floatValue() % valorActual.floatValue();
                    break;
                default:
                    valorActual = numA.getValue().doubleValue() % valorActual.doubleValue();
                    break;
            }
        } else {
            switch(getBitsNumber()) {
                case 8:
                    valorActual = numA.getValue().byteValue() % valorActual.byteValue();
                    break;
                case 16:
                    valorActual = numA.getValue().shortValue() % valorActual.shortValue();
                    break;
                case 32:
                    valorActual = numA.getValue().intValue() % valorActual.intValue();
                    break;
                default:
                    valorActual = numA.getValue().longValue() % valorActual.longValue();
                    break;
            }
        }
    }

    public void cmdAND(calculator numA) {
        numA.setBitsNumber(getBitsNumber());
        numA.setFloating(isFloating());
        if (isFloating()) {
            switch(getBitsNumber()) {
                case 32:
                    valorActual = Float.intBitsToFloat(Float.floatToRawIntBits(numA.getValue().floatValue()) & Float.floatToRawIntBits(valorActual.floatValue()));
                    break;
                default:
                    valorActual = Double.longBitsToDouble(Double.doubleToRawLongBits(numA.getValue().doubleValue()) & Double.doubleToRawLongBits(valorActual.doubleValue()));
                    break;
            }
        } else {
            switch(getBitsNumber()) {
                case 8:
                    valorActual = numA.getValue().byteValue() & valorActual.byteValue();
                    break;
                case 16:
                    valorActual = numA.getValue().shortValue() & valorActual.shortValue();
                    break;
                case 32:
                    valorActual = numA.getValue().intValue() & valorActual.intValue();
                    break;
                default:
                    valorActual = numA.getValue().longValue() & valorActual.longValue();
                    break;
            }
        }
    }

    public void cmdAND(calculator numA) {
        numA.setBitsNumber(getBitsNumber());
        numA.setFloating(isFloating());
        if (isFloating()) {
            switch(getBitsNumber()) {
                case 32:
                    valorActual = Float.intBitsToFloat(Float.floatToRawIntBits(numA.getValue().floatValue()) | Float.floatToRawIntBits(valorActual.floatValue()));
                    break;
                default:
                    valorActual = Double.longBitsToDouble(Double.doubleToRawLongBits(numA.getValue().doubleValue()) | Double.doubleToRawLongBits(valorActual.doubleValue()));
                    break;
            }
        } else {
            switch(getBitsNumber()) {
                case 8:
                    valorActual = numA.getValue().byteValue() | valorActual.byteValue();
                    break;
                case 16:
                    valorActual = numA.getValue().shortValue() | valorActual.shortValue();
                    break;
                case 32:
                    valorActual = numA.getValue().intValue() | valorActual.intValue();
                    break;
                default:
                    valorActual = numA.getValue().longValue() | valorActual.longValue();
                    break;
            }
        }
    }
}
