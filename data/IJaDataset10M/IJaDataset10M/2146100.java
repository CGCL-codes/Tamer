package mecca.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Properties;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mecca.portal.Attributable;
import mecca.portal.ErrorMsg;
import mecca.portal.Loader;
import mecca.portal.MerakConfig;
import mecca.portal.MerakRequest;
import mecca.portal.MerakResponse;
import mecca.portal.velocity.VTemplate;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ProjectCollabPortlet extends GenericPortlet implements mecca.portal.Attributable {

    VelocityEngine engine = new VelocityEngine();

    VelocityContext context = new VelocityContext();

    protected String[] names = { "Moderators" };

    protected Hashtable values = new Hashtable();

    public String[] getNames() {
        return names;
    }

    public Hashtable getValues() {
        return values;
    }

    public void setValues(java.util.Hashtable hashtable) {
        values = hashtable;
    }

    public void doView(RenderRequest req, RenderResponse res) throws PortletException, IOException {
        try {
            PrintWriter out = res.getWriter();
            HttpServletRequest request = ((MerakRequest) req).getHttpServletRequest();
            HttpServletResponse response = ((MerakResponse) res).getHttpServletResponse();
            HttpSession session = request.getSession();
            ServletContext servletContext = session.getServletContext();
            context = (VelocityContext) session.getAttribute("_VELOCITY_CONTEXT");
            engine = (VelocityEngine) session.getAttribute("_VELOCITY_ENGINE");
            session.setAttribute("inCollabModule", "true");
            session.setAttribute("isProject", "true");
            context.put("isProject", new Boolean(true));
            PortletConfig pc = getPortletConfig();
            String pId = "";
            if (pc instanceof MerakConfig) pId = ((MerakConfig) pc).getId();
            String standalone = "true";
            if ("true".equals(standalone.trim())) {
                context.put("standalone", new Boolean(true));
                session.setAttribute("_collab_standalone", "true");
            } else if ("false".equals(standalone.trim())) {
                context.put("standalone", new Boolean(false));
                session.setAttribute("_collab_standalone", "false");
                pId = "";
            } else {
                context.put("standalone", new Boolean(false));
                session.setAttribute("_collab_standalone", "false");
                pId = "";
            }
            String appname = (String) session.getAttribute("_portal_appname");
            context.put("appname", appname);
            String moderators = (values.get(names[0]) != null ? (String) values.get(names[0]) : "") + ",";
            if (moderators.indexOf(((String) session.getAttribute("_portal_login")).trim() + ",") > -1 || "root".equals((String) session.getAttribute("_portal_role")) || "admin".equals((String) session.getAttribute("_portal_role"))) {
                context.put("allowCreateClassroom", new Boolean(true));
                session.setAttribute("_collab_role", "tutor");
            } else context.put("allowCreateClassroom", new Boolean(false));
            String module = "";
            Hashtable prop = null;
            if (session.getAttribute("_collab_module_" + pId) != null) {
                prop = (Hashtable) session.getAttribute("_collab_module_" + pId);
                module = prop.get("module") != null ? (String) prop.get("module") : "";
            } else {
                prop = new Hashtable();
            }
            if (request.getParameter("module") != null) {
                module = request.getParameter("module");
            }
            prop.put("module", module);
            context.put("session", session);
            if ("ExitCollab".equals(module)) {
                module = "";
            } else if ("ExitLesson".equals(module)) {
                module = "Lesson";
            }
            if ("Lesson".equals(module)) {
                context.put("moduleTitle", "Lessons");
                String subjectId = request.getParameter("subjectId");
                if (subjectId == null) {
                    subjectId = prop.get("subjectId") != null ? (String) prop.get("subjectId") : "";
                }
                prop.put("subjectId", subjectId);
                render(request, response, "mecca.app.ExitCollabModule", subjectId);
                render(request, response, "mecca.app.CollabSelectLessonModule", subjectId);
            } else if ("Forum".equals(module)) {
                context.put("moduleTitle", "Discussions");
                String lessonId = request.getParameter("lessonId");
                if (lessonId == null) {
                    lessonId = prop.get("lessonId") != null ? (String) prop.get("lessonId") : "";
                }
                prop.put("lessonId", lessonId);
                render(request, response, "mecca.app.ExitLessonModule", lessonId);
                render(request, response, "mecca.app.ForumModule", lessonId);
            } else if ("Announcement".equals(module)) {
                context.put("moduleTitle", "Announcements");
                String lessonId = request.getParameter("lessonId");
                if (lessonId == null) {
                    lessonId = prop.get("lessonId") != null ? (String) prop.get("lessonId") : "";
                }
                prop.put("lessonId", lessonId);
                render(request, response, "mecca.app.ExitLessonModule", lessonId);
                render(request, response, "mecca.app.AnnouncementModule", lessonId);
            } else if ("Assignment".equals(module)) {
                context.put("moduleTitle", "Assignments");
                String lessonId = request.getParameter("lessonId");
                if (lessonId == null) {
                    lessonId = prop.get("lessonId") != null ? (String) prop.get("lessonId") : "";
                }
                prop.put("lessonId", lessonId);
                render(request, response, "mecca.app.ExitLessonModule", lessonId);
                render(request, response, "mecca.app.AssignmentModule", lessonId);
            } else if ("Quiz".equals(module)) {
                context.put("moduleTitle", "Quizes");
                String lessonId = request.getParameter("lessonId");
                if (lessonId == null) {
                    lessonId = prop.get("lessonId") != null ? (String) prop.get("lessonId") : "";
                }
                prop.put("lessonId", lessonId);
                render(request, response, "mecca.app.ExitLessonModule", lessonId);
                render(request, response, "mecca.app.QuizModule", lessonId);
            } else if ("LessonNote".equals(module)) {
                context.put("moduleTitle", "Lesson Notes");
                String lessonId = request.getParameter("lessonId");
                if (lessonId == null) {
                    lessonId = prop.get("lessonId") != null ? (String) prop.get("lessonId") : "";
                }
                prop.put("lessonId", lessonId);
                render(request, response, "mecca.app.ExitLessonModule", lessonId);
                render(request, response, "mecca.app.LessonNoteModule", lessonId);
            } else if ("Scheduler".equals(module)) {
                context.put("moduleTitle", "Scheduler");
                String subjectId = request.getParameter("subjectId");
                if (subjectId == null) {
                    subjectId = prop.get("subjectId") != null ? (String) prop.get("subjectId") : "";
                }
                prop.put("subjectId", subjectId);
                render(request, response, "mecca.app.ExitSubjectModule", subjectId);
                render(request, response, "mecca.planner.PlannerModule", subjectId);
            }
            if ("".equals(module)) {
                context.put("moduleTitle", "Subjects List");
                String subjectId = request.getParameter("subjectId");
                if (subjectId == null) {
                    subjectId = prop.get("subjectId") != null ? (String) prop.get("subjectId") : "";
                }
                prop.put("subjectId", subjectId);
                render(request, response, "mecca.app.CollabSelectSubjectModule", pId);
            }
            session.setAttribute("_collab_module_" + pId, prop);
            session.setAttribute("inCollabModule", "false");
        } catch (ServletException sex) {
            throw new PortletException(sex.getMessage());
        }
    }

    public void initialize(ServletContext ctx) throws ServletException {
        try {
            Properties p = loadConfigurationSimple(ctx);
            engine.init(p);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    public void render(HttpServletRequest req, HttpServletResponse res, String module, String id) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession();
        res.setContentType("text/html");
        try {
            VTemplate content = null;
            try {
                Class klazz = Loader.load(module);
                content = ((VTemplate) klazz.newInstance());
                content.setEnvironment(engine, context, req, res);
                content.setId(id);
                if (content instanceof mecca.app.ForumModule) {
                    Hashtable h = new Hashtable();
                    h.put("Category", id);
                    h.put("Rows", "5");
                    h.put("Moderators", "tutor");
                    h.put("Attachment", "true");
                    ((Attributable) content).setValues(h);
                } else if (content instanceof mecca.app.AssignmentModule) {
                    Hashtable h = new Hashtable();
                    h.put("Category", id);
                    h.put("Rows", "5");
                    h.put("Moderators", "tutor");
                    h.put("Attachment", "true");
                    ((Attributable) content).setValues(h);
                } else if (content instanceof mecca.app.LessonNoteModule) {
                    Hashtable h = new Hashtable();
                    h.put("Category", id);
                    h.put("Rows", "5");
                    h.put("Moderators", "tutor");
                    h.put("Attachment", "true");
                    h.put("startAsList", "true");
                    ((Attributable) content).setValues(h);
                } else if (content instanceof mecca.app.MessageModule) {
                    Hashtable h = new Hashtable();
                    h.put("Category", id);
                    h.put("Rows", "5");
                    h.put("Moderators", "tutor");
                    h.put("Attachment", "true");
                    h.put("startAsList", "false");
                    ((Attributable) content).setValues(h);
                }
            } catch (ClassNotFoundException cnfex) {
                content = new ErrorMsg(engine, context, req, res);
                ((ErrorMsg) content).setError("ClassNotFoundException : " + cnfex.getMessage());
            } catch (InstantiationException iex) {
                content = new ErrorMsg(engine, context, req, res);
                ((ErrorMsg) content).setError("InstantiationException : " + iex.getMessage());
            } catch (IllegalAccessException illex) {
                content = new ErrorMsg(engine, context, req, res);
                ((ErrorMsg) content).setError("IllegalAccessException : " + illex.getMessage());
            } catch (Exception ex) {
                content = new ErrorMsg(engine, context, req, res);
                ((ErrorMsg) content).setError("Other Exception during class initiation : " + ex.getMessage());
                ex.printStackTrace();
            }
            try {
                if (content != null) content.print();
            } catch (Exception ex) {
                out.println(ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Properties loadConfigurationSimple(ServletContext ctx) throws IOException, FileNotFoundException {
        String path = ctx.getRealPath("/");
        Properties p = new Properties();
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
        return p;
    }
}
