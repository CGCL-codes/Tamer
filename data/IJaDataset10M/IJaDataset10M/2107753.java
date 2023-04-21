package com.brasee.games.chess.web;

import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.View;

public class JsonView implements View {

    private Map<String, Object> responseMap;

    public JsonView(Map<String, Object> responseMap) {
        this.responseMap = responseMap;
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(responseMap);
        String jsonString = jsonObject.toString();
        PrintWriter writer = response.getWriter();
        writer.write(jsonString);
    }
}
