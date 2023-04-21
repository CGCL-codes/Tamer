package org.orangegears.birt.report;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ofbiz.webapp.event.EventHandlerException;
import org.ofbiz.webapp.event.ServiceEventHandler;

public class ReportServiceHandler extends ServiceEventHandler {

    @Override
    public String invoke(String eventPath, String eventMethod, HttpServletRequest request, HttpServletResponse response) throws EventHandlerException {
        try {
            request.setAttribute("outputStream", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.invoke(eventPath, eventMethod, request, response);
    }
}
