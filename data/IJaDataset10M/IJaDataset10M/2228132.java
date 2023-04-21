package com.hack23.cia.service.impl.admin.agent.sweden.impl.agents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.hack23.cia.model.impl.sweden.Committee;
import com.hack23.cia.model.impl.sweden.CommitteeReport;
import com.hack23.cia.model.impl.sweden.ParliamentYear;
import com.hack23.cia.service.impl.admin.agent.sweden.api.CommitteeReportAgent;

/**
 * The Class CommitteeReportAgentImpl.
 */
public class CommitteeReportAgentImpl extends AbstractParliamentDataAgentImpl<CommitteeReport> implements CommitteeReportAgent {

    /**
     * The Class DocumentAnswerPage.
     */
    class DocumentAnswerPage {

        /** The anchors. */
        private List<HtmlAnchor> anchors = new ArrayList<HtmlAnchor>();

        /** The committees. */
        private final List<Committee> committees;

        /** The next page link row. */
        private HtmlTableRow nextPageLinkRow = null;

        /** The parliament year. */
        private final ParliamentYear parliamentYear;

        /**
         * Instantiates a new document answer page.
         * 
         * @param parliamentYear the parliament year
         * @param committees the committees
         * @param page the page
         */
        public DocumentAnswerPage(final ParliamentYear parliamentYear, final List<Committee> committees, final HtmlPage page) {
            this.parliamentYear = parliamentYear;
            this.committees = committees;
            try {
                HtmlElement answerDiv = page.getHtmlElementById(ANSWER);
                Iterator<HtmlElement> iterator = answerDiv.getHtmlElementsByTagName(TABLE).iterator();
                if (iterator.hasNext()) {
                    HtmlTable table = (HtmlTable) iterator.next();
                    anchors = table.getHtmlElementsByTagName(ANCHOR);
                    List<HtmlTableRow> rows = new ArrayList<HtmlTableRow>(table.getRows());
                    rows.remove(0);
                    nextPageLinkRow = rows.remove(0);
                    rows.remove(rows.size() - 1);
                    return;
                } else {
                    logger.warn("Problem with page : " + page.getPage().getTitleText() + "\n\n" + page.asXml());
                }
            } catch (Exception e) {
                logger.warn("", e);
            }
        }

        /**
         * Gets the anchors.
         * 
         * @return the anchors
         */
        public List<HtmlAnchor> getAnchors() {
            return anchors;
        }

        /**
         * Gets the next page.
         * 
         * @return the next page
         * 
         * @throws Exception the exception
         */
        public DocumentAnswerPage getNextPage() throws Exception {
            if (nextPageLinkRow != null) {
                List<HtmlAnchor> anchors = nextPageLinkRow.getHtmlElementsByTagName(ANCHOR);
                for (HtmlAnchor anchor : anchors) {
                    if (NEXT.equals(anchor.asText()) || NEXT_VERSION2.equals(anchor.asText())) {
                        return new DocumentAnswerPage(parliamentYear, committees, (HtmlPage) anchor.click());
                    }
                }
            }
            return null;
        }
    }

    /** The Constant ANCHOR. */
    private static final String ANCHOR = "a";

    /** The Constant ANSWER. */
    private static final String ANSWER = "svar";

    /** The Constant CENTER_PADDING. */
    private static final String CENTER_PADDING = "centerPadding";

    /** The Constant CLASS. */
    private static final String CLASS = "class";

    /** The Constant COMMITEE_REPORTS_CONTAIN. */
    private static final String COMMITEE_REPORTS_CONTAIN = "http://www.riksdagen.se/webbnav/?nid=3120&doktyp=betankande&bet";

    /** The Constant COMMITEE_REPORTS_PERIOD_PARLIAMENT_YEAR. */
    private static final String COMMITEE_REPORTS_PERIOD_PARLIAMENT_YEAR = "http://www.riksdagen.se/webbnav/index.aspx?nid=3110&titel=&rm=${parliament.year}&bet=&doktyp=bet%C3%A4nkande&org=&s=S%C3%B6k#t%22";

    /** The Constant DECISION. */
    private static final String DECISION = "Beslut:";

    /** The Constant DIV. */
    private static final String DIV = "div";

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(CommitteeReportAgentImpl.class);

    /** The Constant NEXT. */
    private static final String NEXT = "nästa sida >";

    /** The Constant NEXT_VERSION2. */
    private static final String NEXT_VERSION2 = "nästa >";

