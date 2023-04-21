package com.ibm.wala.ipa.summaries;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.SyntheticMethod;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAOptions;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

/**
 * A {@link SyntheticMethod} representing the semantics encoded in a {@link MethodSummary}
 */
public class SummarizedMethod extends SyntheticMethod {

    static final boolean DEBUG = false;

    private final MethodSummary summary;

    public SummarizedMethod(MethodReference ref, MethodSummary summary, IClass declaringClass) throws NullPointerException {
        super(ref, declaringClass, summary.isStatic(), summary.isFactory());
        this.summary = summary;
        assert declaringClass != null;
        if (DEBUG) {
            System.err.println(("SummarizedMethod ctor: " + ref + " " + summary));
        }
    }

    /**
   * @see com.ibm.wala.classLoader.IMethod#isNative()
   */
    @Override
    public boolean isNative() {
        return summary.isNative();
    }

    /**
   * @see com.ibm.wala.classLoader.IMethod#isAbstract()
   */
    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    public String getPoison() {
        return summary.getPoison();
    }

    @Override
    public byte getPoisonLevel() {
        return summary.getPoisonLevel();
    }

    @Override
    public boolean hasPoison() {
        return summary.hasPoison();
    }

    @Override
    public SSAInstruction[] getStatements(SSAOptions options) {
        if (DEBUG) {
            System.err.println(("getStatements: " + this));
        }
        return summary.getStatements();
    }

    @Override
    public IR makeIR(Context context, SSAOptions options) {
        SSAInstruction instrs[] = getStatements(options);
        return new SyntheticIR(this, Everywhere.EVERYWHERE, makeControlFlowGraph(instrs), instrs, options, summary.getConstants());
    }

    @Override
    public int getNumberOfParameters() {
        return summary.getNumberOfParameters();
    }

    @Override
    public boolean isStatic() {
        return summary.isStatic();
    }

    @Override
    public TypeReference getParameterType(int i) {
        return summary.getParameterType(i);
    }
}
