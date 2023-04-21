package org.dcm4chee.xero.metadata.servlet;

import javax.servlet.http.HttpServletResponse;

/**
 * Simple factory class that will create an appropriate ErrorResponseItem for
 * an indicated Exception type.
 * <p>
 * TODO: Localize error codes that will be seen by the user.
 * TODO: Provide a way to turn off the printing of errors for users.
 * @author Andrew Cowan (andrew.cowan@agfa.com)
 */
public class ErrorResponseItemFactory {

    /**
    * Generate an appropriate servlet response item for the indicated error.
    */
    public ErrorResponseItem getResponseItem(Exception e) {
        ErrorResponseItem response;
        if (e instanceof SecurityException) response = createForbiddenError(e); else if (e instanceof ResponseException) response = createResponseException((ResponseException) e); else response = createInternalServerError(e);
        return response;
    }

    public ErrorResponseItem createInternalServerError(Exception e) {
        int code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = "Internal server error.  ";
        if (e != null) message += e;
        return new ErrorResponseItem(code, message);
    }

    public ErrorResponseItem createForbiddenError(Exception e) {
        int code = HttpServletResponse.SC_FORBIDDEN;
        String message = "Access to resource has been denied.  ";
        if (e != null) message += e;
        return new ErrorResponseItem(code, message);
    }

    public ErrorResponseItem createResponseException(ResponseException e) {
        int code = e.getCode();
        String message = e.getMessage();
        return new ErrorResponseItem(code, message);
    }

    public ErrorResponseItem createNotFoundError(String resource) {
        int code = HttpServletResponse.SC_NOT_FOUND;
        String message = "No content found for this request.  ";
        if (resource != null) message += resource;
        return new ErrorResponseItem(code, message);
    }
}
