package ca.ubc.jquery.scapes;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.Perspective;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;
import ca.ubc.jquery.JQueryBackendPlugin;

public class RestoreJQueryScapeHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        FileDialog d = new FileDialog(JQueryBackendPlugin.getShell());
        d.setFilterNames(new String[] { "JQueryScapes Files" });
        d.setFilterExtensions(new String[] { "*.jqs" });
        String loadFile = d.open();
        if (loadFile != null) {
            XMLMemento xmlFile = null;
            try {
                Reader reader = new FileReader(loadFile);
                xmlFile = XMLMemento.createReadRoot(reader);
                reader.close();
                PerspectiveRegistry pr = (PerspectiveRegistry) WorkbenchPlugin.getDefault().getPerspectiveRegistry();
                PerspectiveDescriptor pd = (PerspectiveDescriptor) JQueryBackendPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
                WorkbenchPage page = (WorkbenchPage) JQueryBackendPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
                String label = xmlFile.getString("label");
                PerspectiveDescriptor current = pr.createPerspective(label, pd);
                for (int i = 1; current == null; i++) {
                    label = xmlFile.getString("label") + " (" + i + ")";
                    current = pr.createPerspective(label, pd);
                }
                String id = current.getId();
                page.setPerspective(current);
                Perspective persp = page.getActivePerspective();
                IMemento xmlViews = xmlFile.getChild(IWorkbenchConstants.TAG_VIEWS);
                page.getViewFactory().restoreState(xmlViews);
                IMemento xmlPersp = xmlFile.getChild(IWorkbenchConstants.TAG_PERSPECTIVE);
                IMemento xmlDescriptor = xmlPersp.getChild(IWorkbenchConstants.TAG_DESCRIPTOR);
                xmlDescriptor.putString(IWorkbenchConstants.TAG_ID, id);
                xmlDescriptor.putString(IWorkbenchConstants.TAG_LABEL, label);
                persp.restoreState(xmlPersp);
                page.savePerspectiveAs(current);
                page.resetPerspective();
            } catch (WorkbenchException e) {
                JQueryBackendPlugin.error("Restoring perspective ", e);
            } catch (IOException e) {
                JQueryBackendPlugin.error("Restoring perspective ", e);
            }
        }
        return null;
    }
}
