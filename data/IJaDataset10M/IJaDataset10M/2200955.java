package org.devtools.wiki.translators;

import java.util.Properties;
import org.devtools.webtrans.HeaderTranslator;
import org.devtools.webtrans.StringChanges;
import org.devtools.webtrans.Translator;
import org.devtools.webtrans.TranslatorLog;
import org.devtools.webtrans.TranslatorServices;
import org.devtools.webtrans.WebContent;

/**
 * Inserts a stock look and feel header and footer.  The following
 * options are recognized:<BR><BR><TABLE>
 *
 * <TR><TD>widget.PutPage</TD><TD>SavePage</TD></TR>
 * <TR><TD>widget.EditPage</TD><TD>EditPage</TD></TR>
 * <TR><TD>widget.DeletePage</TD><TD>DeletorPage</TD></TR>
 * <TR><TD>widget.SearchResultsPage</TD><TD>SearchResultsPage</TD></TR>
 * <TR><TD>widget.RecentPage</TD><TD>RecentChanges</TD></TR>
 * <TR><TD>widget.FindPage</TD><TD>FindPage</TD></TR>
 * <TR><TD>widget.SiteName</TD><TD>DevWiki</TD></TR>
 * <TR><TD>widget.SiteLink</TD><TD><A HREF="/servlet/wiki"><IMG SRC="/images/devwikilogo.jpg" BORDER="0" WIDTH="100" HEIGHT="100"></A></TD></TR>
 * <TR><TD>widget.OrgLink</TD><TD><A HREF="http://www.devtools.org/"><IMG SRC="/images/devtools-white-blue-th.jpg" WIDTH="80"HEIGHT="28" BORDER="0"></A></TD></TR>
 * <TR><TD>widget.SiteMaster</TD><TD><A HREF="mailto:rus@devtools.org">Rus Heywood</A></TD></TR>
 * <TR><TD>widget.BarColor</TD><TD>#F0F0FF</TD></TR>
 * <TR><TD>widget.BgColor</TD><TD>#FFFFFF</TD></TR>
 * <TR><TD>widget.TextColor</TD><TD>#606060</TD></TR>
 * <TR><TD>widget.LinkColor</TD><TD>#0000FF</TD></TR>
 * <TR><TD>widget.VLinkColor</TD><TD>#600060</TD></TR>
 * <TR><TD>widget.BgImage</TD><TD></TD></TR>
 *
 * <TR><TD>widget.EditButton</TD><TD></TD></TR>
 * <TR><TD>widget.DeleteButton</TD><TD></TD></TR>
 * <TR><TD>widget.RecentButton</TD><TD><IMG SRC="/images/recent-button.jpg" BORDER=0></TD></TR>
 * <TR><TD>widget.FindButton</TD><TD><IMG SRC="/images/search-button.jpg" BORDER=0></TD></TR>
 *
 *
 * Expects the following parameters to be available from the
 * TranslatorServices:<UL>
 *
 * <LI><B>Wiki.FrontPage:</B> The name of the page that is the entry
 * point to the site.</LI>
 *
 * <LI><B>Wiki.EditPage:</B></LI> The name of the page that allows the
 * user to edit pages.
 *
 * <LI><B>Wiki.DeletorPage:</B></LI> The name of the page that will
 * delete pages.</LI></UL>
 *
 * @author rus@devtools.org
 *
 **/
public class StockLNFTranslator implements Translator, HeaderTranslator {

    private String baseURL;

    private String type;

    private String editPage;

    private String delPage;

    private String findPage;

    private String searchResultsPage;

    private String recentPage;

    private String siteName;

    private String siteMaster;

    private String siteLink;

    private String orgLink;

    private String cssLink;

    private String barColor;

    private String bgColor;

    private String textColor;

    private String linkColor;

    private String vlinkColor;

    private String bgImage;

    private String siteIcon;

    private String editButton;

    private String deleteButton;

    private String recentButton;

    private String findButton;

    private int bcount = 4;

    private boolean hasEdit = true;

    private boolean hasDelete = true;

    private boolean hasRecent = true;

    private boolean hasFind = true;

    /**
		 * Constructs a horizontal rule translator.
		 **/
    public StockLNFTranslator() {
    }

    /**
		 * A constructor-like initialization method.  The
		 * TranslatorManager will call this method exactly once, before
		 * any calls to translate().  The Translator may choose to do
		 * nothing during this method, or it may choose to save the
		 * reference to the provided TranslatorServices, which provide
		 * getters for commonly needed information such as the site's
		 * Persister, its base URL, the location of Special Pages such as
		 * the front page, search page, edit page, putter page,
		 * etc.<BR><BR>
		 *
		 * This method replaces a parametric constructor, so that
		 * Translators can be created using the getInstance() method on
		 * class Class, which requires a no-arg constructor.
		 *
		 * @param srv The services provided to this Translator.
		 *
		 * @see org.devtools.webtrans.TranslatorServices
		 **/
    public void init(TranslatorServices srv) {
        baseURL = srv.getBaseURL();
        type = srv.getType();
        editPage = srv.getProperty("EditPage");
        delPage = srv.getProperty("DeletePage");
        findPage = srv.getProperty("FindPage");
        searchResultsPage = srv.getProperty("SearchResultsPage");
        recentPage = srv.getProperty("RecentPage");
        siteName = srv.getProperty("SiteName");
        siteMaster = srv.getProperty("SiteMaster");
        siteLink = srv.getProperty("SiteLink");
        orgLink = srv.getProperty("OrgLink");
        cssLink = srv.getProperty("CSSLink");
        if (cssLink == null || cssLink.length() == 0) cssLink = baseURL + "../wiki.css";
        barColor = srv.getProperty("BarColor");
        bgColor = srv.getProperty("BgColor");
        textColor = srv.getProperty("TextColor");
        linkColor = srv.getProperty("LinkColor");
        vlinkColor = srv.getProperty("VLinkColor");
        bgImage = srv.getProperty("BgImage");
        siteIcon = srv.getProperty("SiteIcon");
        editButton = srv.getProperty("EditButton");
        deleteButton = srv.getProperty("DeleteButton");
        recentButton = srv.getProperty("RecentButton");
        findButton = srv.getProperty("FindButton");
        if (editButton == null || "".equals(editButton)) {
            --bcount;
            hasEdit = false;
        }
        if (deleteButton == null || "".equals(deleteButton)) {
            --bcount;
            hasDelete = false;
        }
        if (recentButton == null || "".equals(recentButton)) {
            --bcount;
            hasRecent = false;
        }
        if (findButton == null || "".equals(findButton)) {
            --bcount;
            hasFind = false;
        }
    }

