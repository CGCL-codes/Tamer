package dsrwebserver.components;

import java.sql.SQLException;
import ssmith.html.HTMLFuncs_old;
import ssmith.html.HTMLFunctions;
import dsr.AppletMain;
import dsrwebserver.DSRWebServer;
import dsrwebserver.pages.AbstractHTMLPage;
import dsrwebserver.pages.dsr.AbstractDSRPage;
import dsrwebserver.pages.dsr.register;
import dsrwebserver.tables.GamesTable;
import dsrwebserver.tables.MessagesTable;

public class MobileLayout {

    public static String GetHTML(AbstractHTMLPage page, String title, StringBuffer html) throws SQLException {
        StringBuffer str = new StringBuffer();
        if (page.session.isLoggedIn() == false) {
            if (page instanceof register == false) {
                str.append("<a href=\"/dsr/register.cls\"><img src=\"/dsr/gettitle.cls?t=Register%20here%20to%20play&h=30&ul=1\" border=0 alt=\"Register here to play\" /></a><br />");
                GetLoginBox(str, page);
            }
        } else {
            if (GamesTable.DoesPlayerHaveActions(page.dbs, page.current_login.getID())) {
                str.append("<span class=\"warning\"><a href=\"/dsr/MyGames.cls\">YOU HAVE ACTIONS!</a></span>");
            } else {
                str.append("<a href=\"/dsr/GameRequests.cls\"><span class=\"warning\">START ANOTHER GAME</span></a>");
            }
            if (MessagesTable.GetNumUnreadMessages(page.dbs, page.current_login.getID()) > 0) {
                str.append(" | <a href=\"/dsr/viewmessages.cls\"><span class=\"warning\"><img src=\"/images/mail.gif\" height=\"20\" border=\"0\" alt=\"Mail\" /></span></a>");
            }
        }
        if (title.length() > 0) {
            str.append("<h1>" + HTMLFunctions.s2HTML(title) + "</h1>");
        }
        str.append(html);
        str.append("<br clear=\"all\" />");
        if (page.session.isLoggedIn()) {
            str.append("<p>Logged in as: <a href=\"/dsr/mysettings.cls\">" + page.current_login.getDisplayName_Enc(false) + "</a></p>");
            if (GamesTable.DoesPlayerHaveActions(page.dbs, page.current_login.getID())) {
                str.append("<span class=\"warning\"><a href=\"/dsr/MyGames.cls\">YOU HAVE ACTIONS!</a></span>");
            } else {
                str.append("<a href=\"/dsr/GameRequests.cls\"><span class=\"warning\">START ANOTHER GAME</span></a>");
            }
            if (MessagesTable.GetNumUnreadMessages(page.dbs, page.current_login.getID()) > 0) {
                str.append(" | <a href=\"/dsr/viewmessages.cls\"><span class=\"warning\"><img src=\"/images/mail.gif\" height=\"20\" border=\"0\" alt=\"Mail\" /></span></a>");
            }
        }
        str.append("<div style=\"position: bottom\";\">");
        AppendMenuBar(page, str);
        str.append("</div>");
        return str.toString();
    }

    private static void AppendMenuBar(AbstractHTMLPage page, StringBuffer str) throws SQLException {
        str.append("<div class=\"mainmenu\">");
        str.append("<ul id=\"sddm\">");
        if (page.session.isLoggedIn()) {
            str.append("<li>Games");
            str.append("<a href=\"/dsr/MyGames.cls\">Current Games</a>");
            str.append("<a href=\"/dsr/GameRequests.cls\">Start New Game</a>");
            str.append("<a href=\"/dsr/finishedgames.cls\">Finished Games</a>");
            str.append("</li>");
        }
        if (page.session.isLoggedIn()) {
            str.append("<li>My Pages");
            str.append("<a href=\"/dsr/viewmessages.cls\">Messages</a>");
            str.append("<a href=\"/dsr/playerawards.cls\">Player Awards</a>");
            str.append("<a href=\"/dsr/mysettings.cls\">Settings</a>");
            str.append("</li>");
        }
        str.append("<li>Help");
        str.append("<a href=\"/dsr/about.cls\">About SF</a>");
        str.append("<a href=\"" + DSRWebServer.WIKI_LINK + "\">Manual</a>");
        str.append("<a href=\"" + DSRWebServer.FAQ_LINK + "\">FAQ</a>");
        str.append("</li>");
        str.append("<li>Stellarpedia");
        str.append("<a href=\"/dsr/missiondescriptions.cls\">The Missions</a>");
        str.append("<a href=\"/dsr/equipmentdetails.cls\">Weapons &amp; Equipment</a>");
        str.append("<a href=\"/dsr/armourdetails.cls\">Armour</a>");
        str.append("</li>");
        str.append("</ul><div style=\"clear:both\"></div>");
        str.append("</div>");
    }

    protected static void GetLoginBox(StringBuffer str, AbstractDSRPage page) {
        HTMLFunctions.StartForm(str, "form1", "/dsr/LoginPage.cls", "post");
        HTMLFunctions.StartTable(str);
        HTMLFunctions.StartRow(str);
        HTMLFunctions.AddCell(str, "<img src=\"/dsr/gettitle.cls?t=Login%20Here&h=20\" border=0 alt=\"Login Here\" />");
        HTMLFunctions.EndRow(str);
        String warning = page.conn.headers.getGetValueAsString("warning");
        if (warning.length() > 0) {
            HTMLFunctions.StartRow(str);
            HTMLFunctions.AddCell(str, warning);
            HTMLFunctions.EndRow(str);
        }
        HTMLFunctions.StartRow(str);
        HTMLFunctions.AddCell(str, "Email Address:");
        HTMLFunctions.EndRow(str);
        HTMLFunctions.StartRow(str);
        HTMLFunctions.AddCell(str, HTMLFuncs_old.TextBox("login", "", 50));
        HTMLFunctions.EndRow(str);
        HTMLFunctions.StartRow(str);
        HTMLFunctions.AddCell(str, "Password: ");
        HTMLFunctions.EndRow(str);
        HTMLFunctions.StartRow(str);
        HTMLFunctions.AddCell(str, HTMLFuncs_old.PasswordBox("pwd"));
        HTMLFunctions.EndRow(str);
        HTMLFunctions.StartRow(str);
        HTMLFunctions.AddCell(str, HTMLFuncs_old.SubmitButton("Login"));
        HTMLFunctions.EndRow(str);
        HTMLFunctions.StartRow(str);
        HTMLFunctions.AddCell(str, "<div class=\"little\"><a href=\"/dsr/forgottenpassword.cls\">Forgotten your password?</a></div>");
        HTMLFunctions.EndRow(str);
        HTMLFunctions.StartRow(str);
        HTMLFunctions.AddCell(str, "<h3><a href=\"/dsr/register.cls\">Register here</a></h3>");
        HTMLFunctions.EndRow(str);
        HTMLFunctions.EndTable(str);
        HTMLFunctions.EndForm(str);
    }
}
