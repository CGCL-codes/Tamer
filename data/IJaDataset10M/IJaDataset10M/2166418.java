package org.jsens.project.internal.influence;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.jsens.project.internal.views.ContentChangeListener;

public class InfluenceAddAction extends Action {

    private final List<ContentChangeListener> changeListeners = new ArrayList<ContentChangeListener>();

    public InfluenceAddAction() {
        this.setText("Neue Einflussmatrix...");
        this.setToolTipText("Neue Einflussmatrix...");
    }

    @Override
    public void run() {
        IWizard wizard = new InfluenceMatrixWizard();
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        dialog.open();
        fireContentChangeListener(null);
    }

    public void addContentChangeListener(ContentChangeListener listener) {
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    private void fireContentChangeListener(Object item) {
        for (ContentChangeListener listener : changeListeners) {
            listener.contentChanged(item);
        }
    }
}
