package com.kescom.matrix.core.api;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutApiVerbHandler implements IApiVerbHandler {

    public ApiDict process(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        ApiDict dict = new ApiDict();
        request.getSession().removeAttribute("MATRIX_USER");
        dict.put("status", "ok");
        return dict;
    }
}
