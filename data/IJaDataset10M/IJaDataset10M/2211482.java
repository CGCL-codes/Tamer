package com.fantosoft.admin.service;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import com.fantosoft.admin.common.LoginUser;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MenuSystem {

    public static void displayMenu(JspWriter out, javax.servlet.http.HttpServletRequest request, List menuList) throws Exception {
        Menu menu;
        String contextPath = "";
        try {
            contextPath = request.getContextPath();
            HttpSession session = request.getSession(false);
            LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
            out.println("<link href=\"" + contextPath + "/css/menu.css\" rel=\"stylesheet\" type=\"text/css\">");
            out.println("<script language=javascript>");
            out.println("function runMenu(menuid)");
            out.println("{");
            out.println("	if(document.all(\"\"+menuid+\"\").style.display==\"none\")");
            out.println("		document.all(\"\"+menuid+\"\").style.display=\"\";");
            out.println("	else");
            out.println("		document.all(\"\"+menuid+\"\").style.display=\"none\";");
            out.println("}");
            out.println("</script>");
            out.println("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"menuTable\">");
            out.println("<tr><td>");
            long lastParentId = -1;
            for (int i = 0; i < menuList.size(); i++) {
                menu = (Menu) menuList.get(i);
                if (menu.getIfDefault().intValue() != 0 && !loginUser.getUserCode().equals("system")) continue;
                long parentId = menu.getParentId().longValue();
                if (lastParentId != -1) {
                    if (parentId == 0) {
                        out.println("		</table>");
                        out.println("	   </td>");
                        out.println("       <td width=\"1\" bgcolor=\"FFFFFF\"></td>");
                        out.println("   </tr>");
                        out.println("   <tr bgcolor=\"FFFFFF\"> ");
                        out.println("	  <td colspan=\"3\" class=menuNullLine></td>");
                        out.println("   </tr>");
                        out.println("   </table>");
                    }
                }
                if (parentId == 0) {
                    out.println("	<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"menuTable\">");
                    if (menu.getMenuUrl().equals("no_url")) {
                        out.println("	<tr onClick=\"runMenu('menu" + menu.getMenuId() + "')\" style=\"cursor:hand\">");
                        out.println("	  <td class=\"menuTitleTdLeft\">");
                        out.println("&nbsp;&nbsp;<b><a class=\"menuTitleLink\">" + menu.getMenuLocName() + "</a></b></td>");
                        out.println("	  <td class=\"menuTitleTdMiddle\"></td>");
                        out.println("	  <td width=\"25\"><img src=\"" + contextPath + "/images/menu_arrow_down.gif\" width=\"25\" height=\"25\" border=\"0\"></td>");
                    } else {
                        out.println("	<tr>");
                        out.println("	  <td class=\"menuTitleTdLeft\">");
                        out.println("&nbsp;&nbsp;<b><a href=\"" + contextPath + menu.getMenuUrl() + "\"  class=\"menuTitleLink\">" + menu.getMenuLocName() + "</a></b></td>");
                        out.println("	  <td class=\"menuTitleTdMiddle\"></td>");
                        out.println("	  <td width=\"25\"><img src=\"" + contextPath + "/images/menu_arrow_no.gif\" width=\"25\" height=\"25\" border=\"0\"></td>");
                    }
                    out.println("	</tr>");
                    out.println("	</table>");
                    out.println("	<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" class=\"menuTable\">");
                    out.println("	<tr> ");
                    out.println("	  <td width=\"1\" bgcolor=\"FFFFFF\"></td>");
                    out.println("	  <td class=\"menuBodyTd\">");
                    out.print("		<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"menuTableContent\" id=\"menu" + menu.getMenuId().longValue() + "\" style=\"display:'");
                    if (menu.getIfDefault() == null || menu.getIfDefault().longValue() == 0) out.print(""); else out.print("none");
                    out.print("'\">");
                    out.println();
                }
                if (parentId != 0) {
                    out.println("		  <tr> ");
                    out.println("			<td class=\"menuBodyContentTd\">&nbsp;&nbsp;<a href=\"" + menu.getMenuUrl() + "\"  class=\"menuBodyLink\">" + menu.getMenuName() + "</a></td>");
                    out.println("		  </tr>");
                }
                lastParentId = parentId;
            }
            if (lastParentId != -1) {
                out.println("		</table>");
                out.println("	   </td>");
                out.println("       <td width=\"1\" bgcolor=\"FFFFFF\"></td>");
                out.println("   </tr>");
                out.println("   <tr bgcolor=\"FFFFFF\"> ");
                out.println("	  <td colspan=\"3\" class=menuNullLine></td>");
                out.println("   </tr>");
                out.println("   </table>");
            }
            out.println("</td></tr>");
            out.println("</table>");
        } catch (Exception ex) {
            System.out.println("^^");
        }
    }
}
