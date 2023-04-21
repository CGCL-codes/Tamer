package de.fraunhofer.isst.axbench.eastadlinterface.models.listener;

import org.eclipse.ui.part.MultiPageEditorPart;
import de.fraunhofer.isst.axbench.Session;
import de.fraunhofer.isst.axbench.api.ISessionListener;
import de.fraunhofer.isst.axbench.axlang.elements.Model;
import de.fraunhofer.isst.axbench.eastadlinterface.Env;
import de.fraunhofer.isst.axbench.editors.axlmultipage.AXLMultiPageEditor;
import de.fraunhofer.isst.axbench.editors.axlmultipage.axleditor.AXLTextEditor;

public class SessionListener implements ISessionListener {

    private Env env;

    private AXLTextEditor axlEditor;

    public SessionListener(Env e) {
        env = e;
        MultiPageEditorPart editorPart = Session.getCurrentmultipage();
        if (editorPart instanceof AXLMultiPageEditor) {
            AXLMultiPageEditor axlMultiPageEditor = (AXLMultiPageEditor) editorPart;
            axlEditor = axlMultiPageEditor.getAxlEditor();
        }
    }

    /**
	 * @brief Reacts on a change of the session element.
	 * 
	 * @param objCaller calling object
	 */
    @Override
    public void elementChanged(Object objCaller) {
        if (env.areListenersDisabled()) return;
        if (!Session.isAxlcurrentinputsaved()) {
            return;
        }
        if (objCaller != axlEditor) {
            return;
        }
        env.disableAdapters();
        env.getModelInterface().updateModel((Model) Session.getCurrentElement());
        env.enableAdapters();
    }

    /**
	 * @brief Reacts on a specified event .
	 * @param objCaller - specified event
	 */
    @Override
    public void handleCall(Object objCaller, String content) {
    }
}
