package net.itsite.search;

import java.util.LinkedHashMap;
import java.util.Map;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.web.page.PageRequestResponse;

public class SearchUtils {

    public static Map<String, String> dataMap = new LinkedHashMap<String, String>();

    static {
        dataMap.put("os", "软件");
        dataMap.put("cs", "代码");
        dataMap.put("docu", "文档");
        dataMap.put("question", "问答");
        dataMap.put("news", "资讯");
        dataMap.put("blog", "博客");
        dataMap.put("bbs", "社区");
    }

    public static String witchSearch(final PageRequestResponse requestResponse) {
        final String url = HTTPUtils.getRequestURI(requestResponse.request);
        final String h = requestResponse.getContextPath();
        if (url != null) {
            if (url.startsWith(h + "/news")) {
                return "news";
            } else if (url.startsWith(h + "/blog")) {
                return "blog";
            } else if (url.startsWith(h + "/bbs")) {
                return "bbs";
            } else if (url.startsWith(h + "/os")) {
                return "os";
            } else if (url.startsWith(h + "/cs")) {
                return "cs";
            } else if (url.startsWith(h + "/docu")) {
                return "docu";
            } else if (url.startsWith(h + "/question")) {
                return "question";
            }
        }
        return "os";
    }

    public static String getApplictionUrl(final String id) {
        if ("os".equals(id)) {
            return "/os.html?s=1&c=";
        } else if ("cs".equals(id)) {
            return "/cs.html?c=";
        } else if ("docu".equals(id)) {
            return "docusearch.html?docu_=all&c=";
        } else if ("question".equals(id)) {
            return "/question.html?c=";
        } else if ("news".equals(id)) {
            return "/news.html?c=";
        } else if ("blog".equals(id)) {
            return "/blog.html?c=";
        } else if ("bbs".equals(id)) {
            return "/bbs/tl.html?c=";
        } else {
            return "/os.html?s=1&c=";
        }
    }
}
