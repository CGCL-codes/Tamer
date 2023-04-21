package net.sourceforge.cruisecontrol.dashboard.web.view;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.servlet.view.AbstractView;

public class JsonView extends AbstractView {

    public static final String RENDER_DIRECT = " ";

    protected void renderMergedOutputModel(Map map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String json = StringUtils.replaceChars(renderJson(map), "\r\t\n", "");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(json);
        writer.close();
    }

    public String renderJson(Map map) {
        StringBuffer sb = new StringBuffer();
        if ((map.size() == 1) && (map.containsKey(RENDER_DIRECT))) {
            renderObject(map.get(RENDER_DIRECT), sb);
        } else {
            renderMap(map, sb);
        }
        return sb.toString();
    }

    private void renderObject(Object obj, StringBuffer sb) {
        if (obj instanceof Map) {
            renderMap((Map) obj, sb);
        } else if (obj instanceof List) {
            renderList((List) obj, sb);
        } else {
            renderAsString(obj, sb);
        }
    }

    private void renderMap(Map map, StringBuffer sb) {
        sb.append("{ ");
        for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            renderAsString(key, sb);
            sb.append(" : ");
            renderObject(map.get(key), sb);
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(" }");
    }

    private void renderList(List list, StringBuffer sb) {
        sb.append("[ ");
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            renderObject(iter.next(), sb);
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(" ]");
    }

    private void renderAsString(Object value, StringBuffer sb) {
        String valueStr = value == null ? "" : value.toString();
        valueStr = StringUtils.replace(valueStr, "\n", "");
        valueStr = StringUtils.replace(valueStr, "\t", "");
        valueStr = StringUtils.replace(valueStr, "\r", "");
        sb.append("\"").append(StringEscapeUtils.escapeJavaScript(valueStr)).append("\"");
    }
}
