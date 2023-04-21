package lebah.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpSession;
import lebah.portal.db.RegisterModule;
import org.apache.velocity.Template;

public class HtmlProxyAjaxModule extends lebah.portal.AjaxBasedModule implements lebah.portal.HtmlContainer {

    private String strUrl = "";

    public void setUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    public String doTemplate2() throws Exception {
        HttpSession session = request.getSession();
        String isLogin = (String) session.getAttribute("_portal_islogin");
        String userId = (String) session.getAttribute("_portal_login");
        context.put("userId", userId);
        String moduleId = getId();
        context.put("isLogin", "true".equals(isLogin) ? Boolean.TRUE : Boolean.FALSE);
        context.put("formname", getId());
        String submit = getParam("command");
        String formname = getParam(getId());
        if (!formname.equals(getId())) submit = "";
        if ("changeProperties".equals(submit)) {
            strUrl = getParam("url");
            changeProperties(moduleId, strUrl);
        }
        context.put("url", strUrl);
        if (getParam("__page_url") != null && !"".equals(getParam("__page_url"))) {
            strUrl = getParam("__page_url");
        }
        try {
            doJob(session);
        } catch (Exception e) {
            context.put("stringbuffer", e.getMessage());
        }
        context.put("stringbuffer", null);
        return "vtl/url/url_ajaxbased.vm";
    }

    private void doJob(HttpSession session) throws Exception {
        String pathInfo = request.getPathInfo();
        pathInfo = pathInfo.substring(1);
        String action = pathInfo != null ? pathInfo : "";
        URL url = null;
        try {
            if (strUrl.indexOf("http://") == -1) {
                strUrl = "http://" + (String) session.getAttribute("_portal_server") + strUrl;
            }
            url = new URL(strUrl);
            if (strUrl.length() < 8) throw new Exception("Invalid URL");
            String last4 = strUrl.substring(strUrl.length() - 4);
            String last5 = strUrl.substring(strUrl.length() - 5);
            int last_bslash = strUrl.lastIndexOf("/");
            String url2 = strUrl.substring(0, last_bslash);
            InputStream content = (InputStream) url.getContent();
            StringBuffer buf = new StringBuffer();
            int ch = 0, prevchar = 0;
            while ((ch = content.read()) != -1) {
                buf.append((char) ch);
            }
            String str = buf.toString().toLowerCase();
            if ((str.indexOf("<style") > -1) && (str.indexOf("</style>") > -1)) {
                String str1 = buf.substring(0, str.indexOf("<style"));
                String str2 = buf.substring(str.indexOf("</style>") + "</style>".length());
                buf = new StringBuffer(str1).append(str2);
            }
            replacetoFullURL(buf, "<a href", url2, action);
            replacetoFullURL(buf, "<a HREF", url2, action);
            replacetoFullURL(buf, "<A HREF", url2, action);
            replacetoFullURL(buf, "<img src", url2);
            replacetoFullURL(buf, "<img SRC", url2);
            replacetoFullURL(buf, "<IMG SRC", url2);
            replacetoFullURL(buf, "<embed src", url2);
            replacetoFullURL(buf, "<embed SRC", url2);
            replacetoFullURL(buf, "<EMBED SRC", url2);
            replacetoFullURL(buf, "<param name=movie value", url2);
            context.put("stringbuffer", buf);
        } catch (MalformedURLException e1) {
            throw new Exception("MalformedURLException: " + e1.getMessage());
        } catch (IOException e2) {
            throw new Exception("IOException: " + e2.getMessage());
        }
    }

