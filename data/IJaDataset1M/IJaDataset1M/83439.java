package de.walware.statet.r.nico.ui.tools;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkbenchPage;
import de.walware.eclipsecommons.ui.util.UIAccess;
import de.walware.statet.nico.core.runtime.IToolRunnable;
import de.walware.statet.nico.core.runtime.SubmitType;
import de.walware.statet.nico.core.runtime.ToolController;
import de.walware.statet.nico.ui.NicoUITools;
import de.walware.statet.nico.ui.console.NIConsole;
import de.walware.statet.r.internal.nico.ui.RNicoMessages;
import de.walware.statet.r.nico.IBasicRAdapter;

/**
 * 
 */
public class RQuitRunnable implements IToolRunnable<IBasicRAdapter> {

    public String getTypeId() {
        return ToolController.QUIT_TYPE_ID;
    }

    public String getLabel() {
        return RNicoMessages.Quit_Task_label;
    }

    public SubmitType getSubmitType() {
        return SubmitType.TOOLS;
    }

    public void run(final IBasicRAdapter tools, final IProgressMonitor monitor) throws InterruptedException, CoreException {
        final String command = "q()";
        tools.submitToConsole(command, monitor);
        final IWorkbenchPage page = UIAccess.getActiveWorkbenchPage(false);
        final NIConsole console = NicoUITools.getConsole(tools.getController().getProcess());
        NicoUITools.showConsole(console, page, true);
    }
}
