package org.webcastellum;

import java.io.Serializable;
import java.util.Map;

public final class RequestDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    String clientAddress;

    String agent;

    String servletPath;

    String queryString;

    Permutation queryStringVariants;

    String requestedSessionId;

    boolean sessionCameFromCookie;

    boolean sessionCameFromURL;

    String referrer;

    String url;

    String uri;

    String method;

    String protocol;

    String mimeType;

    String remoteHost;

    String remoteUser;

    Map headerMap;

    Map headerMapVariants;

    Map cookieMap;

    Map cookieMapVariants;

    String encoding;

    int contentLength;

    String scheme;

    String serverName;

    int serverPort;

    String authType;

    String contextPath;

    String pathInfo;

    String pathTranslated;

    int remotePort;

    int localPort;

    String localAddr;

    String localName;

    Map requestParameterMap;

    Map requestParameterMapVariants;

    String country;

    boolean somethingHasBeenUncovered;

    boolean nonStandardPermutationsRequired;

    byte decodingPermutationLevel;
}
