package com.ibm.wala.eclipse.cg.model;

import java.util.Collection;
import com.ibm.wala.emf.wrappers.EMFScopeWrapper;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.Entrypoints;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.util.graph.InferGraphRootsImpl;
import com.ibm.wala.util.warnings.WalaException;
import com.ibm.wala.util.warnings.WarningSet;

/**
 * 
 * @author aying
 */
public class WalaCGModelWithMain extends WalaCGModel {

    public WalaCGModelWithMain(String appJar) {
        super(appJar);
    }

    /**
	 * @see SWTCallGraph
	 */
    @Override
    protected CallGraph createCallGraph(EMFScopeWrapper scope) throws WalaException {
        WarningSet warnings = new WarningSet();
        ClassHierarchy cha = ClassHierarchy.make(scope, warnings);
        Entrypoints entrypoints = com.ibm.wala.ipa.callgraph.impl.Util.makeMainEntrypoints(scope, cha);
        AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
        com.ibm.wala.ipa.callgraph.CallGraphBuilder builder = Util.makeZeroCFABuilder(options, cha, scope, warnings, null, null);
        CallGraph cg = builder.makeCallGraph(options);
        return cg;
    }

    /**
	 * @see SWTCallGraph
	 */
    @Override
    protected Collection inferRoots(CallGraph cg) throws WalaException {
        return InferGraphRootsImpl.inferRoots(cg);
    }
}
