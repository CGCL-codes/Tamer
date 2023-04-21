package net.sourceforge.jisocreator.action.isoexplorer;

import java.io.File;
import java.util.EventObject;
import net.sourceforge.jisocreator.gui.sashfom.IsoExplorerSashForm;
import net.sourceforge.jisocreator.model.isoexplorer.decl.ITreeNode;
import net.sourceforge.jisocreator.model.osexplorer.OSExplorer;
import net.sourceforge.jisocreator.util.ImageUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;

public class OpenIsoEntryAction extends Action implements IDoubleClickListener, ISelectionChangedListener {

    private static OpenIsoEntryAction instance;

    private ITreeNode node;

    private OpenIsoEntryAction() {
        super("Open", ImageUtils.getInstance().loadImageDescriptor("run.png"));
        setToolTipText("Open a file or folder, in the layout");
        setEnabled(false);
    }

    @Override
    public void run() {
        if (((File) node.getElement()).isFile()) {
            OSExplorer.getInstance().launch((File) node.getElement());
        } else {
            IsoExplorerSashForm.getInstance().getIsoDirectoriesTree().setSelection(IsoExplorerSashForm.getInstance().getIsoDirectoriesTable().getSelection());
            IsoExplorerSashForm.getInstance().getIsoDirectoriesTree().expandToLevel(node, 1);
        }
    }

    @Override
    public void doubleClick(DoubleClickEvent arg0) {
        setNode(arg0);
        if (arg0.getSource() instanceof TreeViewer) {
            if (IsoExplorerSashForm.getInstance().getIsoDirectoriesTree().getExpandedState(node)) {
                IsoExplorerSashForm.getInstance().getIsoDirectoriesTree().collapseToLevel(node, 1);
            } else {
                IsoExplorerSashForm.getInstance().getIsoDirectoriesTree().expandToLevel(node, 1);
            }
        } else {
            run();
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent arg0) {
        setNode(arg0);
        if (arg0.getSource() instanceof TreeViewer) {
            if (node == null) {
                DeleteIsoEntryAction.getInstance().setEnabled(false);
                System.out.println("SWT library bug");
                return;
            } else {
                DeleteIsoEntryAction.getInstance().setEnabled(true);
            }
            if (node.isRoot()) {
                GoToIsoEntryParentAction.getInstance().setEnabled(false);
            } else {
                GoToIsoEntryParentAction.getInstance().setEnabled(true);
            }
            IsoExplorerSashForm.getInstance().getIsoTableText().setText(node.getIsoName());
            IsoExplorerSashForm.getInstance().getIsoDirectoriesTable().setInput(node);
            setEnabled(false);
            DeleteIsoEntryAction.getInstance().setEnabled(false);
        } else {
            setEnabled(true);
            DeleteIsoEntryAction.getInstance().setEnabled(true);
        }
    }

    public void setNode(ITreeNode node) {
        this.node = node;
    }

    public void setNode(EventObject event) {
        IStructuredSelection selection;
        if (event instanceof DoubleClickEvent) {
            DoubleClickEvent dbe = (DoubleClickEvent) event;
            selection = (IStructuredSelection) dbe.getSelection();
        } else {
            SelectionChangedEvent sce = (SelectionChangedEvent) event;
            selection = (IStructuredSelection) sce.getSelection();
        }
        setNode((ITreeNode) selection.getFirstElement());
    }

    public static OpenIsoEntryAction getInstance() {
        if (instance == null) {
            instance = new OpenIsoEntryAction();
        }
        return instance;
    }
}
