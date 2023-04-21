package scotlandyard.servlets.users;

import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import scotlandyard.engine.impl.Engine;
import scotlandyard.servlets.HttpServletEx;

/**
 * shows an icon selector pane
 * @author Hussain Al-Mutawa
 * @version 3.0
 */
public class icons extends HttpServletEx {

    private static final long serialVersionUID = -6648078160838030566L;

    @Override
    public void setHeader(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/html");
    }

    @Override
    public String processRequest(Map<String, String> parameters, String sid) throws ServletException {
        final StringBuffer sb = new StringBuffer();
        final java.util.Random random = new java.util.Random();
        final String _icon = parameters.get("icon");
        final String nid = "icons" + System.currentTimeMillis();
        sb.append("<div><div id='" + nid + "'  class='selectable'>");
        String sel = null;
        if (_icon != null && Engine.instance().icons.get(_icon) != null && !Engine.instance().icons.get(_icon)) {
            sel = _icon;
        }
        for (Entry<String, Boolean> entry : Engine.instance().icons.entrySet()) {
            if (!entry.getValue()) {
                if (sel == null && random.nextBoolean()) {
                    sel = entry.getKey();
                }
                sb.append("<span onmousedown=\"changeIcon('" + entry.getKey() + "')\" class='ui-state-default" + (entry.getKey().equals(sel) ? " ui-selected" : "") + "'>");
                sb.append("<img width='24' height='24' alt='icon' title='profile icon' src='images/icons/" + entry.getKey() + "' />");
                sb.append("</span>\n");
            }
        }
        if (sel == null) {
            for (final Entry<String, Boolean> entry : Engine.instance().icons.entrySet()) {
                if (!entry.getValue()) {
                    sel = entry.getKey();
                    break;
                }
            }
            if (sel == null) {
                sb.append("<h2 style='color:red'>Maximum numberof user has been reached</h2>");
            }
        }
        sb.append("</div><script>");
        sb.append("$(function() {");
        sb.append("$('#icon').val('" + sel + "');");
        sb.append("$('#" + nid + "').selectable();");
        sb.append("});");
        sb.append("</script></div>");
        return sb.toString();
    }
}
