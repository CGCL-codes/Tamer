package net.sourceforge.akrogen.eclipse.ui.actions;

import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.akrogen.core.component.AkrogenComponent;
import net.sourceforge.akrogen.core.component.AkrogenComponents;
import net.sourceforge.akrogen.core.project.AkrogenComponentsRef;
import net.sourceforge.akrogen.core.project.AkrogenProjectType;
import net.sourceforge.akrogen.eclipse.project.AkrogenProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

public class ContextualGenerateComponentAction extends AbstractContextualMenuAction {

    protected void fillMenu(Menu menu) {
        if (fSelection == null) {
            return;
        }
        String selectedFileName = null;
        IFile selectedFile = getSelectedFile();
        if (selectedFile != null) {
            selectedFileName = selectedFile.getName();
        }
        AkrogenProject akrogenProject = getAkrogenProject();
        if (akrogenProject != null) {
            AkrogenProjectType akrogenProjectType = akrogenProject.getAkrogenProjectType();
            if (akrogenProjectType != null) {
                Collection componentsRefList = akrogenProjectType.getComponentsRefList();
                if (componentsRefList != null) {
                    for (Iterator iter = componentsRefList.iterator(); iter.hasNext(); ) {
                        AkrogenComponentsRef akrogenComponentsRef = (AkrogenComponentsRef) iter.next();
                        AkrogenComponents components = getAkrogenComponents(akrogenComponentsRef.getNamespace());
                        if (components != null) {
                            Collection allComponent = components.getAkrogenComponentList();
                            for (Iterator iterator = allComponent.iterator(); iterator.hasNext(); ) {
                                AkrogenComponent component = (AkrogenComponent) iterator.next();
                                if (!component.mustBeFiltered(selectedFileName)) {
                                    populateMenuItem("", component, menu, 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * Add the shortcut to the context menu's launch submenu.
	*/
    private void populateMenuItem(String mode, AkrogenComponent component, Menu menu, int accelerator) {
        GenerateComponentAction action = new GenerateComponentAction(component);
        String helpContextId = null;
        if (helpContextId != null) {
            PlatformUI.getWorkbench().getHelpSystem().setHelp(action, helpContextId);
        }
        String label = component.getLabel();
        if (label == null) label = component.getName();
        action.setText(label.toString());
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(menu, -1);
    }
}
