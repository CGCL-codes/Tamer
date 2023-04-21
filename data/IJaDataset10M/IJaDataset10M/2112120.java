package org.spantus.server.servlet.handeler;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.spantus.server.dto.SpntServletConfig;
import edu.mit.csail.sls.wami.util.ServletUtils;

public class AppletResourceHandler implements ResourceHandler {

    private Pattern appletPatern = Pattern.compile("\\/content\\/applet\\/([\\w\\d\\.\\-]*\\.jar)");

    @Override
    public boolean isApplicable(String resource) {
        Matcher anContentMatcher = appletPatern.matcher(resource);
        return anContentMatcher.find();
    }

    @Override
    public InputStream handle(HttpServletRequest request, String resource, SpntServletConfig config) {
        return ServletUtils.getResourceAsStream(request.getSession().getServletContext(), "content/applet/" + config.getAppletjarName());
    }
}
