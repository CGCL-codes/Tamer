package orcajo.azada.browser.views;

import orcajo.azada.browser.wizard2.WhithMembersWizard;
import orcajo.azada.core.model.QueryManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.MetadataElement;
import org.olap4j.metadata.Dimension.Type;

public class ShowWizard2 extends AddAction {

    private TreeViewer viewer;

    private QueryManager qManager;

    ShowWizard2(TreeViewer viewer, QueryManager qManager, ImageDescriptor desc, String title) {
        super(viewer, null, title);
        super.setImageDescriptor(desc);
        this.viewer = viewer;
        this.qManager = qManager;
    }

    public void run() {
        if (canAdd()) {
            Wizard wizard = new WhithMembersWizard(qManager);
            WizardDialog dialog;
            dialog = new WizardDialog(viewer.getTree().getShell(), wizard);
            dialog.create();
            dialog.open();
        }
    }

    protected MetadataElement getElement() {
        MetadataElement elemet = null;
        ISelection selection = viewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        Node n = (Node) obj;
        if (n != null) {
            if (n.getData() instanceof Dimension) {
                Dimension dim = (Dimension) n.getData();
                Hierarchy hier = dim.getDefaultHierarchy();
                if (hier != null) {
                    try {
                        elemet = hier.getDefaultMember();
                    } catch (OlapException e) {
                    }
                }
            } else if (n.getData() instanceof Hierarchy) {
                Hierarchy hier = (Hierarchy) n.getData();
                try {
                    elemet = hier.getDefaultMember();
                } catch (OlapException e) {
                }
            } else if (n.getData() instanceof Level) {
                elemet = (Level) n.getData();
            } else if (n.getData() instanceof Member) {
                elemet = (Member) n.getData();
            } else if (n.getData() instanceof Cube) {
                elemet = (Cube) n.getData();
            }
        }
        return elemet;
    }

    @Override
    protected boolean canAdd() {
        elemet = getElement();
        if (elemet instanceof Dimension) {
            Dimension dim = (Dimension) elemet;
            try {
                if (Type.MEASURE.equals(dim.getDimensionType())) {
                    return true;
                }
            } catch (OlapException e) {
            }
        } else if (elemet instanceof Cube) {
            return qManager.getOlapModel().isInitialized();
        }
        return elemet instanceof Measure;
    }
}
