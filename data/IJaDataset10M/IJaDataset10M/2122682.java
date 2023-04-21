package net.wgbv.photov.action;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.wgbv.photov.dam.ConnectionFactory;
import net.wgbv.photov.dam.OtherFactory;
import net.wgbv.photov.dam.PhotoFactory;
import net.wgbv.photov.form.CountryForm;
import net.wgbv.photov.objects.User;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 07-20-2004
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/country" name="countryForm" input="/form/country.jsp"
 *                validate="true"
 */
public class CountryAction extends Action {

    /**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Logger l = Logger.getLogger(CountryAction.class);
        CountryForm countryForm = (CountryForm) form;
        String strForward = new String(Constants.COUNTRY_EDIT_FORWARD);
        Connection conn = null;
        User user = null;
        try {
            conn = ConnectionFactory.getConnection();
            if (request.getSession().getAttribute(Constants.USER_KEY) != null) {
                user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            }
            if ((user != null) && (conn != null) && (user.getCanUpdate())) {
                if ((countryForm.getCountryName() == null) || (countryForm.getCountryAbbr() == null)) {
                    OtherFactory.populateCountryForm(countryForm, conn);
                } else if ((request.getParameter(Constants.ACTION) != null) && (request.getParameter(Constants.ACTION).equalsIgnoreCase(Constants.ACTION_CREATE))) {
                    countryForm.reset(mapping, request);
                } else {
                    OtherFactory.setCountryForm(countryForm, conn);
                }
            } else {
                strForward = Constants.NO_USER_FORWARD;
            }
        } catch (SQLException sqle) {
            l.error(" SQL Exception in CountryAction opening Connection ");
            l.error(sqle);
        } finally {
            PhotoFactory.closeConn(conn);
        }
        request.getSession().setAttribute(Constants.USER_KEY, user);
        return (mapping.findForward(strForward));
    }
}
