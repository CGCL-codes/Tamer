package com.google.code.guidatv.server.service.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;

public class GuidaTvApplication extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        TemplateRoute route = router.attach("/login-info", LoginInfoServerResource.class);
        route.setMatchingQuery(false);
        router.attach("/channels", ChannelsServerResource.class);
        router.attach("/channels/{channel}/schedules/{date}", ChannelScheduleServerResource.class);
        return router;
    }
}
