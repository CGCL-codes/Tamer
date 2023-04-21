package org.informaticisenzafrontiere.openstaff;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.informaticisenzafrontiere.openstaff.model.AnagraficaGroup;
import org.informaticisenzafrontiere.openstaff.util.MessageBundle;

public class ManageWagePacketAction extends Action implements ISelectionListener, IWorkbenchAction {

    public static String ID = "org.informaticisenzafrontiere.openstaff.manageWagePacket";

    private final IWorkbenchWindow window;

    private static IStructuredSelection selection;

    public ManageWagePacketAction(IWorkbenchWindow window) {
        super();
        this.window = window;
        setId(ID);
        setActionDefinitionId(ID);
        setText(MessageBundle.getMessage("openstaff.wagepacket.wagePacket"));
        setToolTipText(MessageBundle.getMessage("openstaff.wagepacket.manageWagePacket"));
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.GESTIONE_BUSTEPAGA));
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection incoming) {
        if (incoming instanceof IStructuredSelection) {
            selection = (IStructuredSelection) incoming;
            setEnabled(false);
        }
        AnagraficaGroup stampa = null;
        if (selection.getFirstElement() instanceof AnagraficaGroup) {
            stampa = ((AnagraficaGroup) selection.getFirstElement()).getParent();
        }
        setEnabled(selection.size() == 1 && selection.getFirstElement() instanceof AnagraficaGroup && stampa.getName().equals("RootGroup"));
    }

    @Override
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    public void run() {
        Object item = selection.getFirstElement();
        if (item instanceof AnagraficaGroup) {
            AnagraficaGroup dipendente = (AnagraficaGroup) item;
            IWorkbenchPage page = window.getActivePage();
            ManageWagePacketEditorInput editorInput = new ManageWagePacketEditorInput(dipendente.getName(), getID());
            try {
                page.openEditor(editorInput, ManageWagePacket.ID);
                IViewReference vi = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findViewReference(AnagraficaView.ID);
                IWorkbenchPart part = vi.getView(true);
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(part);
            } catch (PartInitException e) {
            }
        }
    }

    public int getID() {
        Object item = selection.getFirstElement();
        AnagraficaGroup dipendente = (AnagraficaGroup) item;
        String regex = "\\d+";
        String numID = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dipendente.getName());
        while (matcher.find()) numID = matcher.group();
        return Integer.parseInt(numID);
    }
}
