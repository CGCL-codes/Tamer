package uk.ac.ebi.intact.application.search3.struts.controller;

import uk.ac.ebi.intact.application.search3.business.interpro.InterproConstants;
import uk.ac.ebi.intact.application.search3.business.interpro.InterproSearch;
import uk.ac.ebi.intact.application.search3.business.interpro.ThresholdExceededException;
import uk.ac.ebi.intact.application.search3.struts.util.SearchConstants;
import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.model.AnnotatedObject;
import uk.ac.ebi.intact.model.Experiment;
import uk.ac.ebi.intact.model.Interaction;
import uk.ac.ebi.intact.model.Protein;
import uk.ac.ebi.intact.persistence.DAOFactory;
import uk.ac.ebi.intact.persistence.dao.DaoFactory;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * This Action class performs the determination of UniprotKB IDs out of IntAct ACs. <br> These UniprotKB IDs are then
 * used to build up dynamically a link to the corresponding entries in the InterPro Database, like
 * <br><b>http://www.ebi.ac.uk/interpro/ISpy?ac=Q9UKX3%2CQ9UK45%2CP62312</b><br> where <i>Q9UKX3</i>, <i>Q9UK45</i> and
 * <i>P62312</i> specify the UniprotKB IDs related to every IntAct-Protein.
 * <p>
 * Dependent on the user's selection, one has to distinguish from 3 cases:<br> <ol> <li> all of the selected Proteins do
 * have an UniProtKB ID, so it will be forwarded to the <i>Waiting Page</i> <li> no Protein at all does have an
 * UniProtKB ID, so it will be forwarded to the general <i>Error Page</i> <li> at least one Protein do have an UniProtKB
 * ID and at least one Protein has none, so it will be forwarded to the <i>Waiting Page</i> as well, but including one
 * ore more warning hints. </ol>
 * <p>
 * Furthermore, the htmlView corresponding to the Warning-page will be build within this <code>Action Class</code>.
 *
 * @author Christian Kohler (ckohler@ebi.ac.uk)
 * @version $Id: InterProSearchAction.java 5015 2006-06-09 15:37:52Z baranda $
 */
public class InterProSearchAction extends AbstractResultAction {

    protected String processResults(HttpServletRequest request, String helpLink) {
        String intactACs = request.getParameter("ac");
        StringTokenizer intactAcTokenizer = new StringTokenizer(intactACs, ",");
        int intactACsLength = intactAcTokenizer.countTokens();
        HashSet proteinACset;
        proteinACset = parseInputParameter(intactACs);
        Set mappedProteins = new HashSet(InterproSearch.MAXIMUM_NUMBER_OF_SELECTED_PROTEINS);
        Set unmappedProteins = new HashSet();
        Map proteinsWithUniprotKB = new HashMap();
        boolean thresholdExceeded = false;
        Iterator it = proteinACset.iterator();
        try {
            while (it.hasNext()) {
                String proteinAC = (String) it.next();
                try {
                    AnnotatedObject ao = searchAO(proteinAC);
                    if (ao instanceof Protein) {
                        InterproSearch.findUniprotId((Protein) ao, mappedProteins, unmappedProteins, proteinsWithUniprotKB);
                    } else if (ao instanceof Interaction) {
                        Collection proteins = InterproSearch.getProteins((Interaction) ao);
                        for (Iterator iterator = proteins.iterator(); iterator.hasNext(); ) {
                            Protein protein = (Protein) iterator.next();
                            InterproSearch.findUniprotId(protein, mappedProteins, unmappedProteins, proteinsWithUniprotKB);
                        }
                    } else if (ao instanceof Experiment) {
                        Collection interactions = InterproSearch.getProteins((Experiment) ao);
                        for (Iterator iterator = interactions.iterator(); iterator.hasNext(); ) {
                            Interaction interaction = (Interaction) iterator.next();
                            Collection proteins = InterproSearch.getProteins(interaction);
                            for (Iterator iterator1 = proteins.iterator(); iterator1.hasNext(); ) {
                                Protein protein = (Protein) iterator1.next();
                                InterproSearch.findUniprotId(protein, mappedProteins, unmappedProteins, proteinsWithUniprotKB);
                            }
                        }
                    }
                } catch (IntactException e) {
                    logger.error("Error while resolving \"InterproSearch.findUniprotId (" + proteinAC + ")\"", e);
                }
            }
        } catch (ThresholdExceededException tee) {
            logger.info("Threshold exceeded.");
            thresholdExceeded = true;
        }
        if (mappedProteins.size() == 0 && unmappedProteins.size() != 0) {
            super.clearErrors();
            Iterator iterator = unmappedProteins.iterator();
            StringBuffer buffer = new StringBuffer(512);
            Protein protein;
            while (iterator.hasNext()) {
                protein = (Protein) iterator.next();
                buffer.append(protein.getShortLabel()).append(" (");
                buffer.append(protein.getAc()).append(") ");
                if (iterator.hasNext()) {
                    buffer.append("<br>");
                }
            }
            String finalUnmappedProteins = buffer.toString();
            super.addError("error.search.no.uniprot.id", "<br><br><b><font color=\"red\">" + finalUnmappedProteins + "</font><br><br><p>" + " <font size =\"3\">" + "Please search again, using the IntAct Searchfield on the left</font></p>");
            super.saveErrors(request);
            return SearchConstants.FORWARD_FAILURE;
        }
        Iterator iterator = mappedProteins.iterator();
        StringBuffer mappedProteinsBuffer = new StringBuffer(128);
        while (iterator.hasNext()) {
            String id = (String) iterator.next();
            mappedProteinsBuffer.append(id);
            if (iterator.hasNext()) {
                mappedProteinsBuffer.append(',');
            }
        }
        String uniprotIds = mappedProteinsBuffer.toString();
        if (mappedProteins.size() != 0) {
            HashSet uniprotIdSet;
            uniprotIdSet = parseInputParameter(uniprotIds);
            String interproURL = createInterproURL(uniprotIdSet);
            StringBuffer htmlBuffer = new StringBuffer(512);
            buildHtmlMessage(htmlBuffer, unmappedProteins, thresholdExceeded, intactACsLength, proteinsWithUniprotKB, interproURL, request);
            String htmlMsg = htmlBuffer.toString();
            logger.info("Buffer Size (original size:512): " + htmlBuffer.length());
            request.setAttribute(SearchConstants.WAITING_MSG, htmlMsg);
            request.setAttribute(SearchConstants.WAITING_URL, interproURL);
        }
        return SearchConstants.FORWARD_WAITING_PAGE;
    }

