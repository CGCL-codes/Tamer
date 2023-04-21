package in.espirit.tracer.action;

import in.espirit.tracer.database.dao.MilestoneDao;
import in.espirit.tracer.database.dao.ReportDao;
import in.espirit.tracer.util.DateUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/reports/{type}/{milestone}")
public class ReportsActionBean extends BaseActionBean {

    private static final String URL = "/WEB-INF/jsp/reports/burndown.jsp";

    private static final String VELOCITY = "/WEB-INF/jsp/reports/velocity.jsp";

    private static final String EFFICIENCY = "/WEB-INF/jsp/reports/efficiency.jsp";

    private String type;

    private String milestone;

    @DefaultHandler
    public Resolution open() throws Exception {
        if (type == null) {
            type = "burndown";
        } else if (type.equalsIgnoreCase("velocity")) {
            type = "velocity";
            return new ForwardResolution(VELOCITY);
        } else if (type.equalsIgnoreCase("efficiency")) {
            return new ForwardResolution(EFFICIENCY);
        }
        if ((type.equalsIgnoreCase("burndown")) && (milestone == null)) {
            milestone = MilestoneDao.getCurrentMilestone();
        }
        return new ForwardResolution(URL);
    }

    public Resolution getData() throws Exception {
        String data = null;
        if (type.equalsIgnoreCase("burndown")) {
            data = getBurnDownJSON(milestone);
        } else {
            data = getVeolocityJSON();
        }
        return new StreamingResolution("text", data);
    }

    private String getBurnDownJSON(String milestone) {
        String line = null;
        try {
            if (milestone == null) {
                milestone = MilestoneDao.getCurrentMilestone();
            }
            String[] startEndDates = MilestoneDao.getMilestoneDates(milestone);
            ArrayList<String> period = DateUtils.getInBetweenDates(startEndDates[0], startEndDates[1]);
            line = "{" + "\"cols\":[" + "{\"id\":\"\",\"label\":\"x\",\"pattern\":\"\",\"type\":\"string\"}," + "{\"id\":\"\",\"label\":\"Ideal\",\"pattern\":\"\",\"type\":\"number\"}," + "{\"id\":\"\",\"label\":\"Actual\",\"pattern\":\"\",\"type\":\"number\"}," + "{\"id\":\"\",\"label\":\"Target\",\"pattern\":\"\",\"type\":\"number\"}" + "]," + "\"rows\":[";
            String rows = "";
            HashMap<String, Integer> actualData = ReportDao.getBurnDownData(milestone);
            for (int i = 0; i < period.size(); i++) {
                rows += "{\"c\":[{\"v\":\"" + period.get(i) + "\",\"f\":null},{\"v\":" + (100 - (100 * i / (period.size() - 1))) + ",\"f\":null},{\"v\":" + actualData.get(period.get(i)) + ",\"f\":null},{\"v\":4,\"f\":null}]},";
            }
            if (rows.length() > 0) {
                line += rows.substring(0, rows.length() - 1);
                line += "]," + "\"p\":null}";
            }
        } catch (ParseException e) {
            logger.fatal("Getting burndown detail json failed with parsing of start and end dates - " + e.getMessage());
        } catch (Exception e) {
            logger.fatal("Getting burndown detail json failed with error - " + e.getMessage());
        }
        return line;
    }

    private String getVeolocityJSON() {
        String bar = "";
        try {
            Map<String, Integer> result = ReportDao.getSprintStoryPoint();
            String rows = "";
            bar = "{" + "\"cols\": [" + "{\"id\":\"\",\"label\":\"Sprint\",\"pattern\":\"\",\"type\":\"string\"}," + "{\"id\":\"\",\"label\":\"Story points\",\"pattern\":\"\",\"type\":\"number\"}" + "]," + "\"rows\": [";
            for (Entry<String, Integer> entry : result.entrySet()) {
                rows += "{\"c\":[{\"v\":\"" + entry.getKey() + "\",\"f\":null},{\"v\":" + entry.getValue() + ",\"f\":null}]},";
            }
            if (rows.length() > 0) {
                bar += rows.substring(0, rows.length() - 1);
                bar += "]" + "}";
            }
        } catch (Exception e) {
            logger.fatal("Error in getting the spring story point");
        }
        return bar;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getMilestone() {
        return milestone;
    }
}
