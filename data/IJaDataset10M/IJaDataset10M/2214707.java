package edu.vt.eng.swat.workflow.view.workflow.listeners;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import edu.vt.eng.swat.workflow.view.workflow.actions.OpenSignInAction;

public class SignInDoubleClickListener implements IDoubleClickListener {

    private OpenSignInAction openSignInAction;

    public SignInDoubleClickListener(TreeViewer treeViewer) {
        openSignInAction = new OpenSignInAction(treeViewer);
    }

    @Override
    public void doubleClick(DoubleClickEvent event) {
        openSignInAction.run();
    }
}
