package org.directdemocracyportal.democracy.service.governmentloader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directdemocracyportal.democracy.model.world.Issue;
import org.directdemocracyportal.democracy.model.world.Organisation;
import org.directdemocracyportal.democracy.model.world.Person;
import org.directdemocracyportal.democracy.model.world.Resolution;
import org.directdemocracyportal.democracy.model.world.Vote;
import org.directdemocracyportal.democracy.model.world.VoteResult;
import org.directdemocracyportal.democracy.model.world.Vote.Position;
import org.directdemocracyportal.democracy.service.PortalService;
import org.directdemocracyportal.democracy.service.dao.AgentDAO;
import org.directdemocracyportal.democracy.service.dao.CountryDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * The Class SwedishGovernmentDocumentImporterImpl.
 */
@Transactional(propagation = Propagation.REQUIRED)
public class SwedishGovernmentDocumentImporterImpl implements GovernmentImporter {

    /** The log. */
    private static Log log = LogFactory.getLog(SwedishGovernmentDocumentImporterImpl.class);

    /** The web client. */
    private final WebClient webClient;

    /** The country dao. */
    private final CountryDAO countryDAO;

    /** The agent dao. */
    private final AgentDAO agentDAO;

    /** The portal service. */
    private final PortalService portalService;

    /** The url2006. */
    private String url2006 = "http://www.riksdagen.se/webbnav/?nid=3110&doktyp=&rm=2006%2f07&org=&bet=&titel=&aktivitet=%26from=&tom=&persida=20&uttag=ut_bb_tr%c3%a4fflista&sid=1#t";

    /** The url2007. */
    private String url2007 = "http://www.riksdagen.se/webbnav/?nid=3110&doktyp=&rm=2007%2f08&org=&bet=&titel=&aktivitet=%26from=&tom=&persida=20&uttag=ut_bb_tr%c3%a4fflista&sid=1#t";

    /** The STATEMENT. */
    private final String STATEMENT = "doktyp=betankande";

    /** The GOVERNMEN t_ bill. */
    private final String GOVERNMENT_BILL = "doktyp=proposition";

    /** The PRIVAT e_ membe r_ bill. */
    private final String PRIVATE_MEMBER_BILL = "doktyp=motion";

    /**
     * Instantiates a new swedish government document importer impl.
     *
     * @param webClient the web client
     * @param countryDAO the country dao
     * @param agentDAO the agent dao
     * @param portalService the portal service
     * @throws MalformedURLException the malformed url exception
     */
    public SwedishGovernmentDocumentImporterImpl(WebClient webClient, CountryDAO countryDAO, AgentDAO agentDAO, PortalService portalService) throws MalformedURLException {
        this.webClient = webClient;
        this.countryDAO = countryDAO;
        this.agentDAO = agentDAO;
        this.portalService = portalService;
    }

    @SuppressWarnings("unchecked")
    public void doImport() {
    }

