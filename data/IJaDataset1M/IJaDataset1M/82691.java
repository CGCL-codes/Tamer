package com.sitescape.team.samples.remoteapp.web;

import static com.sitescape.util.search.Constants.ENTRY_TYPE_ENTRY;
import static com.sitescape.util.search.Constants.ENTRY_TYPE_FIELD;
import static com.sitescape.util.search.Constants.FAMILY_FIELD;
import static com.sitescape.util.search.Constants.FAMILY_FIELD_TASK;
import static com.sitescape.util.search.Restrictions.eq;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.sitescape.team.ObjectKeys;
import com.sitescape.team.client.ws.TeamingServiceSoapBindingStub;
import com.sitescape.team.client.ws.TeamingServiceSoapServiceLocator;
import com.sitescape.team.client.ws.model.Description;
import com.sitescape.team.client.ws.model.FolderEntry;
import com.sitescape.team.module.shared.EntityIndexUtils;
import com.sitescape.team.search.BasicIndexUtils;
import com.sitescape.util.search.Constants;
import com.sitescape.util.search.Criteria;
import com.sitescape.team.search.filter.SearchFilterKeys;
import com.sitescape.team.task.TaskHelper;
import com.sitescape.util.servlet.StringServletResponse;

public class AddEntryServlet extends HttpServlet {

    private static final String TEAMING_SERVICE_ADDRESS = "http://localhost:8080/ssr/token/ws/TeamingService";

    private static final String PARAMETER_NAME_VERSION = "ss_version";

    private static final String PARAMETER_NAME_APPLICATION_ID = "ss_application_id";

    private static final String PARAMETER_NAME_USER_ID = "ss_user_id";

    private static final String PARAMETER_NAME_ACCESS_TOKEN = "ss_access_token";

    private static final String PARAMETER_NAME_TOKEN_SCOPE = "ss_token_scope";

    private static final String PARAMETER_NAME_RENDERABLE = "ss_renderable";

    private static final String PARAMETER_FORM_TITLE = "title";

    private static final String PARAMETER_FORM_DESCRIPTION = "description";

    private static final String PARAMETER_FORM_BINDER_ID = "binderId";

    private static final String PARAMETER_FORM_ENTRY_ID = "entryId";

    private static final String PARAMETER_FORM_OPERATION = "operation";

    private static final String PARAMETER_FORM_OPERATION_ENTRY_VIEW = "view";

    private static final String PARAMETER_FORM_OPERATION_ENTRY_FORM = "form";

    private static final String PARAMETER_FORM_DEFINITION_ID = "definitionId";

    private static final String PARAMETER_FORM_RETURN_URL = "returnUrl";

    private static final String PARAMETER_FORM_BUTTON_OK = "okBtn";

    private static final String PARAMETER_FORM_BUTTON_CANCEL = "cancelBtn";

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String version = req.getParameter(PARAMETER_NAME_VERSION);
            String applicationId = req.getParameter(PARAMETER_NAME_APPLICATION_ID);
            String userId = req.getParameter(PARAMETER_NAME_USER_ID);
            String accessToken = req.getParameter(PARAMETER_NAME_ACCESS_TOKEN);
            String tokenScope = req.getParameter(PARAMETER_NAME_TOKEN_SCOPE);
            boolean renderable = Boolean.parseBoolean(req.getParameter(PARAMETER_NAME_RENDERABLE));
            String pathInfo = req.getPathInfo();
            if (pathInfo.equals("/form")) {
                String binderId = req.getParameter(PARAMETER_FORM_BINDER_ID);
                String entryId = req.getParameter(PARAMETER_FORM_ENTRY_ID);
                String operation = req.getParameter(PARAMETER_FORM_OPERATION);
                String definitionId = req.getParameter(PARAMETER_FORM_DEFINITION_ID);
                if (definitionId == null) definitionId = ObjectKeys.DEFAULT_FOLDER_ENTRY_DEF;
                String jsp = "/WEB-INF/jsp/addentry/entry_form.jsp";
                if (PARAMETER_FORM_OPERATION_ENTRY_FORM.equals(operation)) {
                    jsp = "/WEB-INF/jsp/addentry/entry_view.jsp";
                }
                RequestDispatcher rd = req.getRequestDispatcher(jsp);
                StringServletResponse resp2 = new StringServletResponse(resp);
                req.setAttribute(PARAMETER_NAME_ACCESS_TOKEN, accessToken);
                req.setAttribute(PARAMETER_NAME_USER_ID, userId);
                req.setAttribute(PARAMETER_FORM_BINDER_ID, binderId);
                req.setAttribute(PARAMETER_FORM_ENTRY_ID, entryId);
                req.setAttribute(PARAMETER_FORM_DEFINITION_ID, definitionId);
                rd.include(req, resp2);
                resp.getWriter().print(resp2.getString());
            } else if (pathInfo.equals("/submit")) {
                Map params = req.getParameterMap();
                String result = "";
                if (params.containsKey(PARAMETER_FORM_BUTTON_OK)) {
                    String title = req.getParameter(PARAMETER_FORM_TITLE);
                    String description = req.getParameter(PARAMETER_FORM_DESCRIPTION);
                    String binderId = req.getParameter(PARAMETER_FORM_BINDER_ID);
                    String definitionId = req.getParameter(PARAMETER_FORM_DEFINITION_ID);
                    String returnUrl = req.getParameter(PARAMETER_FORM_RETURN_URL);
                    TeamingServiceSoapServiceLocator locator = new TeamingServiceSoapServiceLocator();
                    locator.setTeamingServiceEndpointAddress(TEAMING_SERVICE_ADDRESS);
                    TeamingServiceSoapBindingStub stub = (TeamingServiceSoapBindingStub) locator.getTeamingService();
                    FolderEntry entry = new FolderEntry();
                    entry.setTitle(title);
                    Description desc = new Description();
                    desc.setFormat(1);
                    desc.setText(description);
                    entry.setDescription(desc);
                    Long entryId = stub.folder_addEntry(accessToken, entry, null);
                    String jsp = "/WEB-INF/jsp/addentry/entry_return.jsp";
                    RequestDispatcher rd = req.getRequestDispatcher(jsp);
                    StringServletResponse resp2 = new StringServletResponse(resp);
                    req.setAttribute(PARAMETER_NAME_ACCESS_TOKEN, accessToken);
                    req.setAttribute(PARAMETER_NAME_USER_ID, userId);
                    req.setAttribute(PARAMETER_FORM_BINDER_ID, binderId);
                    req.setAttribute(PARAMETER_FORM_DEFINITION_ID, definitionId);
                    req.setAttribute(PARAMETER_FORM_RETURN_URL, returnUrl);
                    rd.include(req, resp2);
                    resp.getWriter().print(resp2.getString());
                } else if (params.containsKey(PARAMETER_FORM_BUTTON_CANCEL)) {
                }
                resp.getWriter().print(result);
            } else {
                String result = "Error: URL must end with \"/form\" or \"/submit\"";
                resp.getWriter().print(result);
            }
        } catch (IOException e) {
            String result = "Error: an unexpected error occurred: ";
            resp.getWriter().print(result);
            resp.getWriter().print(e.toString());
        } catch (Exception e) {
            String result = "Error: an unexpected error occurred: ";
            resp.getWriter().print(result);
            resp.getWriter().print(e.toString());
        }
    }
}
