package de.sicari.web.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.sicari.kernel.Environment;
import de.sicari.util.WhatIs;
import de.sicari.web.HttpResource;
import de.sicari.web.HttpResourceStore;

public class HttpResourceServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpResourceStore resourceStore;
        OutputStream sout;
        HttpResource resource;
        Environment env;
        String mimeType;
        String resourceId;
        byte[] data;
        try {
            env = Environment.getEnvironment();
            resourceStore = (HttpResourceStore) env.lookup(WhatIs.stringValue(HttpResourceStore.WHATIS));
            if (resourceStore != null) {
                resourceId = req.getParameter("id");
                if (resourceId != null) {
                    resource = resourceStore.getResource(resourceId);
                    if (resource != null) {
                        mimeType = resource.getMimeType();
                        data = resource.getData();
                        if (data != null) {
                            sout = res.getOutputStream();
                            sout.write(data, 0, data.length);
                            sout.flush();
                            if (mimeType != null) {
                                res.setContentType(mimeType);
                            }
                            res.setStatus(HttpServletResponse.SC_OK);
                            res.setContentLength(data.length);
                        } else {
                            res.sendError(HttpServletResponse.SC_NO_CONTENT, "Resource with given ID contains no data");
                        }
                    } else {
                        res.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource with given ID not found");
                    }
                } else {
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter 'id' is missing");
                }
            } else {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal service not found");
            }
        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal service error: " + e);
            e.printStackTrace();
        }
    }
}
