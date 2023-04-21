package org.openremote.controller.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.openremote.controller.Constants;
import org.openremote.controller.exception.ControlCommandException;
import org.openremote.controller.exception.NoSuchComponentException;
import org.openremote.controller.service.ControlStatusPollingService;
import org.openremote.controller.service.StatusCacheService;
import org.openremote.controller.spring.SpringContext;

/**
 * Status Polling RESTful servlet of control.
 * It's responsiable for response corresponding result with the RESTful polling url.
 * 
 * @author Handy.Wang 2009-10-19
 */
@SuppressWarnings("serial")
public class ControlStatusPollingRESTServlet extends HttpServlet {

    /** This service is responsible for observe statuses change and return the changed statuses(xml-formatted). */
    private ControlStatusPollingService controlStatusPollingService = (ControlStatusPollingService) SpringContext.getInstance().getBean("controlStatusPollingService");

    /** This service is check whether the component is exist. */
    private StatusCacheService cacheService = (StatusCacheService) SpringContext.getInstance().getBean("statusCacheService");

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
    * The Constructor.
    */
    public ControlStatusPollingRESTServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
    * It's responsible for polling the <b>changed statuses</b> or <b>TIME_OUT</b> if time out.
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Started polling at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String url = request.getRequestURL().toString();
        String regexp = "rest\\/polling\\/(.*?)\\/(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(url);
        String unParsedComponentIDs = null;
        String deviceID = null;
        if (matcher.find()) {
            deviceID = matcher.group(1);
            if (deviceID == null || "".equals(deviceID)) {
                throw new NullPointerException("Device id was null");
            }
            unParsedComponentIDs = matcher.group(2);
            PrintWriter printWriter = response.getWriter();
            try {
                checkComponentId(unParsedComponentIDs);
                String stateFromPolling = controlStatusPollingService.queryChangedState(deviceID, unParsedComponentIDs);
                if (stateFromPolling != null && !"".equals(stateFromPolling)) {
                    if (Constants.SERVER_RESPONSE_TIME_OUT.equalsIgnoreCase(stateFromPolling)) {
                        response.sendError(504, "Time out!");
                    } else {
                        logger.info("Return the polling status.");
                        printWriter.write(stateFromPolling);
                    }
                }
                logger.info("Finished polling at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            } catch (ControlCommandException e) {
                response.sendError(e.getErrorCode(), e.getMessage());
            }
        } else {
            response.sendError(ControlCommandException.INVALID_POLLING_URL, "Invalid polling url:" + url);
        }
    }

    /**
    * check whether the component id is valid.
    * @param unParsedcontrolIDs
    */
    private void checkComponentId(String unParsedcontrolIDs) {
        String[] controlIDs = (unParsedcontrolIDs == null || "".equals(unParsedcontrolIDs)) ? new String[] {} : unParsedcontrolIDs.split(ControlStatusPollingService.CONTROL_ID_SEPARATOR);
        if (controlIDs.length == 0) {
            throw new NullPointerException("Polling ids were null.");
        }
        String tmpStr = null;
        try {
            for (int i = 0; i < controlIDs.length; i++) {
                tmpStr = controlIDs[i];
                cacheService.getStatusByComponentId(Integer.parseInt(tmpStr));
            }
        } catch (NumberFormatException e) {
            throw new NoSuchComponentException("Wrong component id :'" + tmpStr + "' The component id can only be digit");
        }
    }
}
