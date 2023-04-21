package com.mockturtlesolutions.snifflib.pde;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.datatypes.DblParamSet;

/**
Wrapper class for general boundary value problems.
*/
public class BVP implements BoundaryValueProblem {

    private AbstractBoundaryValueProblem realbvp;

    public BVP(AbstractBoundaryValueProblem bvp) {
        this.realbvp = bvp;
    }

    public BVP(NDGrid grid, BoundaryCondition BC) {
        this.realbvp = new GaussSeidel(grid, BC);
    }

    public void setBoundaryCondition(BoundaryCondition BC) {
        this.realbvp.setBoundaryCondition(BC);
    }

    public BoundaryCondition getBoundaryCondition() {
        return (this.realbvp.getBoundaryCondition());
    }

    public DblMatrix getSolution() {
        return (this.realbvp.getSolution());
    }

    public void solve() {
        this.realbvp.solve();
    }

    public int getDimension() {
        return (this.realbvp.getDimension());
    }

    public boolean hasParameter(String name) {
        return (this.realbvp.hasParameter(name));
    }

    public DblMatrix getParam(String name) {
        return (this.realbvp.getParam(name));
    }

    public void setParam(String name, DblMatrix value) {
        this.realbvp.setParam(name, value);
    }

    public void replaceParams(DblParamSet X) {
        this.realbvp.replaceParams(X);
    }

    public String[] parameterSet() {
        return (this.realbvp.parameterSet());
    }
}
