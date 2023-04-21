package org.riverock.portlet.news;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.log4j.Logger;
import org.riverock.portlet.schema.portlet.news_block.NewsItemSimpleType;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.tools.PortletMetadataService;

/**
 *
 * $Author: serg_main $
 *
 * $Id: NewsItemSimple.java,v 1.6 2006/02/17 16:33:08 serg_main Exp $
 *
 */
public final class NewsItemSimple implements PortletResultObject, PortletResultContent {

    private static final Logger log = Logger.getLogger(NewsItemSimple.class);

    private NewsItemSimpleType newsItem = new NewsItemSimpleType();

    private RenderRequest renderRequest = null;

    public void setParameters(RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig) {
        this.renderRequest = renderRequest;
    }

    protected void finalize() throws Throwable {
        newsItem = null;
        super.finalize();
    }

    public NewsItemSimple() {
    }

    public PortletResultContent getInstance() throws PortletException {
        Long id__ = PortletService.getLong(renderRequest, NewsSite.NAME_ID_NEWS_PARAM);
        try {
            NewsItem item = NewsItem.getInstance(id__);
            newsItem.setNewsAnons(item.newsItem.getNewsAnons());
            newsItem.setNewsDate(item.newsItem.getNewsDate());
            newsItem.setNewsHeader(item.newsItem.getNewsHeader());
            newsItem.setNewsText(item.newsItem.getNewsText());
            newsItem.setNewsTime(item.newsItem.getNewsTime());
        } catch (Exception e) {
            final String es = "Error get NewsItem object";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        return this;
    }

    public PortletResultContent getInstance(Long id) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode(String portletCode_) throws PortletException {
        return getInstance();
    }

    public byte[] getPlainHTML() throws Exception {
        StringBuilder s = new StringBuilder().append("\n<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").append("<tr><td colspan=\"2\" class=\"newshead\">").append(newsItem.getNewsHeader()).append("</td></tr>\n").append("<tr><td class=\"newsdate\" valign=\"top\">").append(newsItem.getNewsDate()).append("</td><td>&nbsp;</td></tr>\n").append("<tr>\n").append("<td class=\"newstime\" valign=\"top\">\n").append(newsItem.getNewsTime()).append("</td>\n").append("<td width=\"100%\" class=\"newstitle\">\n<h6> ").append(newsItem.getNewsAnons()).append("</h6>\n").append(newsItem.getNewsText()).append("\n</td></tr>\n").append("</table>\n");
        return s.toString().getBytes(ContentTypeTools.CONTENT_TYPE_UTF8);
    }

    public byte[] getXml() throws Exception {
        return getXml("NewsItemSimple");
    }

    public byte[] getXml(final String rootElement) throws Exception {
        String root = PortletMetadataService.getMetadata(renderRequest, "xml-root-name", rootElement);
        String xml = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n").append('<').append(root).append('>').append("<NewsDate>").append(newsItem.getNewsDate()).append("</NewsDate>").append("<NewsTime>").append(newsItem.getNewsTime()).append("</NewsTime>").append("<NewsHeader>").append(newsItem.getNewsHeader() != null ? newsItem.getNewsHeader() : "").append("</NewsHeader>").append("<NewsAnons>").append(newsItem.getNewsAnons() != null ? newsItem.getNewsAnons() : "").append("</NewsAnons>").append("<NewsText>").append(newsItem.getNewsText() != null ? newsItem.getNewsText() : "").append("</NewsText>").append("</").append(root).append('>').toString();
        if (log.isDebugEnabled()) {
            log.debug("NewsXml. getXml: " + xml);
        }
        return xml.getBytes(ContentTypeTools.CONTENT_TYPE_UTF8);
    }
}
