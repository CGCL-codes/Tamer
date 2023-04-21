package com.ibm.wala.demandpa.flowgraph;

/**
 * An edge label in a flow graph
 * 
 * @author manu
 * 
 */
public interface IFlowLabel {

    public void visit(IFlowLabelVisitor v, Object dst);

    /**
   * 
   * @return the bar (inverse) edge corresponding to this edge
   */
    public IFlowLabel bar();

    public interface IFlowLabelVisitor {

        void visitAssignGlobal(AssignGlobalLabel label, Object dst);

        void visitAssign(AssignLabel label, Object dst);

        void visitGetField(GetFieldLabel label, Object dst);

        void visitMatch(MatchLabel label, Object dst);

        void visitNew(NewLabel label, Object dst);

        void visitPutField(PutFieldLabel label, Object dst);

        void visitParam(ParamLabel label, Object dst);

        void visitReturn(ReturnLabel label, Object dst);

        void visitAssignGlobalBar(AssignGlobalBarLabel label, Object dst);

        void visitAssignBar(AssignBarLabel label, Object dst);

        void visitGetFieldBar(GetFieldBarLabel label, Object dst);

        void visitMatchBar(MatchBarLabel label, Object dst);

        void visitNewBar(NewBarLabel label, Object dst);

        void visitPutFieldBar(PutFieldBarLabel label, Object dst);

        void visitReturnBar(ReturnBarLabel label, Object dst);

        void visitParamBar(ParamBarLabel label, Object dst);
    }

    /**
   * 
   * @return true if this is a "barred" edge
   */
    public boolean isBarred();
}
