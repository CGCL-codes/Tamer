package org.eclipse.smd.rcp;

import org.apache.log4j.Logger;
import org.eclipse.smd.gef.editor.SMDEditor;
import org.eclipse.smd.rcp.editorInput.SMDEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Classe g�rant le workbench ( interface graphique ).
 * 
 * @author Pierrick HYMBERT (phymbert [at] users.sourceforge.net)
 *         
 * 
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    /**
	 * Le logger associ� a cette instance.
	 */
    protected Logger logger = Logger.getLogger(getClass().getName());

    /**
	 * L'identifiant de la perspective du plugin.
	 */
    private static final String PERSPECTIVE_ID = "org.eclipse.smd.perspective";

    /**
	 * Instancie le configurateur du workbench.
	 */
    public ApplicationWorkbenchAdvisor() {
        super();
        logger.debug("Cr�ation du gestionnaire du workbench.");
    }

    /**
	 * Cr�ation du gestionnaire du workbench.
	 */
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        logger.debug("Cr�ation du gestionnaire de la fen�tre du workbench.");
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    /**
	 * Retourne la perspective par d�faut pour le lancement.
	 */
    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    /**
	 * Appel� apr�s l'ouverture du workench. On ouvre par d�faut un �diteur SMD.
	 */
    public void postStartup() {
        try {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            page.openEditor(new SMDEditorInput(), SMDEditor.EDITOR_ID);
        } catch (Exception e) {
            logger.error("Impossible de cr�er l'�diteur SMD.", e);
        }
    }
}
