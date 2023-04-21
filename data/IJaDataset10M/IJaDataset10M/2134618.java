package com.tensegrity.palorcp.app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.keys.IBindingService;
import com.tensegrity.palobrowser.ErrorDialog;
import com.tensegrity.palobrowser.perspectives.DefaultPerspective;

/**
 * <code>ApplicationWorkbenchAdvisor</code>
 * <p>
 * Defines the palor rcp default perspective and reconfigures some used eclipse
 * commands
 * </p>
 * 
 * @author Stepan Rutz
 * @version $Id: ApplicationWorkbenchAdvisor.java,v 1.22 2007/07/13 15:07:48 AndreasEbbert Exp $
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final String PERSPECTIVE_ID = DefaultPerspective.ID;

    private IWorkbenchWindowConfigurer configurer;

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        this.configurer = configurer;
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);
    }

    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    public void postStartup() {
        super.postStartup();
        try {
            postAdjustCommands();
        } catch (RuntimeException e) {
            System.err.println("error in ApplicationWorkbenchAdvisor.postAdjustCommands: " + e.getLocalizedMessage());
        }
    }

    /**
   * Adjust eclipse ui commands
   */
    private void postAdjustCommands() {
        Set toDisable = new HashSet(Arrays.asList(new String[] { "org.eclipse.ui.edit.cut", "org.eclipse.ui.edit.copy", "org.eclipse.ui.edit.paste", "org.eclipse.ui.edit.undo", "org.eclipse.ui.edit.redo", "org.eclipse.ui.newWizard", "org.eclipse.ui.file.properties", "org.eclipse.ui.file.print", "org.eclipse.ui.navigate.next", "org.eclipse.ui.navigate.previous", "org.eclipse.ui.window.nextPerspective", "org.eclipse.ui.window.previousPerspective", "org.eclipse.ui.window.openEditorDropDown" }));
        IWorkbench workbench = configurer.getWorkbenchConfigurer().getWorkbench();
        Object adapter = workbench.getAdapter(IBindingService.class);
        if (adapter != null) {
            IBindingService bindingService = (IBindingService) adapter;
            Binding bindings[] = bindingService.getBindings();
            for (int i = 0; i < bindings.length; ++i) {
                Binding binding = bindings[i];
                if (binding.getParameterizedCommand() != null && toDisable.contains(binding.getParameterizedCommand().getId())) {
                    Command command = binding.getParameterizedCommand().getCommand();
                    command.undefine();
                }
            }
        }
    }

    public void eventLoopException(Throwable exception) {
        super.eventLoopException(exception);
        try {
            Shell shell = getWorkbenchConfigurer().getWorkbench().getActiveWorkbenchWindow().getShell();
            ErrorDialog.getInstance().show(shell, exception);
            exception.printStackTrace();
        } catch (Throwable t) {
        }
    }

    public boolean preShutdown() {
        IWorkbenchPage wPage = getWorkbenchConfigurer().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart vPart = wPage.findView("com.tensegrity.palomodeler.views.NavigatorView");
        if (vPart != null) wPage.hideView(vPart);
        return super.preShutdown();
    }
}