    private void replacetoFullURL(StringBuffer contentBuffer, String str_tag, String url2, String action) {
        String str = contentBuffer.toString();
        int pos_start = 0;
        while (true) {
            int tag_open = str.indexOf(str_tag, pos_start);
            if (tag_open == -1) break;
            StringBuffer sb = new StringBuffer();
            int cnt = 0;
            for (cnt = tag_open; cnt < str.length(); cnt++) {
                sb.append(str.charAt(cnt));
                if (str.charAt(cnt) == '>') break;
            }
            int tag_close = cnt;
            str = sb.toString();
            int first_eq = str.indexOf("=");
            String eq_left = str.substring(0, first_eq).trim();
            String eq_right = str.substring(first_eq + 1).trim();
            String url_root = url2;
            int first_bslash = url2.indexOf("/", "http://".length() + 1);
            if (first_bslash > 0) url_root = url2.substring(0, first_bslash);
            if (eq_right.length() > "http://".length() + 1) {
                if (eq_right.charAt(0) == '\"') {
                    if (!"http://".equals(eq_right.substring(1, "http://".length() + 1)) && !"mailto:".equals(eq_right.substring(1, "mailto:".length() + 1))) {
                        if (eq_right.startsWith("\"/")) {
                            eq_right = "\"" + url_root + eq_right.substring(1);
                        } else {
                            eq_right = "\"" + url2 + "/" + eq_right.substring(1);
                        }
                    }
                } else {
                    if (!"http://".equals(eq_right.substring(0, "http://".length() + 1)) && !"mailto:".equals(eq_right.substring(0, "mailto:".length() + 1))) {
                        if (eq_right.startsWith("\"/")) {
                            eq_right = url_root + eq_right.substring(0);
                        } else {
                            eq_right = url2 + "/" + eq_right;
                        }
                    }
                }
            }
            String uri = request.getRequestURI();
            eq_right = eq_right.substring(1);
            String s = "loadURL" + formName + "('" + eq_right.substring(0, eq_right.length() - 2) + "')\">";
            str = eq_left + "=\"javascript:" + s;
            pos_start = tag_open + str.length();
            contentBuffer.replace(tag_open, tag_close + 1, str);
            str = contentBuffer.toString();
        }
    }

    private void replacetoFullURL(StringBuffer contentBuffer, String str_tag, String url2) {
        String str = contentBuffer.toString();
        int pos_start = 0;
        while (true) {
            int tag_open = str.indexOf(str_tag, pos_start);
            if (tag_open == -1) break;
            StringBuffer sb = new StringBuffer();
            int cnt = 0;
            for (cnt = tag_open; cnt < str.length(); cnt++) {
                sb.append(str.charAt(cnt));
                if (str.charAt(cnt) == '>') break;
            }
            int tag_close = cnt;
            str = sb.toString();
            int first_eq = 0;
            first_eq = str.indexOf("=");
            if (str_tag.equals("<param name=movie value")) {
                first_eq = "<param name=movie value".length();
            }
            String eq_left = str.substring(0, first_eq).trim();
            if ("<a ".equals(eq_left.substring(0, "<a ".length()))) {
                eq_left = "<a target=\"_new\" " + eq_left.substring("<a ".length());
            }
            String eq_right = str.substring(first_eq + 1).trim();
            String url_root = url2;
            int first_bslash = url2.indexOf("/", "http://".length() + 1);
            if (first_bslash > 0) url_root = url2.substring(0, first_bslash);
            if (eq_right.length() > "http://".length() + 1) {
                if (eq_right.charAt(0) == '\"') {
                    if (!"http://".equals(eq_right.substring(1, "http://".length() + 1)) && !"mailto:".equals(eq_right.substring(1, "mailto:".length() + 1))) {
                        if (eq_right.startsWith("\"/")) {
                            eq_right = "\"" + url_root + eq_right.substring(1);
                        } else {
                            eq_right = "\"" + url2 + "/" + eq_right.substring(1);
                        }
                    }
                } else {
                    if (!"http://".equals(eq_right.substring(0, "http://".length() + 1)) && !"mailto:".equals(eq_right.substring(0, "mailto:".length() + 1))) {
                        if (eq_right.startsWith("\"/")) {
                            eq_right = url_root + eq_right.substring(0);
                        } else {
                            eq_right = url2 + "/" + eq_right;
                        }
                    }
                }
            }
            str = eq_left + "=" + eq_right;
            pos_start = tag_open + str.length();
            contentBuffer.replace(tag_open, tag_close + 1, str);
            str = contentBuffer.toString();
        }
    }

    void changeProperties(String id, String url) throws Exception {
        RegisterModule.updateHtmlLocation(id, url);
    }
}
