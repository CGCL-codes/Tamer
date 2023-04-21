package org.yawlfoundation.yawl.resourcing.rsInterface.scheduling;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  The interface InterfaceS_Service defines the event methods that are passed
 *  from the resource service to a scheduling listener.
 *
 *  @author Michael Adams
 *  @date 15/10/2010
 */
public interface InterfaceS_Service {

    void handleUtilisationStatusChangeEvent(String changeXML);

    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
