package com.ibm.wala.cast.ipa.callgraph;

import java.util.Iterator;
import com.ibm.wala.cast.loader.AstMethod;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.CodeScanner;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ContextInsensitiveSSAInterpreter;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.util.collections.EmptyIterator;

/**
 * A version of {@link ContextInsensitiveSSAInterpreter} that uses the IR for
 * {@link #iterateNewSites(CGNode)} and {@link #iterateCallSites(CGNode)} when
 * we have an {@link AstMethod}. ({@link ContextInsensitiveSSAInterpreter}
 * defaults to using {@link CodeScanner}, which only works for bytecodes.)
 */
public class AstContextInsensitiveSSAContextInterpreter extends ContextInsensitiveSSAInterpreter {

    public AstContextInsensitiveSSAContextInterpreter(AnalysisOptions options, AnalysisCache cache) {
        super(options, cache);
    }

    public boolean understands(IMethod method, Context context) {
        return method instanceof AstMethod;
    }

    public Iterator<NewSiteReference> iterateNewSites(CGNode N) {
        IR ir = getIR(N);
        if (ir == null) {
            return EmptyIterator.instance();
        } else {
            return ir.iterateNewSites();
        }
    }

    public Iterator<CallSiteReference> iterateCallSites(CGNode N) {
        IR ir = getIR(N);
        if (ir == null) {
            return EmptyIterator.instance();
        } else {
            return ir.iterateCallSites();
        }
    }
}
