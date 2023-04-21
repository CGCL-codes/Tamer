package de.uni_leipzig.lots.webfrontend.actions;

import de.uni_leipzig.lots.server.services.Configuration;
import de.uni_leipzig.lots.server.services.RestoreException;
import de.uni_leipzig.lots.server.services.Restorer;
import de.uni_leipzig.lots.webfrontend.app.SetupConfig;
import de.uni_leipzig.lots.webfrontend.formbeans.BaseForm;
import de.uni_leipzig.lots.webfrontend.formbeans.InstallRestoreForm;
import de.uni_leipzig.lots.webfrontend.http.LOTSHttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dom4j.DocumentException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Diese Aktion führt die Installation des Webfrontendes aus.
 *
 * @author Alexander Kiel
 * @version $Id: InstallRestoreAction.java,v 1.7 2007/10/23 06:30:01 mai99bxd Exp $
 */
public final class InstallRestoreAction extends BaseInstallAction {

    private Configuration config;

    private Restorer restorer;

    @Required
    public void setConfig(@NotNull Configuration config) {
        this.config = config;
    }

    @Required
    public void setRestorer(@NotNull Restorer restorer) {
        this.restorer = restorer;
    }

    @Nullable
    @Override
    protected ActionForward execute(ActionMapping mapping, BaseForm form, LOTSHttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = mapping.getPath();
        if (path.endsWith("restore/")) {
            return handleRestorePage(mapping, session, request);
        } else if (path.startsWith("/post")) {
            if (form.isCancelPressed()) {
                session.removeAttributes(getClass());
                return getCancelForward(form, mapping);
            } else if ("/post/installRestoreForm".equals(path)) {
                return handleRestorePost(mapping, (InstallRestoreForm) form, session, request);
            }
        }
        throw new IllegalArgumentException("Unknown command \"" + path + "\".");
    }

    private ActionForward handleRestorePage(ActionMapping mapping, LOTSHttpSession session, HttpServletRequest request) {
        if (setupConfig.getState() != SetupConfig.State.database) {
            return mapping.findForward("correctPage");
        }
        request.setAttribute("pageData", new DefaultPageData(request.getLocale()));
        return mapping.findForward("page");
    }

    private ActionForward handleRestorePost(ActionMapping mapping, InstallRestoreForm form, LOTSHttpSession session, HttpServletRequest request) throws RestoreException, DocumentException, IOException {
        if (form.isUploadPressed()) {
            FormFile file = form.getFile();
            if (file.getFileName().endsWith("gz")) {
                restorer.restore(new GZIPInputStream(file.getInputStream()));
            } else {
                restorer.restore(file.getInputStream());
            }
            config.refresh();
            setupConfig.setState(SetupConfig.State.mail);
            return mapping.findForward("complete");
        }
        throw new IllegalArgumentException("Unknown form submit action \"" + form.getSubmitAction() + "\".");
    }
}
