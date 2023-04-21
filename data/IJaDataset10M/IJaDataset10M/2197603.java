package nuts.exts.struts2.views.java.simple;

import java.io.IOException;
import org.apache.struts2.components.template.TemplateRenderingContext;
import nuts.core.lang.StringUtils;
import nuts.exts.struts2.components.Select;
import nuts.exts.struts2.util.StrutsContextUtils;
import nuts.exts.struts2.views.java.AbstractTemplateRenderer;
import nuts.exts.struts2.views.java.Attributes;

public class NutsPagerRenderer extends AbstractTemplateRenderer {

    private String id;

    private Integer start;

    private Integer count;

    private Integer limit;

    private Integer total;

    public NutsPagerRenderer(TemplateRenderingContext context) {
        super(context);
    }

    public void render() throws IOException {
        id = (String) params.get("id");
        start = (Integer) params.get("start");
        count = (Integer) params.get("count");
        limit = (Integer) params.get("limit");
        total = (Integer) params.get("total");
        Attributes a = new Attributes();
        String cssClass = (String) params.get("cssClass");
        a.add("id", id).add("class", "ui-widget n-p" + (cssClass == null ? "" : " " + cssClass)).add("start", defs(start)).add("count", defs(count)).add("limit", defs(limit)).add("total", defs(total)).add("command", (String) params.get("command")).add("links", defs((String) params.get("links"), "false")).cssStyle(params);
        stag("div", a);
        if (total > 0) {
            write("<table class=\"n-p-table\"><tr>");
            write("<td class=\"n-p-info\">");
            write((String) params.get("pageInfo"));
            write("</td>");
            writePagerLinks();
            writePagerLimit();
            write("</tr></table>");
            writeJsc("$(function() { nuts.pager(\"#" + jsstr(id) + "\"); });");
        } else if (count > 0) {
            write("<table class=\"n-p-table\"><tr>");
            Integer ipage = (Integer) params.get("page");
            write("<td>");
            write("<a href=\"#\" class=\"n-p-text n-p-prev");
            if (ipage <= 1) {
                write(" n-p-disabled");
            }
            write("\" pageno=\"");
            write(String.valueOf(ipage - 1));
            write("\">");
            write((String) params.get("prevText"));
            write("</a></td>");
            write("<td class=\"n-p-info\">");
            write((String) params.get("pageInfo"));
            write("</td>");
            write("<td>");
            write("<a href=\"#\" class=\"n-p-text n-p-next");
            if (limit == null || count < limit) {
                write(" n-p-disabled");
            }
            write("\" pageno=\"");
            write(String.valueOf(ipage + 1));
            write("\">");
            write((String) params.get("nextText"));
            write("</a></td>");
            writePagerLimit();
            write("</tr></table>");
            writeJsc("$(function() { nuts.pager(\"#" + jsstr(id) + "\"); });");
        } else {
            write("<div class=\"n-p-info\">");
            write((String) params.get("emptyText"));
            write("</div>");
        }
        etag("div");
    }

    private void writePagerLimit() throws IOException {
        String onLimitChange = (String) params.get("onLimitChange");
        if (onLimitChange != null) {
            write("<td class=\"n-p-limitLabel\">");
            write((String) params.get("limitLabel"));
            write("</td>");
            write("<td class=\"n-p-limitSelect\">");
            Select select = new Select(stack, StrutsContextUtils.getServletRequest(), StrutsContextUtils.getServletResponse());
            StrutsContextUtils.getContainer(stack).inject(select);
            select.setTheme("simple");
            select.setId(id + "_limit");
            String limitName = (String) params.get("limitName");
            if (StringUtils.isEmpty(limitName)) {
                select.setName(id + "_limit");
            }
            select.setCssClass("select");
            select.setValue(limit.toString());
            select.setList(params.get("limitList"));
            select.setOnchange(onLimitChange);
            select.start(writer);
            select.end(writer, "");
            write("</td>");
        }
    }

    private void writePagerLinks() throws IOException {
        String linkStyle = defs((String) params.get("linkStyle"));
        Integer linkSize = (Integer) params.get("linkSize");
        Integer pages = (Integer) params.get("pages");
        Integer ipage = (Integer) params.get("page");
        write("<td");
        if (ipage <= 1 || !linkStyle.contains("f")) {
            write(" class=\"n-p-hidden\"");
        }
        write("><a href=\"#\" class=\"n-p-first\" pageno=\"1\">");
        write("<span class=\"ui-icon ui-icon-seek-first\" title=\"");
        write(getText("pager-tooltip-first", ""));
        write("\"></span></a></td>");
        write("<td");
        if (ipage <= 1) {
            write(" class=\"n-p-hidden\"");
        }
        write("><a href=\"#\" class=\"n-p-prev\" pageno=\"");
        write(String.valueOf(ipage - 1));
        write("\">");
        write("<span class=\"ui-icon ui-icon-triangle-1-w\" title=\"");
        write(getText("pager-tooltip-prev", ""));
        write("\"></span></a></td>");
        int p = 1;
        if (ipage > linkSize / 2) {
            p = ipage - (linkSize / 2);
        }
        if (p + linkSize > pages) {
            p = pages - linkSize;
        }
        if (p < 1) {
            p = 1;
        }
        linkp(ipage, 1);
        if (p == 1) {
            p = 2;
        } else if (p > 2) {
            write("<td><span class=\"n-p-ellipsis\">...</span></td>");
        }
        for (int i = 0; i < linkSize && p <= pages; i++, p++) {
            linkp(ipage, p);
        }
        if (p < pages) {
            write("<td><span class=\"n-p-ellipsis\">...</span></td>");
            linkp(ipage, pages);
        } else if (p == pages) {
            linkp(ipage, pages);
        }
        write("<td");
        if (ipage >= pages) {
            write(" class=\"n-p-hidden\"");
        }
        write("><a href=\"#\" class=\"n-p-next\" pageno=\"");
        write(String.valueOf(ipage + 1));
        write("\">");
        write("<span class=\"ui-icon ui-icon-triangle-1-e\" title=\"");
        write(getText("pager-tooltip-next", ""));
        write("\"></span></a></td>");
        write("<td");
        if (ipage >= pages || !linkStyle.contains("l")) {
            write(" class=\"n-p-hidden\"");
        }
        write("><a href=\"#\" class=\"n-p-last\" pageno=\"");
        write(pages.toString());
        write("\">");
        write("<span class=\"ui-icon ui-icon-seek-end\" title=\"");
        write(getText("pager-tooltip-last", ""));
        write("\"></span></a></td>");
    }

    private void linkp(int page, int p) throws IOException {
        write("<td><a href=\"#\" class=\"");
        if (p == page) {
            write("n-p-current ui-state-active");
        } else {
            write("n-p-pageno");
        }
        write("\" pageno=\"");
        write(String.valueOf(p));
        write("\">");
        write(String.valueOf(p));
        write("</a></td>");
    }
}
