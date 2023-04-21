package org.osmius.webapp.action;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.osmius.model.OsmNSchedule;
import org.osmius.model.OsmNTimeschedule;
import org.osmius.service.OsmNScheduleManager;
import org.osmius.service.OsmNTimescheduleManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.*;

public class OsmNScheduleFormController extends BaseFormController {

    private OsmNScheduleManager osmNScheduleManager;

    private OsmNTimescheduleManager osmNTimescheduleManager;

    public void setOsmNScheduleManager(OsmNScheduleManager osmNScheduleManager) {
        this.osmNScheduleManager = osmNScheduleManager;
    }

    public void setOsmNTimescheduleManager(OsmNTimescheduleManager osmNTimescheduleManager) {
        this.osmNTimescheduleManager = osmNTimescheduleManager;
    }

    public OsmNScheduleFormController() {
        setSessionForm(true);
        setCommandName("osmNSchedule");
        setCommandClass(OsmNSchedule.class);
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String idnSchedule = request.getParameter("idnSchedule");
        String newSchedule = request.getParameter("newSchedule");
        OsmNSchedule osmNSchedule;
        if (newSchedule != null && !newSchedule.equals("") && !newSchedule.equals("null")) {
            osmNSchedule = new OsmNSchedule();
        } else {
            if (!StringUtils.isEmpty(idnSchedule)) {
                osmNSchedule = osmNScheduleManager.getOsmNSchedule(new Long(idnSchedule));
                if (osmNSchedule == null) {
                    saveMessage(request, this.getMessageSourceAccessor().getMessage("osmNSchedule.notexists"));
                    osmNSchedule = new OsmNSchedule();
                }
            } else {
                osmNSchedule = new OsmNSchedule();
            }
        }
        return osmNSchedule;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("---> 'onSubmit' method...");
        }
        String idnSchedule = request.getParameter("idnSchedule");
        String desSchedule = request.getParameter("desSchedule");
        if (desSchedule != null && !desSchedule.equals("")) {
            desSchedule = URLDecoder.decode(desSchedule, request.getCharacterEncoding() == null ? "utf-8" : request.getCharacterEncoding());
        }
        String working = request.getParameter("W");
        String festive = request.getParameter("PH");
        String days = request.getParameter("D");
        String from = request.getParameter("F");
        String to = request.getParameter("T");
        if (idnSchedule == null || idnSchedule.equals("") || idnSchedule.equals("0")) {
            OsmNSchedule osmNSchedule = new OsmNSchedule();
            osmNSchedule.setDesSchedule(desSchedule);
            osmNSchedule.setIndWorkingtime(working.equals("YES") ? 1 : 0);
            osmNSchedule.setIndExcludephdays(festive.equals("YES") ? 1 : 0);
            if (osmNSchedule.getIndWorkingtime() == 1) {
                boolean value = osmNScheduleManager.testWorkingtime();
                if (value) {
                    saveMessage(request, this.getMessageSourceAccessor().getMessage("osmNSchedule.workintTimeexist"));
                    ModelAndView model = new ModelAndView("redirect:/notifications/schedulesForm.html?newSchedule=true");
                    model.addObject("filter", "0");
                    return model;
                }
            }
            String[] arrDays = days.split(",");
            String[] arrFrom = from.split(",");
            String[] arrTo = to.split(",");
            Long identifier = osmNScheduleManager.saveOsmNSchedule(osmNSchedule, arrDays, arrFrom, arrTo);
            saveMessage(request, this.getMessageSourceAccessor().getMessage("osmNSchedule.added"));
        } else {
            String[] arrDays = days.split(",");
            String[] arrFrom = from.split(",");
            String[] arrTo = to.split(",");
            ((OsmNSchedule) command).setIndExcludephdays(festive.equals("YES") ? 1 : 0);
            ((OsmNSchedule) command).setDesSchedule(desSchedule);
            try {
                osmNScheduleManager.updateOsmNSchedule((OsmNSchedule) command);
                osmNTimescheduleManager.deleteTimeschedules(((OsmNSchedule) command).getIdnSchedule());
                Vector vTimeSch = new Vector();
                int cont = 1;
                for (int i = 0; i < arrDays.length; i++) {
                    String arrDay = arrDays[i];
                    String[] _days = arrDay.split("-");
                    for (int j = 0; j < _days.length; j++) {
                        String day = _days[j];
                        Date dateFrom = decodeDay(day, arrFrom[i]);
                        Date dateTo = decodeDay(day, arrTo[i]);
                        OsmNTimeschedule osmNTimeschedule = new OsmNTimeschedule();
                        osmNTimeschedule.setDtiIni(dateFrom);
                        osmNTimeschedule.setDtiFini(dateTo);
                        osmNTimeschedule.setOsmNSchedule((OsmNSchedule) command);
                        vTimeSch.add(osmNTimeschedule);
                        cont++;
                    }
                }
                OsmNTimeschedule[] osmNTimeschedules = new OsmNTimeschedule[vTimeSch.size()];
                vTimeSch.toArray(osmNTimeschedules);
                osmNTimescheduleManager.saveOsmNTimeschedules(osmNTimeschedules);
                saveMessage(request, this.getMessageSourceAccessor().getMessage("osmNSchedule.updated"));
            } catch (Exception e) {
                saveMessage(request, this.getMessageSourceAccessor().getMessage("osmNSchedule.notexists"));
                ModelAndView model = new ModelAndView("redirect:/notifications/osmNScheduleList.html");
                model.addObject("filter", "0");
                return model;
            }
            return showForm(request, response, errors);
        }
        ModelAndView model = new ModelAndView("redirect:/notifications/osmNScheduleList.html");
        model.addObject("filter", "0");
        return model;
    }

    private String toStringDay(int day) {
        String value = "";
        if (day == 1) {
            value = "MO";
        } else if (day == 2) {
            value = "TU";
        } else if (day == 3) {
            value = "WE";
        } else if (day == 4) {
            value = "TH";
        } else if (day == 5) {
            value = "FR";
        } else if (day == 6) {
            value = "SA";
        } else if (day == 7) {
            value = "SU";
        }
        return value;
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map model = new HashMap();
        String idnSchedule = request.getParameter("idnSchedule");
        String newSchedule = request.getParameter("newSchedule");
        List ranges = null;
        if (!(newSchedule != null && !newSchedule.equals("") && !newSchedule.equals("null"))) {
            if (idnSchedule != null && !idnSchedule.equals("") && !idnSchedule.equals("null")) {
                ranges = osmNTimescheduleManager.getOsmNTimeschedules(new Long(idnSchedule));
                HashMap hours = new HashMap();
                int working = 0;
                for (int i = 0; i < ranges.size(); i++) {
                    OsmNTimeschedule osmNTimeschedule = (OsmNTimeschedule) ranges.get(i);
                    working = osmNTimeschedule.getOsmNSchedule().getIndWorkingtime();
                    DateTime ini = new DateTime(osmNTimeschedule.getDtiIni().getTime());
                    DateTime fin = new DateTime(osmNTimeschedule.getDtiFini().getTime());
                    int dayIni = ini.getDayOfWeek();
                    String hIni = "", hFin = "";
                    if (ini.getHourOfDay() == 23 && ini.getMinuteOfHour() == 59) {
                        hIni = "24:00";
                    } else {
                        hIni = (ini.getHourOfDay() >= 10 ? ini.getHourOfDay() : "0" + ini.getHourOfDay()) + ":" + (ini.getMinuteOfHour() >= 10 ? ini.getMinuteOfHour() : "0" + ini.getMinuteOfHour());
                    }
                    if (fin.getHourOfDay() == 23 && fin.getMinuteOfHour() == 59) {
                        hFin = "24:00";
                    } else {
                        hFin = (fin.getHourOfDay() >= 10 ? fin.getHourOfDay() : "0" + fin.getHourOfDay()) + ":" + (fin.getMinuteOfHour() >= 10 ? fin.getMinuteOfHour() : "0" + fin.getMinuteOfHour());
                    }
                    Range range = (Range) hours.get(hIni + "-" + hFin);
                    if (range == null) {
                        range = new Range(hIni, hFin, toStringDay(dayIni));
                        hours.put(hIni + "-" + hFin, range);
                    } else {
                        range.setDays(range.getDays() + "-" + toStringDay(dayIni));
                        hours.remove(hIni + "-" + hFin);
                        hours.put(hIni + "-" + hFin, range);
                    }
                }
                Iterator it = hours.values().iterator();
                StringBuffer str = new StringBuffer("");
                String idnWorking = working == 1 ? "YES" : "NO";
                while (it.hasNext()) {
                    Range value = (Range) it.next();
                    str.append("{working:\"").append(idnWorking).append("\",days:\"").append(value.getDays()).append("\",from:\"").append(value.getIni()).append("\",to:\"").append(value.getFin()).append("\"},");
                }
                StringBuffer json = new StringBuffer(str.substring(0, str.length() - 1));
                model.put("ranges", json.toString());
            }
        }
        return model;
    }

    private Date decodeDay(String day, String time) {
        DateTime date = null;
        String[] _time = time.split(":");
        if (day.equals("MO")) {
            date = new DateTime(2000, 5, 1, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("TU")) {
            date = new DateTime(2000, 5, 2, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("WE")) {
            date = new DateTime(2000, 5, 3, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("TH")) {
            date = new DateTime(2000, 5, 4, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("FR")) {
            date = new DateTime(2000, 5, 5, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("SA")) {
            date = new DateTime(2000, 5, 6, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("SU")) {
            date = new DateTime(2000, 5, 7, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        }
        return date.toDate();
    }

    class Range {

        String ini;

        String fin;

        String days;

        public Range(String ini, String fin, String days) {
            this.ini = ini;
            this.fin = fin;
            this.days = days;
        }

        public String getIni() {
            return ini;
        }

        public void setIni(String ini) {
            this.ini = ini;
        }

        public String getFin() {
            return fin;
        }

        public void setFin(String fin) {
            this.fin = fin;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }
    }
}
