package com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http;

/**
 * @author mbs
 */
public interface HttpCallCommand {

    String getUrl();

    void setUrl(String url);

    HttpMethod getMethod();

    void setMethod(HttpMethod method);

    boolean isApiKeyProtected();

    void setApiKeyProtected(boolean apiKeyProtected);

    boolean isSessionIdProtected();

    void setSessionIdProtected(boolean sessionIdProtected);

    boolean isHeaderAllowed();

    void setHeaderAllowed(boolean headerAllowed);

    Object getInput();

    void setInput(Object input);

    void setAcceptType(String acceptType);

    String getAcceptType();

    Object getOutput();

    String getErrorMessage();

    int getStatus();

    boolean didErrorOccur();

    void execute();

    String getLocation();

    String getContentType();

    HttpMethod getTunneledMethod();

    void setTunneledMethod(HttpMethod tunneledMethod);
}
