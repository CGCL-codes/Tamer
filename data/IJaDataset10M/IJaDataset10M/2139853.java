package com.sun.portal.rssportlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletSecurityException;
import javax.portlet.UnavailableException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSession;
import com.sun.syndication.io.FeedException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * This class implements the Rss portlet.
 *
 * The following is a design overview of the RSS portlet ...
 *
 * The entry points into the application are the view and edit JSPs.
 * These correspond to the portlet's view and edit modes, respectively.
 * The JSPs use the *Handler classes to prepare the *Bean classes for use
 * in the display logic. Control is passed to the JSPs in the do*() methods.
 * The only logic in this
 * class is the processing of portlet actions. This occurs for the "go"
 * form in the portlet's view mode, and the forms in the portlet's edit mode.
 *
 * The portlet's edit mode allows the user to modify a subset of the portlet
 * preferences to personalize the portlet.
 *
 */
public class RssPortlet extends GenericPortlet implements FormNames {

    private PortletContext portletContext;

    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        portletContext = config.getPortletContext();
    }

    /** Include "view" JSP. */
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        include(request, response, "/portlets/rss/view.jsp");
    }

    /** Include "edit" JSP. */
    public void doEdit(RenderRequest request, RenderResponse response) throws PortletException {
        include(request, response, "/portlets/rss/edit.jsp");
    }

    /** Include localized "help" JSP. */
    public void doHelp(RenderRequest request, RenderResponse response) throws PortletException {
        ResourceBundle bundle = ResourceBundle.getBundle("com.sun.portal.rssportlet.RssPortlet", request.getLocale());
        include(request, response, bundle.getString("help_jsp"));
    }

    /** Include a page. */
    private void include(RenderRequest request, RenderResponse response, String pageName) throws PortletException {
        response.setContentType(request.getResponseContentType());
        if (pageName == null || pageName.length() == 0) {
            throw new NullPointerException("null or empty page name");
        }
        try {
            PortletRequestDispatcher dispatcher = portletContext.getRequestDispatcher(pageName);
            dispatcher.include(request, response);
        } catch (IOException ioe) {
            throw new PortletException(ioe);
        }
    }

    /** Process actions from the view mode "go" form, and the edit mode forms. */
    public void processAction(ActionRequest request, ActionResponse response) throws UnavailableException, PortletSecurityException, PortletException, IOException {
        Resources resources = new Resources("com.sun.portal.rssportlet.RssPortlet", request.getLocale());
        AlertHandler ah = (AlertHandler) request.getPortletSession().getAttribute("alertHandler", PortletSession.PORTLET_SCOPE);
        SettingsBean readBean = new SettingsBean();
        SettingsHandler handler = new SettingsHandler();
        handler.setPortletConfig(getPortletConfig());
        handler.setPortletRequest(request);
        handler.setSettingsBean(readBean);
        SettingsBean writeBean = new SettingsBean();
        if (request.getParameter(SUBMIT_ADD) != null) {
            processEditAddAction(request, response, ah, resources, readBean, writeBean);
        } else if (request.getParameter(SUBMIT_GO) != null || request.getParameter(INPUT_SELECT_FEED) != null) {
            processGoAction(request, response, writeBean);
        } else if (request.getParameter(SUBMIT_CANCEL) != null) {
            processEditCancelAction(request, response);
        } else if (request.getParameter(SUBMIT_EDIT) != null) {
            processEditAction(request, response, ah, resources, readBean, writeBean);
        }
        handler.persistSettingsBean(writeBean);
    }

    @SuppressWarnings("unchecked")
    private void processEditAddAction(ActionRequest request, ActionResponse response, AlertHandler alertHandler, Resources resources, SettingsBean readBean, SettingsBean writeBean) {
        String url = request.getParameter(INPUT_ADD_FEED);
        try {
            FeedHelper.getInstance().getFeed(readBean, url);
            LinkedList feeds = readBean.getFeeds();
            feeds.add(url);
            writeBean.setFeeds(feeds);
            writeBean.setSelectedFeed(url);
        } catch (MalformedURLException mue) {
            alertHandler.setError(resources.get("invalid_url"), mue.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", mue);
        } catch (UnknownHostException uhe) {
            alertHandler.setError(resources.get("invalid_url"), uhe.getMessage());
        } catch (FileNotFoundException fnfe) {
            alertHandler.setError(resources.get("invalid_url"), fnfe.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", fnfe);
        } catch (IllegalArgumentException iae) {
            alertHandler.setError(resources.get("invalid_url"), iae.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", iae);
        } catch (FeedException fe) {
            alertHandler.setError(resources.get("invalid_url"), fe.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", fe);
        } catch (IOException ioe) {
            alertHandler.setError(resources.get("invalid_url"), ioe.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", ioe);
        }
    }

    private void processGoAction(ActionRequest request, ActionResponse response, SettingsBean writeBean) {
        String selectedFeed = request.getParameter(INPUT_SELECT_FEED);
        if (null != selectedFeed) {
            writeBean.setSelectedFeed(selectedFeed);
        }
    }

    private void processEditCancelAction(ActionRequest request, ActionResponse response) throws PortletModeException {
        response.setPortletMode(PortletMode.VIEW);
    }

    @SuppressWarnings("unchecked")
    private void processEditAction(ActionRequest request, ActionResponse response, AlertHandler alertHandler, Resources resources, SettingsBean readBean, SettingsBean writeBean) throws PortletModeException {
        String[] checkedFeeds = request.getParameterValues(INPUT_FEEDS);
        if (checkedFeeds == null) {
            writeBean.setFeeds(new LinkedList());
        } else {
            LinkedList feeds = new LinkedList(Arrays.asList(checkedFeeds));
            writeBean.setFeeds(feeds);
            String startFeed = request.getParameter(INPUT_START_FEED);
            if (startFeed != null && feeds.contains(startFeed)) {
                writeBean.setStartFeed(startFeed);
            }
            if (readBean.getSelectedFeed() != null && !feeds.contains(readBean.getSelectedFeed())) {
                String selectedFeed = writeBean.getStartFeed();
                writeBean.setSelectedFeed(selectedFeed);
            }
            if (readBean.getFeeds().size() == 0) {
                writeBean.setSelectedFeed(writeBean.getStartFeed());
            }
        }
        String s = request.getParameter(INPUT_MAX_AGE);
        if (s != null && s.length() > 0) {
            try {
                int n = Integer.parseInt(s);
                if (n < 1) {
                    alertHandler.setError(resources.get("enter_a_whole_number_greater_than_zero"));
                } else {
                    writeBean.setMaxAge(n);
                }
            } catch (NumberFormatException nfe) {
                alertHandler.setError(resources.get("enter_a_whole_number_greater_than_zero"));
            }
        }
        String maxEntries = request.getParameter(INPUT_MAX_ENTRIES);
        if (maxEntries != null && maxEntries.length() > 0) {
            try {
                int n = Integer.parseInt(maxEntries);
                if (n < 1) {
                    alertHandler.setError(resources.get("enter_a_whole_number_greater_than_zero"));
                } else {
                    writeBean.setMaxEntries(n);
                }
            } catch (NumberFormatException nfe) {
                alertHandler.setError(resources.get("enter_a_whole_number_greater_than_zero"));
            }
        }
        String[] disableMaxAge = request.getParameterValues(INPUT_DISABLE_MAX_AGE);
        if (disableMaxAge != null && disableMaxAge.length > 0) {
            writeBean.setDisableMaxAge(true);
        } else {
            writeBean.setDisableMaxAge(false);
        }
        String[] newWindow = request.getParameterValues(INPUT_NEWWIN);
        if (newWindow != null && newWindow.length > 0) {
            writeBean.setNewWindow(true);
        } else {
            writeBean.setNewWindow(false);
        }
        if (!alertHandler.isErrorExists()) {
            response.setPortletMode(PortletMode.VIEW);
        }
    }
}
