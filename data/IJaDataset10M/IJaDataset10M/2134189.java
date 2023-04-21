package com.ibm.wala.ipa.callgraph.impl;

import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.ContextSelector;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.util.intset.IntSet;

/**
 * A context selector that first checks with A, then defaults to B.
 */
public class DelegatingContextSelector implements ContextSelector {

    private static final boolean DEBUG = false;

    private final ContextSelector A;

    private final ContextSelector B;

    public DelegatingContextSelector(ContextSelector A, ContextSelector B) {
        this.A = A;
        this.B = B;
        if (A == null) {
            throw new IllegalArgumentException("null A");
        }
        if (B == null) {
            throw new IllegalArgumentException("null B");
        }
    }

    public Context getCalleeTarget(CGNode caller, CallSiteReference site, IMethod callee, InstanceKey[] receiver) {
        if (DEBUG) {
            System.err.println(("getCalleeTarget " + caller + " " + site + " " + callee));
        }
        if (A != null) {
            Context C = A.getCalleeTarget(caller, site, callee, receiver);
            if (C != null) {
                if (DEBUG) {
                    System.err.println(("Case A " + A.getClass() + " " + C));
                }
                return C;
            }
        }
        Context C = B.getCalleeTarget(caller, site, callee, receiver);
        if (DEBUG) {
            System.err.println(("Case B " + B.getClass() + " " + C));
        }
        return C;
    }

    public IntSet getRelevantParameters(CGNode caller, CallSiteReference site) {
        return A.getRelevantParameters(caller, site).union(B.getRelevantParameters(caller, site));
    }
}
