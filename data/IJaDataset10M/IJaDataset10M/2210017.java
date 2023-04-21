package vqwiki.plugin.export2html;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vqwiki.Environment;
import vqwiki.WikiBase;
import vqwiki.servlets.LongOperationThread;
import vqwiki.servlets.ServletUtils;
import vqwiki.utils.Utilities;

public class SitemapThread extends LongOperationThread {

    public static final String LAST_IN_LIST = "e";

    public static final String MORE_TO_COME = "x";

    public static final String HORIZ_LINE = "a";

    public static final String NOTHING = "s";

    /** Logging */
    private static final Logger logger = Logger.getLogger(SitemapThread.class.getName());

    private StatisticsVWikiBean vwikis;

    private int allWikiSize;

    private int allWikiCount;

    private int numPages;

    private int pageCount;

    /**
     * Handle post request.
     * Generate a RSS feed and send it back as XML.
     *
     * @param request  The current http request
     * @param response What the servlet will send back as response
     *
     * @throws ServletException If something goes wrong during servlet execution
     * @throws IOException If the output stream cannot be accessed
     *
     */
    protected void onRun() {
        logger.fine("Generating sitemap begins...");
        vwikis = new StatisticsVWikiBean();
        NumberFormat nf = NumberFormat.getInstance(locale);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(1);
        Collection allWikis;
        try {
            allWikis = WikiBase.getInstance().getPersistenceHandler().getVirtualWikis();
        } catch (Exception e) {
            allWikis = Collections.EMPTY_LIST;
        }
        if (!allWikis.contains(WikiBase.DEFAULT_VWIKI)) {
            allWikis.add(WikiBase.DEFAULT_VWIKI);
        }
        Environment en = Environment.getInstance();
        String endString = Utilities.resource("topic.ismentionedon", locale);
        allWikiCount = 0;
        allWikiSize = allWikis.size();
        numPages = 0;
        for (Iterator iterator = allWikis.iterator(); iterator.hasNext(); allWikiCount++) {
            pageCount = 0;
            setProgress();
            String currentWiki = (String) iterator.next();
            List sitemapLines = new ArrayList();
            Vector visitedPages = new Vector();
            try {
                numPages = WikiBase.getInstance().getSearchEngine().getAllTopicNames(currentWiki).size();
            } catch (Exception e1) {
                numPages = 1;
            }
            String startTopic = en.getDefaultTopic();
            List startingList = new ArrayList(1);
            startingList.add(LAST_IN_LIST);
            parsePages(currentWiki, startTopic, startingList, "1", sitemapLines, visitedPages, endString);
            SitemapBean onewiki = new SitemapBean();
            onewiki.setName(currentWiki);
            onewiki.setPages(sitemapLines);
            vwikis.getVwiki().add(onewiki);
        }
        progress = PROGRESS_DONE;
        logger.fine("Sitemap finished.");
    }

    /**
     * Set the progress
     * @param allWikiCount Current wiki, we are processing
     * @param allWikiSize  Number of wikis (overall)
     * @param pageCount Current page we are processing
     * @param pageSize Number of pages of this wiki
     */
    private void setProgress() {
        if (numPages == 0) numPages = 1;
        double one = 100.0 / (double) allWikiSize;
        progress = Math.min((int) ((double) allWikiCount * one + (double) pageCount * one / (double) numPages), 99);
    }

    /**
     * We are done. Go to result page.
     * @see vqwiki.servlets.LongLastingOperationAction#dispatchDone(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void dispatchDone(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("virtualwikis", vwikis);
        ServletUtils.dispatch("/jsp/sitemap.jsp", request, response);
    }

    /**
     * Parse the pages starting with startTopic. The results
     * are stored in the list sitemapLines. This functions is
     * called recursivly, but the list is filled in the
     * correct order.
     *
     * @param currentWiki  name of the wiki to refer to
     * @param startTopic  Start with this page
     * @param level   A list indicating the images to use to represent certain levels
     * @param group The group, we are representing
     * @param sitemapLines  A list of all lines, which results in the sitemap
     * @param visitedPages  A vector of all pages, which already have been visited
     * @param endString Beyond this text we do not search for links
     */
    private void parsePages(String currentWiki, String topic, List levelsIn, String group, List sitemapLines, Vector visitedPages, String endString) {
        try {
            WikiBase base = WikiBase.getInstance();
            String onepage = base.readCooked(currentWiki, topic);
            List result = new ArrayList();
            List levels = new ArrayList(levelsIn.size());
            for (int i = 0; i < levelsIn.size(); i++) {
                if ((i + 1) < levelsIn.size()) {
                    if (MORE_TO_COME.equals((String) levelsIn.get(i))) {
                        levels.add(HORIZ_LINE);
                    } else if (LAST_IN_LIST.equals((String) levelsIn.get(i))) {
                        levels.add(NOTHING);
                    } else {
                        levels.add(levelsIn.get(i));
                    }
                } else {
                    levels.add(levelsIn.get(i));
                }
            }
            if (onepage != null) {
                String searchfor = "href=\"Wiki?";
                int iPos = onepage.indexOf(searchfor);
                int iEndPos;
                if (endString == null || endString.trim().length() == 0) {
                    iEndPos = Integer.MAX_VALUE;
                } else {
                    iEndPos = onepage.indexOf(endString);
                    if (iEndPos == -1) iEndPos = Integer.MAX_VALUE;
                }
                while (iPos > -1 && iPos < iEndPos) {
                    String link = onepage.substring(iPos + searchfor.length(), onepage.indexOf('"', iPos + searchfor.length()));
                    if (link.indexOf('&') > -1) {
                        link = link.substring(0, link.indexOf('&'));
                    }
                    if (link.length() > 3 && !link.startsWith("topic=") && !link.startsWith("action=") && !visitedPages.contains(link) && !WikiBase.getInstance().getPseudoTopicHandler().isPseudoTopic(link)) {
                        result.add(link);
                        visitedPages.add(link);
                    }
                    iPos = onepage.indexOf(searchfor, iPos + 10);
                }
                SitemapLineBean slb = new SitemapLineBean();
                slb.setTopic(topic);
                slb.setLevels(new ArrayList(levels));
                slb.setGroup(group);
                slb.setHasChildren(result.size() > 0);
                sitemapLines.add(slb);
                pageCount++;
                setProgress();
                for (int i = 0; i < result.size(); i++) {
                    String link = (String) result.get(i);
                    String newGroup = group + "_" + String.valueOf(i);
                    boolean isLast = ((i + 1) == result.size());
                    if (isLast) {
                        levels.add(LAST_IN_LIST);
                    } else {
                        levels.add(MORE_TO_COME);
                    }
                    parsePages(currentWiki, link, levels, newGroup, sitemapLines, visitedPages, endString);
                    levels.remove(levels.size() - 1);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }
    }
}
