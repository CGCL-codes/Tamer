package com.c2b2.ipoint.presentation.portlets;

import com.c2b2.ipoint.business.MediaServices;
import com.c2b2.ipoint.model.Article;
import com.c2b2.ipoint.model.BlogEntry;
import com.c2b2.ipoint.model.ContentVersion;
import com.c2b2.ipoint.model.Media;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.presentation.MediaURL;
import com.c2b2.ipoint.presentation.PresentationException;
import com.c2b2.ipoint.presentation.forms.fieldtypes.BooleanField;
import com.c2b2.ipoint.processing.PortalRequest;
import com.c2b2.ipoint.util.ContentScanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.DocumentException;

/**
  * $Id: ArticlesPortlet.java,v 1.11 2007/07/03 16:57:46 steve Exp $
  *
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  *
  * This class handles the set up of the Article Portlet
  *
  * @author $Author: steve $
  * @version $Revision: 1.11 $
  * $Date: 2007/07/03 16:57:46 $
  *
  */
public class ArticlesPortlet extends ContentRenderer {

    public static final String ARTICLE_PARAMETER = "ArticleID";

    public ArticlesPortlet() {
    }

    public boolean canRender() throws PresentationException {
        return super.canRender();
    }

    public void initialiseNew() throws PresentationException {
        try {
            Article.createArticle(myPortlet.getName(), "New Article", myPortlet, myPR.getCurrentUser());
            myPortlet.storeProperty("ShowRSSFeed", "true", null, null);
        } catch (PersistentModelException e) {
            throw new PresentationException("Unable to Initialise Articles Portlet with a new article", e);
        }
    }

    public List getValidProperties() {
        List result = super.getValidProperties();
        String showRss = "False";
        if (myRSSMode == true) {
            showRss = "True";
        }
        result.add(new BooleanField("ShowRSSFeed", "Show RSS Feed", showRss));
        return result;
    }

    public void renderContent() throws PresentationException {
        String jsp = myPortlet.getType().getInclude();
        try {
            myPR.includeJSP(jsp);
        } catch (IOException e) {
            throw new PresentationException("Unable to write output", e);
        } catch (ServletException e) {
            throw new PresentationException("Unable to process the JSP " + jsp, e);
        }
    }

    public void renderEdit() throws PresentationException {
        renderContent();
    }

    public void renderHelp() throws PresentationException {
        String jsp = myPortlet.getType().getHelpJSP();
        try {
            myPR.includeJSP(jsp);
        } catch (IOException e) {
            throw new PresentationException("Unable to write output", e);
        } catch (ServletException e) {
            throw new PresentationException("Unable to process the JSP " + jsp, e);
        }
    }

    public void renderMinimized() throws PresentationException {
        String jsp = myPortlet.getType().getMinimizedJSP();
        try {
            myPR.includeJSP(jsp);
        } catch (IOException e) {
            throw new PresentationException("Unable to write output", e);
        } catch (ServletException e) {
            throw new PresentationException("Unable to process the JSP " + jsp, e);
        }
    }

