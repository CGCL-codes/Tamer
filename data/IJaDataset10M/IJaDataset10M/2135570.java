package controllers.front.profile;

import controllers.BaseController;
import helpers.Validator;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;

/**
 *
 * @author Onno Valkering
 */
@WebServlet(name = "Modify Profile Details", urlPatterns = { "/profile/details/modify" })
public class ProfileDetailsModify extends BaseController {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initBase(request, response);
        if (mustHaveClearance(1, false)) {
            request.setAttribute("currentUser", currentUser);
            String stylesheets[] = { "/profile.css", "/tipsy/tipsy.css" };
            String javascript[] = { "/profile-validation.js", "/tipsy/tipsy.js" };
            String address = "/profile/profile_details_modify.jsp", title = "Profile";
            loader.setStylesheets(stylesheets);
            loader.setJavascripts(javascript);
            loader.loadFrontContent(address, title);
        }
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
        initBase(request, response);
        String[] inputFields = { "firstname", "lastname", "birthday", "gender", "country", "postalcode", "city", "streetname", "streetnumber" };
        boolean errorFlag = false;
        for (String input : inputFields) {
            if (request.getParameter(input) != null) {
                String tempField = request.getParameter(input).toString();
                if (tempField.trim().isEmpty()) {
                    errorFlag = true;
                }
            } else {
                errorFlag = true;
            }
        }
        if (errorFlag == true) {
            doGet(request, response);
        } else {
            String firstname = request.getParameter("firstname"), lastname = request.getParameter("lastname"), country = request.getParameter("country"), postalcode = request.getParameter("postalcode"), city = request.getParameter("city"), streetname = request.getParameter("streetname"), streetnumber = request.getParameter("streetnumber"), birthday = request.getParameter("birthday"), gender = request.getParameter("gender");
            char genderCode;
            if (gender.equals("male")) {
                genderCode = 'm';
            } else {
                genderCode = 'f';
            }
            Validator validator = new Validator();
            User tempUser = currentUser;
            tempUser.setFirstname(firstname);
            tempUser.setLastname(lastname);
            if (validator.validateBirthday(birthday) == true) {
                tempUser.setBirthday(birthday);
            }
            tempUser.setGender(genderCode);
            tempUser.setCountry(country);
            if (validator.validatePostalcode(postalcode, country) == true) {
                tempUser.setPostalcode(postalcode);
            }
            tempUser.setCity(city);
            tempUser.setStreetname(streetname);
            tempUser.setStreetnumber(streetnumber);
            userDao.save(tempUser);
            String[][] parameters = { { "modify", "success" } };
            loader.redirect("/profile/details", parameters);
        }
    }
}
