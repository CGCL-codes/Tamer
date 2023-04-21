package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import net.sourceforge.htmlunit.corejs.javascript.*;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;
import java.util.Map;

/**
 * This class performs node transforms to prepare for optimization.
 *
 * @see NodeTransformer
 * @author Norris Boyd
 */
class OptTransformer extends NodeTransformer {

    OptTransformer(Map<String, OptFunctionNode> possibleDirectCalls, ObjArray directCallTargets) {
        this.possibleDirectCalls = possibleDirectCalls;
        this.directCallTargets = directCallTargets;
    }

    @Override
    protected void visitNew(Node node, ScriptNode tree) {
        detectDirectCall(node, tree);
        super.visitNew(node, tree);
    }

    @Override
    protected void visitCall(Node node, ScriptNode tree) {
        detectDirectCall(node, tree);
        super.visitCall(node, tree);
    }

    private void detectDirectCall(Node node, ScriptNode tree) {
        if (tree.getType() == Token.FUNCTION) {
            Node left = node.getFirstChild();
            int argCount = 0;
            Node arg = left.getNext();
            while (arg != null) {
                arg = arg.getNext();
                argCount++;
            }
            if (argCount == 0) {
                OptFunctionNode.get(tree).itsContainsCalls0 = true;
            }
            if (possibleDirectCalls != null) {
                String targetName = null;
                if (left.getType() == Token.NAME) {
                    targetName = left.getString();
                } else if (left.getType() == Token.GETPROP) {
                    targetName = left.getFirstChild().getNext().getString();
                } else if (left.getType() == Token.GETPROPNOWARN) {
                    throw Kit.codeBug();
                }
                if (targetName != null) {
                    OptFunctionNode ofn;
                    ofn = possibleDirectCalls.get(targetName);
                    if (ofn != null && argCount == ofn.fnode.getParamCount() && !ofn.fnode.requiresActivation()) {
                        if (argCount <= 32) {
                            node.putProp(Node.DIRECTCALL_PROP, ofn);
                            if (!ofn.isTargetOfDirectCall()) {
                                int index = directCallTargets.size();
                                directCallTargets.add(ofn);
                                ofn.setDirectTargetIndex(index);
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<String, OptFunctionNode> possibleDirectCalls;

    private ObjArray directCallTargets;
}
