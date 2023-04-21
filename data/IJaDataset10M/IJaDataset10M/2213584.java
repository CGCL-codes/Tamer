package _admin._blog;

import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.sql.*;
import com.beans.log.*;
import com.business.log.*;

public class _edittag__jsp extends com.caucho.jsp.JavaPage {

    private boolean _caucho_isDead;

    public void _jspService(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
        com.caucho.jsp.QPageContext pageContext = (com.caucho.jsp.QPageContext) com.caucho.jsp.QJspFactory.create().getPageContext(this, request, response, "/admin/blog/", true, 8192, true);
        javax.servlet.jsp.JspWriter out = (javax.servlet.jsp.JspWriter) pageContext.getOut();
        javax.servlet.ServletConfig config = getServletConfig();
        javax.servlet.Servlet page = this;
        javax.servlet.http.HttpSession session = pageContext.getSession();
        javax.servlet.ServletContext application = pageContext.getServletContext();
        response.setContentType("text/html; charset=gb2312");
        request.setCharacterEncoding("GB2312");
        try {
            pageContext.write(_jsp_string0, 0, _jsp_string0.length);
            pageContext.write(_jsp_string1, 0, _jsp_string1.length);
            pageContext.write(_jsp_string2, 0, _jsp_string2.length);
            String tag = request.getParameter("id");
            int tagId = Integer.parseInt(tag);
            LogTagBus ltb = new LogTagBus();
            LogTag lt = ltb.getLogTagById(tagId);
            pageContext.write(_jsp_string3, 0, _jsp_string3.length);
            out.print((lt.getTagName()));
            pageContext.write(_jsp_string4, 0, _jsp_string4.length);
            out.print((tagId));
            pageContext.write(_jsp_string5, 0, _jsp_string5.length);
            out.print((lt.getViewCount()));
            pageContext.write(_jsp_string6, 0, _jsp_string6.length);
        } catch (java.lang.Throwable _jsp_e) {
            pageContext.handlePageException(_jsp_e);
        } finally {
            JspFactory.getDefaultFactory().releasePageContext(pageContext);
        }
    }

    private com.caucho.java.LineMap _caucho_line_map;

    private java.util.ArrayList _caucho_depends = new java.util.ArrayList();

    public boolean _caucho_isModified() {
        if (_caucho_isDead) return true;
        if (com.caucho.util.CauchoSystem.getVersionId() != -2089842223) return true;
        for (int i = _caucho_depends.size() - 1; i >= 0; i--) {
            com.caucho.vfs.Depend depend;
            depend = (com.caucho.vfs.Depend) _caucho_depends.get(i);
            if (depend.isModified()) return true;
        }
        return false;
    }

    public long _caucho_lastModified() {
        return 0;
    }

    public com.caucho.java.LineMap _caucho_getLineMap() {
        return _caucho_line_map;
    }

    public void destroy() {
        _caucho_isDead = true;
        super.destroy();
    }

    public void init(com.caucho.java.LineMap lineMap, com.caucho.vfs.Path appDir) throws javax.servlet.ServletException {
        com.caucho.vfs.Path resinHome = com.caucho.util.CauchoSystem.getResinHome();
        com.caucho.vfs.MergePath mergePath = new com.caucho.vfs.MergePath();
        mergePath.addMergePath(appDir);
        mergePath.addMergePath(resinHome);
        mergePath.addClassPath(getClass().getClassLoader());
        _caucho_line_map = new com.caucho.java.LineMap("_edittag__jsp.java", "/admin/blog/edittag.jsp");
        _caucho_line_map.add(5, 1);
        _caucho_line_map.add(1, 30);
        _caucho_line_map.add(4, 31);
        _caucho_line_map.add(15, 33);
        _caucho_line_map.add(24, 40);
        _caucho_line_map.add(27, 42);
        _caucho_line_map.add(28, 44);
        com.caucho.vfs.Depend depend;
        depend = new com.caucho.vfs.Depend(mergePath.lookup("webapps/myblog/admin/blog/edittag.jsp"), 1178965850000L, 1196L);
        _caucho_depends.add(depend);
    }

    protected void _caucho_clearDepends() {
        _caucho_depends.clear();
    }

    private static byte[] _jsp_string2;

    private static byte[] _jsp_string6;

    private static byte[] _jsp_string5;

    private static byte[] _jsp_string3;

    private static byte[] _jsp_string4;

    private static byte[] _jsp_string0;

    private static byte[] _jsp_string1;

    static {
        try {
            _jsp_string2 = "\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" />\r\n<title>无标题文档</title>\r\n</head>\r\n\r\n<body style=\"	font-size: 9pt;\r\n	line-height: 150%;\r\n	color: #000000;\r\n	text-decoration: none;\">\r\n	".getBytes("GB2312");
            _jsp_string6 = "\"/>\r\n       <p>\r\n\r\n	  <label>\r\n		<br />\r\n <input name=\"edit\" type=\"submit\" id=\"edit\" value=\" 编辑 \">\r\n      </label>\r\n  </p>\r\n</form>\r\n</body>\r\n</html>\r\n".getBytes("GB2312");
            _jsp_string5 = "\"/>\r\n	 	<input type=\"hidden\" name=\"viewcount\" value=\"".getBytes("GB2312");
            _jsp_string3 = "\r\n<form id=\"addlogform\" name=\"addlogform\" method=\"post\" action=\"/managetag\">\r\n  <p>Tag名称：\r\n    <label>\r\n    <input name=\"tagname\" type=\"text\" value=\"".getBytes("GB2312");
            _jsp_string4 = "\" />\r\n    </label>\r\n  </p>\r\n  	<input type=\"hidden\" name=\"id\" value=\"".getBytes("GB2312");
            _jsp_string0 = "\r\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n".getBytes("GB2312");
            _jsp_string1 = "\r\n".getBytes("GB2312");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
