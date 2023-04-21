package skylight1.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class StatisticsServlet extends HttpServlet {

    PersistenceManager pm = PMF.getPersistenceManager();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        OutputStream resp = response.getOutputStream();
        List<PersistingException> errorsFound = (List<PersistingException>) pm.newQuery(PersistingException.class).execute();
        resp.write("Exceptions:<br>".getBytes());
        resp.write(exceptionNamePieTag(errorsFound, 800, 300).getBytes());
        resp.write("<br>".getBytes());
        resp.write("Devices:<br>".getBytes());
        resp.write(devicePieTag(errorsFound, 800, 300).getBytes());
        resp.close();
    }

    private String exceptionNamePieTag(List<PersistingException> errors, int width, int height) {
        if (errors.size() == 0) return "#";
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (PersistingException ex : errors) {
            String excType = ex.getExceptionName();
            if (counts.containsKey(excType)) counts.put(excType, counts.get(excType) + 1); else counts.put(excType, 1);
        }
        return pieChart(counts, width, height);
    }

    private String devicePieTag(List<PersistingException> errors, int width, int height) {
        if (errors.size() == 0) return "#";
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (PersistingException ex : errors) {
            String excType = ex.getDevice();
            if (counts.containsKey(excType)) counts.put(excType, counts.get(excType) + 1); else counts.put(excType, 1);
        }
        return pieChart(counts, width, height);
    }

    private String pieChart(Map<String, Integer> data, int width, int height) {
        String vals = "";
        String names = "";
        for (Entry<String, Integer> entry : data.entrySet()) {
            vals += entry.getValue() + ",";
            names += entry.getKey() + "|";
        }
        vals = vals.substring(0, vals.length() - 1);
        names = names.substring(0, names.length() - 1);
        StringBuilder url = new StringBuilder();
        url.append("<img src=http://chart.apis.google.com/chart?cht=p&chd=t:");
        url.append(vals);
        url.append("&chs=");
        url.append(width);
        url.append("x");
        url.append(height);
        url.append("&chl=");
        url.append(names);
        url.append(">");
        return url.toString();
    }
}
