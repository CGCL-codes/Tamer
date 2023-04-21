package lebah.app.content;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import javax.servlet.http.HttpSession;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */
public class ArticleModule extends lebah.portal.velocity.VTemplate implements lebah.portal.Attributable {

    protected String[] names = { "Moderators" };

    protected Hashtable values = new Hashtable();

    private static String[] month_name = { "January", "February", "March", "April", "May", "Jun", "July", "August", "September", "October", "November", "December" };

    private static String[] day_name = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    private static String[] hour_name = { "12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM" };

    public String[] getNames() {
        return names;
    }

    public Hashtable getValues() {
        return values;
    }

    public void setValues(java.util.Hashtable hashtable) {
        values = hashtable;
    }

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/article/content.vm";
        String subjectId = getId();
        context.put("formname", getId());
        String moderators = values.get(names[0]) != null ? (String) values.get(names[0]) + "," : "";
        String _portal_role = (String) session.getAttribute("_portal_role");
        String inCollabModule = session.getAttribute("inCollabModule") != null ? (String) session.getAttribute("inCollabModule") : "false";
        String collab_role = "true".equals(inCollabModule) ? session.getAttribute("_collab_role") != null ? (String) session.getAttribute("_collab_role") : "" : "";
        String roleToAllow = !"".equals(collab_role) ? collab_role : _portal_role;
        String isLogin = (String) session.getAttribute("_portal_islogin");
        if (moderators.indexOf(roleToAllow + ",") > -1 && "true".equals(isLogin)) {
            context.put("allowPost", new Boolean(true));
            context.put("allowUpdate", new Boolean(true));
            context.put("allowDelete", new Boolean(true));
            context.put("isModerator", new Boolean(true));
        } else {
            context.put("allowPost", new Boolean(false));
            context.put("allowUpdate", new Boolean(false));
            context.put("allowDelete", new Boolean(false));
            context.put("isModerator", new Boolean(false));
        }
        context.put("month_name", month_name);
        Calendar now = new GregorianCalendar();
        now.setTime(new java.util.Date());
        String now_day = "" + now.get(Calendar.DAY_OF_MONTH);
        int now_month = now.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR);
        context.put("year", new Integer(year));
        String submit = getParam("command_" + getId());
        String formId = getParam("form_id");
        if (formId.equals(getId())) {
            if ("addPage".equals(submit)) {
                template_name = "vtl/article/add.vm";
                context.put("isNew", new Boolean(true));
            } else if ("addNew".equals(submit)) {
                addNew(session, subjectId);
            } else if ("movePage".equals(submit)) {
                movePage(session, subjectId);
            } else if ("deletePage".equals(submit)) {
                deletePage(session, subjectId);
            } else if ("modifyPage".equals(submit)) {
                template_name = "vtl/article/add.vm";
                modifyPage(session, subjectId);
                context.put("isNew", new Boolean(false));
            } else if ("updatePage".equals(submit)) {
                updatePage(session);
            }
        }
        int pageNum = !"".equals(getParam("pageNum_" + getId())) ? Integer.parseInt(getParam("pageNum_" + getId())) : 0;
        Article p = ArticleData.getArticle(subjectId);
        context.put("portfolio", p);
        context.put("pageNum", new Integer(pageNum));
        Template template = engine.getTemplate(template_name);
        return template;
    }

    void addNew(HttpSession session, String subjectId) throws Exception {
        String text = getParam("message");
        ArticleData.addNewPage(subjectId, text);
    }

    void movePage(HttpSession session, String subjectId) throws Exception {
        int toPage = Integer.parseInt(getParam("movePage_" + getId())) + 1;
        int fromPage = Integer.parseInt(getParam("pageNum_" + getId())) + 1;
        ArticleData.movePage(subjectId, fromPage, toPage);
    }

    void deletePage(HttpSession session, String subjectId) throws Exception {
        int page = Integer.parseInt(getParam("pageNum_" + getId())) + 1;
        ArticleData.deletePage(subjectId, page);
    }

    void modifyPage(HttpSession session, String subjectId) throws Exception {
        int page = Integer.parseInt(getParam("pageNum_" + getId())) + 1;
        Hashtable h = ArticleData.getPageContent(subjectId, page);
        context.put("pageContent", h);
        context.put("pageNum", new Integer(page));
    }

    void updatePage(HttpSession session) throws Exception {
        String text = getParam("message");
        String pageId = getParam("page_id_" + getId());
        ArticleData.updatePage(pageId, text);
    }
}
