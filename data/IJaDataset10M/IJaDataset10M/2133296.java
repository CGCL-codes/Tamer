package net.sourceforge.customercare.client.actions;

import net.sourceforge.customercare.client.editors.EditorInputAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class OpenUserEditor extends Action {

    /**
	 * Run Action
	 * @param pId
	 * @param name
	 */
    public void run(Integer pId, String value) {
        try {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            EditorInputAdapter input = new EditorInputAdapter(value, pId);
            page.openEditor(input, "editors.UserEditor");
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
