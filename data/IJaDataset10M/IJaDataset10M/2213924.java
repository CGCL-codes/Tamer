package com.fh.auge.table.test;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.fh.auge.model.AppModel;
import com.fh.auge.model.DefaultTreeNode;
import com.fh.auge.model.PortfolioModelImpl;
import com.fh.auge.model.Position;
import com.fh.auge.ui.columns.TreeViewerSupport;
import com.fh.auge.ui.columns.portfolio.PortfolioAspectProvider;

public class Test4 {

    TreeViewerSupport manager;

    private TreeViewer viewer;

    private TreeViewerSupport viewerSupport;

    private PortfolioAspectProvider aspectProvider;

    private DefaultTreeModel model;

    private PortfolioModelImpl portfolioModel;

    public Test4(Shell shell) {
        viewer = new TreeViewer(shell, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        viewer.getTree().setHeaderVisible(true);
        portfolioModel = AppModel.getInstance().getPortfolioModel();
        viewerSupport = new TreeViewerSupport(viewer);
        aspectProvider = new PortfolioAspectProvider();
        viewerSupport.setAspectProvider(aspectProvider);
        viewerSupport.setSelectedAspect(aspectProvider.getTreeViewerAspects().get(1));
        model = portfolioModel.getTreeModel();
        viewer.setInput(model);
        viewer.expandAll();
        Button add = new Button(shell, SWT.PUSH);
        add.setText("Add");
        add.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                portfolioModel.refresh(true, true);
            }
        });
        Button del = new Button(shell, SWT.PUSH);
        del.setText("Delete");
        del.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                System.err.println("Delete");
                ISelection sel = viewer.getSelection();
                if (!sel.isEmpty()) {
                    DefaultTreeNode node = (DefaultTreeNode) ((IStructuredSelection) sel).getFirstElement();
                    if (node.getUserObject() instanceof Position) portfolioModel.remove((Position) node.getUserObject());
                }
            }
        });
        Button update = new Button(shell, SWT.PUSH);
        update.setText("Update");
        update.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                System.err.println("Update");
                ISelection sel = viewer.getSelection();
                if (!sel.isEmpty()) {
                    DefaultTreeNode node = (DefaultTreeNode) ((IStructuredSelection) sel).getFirstElement();
                    node.setUserObject(node.getUserObject() + " updated!");
                    model.nodeChanged(node);
                }
            }
        });
    }

    DefaultTreeModel createModel() {
        DefaultTreeNode root = new DefaultTreeNode("Root");
        DefaultTreeNode group1 = new DefaultTreeNode("Group1");
        DefaultTreeNode el1 = new DefaultTreeNode("El1");
        DefaultTreeNode el2 = new DefaultTreeNode("El1");
        group1.add(el1);
        group1.add(el2);
        root.add(group1);
        return new DefaultTreeModel(root);
    }

    ;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        new Test4(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    static class TreeModelContentProvider implements ITreeContentProvider {

        private AbstractTreeViewer viewer;

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            this.viewer = (AbstractTreeViewer) viewer;
            if (oldInput != null) {
                TreeModel treeModel = (TreeModel) oldInput;
                treeModel.removeTreeModelListener(listener);
            }
            if (newInput != null) {
                TreeModel treeModel = (TreeModel) newInput;
                treeModel.addTreeModelListener(listener);
            }
        }

        public Object[] getChildren(Object parentElement) {
            DefaultTreeNode node = (DefaultTreeNode) parentElement;
            return node.getChildren();
        }

        public Object getParent(Object element) {
            DefaultTreeNode node = (DefaultTreeNode) element;
            return node.getParent();
        }

        public boolean hasChildren(Object element) {
            DefaultTreeNode node = (DefaultTreeNode) element;
            return !node.isLeaf();
        }

        public Object[] getElements(Object inputElement) {
            if (inputElement != null) {
                if (inputElement instanceof TreeModel) {
                    TreeModel m = (TreeModel) inputElement;
                    return ((DefaultTreeNode) m.getRoot()).getChildren();
                }
                return new Object[0];
            }
            return new Object[0];
        }

        public void dispose() {
        }

        TreeModelListener listener = new TreeModelListener() {

            public void treeNodesChanged(TreeModelEvent e) {
                System.err.println("treeNodesChanged " + e);
                viewer.refresh(e.getChildren()[0]);
            }

            public void treeNodesInserted(TreeModelEvent e) {
                Object parent = e.getTreePath().getLastPathComponent();
                System.err.println("treeNodesInserted " + parent + " " + e.getTreePath().getPathCount());
                if (e.getTreePath().getPathCount() == 1) {
                    parent = e.getSource();
                }
                viewer.add(parent, e.getChildren());
            }

            public void treeNodesRemoved(TreeModelEvent e) {
                System.err.println("treeNodesRemoved " + e);
                viewer.remove(e.getChildren());
            }

            public void treeStructureChanged(TreeModelEvent e) {
                System.err.println("treeStructureChanged " + e);
            }
        };
    }
}
