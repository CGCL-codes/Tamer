package eu.actorsproject.xlim.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eu.actorsproject.xlim.XlimContainerModule;
import eu.actorsproject.xlim.XlimIfModule;
import eu.actorsproject.xlim.XlimLoopModule;
import eu.actorsproject.xlim.XlimModule;
import eu.actorsproject.xlim.XlimOperation;
import eu.actorsproject.xlim.XlimPhiNode;
import eu.actorsproject.xlim.XlimTaskModule;

/**
 * Moves code to the location given by the code-motion-plugin
 * (see LatestEvaluationAnalysis for an implementation)
 */
public class CodeMotion {

    protected boolean mTrace = false;

    public void codeMotion(XlimTaskModule task, CodeMotionPlugIn plugIn) {
        CodeMotionTraversal taskTraversal = new CodeMotionTraversal();
        taskTraversal.traverse(task, plugIn);
    }

    class CodeMotionTraversal extends XlimTraversal<Object, CodeMotionPlugIn> {

        Map<XlimModule, List<XlimOperation>> mInsert = new HashMap<XlimModule, List<XlimOperation>>();

        @Override
        protected Object handleOperation(XlimOperation op, CodeMotionPlugIn arg) {
            XlimModule toModule = arg.insertionPoint(op);
            if (toModule != op.getParentModule()) {
                List<XlimOperation> operations = mInsert.get(toModule);
                if (operations == null) {
                    operations = new ArrayList<XlimOperation>();
                    mInsert.put(toModule, operations);
                }
                operations.add(op);
            }
            return null;
        }

        @Override
        protected Object handlePhiNode(XlimPhiNode phi, CodeMotionPlugIn arg) {
            return null;
        }

        protected void move(List<XlimOperation> operations, XlimContainerModule m) {
            for (XlimOperation op : operations) {
                if (mTrace) {
                    System.out.println("// CodeMotion: " + op.toString() + " moved from " + op.getParentModule() + " to " + m);
                }
                m.cutAndPaste(op);
            }
            m.completePatchAndFixup();
        }

        @Override
        protected Object traverseContainerModule(XlimContainerModule m, CodeMotionPlugIn arg) {
            List<XlimOperation> operations = mInsert.get(m);
            if (operations != null) {
                m.startPatchAtBeginning();
                move(operations, m);
            }
            return super.traverseContainerModule(m, arg);
        }

        @Override
        protected Object traverseIfModule(XlimIfModule m, CodeMotionPlugIn arg) {
            List<XlimOperation> operations = mInsert.get(m);
            if (operations != null) {
                XlimContainerModule container = m.getParentModule();
                container.startPatchBefore(m);
                move(operations, container);
            }
            return super.traverseIfModule(m, arg);
        }

        @Override
        protected Object traverseLoopModule(XlimLoopModule m, CodeMotionPlugIn arg) {
            List<XlimOperation> operations = mInsert.get(m);
            if (operations != null) {
                XlimContainerModule container = m.getParentModule();
                container.startPatchBefore(m);
                move(operations, container);
            }
            return super.traverseLoopModule(m, arg);
        }
    }
}
