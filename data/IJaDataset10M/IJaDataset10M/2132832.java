package javango.contrib.galoot;

import javango.http.HttpResponse;

public class Helper {

    public static HttpResponse renderToResponse(String template, java.util.Map<String, Object> context) {
        return new GalootResponse(template, context);
    }
}
