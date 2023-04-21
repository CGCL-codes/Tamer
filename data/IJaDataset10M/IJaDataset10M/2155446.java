package org.mftech.dawn.codegen.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.randomice.oawutil.Monitor;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mftech.dawn.codegen.creators.ClientCreator;
import org.mftech.dawn.codegen.creators.Creator;
import org.mftech.dawn.codegen.creators.JavascriptCreator;
import org.mftech.dawn.codegen.creators.ProjectCreationHelper;
import org.mftech.dawn.codegen.creators.ServerCreator;

public class GenerateClientCodeAction implements IObjectActionDelegate {

    private IResource selectedElement;

    private static final String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

    private Object part;

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.part = targetPart;
    }

    @Override
    public void run(IAction action) {
        Monitor.runWithMonitor("Generate Dawn Code", new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("Generate Dawn Code", 1000);
                System.out.println("JUCHU!");
                ArrayList<Creator> creators = new ArrayList<Creator>();
                creators.add(new ClientCreator(selectedElement));
                for (Creator creator : creators) {
                    System.out.println("Creating: " + creator);
                    creator.create(new SubProgressMonitor(monitor, 1000 / creators.size()));
                    System.out.println("Created: " + creator);
                }
            }
        });
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            Object sel = ((IStructuredSelection) selection).getFirstElement();
            if (sel instanceof IResource) {
                selectedElement = (IResource) sel;
            }
        }
    }
}
