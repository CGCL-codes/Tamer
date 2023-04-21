package com.ibm.safe.typestate.base;

import java.util.Collection;
import com.ibm.safe.reporting.IReporter;
import com.ibm.safe.typestate.core.BenignOracle;
import com.ibm.safe.typestate.merge.IMergeFunctionFactory;
import com.ibm.safe.typestate.metrics.TypeStateMetrics;
import com.ibm.safe.typestate.mine.TraceReporter;
import com.ibm.safe.typestate.options.TypeStateOptions;
import com.ibm.safe.typestate.rules.ITypeStateDFA;
import com.ibm.safe.typestate.rules.InstanceBatchIterator;
import com.ibm.wala.escape.ILiveObjectAnalysis;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;

/**
 * @author Eran Yahav (yahave)
 * @author sjfink
 */
public class SeparatingSolver extends BaseSolver {

    public SeparatingSolver(CallGraph cg, PointerAnalysis pointerAnalysis, ITypeStateDFA dfa, TypeStateOptions options, ILiveObjectAnalysis live, BenignOracle ora, TypeStateMetrics metrics, IReporter reporter, TraceReporter traceReporter, IMergeFunctionFactory mergeFactory) {
        super(cg, pointerAnalysis, dfa, options, live, ora, metrics, reporter, traceReporter, mergeFactory);
    }

    protected InstanceBatchIterator makeBatchIterator(Collection<InstanceKey> allInstances) {
        return InstanceBatchIterator.makeSeparation(allInstances);
    }
}
