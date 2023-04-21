package com.evaserver.rof.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.LastModified;
import com.evaserver.rof.WindowManager;
import com.evaserver.rof.script.Browser;

/**
 *
 *
 * @author Max Antoni
 * @version $Revision: 46 $
 */
public abstract class AbstractScriptController implements Controller, LastModified, InitializingBean {

    private static final String ENCODING = "UTF-8";

    private static final String CONTENT_TYPE = "text/javascript; charset=UTF-8";

    private static final String HEADER_USER_AGENT = "user-agent";

    /**
	 * the window manager.
	 */
    private WindowManager windowManager;

    /**
	 * the last modified timestamp is always the time this controller has been
	 * created. The content of the core ROF script cannot change without
	 * application reload.
	 */
    private long lastModified = System.currentTimeMillis();

    /**
	 * @return the window manager.
	 */
    public WindowManager getWindowManager() {
        return windowManager;
    }

    /**
	 * @param inWindowManager the window manager.
	 */
    public void setWindowManager(WindowManager inWindowManager) {
        windowManager = inWindowManager;
    }

    public final ModelAndView handleRequest(HttpServletRequest inRequest, HttpServletResponse inResponse) throws Exception {
        inResponse.setContentType(CONTENT_TYPE);
        String userAgent = inRequest.getHeader(HEADER_USER_AGENT);
        Browser browser = getWindowManager().getPluginForUserAgent(userAgent);
        String script = getScript(inRequest, browser);
        inResponse.getOutputStream().write(script.getBytes(ENCODING));
        return null;
    }

    /**
	 * returns the script to provide for the given request and browser.
	 *
	 * @param inRequest the request.
	 * @param inBrowser the browser.
	 * @return the script.
	 * @throws Exception if an exception occurs.
	 */
    protected abstract String getScript(HttpServletRequest inRequest, Browser inBrowser) throws Exception;

    public long getLastModified(HttpServletRequest inRequest) {
        return lastModified;
    }

    public void afterPropertiesSet() throws Exception {
        if (windowManager == null) {
            throw new NullPointerException("Property windowManager must be set.");
        }
    }
}