    /**
     * Import vote result.
     *
     * @param issue the issue
     * @param resolution the resolution
     */
    private void importVoteResult(Issue issue, Resolution resolution) {
        try {
            Organisation riksdag = (Organisation) agentDAO.findByName(SwedishGovernmentImporterImpl.SVERIGES_RIKSDAG);
            System.out.println(issue.getVoteResult().getHref());
            HtmlPage page = (HtmlPage) webClient.getPage(issue.getVoteResult().getHref());
            HtmlTable table = (HtmlTable) page.getDocumentElement().getHtmlElementsByTagName("table").iterator().next();
            List<HtmlTableRow> rows = table.getRows();
            VoteResult voteResult = issue.getVoteResult();
            try {
                for (int i = 1; i < rows.size(); i++) {
                    HtmlTableRow row = rows.get(i);
                    String[] names = row.getCell(0).asText().split(",");
                    String fName = names[0].trim();
                    String lName = names[1].trim();
                    String party = row.getCell(1).asText();
                    String electoralArea = row.getCell(2).asText();
                    String voteStr = row.getCell(3).asText().trim();
                    Person member = riksdag.findMemberByFullNameAndParty(fName, lName, party);
                    if (member == null) {
                        System.out.println("Missing " + names[0] + " ," + names[1]);
                    } else {
                        Vote vote = new Vote();
                        vote.setName("Vote " + issue.getName() + " :" + member.getName());
                        vote.setOwner(member);
                        vote.setVoteDate(resolution.getDecidedDate());
                        if (voteStr.equalsIgnoreCase("Ja")) {
                            vote.setPosition(Position.Yes);
                        } else if (voteStr.equalsIgnoreCase("Nej")) {
                            vote.setPosition(Position.No);
                        } else if (voteStr.equalsIgnoreCase("Frånvarande")) {
                            vote.setPosition(Position.Absent);
                        } else if (voteStr.equalsIgnoreCase("Avstående")) {
                            vote.setPosition(Position.Neutral);
                        }
                        if (!voteResult.containsVote(vote.getName())) {
                            voteResult.getVotes().add(vote);
                            vote.setVoteResult(voteResult);
                        }
                    }
                }
                portalService.updateVoteResult(voteResult);
            } catch (IndexOutOfBoundsException ie) {
                System.out.println("Vote result missing: " + issue.getVoteResult().getHref());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Import issues and votes.
     */
    private void ImportIssuesAndVotes() {
        List<Resolution> decidedResolutions = portalService.getDecidedResolutions();
        for (Resolution resolution : decidedResolutions) {
            System.out.println(resolution.getName() + " " + resolution.getDecidedDate());
            try {
                HtmlPage page = (HtmlPage) webClient.getPage(resolution.getHref());
                List<HtmlAnchor> anchors = page.getDocumentElement().getHtmlElementsByTagName("a");
                HtmlAnchor findVoteAnchor = findVoteAnchor(anchors);
                if (findVoteAnchor != null) {
                    findVoteResultAnchors(findVoteAnchor, resolution);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Import all resulotions.
     */
    private void ImportAllResulotions() {
        try {
            Organisation riksdag = (Organisation) agentDAO.findByName(SwedishGovernmentImporterImpl.SVERIGES_RIKSDAG);
            DocumentAnswerPage answerPage = new DocumentAnswerPage((HtmlPage) webClient.getPage(url2006));
            while (answerPage != null) {
                for (HtmlTableRow row : answerPage.getRows()) {
                    List<HtmlTableCell> cells = row.getCells();
                    if (cells.size() != 1) {
                        Iterator iterator = row.getHtmlElementsByTagName("a").iterator();
                        if (iterator.hasNext()) {
                            HtmlAnchor anchor = (HtmlAnchor) iterator.next();
                            if (!anchor.asText().startsWith("2006")) {
                                if (anchor.getHrefAttribute().contains(STATEMENT)) {
                                    collectVotes(anchor, riksdag);
                                } else if (anchor.getHrefAttribute().contains(GOVERNMENT_BILL)) {
                                    answerPage = null;
                                } else if (anchor.getHrefAttribute().contains(PRIVATE_MEMBER_BILL)) {
                                    answerPage = null;
                                }
                            }
                        }
                    }
                }
                if (answerPage != null) {
                    answerPage = answerPage.getNextPage();
                }
            }
            answerPage = new DocumentAnswerPage((HtmlPage) webClient.getPage(url2007));
            while (answerPage != null) {
                for (HtmlTableRow row : answerPage.getRows()) {
                    List<HtmlTableCell> cells = row.getCells();
                    if (cells.size() != 1) {
                        Iterator iterator = row.getHtmlElementsByTagName("a").iterator();
                        if (iterator.hasNext()) {
                            HtmlAnchor anchor = (HtmlAnchor) iterator.next();
                            if (!anchor.asText().startsWith("2007")) {
                                if (anchor.getHrefAttribute().contains(STATEMENT)) {
                                    collectVotes(anchor, riksdag);
                                } else if (anchor.getHrefAttribute().contains(GOVERNMENT_BILL)) {
                                    answerPage = null;
                                } else if (anchor.getHrefAttribute().contains(PRIVATE_MEMBER_BILL)) {
                                    answerPage = null;
                                }
                            }
                        }
                    }
                }
                if (answerPage != null) {
                    answerPage = answerPage.getNextPage();
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Collect votes.
     *
     * @param anchor the anchor
     * @param riksdag the riksdag
     */
    private void collectVotes(HtmlAnchor anchor, Organisation riksdag) {
        HtmlPage page;
        try {
            page = (HtmlPage) anchor.click();
            String orgCode = checkForResolutionGetOrgCode(anchor.getHrefAttribute());
            if (orgCode != null) {
                Organisation organisation = riksdag.findOrgByAbbr(orgCode);
                if (organisation != null && (organisation.findResourceByName(anchor.asText()) == null)) {
                    System.out.println(anchor.asText() + " - " + anchor.getHrefAttribute());
                    Resolution resolution = new Resolution();
                    resolution.setName(anchor.asText());
                    resolution.setHref(anchor.getHrefAttribute());
                    resolution.setOwner(organisation);
                    portalService.createResolution(resolution);
                    checkForDecidedDate(page, resolution);
                } else if (organisation != null && (organisation.findResourceByName(anchor.asText()) != null)) {
                    Resolution resolution = (Resolution) organisation.findResourceByName(anchor.asText());
                    checkForDecidedDate(page, resolution);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check for decided date.
     *
     * @param page the page
     * @param resolution the resolution
     */
    private void checkForDecidedDate(HtmlPage page, Resolution resolution) {
        HtmlElement contentDiv = page.getDocumentElement().getElementsByAttribute("div", "class", "centerPadding").iterator().next();
        List<HtmlElement> contentBlocks = contentDiv.getElementsByAttribute("span", "class", "normal");
        for (HtmlElement element : contentBlocks) {
            String str = element.asText().trim();
            if (str.startsWith("Riksdagens beslut")) {
                int startIndex = str.indexOf("Beslut:");
                String dateStr = str.substring(startIndex + 8, startIndex + 18).replace("/", "-");
                portalService.setResolutionDecidedDate(resolution, parseDate(dateStr));
            }
        }
    }

    /** The format. */
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Parses the date.
     *
     * @param dateStr the date str
     * @return the date
     */
    public Date parseDate(String dateStr) {
        try {
            return format.parse(dateStr);
        } catch (Exception pe) {
        }
        return null;
    }

    /**
     * Check for resolution get org code.
     *
     * @param hrefAttribute the href attribute
     * @return the string
     */
    private String checkForResolutionGetOrgCode(String hrefAttribute) {
        int lastIndexOf = hrefAttribute.lastIndexOf("&bet=");
        if (lastIndexOf >= 0) {
            String str = hrefAttribute.substring(lastIndexOf + 13, hrefAttribute.length());
            return stripDigits(str);
        }
        return null;
    }

    /**
     * Strip digits.
     *
     * @param s the s
     * @return the string
     */
    public String stripDigits(String s) {
        String bad = "0123456789";
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (bad.indexOf(s.charAt(i)) < 0) result += s.charAt(i);
        }
        return result;
    }

    /**
     * Find vote anchor.
     *
     * @param anchors the anchors
     * @return the html anchor
     */
    private HtmlAnchor findVoteAnchor(List<HtmlAnchor> anchors) {
        for (HtmlAnchor anchor : anchors) {
            if ("Utskottets förslag och kammarens omröstning".equals(anchor.asText())) {
                return anchor;
            }
        }
        return null;
    }

    /**
     * Find vote result anchors.
     *
     * @param votePage the vote page
     * @param resolution the resolution
     * @return the list
     */
    private List<HtmlAnchor> findVoteResultAnchors(HtmlAnchor votePage, Resolution resolution) {
        HtmlPage page;
        try {
            page = (HtmlPage) votePage.click();
            List<HtmlAnchor> anchors = page.getDocumentElement().getHtmlElementsByTagName("a");
            List<HtmlTable> tables = page.getDocumentElement().getHtmlElementsByTagName("table");
            int index = 0;
            for (HtmlAnchor anchor : anchors) {
                if ("Visa ledamöternas röster".equals(anchor.asText())) {
                    Issue issue = new Issue();
                    issue.setHref(votePage.getHrefAttribute());
                    issue.setName(tables.get(index).getRow(0).asText());
                    VoteResult voteResult = new VoteResult();
                    voteResult.setName("Vote result: " + resolution.getName() + " ," + issue.getName());
                    voteResult.setHref("http://www.riksdagen.se" + anchor.getHrefAttribute());
                    portalService.addResolutionIssue(resolution, issue, voteResult);
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The Class DocumentReport.
     */
    class DocumentReport {

        /** The report. */
        private HtmlAnchor report;

        /** The vote result. */
        private List<HtmlAnchor> voteResult;
    }

    /**
     * The Class DocumentAnswerPage.
     */
    class DocumentAnswerPage {

        /** The next page link row. */
        private final HtmlTableRow nextPageLinkRow;

        /** The rows. */
        private final List<HtmlTableRow> rows;

        /**
         * Instantiates a new document answer page.
         *
         * @param page the page
         */
        public DocumentAnswerPage(HtmlPage page) {
            HtmlElement answerDiv = page.getHtmlElementById("svar");
            HtmlTable table = (HtmlTable) answerDiv.getHtmlElementsByTagName("table").iterator().next();
            rows = new ArrayList<HtmlTableRow>(table.getRows());
            rows.remove(0);
            nextPageLinkRow = rows.remove(0);
            rows.remove(rows.size() - 1);
        }

        /**
         * Gets the next page.
         *
         * @return the next page
         * @throws Exception the exception
         */
        public DocumentAnswerPage getNextPage() throws Exception {
            List<HtmlAnchor> anchors = nextPageLinkRow.getHtmlElementsByTagName("a");
            for (HtmlAnchor anchor : anchors) {
                if ("nästa >".equals(anchor.asText())) {
                    return new DocumentAnswerPage((HtmlPage) anchor.click());
                }
            }
            return null;
        }

        /**
         * Gets the rows.
         *
         * @return the rows
         */
        public List<HtmlTableRow> getRows() {
            return rows;
        }
    }
}
