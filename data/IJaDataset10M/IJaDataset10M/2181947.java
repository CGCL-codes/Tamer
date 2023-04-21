package br.com.gonow.gtt.rpc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.gonow.gtt.model.Project;
import br.com.gonow.gtt.model.Role;
import br.com.gonow.gtt.service.DownloadService;
import br.com.gonow.gtt.service.TranslationToolService;
import br.com.gonow.gtt.service.impl.DownloadServiceImpl;
import br.com.gonow.gtt.service.impl.TranslationToolServiceImpl;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class DownloadProjectServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(DownloadProjectServlet.class.getName());

    private TranslationToolService translationToolService = null;

    private DownloadService downloadService = null;

    @Override
    public void init() throws ServletException {
        log.info("loading DownloadProjectServlet");
        translationToolService = new TranslationToolServiceImpl();
        downloadService = new DownloadServiceImpl();
        super.init();
        log.info("loaded DownloadProjectServlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String projectId = null;
            projectId = req.getParameter("projectId");
            Project project = translationToolService.getProjectById(projectId);
            if (project == null) {
                log.info("Project " + projectId + " not found.");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            User user = UserServiceFactory.getUserService().getCurrentUser();
            boolean participate = translationToolService.doesUserParticipateInProject(user, project.getId(), new Role[] { Role.ADMINISTRATOR, Role.DEVELOPER });
            if (!participate) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            resp.setContentType("application/zip");
            String filename = project.getName() + ".zip";
            resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
            downloadService.dowloadProjectFile(project, resp.getOutputStream());
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }
}
