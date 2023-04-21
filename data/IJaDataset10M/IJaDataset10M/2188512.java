package octopus.presentation;

import java.util.Vector;
import org.w3c.dom.Element;
import hambo.app.util.Link;
import hambo.util.StringUtil;
import hambo.app.util.DOMUtil;
import com.lutris.appserver.server.httpPresentation.HttpPresentationException;
import octopus.OctopusApplication;
import octopus.tools.Messages.OctopusErrorMessages;
import octopus.requests.OctopusRequest;
import octopus.requests.OctopusRequestFactory;
import octopus.tools.Objects.ObjectTranslation;

/**
 * Page used to Print all Translation in the Main Language and in a Specific Language
 *
 */
public class alltags extends octoPage {

    /**
     * Default constructor. Does nothing except calling the constructor
     * of the superclass with the page id as an argument. The page id 
     * is set in the page_id variable that is inherited from PortalPage.
     */
    public alltags() {
        super("alltags");
    }

    public void processPage() throws HttpPresentationException {
        DOMUtil.setFirstNodeText(getElement(skeleton, "pagetitle"), "Tag Listing");
        initialisationOctopus("alltags.po");
        String idx = (String) comms.request.getParameter("idx");
        if (idx == null) idx = "0";
        Element elementIdx = getElement("idx");
        DOMUtil.setFirstMatchingAttribute(elementIdx, "VALUE", idx);
        String tagToDelete = (String) comms.request.getParameter("del");
        if (tagToDelete != null && !tagToDelete.trim().equals("") && current_userid != null && current_userid.equals(OctopusApplication.ADMIN_LOGIN)) {
            tagToDelete = StringUtil.replace(tagToDelete, "%", "");
            OctopusRequestFactory.deleteTag(tagToDelete);
        }
        Element elementApplications = getElement("cbApplications");
        printComboBox(elementApplications, current_application, "Applications");
        Element elementLanguages = getElement("cbLanguages");
        printComboBox(elementLanguages, current_language, "Languages");
        Link listing = new Link("listing.po");
        listing.addParam("Application", current_application);
        listing.addParam("Language", current_language);
        listing.addParam("idx", idx);
        DOMUtil.setAttribute(getElement("otherview"), "HREF", listing.toString());
        DOMUtil.setFirstNodeText(getElement("mainlg"), OctopusApplication.MASTER_LANGUAGE);
        Vector vAllTag = OctopusRequest.getAllTags(current_application, OctopusApplication.MASTER_LANGUAGE);
        if (vAllTag != null) {
            int nb_max = 51;
            try {
                nb_max = Integer.parseInt(OctopusApplication.NUMBER_MAX_OF_TAGS_DISPLAYED_FOR_LISTING_TAGS_PAGE);
            } catch (NumberFormatException ne) {
            }
            printItems(vAllTag, nb_max);
            if (vAllTag.size() > 0) {
                String nbtag = String.valueOf(vAllTag.size());
                if (vAllTag.size() == 1) nbtag += " Tag "; else nbtag += " Tags ";
                DOMUtil.setFirstNodeText(getElement("nbtag"), nbtag);
            } else {
                removeElement("trtemp");
                removeElement("nbtag");
                removeElement("title");
            }
        } else {
            removeElement("trtemp");
            removeElement("nbtag");
            removeElement("nexprev");
            removeElement("nexprev1");
            removeElement("title");
        }
    }

