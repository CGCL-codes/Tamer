package org.gbif.biogarage.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.gbif.biogarage.model.User;
import org.gbif.biogarage.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 *
 */
public abstract class BaseAction extends ActionSupport implements Action, SessionAware {

    protected final Log log = LogFactory.getLog(getClass());

    public static final String NOT_FOUND = "404";

    @Autowired
    protected AppConfig cfg;

    protected Map<String, Object> session;

    protected String currentMenu = "home";

    protected Long id;

    public String execute() {
        return SUCCESS;
    }

    public String getBase() {
        return cfg.getBaseURL();
    }

    public String getCurrentMenu() {
        return currentMenu;
    }

    public User getUser() {
        return (User) session.get(AppConfig.SESSION_USER);
    }

    protected List<String> splitMultiValueParameter(String value) {
        if (value == null) {
            return new ArrayList<String>();
        }
        String[] paras = StringUtils.split(value, ", ");
        return Arrays.asList(paras);
    }

    protected Map<String, String> translateI18nMap(Map<String, String> map) {
        for (String key : map.keySet()) {
            String i18Key = map.get(key);
            map.put(key, getText(i18Key));
        }
        return map;
    }

    public Long getID() {
        return this.id;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
