package net.sf.openforge.lim.op;

import java.util.*;
import net.sf.openforge.lim.*;
import net.sf.openforge.util.SizedInteger;

/**
 * A unary arithmetic negation operation in a form of -.
 *
 * Created: Thu Mar 08 16:39:34 2002
 *
 * @author  Conor Wu
 * @version $Id: MinusOp.java 2 2005-06-09 20:00:48Z imiller $
 */
public class MinusOp extends UnaryOp implements Emulatable {

    private static final String _RCS_ = "$Rev: 2 $";

    /**
     * Constructs an arithmetic negation minus operation.
     *
     */
    public MinusOp() {
        super();
    }

    /**
     * Gets the gate depth of this component.  This is the maximum number of gates
     * that any input signal must traverse before reaching an {@link Exit}.
     *
     * @return a non-negative integer
     */
    public int getGateDepth() {
        final int width = getDataPort().getValue().getSize();
        return (3 * (width - 1)) + 2;
    }

    /**
     * Accept method for the Visitor interface
     */
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * Performes a high level numerical emulation of this component.
     *
     * @param portValues a map of owner {@link Port} to {@link SizedInteger}
     *          input value
     * @return a map of {@link Bus} to {@link SizedInteger} result value
     */
    public Map emulate(Map portValues) {
        final SizedInteger inval = (SizedInteger) portValues.get(getDataPort());
        return Collections.singletonMap(getResultBus(), inval.negate());
    }

    /**
     * Pushes size, care, and constant information forward through
     * this MinusOp according to this rule:
     *
     * All result bit are cares.  Any consecutive constant lsb bits
     * on both inputs can be pre-calculated by doing the addition.
     *
     * @return a value of type 'boolean'
     */
    public boolean pushValuesForward() {
        boolean mod = false;
        boolean keepGoing = true;
        Value inValue = getDataPort().getValue();
        int newSize = inValue.getSize();
        Value newValue = new Value(newSize, inValue.isSigned());
        long value = -inValue.getValueMask();
        for (int i = 0; i < newSize && keepGoing; i++) {
            Bit bit0 = inValue.getBit(i);
            if (bit0.isConstant()) {
                if (((value >>> i) & 0x1L) != 0) newValue.setBit(i, Bit.ONE); else newValue.setBit(i, Bit.ZERO);
            } else {
                keepGoing = false;
            }
        }
        if (getResultBus().getValue() != null) {
            if (!inValue.isConstant()) {
                int compactedSize = Math.min(newSize, inValue.getCompactedSize() + 1);
                Bit carryoutBit = getResultBus().getValue().getBit(compactedSize - 1);
                for (int i = compactedSize; i < newSize; i++) {
                    if (newValue.getBit(i) != Bit.DONT_CARE) newValue.setBit(i, carryoutBit);
                }
            }
        }
        mod |= getResultBus().pushValueForward(newValue);
        return mod;
    }

    /**
     * Reverse constant prop on an MinusOp simply propagates the
     * result bus value back to the Ports. Any bits in the output
     * produce care bits on the inputs.
     *
     * @return a value of type 'boolean'
     */
    public boolean pushValuesBackward() {
        boolean mod = false;
        Value resultBusValue = getResultBus().getValue();
        Value newValue = new Value(resultBusValue.getSize(), resultBusValue.isSigned());
        for (int i = 0; i < resultBusValue.getSize(); i++) {
            Bit bit = resultBusValue.getBit(i);
            if (!bit.isCare() || bit.isConstant()) {
                newValue.setBit(i, Bit.DONT_CARE);
            }
        }
        if (!getDataPort().getValue().isConstant()) {
            mod |= getDataPort().pushValueBackward(newValue);
        }
        return mod;
    }
}
