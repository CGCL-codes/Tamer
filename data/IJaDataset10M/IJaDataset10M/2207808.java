package com.ibm.wala.demandpa.flowgraph;

import java.util.List;
import java.util.Set;
import com.ibm.wala.classLoader.ArrayClass;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IField;
import com.ibm.wala.classLoader.ProgramCounter;
import com.ibm.wala.demandpa.util.ArrayContents;
import com.ibm.wala.demandpa.util.MemoryAccessMap;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.propagation.FilteredPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.HeapModel;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.callgraph.propagation.SSAPropagationCallGraphBuilder;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSAArrayLengthInstruction;
import com.ibm.wala.ssa.SSAArrayLoadInstruction;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSABinaryOpInstruction;
import com.ibm.wala.ssa.SSACheckCastInstruction;
import com.ibm.wala.ssa.SSAComparisonInstruction;
import com.ibm.wala.ssa.SSAConversionInstruction;
import com.ibm.wala.ssa.SSAGetCaughtExceptionInstruction;
import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.ssa.SSAInstanceofInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSALoadMetadataInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.ssa.SSAPiInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;
import com.ibm.wala.ssa.SSAThrowInstruction;
import com.ibm.wala.ssa.SSAUnaryOpInstruction;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.collections.MapUtil;
import com.ibm.wala.util.debug.Assertions;

/**
 * A flow graph including both pointer and primitive values.
 * 
 * TODO share more code with {@link DemandPointerFlowGraph}
 * 
 * @author Manu Sridharan
 * 
 */
public class DemandValueFlowGraph extends AbstractDemandFlowGraph {

    public DemandValueFlowGraph(CallGraph cg, HeapModel heapModel, MemoryAccessMap mam, ClassHierarchy cha) {
        super(cg, heapModel, mam, cha);
    }

    @Override
    protected void addNodesForParameters(CGNode node, IR ir) {
        SymbolTable symbolTable = ir.getSymbolTable();
        int numParams = symbolTable.getNumberOfParameters();
        for (int i = 0; i < numParams; i++) {
            int parameter = symbolTable.getParameter(i);
            PointerKey paramPk = heapModel.getPointerKeyForLocal(node, parameter);
            addNode(paramPk);
            params.put(paramPk, node);
        }
        PointerKey returnKey = heapModel.getPointerKeyForReturnValue(node);
        addNode(returnKey);
        returns.put(returnKey, node);
        PointerKey exceptionReturnKey = heapModel.getPointerKeyForExceptionalReturnValue(node);
        addNode(exceptionReturnKey);
        returns.put(exceptionReturnKey, node);
    }

    @Override
    protected FlowStatementVisitor makeVisitor(CGNode node) {
        return new AllValsStatementVisitor(node);
    }

    private class AllValsStatementVisitor extends Visitor implements FlowStatementVisitor {

        /**
     * The node whose statements we are currently traversing
     */
        protected final CGNode node;

        /**
     * The governing IR
     */
        protected final IR ir;

        /**
     * The basic block currently being processed
     */
        private ISSABasicBlock basicBlock;

        /**
     * Governing symbol table
     */
        protected final SymbolTable symbolTable;

        /**
     * Def-use information
     */
        protected final DefUse du;

        public AllValsStatementVisitor(CGNode node) {
            this.node = node;
            this.ir = node.getIR();
            this.symbolTable = ir.getSymbolTable();
            assert symbolTable != null;
            this.du = node.getDU();
        }

        @Override
        public void visitArrayLoad(SSAArrayLoadInstruction instruction) {
            PointerKey result = heapModel.getPointerKeyForLocal(node, instruction.getDef());
            PointerKey arrayRef = heapModel.getPointerKeyForLocal(node, instruction.getArrayRef());
            addNode(result);
            addNode(arrayRef);
            addEdge(result, arrayRef, GetFieldLabel.make(ArrayContents.v()));
        }

