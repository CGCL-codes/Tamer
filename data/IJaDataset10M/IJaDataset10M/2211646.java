package com.ibm.JikesRVM.opt;

import com.ibm.JikesRVM.*;
import com.ibm.JikesRVM.opt.ir.*;

/**
 * Assemble PowerPC MIR into binary code.
 *
 * @author Jong-Deok Choi
 * @author Dave Grove
 * @author Igor Pechtchanski
 * @author Mauricio Serrano
 */
public final class OPT_Assembler implements OPT_Operators, VM_Constants {

    private static final boolean DEBUG = false;

    private static final boolean DEBUG_CODE_PATCH = false;

    private static final int REG_MASK = 0x1F;

    private static final int SHORT_MASK = 0xFFFF;

    private static final int LI_MASK = 0x3FFFFFC;

    private static final int BD_MASK = 0xFFFC;

    static final int MAX_24_BITS = 0x7FFFFF;

    private static final int MIN_24_BITS = -0x800000;

    private static final int MAX_14_BITS = 0x1FFF;

    private static final int MIN_14_BITS = -0x2000;

    static final int MAX_COND_DISPL = MAX_14_BITS;

    private static final int MIN_COND_DISPL = MIN_14_BITS;

    private static final int MAX_DISPL = MAX_24_BITS;

    private static final int MIN_DISPL = MIN_24_BITS;

    private static final int CFLIP_MASK = 0x14 << 21;

    private static final int NOPtemplate = (24 << 26);

    private static final int Btemplate = (18 << 26);

    private int unresolvedBranches = 0;

    /**
   * Generate machine code into ir.MIRInfo.machinecode.
   * 
   * @param ir the IR to generate
   * @return   the number of machinecode instructions generated
   */
    public static final int generateCode(OPT_IR ir, boolean shouldPrint) {
        return new OPT_Assembler().genCode(ir, shouldPrint);
    }

