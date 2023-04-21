package locmanager.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import locmanager.beans.ActivityBean;
import locmanager.beans.LocationBean;
import locmanager.persistence.DerbyPersistence;

/**
 * Check Location Schedule Controller.
 * 
 * @author Jordi Vilaplana Mayoral <jvilaplana@alumnes.udl.cat>
 * @version 1.0
 */
public class CheckLocationScheduleServlet extends HttpServlet {

    /** 
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        DerbyPersistence db = new DerbyPersistence();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        List<ActivityBean> activities = null;
        int duration = Integer.parseInt((String) request.getParameter("duration"));
        int locationId = Integer.parseInt((String) request.getParameter("locationId"));
        try {
            startDate = formater.parse((String) request.getParameter("startDate"));
        } catch (Exception ex) {
        }
        LocationBean location = db.getLocation(locationId);
        activities = db.getActivitiesByLocation(location, startDate);
        int totalDuration = duration;
        for (ActivityBean activity : activities) {
            totalDuration += activity.getDuration();
        }
        String resu = "";
        Date result = startDate;
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(startDate);
        while (totalDuration > 8) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            activities = db.getActivitiesByLocation(location, calendar.getTime());
            totalDuration = duration;
            for (ActivityBean activity : activities) {
                totalDuration += activity.getDuration();
            }
            result = calendar.getTime();
            resu = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        }
        try {
            out.print(resu);
        } finally {
            out.close();
        }
    }

    /** 
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
   * Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