        @Override
        public void visitArrayStore(SSAArrayStoreInstruction instruction) {
            PointerKey value = heapModel.getPointerKeyForLocal(node, instruction.getValue());
            PointerKey arrayRef = heapModel.getPointerKeyForLocal(node, instruction.getArrayRef());
            addNode(value);
            addNode(arrayRef);
            addEdge(arrayRef, value, PutFieldLabel.make(ArrayContents.v()));
        }

        @Override
        public void visitCheckCast(SSACheckCastInstruction instruction) {
            Set<IClass> types = HashSetFactory.make();
            for (TypeReference t : instruction.getDeclaredResultTypes()) {
                IClass cls = cha.lookupClass(t);
                if (cls == null) {
                    return;
                } else {
                    types.add(cls);
                }
            }
            PointerKey result = heapModel.getFilteredPointerKeyForLocal(node, instruction.getResult(), new FilteredPointerKey.MultipleClassesFilter(types.toArray(new IClass[types.size()])));
            PointerKey value = heapModel.getPointerKeyForLocal(node, instruction.getVal());
            addNode(result);
            addNode(value);
            addEdge(result, value, AssignLabel.noFilter());
        }

        @Override
        public void visitReturn(SSAReturnInstruction instruction) {
            if (instruction.returnsVoid()) {
                return;
            } else {
                PointerKey def = heapModel.getPointerKeyForLocal(node, instruction.getResult());
                addNode(def);
                PointerKey returnValue = heapModel.getPointerKeyForReturnValue(node);
                addNode(returnValue);
                addEdge(returnValue, def, AssignLabel.noFilter());
            }
        }

        @Override
        public void visitGet(SSAGetInstruction instruction) {
            visitGetInternal(instruction.getDef(), instruction.getRef(), instruction.isStatic(), instruction.getDeclaredField());
        }

        protected void visitGetInternal(int lval, int ref, boolean isStatic, FieldReference field) {
            IField f = cg.getClassHierarchy().resolveField(field);
            if (f == null) {
                return;
            }
            PointerKey def = heapModel.getPointerKeyForLocal(node, lval);
            assert def != null;
            if (isStatic) {
                PointerKey fKey = heapModel.getPointerKeyForStaticField(f);
                addNode(def);
                addNode(fKey);
                addEdge(def, fKey, AssignGlobalLabel.v());
            } else {
                PointerKey refKey = heapModel.getPointerKeyForLocal(node, ref);
                addNode(def);
                addNode(refKey);
                addEdge(def, refKey, GetFieldLabel.make(f));
            }
        }

        @Override
        public void visitPut(SSAPutInstruction instruction) {
            visitPutInternal(instruction.getVal(), instruction.getRef(), instruction.isStatic(), instruction.getDeclaredField());
        }

        public void visitPutInternal(int rval, int ref, boolean isStatic, FieldReference field) {
            IField f = cg.getClassHierarchy().resolveField(field);
            if (f == null) {
                return;
            }
            PointerKey use = heapModel.getPointerKeyForLocal(node, rval);
            assert use != null;
            if (isStatic) {
                PointerKey fKey = heapModel.getPointerKeyForStaticField(f);
                addNode(use);
                addNode(fKey);
                addEdge(fKey, use, AssignGlobalLabel.v());
            } else {
                PointerKey refKey = heapModel.getPointerKeyForLocal(node, ref);
                addNode(use);
                addNode(refKey);
                addEdge(refKey, use, PutFieldLabel.make(f));
            }
        }

        @Override
        public void visitInvoke(SSAInvokeInstruction instruction) {
            for (int i = 0; i < instruction.getNumberOfUses(); i++) {
                PointerKey use = heapModel.getPointerKeyForLocal(node, instruction.getUse(i));
                addNode(use);
                Set<SSAInvokeInstruction> s = MapUtil.findOrCreateSet(callParams, use);
                s.add(instruction);
            }
            if (instruction.hasDef()) {
                PointerKey def = heapModel.getPointerKeyForLocal(node, instruction.getDef());
                addNode(def);
                callDefs.put(def, instruction);
            }
            PointerKey exc = heapModel.getPointerKeyForLocal(node, instruction.getException());
            addNode(exc);
            callDefs.put(exc, instruction);
        }

