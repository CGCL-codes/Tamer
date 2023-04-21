package org.gtugs.web;

import org.json.JSONObject;
import org.springframework.web.servlet.View;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jasonacooper@google.com (Jason Cooper)
 */
class JsonView implements View {

    public String getContentType() {
        return "text/json";
    }

    public void render(Map model, HttpServletRequest request, HttpServletResponse response) {
        try {
            String data = new JSONObject(model).toString();
            response.getWriter().write(data);
        } catch (java.io.IOException e) {
        }
    }
}
