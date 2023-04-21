package org.kablink.teaming.servlet.forum;

import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import org.kablink.teaming.ObjectKeys;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.domain.FolderEntry;
import org.kablink.teaming.domain.User;
import org.kablink.teaming.ical.util.ICalUtils;
import org.kablink.teaming.module.mail.MailModule;
import org.kablink.teaming.util.XmlFileUtil;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.servlet.SAbstractController;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

public class ViewICalController extends SAbstractController {

    private MailModule mailModule;

    @SuppressWarnings("unchecked")
    protected ModelAndView handleRequestAfterValidation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long binderId = new Long(ServletRequestUtils.getRequiredLongParameter(request, WebKeys.URL_BINDER_ID));
        Long entryId = new Long(ServletRequestUtils.getRequiredStringParameter(request, WebKeys.URL_ENTRY_ID));
        Map folderEntries = getFolderModule().getEntryTree(binderId, entryId);
        FolderEntry entry = (FolderEntry) folderEntries.get(ObjectKeys.FOLDER_ENTRY);
        User user = (User) request.getUserPrincipal();
        if (user == null) {
            HttpSession ses = request.getSession(false);
            if (ses != null) {
                user = (User) ses.getAttribute(WebKeys.USER_PRINCIPAL);
                if (user == null) {
                    user = RequestContextHolder.getRequestContext().getUser();
                }
            } else {
                throw new ServletException("No session in place - Illegal request sequence.");
            }
        }
        response.resetBuffer();
        response.setContentType(MailModule.CONTENT_TYPE_CALENDAR + "; charset=" + XmlFileUtil.FILE_ENCODING);
        response.setHeader("Cache-Control", "private");
        CalendarOutputter calendarOutputter = ICalUtils.getCalendarOutputter();
        Calendar calendar = getIcalModule().generate(entry, entry.getEvents(), mailModule.getMailProperty(RequestContextHolder.getRequestContext().getZoneName(), MailModule.Property.DEFAULT_TIMEZONE));
        calendarOutputter.output(calendar, response.getWriter());
        response.flushBuffer();
        return null;
    }

    public MailModule getMailModule() {
        return mailModule;
    }

    public void setMailModule(MailModule mailModule) {
        this.mailModule = mailModule;
    }
}
