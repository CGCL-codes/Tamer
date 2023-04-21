package com.hack23.cia.service.agent.sweden;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.hack23.cia.model.sweden.ParliamentMember;

/**
 * The Class ParliamentMemberAgentImpl.
 */
public class ParliamentMemberAgentImpl implements ParliamentMemberAgent {

    /**
     * The Constant ANCHOR.
     */
    private static final String ANCHOR = "a";

    /**
     * The Constant PARLIAMENT_MEMBER_LIST.
     */
    private static final String PARLIAMENT_MEMBER_LIST = "http://www.riksdagen.se/webbnav/index.aspx?fnamn=&enamn=&f_ar=&kn=&party=&electoralRegion=&rdlstatus=&org=&sort=&s=1&nid=1102";

    /**
     * The Constant PARLIAMENT_MEMBER_SWEDISH_WIKI_LIST.
     */
    private static final String PARLIAMENT_MEMBER_SWEDISH_WIKI_LIST = "http://sv.wikipedia.org/wiki/Lista_%C3%B6ver_ledam%C3%B6ter_av_Sveriges_riksdag_2006-2010";

    /**
     * The Constant PARLIAMENT_MEMBER_ENGLISH_WIKI_LIST.
     */
    private static final String PARLIAMENT_MEMBER_ENGLISH_WIKI_LIST = "http://en.wikipedia.org/wiki/List_of_members_of_the_Riksdag,_2006-2010";

    /**
     * The log.
     */
    private static Log log = LogFactory.getLog(ParliamentMemberAgentImpl.class);

    /**
     * The href map.
     */
    private Map<String, String> hrefMap = new HashMap<String, String>();

    /**
     * The wiki href map.
     */
    private Map<String, String> wikiHrefMap = new HashMap<String, String>();

    /**
     * The english wiki href map.
     */
    private Map<String, String> englishWikiHrefMap = new HashMap<String, String>();

    /**
     * The web client.
     */
    private final WebClient webClient;

    /**
     * Instantiates a new parliament member agent impl.
     * 
     * @param webClient the web client
     */
    public ParliamentMemberAgentImpl(final WebClient webClient) {
        super();
        this.webClient = webClient;
    }

    @Override
    public final String getHref(final ParliamentMember parliamentMember) {
        return hrefMap.get(parliamentMember.getName());
    }

    public final void initData() {
        try {
            HtmlPage htmlPage = (HtmlPage) webClient.getPage(PARLIAMENT_MEMBER_LIST);
            List<HtmlAnchor> anchors = htmlPage.getDocumentElement().getHtmlElementsByTagName(ANCHOR);
            for (HtmlAnchor anchor : anchors) {
                hrefMap.put(anchor.asText(), anchor.getHrefAttribute());
                log.info("homepage:" + anchor.asText() + " - " + anchor.getHrefAttribute());
            }
        } catch (Exception e) {
            log.warn("Problem Loading Parliament web site info", e);
        }
        try {
            HtmlPage htmlPage = (HtmlPage) webClient.getPage(PARLIAMENT_MEMBER_SWEDISH_WIKI_LIST);
            List<HtmlTable> tables = htmlPage.getDocumentElement().getHtmlElementsByAttribute("table", "class", "wikitable sortable");
            HtmlTable table = tables.iterator().next();
            List<HtmlTableRow> rows = table.getRows();
            for (HtmlTableRow row : rows) {
                if (row.getCells().size() > 1) {
                    HtmlTableCell cell = row.getCell(1);
                    List<HtmlAnchor> anchors = cell.getHtmlElementsByTagName(ANCHOR);
                    HtmlAnchor anchor = null;
                    if (anchors.size() > 0) {
                        if (!cell.asText().contains("ersatt av")) {
                            anchor = anchors.get(0);
                        } else {
                            anchor = anchors.get(anchors.size() - 1);
                        }
                        String name = extractName(anchor);
                        String href = "http://sv.wikipedia.org" + anchor.getHrefAttribute();
                        log.info("wiki sv: " + name + " - " + href);
                        wikiHrefMap.put(name, href);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Problem Loading Parliament Swedish wiki site info", e);
        }
        try {
            HtmlPage htmlPage = (HtmlPage) webClient.getPage(PARLIAMENT_MEMBER_ENGLISH_WIKI_LIST);
            List<HtmlTable> tables = htmlPage.getDocumentElement().getHtmlElementsByAttribute("table", "class", "wikitable");
            HtmlTable table = tables.get(1);
            List<HtmlTableRow> rows = table.getRows();
            for (HtmlTableRow row : rows) {
                if (row.getCells().size() > 2) {
                    HtmlTableCell cell = row.getCell(2);
                    List<HtmlAnchor> anchors = cell.getHtmlElementsByTagName(ANCHOR);
                    HtmlAnchor anchor = null;
                    if (anchors.size() > 0) {
                        if (!(cell.asText().contains("substituted") || cell.asText().contains("replaced"))) {
                            anchor = anchors.get(0);
                        } else {
                            anchor = anchors.get(anchors.size() - 1);
                        }
                        String name = extractName(anchor);
                        String href = "http://en.wikipedia.org" + anchor.getHrefAttribute();
                        log.info("wiki en: " + name + " - " + href);
                        englishWikiHrefMap.put(name, href);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Problem Loading Parliament English wiki site info", e);
        }
    }

    /**
     * Extract name.
     * 
     * @param anchor the anchor
     * 
     * @return the string
     */
    private String extractName(final HtmlAnchor anchor) {
        String name = anchor.asText().replace(".", "");
        String[] split = name.trim().split(" ");
        if (split.length == 2) {
            name = split[1] + ", " + split[0];
        } else {
            name = split[1] + " " + split[2] + ", " + split[0];
        }
        return name;
    }

    @Override
    public final String getEnglishWikiHref(final ParliamentMember parliamentMember) {
        return englishWikiHrefMap.get(parliamentMember.getName());
    }

    @Override
    public final String getWikiHref(final ParliamentMember parliamentMember) {
        return wikiHrefMap.get(parliamentMember.getName());
    }
}
