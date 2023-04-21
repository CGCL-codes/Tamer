package net.jotwiki.ctrl;

import net.jot.web.ctrl.JOTController;
import net.jot.web.views.JOTGeneratedFormView;
import net.jotwiki.db.WikiPermission;
import net.jotwiki.forms.setup.NamespaceSetup;

/**
 * Handles a namespace options Edition request
 * @author tcolar
 */
public class NsEditController extends JOTController {

    public String process() throws Exception {
        NamespaceSetup form = (NamespaceSetup) getForm(NamespaceSetup.class);
        request.setAttribute(JOTGeneratedFormView.GENERATED_FORM, form);
        return RESULT_SUCCESS;
    }

    public boolean validatePermissions() {
        return WikiPermission.hasPermission(request, WikiPermission.SETUP);
    }
}
