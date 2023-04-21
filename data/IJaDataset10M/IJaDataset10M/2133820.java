package org.jikesrvm.compilers.opt.mir2mc.ppc;

import org.jikesrvm.ArchitectureSpecific;
import org.jikesrvm.ArchitectureSpecific.VM_CodeArray;
import org.jikesrvm.VM;
import org.jikesrvm.VM_Constants;
import org.jikesrvm.VM_Services;
import org.jikesrvm.compilers.opt.OperationNotImplementedException;
import org.jikesrvm.compilers.opt.OptimizingCompilerException;
import org.jikesrvm.compilers.opt.driver.OptimizingCompiler;
import org.jikesrvm.compilers.opt.ir.InlineGuard;
import org.jikesrvm.compilers.opt.ir.MIR_Binary;
import org.jikesrvm.compilers.opt.ir.MIR_Branch;
import org.jikesrvm.compilers.opt.ir.MIR_CacheOp;
import org.jikesrvm.compilers.opt.ir.MIR_Call;
import org.jikesrvm.compilers.opt.ir.MIR_CondBranch;
import org.jikesrvm.compilers.opt.ir.MIR_CondCall;
import org.jikesrvm.compilers.opt.ir.MIR_Condition;
import org.jikesrvm.compilers.opt.ir.MIR_DataInt;
import org.jikesrvm.compilers.opt.ir.MIR_DataLabel;
import org.jikesrvm.compilers.opt.ir.MIR_Load;
import org.jikesrvm.compilers.opt.ir.MIR_LoadUpdate;
import org.jikesrvm.compilers.opt.ir.MIR_Move;
import org.jikesrvm.compilers.opt.ir.MIR_RotateAndMask;
import org.jikesrvm.compilers.opt.ir.MIR_Store;
import org.jikesrvm.compilers.opt.ir.MIR_StoreUpdate;
import org.jikesrvm.compilers.opt.ir.MIR_Ternary;
import org.jikesrvm.compilers.opt.ir.MIR_Trap;
import org.jikesrvm.compilers.opt.ir.MIR_Unary;
import org.jikesrvm.compilers.opt.ir.NullCheck;
import org.jikesrvm.compilers.opt.ir.IR;
import org.jikesrvm.compilers.opt.ir.Instruction;
import org.jikesrvm.compilers.opt.ir.Operators;
import org.jikesrvm.compilers.opt.ir.operand.BranchOperand;
import org.jikesrvm.compilers.opt.ir.operand.RegisterOperand;
import org.jikesrvm.compilers.opt.ir.operand.ppc.PowerPCTrapOperand;
import org.jikesrvm.compilers.opt.ir.ppc.PhysicalRegisterSet;
import org.jikesrvm.ppc.VM_ArchConstants;
import org.jikesrvm.ppc.VM_Disassembler;

/**
 * Assemble PowerPC MIR into binary code.
 */
public abstract class Assembler implements Operators, VM_Constants, VM_ArchConstants {

    private static final boolean DEBUG = false;

    private static final boolean DEBUG_CODE_PATCH = false;

    private static final int REG_MASK = 0x1F;

    private static final int SHORT_MASK = 0xFFFF;

    private static final int LI_MASK = 0x3FFFFFC;

    private static final int BD_MASK = 0xFFFC;

    public static final int MAX_24_BITS = 0x7FFFFF;

    private static final int MIN_24_BITS = -0x800000;

    private static final int MAX_14_BITS = 0x1FFF;

    private static final int MIN_14_BITS = -0x2000;

    public static final int MAX_COND_DISPL = MAX_14_BITS;

    private static final int MIN_COND_DISPL = MIN_14_BITS;

    private static final int MAX_DISPL = MAX_24_BITS;

    private static final int MIN_DISPL = MIN_24_BITS;

    private static final int SHORT14_MASK = 0x3FFF;

    private static final int SIXBIT_MASK = 0x3F;

