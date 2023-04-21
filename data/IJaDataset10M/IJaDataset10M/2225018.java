package com.core.util;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.springframework.web.servlet.view.AbstractView;

;

/**
 * @author laker
 *
 */
public class AjaxJsonView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> arg0, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/xml;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store,max-age=0,no-cache,must-revalidate");
        response.addHeader("Cache-Control", "post-check=0,pre-check=0");
        response.setHeader("Pragma", "no-cache");
        JSONArray json = (JSONArray) arg0.get("ajax_json");
        response.getWriter().write(json.toString());
    }
}