    /**
		 *
		 *
		 * @see TranslatorLog
		 **/
    public void translate(StringChanges c, WebContent content, TranslatorLog log, Properties query) {
        StringBuffer buttonCells = new StringBuffer(100);
        if (hasEdit) buttonCells.append("<TD ALIGN=\"CENTER\" WIDTH=\"12%\">" + "<A HREF=\"" + baseURL + editPage + "?page=" + content.getTitle() + "&type=" + type + "\">" + editButton + "</A></TD>");
        if (hasDelete) buttonCells.append("<TD ALIGN=\"CENTER\" WIDTH=\"12%\">" + "<A HREF=\"" + baseURL + delPage + "?page=" + content.getTitle() + "&type=" + type + "\">" + deleteButton + "</A></TD>");
        if (hasRecent) buttonCells.append("<TD ALIGN=\"CENTER\" WIDTH=\"12%\">" + "<A HREF=\"" + baseURL + recentPage + "\">" + recentButton + "</A></TD>");
        if (hasFind) buttonCells.append("<TD ALIGN=\"CENTER\" WIDTH=\"12%\">" + "<A HREF=\"" + baseURL + findPage + "\">" + findButton + "</A></TD>");
        c.prepend("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML//EN\">\n" + "<HTML>\n" + "<HEAD>\n" + (siteIcon != null ? "<link REL=\"icon\" TYPE=\"image/gif\" HREF=\"" + siteIcon + "\">" : "") + "<TITLE>" + siteName + ": " + content.getTitle() + "</TITLE></HEAD>\n" + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssLink + "\">" + "<BODY BGCOLOR=\"" + bgColor + "\" TEXT=\"" + textColor + "\" LINK=\"" + linkColor + "\" VLINK=\"" + vlinkColor + "\" BACKGROUND=\"" + bgImage + "\">\n" + "<TABLE WIDTH=\"100%\" BORDER=\"0\">" + "<TR><TD WIDTH=\"100\" ROWSPAN=\"2\">" + siteLink + "</TD>" + "<TD WIDTH=\"100%\" VALIGN=\"BOTTOM\" ALIGN=\"LEFT\">" + "<FONT SIZE=\"+3\"><b><A HREF=\"" + searchResultsPage + "?search=" + content.getTitle() + "\">" + content.getTitle() + "</A>" + "</b></FONT></TD>" + "<TD WIDTH=\"50\" VALIGN=\"BOTTOM\" ALIGN=\"RIGHT\">" + orgLink + "</TD></TR>" + "<TR><TD ALIGN=\"LEFT\" COLSPAN=\"2\">" + "<TABLE WIDTH=\"100%\"><TR>" + "<TD BGCOLOR=\"" + barColor + "\" " + "ALIGN=\"LEFT\" WIDTH=\"" + (100 - bcount * 12) + "%\">" + "<FONT SIZE=\"-3\" COLOR=\"" + barColor + "\">.</FONT></TD>" + buttonCells.toString() + "</TR></TABLE>" + "</FONT></TD></TR>" + "</TABLE>" + "<BR>");
        c.append("<TABLE WIDTH=\"100%\"><TR>" + buttonCells.toString() + "<TD BGCOLOR=\"" + barColor + "\" ALIGN=\"RIGHT\" WIDTH=\"" + (100 - bcount * 12) + "%\"><FONT SIZE=\"-3\">" + siteName + " is maintained by " + siteMaster + "</FONT></TD></TR></TABLE>" + "</BODY>\n</HTML>\n");
        log.addDetail("stock look & feel added.");
    }

    /**
		 * Returns the name of the translator.  This should be something
		 * descriptive and human-readable that uniquely indicates what the
		 * translator does.  This name will be included in error logging
		 * if this translator has a conflict with another one, throws an
		 * exception, etc.
		 *
		 * @return The name of this translator
		 **/
    public String getName() {
        return "StockLNFTranslator";
    }

    /**
		 * Returns some HTML representing a description of what text this
		 * translator processes.  This will be shown to the user so that
		 * they can determine how to write their documents.  (If this
		 * translator has no usage or if you wish it to be invisible to
		 * the user, return null.  This Translator will not be included in
		 * the usage list.)
		 *
		 * @return The usage for this translator, as HTML.  Return null to
		 * hide this translator from the user.  (The translator will still
		 * be called on each document even if it is hidden.)
		 **/
    public String getUsage() {
        return "Pages will be formatted with\n" + "a custom header and footer.";
    }
}