    private final int genCode(OPT_IR ir, boolean shouldPrint) {
        int mi = 0;
        INSTRUCTION[] machinecodes = ir.MIRInfo.machinecode;
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        boolean unsafeCondDispl = machinecodes.length > MAX_COND_DISPL;
        boolean unsafeDispl = machinecodes.length > MAX_DISPL;
        for (OPT_Instruction p = ir.firstInstructionInCodeOrder(); p != null; p = p.nextInstructionInCodeOrder()) {
            int inst = p.operator().instTemplate;
            switch(p.getOpcode()) {
                case LABEL_opcode:
                    for (BranchSrcElement bSrc = (BranchSrcElement) p.scratchObject; bSrc != null; bSrc = bSrc.next) {
                        OPT_Instruction branchStmt = bSrc.source;
                        int bo = branchStmt.getmcOffset() - (1 << LG_INSTRUCTION_WIDTH);
                        int bi = bo >> LG_INSTRUCTION_WIDTH;
                        int targetOffset = (mi - bi) << LG_INSTRUCTION_WIDTH;
                        boolean setLink = false;
                        if (targetOffset > MAX_DISPL << LG_INSTRUCTION_WIDTH) {
                            throw new OPT_OptimizingCompilerException("CodeGen", "Branch positive offset too large: ", targetOffset);
                        }
                        switch(branchStmt.getOpcode()) {
                            case PPC_B_opcode:
                            case PPC_BL_opcode:
                                machinecodes[bi] |= targetOffset & LI_MASK;
                                break;
                            case PPC_DATA_LABEL_opcode:
                                machinecodes[bi] = targetOffset & LI_MASK;
                                break;
                            case IG_PATCH_POINT_opcode:
                                break;
                            case PPC_BCL_opcode:
                                setLink = true;
                            default:
                                if (targetOffset <= MAX_COND_DISPL << 2) {
                                    machinecodes[bi] |= targetOffset & BD_MASK;
                                    if (DEBUG) {
                                        VM.sysWrite("**** Forward Short Cond. Branch ****\n");
                                        VM.sysWrite(disasm(machinecodes[bi], 0) + "\n");
                                    }
                                } else {
                                    branchStmt.setmcOffset(branchStmt.getmcOffset() + (1 << LG_INSTRUCTION_WIDTH));
                                    machinecodes[bi] = flipCondition(machinecodes[bi]);
                                    machinecodes[bi] |= (2 << LG_INSTRUCTION_WIDTH);
                                    machinecodes[bi] &= 0xfffffffe;
                                    machinecodes[bi + 1] = Btemplate | ((targetOffset - 4) & LI_MASK);
                                    if (setLink) machinecodes[bi + 1] |= 1;
                                    if (DEBUG) {
                                        VM.sysWrite("**** Forward Long Cond. Branch ****\n");
                                        VM.sysWrite(disasm(machinecodes[bi], 0) + "\n");
                                        VM.sysWrite(disasm(machinecodes[bi + 1], 0) + "\n");
                                    }
                                }
                                break;
                        }
                        unresolvedBranches--;
                    }
                    p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    break;
                case BBEND_opcode:
                case UNINT_BEGIN_opcode:
                case UNINT_END_opcode:
                case GUARD_MOVE_opcode:
                case GUARD_COMBINE_opcode:
                    p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    break;
                case PPC_DATA_INT_opcode:
                    {
                        int value = MIR_DataInt.getValue(p).value;
                        machinecodes[mi++] = value;
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_DATA_LABEL_opcode:
                    {
                        OPT_Instruction target = MIR_DataLabel.getTarget(p).target;
                        int targetOffset = resolveBranch(p, target, mi);
                        machinecodes[mi++] = targetOffset;
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_CRAND_opcode:
                case PPC_CRANDC_opcode:
                case PPC_CROR_opcode:
                case PPC_CRORC_opcode:
                    {
                        int op0 = MIR_Condition.getResultBit(p).value & REG_MASK;
                        int op1 = MIR_Condition.getValue1Bit(p).value & REG_MASK;
                        int op2 = MIR_Condition.getValue2Bit(p).value & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_ADD_opcode:
                case PPC_ADDr_opcode:
                case PPC_ADDC_opcode:
                case PPC_ADDE_opcode:
                case PPC_SUBF_opcode:
                case PPC_SUBFr_opcode:
                case PPC_SUBFC_opcode:
                case PPC_SUBFCr_opcode:
                case PPC_SUBFE_opcode:
                case PPC_FADD_opcode:
                case PPC_FADDS_opcode:
                case PPC_FDIV_opcode:
                case PPC_FDIVS_opcode:
                case PPC_DIVW_opcode:
                case PPC_DIVWU_opcode:
                case PPC_MULLW_opcode:
                case PPC_MULHW_opcode:
                case PPC_MULHWU_opcode:
                case PPC_FSUB_opcode:
                case PPC_FSUBS_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LWZX_opcode:
                case PPC_LWARX_opcode:
                case PPC_LBZX_opcode:
                case PPC_LHAX_opcode:
                case PPC_LHZX_opcode:
                case PPC_LFDX_opcode:
                case PPC_LFSX_opcode:
                    {
                        int op0 = MIR_Load.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Load.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_Load.getOffset(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_STWX_opcode:
                case PPC_STWCXr_opcode:
                case PPC_STBX_opcode:
                case PPC_STHX_opcode:
                case PPC_STFDX_opcode:
                case PPC_STFSX_opcode:
                    {
                        int op0 = MIR_Store.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_Store.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_Store.getOffset(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LWZUX_opcode:
                case PPC_LBZUX_opcode:
                    {
                        int op0 = MIR_LoadUpdate.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_LoadUpdate.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_LoadUpdate.getOffset(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LWZU_opcode:
                    {
                        int op0 = MIR_LoadUpdate.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_LoadUpdate.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_LoadUpdate.getOffset(p).asIntConstant().value & SHORT_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | op2);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_TW_opcode:
                    {
                        int op0 = MIR_Trap.getCond(p).value;
                        int op1 = MIR_Trap.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Trap.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_TWI_opcode:
                    {
                        int op0 = MIR_Trap.getCond(p).value;
                        int op1 = MIR_Trap.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Trap.getValue2(p).asIntConstant().value & SHORT_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | op2);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case NULL_CHECK_opcode:
                    {
                        int op0 = OPT_PowerPCTrapOperand.LOWER;
                        int op1 = ((OPT_RegisterOperand) NullCheck.getRef(p)).register.number & REG_MASK;
                        int op2 = 1;
                        inst = PPC_TWI.instTemplate;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | op2);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LDI_opcode:
                case PPC_LDIS_opcode:
                    {
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asIntConstant().value & SHORT_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | op1);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_ADDIC_opcode:
                case PPC_ADDICr_opcode:
                case PPC_SUBFIC_opcode:
                case PPC_MULLI_opcode:
                case PPC_ADDI_opcode:
                case PPC_ADDIS_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SHORT_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | op2);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_CNTLZW_opcode:
                case PPC_EXTSB_opcode:
                case PPC_EXTSBr_opcode:
                case PPC_EXTSH_opcode:
                case PPC_EXTSHr_opcode:
                    {
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_ADDZE_opcode:
                case PPC_SUBFZE_opcode:
                case PPC_NEG_opcode:
                case PPC_NEGr_opcode:
                case PPC_ADDME_opcode:
                    {
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_XORI_opcode:
                case PPC_XORIS_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SHORT_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | op2);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_AND_opcode:
                case PPC_ANDr_opcode:
                case PPC_NAND_opcode:
                case PPC_NANDr_opcode:
                case PPC_ANDC_opcode:
                case PPC_ANDCr_opcode:
                case PPC_OR_opcode:
                case PPC_ORr_opcode:
                case PPC_NOR_opcode:
                case PPC_NORr_opcode:
                case PPC_ORC_opcode:
                case PPC_ORCr_opcode:
                case PPC_XOR_opcode:
                case PPC_XORr_opcode:
                case PPC_EQV_opcode:
                case PPC_EQVr_opcode:
                case PPC_SLW_opcode:
                case PPC_SLWr_opcode:
                case PPC_SRW_opcode:
                case PPC_SRWr_opcode:
                case PPC_SRAW_opcode:
                case PPC_SRAWr_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_MOVE_opcode:
                    {
                        int op0 = MIR_Move.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Move.getValue(p).register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_SRWI_opcode:
                case PPC_SRWIr_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int shift = MIR_Binary.getValue2(p).asIntConstant().value & REG_MASK;
                        int op2 = (32 - shift);
                        int op3 = shift;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_SLWI_opcode:
                case PPC_SLWIr_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int shift = MIR_Binary.getValue2(p).asIntConstant().value & REG_MASK;
                        int op2 = shift;
                        int op3 = (31 - shift);
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 1));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_SRAWI_opcode:
                case PPC_SRAWIr_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_ANDIr_opcode:
                case PPC_ANDISr_opcode:
                case PPC_ORI_opcode:
                case PPC_ORIS_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SHORT_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | op2);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_RLWINM_opcode:
                case PPC_RLWINMr_opcode:
                    {
                        int op0 = MIR_RotateAndMask.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_RotateAndMask.getValue(p).register.number & REG_MASK;
                        int op2 = MIR_RotateAndMask.getShift(p).asIntConstant().value & REG_MASK;
                        int op3 = MIR_RotateAndMask.getMaskBegin(p).value & REG_MASK;
                        int op4 = MIR_RotateAndMask.getMaskEnd(p).value & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6) | (op4 << 1));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_RLWIMI_opcode:
                case PPC_RLWIMIr_opcode:
                    {
                        int op0 = MIR_RotateAndMask.getResult(p).register.number & REG_MASK;
                        int op0f = MIR_RotateAndMask.getSource(p).register.number & REG_MASK;
                        if (op0 != op0f) throw new OPT_OptimizingCompilerException("CodeGen", "format for RLWIMI is incorrect");
                        int op1 = MIR_RotateAndMask.getValue(p).register.number & REG_MASK;
                        int op2 = MIR_RotateAndMask.getShift(p).asIntConstant().value & REG_MASK;
                        int op3 = MIR_RotateAndMask.getMaskBegin(p).value & REG_MASK;
                        int op4 = MIR_RotateAndMask.getMaskEnd(p).value & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6) | (op4 << 1));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_RLWNM_opcode:
                case PPC_RLWNMr_opcode:
                    {
                        int op0 = MIR_RotateAndMask.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_RotateAndMask.getValue(p).register.number & REG_MASK;
                        int op2 = MIR_RotateAndMask.getShift(p).asRegister().register.number & REG_MASK;
                        int op3 = MIR_RotateAndMask.getMaskBegin(p).value & REG_MASK;
                        int op4 = MIR_RotateAndMask.getMaskEnd(p).value & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6) | (op4 << 1));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_B_opcode:
                    {
                        OPT_BranchOperand o = MIR_Branch.getTarget(p);
                        int targetOffset = resolveBranch(p, o.target, mi);
                        machinecodes[mi++] = inst | (targetOffset & LI_MASK);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_BLR_opcode:
                case PPC_BCTR_opcode:
                    {
                        machinecodes[mi++] = inst;
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_BC_opcode:
                case PPC_BCOND_opcode:
                case PPC_BCC_opcode:
                    {
                        int op0 = MIR_CondBranch.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_CondBranch.getCond(p).value;
                        int bo_bi = op0 << 2 | op1;
                        OPT_BranchOperand o = MIR_CondBranch.getTarget(p);
                        int targetOffset = resolveBranch(p, o.target, mi);
                        if (targetOffset == 0) {
                            if (DEBUG) VM.sysWrite("**** Forward Cond. Branch ****\n");
                            machinecodes[mi++] = inst | (bo_bi << 16);
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                            if (unsafeCondDispl) {
                                machinecodes[mi++] = NOPtemplate;
                                if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                            }
                        } else if (targetOffset < MIN_COND_DISPL << 2) {
                            if (DEBUG) VM.sysWrite("**** Backward Long Cond. Branch ****\n");
                            if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                            machinecodes[mi++] = inst | flipCondition(bo_bi << 16) | (2 << 2);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                            machinecodes[mi++] = Btemplate | ((targetOffset - 4) & LI_MASK);
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                        } else {
                            if (DEBUG) VM.sysWrite("**** Backward Short Cond. Branch ****\n");
                            machinecodes[mi++] = inst | (bo_bi << 16) | (targetOffset & BD_MASK);
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                        }
                    }
                    break;
                case PPC_BCLR_opcode:
                case PPC_BCCTR_opcode:
                    {
                        int op0 = MIR_CondBranch.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_CondBranch.getCond(p).value;
                        int bo_bi = op0 << 2 | op1;
                        machinecodes[mi++] = inst | (bo_bi << 16);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0));
                    }
                    break;
                case PPC_BL_opcode:
                    {
                        OPT_BranchOperand o = (OPT_BranchOperand) MIR_Call.getTarget(p);
                        int targetOffset = resolveBranch(p, o.target, mi);
                        machinecodes[mi++] = inst | (targetOffset & LI_MASK);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_BLRL_opcode:
                case PPC_BCTRL_opcode:
                    {
                        machinecodes[mi++] = inst;
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_BCL_opcode:
                    {
                        int op0 = MIR_CondCall.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_CondCall.getCond(p).value;
                        int bo_bi = op0 << 2 | op1;
                        OPT_BranchOperand o = (OPT_BranchOperand) MIR_CondCall.getTarget(p);
                        int targetOffset = resolveBranch(p, o.target, mi);
                        if (targetOffset == 0) {
                            if (DEBUG) VM.sysWrite("**** Forward Cond. Branch ****\n");
                            machinecodes[mi++] = inst | (bo_bi << 16);
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                            if (unsafeCondDispl) {
                                machinecodes[mi++] = NOPtemplate;
                                if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                            }
                        } else if (targetOffset < MIN_COND_DISPL << 2) {
                            throw new OPT_OperationNotImplementedException("Support for long backwards conditional branch and link is incorrect.");
                        } else {
                            if (DEBUG) VM.sysWrite("**** Backward Short Cond. Branch ****\n");
                            machinecodes[mi++] = inst | (bo_bi << 16) | (targetOffset & BD_MASK);
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0) + "\n");
                        }
                    }
                    break;
                case PPC_BCLRL_opcode:
                    {
                        int op0 = MIR_CondCall.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_CondCall.getCond(p).value;
                        int bo_bi = op0 << 2 | op1;
                        machinecodes[mi++] = inst | (bo_bi << 16);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        if (DEBUG) VM.sysWrite(disasm(machinecodes[mi - 1], 0));
                    }
                    break;
                case PPC_CMP_opcode:
                case PPC_CMPL_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 23) | (op1 << 16) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_CMPI_opcode:
                case PPC_CMPLI_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SHORT_MASK;
                        machinecodes[mi++] = (inst | (op0 << 23) | (op1 << 16) | op2);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FMR_opcode:
                    {
                        int op0 = MIR_Move.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Move.getValue(p).register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FABS_opcode:
                case PPC_FCFID_opcode:
                case PPC_FNEG_opcode:
                case PPC_FRSP_opcode:
                case PPC_FCTIW_opcode:
                case PPC_FCTIWZ_opcode:
                    {
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FCMPO_opcode:
                case PPC_FCMPU_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 23) | (op1 << 16) | (op2 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FMUL_opcode:
                case PPC_FMULS_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | (op2 << 6));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FMADD_opcode:
                case PPC_FMADDS_opcode:
                case PPC_FMSUB_opcode:
                case PPC_FMSUBS_opcode:
                case PPC_FNMADD_opcode:
                case PPC_FNMADDS_opcode:
                case PPC_FNMSUB_opcode:
                case PPC_FNMSUBS_opcode:
                case PPC_FSEL_opcode:
                    {
                        int op0 = MIR_Ternary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Ternary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Ternary.getValue2(p).register.number & REG_MASK;
                        int op3 = MIR_Ternary.getValue3(p).register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | (op2 << 6) | (op3 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LWZ_opcode:
                case PPC_LBZ_opcode:
                case PPC_LHA_opcode:
                case PPC_LHZ_opcode:
                case PPC_LFD_opcode:
                case PPC_LFS_opcode:
                case PPC_LMW_opcode:
                    {
                        int op0 = MIR_Load.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Load.getOffset(p).asIntConstant().value & SHORT_MASK;
                        int op2 = MIR_Load.getAddress(p).register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | op1 | (op2 << 16));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_STW_opcode:
                case PPC_STB_opcode:
                case PPC_STH_opcode:
                case PPC_STFD_opcode:
                case PPC_STFS_opcode:
                case PPC_STMW_opcode:
                    {
                        int op0 = MIR_Store.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_Store.getOffset(p).asIntConstant().value & SHORT_MASK;
                        int op2 = MIR_Store.getAddress(p).register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | op1 | (op2 << 16));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_STWU_opcode:
                case PPC_STFDU_opcode:
                case PPC_STFSU_opcode:
                    {
                        int op0 = MIR_StoreUpdate.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_StoreUpdate.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_StoreUpdate.getOffset(p).asIntConstant().value & SHORT_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16) | op2);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_MFSPR_opcode:
                    {
                        int op0 = MIR_Move.getResult(p).register.number & REG_MASK;
                        int op1 = phys.getSPR(MIR_Move.getValue(p).register);
                        machinecodes[mi++] = (inst | (op0 << 21) | (op1 << 16));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_MTSPR_opcode:
                    {
                        int op0 = phys.getSPR(MIR_Move.getResult(p).register);
                        int op1 = MIR_Move.getValue(p).register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 21));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_MFTB_opcode:
                case PPC_MFTBU_opcode:
                    {
                        int op0 = MIR_Move.getResult(p).register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 21));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_SYNC_opcode:
                case PPC_ISYNC_opcode:
                    {
                        machinecodes[mi++] = inst;
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_DCBST_opcode:
                case PPC_DCBF_opcode:
                case PPC_ICBI_opcode:
                    {
                        int op0 = MIR_CacheOp.getAddress(p).register.number & REG_MASK;
                        int op1 = MIR_CacheOp.getOffset(p).register.number & REG_MASK;
                        machinecodes[mi++] = (inst | (op0 << 16) | (op1 << 11));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case IG_PATCH_POINT_opcode:
                    {
                        OPT_BranchOperand bop = InlineGuard.getTarget(p);
                        OPT_Instruction target = bop.target;
                        if (VM.VerifyAssertions) {
                            VM._assert(target.getOpcode() == LABEL_opcode);
                        }
                        int targetOffset = resolveBranch(p, target, mi);
                        machinecodes[mi++] = NOPtemplate;
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        if (DEBUG_CODE_PATCH) {
                            VM.sysWrite("to be patched at ", mi - 1);
                            VM.sysWrite(" inst ");
                            VM.sysWriteHex(machinecodes[mi - 1]);
                            VM.sysWrite("\n");
                        }
                    }
                    break;
                default:
                    throw new OPT_OptimizingCompilerException("CodeGen", "OPCODE not implemented:", p);
            }
        }
        if (unresolvedBranches != 0) throw new OPT_OptimizingCompilerException("CodeGen", " !!! Unresolved Branch Targets Exist!!! \n");
        if (shouldPrint) {
            OPT_Compiler.header("Final machine code", ir.method);
            for (int i = 0; i < machinecodes.length; i++) {
                System.out.print(VM_Services.getHexString(i << LG_INSTRUCTION_WIDTH, true) + " : " + VM_Services.getHexString(machinecodes[i], false));
                System.out.print("  ");
                System.out.print(disasm(machinecodes[i], i << LG_INSTRUCTION_WIDTH));
                System.out.println();
            }
        }
        return mi;
    }

    private static final class BranchSrcElement {

        OPT_Instruction source;

        BranchSrcElement next;

        BranchSrcElement(OPT_Instruction src, BranchSrcElement Next) {
            source = src;
            next = Next;
        }
    }

    /**
   * Resolve a branch instruction to a machine code offset.
   * @param src
   * @param tgt
   * @param mi
   */
    private int resolveBranch(OPT_Instruction src, OPT_Instruction tgt, int mi) {
        if (tgt.getmcOffset() < 0) {
            unresolvedBranches++;
            tgt.scratchObject = new BranchSrcElement(src, (BranchSrcElement) tgt.scratchObject);
            return 0;
        } else {
            int targetOffset = tgt.getmcOffset() - (mi << LG_INSTRUCTION_WIDTH);
            if (targetOffset < (MIN_DISPL << LG_INSTRUCTION_WIDTH)) {
                throw new OPT_OptimizingCompilerException("CodeGen", " Branch negative offset too large: ", targetOffset);
            }
            return targetOffset;
        }
    }

    private int flipCondition(int inst) {
        int flip = (~inst & CFLIP_MASK) >> 1;
        return (inst ^ flip);
    }

    /**
   * Debugging support (return a printable representation of the machine code).
   *
   * @param instr, an integer to be interpreted as a PowerPC instruction
   * @param offset the mcoffset (in bytes) of the instruction
   */
    private String disasm(int instr, int offset) {
        return PPC_Disassembler.disasm(instr, offset);
    }

    /** Apply a patch.
   * The instruction at patchOffset should be a NOP instruction.
   * It is replaced by a "B rel32" instruction. 
   *  
   * @param code        the code intructions to be patched
   * @param patchOffset the offset of the last byte of the patch point
   * @param rel32       the new immediate to use in the branch instruction
   * 
   */
    static final void patchCode(INSTRUCTION[] code, int patchOffset, int rel32) {
        if (DEBUG_CODE_PATCH) {
            VM.sysWrite("patching at ", patchOffset);
            VM.sysWrite(" inst ");
            VM.sysWriteHex(code[patchOffset]);
            VM.sysWriteln(" offset ", rel32);
        }
        if (VM.VerifyAssertions) {
            VM._assert(code[patchOffset] == NOPtemplate);
            VM._assert(rel32 <= (MAX_DISPL << LG_INSTRUCTION_WIDTH));
            VM._assert(rel32 >= (MIN_DISPL << LG_INSTRUCTION_WIDTH));
        }
        code[patchOffset] = (18 << 26) | (rel32 & LI_MASK);
    }
}
