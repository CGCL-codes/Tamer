package com.georgeandabe.ignant;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.georgeandabe.util.ArgumentUtils;
import com.georgeandabe.util.StreamUtils;

public class PackageResource extends DataResource {

    private String packageName = null;

    public PackageResource(String packageName, String pathElement) {
        super(pathElement);
        ArgumentUtils.assertNotNull(pathElement);
        this.packageName = packageName;
        addSubResource(new JarResourcesResource());
    }

    private class JarResourcesResource extends DataResource {

        public JarResourcesResource() {
            super(DataResource.WILDCARD_ELEMENT);
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response, String[] pathElements) throws ServletException, IOException {
            String resourcePath = packageName + "/" + pathElements[pathElements.length - 1];
            if (pathElements[pathElements.length - 1].endsWith(".js")) {
                String script = StreamUtils.readResource(resourcePath);
                if (script == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                sendStringResponse(script, "text/javascript", response);
                return;
            }
            InputStream input = StreamUtils.getResourceStream(resourcePath);
            if (input == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setStatus(HttpServletResponse.SC_OK);
            StreamUtils.write(input, response.getOutputStream());
        }
    }
}
