package fr.graphit.web.uri;

public interface URIContext {

    public static final String ROOT = "/";

    public static final String CONNECT = "/connect";

    public static final String DISCONNECT = "/disconnect";

    public static final String DISPLAY = ROOT + "display" + "/:itemId";

    public static final String SUBGRAPH = ROOT + "subgraph" + "/:subgraphId";

    public static final String EDIT = ROOT + "edit";

    public static final String ADD = ROOT + "add";

    public static final String SEARCH = ROOT + "search";

    public static final String BAD_BROWSER = ROOT + "badbrowser";

    public static final String API = ROOT + "api/";

    public static final String API_DELETE_LINK = API + "deleteLink";

    public static final String API_ADD_LINK = API + "addLink";

    public static final String API_GET_LINKS = API + "getLinks";

    public static final String API_SEARCH = API + "search";

    public static final String API_GRAPH = API + "graph";

    public static final String API_NODE = API + "node";

    public static final String CONF = ROOT + "conf";
}
