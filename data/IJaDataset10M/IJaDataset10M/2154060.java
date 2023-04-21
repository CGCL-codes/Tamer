package org.xaware.tracer.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.xaware.shared.util.logging.XAwareLogger;
import org.xaware.tracer.editors.IMainTracer;

public class LoadInputActionDelegate extends AbstractTracerExecutionActionDelegate implements IWorkbenchWindowActionDelegate {

    protected static final XAwareLogger logger = XAwareLogger.getXAwareLogger(LoadInputActionDelegate.class.getName());

    @Override
    protected XAwareLogger getLogger() {
        return logger;
    }

    public void run(final IAction action) {
        if (m_editor == null || (!(m_editor instanceof IMainTracer))) {
            return;
        }
        final IMainTracer mainTracer = (IMainTracer) m_editor;
        LoadInputDataHelper.loadInputData(mainTracer.getActiveTracer());
    }

    @Override
    protected void updateActionStatus() {
        if (m_action == null) {
            return;
        }
        final IWorkbenchPage page = m_window.getActivePage();
        if (page == null) {
            m_editor = null;
        } else {
            m_editor = page.getActiveEditor();
        }
        if (m_editor == null || (!(m_editor instanceof IMainTracer))) {
            m_action.setEnabled(false);
        } else {
            final IMainTracer tracer = (IMainTracer) m_editor;
            m_action.setEnabled(tracer.needsInput());
        }
    }
}
