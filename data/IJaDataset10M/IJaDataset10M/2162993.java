package com.projectmanagement;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.projectmanagement.helpers.hibernate.HibernateUtil;
import com.projectmanagement.view.Login;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class PMApplication extends Application implements HttpServletRequestListener {

    private static ThreadLocal<PMApplication> threadLocal = new ThreadLocal<PMApplication>();

    /**
	 * Application entry point
	 */
    @Override
    public void init() {
        setInstance(this);
        setTheme("projectmanagement");
        HibernateUtil.getInstance();
        Login login = new Login();
        setView(login);
    }

    /**
	 * Sets the main view.
	 * 
	 * @param window
	 */
    public void setView(Window window) {
        this.removeWindow(getMainWindow());
        setMainWindow(window);
    }

    /**
	 * Sets the language of the application and calls
	 * {@link ContestApplication#resetApplication()}<br/>
	 * This way all widgets will be regenerated with the new language
	 * 
	 * @param language
	 */
    public void setLanguage(String language) {
        if (language == null) return;
        setLocale(new Locale(language));
    }

    public static PMApplication getInstance() {
        return threadLocal.get();
    }

    public static void setInstance(PMApplication application) {
        if (getInstance() == null) {
            threadLocal.set(application);
        }
    }

    @Override
    public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
        PMApplication.setInstance(this);
    }

    @Override
    public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
        threadLocal.remove();
    }
}
