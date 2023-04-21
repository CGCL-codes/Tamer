package org.intellij.trinkets.research.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jetbrains.annotations.NonNls;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Google search util.
 *
 * @author Alexey Efimov
 */
public final class HttpSearchUtil {

    private HttpSearchUtil() {
    }

    @NonNls
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0";

    @NonNls
    private static final String USER_AGENT = "User-Agent";

    public static Map<String, String> search(String server, String action, String queryParameter, String query, Pattern resultRegExp, String[][] additionalParamenters) throws IOException {
        Map<String, String> result = new TreeMap<String, String>();
        HttpClient httpClient = new HttpClient();
        httpClient.startSession(new URL(server));
        try {
            GetMethod getMethod = new GetMethod(action);
            getMethod.setHeader(USER_AGENT, DEFAULT_USER_AGENT);
            getMethod.setParameter(queryParameter, query);
            if (additionalParamenters != null) {
                for (String[] strings : additionalParamenters) {
                    getMethod.setParameter(strings[0], strings[1]);
                }
            }
            httpClient.executeMethod(getMethod);
            String response = getMethod.getDataAsString();
            String trimmed = response.replaceAll("[\\s\\n\\r]+", " ");
            Matcher matcher = resultRegExp.matcher(trimmed);
            while (matcher.find()) {
                String url = matcher.group(1);
                if (url.matches("^\\w+://.*")) {
                    result.put(url, matcher.group(2));
                } else {
                    if (!url.startsWith("/") && !server.endsWith("/")) {
                        result.put(server + "/" + url, matcher.group(2));
                    } else {
                        result.put(server + url, matcher.group(2));
                    }
                }
            }
        } catch (HttpException e) {
            throw new IOException(e.getMessage());
        } finally {
            httpClient.endSession();
        }
        return result;
    }
}