    /**
     * Builds final URL to the InterPro website. URL contains no doubled entries.
     *
     * @param uniprotIdSet the <code>Set</code> containing only unique UniProtIDs.
     *
     * @return the <code>URL</code> to InterPro.
     */
    private String createInterproURL(HashSet uniprotIdSet) {
        Iterator i = uniprotIdSet.iterator();
        StringBuffer interproUrlBuffer = new StringBuffer(512);
        StringBuffer uniprotIdBuffer = new StringBuffer(512);
        interproUrlBuffer.append(InterproConstants.INTERPRO_URL);
        while (i.hasNext()) {
            String id = (String) i.next();
            interproUrlBuffer.append(id);
            uniprotIdBuffer.append(id);
            if (i.hasNext()) {
                interproUrlBuffer.append(InterproConstants.PROTEIN_ID_SEPARATOR);
                uniprotIdBuffer.append(", ");
            }
        }
        return interproUrlBuffer.toString();
    }

    /**
     * Builds the Html-View for the waiting page.
     *
     * @param htmlBuffer        used to append all Html-statements that will finally create the Summary-Message.
     * @param unmappedProteins  contains all Proteins having no UniprotKB ID.
     * @param thresholdExceeded indicates if the total number of intact ACs has been exceeded (default value: 20).
     * @param intactACsLength   total number of intact ACs.
     * @param proteinMap        contains: key = shortlabel of Protein , value = corresponding uniprotkb ID.
     * @param interproURL       the <code>URL</code> to InterPro.
     * @param request           the <code>HttpServletRequest</code>.
     */
    private void buildHtmlMessage(StringBuffer htmlBuffer, Set unmappedProteins, boolean thresholdExceeded, int intactACsLength, Map proteinMap, String interproURL, HttpServletRequest request) {
        htmlBuffer.append("<table border=\"0\">");
        createUniprotFoundMessage(htmlBuffer, intactACsLength, proteinMap, request);
        if (unmappedProteins.size() != 0 || thresholdExceeded) {
            createProblemsOccuredMessage(htmlBuffer);
            String unmappedProteinsString = unmappedProteinsIterator(unmappedProteins);
            if (unmappedProteins.size() != 0) {
                createUniprotNotFoundMessage(htmlBuffer, unmappedProteinsString, request);
            }
            if (thresholdExceeded) {
                createThresholdExceededMessage(htmlBuffer, request);
            }
            createButton(InterproConstants.CONTINUE_BUTTON_LABEL, htmlBuffer, interproURL);
            createCloseWindowButton(InterproConstants.NEW_SEARCH_BUTTON_LABEL, htmlBuffer);
        }
        htmlBuffer.append("</table>");
    }

