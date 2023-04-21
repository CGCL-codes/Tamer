package org.gaplan.glanet;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class FetcherServlet extends HttpServlet {

    private static ArrayList<UserPost> allPosts = new ArrayList<UserPost>();

    private static ArrayList<User> userList = new ArrayList<User>();

    private static String propertiesPath;

    private static final long serialVersionUID = 1807930740483845702L;

    private Timer timer = new Timer();

    private ContentGenerator contentGenerator;

    public void init() throws ServletException {
        message("Initializing FetcherServlet ...");
        propertiesPath = this.getServletContext().getRealPath("") + "/WEB-INF/glanet.xml";
        contentGenerator = new ContentGenerator();
        timer.scheduleAtFixedRate(contentGenerator, 20000, 5 * 60000);
        message("FetcherServlet initialized!");
        contentGenerator.setRuning(true);
    }

    public void destroy() {
        contentGenerator.setRuning(false);
        timer.cancel();
        message("Fetch Timer Cancelled!");
    }

    public static ArrayList<User> getUserList() {
        return userList;
    }

    public static void setUserList(ArrayList<User> users) {
        userList = users;
    }

    public static String getPropertiesPath() {
        return propertiesPath;
    }

    public static ArrayList<UserPost> getAllPosts() {
        return allPosts;
    }

    public static void setAllPosts(ArrayList<UserPost> aAllPosts) {
        allPosts = aAllPosts;
    }

    public static void message(String messageString) {
        System.out.println("[Glanet] - " + new Date() + " - " + messageString);
    }
}
