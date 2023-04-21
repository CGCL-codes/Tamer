package es.gavab.patterndetect.popup.actions;

import java.util.ArrayList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.ObjectPluginAction;
import es.gavab.patterndetect.algorithms.AdapterDetector;
import es.gavab.patterndetect.algorithms.Candidato;
import es.gavab.patterndetect.dialog.PatternDialog;

/***
 * Clase que representa la accion del men� correspondiente 
 * a la deteccion del patr�n Adapter 
 * @author <a href="mailto:anebur@terra.es">Rub�n Alonso Barrio</a>
 */
public class DetAdapter implements IObjectActionDelegate {

    public DetAdapter() {
        super();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void run(IAction action) {
        Shell s = new Shell();
        ISelection selection = ((ObjectPluginAction) action).getSelection();
        IJavaProject javaProject = (IJavaProject) ((StructuredSelection) selection).getFirstElement();
        ArrayList<Candidato> c = AdapterDetector.detectarPatronAdapter(javaProject);
        PatternDialog dialog = new PatternDialog(s, c);
        dialog.open();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
