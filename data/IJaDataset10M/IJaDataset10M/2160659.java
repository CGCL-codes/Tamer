package org.bpmn.bpel.exporter.phase3;

/**
 * @author jassuncao
 *
 */
public interface InstructionVisitor2 {

    public void visit(SimpleInstruction instruction);

    public void visit(AssignInstruction instruction);

    public void visit(EmptyInstruction instruction);

    public void visitBegin(InstructionSequence sequence);

    public void visitEnd(InstructionSequence sequence);

    public void visitBegin(SplitMergeInstruction instruction);

    public void visitEnd(SplitMergeInstruction instruction);

    public void beginVisit(Branch case1);

    public void endVisit(Branch case1);

    public void beginRepeat(LoopInstruction instruction);

    public void beginWhile(LoopInstruction instruction);

    public void endWhile(LoopInstruction instruction);

    public void endRepeat(LoopInstruction instruction);
}