    /**
     * Iterates through a Set of unmapped Proteins (meaning: no corresponding UniprotKB ID was found) and appends the
     * Protein-shortlabel and Protein-AC to a StringBuffer afterwards.
     *
     * @param unmappedProteins contains those Proteins, where no UniprotKB ID exists.
     *
     * @return proteinsWithoutUniprotidBuffer a <code>String</code> containing the shortlabel and proteinAC. of those
     *         Proteins where no uniprotKB ID is available (e.g.: myome-afcs_mouse (EBI-659248), fnlb-afcs_mouse
     *         (EBI-688023)...)
     */
    private String unmappedProteinsIterator(Set unmappedProteins) {
        Iterator unmappedProteinsIterator = unmappedProteins.iterator();
        StringBuffer proteinsWithoutUniprotidBuffer = new StringBuffer(512);
        int count = 1;
        boolean breakLine;
        while (unmappedProteinsIterator.hasNext()) {
            breakLine = (count % 4) == 0;
            Protein protein = (Protein) unmappedProteinsIterator.next();
            proteinsWithoutUniprotidBuffer.append(protein.getShortLabel()).append(' ');
            proteinsWithoutUniprotidBuffer.append('(').append(protein.getAc()).append(')');
            if (unmappedProteinsIterator.hasNext()) {
                proteinsWithoutUniprotidBuffer.append(", ");
                if (breakLine) {
                    proteinsWithoutUniprotidBuffer.append("<br>");
                }
            }
            count++;
        }
        return proteinsWithoutUniprotidBuffer.toString();
    }

    /**
     * Builds the html-View for that part of the message where an UniprotKB ID could be found for every Intact-AC and no
     * error/warning occured.
     *
     * @param htmlBuffer      used to append all Html-statements that will finally create the Summary-Message.
     * @param intactACsLength total number of intact ACs.
     * @param proteinMap      contains: <code>key</code> = shortlabel of Protein , <code>value</code> = uniprotkb ID.
     * @param request         the <code>HttpServletRequest</code>.
     */
    private void createUniprotFoundMessage(StringBuffer htmlBuffer, int intactACsLength, Map proteinMap, HttpServletRequest request) {
        String shortTime;
        htmlBuffer.append("<font color=\"#999999\">");
        htmlBuffer.append("<u>Step 1:</u>").append("      Converted ").append(intactACsLength).append(" IntAct AC(s) into UniProtKB ID (s):").append("<br>");
        htmlBuffer.append("</font>");
        htmlBuffer.append("<br>");
        htmlBuffer.append("<font size =\"3\">");
        htmlBuffer.append("<u>Step 2:</u>").append("       Searching InterPro ID by UniProtKB ID: ").append("</font>").append("<br>").append("<br>");
        htmlBuffer.append("<img src= ../images/check.gif width=\"16\" height=\"16\">").append("      UniProtKB ID(s) found:   (  <i>IntAct AC</i>   ==> <b>UniProtKB ID</b> )");
        htmlBuffer.append("<br>");
        int count = 1;
        boolean breakLine;
        htmlBuffer.append("<p align =\"left\">");
        Iterator it = proteinMap.entrySet().iterator();
        while (it.hasNext()) {
            breakLine = (count % 4) == 0;
            Map.Entry entry = (Map.Entry) it.next();
            htmlBuffer.append("<big><i>").append(entry.getKey()).append("</i></big>").append(" ==> ").append("<b><big>").append(entry.getValue()).append("</big></b>");
            if (it.hasNext()) {
                htmlBuffer.append(", ");
                if (breakLine) {
                    htmlBuffer.append("<br>");
                }
            }
            count++;
        }
        htmlBuffer.append("<big>").append(" . . . ").append("</big>");
        htmlBuffer.append("</p>");
        htmlBuffer.append("<tr><td>").append("</tr></td>");
        request.setAttribute(SearchConstants.WAITING_TIME, SearchConstants.DEFAULT_WAITING_TIME);
    }

    /**
     * Builds the html-View for that part of the message where no UniProtKB ID was found.
     *
     * @param htmlBuffer             used to append all Html-statements that will finally create the Summary-Message.
     * @param unmappedProteinsString contains those Proteins, where no UniprotKB ID is existing.
     * @param request                the <code>HttpServletRequest</code>.
     */
    private void createUniprotNotFoundMessage(StringBuffer htmlBuffer, String unmappedProteinsString, HttpServletRequest request) {
        String longTime;
        htmlBuffer.append("<img src= ../images/warning.gif width=\"18\" height=\"17\">");
        htmlBuffer.append("<font size =\"3\">    No UniProtKB ID(s) found for: </font>");
        htmlBuffer.append("<br>").append("<b><big>").append(unmappedProteinsString).append("</big></b>");
        htmlBuffer.append("<br><br>");
        htmlBuffer.append("<font size =\"3\">");
        htmlBuffer.append("<tr><td>").append("</tr></td>");
        htmlBuffer.append("<br>");
        request.setAttribute(SearchConstants.DO_NOT_FORWARD, Boolean.TRUE);
    }

