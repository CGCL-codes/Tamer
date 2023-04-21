package org.academ.jabber.actions;

import com.opensymphony.xwork2.ActionSupport;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.academ.jabber.managers.HelpsManager;
import org.academ.jabber.entities.helps.Applications;
import org.academ.jabber.entities.helps.Helps;
import org.academ.jabber.entities.helps.HelpsSteps;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author moskvin
 */
@Controller("helpAction")
@Scope("request")
public class HelpAction extends ActionSupport {

    public String action = "help";

    private Map<String, String> urls = new LinkedHashMap<String, String>();

    private String subject;

    private Applications applications;

    private Helps helps;

    private List<HelpsSteps> helpsSteps;

    private HelpsManager helpsManager;

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String psi() {
        action = "help-psi";
        subject = "psi";
        urls.put("help-psidownload", "download.psi");
        urls.put("help-psilogin", "enter.from.psi");
        urls.put("help-psiconference", "enter.in.room.from.psi");
        urls.put("help-psiirc", "enter.in.irc.from.psi");
        return "help";
    }

    public String psilogin() {
        action = "help-psilogin";
        return "psi_login";
    }

    public String psiconference() {
        action = "help-psiconference";
        return "psi_conference";
    }

    public String psiirc() {
        action = "help-psiirc";
        if (helpsManager != null) {
            applications = helpsManager.getApplications().get(0);
            helps = helpsManager.getHelps(applications.getId()).get(2);
            helpsSteps = helpsManager.getSteps(helps.getId());
        }
        return "psi_irc";
    }

    public String psidownload() {
        action = "help-psidownload";
        return "psi_download";
    }

    public String pidgin() {
        action = "help-pidgin";
        subject = "pidgin";
        urls.put("help-pidgindownload", "download.pidgin");
        urls.put("help-pidginlogin", "enter.from.pidgin");
        urls.put("help-pidginconference", "enter.in.room.from.pidgin");
        return "help";
    }

    public String pidginlogin() {
        action = "help-pidginlogin";
        return "pidgin_login";
    }

    public String pidginconference() {
        action = "help-pidginconference";
        return "pidgin_conference";
    }

    public String pidgindownload() {
        action = "help-pidgindownload";
        return "pidgin_download";
    }

    public String qutim() {
        action = "help-qutim";
        subject = "qutim";
        urls.put("help-qutimlogin", "enter.from.qutim");
        urls.put("help-qutimconference", "enter.in.room.from.qutim");
        return "help";
    }

    public String qutimlogin() {
        action = "help-qutimlogin";
        return "qutim_login";
    }

    public String qutimconference() {
        action = "help-qutimconference";
        return "qutim_conference";
    }

    public String gajim() {
        action = "help-gajim";
        subject = "gajim";
        urls.put("help-gajimlogin", "enter.from.gajim");
        urls.put("help-gajimconference", "enter.in.room.from.gajim");
        return "help";
    }

    public String gajimlogin() {
        action = "help-gajimlogin";
        return "gajim_login";
    }

    public String gajimconference() {
        action = "help-gajimconference";
        return "gajim_conference";
    }

    public Map<String, String> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<HelpsSteps> getHelpsSteps() {
        return helpsSteps;
    }

    public void setHelpsSteps(List<HelpsSteps> helpsSteps) {
        this.helpsSteps = helpsSteps;
    }

    public HelpsManager getHelpsManager() {
        return helpsManager;
    }

    public void setHelpsManager(HelpsManager helpsManager) {
        this.helpsManager = helpsManager;
    }
}
