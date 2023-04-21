package com.ibm.safe.callgraph;

import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.callgraph.ContextSelector;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.SSAContextInterpreter;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXInstanceKeys;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.IClassHierarchy;

public class CHABasedCFABuilder implements CallGraphBuilder {

    private IClassHierarchy cha;

    private AnalysisCache cache;

    public CHABasedCFABuilder(IClassHierarchy cha, AnalysisOptions options, AnalysisCache cache, ContextSelector appContextSelector, SSAContextInterpreter appContextInterpreter) {
        this.cha = cha;
        this.cache = cache;
    }

    protected ContextSelector makeContainerContextSelector(ClassHierarchy cha, ZeroXInstanceKeys keys) {
        return new CustomContextSelector(cha, keys);
    }

    public CallGraph makeCallGraph(AnalysisOptions options) throws IllegalArgumentException, CallGraphBuilderCancelException {
        return makeCallGraph(options, null);
    }

    public AnalysisCache getAnalysisCache() {
        return cache;
    }

    public PointerAnalysis getPointerAnalysis() {
        return null;
    }

    @Override
    public CallGraph makeCallGraph(AnalysisOptions options, com.ibm.wala.util.MonitorUtil.IProgressMonitor monitor) throws IllegalArgumentException, CallGraphBuilderCancelException {
        return new CHABasedCallGraph(cha, options, getAnalysisCache());
    }
}
