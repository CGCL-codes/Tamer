package org.eoti.math.ternary;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CopyOnWriteArrayList;

public class Ternary extends Number implements Comparable<Ternary> {

    protected BigInteger biThree = BigInteger.valueOf(3);

    protected BigDecimal bdThree = new BigDecimal(biThree);

    protected CopyOnWriteArrayList<Trit> trits;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.format("java org.eoti.lang.Ternary number [[number]...]\n");
            System.exit(0);
        }
        for (String arg : args) {
            BigInteger toConvert = new BigInteger(arg);
            Ternary ternary = new Ternary(toConvert);
            System.out.format("\nDecimal:\t%s\nTernary:\t%s\n", toConvert, ternary);
        }
    }

    public Ternary(int toConvert) {
        this(BigInteger.valueOf(toConvert));
    }

    public Ternary(BigInteger toConvert) {
        this();
        int position = 0;
        BigInteger remaining = toConvert;
        BigInteger rounded, left;
        while (!remaining.equals(BigInteger.ZERO)) {
            rounded = ((new BigDecimal(remaining)).divide(bdThree, 0, BigDecimal.ROUND_HALF_UP)).toBigInteger();
            left = remaining.subtract(rounded.multiply(biThree));
            if (left.equals(BigInteger.ONE)) setTrit(position++, Trit.POSITIVE); else if (left.equals(BigInteger.ZERO)) setTrit(position++, Trit.NEUTRAL); else setTrit(position++, Trit.NEGATIVE);
            remaining = rounded;
        }
    }

    public Ternary() {
        super();
        trits = new CopyOnWriteArrayList<Trit>();
    }

    public Ternary(Ternary toClone) {
        this();
        trits.addAll(toClone.trits);
    }

    public Ternary abs() {
        if (signum() >= 0) return new Ternary(this);
        return invert(this);
    }

    public int tritLength() {
        for (int position = trits.size() - 1; position >= 0; position--) {
            if (!trits.get(position).equals(Trit.NEUTRAL)) return position + 1;
        }
        return 0;
    }

    public void clearTrit(int position) {
        setTrit(position, Trit.NEUTRAL);
    }

    public void setTrit(int position, Trit trit) {
        if (trits.size() <= position) ensureCapacity(position + 1);
        trits.set(position, trit);
    }

    public Trit getTrit(int position) {
        if (position < 0) return Trit.NEUTRAL;
        if (trits.size() <= position) ensureCapacity(position + 1);
        return trits.get(position);
    }

    public int signum() {
        return getTrit(tritLength() - 1).toInt();
    }

    public void ensureCapacity(int nTerts) {
        while (trits.size() < nTerts) trits.add(Trit.NEUTRAL);
    }

    public void trim() {
        while ((trits.size() > 0) && (trits.get(trits.size() - 1).isNeutral())) trits.remove(trits.size() - 1);
    }

    public void increment() {
        increment(0);
    }

    public void increment(int position) {
        Trit t = getTrit(position).rotateUp();
        setTrit(position, t);
        if (t.isNegative()) increment(position + 1);
    }

    public void decrement() {
        decrement(0);
    }

    public void decrement(int position) {
        Trit t = getTrit(position).rotateDown();
        setTrit(position, t);
        if (t.isPositive()) decrement(position + 1);
    }

    public String toString() {
        if (trits.size() == 0) return Trit.NEUTRAL.toString();
        StringBuilder sb = new StringBuilder();
        for (Trit trit : trits) sb.append(trit);
        return sb.reverse().toString();
    }

    public int compareTo(Ternary t) {
        int ourSig = signum();
        int theirSig = t.signum();
        if (ourSig > theirSig) return 1;
        if (theirSig > ourSig) return -1;
        int ourLength = tritLength();
        int theirLength = t.tritLength();
        if (ourLength > theirLength) return (ourSig > 0 ? 1 : -1);
        if (theirLength > ourLength) return (ourSig > 0 ? -1 : 1);
        for (int position = ourLength - 1; position >= 0; position--) {
            int comparison = getTrit(position).compareTo(t.getTrit(position));
            if (comparison != 0) return comparison;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        return toString().equals(obj.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public BigInteger toBigInteger() {
        BigInteger toRet = BigInteger.ZERO;
        BigInteger curr;
        for (int pos = 0; pos < trits.size(); pos++) {
            curr = (biThree.pow(pos)).multiply(BigInteger.valueOf(getTrit(pos).toInt()));
            toRet = toRet.add(curr);
        }
        return toRet;
    }

    public void shiftLeft() {
        shiftLeft(1);
    }

    public void shiftLeft(int num) {
        CopyOnWriteArrayList<Trit> newTrits = new CopyOnWriteArrayList<Trit>();
        for (int i = 0; i < num; i++) newTrits.add(Trit.NEUTRAL);
        newTrits.addAll(trits);
        trits = newTrits;
    }

    public void shiftRight() {
        shiftRight(1);
    }

    public void shiftRight(int num) {
        ensureCapacity(trits.size() + num);
        for (int i = 0; i < num; i++) trits.remove(0);
    }

    public byte byteValue() {
        return toBigInteger().byteValue();
    }

    public double doubleValue() {
        return toBigInteger().doubleValue();
    }

    public float floatValue() {
        return toBigInteger().floatValue();
    }

    public int intValue() {
        return toBigInteger().intValue();
    }

    public long longValue() {
        return toBigInteger().longValue();
    }

    public short shortValue() {
        return toBigInteger().shortValue();
    }

    /**
	 * Add the specified addend to this augend
	 *
	 * @param addend to be added
	 * @return ternary summation
	 */
    public Ternary add(Ternary addend) {
        if (tritLength() < addend.tritLength()) return addend.add(this);
        Ternary summation = new Ternary(this);
        Ternary toCarry = new Ternary();
        for (int pos = 0; pos < trits.size(); pos++) {
            Trit a = summation.getTrit(pos);
            Trit b = addend.getTrit(pos);
            switch(a.state) {
                case Negative:
                    summation.setTrit(pos, b.rotateDown());
                    break;
                case Positive:
                    summation.setTrit(pos, b.rotateUp());
                    break;
                default:
                    summation.setTrit(pos, b);
            }
            if (a.equals(b)) toCarry.setTrit(pos + 1, a);
        }
        if (toCarry.tritLength() == 0) return summation;
        return summation.add(toCarry);
    }

    /**
	 * Subtract the specified subtrahend from this minuend
	 *
	 * @param subtrahend to be subtracted
	 * @return ternary difference
	 */
    public Ternary subtract(Ternary subtrahend) {
        return add(invert(subtrahend));
    }

    public static Ternary invert(Ternary toInvert) {
        Ternary inverted = new Ternary();
        for (int i = 0; i < toInvert.trits.size(); i++) inverted.setTrit(i, toInvert.getTrit(i).invert());
        return inverted;
    }

    /**
	 * Multiply this multiplicand by the specified multiplier
	 *
	 * @param multiplier to multiply by
	 * @return ternary product
	 */
    public Ternary multiply(Ternary multiplier) {
        if (tritLength() < multiplier.tritLength()) return multiplier.multiply(this);
        Ternary product = new Ternary();
        for (int posB = 0; posB < multiplier.trits.size(); posB++) {
            Ternary row = new Ternary();
            for (int posA = 0; posA < trits.size(); posA++) {
                Trit a = getTrit(posA);
                Trit b = multiplier.getTrit(posB);
                Trit c = a.equality(b);
                if (a.isNeutral() && b.isNeutral()) c = Trit.NEUTRAL;
                row.setTrit(posA + posB, c);
            }
            product = product.add(row);
        }
        return product;
    }

    /**
	 * Divide this divided by the specified divisor
	 *
	 * @param divisor to divide by
	 * @return ternary[] containing {quotient,remainder}
	 */
    public Ternary[] divide(Ternary divisor) {
        Ternary dividend = new Ternary(this);
        Ternary quotient = new Ternary(0);
        Ternary remainder = new Ternary(0);
        int dividendSign = dividend.signum();
        if (dividendSign == 0) return new Ternary[] { quotient, remainder };
        int divisorSign = divisor.signum();
        if (divisorSign == 0) throw new ArithmeticException("Divide by Zero not currently supported.");
        if (dividendSign != divisorSign) {
            Ternary tmp = null;
            Ternary[] results = null;
            if (dividendSign < 0) {
                tmp = new Ternary(dividend);
                tmp = invert(tmp);
                results = tmp.divide(divisor);
            } else {
                tmp = new Ternary(divisor);
                tmp = invert(tmp);
                results = dividend.divide(tmp);
            }
            quotient = invert(results[0]);
            remainder = dividend.subtract(quotient.multiply(divisor));
            return new Ternary[] { quotient, remainder };
        }
        if (dividendSign < 0) {
            dividend = invert(dividend);
            divisor = invert(divisor);
        }
        int position = dividend.tritLength() - 1;
        while (position >= 0) {
            remainder = (new Ternary(dividend)).subtract(quotient.multiply(divisor));
            remainder.shiftRight(position);
            int compare = remainder.compareTo(divisor);
            if (compare > 0) {
                quotient.increment();
            } else if (compare < 0) {
                if (position > 0) quotient.shiftLeft(1);
                position--;
            } else {
                quotient.increment();
                position--;
            }
        }
        remainder = (new Ternary(dividend)).subtract(quotient.multiply(divisor));
        return new Ternary[] { quotient, remainder };
    }

    public Ternary sqrt() {
        return sqrt(this);
    }

    public static Ternary sqrt(Ternary number) {
        return sqrt(number, new Ternary(1));
    }

    protected static Ternary sqrt(Ternary number, Ternary guess) {
        Ternary result = new Ternary(0);
        Ternary flipA = new Ternary(result);
        Ternary flipB = new Ternary(result);
        boolean first = true;
        while (result.compareTo(guess) != 0) {
            if (!first) guess = result; else first = false;
            result = (number.divide(guess))[0];
            result = result.add(guess);
            result = (result.divide(new Ternary(2)))[0];
            result.trim();
            if (result.equals(flipB)) return flipA;
            flipB = flipA;
            flipB.trim();
            flipA = result;
            flipA.trim();
        }
        return result;
    }
}
