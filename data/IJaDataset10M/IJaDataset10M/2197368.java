package org.monet.backmobile.control;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.monet.backmobile.ApplicationBackmobile;
import org.monet.backmobile.control.actions.Action;
import org.monet.backmobile.control.actions.ActionFactory;
import org.monet.backmobile.control.actions.TypedAction;
import org.monet.backmobile.exceptions.ActionException;
import org.monet.backmobile.model.Language;
import org.monet.backmobile.service.ActionCode;
import org.monet.backmobile.service.Response;
import org.monet.backmobile.service.errors.BusinessUnitNotAvailableError;
import org.monet.backmobile.service.errors.ServerError;
import org.monet.kernel.agents.AgentLogger;
import org.monet.kernel.constants.ApplicationInterface;
import org.monet.kernel.constants.Database;
import org.monet.kernel.constants.ErrorCode;
import org.monet.kernel.model.BusinessUnit;
import org.monet.kernel.model.Context;
import org.monet.kernel.utils.StreamHelper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class Controller extends HttpServlet {

    private static final long serialVersionUID = -9110665304913574501L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AgentLogger logger = AgentLogger.getInstance();
        ActionFactory actionFactory = ActionFactory.getInstance();
        String idSession = request.getSession().getId();
        Context oContext = Context.getInstance();
        Long idThread = Thread.currentThread().getId();
        Serializer serializer = new Persister();
        GZIPOutputStream outputStream = null;
        Response result = null;
        String actionCode = request.getParameter("op");
        try {
            oContext.setApplication(idThread, request.getRemoteHost(), ApplicationBackmobile.NAME, ApplicationInterface.USER);
            oContext.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
            oContext.setSessionId(idThread, idSession);
            oContext.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);
            Language.fillCurrentLanguage(request);
            try {
                if ((!BusinessUnit.started()) || (!ApplicationBackmobile.started())) {
                    result = new Response(new BusinessUnitNotAvailableError());
                } else {
                    logger.debug("Request action: %s", actionCode);
                    TypedAction<?, ?> action = (TypedAction<?, ?>) actionFactory.get(ActionCode.valueOf(actionCode));
                    result = new Response(action.execute(request, response));
                }
            } catch (ActionException e) {
                result = new Response(e.getErrorResult());
            } catch (Exception e) {
                logger.error(e);
                result = new Response(new ServerError());
            }
            response.setHeader("Content-Encoding", "gzip");
            response.setContentType("text/xml");
            outputStream = new GZIPOutputStream(response.getOutputStream());
            serializer.write(result, outputStream);
        } catch (Exception e) {
            logger.error(e);
            try {
                serializer.write(new ServerError(), outputStream);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
        StreamHelper.close(outputStream);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AgentLogger logger = AgentLogger.getInstance();
        ActionFactory actionFactory = ActionFactory.getInstance();
        String idSession = request.getSession().getId();
        Context oContext = Context.getInstance();
        Long idThread = Thread.currentThread().getId();
        String result = null;
        String actionCode = request.getParameter("op");
        try {
            oContext.setApplication(idThread, request.getRemoteHost(), ApplicationBackmobile.NAME, ApplicationInterface.USER);
            oContext.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
            oContext.setSessionId(idThread, idSession);
            oContext.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);
            Language.fillCurrentLanguage(request);
            try {
                if ((!BusinessUnit.started()) || (!ApplicationBackmobile.started())) {
                    result = ErrorCode.BUSINESS_UNIT_STOPPED;
                } else {
                    logger.debug("Request action: %s", actionCode);
                    Action<?> action = actionFactory.get(ActionCode.valueOf(actionCode));
                    action.execute(request, response);
                    return;
                }
            } catch (ActionException e) {
                result = e.getErrorResult().toString();
            } catch (Exception e) {
                logger.error(e);
                result = (new ServerError()).toString();
            }
            response.getWriter().write(result);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
