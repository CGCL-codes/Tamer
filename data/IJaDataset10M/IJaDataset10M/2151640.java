package com.ibm.wala.demandpa.alg;

import java.util.Collection;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.propagation.HeapModel;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.cha.IClassHierarchy;

/**
 * Basic interface for a demand-driven points-to analysis.
 * 
 * @author Manu Sridharan
 *
 */
public interface IDemandPointerAnalysis {

    public HeapModel getHeapModel();

    public CallGraph getBaseCallGraph();

    public IClassHierarchy getClassHierarchy();

    public Collection<InstanceKey> getPointsTo(PointerKey pk);
}
