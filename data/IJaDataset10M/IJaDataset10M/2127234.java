package com.google.gwt.dev.jjs.impl.gflow.copy;

import com.google.gwt.dev.jjs.ast.JLocal;
import com.google.gwt.dev.jjs.ast.JParameter;
import com.google.gwt.dev.jjs.ast.JVariable;
import com.google.gwt.dev.jjs.ast.JVariableRef;
import com.google.gwt.dev.jjs.impl.gflow.AssumptionMap;
import com.google.gwt.dev.jjs.impl.gflow.AssumptionUtil;
import com.google.gwt.dev.jjs.impl.gflow.FlowFunction;
import com.google.gwt.dev.jjs.impl.gflow.cfg.Cfg;
import com.google.gwt.dev.jjs.impl.gflow.cfg.CfgEdge;
import com.google.gwt.dev.jjs.impl.gflow.cfg.CfgNode;
import com.google.gwt.dev.jjs.impl.gflow.cfg.CfgReadWriteNode;
import com.google.gwt.dev.jjs.impl.gflow.cfg.CfgVisitor;
import com.google.gwt.dev.jjs.impl.gflow.cfg.CfgWriteNode;
import com.google.gwt.dev.jjs.impl.gflow.copy.CopyAssumption.Updater;

/**  
 * Flow function for CopyAnalysis.
 */
public class CopyFlowFunction implements FlowFunction<CfgNode<?>, CfgEdge, Cfg, CopyAssumption> {

    public void interpret(CfgNode<?> node, Cfg g, AssumptionMap<CfgEdge, CopyAssumption> assumptionMap) {
        CopyAssumption in = AssumptionUtil.join(g.getInEdges(node), assumptionMap);
        final Updater result = new Updater(in);
        node.accept(new CfgVisitor() {

            @Override
            public void visitReadWriteNode(CfgReadWriteNode node) {
                JVariable targetVariable = node.getTargetVariable();
                if (isSupportedVar(targetVariable)) {
                    result.kill(targetVariable);
                }
            }

            @Override
            public void visitWriteNode(CfgWriteNode node) {
                JVariable targetVariable = node.getTargetVariable();
                if (!isSupportedVar(targetVariable)) {
                    return;
                }
                if (!(node.getValue() instanceof JVariableRef)) {
                    result.kill(targetVariable);
                    return;
                }
                JVariable original = ((JVariableRef) node.getValue()).getTarget();
                original = result.getMostOriginal(original);
                if (original != targetVariable) {
                    result.kill(targetVariable);
                    if (isSupportedVar(original) && original.getType() == targetVariable.getType()) {
                        result.addCopy(original, targetVariable);
                    }
                } else {
                }
            }

            private boolean isSupportedVar(JVariable targetVariable) {
                return targetVariable instanceof JParameter || targetVariable instanceof JLocal;
            }
        });
        AssumptionUtil.setAssumptions(g.getOutEdges(node), result.unwrap(), assumptionMap);
    }
}
