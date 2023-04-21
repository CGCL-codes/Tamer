package _admin._blog;

import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import com.beans.photo.*;
import com.business.photo.*;

public class _editphotoreply__jsp extends com.caucho.jsp.JavaPage {

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
            pageContext.write(_jsp_string0, 0, _jsp_string0.length);
            pageContext.write(_jsp_string1, 0, _jsp_string1.length);
            String replyId = request.getParameter("id");
            PhotoReplyBus prb = new PhotoReplyBus();
            PhotoReply pr = prb.getReplyById(replyId);
            pageContext.write(_jsp_string2, 0, _jsp_string2.length);
            out.print((pr.getNickname()));
            pageContext.write(_jsp_string3, 0, _jsp_string3.length);
            out.print((pr.getReplyContent()));
            pageContext.write(_jsp_string4, 0, _jsp_string4.length);
            out.print((pr.getNickname()));
            pageContext.write(_jsp_string5, 0, _jsp_string5.length);
            out.print((pr.getPhotoNo()));
            pageContext.write(_jsp_string6, 0, _jsp_string6.length);
            out.print((replyId));
            pageContext.write(_jsp_string7, 0, _jsp_string7.length);
            out.print((pr.getReplyTime()));
            pageContext.write(_jsp_string8, 0, _jsp_string8.length);
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
        _caucho_line_map = new com.caucho.java.LineMap("_editphotoreply__jsp.java", "/admin/blog/editphotoreply.jsp");
        _caucho_line_map.add(3, 1);
        _caucho_line_map.add(1, 30);
        _caucho_line_map.add(15, 33);
        _caucho_line_map.add(22, 39);
        _caucho_line_map.add(25, 41);
        _caucho_line_map.add(28, 45);
        _caucho_line_map.add(29, 47);
        _caucho_line_map.add(30, 49);
        com.caucho.vfs.Depend depend;
        depend = new com.caucho.vfs.Depend(mergePath.lookup("webapps/myblog/admin/blog/editphotoreply.jsp"), 1178980250000L, 1402L);
        _caucho_depends.add(depend);
    }

    protected void _caucho_clearDepends() {
        _caucho_depends.clear();
    }

    private static byte[] _jsp_string8;

    private static byte[] _jsp_string3;

    private static byte[] _jsp_string6;

    private static byte[] _jsp_string7;

    private static byte[] _jsp_string1;

    private static byte[] _jsp_string5;

    private static byte[] _jsp_string2;

    private static byte[] _jsp_string0;

    private static byte[] _jsp_string4;

    static {
        try {
            _jsp_string8 = "\"/>\r\n       <p>\r\n\r\n	  <label>\r\n		<br />\r\n <input name=\"edit\" type=\"submit\" id=\"edit\" value=\" 编辑 \">\r\n      </label>\r\n  </p>\r\n</form>\r\n</body>\r\n</html>\r\n".getBytes("GBK");
            _jsp_string3 = "\r\n  </p>\r\n  <p>简要描述（100字符内）：\r\n    <textarea name=\"replycontent\" cols=\"40\" id=\"replycontent\">".getBytes("GBK");
            _jsp_string6 = "\"/>\r\n	<input type=\"hidden\" name=\"id\" value=\"".getBytes("GBK");
            _jsp_string7 = "\"/>\r\n	<input type=\"hidden\" name=\"replytime\" value=\"".getBytes("GBK");
            _jsp_string1 = "\r\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\" />\r\n<title>修改图片回复</title>\r\n</head>\r\n\r\n<body style=\"	font-size: 9pt;\r\n	line-height: 150%;\r\n	color: #000000;\r\n	text-decoration: none;\">\r\n".getBytes("GBK");
            _jsp_string5 = "\"/>\r\n	<input type=\"hidden\" name=\"photono\" value=\"".getBytes("GBK");
            _jsp_string2 = "\r\n<form id=\"addlogform\" name=\"addlogform\" method=\"post\" action=\"/managephotoreply\">\r\n  <p>回复者：\r\n    ".getBytes("GBK");
            _jsp_string0 = "\r\n".getBytes("GBK");
            _jsp_string4 = "</textarea>\r\n  </p>\r\n  	<input type=\"hidden\" name=\"nickname\" value=\"".getBytes("GBK");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
