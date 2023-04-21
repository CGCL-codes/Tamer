package gu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.List;

public class nship extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String s = "", scn = "";
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String thisURL = req.getRequestURI();
        if (user != null) {
            scn = req.getUserPrincipal().getName();
            s = shta.rff("new_shipper_templ.htm");
            s = s.replaceAll("<!- customer_name ->", scn);
        } else {
            user = new User("test@quicklydone.com", "quicklydone.com", "test qq");
            s = shta.rff("new_shipper_templ.htm");
            s = s.replaceAll("<!- customer_name ->", scn);
        }
        Cookie userCookie = new Cookie("user", "qqqqq...uid1234");
        resp.addCookie(userCookie);
        out.println(s);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String s = "", ss = "", sq = "";
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String customer_name = user.getNickname();
        String company_name = req.getParameter("TextBox1");
        String address1 = req.getParameter("TextBox2");
        String address2 = req.getParameter("TextBox3");
        String city = req.getParameter("TextBox4");
        String prov_state = req.getParameter("DropDownList2");
        String postal_code = req.getParameter("TextBox6");
        String country = req.getParameter("DropDownList1");
        String contact = req.getParameter("TextBox8");
        String phone = req.getParameter("TextBox9");
        s = "Customer: " + customer_name + "\r\nCompany name: " + company_name + "\r\nAddress: " + address1 + " " + address2 + "\r\nCity: " + city + "\r\nProv/State: " + prov_state + "\r\nPostal code: " + postal_code + "\r\nCountry: " + country + "\r\nContact: " + contact + "\r\nPhone: " + phone;
        s = shta.send_mail("weborder@quicklydone.com", "new shipper", s);
        out.println(customer_name);
    }

    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())) return (cookie.getValue());
        }
        return (defaultValue);
    }
}
