package org.zamia.rtl.sim.behaviors;

import org.zamia.ExceptionLogger;
import org.zamia.SourceLocation;
import org.zamia.ZamiaException;
import org.zamia.ZamiaLogger;
import org.zamia.rtl.RTLNode;
import org.zamia.rtl.RTLPort;
import org.zamia.rtl.RTLType;
import org.zamia.rtl.RTLValue;
import org.zamia.rtl.RTLValueBuilder;
import org.zamia.rtl.RTLValue.BitValue;
import org.zamia.rtl.nodes.RTLNBinaryOp;
import org.zamia.rtl.nodes.RTLNBinaryOp.BinaryOp;
import org.zamia.rtl.sim.RTLNodeBehavior;
import org.zamia.rtl.sim.RTLPortSimAnnotation;
import org.zamia.rtl.sim.RTLSimContext;
import org.zamia.rtl.sim.RTLSimulator;

/**
 * 
 * @author Guenter Bartsch
 * 
 */
public class RTLBBinaryOp implements RTLNodeBehavior {

    public static final ZamiaLogger logger = ZamiaLogger.getInstance();

    public static final ExceptionLogger el = ExceptionLogger.getInstance();

    @Override
    public void portChange(RTLPortSimAnnotation aPA, RTLValue aValue, RTLSimulator aSimulator) throws ZamiaException {
        RTLPort port = aPA.getPort();
        RTLNode node = port.getNode();
        RTLNBinaryOp bop = (RTLNBinaryOp) node;
        RTLPort z = bop.getZ();
        if (port == z) return;
        aPA.setValue(aValue);
        RTLSimContext context = aPA.getContext();
        RTLPortSimAnnotation a = context.findPortSimAnnotation(bop.getA());
        RTLPortSimAnnotation b = context.findPortSimAnnotation(bop.getB());
        BinaryOp op = bop.getOp();
        RTLType t = a.getPort().getType();
        RTLValue va = a.getValue();
        RTLValue vb = b.getValue();
        logger.debug("RTLSimulator: %s", "Binary operation, type=" + t + ", a=" + va + ", b=" + vb + ", op=" + op);
        SourceLocation location = port.computeSourceLocation();
        RTLValue vz = null;
        switch(t.getCat()) {
            case BIT:
                switch(op) {
                    case XOR:
                        switch(va.getBit()) {
                            case BV_0:
                                switch(vb.getBit()) {
                                    case BV_0:
                                        vz = va;
                                        break;
                                    case BV_1:
                                        vz = vb;
                                        break;
                                    default:
                                        vz = RTLValueBuilder.generateBit(t, BitValue.BV_X, location, aSimulator.getZDB());
                                }
                                break;
                            case BV_1:
                                switch(vb.getBit()) {
                                    case BV_0:
                                        vz = va;
                                        break;
                                    case BV_1:
                                        vz = RTLValueBuilder.generateBit(t, BitValue.BV_0, location, aSimulator.getZDB());
                                        break;
                                    default:
                                        vz = RTLValueBuilder.generateBit(t, BitValue.BV_X, location, aSimulator.getZDB());
                                }
                                break;
                            case BV_U:
                            case BV_X:
                            case BV_Z:
                                vz = RTLValueBuilder.generateBit(t, BitValue.BV_X, location, aSimulator.getZDB());
                                break;
                        }
                        break;
                    case AND:
                        switch(va.getBit()) {
                            case BV_0:
                                vz = va;
                                break;
                            case BV_1:
                                vz = vb;
                                break;
                            case BV_U:
                            case BV_X:
                            case BV_Z:
                                vz = RTLValueBuilder.generateBit(t, BitValue.BV_X, location, aSimulator.getZDB());
                                break;
                        }
                        break;
                    case OR:
                        switch(va.getBit()) {
                            case BV_0:
                                vz = vb;
                                break;
                            case BV_1:
                                vz = va;
                                break;
                            case BV_U:
                            case BV_X:
                            case BV_Z:
                                vz = RTLValueBuilder.generateBit(t, BitValue.BV_X, location, aSimulator.getZDB());
                                break;
                        }
                        break;
                }
                break;
        }
        if (vz != null) {
            aSimulator.setDelta(context.findPortSimAnnotation(z), vz);
            return;
        }
        throw new ZamiaException("Sorry, not implemented yet.");
    }

    @Override
    public void reset(RTLNode aNode, RTLSimulator aSimulator, RTLSimContext aContext) throws ZamiaException {
        RTLNBinaryOp bop = (RTLNBinaryOp) aNode;
        RTLPortSimAnnotation pz = aContext.findPortSimAnnotation(bop.getZ());
        pz.setDriving(true);
    }
}
