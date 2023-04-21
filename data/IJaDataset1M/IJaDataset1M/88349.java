package com.lb.trac.controller;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import com.lb.trac.pojo.Utenti;
import com.lb.trac.service.SearchService;
import com.lb.trac.service.UserNotFoundException;
import com.lb.trac.util.TracSetupUtil;

public class LoginController extends SimpleFormController {

    private SearchService searchService;

    private VelocityConfigurer velocityConfigurer;

    private Properties prop;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        try {
            Utenti u = (Utenti) command;
            String encPasswd = new TracSetupUtil().md5(u.getUsername().toUpperCase() + u.getPassword());
            Utenti usr = getSearchService().findUtenteByUsername(u.getUsername().toUpperCase());
            if (usr == null) {
                System.out.println("utente inesistente");
                return null;
            }
            if (!usr.getPassword().equals(encPasswd)) {
                throw new UserNotFoundException("La password non e' corretta");
            }
            VelocityContext vc = new VelocityContext();
            VelocityEngine ve = getVelocityConfigurer().getVelocityEngine();
            vc.put("itemsPerPage", prop.getProperty("itemsPerPage"));
            Template t = ve.getTemplate("jsTable.vm", "UTF-8");
            StringWriter w = new StringWriter();
            t.merge(vc, w);
            JSONArray json = new JSONArray();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("result", true);
            map.put("method", w.toString());
            json.put(map);
            json.write(response.getWriter());
            return null;
        } catch (Exception e) {
            JSONArray a = new JSONArray();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("result", false);
            List<String> messages = new ArrayList<String>();
            messages.add(e.getMessage());
            map.put("messages", messages);
            a.put(map);
            a.write(response.getWriter());
            return null;
        }
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel) throws Exception {
        return super.showForm(request, response, errors, controlModel);
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new Utenti();
    }

    @Override
    protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
        super.onBind(request, command, errors);
    }

    public VelocityConfigurer getVelocityConfigurer() {
        return velocityConfigurer;
    }

    public void setVelocityConfigurer(VelocityConfigurer velocityConfigurer) {
        this.velocityConfigurer = velocityConfigurer;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }
}
