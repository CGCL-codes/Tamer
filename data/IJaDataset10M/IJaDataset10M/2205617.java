package _admin._blog;

import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import com.beans.music.*;
import com.business.music.*;

public class _editmusic__jsp extends com.caucho.jsp.JavaPage {

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
            pageContext.write(_jsp_string1, 0, _jsp_string1.length);
            pageContext.write(_jsp_string2, 0, _jsp_string2.length);
            String musicNo = request.getParameter("no");
            MusicListBus mlb = new MusicListBus();
            MusicInfo mi = mlb.getMusicByNo(musicNo);
            pageContext.write(_jsp_string3, 0, _jsp_string3.length);
            out.print((mi.getMusicName()));
            pageContext.write(_jsp_string4, 0, _jsp_string4.length);
            out.print((mi.getMusicMemo()));
            pageContext.write(_jsp_string5, 0, _jsp_string5.length);
            MusicTypeBus ptl = new MusicTypeBus();
            String username = mi.getUsername();
            List typeList = ptl.getAllTypes(username);
            for (int i = 0; i < typeList.size(); i++) {
                MusicType pt = (MusicType) typeList.get(i);
                pageContext.write(_jsp_string6, 0, _jsp_string6.length);
                out.print((pt.getMusictypeId()));
                pageContext.write(_jsp_string7, 0, _jsp_string7.length);
                if (mi.getMusictypeId().equals(pt.getMusictypeId())) {
                    pageContext.write(_jsp_string8, 0, _jsp_string8.length);
                }
                pageContext.write(_jsp_string9, 0, _jsp_string9.length);
                out.print((pt.getMusictypeName()));
                pageContext.write(_jsp_string10, 0, _jsp_string10.length);
            }
            pageContext.write(_jsp_string11, 0, _jsp_string11.length);
            if (mi.getIsShow().equals("1")) {
                pageContext.write(_jsp_string12, 0, _jsp_string12.length);
            }
            pageContext.write(_jsp_string13, 0, _jsp_string13.length);
            out.print((username));
            pageContext.write(_jsp_string14, 0, _jsp_string14.length);
            out.print((musicNo));
            pageContext.write(_jsp_string15, 0, _jsp_string15.length);
            out.print((mi.getViewCount()));
            pageContext.write(_jsp_string16, 0, _jsp_string16.length);
            out.print((mi.getMusicUrl()));
            pageContext.write(_jsp_string17, 0, _jsp_string17.length);
            out.print((mi.getClassName()));
            pageContext.write(_jsp_string18, 0, _jsp_string18.length);
            out.print((mi.getSinger()));
            pageContext.write(_jsp_string19, 0, _jsp_string19.length);
            out.print((mi.getLrcUrl()));
            pageContext.write(_jsp_string20, 0, _jsp_string20.length);
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
        _caucho_line_map = new com.caucho.java.LineMap("_editmusic__jsp.java", "/admin/blog/editmusic.jsp");
        _caucho_line_map.add(5, 1);
        _caucho_line_map.add(1, 30);
        _caucho_line_map.add(4, 31);
        _caucho_line_map.add(33, 33);
        _caucho_line_map.add(42, 39);
        _caucho_line_map.add(47, 41);
        _caucho_line_map.add(52, 43);
        _caucho_line_map.add(59, 51);
        _caucho_line_map.add(59, 53);
        _caucho_line_map.add(59, 55);
        _caucho_line_map.add(59, 57);
        _caucho_line_map.add(60, 59);
        _caucho_line_map.add(68, 63);
        _caucho_line_map.add(68, 65);
        _caucho_line_map.add(71, 69);
        _caucho_line_map.add(72, 71);
        _caucho_line_map.add(78, 73);
        _caucho_line_map.add(84, 75);
        _caucho_line_map.add(89, 77);
        _caucho_line_map.add(96, 79);
        com.caucho.vfs.Depend depend;
        depend = new com.caucho.vfs.Depend(mergePath.lookup("webapps/myblog/admin/blog/editmusic.jsp"), 1179042446000L, 3291L);
        _caucho_depends.add(depend);
    }

    protected void _caucho_clearDepends() {
        _caucho_depends.clear();
    }

    private static byte[] _jsp_string9;

    private static byte[] _jsp_string5;

    private static byte[] _jsp_string15;

    private static byte[] _jsp_string20;

    private static byte[] _jsp_string3;

    private static byte[] _jsp_string11;

    private static byte[] _jsp_string8;

    private static byte[] _jsp_string1;

    private static byte[] _jsp_string18;

    private static byte[] _jsp_string2;

    private static byte[] _jsp_string4;

    private static byte[] _jsp_string0;

    private static byte[] _jsp_string19;

    private static byte[] _jsp_string14;

    private static byte[] _jsp_string10;

    private static byte[] _jsp_string16;

    private static byte[] _jsp_string12;

    private static byte[] _jsp_string17;

    private static byte[] _jsp_string6;

    private static byte[] _jsp_string7;

    private static byte[] _jsp_string13;

    static {
        try {
            _jsp_string9 = ">".getBytes("GBK");
            _jsp_string5 = "</textarea>\r\n  </label>\r\n  <p class=\"STYLE2\">所在相册：\r\n    <label>\r\n	<select name=\"musictypeid\" >\r\n	".getBytes("GBK");
            _jsp_string15 = "\"/>\r\n	   <input name=\"viewcount\" type=\"hidden\" value=\"".getBytes("GBK");
            _jsp_string20 = "\" />\r\n        </label>\r\n      </p>\r\n  <p>\r\n   <label>\r\n		<br />\r\n  <center> <input type=\"submit\"  value=\" 提交 \"></center>\r\n      </label>\r\n</p>\r\n</form>\r\n</body>\r\n</html>\r\n".getBytes("GBK");
            _jsp_string3 = "\r\n<form action=\"/managemusic\" method=\"post\" enctype=\"multipart/form-data\" name=\"addmusicform\" id=\"addmusicform\">\r\n  <p><b>您将修改音乐相关信息</b></p>\r\n  <p>音乐名：\r\n    <label>\r\n    <input name=\"musicname\" type=\"text\" id=\"musicname\" value=\"".getBytes("GBK");
            _jsp_string11 = "\r\n	</select>\r\n	</label>\r\n  </p>\r\n  <p>是否显示\r\n    <label>\r\n    <input name=\"isshow\" type=\"checkbox\" id=\"isshow\" value=\"1\" ".getBytes("GBK");
            _jsp_string8 = "selected".getBytes("GBK");
            _jsp_string1 = "\r\n".getBytes("GBK");
            _jsp_string18 = "\"/>\r\n        </label>\r\n      </p>\r\n      <p>歌手名：\r\n        <label>\r\n        <input name=\"singer\" type=\"text\" id=\"singer\"  value=\"".getBytes("GBK");
            _jsp_string2 = "\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\" />\r\n<title>修改音乐</title>\r\n<style type=\"text/css\">\r\n<!--\r\n.STYLE1 {\r\n	color: #FF0000;\r\n	font-weight: bold;\r\n}\r\n.STYLE2 {color: #000000}\r\n-->\r\n</style>\r\n\r\n<script type=\"text/JavaScript\">\r\n<!--\r\nfunction MM_jumpMenu(targ,selObj,restore){ //v3.0\r\n  eval(targ+\".location='\"+selObj.options[selObj.selectedIndex].value+\"'\");\r\n  if (restore) selObj.selectedIndex=0;\r\n}\r\n//-->\r\n</script>\r\n</head>\r\n\r\n<body style=\"	font-size: 9pt;\r\n	line-height: 150%;\r\n	color: #000000;\r\n	text-decoration: none;\">\r\n".getBytes("GBK");
            _jsp_string4 = "\"/>\r\n    </label>\r\n  </p>\r\n  <p >备注：</p>\r\n  <label>\r\n  <textarea name=\"musicmemo\" cols=\"40\" rows=\"4\" id=\"musicmemo\" >".getBytes("GBK");
            _jsp_string0 = "\r\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n".getBytes("GBK");
            _jsp_string19 = "\"/>\r\n        </label>\r\n      </p>\r\n	   <p>歌词上传：\r\n        <label>\r\n        <input name=\"file\" type=\"file\"   />\r\n        (请使用txt文件)\r\n		<input name=\"lrcurl\" type=\"hidden\" value=\"".getBytes("GBK");
            _jsp_string14 = "\" />\r\n	   <input name=\"no\" type=\"hidden\" value=\"".getBytes("GBK");
            _jsp_string10 = "</option>\r\n	".getBytes("GBK");
            _jsp_string16 = "\" />\r\n	   <input name=\"action\" type=\"hidden\" value=\"edit\" />\r\n  </p>\r\n  <p>\r\n    <label>请选择上传的音乐文件：\r\n    <input type=\"file\" name=\"file\" />\r\n	<input name=\"musicurl\" type=\"hidden\" value=\"".getBytes("GBK");
            _jsp_string12 = "checked=\"checked\" ".getBytes("GBK");
            _jsp_string17 = "\" />\r\n    </label>\r\n  </p>\r\n  <p>此音乐相关信息填写：（选填）</p>\r\n      <p>音乐专辑名：\r\n        <label>\r\n        <input name=\"classname\" type=\"text\" id=\"classname\" value=\"".getBytes("GBK");
            _jsp_string6 = "\r\n	<option value=\"".getBytes("GBK");
            _jsp_string7 = "\" ".getBytes("GBK");
            _jsp_string13 = "/>\r\n    </label>\r\n       <input name=\"username\" type=\"hidden\" id=\"username\" value=\"".getBytes("GBK");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