    private static final int CFLIP_MASK = 0x14 << 21;

    private static final int NOPtemplate = (24 << 26);

    private static final int Btemplate = (18 << 26);

    private int unresolvedBranches = 0;

    /**
   * Generate machine code into ir.MIRInfo.machinecode.
   *
   * @param ir the IR to generate
   * @param shouldPrint should we print the machine code?
   * @return the number of machinecode instructions generated
   */
    public static int generateCode(IR ir, boolean shouldPrint) {
        ir.MIRInfo.machinecode = ArchitectureSpecific.VM_CodeArray.Factory.create(ir.MIRInfo.mcSizeEstimate, true);
        return new ArchitectureSpecific.Assembler().genCode(ir, shouldPrint);
    }

    protected final int genCode(IR ir, boolean shouldPrint) {
        int mi = 0;
        VM_CodeArray machinecodes = ir.MIRInfo.machinecode;
        PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        boolean unsafeCondDispl = machinecodes.length() > MAX_COND_DISPL;
        for (Instruction p = ir.firstInstructionInCodeOrder(); p != null; p = p.nextInstructionInCodeOrder()) {
            int inst = p.operator().instTemplate;
            switch(p.getOpcode()) {
                case LABEL_opcode:
                    for (BranchSrcElement bSrc = (BranchSrcElement) p.scratchObject; bSrc != null; bSrc = bSrc.next) {
                        Instruction branchStmt = bSrc.source;
                        int bo = branchStmt.getmcOffset() - (1 << LG_INSTRUCTION_WIDTH);
                        int bi = bo >> LG_INSTRUCTION_WIDTH;
                        int targetOffset = (mi - bi) << LG_INSTRUCTION_WIDTH;
                        boolean setLink = false;
                        if (targetOffset > MAX_DISPL << LG_INSTRUCTION_WIDTH) {
                            throw new OptimizingCompilerException("CodeGen", "Branch positive offset too large: ", targetOffset);
                        }
                        switch(branchStmt.getOpcode()) {
                            case PPC_B_opcode:
                            case PPC_BL_opcode:
                                machinecodes.set(bi, machinecodes.get(bi) | targetOffset & LI_MASK);
                                break;
                            case PPC_DATA_LABEL_opcode:
                                machinecodes.set(bi, targetOffset);
                                break;
                            case IG_PATCH_POINT_opcode:
                                break;
                            case PPC_BCL_opcode:
                                setLink = true;
                            default:
                                if (targetOffset <= MAX_COND_DISPL << 2) {
                                    machinecodes.set(bi, machinecodes.get(bi) | targetOffset & BD_MASK);
                                    if (DEBUG) {
                                        VM.sysWrite("**** Forward Short Cond. Branch ****\n");
                                        VM.sysWrite(disasm(machinecodes.get(bi), 0) + "\n");
                                    }
                                } else {
                                    branchStmt.setmcOffset(branchStmt.getmcOffset() + (1 << LG_INSTRUCTION_WIDTH));
                                    machinecodes.set(bi, flipCondition(machinecodes.get(bi)));
                                    machinecodes.set(bi, machinecodes.get(bi) | (2 << LG_INSTRUCTION_WIDTH));
                                    machinecodes.set(bi, machinecodes.get(bi) & 0xfffffffe);
                                    machinecodes.set(bi + 1, Btemplate | ((targetOffset - 4) & LI_MASK));
                                    if (setLink) {
                                        machinecodes.set(bi + 1, machinecodes.get(bi + 1) | 1);
                                    }
                                    if (DEBUG) {
                                        VM.sysWrite("**** Forward Long Cond. Branch ****\n");
                                        VM.sysWrite(disasm(machinecodes.get(bi), 0) + "\n");
                                        VM.sysWrite(disasm(machinecodes.get(bi + 1), 0) + "\n");
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
                        machinecodes.set(mi++, value);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_DATA_LABEL_opcode:
                    {
                        Instruction target = MIR_DataLabel.getTarget(p).target;
                        int targetOffset = resolveBranch(p, target, mi);
                        machinecodes.set(mi++, targetOffset);
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
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
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
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_MULLD_opcode:
                case PPC64_DIVD_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
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
                case PPC_LIntX_opcode:
                case PPC_LAddrARX_opcode:
                case PPC_LAddrX_opcode:
                    {
                        int op0 = MIR_Load.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Load.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_Load.getOffset(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_LDX_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Load.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Load.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_Load.getOffset(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_STWX_opcode:
                case PPC_STWCXr_opcode:
                case PPC_STBX_opcode:
                case PPC_STHX_opcode:
                case PPC_STFDX_opcode:
                case PPC_STFSX_opcode:
                case PPC_STAddrCXr_opcode:
                case PPC_STAddrX_opcode:
                case PPC_STAddrUX_opcode:
                    {
                        int op0 = MIR_Store.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_Store.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_Store.getOffset(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LWZUX_opcode:
                case PPC_LBZUX_opcode:
                case PPC_LIntUX_opcode:
                case PPC_LAddrUX_opcode:
                    {
                        int op0 = MIR_LoadUpdate.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_LoadUpdate.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_LoadUpdate.getOffset(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LWZU_opcode:
                    {
                        int op0 = MIR_LoadUpdate.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_LoadUpdate.getAddress(p).register.number & REG_MASK;
                        int op2 = MIR_LoadUpdate.getOffset(p).asIntConstant().value & SHORT_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | op2));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_TW_opcode:
                case PPC_TAddr_opcode:
                    {
                        int op0 = MIR_Trap.getCond(p).value;
                        int op1 = MIR_Trap.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Trap.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_TD_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Trap.getCond(p).value;
                        int op1 = MIR_Trap.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Trap.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_TWI_opcode:
                    {
                        int op0 = MIR_Trap.getCond(p).value;
                        int op1 = MIR_Trap.getValue1(p).register.number & REG_MASK;
                        int op2;
                        if (VM.BuildFor64Addr && MIR_Trap.getValue2(p).isLongConstant()) {
                            op2 = ((int) MIR_Trap.getValue2(p).asLongConstant().value) & SHORT_MASK;
                        } else {
                            op2 = MIR_Trap.getValue2(p).asIntConstant().value & SHORT_MASK;
                        }
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | op2));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_TDI_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Trap.getCond(p).value;
                        int op1 = MIR_Trap.getValue1(p).register.number & REG_MASK;
                        int op2;
                        if (MIR_Trap.getValue2(p).isLongConstant()) {
                            op2 = ((int) MIR_Trap.getValue2(p).asLongConstant().value) & SHORT_MASK;
                        } else {
                            op2 = MIR_Trap.getValue2(p).asIntConstant().value & SHORT_MASK;
                        }
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | op2));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case NULL_CHECK_opcode:
                    {
                        int op0 = PowerPCTrapOperand.LOWER;
                        int op1 = ((RegisterOperand) NullCheck.getRef(p)).getRegister().number & REG_MASK;
                        int op2 = 1;
                        inst = VM.BuildFor64Addr ? PPC64_TDI.instTemplate : PPC_TWI.instTemplate;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | op2));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LDI_opcode:
                case PPC_LDIS_opcode:
                    {
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asIntConstant().value & SHORT_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | op1));
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
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | op2));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_CNTLZW_opcode:
                case PPC_CNTLZAddr_opcode:
                case PPC_EXTSB_opcode:
                case PPC_EXTSBr_opcode:
                case PPC_EXTSH_opcode:
                case PPC_EXTSHr_opcode:
                    {
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_EXTSW_opcode:
                case PPC64_EXTSWr_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_EXTZW_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asRegister().register.number & REG_MASK;
                        int op3high = 1;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op3high << 5)));
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
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_XORI_opcode:
                case PPC_XORIS_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SHORT_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | op2));
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
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_SLD_opcode:
                case PPC64_SLDr_opcode:
                case PPC64_SRD_opcode:
                case PPC64_SRDr_opcode:
                case PPC64_SRAD_opcode:
                case PPC64_SRADr_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_MOVE_opcode:
                    {
                        int op0 = MIR_Move.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Move.getValue(p).register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21)));
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
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6)));
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
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 1)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_SRAWI_opcode:
                case PPC_SRAWIr_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_SRAddrI_opcode:
                    {
                        if (VM.BuildFor32Addr) {
                            int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                            int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                            int shift = MIR_Binary.getValue2(p).asIntConstant().value & REG_MASK;
                            int op2 = (32 - shift);
                            int op3 = shift;
                            machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        } else {
                            int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                            int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                            int op3 = MIR_Binary.getValue2(p).asIntConstant().value & SIXBIT_MASK;
                            int op2 = 64 - op3;
                            int op2low = op2 & 0x1F;
                            int op2high = (op2 & 0x20) >>> 5;
                            int op3low = op3 & 0x1F;
                            int op3high = (op3 & 0x20) >>> 5;
                            machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2low << 11) | (op2high << 1) | (op3low << 6) | (op3high << 5)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        }
                    }
                    break;
                case PPC_SRAAddrI_opcode:
                    {
                        if (VM.BuildFor32Addr) {
                            int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                            int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                            int op2 = MIR_Binary.getValue2(p).asIntConstant().value & REG_MASK;
                            machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        } else {
                            int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                            int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                            int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SIXBIT_MASK;
                            int op2low = op2 & 0x1F;
                            int op2high = (op2 & 0x20) >>> 5;
                            machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2low << 11) | (op2high << 1)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        }
                    }
                    break;
                case PPC64_SRADI_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SIXBIT_MASK;
                        int op2low = op2 & 0x1F;
                        int op2high = (op2 & 0x20) >>> 5;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2low << 11) | (op2high << 1)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_SRDI_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op3 = MIR_Binary.getValue2(p).asIntConstant().value & SIXBIT_MASK;
                        int op2 = 64 - op3;
                        int op2low = op2 & 0x1F;
                        int op2high = (op2 & 0x20) >>> 5;
                        int op3low = op3 & 0x1F;
                        int op3high = (op3 & 0x20) >>> 5;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2low << 11) | (op2high << 1) | (op3low << 6) | (op3high << 5)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_SLDI_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int shift = MIR_Binary.getValue2(p).asIntConstant().value & SIXBIT_MASK;
                        int op2 = shift;
                        int op2low = op2 & 0x1F;
                        int op2high = (op2 & 0x20) >>> 5;
                        int op3 = 63 - shift;
                        int op3low = op3 & 0x1F;
                        int op3high = (op3 & 0x20) >>> 5;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2low << 11) | (op2high << 1) | (op3low << 6) | (op3high << 5)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_RLDICR_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_RotateAndMask.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_RotateAndMask.getValue(p).register.number & REG_MASK;
                        int op2 = MIR_RotateAndMask.getShift(p).asIntConstant().value & SIXBIT_MASK;
                        int op2low = op2 & 0x1F;
                        int op2high = (op2 & 0x20) >>> 5;
                        int op3 = MIR_RotateAndMask.getMaskEnd(p).value & SIXBIT_MASK;
                        int op3low = op3 & 0x1F;
                        int op3high = (op3 & 0x20) >>> 5;
                        if (VM.VerifyAssertions) {
                            int op4 = MIR_RotateAndMask.getMaskBegin(p).value & SIXBIT_MASK;
                            VM._assert(op4 == 0);
                        }
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2low << 11) | (op2high << 1) | (op3low << 6) | (op3high << 5)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_RLDICL_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_RotateAndMask.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_RotateAndMask.getValue(p).register.number & REG_MASK;
                        int op2 = MIR_RotateAndMask.getShift(p).asIntConstant().value & SIXBIT_MASK;
                        int op2low = op2 & 0x1F;
                        int op2high = (op2 & 0x20) >>> 5;
                        int op3 = MIR_RotateAndMask.getMaskBegin(p).value & SIXBIT_MASK;
                        int op3low = op3 & 0x1F;
                        int op3high = (op3 & 0x20) >>> 5;
                        if (VM.VerifyAssertions) {
                            int op4 = MIR_RotateAndMask.getMaskEnd(p).value & SIXBIT_MASK;
                            VM._assert(op4 == 63);
                        }
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2low << 11) | (op2high << 1) | (op3low << 6) | (op3high << 5)));
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
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | op2));
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
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6) | (op4 << 1)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_RLWIMI_opcode:
                case PPC_RLWIMIr_opcode:
                    {
                        int op0 = MIR_RotateAndMask.getResult(p).register.number & REG_MASK;
                        int op0f = MIR_RotateAndMask.getSource(p).register.number & REG_MASK;
                        if (op0 != op0f) {
                            throw new OptimizingCompilerException("CodeGen", "format for RLWIMI is incorrect");
                        }
                        int op1 = MIR_RotateAndMask.getValue(p).register.number & REG_MASK;
                        int op2 = MIR_RotateAndMask.getShift(p).asIntConstant().value & REG_MASK;
                        int op3 = MIR_RotateAndMask.getMaskBegin(p).value & REG_MASK;
                        int op4 = MIR_RotateAndMask.getMaskEnd(p).value & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6) | (op4 << 1)));
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
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21) | (op2 << 11) | (op3 << 6) | (op4 << 1)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_B_opcode:
                    {
                        BranchOperand o = MIR_Branch.getTarget(p);
                        int targetOffset = resolveBranch(p, o.target, mi);
                        machinecodes.set(mi++, inst | (targetOffset & LI_MASK));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_BLR_opcode:
                case PPC_BCTR_opcode:
                    {
                        machinecodes.set(mi++, inst);
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
                        BranchOperand o = MIR_CondBranch.getTarget(p);
                        int targetOffset = resolveBranch(p, o.target, mi);
                        if (targetOffset == 0) {
                            if (DEBUG) VM.sysWrite("**** Forward Cond. Branch ****\n");
                            machinecodes.set(mi++, inst | (bo_bi << 16));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                            if (unsafeCondDispl) {
                                machinecodes.set(mi++, NOPtemplate);
                                if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                            }
                        } else if (targetOffset < MIN_COND_DISPL << 2) {
                            if (DEBUG) VM.sysWrite("**** Backward Long Cond. Branch ****\n");
                            if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                            machinecodes.set(mi++, inst | flipCondition(bo_bi << 16) | (2 << 2));
                            if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                            machinecodes.set(mi++, Btemplate | ((targetOffset - 4) & LI_MASK));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                        } else {
                            if (DEBUG) VM.sysWrite("**** Backward Short Cond. Branch ****\n");
                            machinecodes.set(mi++, inst | (bo_bi << 16) | (targetOffset & BD_MASK));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                        }
                    }
                    break;
                case PPC_BCLR_opcode:
                case PPC_BCCTR_opcode:
                    {
                        int op0 = MIR_CondBranch.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_CondBranch.getCond(p).value;
                        int bo_bi = op0 << 2 | op1;
                        machinecodes.set(mi++, inst | (bo_bi << 16));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0));
                    }
                    break;
                case PPC_BL_opcode:
                case PPC_BL_SYS_opcode:
                    {
                        BranchOperand o = (BranchOperand) MIR_Call.getTarget(p);
                        int targetOffset = resolveBranch(p, o.target, mi);
                        machinecodes.set(mi++, inst | (targetOffset & LI_MASK));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_BLRL_opcode:
                case PPC_BCTRL_opcode:
                case PPC_BCTRL_SYS_opcode:
                    {
                        machinecodes.set(mi++, inst);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_BCL_opcode:
                    {
                        int op0 = MIR_CondCall.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_CondCall.getCond(p).value;
                        int bo_bi = op0 << 2 | op1;
                        BranchOperand o = (BranchOperand) MIR_CondCall.getTarget(p);
                        int targetOffset = resolveBranch(p, o.target, mi);
                        if (targetOffset == 0) {
                            if (DEBUG) VM.sysWrite("**** Forward Cond. Branch ****\n");
                            machinecodes.set(mi++, inst | (bo_bi << 16));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                            if (unsafeCondDispl) {
                                machinecodes.set(mi++, NOPtemplate);
                                if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                            }
                        } else if (targetOffset < MIN_COND_DISPL << 2) {
                            throw new OperationNotImplementedException("Support for long backwards conditional branch and link is incorrect.");
                        } else {
                            if (DEBUG) VM.sysWrite("**** Backward Short Cond. Branch ****\n");
                            machinecodes.set(mi++, inst | (bo_bi << 16) | (targetOffset & BD_MASK));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                            if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0) + "\n");
                        }
                    }
                    break;
                case PPC_BCLRL_opcode:
                    {
                        int op0 = MIR_CondCall.getValue(p).register.number & REG_MASK;
                        int op1 = MIR_CondCall.getCond(p).value;
                        int bo_bi = op0 << 2 | op1;
                        machinecodes.set(mi++, inst | (bo_bi << 16));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        if (DEBUG) VM.sysWrite(disasm(machinecodes.get(mi - 1), 0));
                    }
                    break;
                case PPC_CMP_opcode:
                case PPC_CMPL_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 23) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_CMP_opcode:
                case PPC64_CMPL_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 23) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_CMPI_opcode:
                case PPC_CMPLI_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SHORT_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 23) | (op1 << 16) | op2));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_CMPI_opcode:
                case PPC64_CMPLI_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asIntConstant().value & SHORT_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 23) | (op1 << 16) | op2));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FMR_opcode:
                    {
                        int op0 = MIR_Move.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Move.getValue(p).register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FABS_opcode:
                case PPC_FNEG_opcode:
                case PPC_FSQRT_opcode:
                case PPC_FSQRTS_opcode:
                case PPC_FRSP_opcode:
                case PPC_FCTIW_opcode:
                case PPC_FCTIWZ_opcode:
                    {
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_FCFID_opcode:
                case PPC64_FCTIDZ_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Unary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Unary.getValue(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FCMPO_opcode:
                case PPC_FCMPU_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 23) | (op1 << 16) | (op2 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_FMUL_opcode:
                case PPC_FMULS_opcode:
                    {
                        int op0 = MIR_Binary.getResult(p).register.number & REG_MASK;
                        int op1 = MIR_Binary.getValue1(p).register.number & REG_MASK;
                        int op2 = MIR_Binary.getValue2(p).asRegister().register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 6)));
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
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | (op2 << 6) | (op3 << 11)));
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
                        machinecodes.set(mi++, (inst | (op0 << 21) | op1 | (op2 << 16)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_LD_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Load.getResult(p).register.number & REG_MASK;
                        int op1 = (MIR_Load.getOffset(p).asIntConstant().value >> 2) & SHORT14_MASK;
                        int op2 = MIR_Load.getAddress(p).register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 2) | (op2 << 16)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_LAddr_opcode:
                case PPC_LInt_opcode:
                    {
                        if (VM.BuildFor32Addr) {
                            int op0 = MIR_Load.getResult(p).register.number & REG_MASK;
                            int op1 = MIR_Load.getOffset(p).asIntConstant().value & SHORT_MASK;
                            int op2 = MIR_Load.getAddress(p).register.number & REG_MASK;
                            machinecodes.set(mi++, (inst | (op0 << 21) | op1 | (op2 << 16)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        } else {
                            int op0 = MIR_Load.getResult(p).register.number & REG_MASK;
                            int op1 = (MIR_Load.getOffset(p).asIntConstant().value >> 2) & SHORT14_MASK;
                            int op2 = MIR_Load.getAddress(p).register.number & REG_MASK;
                            machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 2) | (op2 << 16)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        }
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
                        machinecodes.set(mi++, (inst | (op0 << 21) | op1 | (op2 << 16)));
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
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | op2));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC64_STD_opcode:
                    {
                        if (VM.VerifyAssertions) VM._assert(VM.BuildFor64Addr);
                        int op0 = MIR_Store.getValue(p).register.number & REG_MASK;
                        int op1 = (MIR_Store.getOffset(p).asIntConstant().value >> 2) & SHORT14_MASK;
                        int op2 = MIR_Store.getAddress(p).register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 2) | (op2 << 16)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_STAddr_opcode:
                    {
                        if (VM.BuildFor32Addr) {
                            int op0 = MIR_Store.getValue(p).register.number & REG_MASK;
                            int op1 = MIR_Store.getOffset(p).asIntConstant().value & SHORT_MASK;
                            int op2 = MIR_Store.getAddress(p).register.number & REG_MASK;
                            machinecodes.set(mi++, (inst | (op0 << 21) | op1 | (op2 << 16)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        } else {
                            int op0 = MIR_Store.getValue(p).register.number & REG_MASK;
                            int op1 = (MIR_Store.getOffset(p).asIntConstant().value >> 2) & SHORT14_MASK;
                            int op2 = MIR_Store.getAddress(p).register.number & REG_MASK;
                            machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 2) | (op2 << 16)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        }
                    }
                    break;
                case PPC_STAddrU_opcode:
                    {
                        if (VM.BuildFor32Addr) {
                            int op0 = MIR_StoreUpdate.getValue(p).register.number & REG_MASK;
                            int op1 = MIR_StoreUpdate.getAddress(p).register.number & REG_MASK;
                            int op2 = MIR_StoreUpdate.getOffset(p).asIntConstant().value & SHORT_MASK;
                            machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16) | op2));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        } else {
                            int op0 = MIR_StoreUpdate.getValue(p).register.number & REG_MASK;
                            int op1 = (MIR_StoreUpdate.getOffset(p).asIntConstant().value >> 2) & SHORT14_MASK;
                            int op2 = MIR_StoreUpdate.getAddress(p).register.number & REG_MASK;
                            machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 2) | (op2 << 16)));
                            p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        }
                    }
                    break;
                case PPC_MFSPR_opcode:
                    {
                        int op0 = MIR_Move.getResult(p).register.number & REG_MASK;
                        int op1 = phys.getSPR(MIR_Move.getValue(p).register);
                        machinecodes.set(mi++, (inst | (op0 << 21) | (op1 << 16)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_MTSPR_opcode:
                    {
                        int op0 = phys.getSPR(MIR_Move.getResult(p).register);
                        int op1 = MIR_Move.getValue(p).register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 21)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_MFTB_opcode:
                case PPC_MFTBU_opcode:
                    {
                        int op0 = MIR_Move.getResult(p).register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 21)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_SYNC_opcode:
                case PPC_ISYNC_opcode:
                    {
                        machinecodes.set(mi++, inst);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case PPC_DCBST_opcode:
                case PPC_DCBT_opcode:
                case PPC_DCBTST_opcode:
                case PPC_DCBZ_opcode:
                case PPC_DCBZL_opcode:
                case PPC_DCBF_opcode:
                case PPC_ICBI_opcode:
                    {
                        int op0 = MIR_CacheOp.getAddress(p).register.number & REG_MASK;
                        int op1 = MIR_CacheOp.getOffset(p).register.number & REG_MASK;
                        machinecodes.set(mi++, (inst | (op0 << 16) | (op1 << 11)));
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                    }
                    break;
                case IG_PATCH_POINT_opcode:
                    {
                        BranchOperand bop = InlineGuard.getTarget(p);
                        Instruction target = bop.target;
                        if (VM.VerifyAssertions) {
                            VM._assert(target.getOpcode() == LABEL_opcode);
                        }
                        resolveBranch(p, target, mi);
                        machinecodes.set(mi++, NOPtemplate);
                        p.setmcOffset(mi << LG_INSTRUCTION_WIDTH);
                        if (DEBUG_CODE_PATCH) {
                            VM.sysWrite("to be patched at ", mi - 1);
                            VM.sysWrite(" inst ");
                            VM.sysWriteHex(machinecodes.get(mi - 1));
                            VM.sysWrite("\n");
                        }
                    }
                    break;
                default:
                    throw new OptimizingCompilerException("CodeGen", "OPCODE not implemented:", p);
            }
        }
        if (unresolvedBranches != 0) {
            throw new OptimizingCompilerException("CodeGen", " !!! Unresolved Branch Targets Exist!!! \n");
        }
        if (shouldPrint) {
            OptimizingCompiler.header("Final machine code", ir.method);
            for (int i = 0; i < machinecodes.length(); i++) {
                System.out.print(VM_Services.getHexString(i << LG_INSTRUCTION_WIDTH, true) + " : " + VM_Services.getHexString(machinecodes.get(i), false));
                System.out.print("  ");
                System.out.print(disasm(machinecodes.get(i), i << LG_INSTRUCTION_WIDTH));
                System.out.println();
            }
        }
        return mi;
    }

    private static final class BranchSrcElement {

        Instruction source;

        BranchSrcElement next;

        BranchSrcElement(Instruction src, BranchSrcElement Next) {
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
    private int resolveBranch(Instruction src, Instruction tgt, int mi) {
        if (tgt.getmcOffset() < 0) {
            unresolvedBranches++;
            tgt.scratchObject = new BranchSrcElement(src, (BranchSrcElement) tgt.scratchObject);
            return 0;
        } else {
            int targetOffset = tgt.getmcOffset() - (mi << LG_INSTRUCTION_WIDTH);
            if (targetOffset < (MIN_DISPL << LG_INSTRUCTION_WIDTH)) {
                throw new OptimizingCompilerException("CodeGen", " Branch negative offset too large: ", targetOffset);
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
   * @param instr  An integer to be interpreted as a PowerPC instruction
   * @param offset the mcoffset (in bytes) of the instruction
   */
    private String disasm(int instr, int offset) {
        return VM_Disassembler.disasm(instr, offset);
    }

    /** Apply a patch.
   * The instruction at patchOffset should be a NOP instruction.
   * It is replaced by a "B rel32" instruction.
   *
   * @param code        the code intructions to be patched
   * @param patchOffset the offset of the last byte of the patch point
   * @param rel32       the new immediate to use in the branch instruction
   */
    public static void patchCode(VM_CodeArray code, int patchOffset, int rel32) {
        if (DEBUG_CODE_PATCH) {
            VM.sysWrite("patching at ", patchOffset);
            VM.sysWrite(" inst ");
            VM.sysWriteHex(code.get(patchOffset));
            VM.sysWriteln(" offset ", rel32);
        }
        if (VM.VerifyAssertions) {
            VM._assert(code.get(patchOffset) == NOPtemplate);
            VM._assert(rel32 <= (MAX_DISPL << LG_INSTRUCTION_WIDTH));
            VM._assert(rel32 >= (MIN_DISPL << LG_INSTRUCTION_WIDTH));
        }
        code.set(patchOffset, (18 << 26) | (rel32 & LI_MASK));
    }
}
