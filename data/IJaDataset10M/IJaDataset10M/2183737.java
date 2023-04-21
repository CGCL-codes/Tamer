package tefkat.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import tefkat.engine.Binding;
import tefkat.engine.Node;
import tefkat.engine.TefkatListenerAdapter;
import tefkat.model.TRule;
import tefkat.model.Term;
import tefkat.model.Transformation;

/**
 * @author lawley
 * 
 */
public class TransformationView extends ViewPart {

    private TreeViewer viewer;

    public void createPartControl(Composite parent) {
        Tree tree = new Tree(parent, SWT.V_SCROLL | SWT.H_SCROLL);
        viewer = new TreeViewer(tree);
        viewer.setLabelProvider(new LabelProvider());
        viewer.setContentProvider(new TransformationViewContentProvider());
        viewer.setInput(new NodeProxy());
    }

    public void setFocus() {
        viewer.getControl().setFocus();
    }

    private static class TransformationViewContentProvider extends TefkatListenerAdapter implements ITreeContentProvider {

        public TransformationViewContentProvider() {
            TefkatPlugin.getPlugin().addTefkatListener(this);
        }

        public Object[] getChildren(Object parentElement) {
            return ((NodeProxy) parentElement).children.toArray();
        }

        public Object getParent(Object element) {
            return ((NodeProxy) element).parent;
        }

        public boolean hasChildren(Object element) {
            return ((NodeProxy) element).children.size() > 0;
        }

        public Object[] getElements(Object inputElement) {
            if (null == ((NodeProxy) inputElement).parent) {
                return new Object[] {};
            }
            return new Object[] { inputElement };
        }

        public void dispose() {
            TefkatPlugin.getPlugin().removeTefkatListener(this);
        }

        private TreeViewer viewer;

        public void inputChanged(Viewer treeViewer, Object oldInput, Object newInput) {
            viewer = (TreeViewer) treeViewer;
        }

        private NodeProxy getRoot() {
            return (NodeProxy) viewer.getInput();
        }

        private Map nodeMap = new HashMap();

        private NodeProxy newNode(NodeProxy parent, Term term, String txt) {
            NodeProxy node = new NodeProxy(parent, txt);
            if (null != term) {
                nodeMap.put(term, node);
            }
            return node;
        }

        private NodeProxy getNode(Term term) {
            return (NodeProxy) nodeMap.get(term);
        }

        private NodeProxy topNode = null;

        private NodeProxy middleNode = null;

        private NodeProxy leafNode = null;

        private void clear() {
            nodeMap.clear();
            NodeProxy root = getRoot();
            root.children.clear();
            leafNode = topNode = null;
            Control ctlr = viewer.getControl();
            if (null == ctlr || ctlr.isDisposed()) {
                return;
            }
            ctlr.getDisplay().syncExec(new Runnable() {

                public void run() {
                    if (!viewer.getControl().isDisposed()) {
                        viewer.refresh();
                    }
                }
            });
        }

        private void updateViewer(final NodeProxy parent, final NodeProxy child) {
            if (null == parent) {
                System.err.println("ERROR: Parent is NULL (child == " + child + ")");
                return;
            }
            if (null == child) {
                System.err.println("ERROR: Child is NULL (parent == " + parent + ")");
                return;
            }
            Control ctlr = viewer.getControl();
            if (null == ctlr || ctlr.isDisposed()) {
                return;
            }
            ctlr.getDisplay().asyncExec(new Runnable() {

                public void run() {
                    if (!viewer.getControl().isDisposed()) {
                        viewer.add(parent, child);
                    }
                }
            });
        }

        private NodeProxy appendTop(String str) {
            NodeProxy parent = getRoot();
            leafNode = topNode = newNode(parent, null, str);
            updateViewer(parent, leafNode);
            return topNode;
        }

        private void appendMiddle(String str) {
            NodeProxy parent = topNode;
            leafNode = middleNode = newNode(parent, null, str);
            updateViewer(parent, leafNode);
        }

        private void addText(String str) {
            NodeProxy node = newNode(leafNode, null, str);
            updateViewer(leafNode, node);
        }

        private void insert(NodeProxy parent, Term term, String str) {
            NodeProxy node = newNode(parent, term, str);
            updateViewer(parent, node);
            leafNode = node;
        }

        public void started() {
            clear();
        }

        public void info(String message) {
            addText("I " + message);
        }

        public void warning(String message) {
            addText("W " + message);
        }

        public void error(String message) {
            addText("E " + message);
        }

        public void transformationStarted(Transformation transformation, Resource[] srcs, Resource[] tgts, Resource trace, Binding context) {
            appendTop("Started " + transformation.getName() + "...");
        }

        public void transformationFinished() {
            appendTop("...Finished");
        }

        public void evaluateRule(TRule rule, Binding context, boolean cached) {
        }

        public void evaluateSource(TRule rule, Binding context) {
            appendMiddle("S ");
        }

        public void evaluateTarget(TRule rule, Binding context) {
            appendMiddle("T ");
        }

        public void enterTerm(Node node) {
            if (null == node.getParentNode()) {
                Term term = node.selectedLiteral();
                insert(middleNode, term, "> " + term);
            } else {
                Term term = node.selectedLiteral();
                Term parentTerm = node.getParentNode().selectedLiteral();
                insert(getNode(parentTerm), term, "> " + term);
            }
        }

        public void exitTerm(Node node) {
            Term term = node.selectedLiteral();
            insert(leafNode, term, "< " + (node.isFailure() ? "N " : "Y ") + term);
        }
    }

    private static class NodeProxy {

        Object value;

        NodeProxy parent;

        List children = new ArrayList();

        public NodeProxy() {
            this(null, null);
        }

        public NodeProxy(NodeProxy parent, Object value) {
            this.value = value;
            this.parent = parent;
            if (null != parent) {
                parent.children.add(this);
            }
        }

        public String toString() {
            return value.toString();
        }
    }
}