    /** The Constant NORMAL. */
    private static final String NORMAL = "normal";

    /** The Constant PARLIAMENT_DECISION. */
    private static final String PARLIAMENT_DECISION = "Riksdagens beslut";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant SPAN. */
    private static final String SPAN = "span";

    /** The Constant TABLE. */
    private static final String TABLE = "table";

    /** The format. */
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Instantiates a new committee report agent impl.
     * 
     * @param webClient the web client
     */
    public CommitteeReportAgentImpl(final WebClient webClient) {
        super(webClient);
    }

    /**
     * Adds the committe reports from year.
     * 
     * @param parliamentYear the parliament year
     * @param resultat the resultat
     * @param committees the committees
     * @param answerPage the answer page
     * 
     * @throws Exception the exception
     */
    private void addCommitteReportsFromYear(final ParliamentYear parliamentYear, final List<CommitteeReport> resultat, final List<Committee> committees, final DocumentAnswerPage answerPage) throws Exception {
        if (answerPage != null) {
            for (HtmlAnchor anchor : answerPage.getAnchors()) {
                if (anchor.getHrefAttribute().contains(COMMITEE_REPORTS_CONTAIN)) {
                    CommitteeReport commiteeReport = new CommitteeReport();
                    commiteeReport.setName(anchor.asText());
                    String shortCode = "shortCode";
                    commiteeReport.setShortCode(shortCode);
                    commiteeReport.setHref(anchor.getHrefAttribute());
                    Committee committee = findMatchingCommittee(committees, commiteeReport);
                    commiteeReport.setCommittee(committee);
                    commiteeReport.setParliamentYear(parliamentYear);
                    resultat.add(commiteeReport);
                }
            }
            addCommitteReportsFromYear(parliamentYear, resultat, committees, answerPage.getNextPage());
        }
    }

    /**
     * Find matching committee.
     * 
     * @param resultat the resultat
     * @param commiteeReport the commitee report
     * 
     * @return the committee
     */
    private Committee findMatchingCommittee(final List<Committee> resultat, final CommitteeReport commiteeReport) {
        for (Committee committee : resultat) {
            if (commiteeReport.getName().toLowerCase().contains(committee.getShortCode().toLowerCase())) {
                return committee;
            }
        }
        return null;
    }

    public final Date getDecidedDateIfAny(final CommitteeReport commiteeReport) {
        HtmlPage page;
        try {
            page = (HtmlPage) webClient.getPage(commiteeReport.getHref());
            HtmlElement contentDiv = page.getDocumentElement().getElementsByAttribute(DIV, CLASS, CENTER_PADDING).iterator().next();
            List<HtmlElement> contentBlocks = contentDiv.getElementsByAttribute(SPAN, CLASS, NORMAL);
            logger.info("Checking if decision has been made  " + commiteeReport.getHref());
            for (HtmlElement element : contentBlocks) {
                String str = element.asText().trim();
                if (str.startsWith(PARLIAMENT_DECISION)) {
                    int startIndex = str.indexOf(DECISION);
                    if (startIndex >= 0) {
                        String dateStr = str.substring(startIndex + 8, startIndex + 18).replace("/", "-");
                        return parseDate(dateStr);
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Problem deciding decidedDate for " + commiteeReport.getName(), e);
        }
        return null;
    }

    @Override
    public final List<CommitteeReport> getList(final ParliamentYear parliamentYear, final List<Committee> committees) {
        List<CommitteeReport> resultat = new ArrayList<CommitteeReport>();
        String reportsHref = COMMITEE_REPORTS_PERIOD_PARLIAMENT_YEAR.replace("${parliament.year}", parliamentYear.getShortCode());
        try {
            addCommitteReportsFromYear(parliamentYear, resultat, committees, new DocumentAnswerPage(parliamentYear, committees, (HtmlPage) webClient.getPage(reportsHref)));
        } catch (Exception e) {
            logger.error("Problem importing committee reports", e);
        }
        logger.info("CommiteeReports found : " + resultat.size());
        return resultat;
    }

    /**
	 * Parses the date.
	 * 
	 * @param dateStr the date str
	 * 
	 * @return the date
	 */
    private Date parseDate(final String dateStr) {
        Date result = null;
        try {
            result = format.parse(dateStr);
        } catch (Exception pe) {
            logger.warn("Problem parsing date ;" + dateStr, pe);
        }
        return result;
    }
}
