package com.icovganet.imob1.util;

import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javax.servlet.jsp.JspWriter;
import com.google.appengine.api.users.User;
import com.icovganet.imob1.bean.Advert;
import com.icovganet.imob1.bean.UserDetails;

public class JspTemplates {

    public static void printAdvert(JspWriter out, Advert[] adverts, User user, ResourceBundle langs) throws Exception {
        UserDetails userDetails = user == null ? null : UserFactory.getFactory().getUser(user.getUserId());
        SimpleDateFormat fmt = new SimpleDateFormat(ImobConstants.DATE_FORMAT);
        for (Advert adv : adverts) {
            out.println("<li><div>");
            if (user != null) {
                if (user.getNickname().equalsIgnoreCase(adv.getAuthor())) {
                    out.print("<span class=\"advButton\">");
                    out.print("<a href=\"/delete?ids=" + adv.getId() + "\"><img src=\"/img/trash_(delete).gif\" title=\"" + langs.getString("delete") + "\" /></a>");
                    out.print("<a href=\"addAdvert.jsp?id=" + adv.getId() + "\"><img src=\"/img/edit.png\" title=\"" + langs.getString("edit") + "\" /></a>");
                    out.print("</span>");
                }
            }
            out.println("<h3><a href=\"showAdvert.jsp?id=" + adv.getId() + "\">" + adv.getTitle() + "</a></h3>");
            out.print("<div class=\"artinfotop\">");
            out.print("<span class=\"date\">" + fmt.format(adv.getDate()) + "</span>");
            if (adv.getCategoryId() != null) {
                int catId = adv.getCategoryId().intValue();
                out.print("<span class=\"category\"> <a href=\"showAdverts.jsp?catId=" + catId + "\">" + langs.getString("menu1_" + ImobConstants.CAT_NAME[catId / 100]) + "(" + langs.getString("menu1_" + ImobConstants.SUBCAT_NAME[catId % 100]) + ")</a></span>");
            } else {
                out.print(ImobConstants.UNDEFINED);
            }
            out.print("</div>");
            out.print("<p>");
            if (adv.getShortContent() != null) {
                out.print(adv.getShortContent());
            } else {
                out.print(ImobConstants.UNDEFINED);
            }
            out.print("</p>");
            out.print("<div class=\"artinfobottom\">");
            out.print("<span class=\"author\">" + langs.getString("by") + ": <a href=\"#" + adv.getId() + "\">" + adv.getAuthor() + "</a></span>");
            out.print("<span class=\"advButton\">");
            if (user != null) {
                if (userDetails == null || !userDetails.isFavorite(adv)) {
                    out.print("<a href=\"/favorite?action=add&ids=" + adv.getId() + "\"><img src=\"/img/heart_add.png\" title=\"" + langs.getString("favorite_add") + "\" /></a>");
                } else {
                    out.print("<a href=\"/favorite?action=delete&ids=" + adv.getId() + "\"><img src=\"/img/heart_delete.png\" title=\"" + langs.getString("favorite_delete") + "\" /></a>");
                }
            }
            out.print("</span>");
            out.print("</div>");
            out.println("</div></li>");
        }
    }
}
