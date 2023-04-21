package dk.bestbrains.friendly;

public class DefaultUrlRouting implements RoutingHandler {

    @Override
    public RoutingResult getRoute(String uri, ParameterMap requestParameters) {
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        String[] parts = uri.split("/");
        String controller = "HomeController";
        String action = "index";
        if (parts.length > 0 && parts[0].length() > 0) {
            controller = parts[0] + "Controller";
        }
        if (parts.length > 1) {
            action = parts[1].replace(".", "_");
        }
        return new RoutingResult(controller, action);
    }
}
