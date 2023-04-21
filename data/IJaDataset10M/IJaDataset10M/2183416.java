package pub.tairtags;

import pub.utils.StringUtils;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class NavigateBarTag extends TagSupport {

    private int page_limit;

    private int current_position;

    private int max_position;

    private String variable_name;

    private int link_range;

    public NavigateBarTag() {
        page_limit = 20;
        current_position = 1;
        max_position = 0;
        variable_name = "start";
        link_range = 5;
    }

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String nav_url = makeNavigationUrl(request);
        try {
            out.print("[");
            int counter = 1;
            for (int i = 1; i <= max_position; i += page_limit) {
                out.print(" <a href=\"");
                out.print(nav_url);
                out.print("&" + variable_name + "=" + i);
                out.print("\">");
                out.println(" " + counter);
                out.println("</a> ");
                counter++;
            }
            out.print("]");
        } catch (IOException e) {
            ;
        }
        return SKIP_BODY;
    }

    public void setLink_range(String link_range) {
        this.link_range = Integer.parseInt(link_range);
    }

    public String getLink_range() {
        return "" + this.link_range;
    }

    public void setPage_limit(String page_limit) {
        this.page_limit = Math.max(1, Integer.parseInt(page_limit));
    }

    public String getPage_limit() {
        return this.page_limit + "";
    }

    public void setCurrent_position(String current_position) {
        this.current_position = Math.max(1, Integer.parseInt(current_position));
    }

    public String getCurrent_position() {
        return this.current_position + "";
    }

    public void setMax_position(String max_position) {
        this.max_position = Math.max(1, Integer.parseInt(max_position));
    }

    public String getMax_position() {
        return this.max_position + "";
    }

    public void setVariable_name(String variable_name) {
        this.variable_name = variable_name;
    }

    public String getVariable_name() {
        return this.variable_name;
    }

    private String makeNavigationUrl(HttpServletRequest req) {
        String self_url = StringUtils.getSelfUrl2(req);
        String uri = StringUtils.getURIString(self_url);
        String query = StringUtils.getQueryString(self_url);
        return uri + "?" + StringUtils.removeQueryParameter(query, variable_name);
    }
}
