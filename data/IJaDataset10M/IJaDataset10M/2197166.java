package org.jikesrvm.compilers.opt.driver;

import org.jikesrvm.classloader.NormalMethod;
import org.jikesrvm.classloader.TypeReference;
import org.jikesrvm.compilers.opt.OptOptions;
import org.jikesrvm.compilers.opt.inlining.DefaultInlineOracle;
import org.jikesrvm.compilers.opt.inlining.InlineOracle;
import org.jikesrvm.compilers.opt.ir.IR;

public final class CompilationPlan {

    /**
   * The method to be compiled.
   */
    public final NormalMethod method;

    public NormalMethod getMethod() {
        return method;
    }

    /**
   * The specialized parameters to use in place of those defined in method.
   */
    public final TypeReference[] params;

    /**
   * The OptimizationPlanElements to be invoked during compilation.
   */
    public final OptimizationPlanElement[] optimizationPlan;

    /**
   * The instrumentation plan for the method.
   */
    public final InstrumentationPlan instrumentationPlan;

    /**
   * The oracle to be consulted for all inlining decisions.
   */
    public InlineOracle inlinePlan;

    /**
   * The Options object that contains misc compilation control data
   */
    public final OptOptions options;

    /**
   * Whether this compilation is for analysis only?
   */
    public boolean analyzeOnly;

    public boolean irGeneration;

    /**
   * Construct a compilation plan
   *
   * @param m    The NormalMethod representing the source method to be compiled
   * @param pms  The specialized parameters to use in place of those defined in method
   * @param op   The optimization plan to be executed on m
   * @param mp   The instrumentation plan to be executed on m
   * @param opts The Options to be used for compiling m
   */
    public CompilationPlan(NormalMethod m, TypeReference[] pms, OptimizationPlanElement[] op, InstrumentationPlan mp, OptOptions opts) {
        method = m;
        params = pms;
        inlinePlan = new DefaultInlineOracle();
        optimizationPlan = op;
        instrumentationPlan = mp;
        options = opts;
    }

    /**
   * Construct a compilation plan
   *
   * @param m    The NormalMethod representing the source method to be compiled
   * @param op   The optimization plan to be executed on m
   * @param mp   The instrumentation plan to be executed on m
   * @param opts The Options to be used for compiling m
   */
    public CompilationPlan(NormalMethod m, OptimizationPlanElement[] op, InstrumentationPlan mp, OptOptions opts) {
        this(m, null, op, mp, opts);
    }

    /**
   * Construct a compilation plan
   * @param m    The NormalMethod representing the source method to be compiled
   * @param op   A single optimization pass to execute on m
   * @param mp   The instrumentation plan to be executed on m
   * @param opts The Options to be used for compiling m
   */
    public CompilationPlan(NormalMethod m, OptimizationPlanElement op, InstrumentationPlan mp, OptOptions opts) {
        this(m, new OptimizationPlanElement[] { op }, mp, opts);
    }

    /**
   * Set the inline oracle
   */
    public void setInlineOracle(InlineOracle o) {
        inlinePlan = o;
    }

    /**
   * Execute a compilation plan by executing each element
   * in the optimization plan.
   */
    public IR execute() {
        IR ir = new IR(method, this);
        if (instrumentationPlan != null) {
            instrumentationPlan.initInstrumentation(method);
        }
        for (OptimizationPlanElement element : optimizationPlan) {
            element.perform(ir);
        }
        if (instrumentationPlan != null) {
            instrumentationPlan.finalizeInstrumentation(method);
        }
        return ir;
    }
}