        @Override
        public void visitNew(SSANewInstruction instruction) {
            InstanceKey iKey = heapModel.getInstanceKeyForAllocation(node, instruction.getNewSite());
            if (iKey == null) {
                return;
            }
            PointerKey def = heapModel.getPointerKeyForLocal(node, instruction.getDef());
            addNode(iKey);
            addNode(def);
            addEdge(def, iKey, NewLabel.v());
            IClass klass = iKey.getConcreteType();
            int dim = 0;
            InstanceKey lastInstance = iKey;
            PointerKey lastVar = def;
            while (klass != null && klass.isArrayClass()) {
                klass = ((ArrayClass) klass).getElementClass();
                if (klass != null && klass.isArrayClass()) {
                    InstanceKey ik = heapModel.getInstanceKeyForMultiNewArray(node, instruction.getNewSite(), dim);
                    PointerKey pk = heapModel.getPointerKeyForArrayContents(lastInstance);
                    addNode(ik);
                    addNode(pk);
                    addEdge(pk, ik, NewLabel.v());
                    addEdge(lastVar, pk, PutFieldLabel.make(ArrayContents.v()));
                    lastInstance = ik;
                    lastVar = pk;
                    dim++;
                }
            }
        }

        @Override
        public void visitThrow(SSAThrowInstruction instruction) {
        }

        @Override
        public void visitGetCaughtException(SSAGetCaughtExceptionInstruction instruction) {
            List<ProgramCounter> peis = SSAPropagationCallGraphBuilder.getIncomingPEIs(ir, getBasicBlock());
            PointerKey def = heapModel.getPointerKeyForLocal(node, instruction.getDef());
            Set<IClass> types = SSAPropagationCallGraphBuilder.getCaughtExceptionTypes(instruction, ir);
            addExceptionDefConstraints(ir, node, peis, def, types);
        }

        @Override
        public void visitPi(SSAPiInstruction instruction) {
            PointerKey src = heapModel.getPointerKeyForLocal(node, instruction.getDef());
            PointerKey dst = heapModel.getPointerKeyForLocal(node, instruction.getVal());
            addNode(src);
            addNode(dst);
            addEdge(src, dst, AssignLabel.noFilter());
        }

        private void handleNonHeapInstruction(SSAInstruction instruction) {
            for (int i = 0; i < instruction.getNumberOfDefs(); i++) {
                int def = instruction.getDef(i);
                PointerKey defPk = heapModel.getPointerKeyForLocal(node, def);
                addNode(defPk);
                for (int j = 0; j < instruction.getNumberOfUses(); j++) {
                    int use = instruction.getUse(j);
                    PointerKey usePk = heapModel.getPointerKeyForLocal(node, use);
                    addNode(usePk);
                    addEdge(defPk, usePk, AssignLabel.noFilter());
                }
            }
        }

        @Override
        public void visitArrayLength(SSAArrayLengthInstruction instruction) {
            handleNonHeapInstruction(instruction);
        }

        @Override
        public void visitBinaryOp(SSABinaryOpInstruction instruction) {
            handleNonHeapInstruction(instruction);
        }

        @Override
        public void visitComparison(SSAComparisonInstruction instruction) {
            handleNonHeapInstruction(instruction);
        }

        @Override
        public void visitConversion(SSAConversionInstruction instruction) {
            handleNonHeapInstruction(instruction);
        }

        @Override
        public void visitInstanceof(SSAInstanceofInstruction instruction) {
            handleNonHeapInstruction(instruction);
        }

        @Override
        public void visitUnaryOp(SSAUnaryOpInstruction instruction) {
            handleNonHeapInstruction(instruction);
        }

        public ISSABasicBlock getBasicBlock() {
            return basicBlock;
        }

        /**
     * The calling loop must call this in each iteration!
     */
        public void setBasicBlock(ISSABasicBlock block) {
            basicBlock = block;
        }

        @Override
        public void visitLoadMetadata(SSALoadMetadataInstruction instruction) {
            Assertions.UNREACHABLE();
        }
    }
}