    /**
     * Method used to Print all the Translation from a Vector <BR>
     * This Method manage as well Next and Previous link according to the parameters
     *
     * @param vAllItems Vector that contains the ObjectTranslation Objects
     * @param nb_max_displayed Number Maximal of Tag shown in the Page
     */
    private void printItems(Vector vAllItems, int nb_max_displayed) throws HttpPresentationException {
        String idx_label = "idx";
        String line_element_id = "trtemp";
        String noitem_element_id = "noitem";
        String target_page_name = "alltags.po";
        String link_next_element_id = "nextlink";
        String link_next_element_id1 = "nextlink1";
        String link_previous_element_id = "backlink";
        String link_previous_element_id1 = "backlink1";
        String line_nextprevious_element_id = "nextprev";
        String line_nextprevious_element_id1 = "nextprev1";
        String sIdx = comms.request.getParameter(idx_label);
        if (sIdx == null) sIdx = "0";
        int idx = Integer.parseInt(sIdx);
        int originalidx = idx;
        int counter = 0;
        Vector vItemsToPrint = new Vector();
        for (int k = idx; k < idx + nb_max_displayed && k < vAllItems.size(); k++) {
            vItemsToPrint.add(vAllItems.get(k));
            counter++;
        }
        idx = idx + counter;
        int counter_nbtagtobetranslated = 0;
        if (vItemsToPrint.size() > 0) {
            removeElement(noitem_element_id);
            int nbOfItemsToPrint = vItemsToPrint.size();
            int nbLine = nbOfItemsToPrint;
            int counter_user = 0;
            Element line = getElement(line_element_id);
            for (int j = 0; j < nbLine; j++) {
                Element new_line = (Element) line.cloneNode(true);
                Element td_tagid = getElement(new_line, "tagidline");
                Element td_gbtext = getElement(new_line, "gbtext");
                Element td_transtext = getElement(new_line, "transtext");
                ObjectTranslation oItem = null;
                if (counter_user < nbOfItemsToPrint) {
                    oItem = (ObjectTranslation) vItemsToPrint.get(counter_user);
                }
                if (oItem != null) {
                    counter_user++;
                    String tagId = oItem.getId();
                    if (tagId != null) {
                        String tag_status = "";
                        String tagTranslationVersion = "-";
                        String tagMainLanguageVersion = oItem.getVersion();
                        if (tagMainLanguageVersion == null) tagMainLanguageVersion = "0";
                        String tagAuthor = oItem.getLUN();
                        if (tagAuthor == null) tagAuthor = "-";
                        String tagDate = oItem.getLUD();
                        if (tagDate == null) tagDate = "-";
                        String tagDescription = oItem.getInfo();
                        if (tagDescription == null) tagDescription = "-";
                        String tagMainLanguageText = oItem.getTranslation();
                        if (tagMainLanguageText == null) tagMainLanguageText = "-";
                        String tagTranslationText = "-";
                        if (!current_language.equals(OctopusApplication.MASTER_LANGUAGE)) {
                            ObjectTranslation oTranslatedItem = (ObjectTranslation) OctopusRequest.getTranslation(tagId, current_language);
                            if (oTranslatedItem != null) {
                                tagTranslationText = oTranslatedItem.getTranslation();
                                if (tagTranslationText == null) tagTranslationText = "Not yet Translated!";
                            }
                        } else {
                            tagTranslationText = tagMainLanguageText;
                        }
                        if (!current_language.equals(OctopusApplication.MASTER_LANGUAGE)) {
                            ObjectTranslation oTranslatedItem = (ObjectTranslation) OctopusRequest.getTranslation(tagId, current_language);
                            if (oTranslatedItem != null) {
                                tagTranslationText = oTranslatedItem.getTranslation();
                                if (tagTranslationText == null) tagTranslationText = "Not yet Translated!";
                                tagTranslationVersion = oTranslatedItem.getVersion();
                                if (tagTranslationVersion == null) {
                                    tagTranslationVersion = "X";
                                    tag_status = "E";
                                    counter_nbtagtobetranslated++;
                                } else if (new Integer(tagTranslationVersion).intValue() < new Integer(tagMainLanguageVersion).intValue()) {
                                    tag_status = "O";
                                    counter_nbtagtobetranslated++;
                                }
                            }
                        }
                        String textToPrint = "<U><B>Desc:</B></U><BR>" + StringUtil.replace(tagDescription, "'", "&#x27;");
                        textToPrint += "<BR>-----------<BR><B><U>" + OctopusApplication.MASTER_LANGUAGE + ":</U></B><BR>" + StringUtil.replace(tagMainLanguageText, "'", "&#x27;") + "(" + tagMainLanguageVersion + ")";
                        if (!current_language.equals(OctopusApplication.MASTER_LANGUAGE)) {
                            textToPrint += "<BR>-----------<BR><B><U>" + current_language + ":</U></B><BR>" + StringUtil.replace(tagTranslationText, "'", "&#x27;") + "(" + tagTranslationVersion + ")";
                        }
                        textToPrint += "<BR>-----------<BR> " + tagAuthor + " " + tagDate;
                        DOMUtil.setFirstMatchingAttribute(td_tagid, "onmouseover", "return overlib('" + textToPrint + "',CAPTION,'" + tagId + "',VAUTO,HAUTO);");
                        DOMUtil.setFirstMatchingAttribute(td_tagid, "href", "javascript:show_window('manage','" + tagId + "','" + current_application + "','" + current_language + "','true');");
                        DOMUtil.setFirstNodeText(getElement(td_tagid, "tag_status"), tag_status);
                        DOMUtil.setFirstNodeText(getElement(td_tagid, "tag_id"), tagId);
                        DOMUtil.setFirstNodeText(getElement(td_tagid, "tag_nb"), String.valueOf(counter_user + originalidx));
                        removeElement("nbtagtobetranslated");
                        DOMUtil.setFirstNodeText(td_gbtext, tagMainLanguageText);
                        DOMUtil.setFirstNodeText(td_transtext, tagTranslationText);
                        if (current_userid.equals(OctopusApplication.ADMIN_LOGIN) && current_language.equals(OctopusApplication.MASTER_LANGUAGE)) {
                            DOMUtil.setFirstMatchingAttribute(getElement("title"), "colspan", "2");
                            DOMUtil.setFirstMatchingAttribute(getElement(new_line, "deleteLink"), "href", "alltags.po?del=" + tagId);
                            DOMUtil.setFirstMatchingAttribute(getElement(new_line, "deleteLink"), "onclick", "return areYouSure('" + OctopusErrorMessages.ARE_YOU_SURE_TAG + ": " + tagId + " ?')");
                        } else {
                            removeElement(new_line, "deleteLink");
                        }
                    }
                }
                line.getParentNode().insertBefore(new_line, line);
            }
            line.getParentNode().removeChild(line);
        } else {
            removeElement(line_element_id);
            removeElement("nbtag");
        }
        if (link_next_element_id != null && link_previous_element_id != null && line_nextprevious_element_id != null && link_next_element_id1 != null && link_previous_element_id1 != null && line_nextprevious_element_id1 != null) {
            boolean nexprev = false;
            if (idx >= vAllItems.size()) {
                removeElement(link_next_element_id);
                removeElement(link_next_element_id1);
                nexprev = false;
            } else {
                Link target_pageNext = new Link(target_page_name);
                target_pageNext.addParam("Application", current_application);
                target_pageNext.addParam("Language", current_language);
                target_pageNext.addParam(idx_label, idx);
                getElement(link_next_element_id).setAttribute("href", target_pageNext.toString());
                getElement(link_next_element_id1).setAttribute("href", target_pageNext.toString());
                nexprev = true;
            }
            if (idx - nb_max_displayed > 0) {
                int var = idx - nb_max_displayed - counter;
                if (var < 0) var = 0;
                Link target_pagePrevious = new Link(target_page_name);
                target_pagePrevious.addParam("Application", current_application);
                target_pagePrevious.addParam("Language", current_language);
                target_pagePrevious.addParam(idx_label, var);
                getElement(link_previous_element_id).setAttribute("href", target_pagePrevious.toString());
                getElement(link_previous_element_id1).setAttribute("href", target_pagePrevious.toString());
                nexprev = true;
            } else {
                removeElement(link_previous_element_id);
                removeElement(link_previous_element_id1);
            }
            if (!nexprev) {
                removeElement(line_nextprevious_element_id);
                removeElement(line_nextprevious_element_id1);
            }
        }
    }
}