    public void preProcess() throws PresentationException {
        myPR.requireStyle("article.css");
        myPR.requireStyle("content.css");
        try {
            super.preProcess();
            String rssProperty = myPortlet.getDefinitiveProperty("ShowRSSFeed", getCurrentUser(), getCurrentPage());
            if (rssProperty != null) {
                if (rssProperty.equalsIgnoreCase("true")) {
                    myRSSMode = true;
                }
            } else {
                myRSSMode = false;
            }
            String portletStr = (String) myPR.getParameter("portlet");
            if (portletStr != null) {
                long portletID = Long.parseLong(portletStr);
                if (portletID == myPortlet.getID()) {
                    handlePost();
                }
            }
            myArticles = Article.findArticlesForPortlet(myPortlet);
            if (this.isEditableByCurrentUser()) {
                myVisibleArticles = myArticles;
            } else {
                myVisibleArticles = new ArrayList();
                Iterator<Article> mine = myArticles.iterator();
                while (mine.hasNext()) {
                    Article a = mine.next();
                    Date now = new Date();
                    ContentVersion publishedVersion = a.getSynopsisContent().getPublishedVersion();
                    if ((a.getSynopsisContent().isVisibleTo(myPR.getCurrentUser()) && publishedVersion.getValidFrom().before(now) && publishedVersion.getValidTo().after(now)) || a.getSynopsisContent().isEditableBy(myPR.getCurrentUser())) {
                        myVisibleArticles.add(a);
                    }
                }
            }
            loadCurrentArticle();
            Article currentArticle = getCurrentArticle();
            if (currentArticle != null) {
                addContentMetadata(currentArticle.getContent());
            } else {
                Iterator articles = myArticles.iterator();
                while (articles.hasNext()) {
                    Article article = (Article) articles.next();
                    if (article.getSynopsisContent().getPublishedVersion().getValidTo().after(new Date())) {
                        addContentMetadata(article.getSynopsisContent());
                    }
                }
            }
        } catch (PersistentModelException e) {
            throw new PresentationException("Unable to find articles for this portlet", e);
        }
    }

    public Article getCurrentArticle() {
        return myCurrentArticle;
    }

    public static String getArticleBodyURL(Article article, ArticlesPortlet portlet) {
        PortalURL url = new PortalURL(portlet.getPortlet());
        url.setQueryStringParameter(ARTICLE_PARAMETER, Long.toString(article.getID()));
        return url.toString();
    }

    public static String getArticleListURL(ArticlesPortlet portlet) {
        PortalURL pu = new PortalURL(portlet.getPortlet());
        pu.removeQueryStringParameter(ARTICLE_PARAMETER);
        String href = pu.getURL();
        return href;
    }

    /**
   * Returns a URL for linking to an RSS feed of this blog
   * @return A URL
   */
    public String getRssURL() {
        ResourceURL ru = new ResourceURL(myPortlet);
        if (myPR.getParameter("Portlet") == null) {
            ru.setQueryStringParameter("ArticlePage", new Long(getCurrentPage().getID()).toString());
        }
        return ru.getURL();
    }

    public List getArticles() {
        return myVisibleArticles;
    }

    public int getNArticles() {
        return myVisibleArticles.size();
    }

    public boolean isFullArticleMode() {
        return myCurrentArticle != null;
    }

    private void handlePost() {
        String articleIDStr = (String) myPR.getParameter("articleID");
        String operation = (String) myPR.getParameter("operation");
        if (articleIDStr != null && operation != null) {
            long articleID = Long.parseLong(articleIDStr);
            try {
                Article article = Article.findArticle(articleID);
                if (operation.equals("DeleteArticle") && article.getPortlet().getID() == myPortlet.getID()) {
                    try {
                        myPR.getSearchEngine().delete(article.getContent());
                    } catch (IOException e) {
                        myLogger.log(Level.WARNING, "Unable to remove the content with ID " + article.getContent().getID() + " from the search index ");
                    }
                    article.delete();
                }
            } catch (PersistentModelException e) {
                myLogger.log(Level.WARNING, "Unable to delete article " + articleID, e);
            }
        }
    }

    private void loadCurrentArticle() {
        String articleID = getRequestProperty(this.ARTICLE_PARAMETER);
        if (articleID != null) {
            try {
                myCurrentArticle = Article.findArticle(Long.parseLong(articleID));
                if (myCurrentArticle.getPortlet().getID() != myPortlet.getID()) {
                    myCurrentArticle = null;
                    myLogger.info("Article ID " + articleID + " which does not belong to the portlet passed to Portlet");
                }
            } catch (PersistentModelException e) {
                myLogger.log(Level.INFO, "Bad Article ID " + articleID + " passed to Portlet", e);
            }
        }
    }

    public boolean getShowRSSFeed() {
        return this.myRSSMode;
    }

