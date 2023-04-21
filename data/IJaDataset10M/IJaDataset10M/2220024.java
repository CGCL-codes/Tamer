package lebah.app.content;

import java.util.*;
import javax.servlet.http.HttpSession;
import org.apache.velocity.Template;

public class JournalEntryModule extends lebah.portal.velocity.VTemplate {

    private static String[] month_name = { "January", "February", "March", "April", "May", "Jun", "July", "August", "September", "October", "November", "December" };

    private static String[] day_name = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    private static String[] hour_name = { "12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM" };

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/article/content.vm";
        String userId = (String) session.getAttribute("_portal_login");
        String subjectId = userId;
        context.put("formname", getId());
        String isLogin = (String) session.getAttribute("_portal_islogin");
        context.put("month_name", month_name);
        Calendar now = new GregorianCalendar();
        now.setTime(new java.util.Date());
        String now_day = "" + now.get(Calendar.DAY_OF_MONTH);
        int now_month = now.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR);
        context.put("year", new Integer(year));
        String submit = getParam("command");
        String formId = getParam("form_id");
        if ("".equals(submit)) {
            submit = getParam("command_" + getId());
            if ("".equals(submit)) submit = "entries_list";
        }
        System.out.println(submit);
        if ("entries_list".equals(submit)) {
            Vector entries = ArticleData.getEntries(userId);
            context.put("entries", entries);
            template_name = "vtl/article/entries.vm";
        } else if ("new_entry".equals(submit)) {
            template_name = "vtl/article/new_entry.vm";
        } else if ("add_entry".equals(submit)) {
            addEntry(userId);
            Vector entries = ArticleData.getEntries(userId);
            context.put("entries", entries);
            template_name = "vtl/article/entries.vm";
        } else if ("view_entry".equals(submit)) {
            subjectId = getParam("entry_id");
            session.setAttribute("subjectId", subjectId);
            int pageNum = !"".equals(getParam("pageNum_" + getId())) ? Integer.parseInt(getParam("pageNum_" + getId())) : 0;
            Article p = ArticleData.getArticle(subjectId);
            context.put("portfolio", p);
            context.put("pageNum", new Integer(pageNum));
            context.put("allowPost", new Boolean(true));
            context.put("allowUpdate", new Boolean(true));
            context.put("allowDelete", new Boolean(true));
            context.put("isModerator", new Boolean(true));
        } else {
            subjectId = (String) session.getAttribute("subjectId");
            System.out.println(subjectId);
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
            int pageNum = !"".equals(getParam("pageNum_" + getId())) ? Integer.parseInt(getParam("pageNum_" + getId())) : 0;
            Article p = ArticleData.getArticle(subjectId);
            context.put("portfolio", p);
            context.put("pageNum", new Integer(pageNum));
            context.put("allowPost", new Boolean(true));
            context.put("allowUpdate", new Boolean(true));
            context.put("allowDelete", new Boolean(true));
            context.put("isModerator", new Boolean(true));
        }
        Template template = engine.getTemplate(template_name);
        return template;
    }

    void addEntry(String catId) throws Exception {
        String title = getParam("title");
        Entry e = new Entry();
        e.setTitle(title);
        e.setCategory(catId);
        ArticleData.addEntry(e);
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
