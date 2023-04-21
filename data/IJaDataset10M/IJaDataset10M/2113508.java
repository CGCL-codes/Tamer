package org.jikesrvm.ppc;

import org.jikesrvm.ArchitectureSpecific;
import org.jikesrvm.VM;
import org.jikesrvm.classloader.RVMMethod;
import org.jikesrvm.compilers.common.assembler.ppc.Assembler;
import org.jikesrvm.compilers.common.assembler.ppc.AssemblerConstants;
import org.jikesrvm.objectmodel.ObjectModel;
import org.jikesrvm.runtime.Magic;
import org.jikesrvm.runtime.Memory;

/**
 * Generates a custom IMT-conflict resolution stub.
 * We create a binary search tree.
 */
public abstract class InterfaceMethodConflictResolver implements BaselineConstants, AssemblerConstants {

    public static ArchitectureSpecific.CodeArray createStub(int[] sigIds, RVMMethod[] targets) {
        int numEntries = sigIds.length;
        Assembler asm = new ArchitectureSpecific.Assembler(numEntries);
        if (VM.VerifyAssertions) {
            for (int i = 1; i < sigIds.length; i++) {
                VM._assert(sigIds[i - 1] < sigIds[i]);
            }
        }
        int[] bcIndices = new int[numEntries];
        assignBytecodeIndices(0, bcIndices, 0, numEntries - 1);
        insertStubPrologue(asm);
        insertStubCase(asm, sigIds, targets, bcIndices, 0, numEntries - 1);
        ArchitectureSpecific.CodeArray stub = asm.makeMachineCode().getInstructions();
        if (VM.runningVM) Memory.sync(Magic.objectAsAddress(stub), stub.length() << LG_INSTRUCTION_WIDTH);
        return stub;
    }

    private static int assignBytecodeIndices(int bcIndex, int[] bcIndices, int low, int high) {
        int middle = (high + low) / 2;
        bcIndices[middle] = bcIndex++;
        if (low == middle && middle == high) {
            return bcIndex;
        } else {
            if (low < middle) {
                bcIndex = assignBytecodeIndices(bcIndex, bcIndices, low, middle - 1);
            }
            if (middle < high) {
                bcIndex = assignBytecodeIndices(bcIndex, bcIndices, middle + 1, high);
            }
            return bcIndex;
        }
    }

    private static void insertStubPrologue(Assembler asm) {
        ObjectModel.baselineEmitLoadTIB((ArchitectureSpecific.Assembler) asm, S0, T0);
    }

    private static void insertStubCase(Assembler asm, int[] sigIds, RVMMethod[] targets, int[] bcIndices, int low, int high) {
        int middle = (high + low) / 2;
        asm.resolveForwardReferences(bcIndices[middle]);
        if (low == middle && middle == high) {
            RVMMethod target = targets[middle];
            if (target.isStatic()) {
                asm.emitLAddrToc(S0, target.getOffset());
            } else {
                asm.emitLAddrOffset(S0, S0, target.getOffset());
            }
            asm.emitMTCTR(S0);
            asm.emitBCCTR();
        } else {
            asm.emitCMPI(S1, sigIds[middle]);
            if (low < middle) {
                asm.emitShortBC(LT, 0, bcIndices[(low + middle - 1) / 2]);
            }
            if (middle < high) {
                asm.emitShortBC(GT, 0, bcIndices[(middle + 1 + high) / 2]);
            }
            RVMMethod target = targets[middle];
            if (target.isStatic()) {
                asm.emitLAddrToc(S0, target.getOffset());
            } else {
                asm.emitLAddrOffset(S0, S0, target.getOffset());
            }
            asm.emitMTCTR(S0);
            asm.emitBCCTR();
            if (low < middle) {
                insertStubCase(asm, sigIds, targets, bcIndices, low, middle - 1);
            }
            if (middle < high) {
                insertStubCase(asm, sigIds, targets, bcIndices, middle + 1, high);
            }
        }
    }
}