    /**
   * Serve an RSS feed to the requestor
   * @throws PresentationException
   */
    public void serveResource() throws PresentationException {
        HttpServletResponse response = PortalRequest.getCurrentRequest().getResponse();
        try {
            response.setContentType("text/xml");
            PrintWriter pw = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            HttpServletRequest request = myPR.getRequest();
            String preServletURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String pageID = getDefinitiveProperty("ArticlePage");
            String link = preServletURL + "/" + myPortlet.getID() + ".portlet";
            if (pageID != null) {
                link = preServletURL + "/" + pageID + ".page";
            }
            StringBuffer content = new StringBuffer();
            StringBuffer header = new StringBuffer();
            if (myArticles != null) {
                try {
                    header.append("<title>iPoint Blog Feed</title>");
                    header.append("<description>An RSS Feed generated by iPoint portal</description>");
                    header.append("<link>" + link + "</link>");
                    header.append("<generator>iPoint portal</generator>");
                    if (myArticles.size() > 0) {
                        Iterator<Article> it = myArticles.iterator();
                        while (it.hasNext()) {
                            Article entry = it.next();
                            Date now = new Date();
                            ContentVersion publishedVersion = entry.getSynopsisContent().getPublishedVersion();
                            if (publishedVersion.getValidFrom().before(now) && publishedVersion.getValidTo().after(now)) {
                                if (entry.getContent().isVisibleTo(myPR.getCurrentUser())) {
                                    content.append("<item>");
                                    content.append("<guid>" + link + "?&amp;" + myPortlet.getID() + ARTICLE_PARAMETER + "=" + entry.getID() + "</guid>");
                                    content.append("<title>");
                                    content.append(entry.getContent().getName());
                                    content.append("</title>");
                                    content.append("<description><![CDATA[");
                                    String description = entry.getSynopsisContent().getBody();
                                    try {
                                        ContentScanner cs = new ContentScanner(entry.getContent());
                                        cs.prependLinks(preServletURL + "/");
                                        cs.prependImages(preServletURL + "/");
                                        description = cs.getText();
                                    } catch (DocumentException e) {
                                    }
                                    content.append(description);
                                    content.append("]]></description>");
                                    content.append("<link>");
                                    content.append(link + "?&amp;" + myPortlet.getID() + ARTICLE_PARAMETER + "=" + entry.getID());
                                    content.append("</link>");
                                    if (entry.getContent().getAuthor().getDetails().getEMail() != null) {
                                        content.append("<author>" + entry.getContent().getAuthor().getDetails().getEMail() + "</author>");
                                    }
                                    content.append("<pubDate>" + DateFormatUtils.format(entry.getContent().getLastUpdateDate(), "EEE, dd MMM yyyy hh:mm:ss Z") + "</pubDate>");
                                    MediaServices ms = new MediaServices(entry.getContent().getMediaRepository());
                                    for (Media media : entry.getSynopsisContent().getMediaRepository().getMedia()) {
                                        if (!media.getMimeType().getType().startsWith("image")) {
                                            MediaURL mu = new MediaURL(media, true);
                                            File mediaFile = ms.getFile(media);
                                            content.append("<enclosure url=\"" + mu.getAbsoluteURL(true) + "\" ");
                                            content.append("length=\"" + mediaFile.length() + "\" ");
                                            content.append("type=\"" + media.getMimeType().getType() + "\" />");
                                        }
                                    }
                                    content.append("</item>");
                                }
                            }
                        }
                    }
                } catch (PersistentModelException e) {
                    throw new PresentationException("Unable to find the requested articles");
                }
            }
            writeResponse(pw, header.toString(), content.toString());
        } catch (IOException e) {
            throw new PresentationException("Unable to get the Writer from the Response");
        }
    }

    public void writeResponse(PrintWriter pw, String header, String content) {
        pw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        pw.write("<rss version=\"2.0\">");
        pw.write("<channel>");
        pw.write(header);
        pw.write(content);
        pw.write("</channel>");
        pw.write("</rss>");
    }

    private List myArticles;

    private List myVisibleArticles;

    private Article myCurrentArticle;

    private boolean myRSSMode;
}
