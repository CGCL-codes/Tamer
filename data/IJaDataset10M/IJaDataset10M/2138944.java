package com.newisys.verilog.pli;

import com.newisys.verilog.VerilogPort;
import com.newisys.verilog.VerilogPortBit;

/**
 * PLI implementation of VerilogPortBit.
 * 
 * @author Trevor Robinson
 */
public final class PLIVerilogPortBit extends PLIVerilogPorts implements VerilogPortBit {

    public PLIVerilogPortBit(PLIInterface pliIntf, long handle) {
        super(pliIntf, PLIObjectType.PORT_BIT, handle);
    }

    public final VerilogPort getParent() {
        return (VerilogPort) getRelatedObject(PLIRelationTypeConstants.vpiParent);
    }
}
