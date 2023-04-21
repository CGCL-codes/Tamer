package com.nepxion.net.http.apache.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.nepxion.util.net.http.apache.ServerInvoker;

public class StringServerInvoker extends ServerInvoker {

    public StringServerInvoker() {
        setRequestEntityType(REQUEST_ENTITY_TYPE_STRING);
        setResponseEntityType(RESPONSE_ENTITY_TYPE_STREAM);
        setCharset("GBK");
    }

    public Object invoke(Object requestObject, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (requestObject instanceof String) {
            request.setCharacterEncoding("GBK");
            response.setCharacterEncoding("GBK");
            String reponseObject = requestObject.toString();
            return reponseObject + "��Ӧ";
        }
        return null;
    }
}