    /**
     * Builds the html-View Header for that part of the message where problems during the processing of the user request
     * occured.
     *
     * @param htmlBuffer used to append all Html-statements that will finally create the Summary-Message.
     */
    private void createProblemsOccuredMessage(StringBuffer htmlBuffer) {
        htmlBuffer.append("<br>");
        htmlBuffer.append("<h5>").append("<font color=\"red\">");
        htmlBuffer.append("<big>");
        htmlBuffer.append(" Some problems occured during the processing of your request: ");
        htmlBuffer.append("</big>");
        htmlBuffer.append("</h5>").append("</font>");
    }

    /**
     * Builds the html-View for that part of the message where the Maximum Number of selectable Proteins has been
     * exceeded.
     *
     * @param htmlBuffer used to append all Html-statements that will finally create the Summary-Message.
     * @param request    the <code>HttpServletRequest</code>.
     */
    private void createThresholdExceededMessage(StringBuffer htmlBuffer, HttpServletRequest request) {
        String longTime;
        htmlBuffer.append("<img src= ../images/warning.gif width=\"18\" height=\"17\">");
        htmlBuffer.append("<font size =\"3\">");
        htmlBuffer.append(" You have requested the InterPro domain architecture of more than ");
        htmlBuffer.append(InterproSearch.MAXIMUM_NUMBER_OF_SELECTED_PROTEINS).append(" proteins.");
        htmlBuffer.append("<br>");
        htmlBuffer.append("Due to system limitations, we can only display");
        htmlBuffer.append(" the domain architecture for");
        htmlBuffer.append(" the first ");
        htmlBuffer.append(InterproSearch.MAXIMUM_NUMBER_OF_SELECTED_PROTEINS).append(" proteins only!");
        htmlBuffer.append("</font>");
        htmlBuffer.append("<tr><td>").append("</tr></td>");
        htmlBuffer.append("<br><br><br>");
        request.setAttribute(SearchConstants.DO_NOT_FORWARD, Boolean.TRUE);
    }

    /**
     * Builds a simple Html-Button.
     *
     * @param name       the name of the Button.
     * @param htmlBuffer used to append all Html-statements that will finally create the Summary-Message.
     * @param url        the <code>Url</code> to forward to.
     */
    private void createButton(String name, StringBuffer htmlBuffer, String url) {
        htmlBuffer.append("<form>");
        htmlBuffer.append("<input type=button value=\"");
        htmlBuffer.append(name);
        htmlBuffer.append("\"");
        htmlBuffer.append(" onClick=\"self.location=\'");
        htmlBuffer.append(url);
        htmlBuffer.append("\'\"");
        htmlBuffer.append(">");
        htmlBuffer.append("</form>");
    }

    /**
     * Builds a simple Html-Button.
     *
     * @param name       the name of the Button.
     * @param htmlBuffer used to append all Html-statements that will finally create the Summary-Message.
     */
    private void createCloseWindowButton(String name, StringBuffer htmlBuffer) {
        htmlBuffer.append("<form>");
        htmlBuffer.append("<input type=button value=\"");
        htmlBuffer.append(name);
        htmlBuffer.append("\"");
        htmlBuffer.append(" onClick=\"javascript:window.close();\"");
        htmlBuffer.append(">");
        htmlBuffer.append("</form>");
    }

    /**
     * Determines the specific AnnotatedObject object, found for the specific parameter value.
     *
     * @param ac the <code>String</code> of the search Value (here: "ac") of an <code>AnnotatedObject</code> object
     *           (e.g. "shortlabel", "AC", ...) to be searched for.
     *
     * @return the specific <code>AnnotatedObject</code> object, found for the specific parameter value.
     *
     * @throws IntactException if an error occurs.
     */
    public AnnotatedObject searchAO(String ac) throws IntactException {
        AnnotatedObject ao = DaoFactory.getProteinDao().getByAc(ac);
        if (ao != null) {
            return ao;
        }
        ao = DaoFactory.getInteractionDao().getByAc(ac);
        if (ao != null) {
            return ao;
        }
        ao = DaoFactory.getExperimentDao().getByAc(ac);
        if (ao != null) {
            return ao;
        }
        return null;
    }

    /**
     * Parses a <code>String</code>, which might include doubled entries, into a <code>HashSet</code>. By using a
     * HashSet, it is guaranteed that every entry in it is unique.
     *
     * @param ac the <code>String</code> to parse.
     *
     * @return proteinACset  the <code>HashSet</code> with unique entries.
     */
    private HashSet parseInputParameter(String ac) {
        StringTokenizer st = new StringTokenizer(ac, ",");
        HashSet proteinACset = new HashSet(32);
        while (st.hasMoreTokens()) {
            String id = st.nextToken();
            if (!proteinACset.contains(id)) {
                proteinACset.add(id);
            }
        }
        return proteinACset;
    }
}
