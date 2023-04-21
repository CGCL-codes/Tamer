package net.sourceforge.vplace;

import net.sourceforge.vplace.authentication.UserAuthentication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.*;
import java.io.*;
import java.text.DateFormat;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * DOCUMENT ME!
 *
 * @author Andrew Elcock
 * @version 1.0
 */
public class EditPlacement extends HttpServlet {

    static ResourceBundle settings = ResourceBundle.getBundle("vpAuth");

    static Log logging = LogFactory.getLog(EditPlacement.class);

    DateFormat formatter = DateFormat.getInstance();

    /**
   * DOCUMENT ME!
   *
   * @param req DOCUMENT ME!
   * @param res DOCUMENT ME!
   *
   * @throws IOException DOCUMENT ME!
   */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String placementName = req.getParameter("placement");
        logging.debug("loading EditPlacement page for placement " + placementName);
        if ((placementName == null) || placementName.equals("")) {
            VPHelpers.error(req, res, "You must choose a plcement from the list");
            return;
        }
        VPUser tutor = UserAuthentication.getUser(req);
        req.setAttribute("VPUser", tutor);
        VPPlacement placement = new VPPlacement(placementName, tutor.getUserName());
        req.setAttribute("placement", placement);
        String forward = "../edit-placement.jsp";
        String action = req.getParameter("action");
        if ((action != null) && action.equals("View Student Files")) {
            forward = "../list-placement-files.jsp";
            Hashtable phases = new Hashtable();
            File[] allStudents = new File(settings.getString("vp.datadir") + "/students/").listFiles();
            for (int i = 0; i < allStudents.length; i++) {
                File placementStore = new File(allStudents[i], placementName + "/" + tutor.getUserName());
                if (placementStore.exists()) {
                    File[] allSubmits = placementStore.listFiles();
                    for (int j = 0; j < allSubmits.length; j++) {
                        Hashtable students = (Hashtable) phases.get(allSubmits[j].getName());
                        if (students == null) {
                            students = new Hashtable();
                            phases.put(allSubmits[j].getName(), students);
                        }
                        Vector hisFiles = new Vector();
                        int end = 1;
                        File[] files = allSubmits[j].listFiles();
                        for (int y = 0; (y < end) && (y < files.length); y++) {
                            File file = files[y];
                            String[] thisFile = new String[2];
                            try {
                                if (!file.getName().endsWith(".text")) {
                                    end++;
                                    continue;
                                }
                                if (allSubmits[j].getName().equals("forms")) {
                                    end++;
                                }
                                thisFile[0] = file.getName().substring(0, file.getName().length() - 5);
                                BufferedReader description = new BufferedReader(new FileReader(file));
                                thisFile[1] = description.readLine();
                            } catch (ArrayIndexOutOfBoundsException bounds) {
                                thisFile[0] = files[0].getName();
                                thisFile[1] = "Missing description";
                            }
                            hisFiles.add(thisFile);
                        }
                        students.put(allStudents[i].getName(), hisFiles);
                    }
                }
            }
            req.setAttribute("vp.placement.filelist", phases);
        }
        RequestDispatcher rd = req.getRequestDispatcher(forward);
        logging.debug("forwarding to " + forward);
        try {
            rd.forward(req, res);
        } catch (ServletException e) {
            logging.error("Could not forward to " + forward);
            VPHelpers.error(req, res, "Error forwarding to: " + forward + ".");
            return;
        }
    }

    /**
   * Saves the placement
   *
   * @param req DOCUMENT ME!
   * @param res DOCUMENT ME!
   *
   * @throws IOException DOCUMENT ME!
   */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String placementName = req.getParameter("placement");
        logging.debug("Saving placement " + placementName);
        if ((placementName == null) || placementName.equals("")) {
            VPHelpers.error(req, res, "No plcement selected for editing");
            return;
        }
        VPUser tutor = UserAuthentication.getUser(req);
        req.setAttribute("VPUser", tutor);
        VPPlacement placement = new VPPlacement(placementName, tutor.getUserName());
        String placementTitle = req.getParameter("placementName");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");
        String password = placement.getPassword();
        if ((password1 != null) && (password2 != null) && !password1.equals("")) {
            if (!password1.equals(password2)) {
                VPHelpers.error(req, res, "Passwords do not match");
                return;
            } else {
                password = password1;
            }
        }
        String companyName = req.getParameter("companyName");
        String companySite = req.getParameter("companyWebsite");
        Element root = new Element("placement");
        root.setAttribute("password", password);
        root.setAttribute("company", companyName);
        root.setAttribute("companysite", companySite);
        root.setText(placementTitle);
        int phaseCount = Integer.parseInt(req.getParameter("phaseCount"));
        for (int i = 0; i < phaseCount; i++) {
            Element node = new Element("phase");
            node.setText(req.getParameter("phaseTitle" + i));
            Calendar start = Calendar.getInstance();
            boolean showStart = false;
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            int startDay = Integer.parseInt(req.getParameter("startDay" + i));
            if ((startDay >= 1) && (startDay <= 31)) {
                start.set(Calendar.DAY_OF_MONTH, startDay);
                showStart = true;
            }
            int startMonth = Integer.parseInt(req.getParameter("startMonth" + i));
            if ((startMonth >= 0) && (startMonth <= 11)) {
                start.set(Calendar.MONTH, startMonth);
                showStart = true;
            }
            int startYear = Integer.parseInt(req.getParameter("startYear" + i));
            if (startYear >= 0) {
                start.set(Calendar.YEAR, startYear);
                showStart = true;
            }
            if (showStart) {
                node.setAttribute("start", formatter.format(start.getTime()));
            } else {
                node.setAttribute("start", "");
            }
            node.setAttribute("length", req.getParameter("phaseLength" + i));
            Calendar end = Calendar.getInstance();
            boolean showEnd = false;
            end.set(Calendar.HOUR_OF_DAY, 0);
            end.set(Calendar.MINUTE, 0);
            int endDay = Integer.parseInt(req.getParameter("endDay" + i));
            if ((endDay >= 1) && (endDay <= 31)) {
                end.set(Calendar.DAY_OF_MONTH, endDay);
                showEnd = true;
            }
            int endMonth = Integer.parseInt(req.getParameter("endMonth" + i));
            if ((endMonth >= 0) && (endMonth <= 11)) {
                end.set(Calendar.MONTH, endMonth);
                showEnd = true;
            }
            int endYear = Integer.parseInt(req.getParameter("endYear" + i));
            if (endYear >= 0) {
                end.set(Calendar.YEAR, endYear);
                showEnd = true;
            }
            if (showEnd) {
                node.setAttribute("end", formatter.format(end.getTime()));
            } else {
                node.setAttribute("end", "");
            }
            root.addContent(node);
        }
        int roleCount = Integer.parseInt(req.getParameter("roleCount"));
        Element roles = new Element("roles");
        for (int i = 0; i < roleCount; i++) {
            Element node = new Element("role");
            node.setAttribute("email", req.getParameter("roleEmail" + i));
            node.setText(req.getParameter("roleName" + i));
            roles.addContent(node);
        }
        Document doc = new Document(root);
        Document roledoc = new Document(roles);
        if (placement.updateAndSave(doc) && placement.updateAndSaveRoles(roledoc)) {
            logging.debug("Saved changes to placement " + placementName);
        } else {
            logging.error("failed to save changes to placement " + placementName);
            VPHelpers.error(req, res, "Could not save changes to placement " + placementName);
            return;
        }
        String forward = "../edit-placement-done.jsp";
        RequestDispatcher rd = req.getRequestDispatcher(forward);
        logging.debug("forwarding to " + forward);
        try {
            rd.forward(req, res);
        } catch (ServletException e) {
            logging.error("Could not forward to " + forward);
            VPHelpers.error(req, res, "Error forwarding to: " + forward + ".");
            return;
        }
    }
}
