package com.alianzamedica.controllers;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.objectsearch.sqlsearch.ObjectSearch;
import org.w3c.dom.Document;
import com.alianzamedica.businessobject.Doctor;
import com.alianzamedica.tools.Enviroment;
import com.alianzamedica.view.LoginForm;

/**
 * @author Carlos
 * 
 */
public class LoginAction extends Action {

    @SuppressWarnings("unchecked")
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Enviroment env = Enviroment.getInstance();
        Document doc = env.getDocument();
        LoginForm loginForm = (LoginForm) form;
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();
        Doctor d = new Doctor();
        d.setEmail(email);
        d.setPassword(password);
        ObjectSearch search = new ObjectSearch(doc, "com.alianzamedica.connection.ConnectionImpl");
        Iterator<Doctor> iterator = search.searchObjects(d).iterator();
        Doctor doctor = null;
        while (iterator.hasNext()) {
            doctor = (Doctor) iterator.next();
            session.setAttribute("doctor", doctor);
        }
        if (doctor == null) {
            return mapping.findForward("fail");
        } else {
            return mapping.findForward("success");
        }
    }
}
