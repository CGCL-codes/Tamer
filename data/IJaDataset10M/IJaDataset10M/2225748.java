package org.restlet.example.tutorial;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Directory;
import org.restlet.Guard;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import static org.restlet.example.tutorial.Constants.*;

/**
 * Routers and hierarchical URIs
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class Part11 {

    public static void main(String[] args) throws Exception {
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8182);
        component.getClients().add(Protocol.FILE);
        Application application = new Application(component.getContext()) {

            @Override
            public Restlet createRoot() {
                Router router = new Router(getContext());
                Guard guard = new Guard(getContext(), ChallengeScheme.HTTP_BASIC, "Restlet tutorial");
                guard.getSecrets().put("scott", "tiger".toCharArray());
                router.attach("/docs/", guard);
                Directory directory = new Directory(getContext(), ROOT_URI);
                guard.setNext(directory);
                Restlet account = new Restlet() {

                    @Override
                    public void handle(Request request, Response response) {
                        String message = "Account of user \"" + request.getAttributes().get("user") + "\"";
                        response.setEntity(message, MediaType.TEXT_PLAIN);
                    }
                };
                Restlet orders = new Restlet(getContext()) {

                    @Override
                    public void handle(Request request, Response response) {
                        String message = "Orders of user \"" + request.getAttributes().get("user") + "\"";
                        response.setEntity(message, MediaType.TEXT_PLAIN);
                    }
                };
                Restlet order = new Restlet(getContext()) {

                    @Override
                    public void handle(Request request, Response response) {
                        String message = "Order \"" + request.getAttributes().get("order") + "\" for user \"" + request.getAttributes().get("user") + "\"";
                        response.setEntity(message, MediaType.TEXT_PLAIN);
                    }
                };
                router.attach("/users/{user}", account);
                router.attach("/users/{user}/orders", orders);
                router.attach("/users/{user}/orders/{order}", order);
                return router;
            }
        };
        component.getDefaultHost().attach(application);
        component.start();
    }
}
