package _admin._manager;

import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.sql.*;
import com.util.*;

public class _userlist__jsp extends com.caucho.jsp.JavaPage {

    private boolean _caucho_isDead;

    public void _jspService(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
        com.caucho.jsp.QPageContext pageContext = (com.caucho.jsp.QPageContext) com.caucho.jsp.QJspFactory.create().getPageContext(this, request, response, "/error.jsp", true, 8192, true);
        javax.servlet.jsp.JspWriter out = (javax.servlet.jsp.JspWriter) pageContext.getOut();
        javax.servlet.ServletConfig config = getServletConfig();
        javax.servlet.Servlet page = this;
        javax.servlet.http.HttpSession session = pageContext.getSession();
        javax.servlet.ServletContext application = pageContext.getServletContext();
        response.setContentType("text/html; charset=GBK");
        request.setCharacterEncoding("GBK");
        try {
            pageContext.write(_jsp_string0, 0, _jsp_string0.length);
            String pageno = null;
            if (request.getParameter("GotoPage") != null && !request.getParameter("GotoPage").equals("")) {
                pageno = request.getParameter("GotoPage");
            } else {
                pageno = request.getParameter("pageStart");
            }
            if (pageno == null) {
                pageno = "1";
            }
            int firstno = Integer.parseInt(pageno);
            String sql = "select admin_id ,admin_name as '用户名' ,admin_password as '密码' from admin";
            PageDisplay display = new PageDisplay();
            display.addRowClick("1", " target=\"_self\"", "Edit_User.jsp?UserNo=");
            display.init(request, firstno, 20, sql);
            display.setFormAction("../../servlet/user.user");
            String list = display.list();
            pageContext.write(_jsp_string1, 0, _jsp_string1.length);
            out.print((list));
            pageContext.write(_jsp_string2, 0, _jsp_string2.length);
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
        _caucho_line_map = new com.caucho.java.LineMap("_userlist__jsp.java", "/admin/manager/userlist.jsp");
        _caucho_line_map.add(1, 1);
        _caucho_line_map.add(1, 29);
        _caucho_line_map.add(51, 30);
        _caucho_line_map.add(73, 53);
        com.caucho.vfs.Depend depend;
        depend = new com.caucho.vfs.Depend(mergePath.lookup("webapps/myblog/admin/manager/userlist.jsp"), 1178890154000L, 2074L);
        _caucho_depends.add(depend);
    }

    protected void _caucho_clearDepends() {
        _caucho_depends.clear();
    }

    private static byte[] _jsp_string2;

    private static byte[] _jsp_string0;

    private static byte[] _jsp_string1;

    static {
        try {
            _jsp_string2 = "\r\n</body>\r\n</html>\r\n".getBytes("GBK");
            _jsp_string0 = "\r\n<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html>\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">\r\n<title>管理员列表</title>\r\n<style type=\"text/css\">\r\n<!--\r\n@import url(\"../images/css.css\");\r\n.style1 {font-size: 13px}\r\n-->\r\n</style>\r\n</head>\r\n<SCRIPT LANGUAGE=\"JavaScript\">\r\n<!--\r\nfunction gotopage(OtherArg){\r\n	if (OtherArg==null || OtherArg==\"null\")\r\n	{\r\n		OtherArg=\"\";\r\n	}\r\n	delfrm.action=\"userlist.jsp?action=gotopage\"+OtherArg;\r\n    delfrm.submit(); \r\n	}\r\nfunction delit(){\r\ndocument.delfrm.v1.value=\"\";\r\nif(document.delfrm.deleted.length){\r\n	for(i=0;i<document.delfrm.deleted.length;i++){\r\n		if(document.delfrm.deleted[i].checked){\r\n			document.delfrm.v1.value+=document.delfrm.deleted[i].value + \",\";\r\n		}//if\r\n	}//for\r\n}//if\r\nelse{\r\nif(document.delfrm.deleted.checked){\r\n	document.delfrm.v1.value+=document.delfrm.deleted.value + \",\";\r\n	}//if\r\n}//else\r\n	return confirm(\"你要删除的分组id为：\" + document.delfrm.v1.value);\r\n}\r\n//-->\r\nfunction selectidall()\r\n{\r\n	for(i=0;i<document.delfrm.deleted.length;i++)\r\n	{\r\n		document.delfrm.deleted[i].checked = !document.delfrm.deleted[i].checked;\r\n	}\r\n}\r\n\r\n</SCRIPT>\r\n<body background=\"\" class=\"table-bg style1\">\r\n".getBytes("GBK");
            _jsp_string1 = "\r\n".getBytes("GBK");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
