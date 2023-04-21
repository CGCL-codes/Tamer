package at.ac.tuwien.infosys.www.pixy.analysis.inter;

import java.util.*;
import at.ac.tuwien.infosys.www.pixy.conversion.TacFunction;
import at.ac.tuwien.infosys.www.pixy.conversion.nodes.CfgNodeCall;

public class CallGraphNode {

    private TacFunction function;

    private Map<CfgNodeCall, CallGraphNode> outEdges;

    private Map<CfgNodeCall, CallGraphNode> inEdges;

    CallGraphNode(TacFunction function) {
        this.function = function;
        this.outEdges = new HashMap<CfgNodeCall, CallGraphNode>();
        this.inEdges = new HashMap<CfgNodeCall, CallGraphNode>();
    }

    public TacFunction getFunction() {
        return this.function;
    }

    Collection<CallGraphNode> getSuccessors() {
        return this.outEdges.values();
    }

    Collection<CallGraphNode> getPredecessors() {
        return this.inEdges.values();
    }

    Set<CfgNodeCall> getCallsTo() {
        return this.inEdges.keySet();
    }

    public boolean equals(Object compX) {
        if (compX == this) {
            return true;
        }
        if (!(compX instanceof CallGraphNode)) {
            return false;
        }
        CallGraphNode comp = (CallGraphNode) compX;
        return this.function.equals(comp.function);
    }

    public int hashCode() {
        return this.function.hashCode();
    }

    public void addCallee(CfgNodeCall callNode, CallGraphNode calleeNode) {
        this.outEdges.put(callNode, calleeNode);
    }

    public void addCaller(CfgNodeCall callNode, CallGraphNode callerNode) {
        this.inEdges.put(callNode, callerNode);
    }
}
