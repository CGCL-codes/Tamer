package com.ibm.wala.refactoring.buckets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.ibm.wala.cfg.ControlFlowGraph;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.analysis.ExplodedControlFlowGraph;
import com.ibm.wala.ssa.analysis.ExplodedControlFlowGraph.ExplodedBasicBlock;
import com.ibm.wala.ssa.analysis.IExplodedBasicBlock;
import com.ibm.wala.util.intset.BitVector;
import com.ibm.wala.util.intset.IntSet;

public class InvertedControlFlowGraph implements ControlFlowGraph<SSAInstruction, IExplodedBasicBlock> {

    ExplodedControlFlowGraph originalControlFlowGraph;

    InvertedControlFlowGraph(ExplodedControlFlowGraph cfg) {
        this.originalControlFlowGraph = cfg;
    }

    @Override
    public IExplodedBasicBlock entry() {
        return originalControlFlowGraph.exit();
    }

    @Override
    public IExplodedBasicBlock exit() {
        return originalControlFlowGraph.entry();
    }

    @Override
    public IExplodedBasicBlock getBlockForInstruction(int index) {
        return originalControlFlowGraph.getBlockForInstruction(index);
    }

    @Override
    public BitVector getCatchBlocks() {
        return originalControlFlowGraph.getCatchBlocks();
    }

    @Override
    public Collection<IExplodedBasicBlock> getExceptionalPredecessors(IExplodedBasicBlock b) {
        return originalControlFlowGraph.getExceptionalSuccessors(b);
    }

    @Override
    public List<IExplodedBasicBlock> getExceptionalSuccessors(IExplodedBasicBlock b) {
        return new ArrayList<IExplodedBasicBlock>(originalControlFlowGraph.getExceptionalPredecessors(b));
    }

    @Override
    public SSAInstruction[] getInstructions() {
        return originalControlFlowGraph.getInstructions();
    }

    @Override
    public IMethod getMethod() {
        return originalControlFlowGraph.getMethod();
    }

    @Override
    public Collection<IExplodedBasicBlock> getNormalPredecessors(IExplodedBasicBlock b) {
        return originalControlFlowGraph.getNormalSuccessors(b);
    }

    @Override
    public Collection<IExplodedBasicBlock> getNormalSuccessors(IExplodedBasicBlock b) {
        return originalControlFlowGraph.getNormalPredecessors(b);
    }

    @Override
    public int getProgramCounter(int index) {
        return originalControlFlowGraph.getProgramCounter(index);
    }

    @Override
    public void removeNodeAndEdges(IExplodedBasicBlock N) throws UnsupportedOperationException {
    }

    @Override
    public void addNode(IExplodedBasicBlock n) {
    }

    @Override
    public boolean containsNode(IExplodedBasicBlock N) {
        return originalControlFlowGraph.containsNode(N);
    }

    @Override
    public int getNumberOfNodes() {
        return originalControlFlowGraph.getNumberOfNodes();
    }

    @Override
    public Iterator<IExplodedBasicBlock> iterator() {
        return originalControlFlowGraph.iterator();
    }

    @Override
    public void removeNode(IExplodedBasicBlock n) {
    }

    @Override
    public void addEdge(IExplodedBasicBlock src, IExplodedBasicBlock dst) {
    }

    @Override
    public int getPredNodeCount(IExplodedBasicBlock bb) {
        ExplodedBasicBlock b = (ExplodedBasicBlock) bb;
        if (b == null) {
            throw new IllegalArgumentException("b == null");
        }
        if (b.isExitBlock()) {
            return 0;
        }
        if (b.isEntryBlock()) {
            return originalControlFlowGraph.ir.getControlFlowGraph().getSuccNodeCount(originalControlFlowGraph.ir.getControlFlowGraph().entry());
        }
        if (b.instructionIndex == b.original.getLastInstructionIndex()) {
            if (b.original.isExitBlock()) {
                return 1;
            } else {
                return originalControlFlowGraph.ir.getControlFlowGraph().getSuccNodeCount(b.original);
            }
        } else {
            return 1;
        }
    }

    @Override
    public Iterator<IExplodedBasicBlock> getPredNodes(IExplodedBasicBlock N) {
        return originalControlFlowGraph.getSuccNodes(N);
    }

    @Override
    public int getSuccNodeCount(IExplodedBasicBlock N) {
        return originalControlFlowGraph.getPredNodeCount(N);
    }

    @Override
    public Iterator<IExplodedBasicBlock> getSuccNodes(IExplodedBasicBlock N) {
        return originalControlFlowGraph.getPredNodes(N);
    }

    @Override
    public boolean hasEdge(IExplodedBasicBlock src, IExplodedBasicBlock dst) {
        return originalControlFlowGraph.hasEdge(src, dst);
    }

    @Override
    public void removeAllIncidentEdges(IExplodedBasicBlock node) throws UnsupportedOperationException {
    }

    @Override
    public void removeEdge(IExplodedBasicBlock src, IExplodedBasicBlock dst) throws UnsupportedOperationException {
    }

    @Override
    public void removeIncomingEdges(IExplodedBasicBlock node) throws UnsupportedOperationException {
    }

    @Override
    public void removeOutgoingEdges(IExplodedBasicBlock node) throws UnsupportedOperationException {
    }

    @Override
    public int getMaxNumber() {
        return originalControlFlowGraph.getMaxNumber();
    }

    @Override
    public IExplodedBasicBlock getNode(int number) {
        return originalControlFlowGraph.getNode(number);
    }

    @Override
    public int getNumber(IExplodedBasicBlock N) {
        return originalControlFlowGraph.getNumber(N);
    }

    @Override
    public Iterator<IExplodedBasicBlock> iterateNodes(IntSet s) {
        return originalControlFlowGraph.iterateNodes(s);
    }

    @Override
    public IntSet getPredNodeNumbers(IExplodedBasicBlock node) {
        return originalControlFlowGraph.getSuccNodeNumbers(node);
    }

    @Override
    public IntSet getSuccNodeNumbers(IExplodedBasicBlock node) {
        return originalControlFlowGraph.getPredNodeNumbers(node);
    }
}
