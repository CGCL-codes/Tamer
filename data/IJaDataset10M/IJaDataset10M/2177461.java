package org.phramer.v1.constraints.blockorder.hard.expression;

public class ConstraintObjectOnNode {

    public int[] bcState;

    public int[] ocState;

    public int[] variables;

    public ConstraintObjectOnNode getSuccessor() {
        ConstraintObjectOnNode out = new ConstraintObjectOnNode();
        out.bcState = bcState.clone();
        out.ocState = ocState.clone();
        out.variables = variables.clone();
        return out;
    }
}
