package org.argoidea.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.application.ApplicationManager;
import org.argoidea.ArgoIdeaProjectIntegration;
import org.argoidea.ClasspathUtils;
import org.argoidea.L10N;
import static org.argoidea.actions.Utils.getIdeaProjectIntegration;
import org.argoidea.images.Icons;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initializes ArgoUML.
 *
 * @author Juergen Kellerer, 2009-07-24
 */
public class ArgoUMLInitializer extends AnAction {

    public ArgoUMLInitializer() {
        getTemplatePresentation().setIcon(Icons.ARGO_ICON_16);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        ArgoIdeaProjectIntegration projectIntegration = getIdeaProjectIntegration(e);
        e.getPresentation().setEnabled(projectIntegration != null && !projectIntegration.isInitialized() && !projectIntegration.getGlobalConfigurationData().isArgoUmlInstallPathEmpty());
    }

    /**
	 * {@inheritDoc}
	 */
    public void actionPerformed(AnActionEvent e) {
        final ArgoIdeaProjectIntegration integration = getIdeaProjectIntegration(e);
        ClasspathUtils.invokeLater(getClass().getClassLoader(), new Runnable() {

            public void run() {
                String message = L10N.get("initAgroUMLFailed");
                try {
                    integration.initializeArgoUml();
                    if (integration.isInitialized()) message = null;
                } catch (IOException e1) {
                    Logger.getAnonymousLogger().log(Level.WARNING, e1.getMessage(), e1);
                }
                if (message != null) Messages.showErrorDialog(integration.getProject(), message, L10N.get("initMainTask"));
            }
        });
    }
}
