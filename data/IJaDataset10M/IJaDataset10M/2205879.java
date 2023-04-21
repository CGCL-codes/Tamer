package de.felixbruns.jotify.gateway.handlers;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import de.felixbruns.jotify.gateway.GatewayConnection;
import de.felixbruns.jotify.gateway.GatewayApplication;
import de.felixbruns.jotify.gateway.GatewayHandler;
import de.felixbruns.jotify.gateway.GatewayConnection.BrowseType;
import de.felixbruns.jotify.util.Hex;

public class BrowseHandler extends GatewayHandler {

    @Override
    public String handle(Map<String, String> params) {
        if (params.containsKey("session") && params.containsKey("type") && params.containsKey("id")) {
            String session = params.get("session");
            int type = Integer.parseInt(params.get("type"));
            String id = params.get("id");
            if (GatewayApplication.sessions.containsKey(session)) {
                GatewayConnection jotify = GatewayApplication.sessions.get(session);
                try {
                    return jotify.browse(BrowseType.valueOf(type), id);
                } catch (TimeoutException e) {
                    System.out.println("<error>" + e.getMessage() + "</error>");
                    return "<error>" + e.getMessage() + "</error>";
                }
            } else {
                return "<error>Session not found!</error>";
            }
        } else if (params.containsKey("session") && params.containsKey("ids")) {
            String session = params.get("session");
            String[] ids = params.get("ids").split(",");
            String[] idsFiltered = {};
            int a = 0;
            for (int i = 0; i < ids.length; i++) {
                if (Hex.isHex(ids[i])) {
                    idsFiltered[a] = ids[i];
                    a++;
                }
            }
            if (GatewayApplication.sessions.containsKey(session)) {
                GatewayConnection jotify = GatewayApplication.sessions.get(session);
                try {
                    return jotify.browse(Arrays.asList(idsFiltered));
                } catch (TimeoutException e) {
                    return "<error>" + e.getMessage() + "</error>";
                }
            } else {
                System.out.println("<error>Session not found!</error>");
                return "<error>Session not found!</error>";
            }
        } else {
            System.out.println("<error>Invalid request parameters!</error>");
            return "<error>Invalid request parameters!</error>";
        }
    }
}
