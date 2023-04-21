package net.sf.signs.sim.gates;

import java.math.BigInteger;
import net.sf.signs.Port;
import net.sf.signs.PortBit;
import net.sf.signs.Value;
import net.sf.signs.gates.MathOp;
import net.sf.signs.intermediate.OperationMath;
import net.sf.signs.sim.SimException;
import net.sf.signs.sim.Simulator;

/**
 * @author bartscgr
 */
public class MathOpBehaviour {

    public static final int DELAY = 2;

    public static void init(MathOp gate_, Simulator sim_) throws SimException {
    }

    public static void setPort(MathOp gate_, PortBit port_, char value_, Simulator sim_) throws SimException {
        sim_.setValue(port_, value_);
        if (port_.getDirection() == Port.DIR_OUT) {
            return;
        }
        Port a = gate_.getA();
        Port b = gate_.getB();
        String av = sim_.getValue(a);
        if (!a.getType().isAscending()) {
            StringBuffer buf = new StringBuffer(av);
            av = buf.reverse().toString();
        }
        String bv = null;
        if (b != null) {
            bv = sim_.getValue(b);
            if (!b.getType().isAscending()) {
                StringBuffer buf = new StringBuffer(bv);
                bv = buf.reverse().toString();
            }
        }
        BigInteger ai = Value.getBigInt(av);
        BigInteger bi = null;
        if (bv != null) bi = Value.getBigInt(bv);
        BigInteger zi = ai;
        switch(gate_.getOp()) {
            case OperationMath.OP_NEG:
                zi = bi.multiply(new BigInteger("-1"));
                break;
            case OperationMath.OP_ADD:
                zi = ai.add(bi);
                break;
            case OperationMath.OP_SUB:
                zi = ai.subtract(bi);
                break;
            case OperationMath.OP_MUL:
                zi = ai.multiply(bi);
                break;
            case OperationMath.OP_DIV:
                zi = ai.divide(bi);
                break;
            case OperationMath.OP_MOD:
                zi = ai.mod(bi);
                break;
            case OperationMath.OP_REM:
                zi = ai.remainder(bi);
                break;
            case OperationMath.OP_POWER:
                zi = ai.pow(bi.intValue());
                break;
            case OperationMath.OP_ABS:
                zi = ai.abs();
                break;
            default:
                throw new SimException("Internal sim error: unknown math op " + gate_.getOp());
        }
        String zv = Value.convert(zi, gate_.getType().getWidth());
        Port z = gate_.getZ();
        if (!z.getType().isAscending()) {
            StringBuffer buf = new StringBuffer(zv);
            zv = buf.reverse().toString();
        }
        sim_.scheduleEvent(DELAY, z, zv);
    }
}
