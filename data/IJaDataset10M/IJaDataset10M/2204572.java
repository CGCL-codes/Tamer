package com.ecyrd.jspwiki.rpc.atom;

import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.intabulas.sandler.*;
import org.intabulas.sandler.exceptions.*;
import org.intabulas.sandler.elements.*;
import com.ecyrd.jspwiki.*;
import com.ecyrd.jspwiki.util.*;
import com.ecyrd.jspwiki.plugin.WeblogEntryPlugin;
import com.ecyrd.jspwiki.plugin.WeblogPlugin;
import com.ecyrd.jspwiki.providers.ProviderException;

public class AtomAPIServlet extends HttpServlet {

    static final Logger log = Logger.getLogger(AtomAPIServlet.class.getName());

    private static final long serialVersionUID = 0L;

    private WikiEngine m_engine;

    /**
     *  {@inheritDoc}
     */
    public void init(ServletConfig config) throws ServletException {
        m_engine = WikiEngine.getInstance(config);
    }

    /**
     *  Takes the name of the page from the request URI.
     *  The initial slash is also removed.  If there is no page,
     *  returns null.
     */
    private String getPageName(HttpServletRequest request) {
        String name = request.getPathInfo();
        if (name == null || name.length() <= 1) {
            return null;
        } else if (name.charAt(0) == '/') {
            name = name.substring(1);
        }
        name = TextUtil.urlDecodeUTF8(name);
        return name;
    }

    /**
     *  Implements the PostURI of the Atom spec.
     *  <p>
     *  Implementation notes:
     *  <ul>
     *   <li>Only fetches the first content.  All other contents are ignored.
     *   <li>Assumes that incoming code is plain text or WikiMarkup, not html.
     *  </ul>
     *  
     *  @param request {@inheritDoc}
     *  @param response {@inheritDoc}
     *  @throws ServletException {@inheritDoc}
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.log(Level.INFO, "Received POST to AtomAPIServlet");
        try {
            String blogid = getPageName(request);
            WikiPage page = m_engine.getPage(blogid);
            if (page == null) {
                throw new ServletException("Page " + blogid + " does not exist, cannot add blog post.");
            }
            Entry entry = Sandler.unmarshallEntry(request.getInputStream());
            Content title = entry.getTitle();
            Content content = entry.getContent(0);
            Person author = entry.getAuthor();
            WeblogEntryPlugin plugin = new WeblogEntryPlugin();
            String pageName = plugin.getNewEntryPage(m_engine, blogid);
            String username = author.getName();
            WikiPage entryPage = new WikiPage(m_engine, pageName);
            entryPage.setAuthor(username);
            WikiContext context = new WikiContext(m_engine, request, entryPage);
            StringBuffer text = new StringBuffer();
            text.append("!" + title.getBody());
            text.append("\n\n");
            text.append(content.getBody());
            log.log(Level.INFO, "Writing entry: " + text);
            m_engine.saveText(context, text.toString());
        } catch (FeedMarshallException e) {
            log.log(Level.SEVERE, "Received faulty Atom entry", e);
            throw new ServletException("Faulty Atom entry", e);
        } catch (IOException e) {
            log.log(Level.SEVERE, "I/O exception", e);
            throw new ServletException("Could not get body of request", e);
        } catch (WikiException e) {
            log.log(Level.SEVERE, "Provider exception while posting", e);
            throw new ServletException("JSPWiki cannot save the entry", e);
        }
    }

    /**
     *  Handles HTTP GET.  However, we do not respond to GET requests,
     *  other than to show an explanatory text.
     *  
     *  {@inheritDoc}
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.log(Level.INFO, "Received HTTP GET to AtomAPIServlet");
        String blogid = getPageName(request);
        log.log(Level.INFO, "Requested page " + blogid);
        try {
            if (blogid == null) {
                Feed feed = listBlogs();
                response.setContentType("application/x.atom+xml; charset=UTF-8");
                response.getWriter().println(Sandler.marshallFeed(feed));
                response.getWriter().flush();
            } else {
                Entry entry = getBlogEntry(blogid);
                response.setContentType("application/x.atom+xml; charset=UTF-8");
                response.getWriter().println(Sandler.marshallEntry(entry));
                response.getWriter().flush();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Unable to generate response", e);
            throw new ServletException("Internal problem - whack Janne on the head to get a better error report", e);
        }
    }

    private Entry getBlogEntry(String entryid) throws ProviderException {
        WikiPage page = m_engine.getPage(entryid);
        WikiPage firstVersion = m_engine.getPage(entryid, 1);
        Entry entry = SyndicationFactory.newSyndicationEntry();
        String pageText = m_engine.getText(page.getName());
        String title = "";
        int firstLine = pageText.indexOf('\n');
        if (firstLine > 0) {
            title = pageText.substring(0, firstLine);
        }
        if (title.trim().length() == 0) title = page.getName();
        while (title.startsWith("!")) title = title.substring(1);
        entry.setTitle(title);
        entry.setCreated(firstVersion.getLastModified());
        entry.setModified(page.getLastModified());
        entry.setAuthor(SyndicationFactory.createPerson(page.getAuthor(), null, null));
        entry.addContent(SyndicationFactory.createEscapedContent(pageText));
        return entry;
    }

    /**
     *  Creates and outputs a full list of all available blogs
     */
    private Feed listBlogs() throws ProviderException, IOException {
        Collection pages = m_engine.getPageManager().getAllPages();
        Feed feed = SyndicationFactory.newSyndicationFeed();
        feed.setTitle("List of blogs at this site");
        feed.setModified(new Date());
        for (Iterator i = pages.iterator(); i.hasNext(); ) {
            WikiPage p = (WikiPage) i.next();
            log.log(Level.INFO, p.getName() + " = " + p.getAttribute(WeblogPlugin.ATTR_ISWEBLOG));
            if (!("true".equals(p.getAttribute(WeblogPlugin.ATTR_ISWEBLOG)))) continue;
            String encodedName = TextUtil.urlEncodeUTF8(p.getName());
            WikiContext context = new WikiContext(m_engine, p);
            String title = TextUtil.replaceEntities(BlogUtil.getSiteName(context));
            Link postlink = createLink("service.post", m_engine.getBaseURL() + "atom/" + encodedName, title);
            Link editlink = createLink("service.edit", m_engine.getBaseURL() + "atom/" + encodedName, title);
            Link feedlink = createLink("service.feed", m_engine.getBaseURL() + "atom.jsp?page=" + encodedName, title);
            feed.addLink(postlink);
            feed.addLink(feedlink);
            feed.addLink(editlink);
        }
        return feed;
    }

    private Link createLink(String rel, String href, String title) {
        org.intabulas.sandler.elements.impl.LinkImpl link = new org.intabulas.sandler.elements.impl.LinkImpl();
        link.setRelationship(rel);
        link.setTitle(title);
        link.setType("application/x.atom+xml");
        link.setHref(href);
        return link;
    }

    /**
     *  {@inheritDoc}
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.log(Level.INFO, "Received HTTP DELETE");
    }

    /**
     *  {@inheritDoc}
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.log(Level.INFO, "Received HTTP PUT");
    }
}
