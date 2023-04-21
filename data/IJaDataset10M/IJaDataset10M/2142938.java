package peersim.config;

import java.math.*;
import org.lsmp.djep.groupJep.groups.*;
import org.lsmp.djep.groupJep.interfaces.*;

/**
 * This class implements the <code>Group</code> interface of JEP,
 * enabling the configuration system to read integers with arbitrary 
 * length.
 *
 * 这个类实现了JEP中的Group接口，可以让配置系统能读取任意长度的整数
 */
public class Operators extends Group implements IntegralDomainI, HasDivI, OrderedSetI, HasModI, HasPowerI {

    /**
     * Operations on the reals (Implemented as BigInteger).
     */
    public Operators() {
    }

    public Number getZERO() {
        return BigInteger.ZERO;
    }

    public Number getONE() {
        return BigInteger.ONE;
    }

    public Number getInverse(Number num) {
        if (num instanceof BigInteger) {
            BigInteger a = (BigInteger) num;
            return a.negate();
        } else {
            return -num.doubleValue();
        }
    }

    public Number add(Number num1, Number num2) {
        if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() + num2.doubleValue();
        } else {
            BigInteger a = (BigInteger) num1;
            BigInteger b = (BigInteger) num2;
            return a.add(b);
        }
    }

    public Number sub(Number num1, Number num2) {
        if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() - num2.doubleValue();
        } else {
            BigInteger a = (BigInteger) num1;
            BigInteger b = (BigInteger) num2;
            return a.subtract(b);
        }
    }

    public Number mul(Number num1, Number num2) {
        if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() * num2.doubleValue();
        } else {
            BigInteger a = (BigInteger) num1;
            BigInteger b = (BigInteger) num2;
            return a.multiply(b);
        }
    }

    public Number div(Number num1, Number num2) {
        if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() / num2.doubleValue();
        } else {
            BigInteger a = (BigInteger) num1;
            BigInteger b = (BigInteger) num2;
            return a.divide(b);
        }
    }

    public Number mod(Number num1, Number num2) {
        if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() % num2.doubleValue();
        } else {
            BigInteger a = (BigInteger) num1;
            BigInteger b = (BigInteger) num2;
            return a.remainder(b);
        }
    }

    public Number pow(Number num1, Number num2) {
        if (num1 instanceof Double || num2 instanceof Double) {
            return Math.pow(num1.doubleValue(), num2.doubleValue());
        } else {
            BigInteger a = (BigInteger) num1;
            BigInteger b = (BigInteger) num2;
            return a.pow(b.intValue());
        }
    }

    public boolean equals(Number num1, Number num2) {
        if (num1 instanceof Double || num2 instanceof Double) {
            return num1.doubleValue() == num2.doubleValue();
        } else {
            BigInteger a = (BigInteger) num1;
            BigInteger b = (BigInteger) num2;
            return a.equals(b);
        }
    }

    public int compare(Number num1, Number num2) {
        if (num1 instanceof Double || num2 instanceof Double) {
            double n1 = num1.doubleValue();
            double n2 = num2.doubleValue();
            return (n1 < n2 ? -1 : (n1 == n2 ? 0 : 1));
        } else {
            BigInteger a = (BigInteger) num1;
            BigInteger b = (BigInteger) num2;
            return a.compareTo(b);
        }
    }

    public Number valueOf(String str) {
        try {
            return new BigInteger(str);
        } catch (NumberFormatException e) {
            return new Double(str);
        }
    }

    public String toString() {
        return "";
    }
}
