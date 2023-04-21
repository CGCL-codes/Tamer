package de.felixbruns.jotify.gateway.handlers;

import java.util.Map;
import de.felixbruns.jotify.gateway.GatewayApplication;
import de.felixbruns.jotify.gateway.GatewayHandler;

public class CheckHandler extends GatewayHandler {

    @Override
    public String handle(Map<String, String> params) {
        if (params.containsKey("session")) {
            String session = params.get("session");
            if (GatewayApplication.sessions.containsKey(session)) {
                return "<session>" + session + "</session>";
            } else {
                return "<error>Session not found!</error>";
            }
        } else {
            return "<error>Invalid request parameters!</error>";
        }
    }
}
