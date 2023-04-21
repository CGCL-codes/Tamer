package com.simoncat.actions;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import com.simoncat.beans.CalendarScreenOutput;
import com.simoncat.Constants;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import com.simoncat.net.HttpClient;
import com.simoncat.beans.XMLServers;
import com.simoncat.vo.Server;
import com.simoncat.calendar.WebCalendar;
import java.util.Calendar;
import com.simoncat.beans.XMLEvents;
import com.simoncat.vo.Event;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.Format;
import com.simoncat.XMLHandler;

public final class ScheduleAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CalendarScreenOutput fso;
        if (form != null) {
            DynaActionForm df = (DynaActionForm) form;
            String serName = request.getParameter("Submit");
            String date = request.getParameter("date");
            String form_ = request.getParameter("form");
            String value = request.getParameter("value");
            if (form != null && value != null) {
                XMLServers xmlserver = new XMLServers();
                Server server = xmlserver.loadServerByName(serName, servlet.getServletContext());
                XMLEvents xmlevents = new XMLEvents();
                if (form_.equals("new")) {
                    int hourint = new Integer(request.getParameter("hour")).intValue();
                    if (request.getParameter("ampm").equals("PM")) {
                        hourint = hourint + 12;
                        if (hourint == 24) hourint = 0;
                    }
                    String hour = "" + hourint + ":" + request.getParameter("minute");
                    String id = serName + value + hour;
                    String description = "";
                    String user = "";
                    String results = "";
                    Locale locale = new Locale("es", "ES");
                    Date date1 = new Date(value);
                    Format formatter = new SimpleDateFormat("MMMM_yyyy");
                    String folName = formatter.format(date1);
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateToKeep = formatter.format(date1);
                    String type = request.getParameter("type");
                    Event event = new Event(id, dateToKeep, hour, description, user, results, type);
                    xmlevents.addEvent(event, server.getFilePath(), serName, folName);
                    if (XMLHandler.addEvent(server, event)) {
                        System.out.println("programed event:" + event + " at Server:" + server);
                    }
                }
                if (form_.equals("revise")) {
                    String hour = request.getParameter("hour") + ":" + request.getParameter("minute");
                    String id = serName + value + hour;
                    String description = "";
                    String user = "";
                    String results = "";
                    String type = request.getParameter("type");
                    Event event = new Event(id, value, hour, description, user, results, type);
                    xmlevents.updEvent(id, event, server.getFilePath(), serName);
                }
                if (form_.equals("delete")) {
                    String id = "";
                    xmlevents.delEvent(id, server.getFilePath(), serName);
                }
            }
            if (date == null || date.trim().equals("")) {
                Calendar c = Calendar.getInstance();
                date = "" + (c.get(Calendar.MONTH) + 1);
                date = date + "/" + c.get(Calendar.DAY_OF_MONTH);
                date = date + "/" + c.get(Calendar.YEAR);
            }
            XMLServers xmlserver = new XMLServers();
            Server server = xmlserver.loadServerByName(serName, servlet.getServletContext());
            fso = new CalendarScreenOutput();
            fso.setPageTitle("Schedule Server War[Finished]");
            fso.setCalendarCode(new WebCalendar(date, serName, request.getRequestURI(), servlet.getServletContext().getRealPath("/") + "WEB-INF").getCode());
            fso.setMessage("Server Calendar for restarting");
        } else {
            fso = new CalendarScreenOutput();
            fso.setPageTitle("Schedule Server [NOT Finished]");
            fso.setMessage("Sorry, problem with submitted form!");
        }
        if (Constants.xmlOutputOnly) {
            request.setAttribute("content", Constants.xmlContentType);
        } else {
            request.setAttribute("content", Constants.htmlContentType);
            request.setAttribute("style", "confirmSchedule.xsl");
        }
        request.setAttribute("form", fso);
        return (mapping.findForward(Constants.XMLHandler));
    }
}
