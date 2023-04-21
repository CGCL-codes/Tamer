package org.libreplan.ws.common.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.WebClient;
import org.libreplan.ws.common.api.InstanceConstraintViolationsDTOId;
import org.libreplan.ws.common.api.IntegrationEntityDTO;

/**
 * Utilities class related with web service.
 *
 * @author Manuel Rego Casasnovas <mrego@igalia.com>
 * @author Fernando Bellas Permuy <fbellas@udc.es>
 */
public class Util {

    public static InstanceConstraintViolationsDTOId generateInstanceConstraintViolationsDTOId(Long numItem, IntegrationEntityDTO integrationEntityDTO) {
        return new InstanceConstraintViolationsDTOId(numItem, integrationEntityDTO.code, integrationEntityDTO.getEntityType());
    }

    @Deprecated
    public static String generateInstanceId(int instanceNumber, String instanceIdentifier) {
        String instanceId = instanceNumber + "";
        if (instanceIdentifier != null && instanceIdentifier.length() >= 0) {
            instanceId += " (" + instanceIdentifier + ")";
        }
        return instanceId;
    }

    public static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static String getAuthorizationHeader(String username, String password) {
        String authorization = Base64Utility.encode((username + ":" + password).getBytes());
        return "Basic " + authorization;
    }

    public static void addAuthorizationHeader(WebClient client, String login, String password) {
        String authorizationHeader = getAuthorizationHeader(login, password);
        client.header("Authorization", authorizationHeader);
    }
}
