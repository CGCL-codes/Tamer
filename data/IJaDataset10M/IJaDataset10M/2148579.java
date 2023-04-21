package org.osmius.webapp.action;

import net.sf.json.JSONObject;
import org.osmius.model.aux.TestGenericErrors;
import org.springframework.web.servlet.view.AbstractView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class OsmAggrupationResultView extends AbstractView {

    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-cache");
        Boolean ok = (Boolean) model.get("ok");
        String[] errors = (String[]) model.get("errors");
        TestGenericErrors test = new TestGenericErrors(ok, errors);
        JSONObject json = JSONObject.fromObject(test);
        PrintWriter out = response.getWriter();
        out.print(json);
    }
}
