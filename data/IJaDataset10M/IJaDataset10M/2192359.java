package com.sfeir.server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gdata.client.sites.ContentQuery;
import com.google.gdata.client.sites.SitesService;
import com.google.gdata.data.sites.ContentFeed;
import com.google.gdata.data.sites.WebPageEntry;
import com.sfeir.google.site.Util;
import com.sfeir.google.site.WebPage;
import com.sfeir.google.site.config.Configuration;
import com.sfeir.google.site.credential.CredentialManager;
import com.sfeir.google.site.enums.FeedKingEnum;

public class GoogleSiteServlet extends HttpServlet {

    String html = "";

    /**
	 * 
	 */
    private static final long serialVersionUID = 1776148091734248863L;

    public static final String APP_NAME = "sfeir-google-sites-datas";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = "/actualites";
        String pageName = null;
        if (s != null) {
            s = s.substring(s.indexOf("/") + 1);
            String[] mespages = s.split("/");
            pageName = mespages[mespages.length - 1];
        }
        List<WebPageEntry> listeWebPageEntry;
        try {
            SitesService service = new SitesService(APP_NAME);
            service.setUserCredentials(CredentialManager.getLogin(), CredentialManager.getMdp());
            ContentQuery query = new ContentQuery(new URL(getContentFeedUrl()));
            query.setKind(FeedKingEnum.webpage.name());
            ContentFeed contentFeed = service.getFeed(query, ContentFeed.class);
            listeWebPageEntry = contentFeed.getEntries(WebPageEntry.class);
            List<WebPage> pages = new ArrayList<WebPage>();
            pages = Util.transfomListEntryToPage(listeWebPageEntry);
            for (WebPage page : pages) {
                if (pageName != null && !pageName.equals("")) {
                    if (page.getName().equals(pageName)) {
                        html = Util.genererHTMLVelocityForOnePage(page);
                    }
                } else {
                    html = Util.genererHTMLVelocity(Util.retraiterLesPages(pages));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String gethtml() {
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        try {
            this.service(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    public String getContentFeedUrl() {
        return "http://sites.google.com/feeds/content/" + Configuration.getDomain() + "/" + Configuration.getSite() + "/";
    }
}
