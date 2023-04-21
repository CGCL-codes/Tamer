package com.vircon.myajax.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.vircon.myajax.web.mapping.Destination;
import com.vircon.myajax.web.mapping.PageMap;

public class PageUtils {

    public static PageMap getMapper(ServletContext context) {
        return (PageMap) context.getAttribute("mapper");
    }

    public static Page getPage(ServletContext context, HttpServletRequest request, String path) {
        PageMap mapper = (PageMap) context.getAttribute("mapper");
        String pagePolicy = (String) context.getInitParameter("pagePolicy");
        String pageName = mapper.getPageName(path);
        @SuppressWarnings("unchecked") Map<String, Page> pageTable = getPageTable(request);
        Page page = pageTable.get(pageName);
        if (page == null || "nocache".equals(pagePolicy)) {
            page = mapper.getPageByName(pageName);
            pageTable.put(pageName, page);
        }
        return page;
    }

    public static Page getPage(ServletContext context, HttpServletRequest request) {
        return PageUtils.getPage(context, request, stripContextPath(request));
    }

    private static Map<String, Page> getPageTable(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        @SuppressWarnings("unchecked") Map<String, Page> pageTable = (Map<String, Page>) session.getAttribute("pageTable");
        if (pageTable == null) {
            pageTable = new HashMap<String, Page>();
            session.setAttribute("pageTable", pageTable);
        }
        return pageTable;
    }

    public static Destination getDestination(ServletContext context, HttpServletRequest request, String name) {
        PageMap mapper = (PageMap) context.getAttribute("mapper");
        Destination dest = mapper.findDestination(stripContextPath(request), name);
        return dest;
    }

    public static String stripContextPath(HttpServletRequest request) {
        String url = request.getRequestURI();
        String newUrl = url;
        String contextPath = request.getContextPath();
        if (!(contextPath == null) || !"".equals(contextPath)) {
            newUrl = url.replaceFirst("^" + contextPath, "");
        }
        return newUrl;
    }
}
