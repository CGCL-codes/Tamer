package ua.orion.birt.services;

import ua.orion.birt.ReportPreviewLink;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.Response;
import ua.orion.core.utils.Defense;

/**
 *
 * @author sl
 */
public class ReportPreviewLinkFactoryImpl implements ReportPreviewLinkFactory {

    private static String PROTOCOL = "data://rptdesign/";

    private static String P_REPORT = "__report";

    private static String P_DOCUMENT = "__document";

    private static String P_LOCALE = "__locale";

    private final Response response;

    private final String uri;

    public ReportPreviewLinkFactoryImpl(HttpServletRequest request, Response response) {
        this.response = response;
        uri = String.format("http://%s:%d%s/frameset", request.getServerName(), request.getServerPort(), request.getContextPath());
    }

    @Override
    public Link buildByReport(String name) {
        Defense.notBlank(name, "name");
        Map<String, String> params = new HashMap<String, String>();
        params.put(P_REPORT, PROTOCOL + name);
        return new ReportPreviewLink(uri, params, response);
    }

    @Override
    public Link buildByReport(String name, Locale locale) {
        Defense.notBlank(name, "name");
        Defense.notNull(locale, "locale");
        Map<String, String> params = new HashMap<String, String>();
        params.put(P_REPORT, PROTOCOL + name);
        params.put(P_LOCALE, locale.toString());
        return new ReportPreviewLink(uri, params, response);
    }

    @Override
    public Link buildByDocument(String name) {
        Defense.notBlank(name, "name");
        Map<String, String> params = new HashMap<String, String>();
        params.put(P_DOCUMENT, PROTOCOL + name);
        return new ReportPreviewLink(uri, params, response);
    }

    @Override
    public Link buildByDocument(String name, Locale locale) {
        Defense.notBlank(name, "name");
        Defense.notNull(locale, "locale");
        Map<String, String> params = new HashMap<String, String>();
        params.put(P_DOCUMENT, PROTOCOL + name);
        params.put(P_LOCALE, locale.toString());
        return new ReportPreviewLink(uri, params, response);
    }
}
