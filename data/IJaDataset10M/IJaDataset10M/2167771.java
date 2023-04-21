package valoskmdev.controller;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import valoskmdev.bean.AppUser;
import valoskmdev.bean.Issue;
import valoskmdev.bean.Vehicle;
import valoskmdev.dao.IssueDao;
import valoskmdev.dao.UserDao;
import valoskmdev.dao.VehicleDao;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.servlet.PMF;

public class VehicleServlet extends HttpServlet {

    private static final long serialVersionUID = -2097615788755325020L;

    private static final Logger log = Logger.getLogger(VehicleServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            AppUser currentUser = UserDao.getUser(pm, user);
            if (currentUser == null) {
                currentUser = initDatabase(pm, user);
            }
            request.setAttribute("appUser", currentUser);
            request.getRequestDispatcher("/WEB-INF/jsp/vehicles.jsp").forward(request, response);
        } finally {
            if (pm != null) {
                pm.close();
            }
        }
    }

    private AppUser initDatabase(PersistenceManager pm, User user) {
        log.info("init DB");
        final Date date = new Date();
        final AppUser appUser = new AppUser();
        appUser.setUser(user);
        appUser.setTimeCreated(date);
        appUser.setTimeLastModified(date);
        appUser.setFirstName("Joe");
        appUser.setLastName("Example");
        appUser.setUserName("exjoe");
        appUser.setRoleName("USER");
        UserDao.insertNew(pm, appUser);
        log.info("appUser: " + appUser.getId());
        final Vehicle vehicle = new Vehicle();
        vehicle.setAppUser(appUser);
        vehicle.setBrand("HONDA");
        vehicle.setType("CIVIC");
        vehicle.setVin("11111111111111111");
        vehicle.setRegNumber("AAA-111");
        vehicle.setColor("white");
        vehicle.setTimeCreated(date);
        vehicle.setTimeLastModified(date);
        vehicle.setDescription(new Text("Le�r�s"));
        vehicle.setDisabled(false);
        vehicle.setPublic(true);
        VehicleDao.insertNew(pm, appUser, vehicle);
        log.info("vehicle: " + vehicle.getId());
        final Issue issue = new Issue();
        issue.setIssueDate(new Date());
        issue.setTimeCreated(date);
        issue.setTimeLastModified(date);
        issue.setKilometer(10000);
        issue.setType("BUY");
        issue.setDescription(new Text("V�tel"));
        issue.setVehicle(vehicle);
        issue.setDisabled(false);
        issue.setPublic(true);
        IssueDao.insertNew(pm, vehicle, issue);
        log.info("issue: " + issue.getId());
        final Issue serviceIssue = new Issue();
        serviceIssue.setIssueDate(new Date());
        serviceIssue.setTimeCreated(date);
        serviceIssue.setTimeLastModified(date);
        serviceIssue.setKilometer(20000);
        serviceIssue.setType("SERVICE");
        serviceIssue.setDescription(new Text("Olajcsere"));
        serviceIssue.setVehicle(vehicle);
        serviceIssue.setDisabled(false);
        serviceIssue.setPublic(true);
        IssueDao.insertNew(pm, vehicle, serviceIssue);
        log.info("serviceIssue: " + serviceIssue.getId());
        return appUser;
    }
}
