package net.joindesk.response.error;

import javax.servlet.http.HttpServletResponse;

public interface ErrorResponse {

    void write(HttpServletResponse response, String code, String message);
}
