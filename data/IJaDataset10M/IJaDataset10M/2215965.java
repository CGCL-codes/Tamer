package com.greatlogic.bigquery.ui.actionhandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.greatlogic.bigquery.dao.Property;
import com.greatlogic.bigquery.daofactory.PropertyFactory;
import com.greatlogic.bigquery.ui.AjaxDispatcher;

public class GetPropertiesActionHandler implements ActionHandler {

    private static final Logger logger = Logger.getLogger(GetPropertiesActionHandler.class.getName());

    @Override
    public void handleAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.fine("handleAction");
        String kindName = AjaxDispatcher.getRequiredStringParameterValue("key", req);
        logger.fine(String.format("kindName: %s", kindName));
        JSONArray properties = new JSONArray();
        for (Property property : PropertyFactory.getPropertiesByKind(kindName)) {
            properties.put(property.toJSONNodeObject());
        }
        logger.fine(String.format("getProperties... properties: %s", properties));
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(properties.toString());
    }
}
