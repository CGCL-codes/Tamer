package org.verus.ngl.web.beans.technicalprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import org.verus.ngl.sl.bprocess.technicalprocessing.NGLAuthoritySearch;
import org.verus.ngl.sl.utilities.NewGenLibRoot;
import org.verus.ngl.sl.objectmodel.technicalprocessing.AUTHORITY_CUSTOM_INDEX;
import org.verus.ngl.utilities.NGLXMLUtility;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import org.richfaces.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.swing.JEditorPane;
import org.jdom.Element;
import org.verus.ngl.sl.bprocess.technicalprocessing.AuthorityFiles;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.utilities.NGLUtility;
import org.verus.ngl.utilities.marc.NGLConverter;
import org.verus.ngl.utilities.marc.NGLMARCRecord;
import org.verus.ngl.utilities.marc.NGLSubfield;
import org.verus.ngl.utilities.marc.NGLTag;
import org.verus.ngl.web.jsp.customtags.NGLMARCEditor;
import org.verus.ngl.web.util.NGLButtonConstants;
import org.verus.ngl.web.util.NGLUserSession;

/**
 *
 * @author root
 */
public class AuthorityFileSearch implements NGLButtonConstants {

    org.verus.ngl.sl.bprocess.technicalprocessing.AuthorityFiles authorityFilesInt;

    private String authorityFileType;

    private String checkall;

    private Hashtable indexHash = new Hashtable();

    private List<AuthorityFile> authoritySearchResultsTemp;

    private List<AuthorityFile> authoritySearchResults;

    private List<AuthorityFile> displayResults = new ArrayList<AuthorityFile>();

    private HtmlDataTable authorityResultTable;

    private HtmlDataTable authorityDisplayResultTable;

    private String pageno;

    private String searchTerm;

    private HtmlSelectOneMenu htmlSelectOneMenuIT;

    private UISelectItems indexTypesSelectItems;

    private SelectItemGroup itemGroup;

    private NGLTag validationTag = null;

    private String deleteMode;

    private String recordsPerPage = "10";

    private org.verus.ngl.utilities.NGLUtility utility = null;

    private HtmlSelectBooleanCheckbox chkBoxAll = null;

    private HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox;

    private HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox1;

    private Vector selectedIds = new Vector();

    private Vector selectedObjects = new Vector();

    private HtmlMenuItem miViewAuthorisedHeading = new HtmlMenuItem();

    private HtmlMenuItem miViewOtherTerms = new HtmlMenuItem();

    private AuthorityFile authorityFileM;

    private String message = "Please select atleast one record";

    private String note = " ";

    private String bnYesValue = "Ok";

    private String bnNoValue = "Cancel";

    private String mode;

    private Vector tempVect = new Vector();

    private Integer selectedRecordsCount = 0;

    private Integer selectedAHRecordsCount = 0;

    private Integer afId = 0;

    private Integer clickedButton = BUTTON_CANCEL;

    private Integer from = 1;

    private Integer to = 10;

    private HtmlCommandButton bnPrev = null;

    private HtmlCommandButton bnNext = null;

    public AuthorityFileSearch() {
        utility = NGLUtility.getInstance();
        authorityFilesInt = (org.verus.ngl.sl.bprocess.technicalprocessing.AuthorityFiles) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("authorityFiles");
        chkBoxAll = new HtmlSelectBooleanCheckbox();
        htmlSelectOneMenuIT = new HtmlSelectOneMenu();
        selectedIds = new Vector();
        selectedObjects = new Vector();
        bnPrev = new HtmlCommandButton();
        bnPrev.setDisabled(true);
        bnNext = new HtmlCommandButton();
        bnNext.setDisabled(true);
        loadAuthorityTypes();
    }

    public String bnSelectExisting() {
        System.out.println("selectedIds.size ========== " + selectedIds.size());
        if (selectedIds.size() == 1) {
            afId = (Integer) new Integer(selectedIds.get(0).toString().trim());
            AuthorityFile aFile = (AuthorityFile) selectedObjects.get(0);
            String authXML = aFile.getAuthorityXML();
            NGLMARCRecord[] mARCRecords = NGLConverter.getInstance().getMarcModel(authXML);
            NGLTag nGLTag = mARCRecords[0].get1XXTag();
            nGLTag.setAfId(afId.toString());
            validationTag = nGLTag;
            clickedButton = BUTTON_SELECT_EXISTING;
        } else {
            afId = 0;
        }
        return null;
    }

    public String bnCreateNew() {
        if (validationTag != null) {
            String headingType = validationTag.getTag();
            NGLMARCEditor mARCEditor = new NGLMARCEditor(headingType, "A");
            NGLMARCRecord mARCRecord = mARCEditor.getNGLMARCRecord();
            if (mARCRecord == null) {
                mARCRecord = new NGLMARCRecord();
            }
            NGLTag[] nGLTags = mARCRecord.getTags();
            if (nGLTags != null && nGLTags.length > 0) {
                for (int i = 0; i < nGLTags.length; i++) {
                    NGLTag nGLTag = nGLTags[i];
                    if (validationTag.getTag().equalsIgnoreCase(nGLTag.getTag())) {
                        nGLTag = validationTag;
                        break;
                    }
                }
            } else {
                mARCRecord.addTag(validationTag);
            }
            String authorityXML = NGLConverter.getInstance().getMarcXml(new NGLMARCRecord[] { mARCRecord });
            if (authorityXML != null) {
                NGLUserSession userSession = new NGLUserSession();
                AuthorityFiles authorityFiles = (AuthorityFiles) NGLBeanFactory.getInstance().getBean("authorityFiles");
                Integer id = authorityFiles.bm_createAuthorityRecord(userSession.getUserId(), Integer.parseInt(userSession.getLibraryId()), userSession.getDatabaseId(), headingType, authorityXML);
                if (id > 0) {
                    afId = id;
                    if (validationTag != null) {
                        String authSFIdentifier = getAuthSFIdentifier(validationTag.getTag());
                        if (authSFIdentifier.length() > 0) {
                            NGLSubfield[] subfields = validationTag.getSubfields();
                            if (subfields != null && subfields.length > 0) {
                                for (int i = 0; i < subfields.length; i++) {
                                    NGLSubfield nGLSubfield = subfields[i];
                                    if (nGLSubfield.getIdentifier() == authSFIdentifier.charAt(0)) {
                                        nGLSubfield.setAfId(id.toString());
                                    }
                                }
                            }
                        } else {
                            validationTag.setAfId(afId.toString());
                        }
                    }
                    clickedButton = BUTTON_CREATE_NEW;
                }
            }
        }
        return null;
    }

    private String getAuthSFIdentifier(String tag) {
        if (tag != null) {
            if (tag.equalsIgnoreCase("185")) {
                return "v";
            } else if (tag.equalsIgnoreCase("180")) {
                return "x";
            } else if (tag.equalsIgnoreCase("182")) {
                return "y";
            } else if (tag.equalsIgnoreCase("181")) {
                return "z";
            }
        }
        return "";
    }

    public Integer getSelectedRecordsCount() {
        return selectedRecordsCount;
    }

    public void setSelectedRecordsCount(Integer selectedRecordsCount) {
        this.selectedRecordsCount = selectedRecordsCount;
    }

    public String selectActionPerformed() {
        AuthorityFile authorityFile = (AuthorityFile) authorityResultTable.getRowData();
        this.setAuthorityFileM(authorityFile);
        String temp = authorityFile.getSeeTerms();
        if (htmlSelectBooleanCheckbox.isSelected()) {
            selectedRecordsCount++;
            if (temp != null && temp.trim().equalsIgnoreCase("AH")) {
                selectedAHRecordsCount++;
            }
        } else {
            selectedRecordsCount--;
            if (temp != null && temp.trim().equalsIgnoreCase("AH")) {
                selectedAHRecordsCount--;
            }
        }
        if (authorityFile != null && this.getHtmlSelectBooleanCheckbox().isSelected() == true && !temp.equalsIgnoreCase("ST")) {
            if (!selectedIds.contains(authorityFile.getId())) {
                selectedIds.addElement(authorityFile.getId());
                if (selectedObjects.size() > 0) {
                    boolean flag = true;
                    for (int i = 0; i < selectedObjects.size(); i++) {
                        AuthorityFile af = (AuthorityFile) selectedObjects.get(i);
                        if (af.getId().equalsIgnoreCase(authorityFile.getId())) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        selectedObjects.addElement(authorityFile);
                    }
                } else {
                    selectedObjects.addElement(authorityFile);
                }
            }
            System.out.println("$$$$$$$$$$:" + authorityFile.getId() + ", size ========= " + selectedObjects.size());
        }
        if (authorityFile != null && this.getHtmlSelectBooleanCheckbox().isSelected() == false && !temp.equalsIgnoreCase("ST")) {
            if (selectedIds.contains(authorityFile.getId())) {
                selectedIds.remove(authorityFile.getId());
                selectedObjects.remove(authorityFile);
            }
        }
        if (authorityFile.getSeeTerms().equalsIgnoreCase("AH")) {
            this.getMiViewAuthorisedHeading().setDisabled(true);
            this.getMiViewOtherTerms().setDisabled(false);
        } else if (authorityFile.getSeeTerms().equalsIgnoreCase("ST")) {
            this.getMiViewAuthorisedHeading().setDisabled(false);
            this.getMiViewOtherTerms().setDisabled(true);
        }
        String msg;
        String note1;
        String yesV;
        String noV;
        if (selectedIds.size() > 0) {
            msg = "Do you want to delete " + selectedIds.size() + " selected records...";
            note1 = "Note: See Term Heading records could not be deleted.";
            yesV = "Yes";
            noV = "NO";
        } else {
            msg = "Please select atleast one record...";
            note1 = " ";
            yesV = "Ok";
            noV = "Cancel";
        }
        this.setMessage(msg);
        this.setNote(note1);
        this.setBnYesValue(yesV);
        this.setBnNoValue(noV);
        return null;
    }

    public String bnCancelMethod() {
        if (getAuthoritySearchResults() != null) {
            getAuthoritySearchResults().clear();
        }
        if (authoritySearchResultsTemp != null) {
            authoritySearchResultsTemp.clear();
        }
        if (displayResults != null) {
            displayResults.clear();
        }
        afId = 0;
        validationTag = null;
        selectedAHRecordsCount = 0;
        selectedRecordsCount = 0;
        clickedButton = BUTTON_CANCEL;
        return null;
    }

    public String selectActionPerformedDisplay() {
        AuthorityFile authorityFile = (AuthorityFile) authorityDisplayResultTable.getRowData();
        if (authorityFile != null && this.getHtmlSelectBooleanCheckbox1().isSelected() == true) {
            String id = authorityFile.getId();
            if (!tempVect.contains(id)) {
                tempVect.addElement(id);
            }
        }
        if (authorityFile != null && this.getHtmlSelectBooleanCheckbox1().isSelected() == false) {
            String id = authorityFile.getId();
            if (tempVect.contains(id)) {
                tempVect.remove(id);
            }
        }
        return null;
    }

    public String bnAddActionPerformed() {
        if (selectedObjects.size() > 0) {
            displayResults = new ArrayList<AuthorityFile>();
            for (int i = 0; i < selectedObjects.size(); i++) {
                AuthorityFile af = (AuthorityFile) selectedObjects.elementAt(i);
                String authorityXml = af.getAuthorityXML();
                String xmlDisplayProfile = this.getDisplayProfileXml("DisplayProfilesAF");
                NGLMARCRecord nGLAFRecords[] = NGLConverter.getInstance().getMarcModel(authorityXml);
                String display = this.getDisplayToolTipText(nGLAFRecords, xmlDisplayProfile);
                af.setDisplayNew(display);
                displayResults.add(af);
            }
        }
        return null;
    }

    public String bnMinusActionPerformed() {
        if (tempVect.size() > 0 && displayResults.size() > 0) {
            for (int i = 0; i < displayResults.size(); i++) {
                AuthorityFile af = (AuthorityFile) displayResults.get(i);
                String id = af.getId();
                if (tempVect.contains(id)) {
                    displayResults.remove(i);
                    i--;
                }
            }
            tempVect.removeAllElements();
        }
        return null;
    }

    public String getDisplayToolTipText(NGLMARCRecord[] nGLAFRecords, String xmlDisplayProfile) {
        JEditorPane editorPane = null;
        try {
            String strHtml = "<html><body width=\"100%\" height=\"100%\">";
            Vector vDisplayProfile = this.getDisplayProfileStructure(xmlDisplayProfile);
            if (vDisplayProfile != null && nGLAFRecords != null && nGLAFRecords.length > 0) {
                strHtml += "<table width=\"100%\" border=\"0\">";
                for (int i = 0; i < nGLAFRecords.length; i++) {
                    if (nGLAFRecords[i] != null) {
                        String htmlNGLAFRecord = "";
                        String dpName = utility.getTestedString(vDisplayProfile.get(0)).trim();
                        String dpType = utility.getTestedString(vDisplayProfile.get(1)).trim();
                        Hashtable htAreas = (Hashtable) vDisplayProfile.get(2);
                        if (htAreas != null && htAreas.size() > 0) {
                            for (int j = 0; j < htAreas.size(); j++) {
                                Vector vArea = (Vector) htAreas.get(Integer.toString(j + 1));
                                if (vArea != null) {
                                    String htmlArea = "";
                                    String displayName = utility.getTestedString(vArea.get(0)).trim();
                                    Hashtable htTags = (Hashtable) vArea.get(1);
                                    if (htTags != null && htTags.size() > 0) {
                                        for (int k = 0; k < htTags.size(); k++) {
                                            Hashtable htTag = (Hashtable) htTags.get(Integer.toString(k + 1));
                                            if (htTag != null) {
                                                String htmlTag = "";
                                                String value = utility.getTestedString(htTag.get("Value")).trim();
                                                String i1FilterValue = utility.getTestedString(htTag.get("I1FilterValue")).trim();
                                                String i2FilterValue = utility.getTestedString(htTag.get("I2FilterValue")).trim();
                                                String repetitionSeparator = utility.getTestedString(htTag.get("RepetitionSeparator"));
                                                String tagSeparator = utility.getTestedString(htTag.get("TagSeparator"));
                                                String tagSecurity = utility.getTestedString(htTag.get("TagSecurity")).trim();
                                                if (value.length() > 0) {
                                                    NGLTag nGLTags[] = nGLAFRecords[i].getTags();
                                                    if (nGLTags != null && nGLTags.length > 0) {
                                                        for (int l = 0; l < nGLTags.length; l++) {
                                                            if (nGLTags[l] != null) {
                                                                String htmlRepTag = "";
                                                                String tag = utility.getTestedStringWithTrim(nGLTags[l].getTag());
                                                                if (tag.length() > 0 && (tag.charAt(0) == value.charAt(0))) {
                                                                    htmlRepTag += nGLTags[l].getDisplayString();
                                                                }
                                                                if (!htmlRepTag.equalsIgnoreCase("")) {
                                                                    if (!htmlTag.equalsIgnoreCase("")) {
                                                                        htmlTag += repetitionSeparator;
                                                                    }
                                                                    htmlTag += htmlRepTag;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!htmlTag.equalsIgnoreCase("")) {
                                                    if (!htmlArea.equalsIgnoreCase("")) {
                                                        htmlArea += tagSeparator;
                                                    }
                                                    if (value.startsWith("1")) {
                                                        htmlArea += "<b>" + htmlTag + "</b>";
                                                    } else {
                                                        htmlArea += htmlTag;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!htmlArea.equalsIgnoreCase("")) {
                                        htmlNGLAFRecord += "<tr><td width=\"20%\" align=\"left\" valign=\"top\"><font face=\"SansSerif\" size=\"4\"><b>" + displayName + "</b></font></td><td width=\"80%\" align=\"left\" valign=\"top\"><font face=\"SansSerif\" size=\"4\">" + htmlArea + "</font></td></tr>";
                                    }
                                }
                            }
                        }
                        if (!htmlNGLAFRecord.equalsIgnoreCase("")) {
                            if (i != 0) {
                                strHtml += "<tr><td width=\"100%\"><hr width=\"100%\"></td></tr>";
                            }
                            strHtml += "<tr><td width=\"100%\"><table width=\"100%\" border=\"0\">" + htmlNGLAFRecord + "</table></td></tr>";
                        }
                    }
                }
                strHtml += "</table>";
            }
            strHtml += "</body></html>";
            editorPane = new JEditorPane();
            editorPane.setContentType("text/html");
            editorPane.setText(strHtml);
        } catch (Exception e) {
            System.out.println("Exception in getDisplayToolTipText method : " + e);
            editorPane = new JEditorPane();
            editorPane.setText("EXCEPTION");
        }
        return editorPane.getText();
    }

    private Vector getDisplayProfileStructure(String xmlDisplayProfile) {
        Vector vDP = null;
        try {
            Element root = NGLXMLUtility.getInstance().getRootElementFromXML(xmlDisplayProfile);
            if (root != null) {
                vDP = new Vector();
                String dpName = "";
                String dpType = "";
                Hashtable htAreas = null;
                dpType = this.utility.getTestedString(root.getChildText("Type")).trim();
                Locale locale = null;
                if (locale == null) {
                    locale = new Locale("");
                }
                Element eleName = root.getChild("Name");
                List listLocales = eleName.getChildren("Locale");
                if (listLocales != null) {
                    boolean keyLocale = true;
                    if (!this.utility.getTestedString(locale.getLanguage()).equals("")) {
                        for (int j = 0; j < listLocales.size(); j++) {
                            Element eleLocale = (Element) listLocales.get(j);
                            String localeValue = this.utility.getTestedString(eleLocale.getAttributeValue("value"));
                            if (localeValue.equals(locale.getLanguage() + "_" + locale.getCountry())) {
                                dpName = this.utility.getTestedString(eleLocale.getText());
                                keyLocale = false;
                            }
                        }
                        if (keyLocale) {
                            for (int j = 0; j < listLocales.size(); j++) {
                                Element eleLocale = (Element) listLocales.get(j);
                                String localeValue = this.utility.getTestedString(eleLocale.getAttributeValue("value"));
                                if (localeValue.equals("")) {
                                    dpName = this.utility.getTestedString(eleLocale.getText());
                                }
                            }
                        }
                    }
                }
                List listAreas = root.getChildren("Area");
                if (listAreas != null && listAreas.size() > 0) {
                    htAreas = new Hashtable();
                    for (int i = 0; i < listAreas.size(); i++) {
                        Element eleArea = (Element) listAreas.get(i);
                        if (eleArea != null) {
                            Vector vArea = new Vector();
                            String areaDisplayName = "";
                            Hashtable htTags = null;
                            String areaSequence = this.utility.getTestedString(eleArea.getAttributeValue("sequence"));
                            String type = this.utility.getTestedString(eleArea.getAttributeValue("type"));
                            if (type.equalsIgnoreCase("")) {
                                Element eleDisplayName = eleArea.getChild("DisplayName");
                                listLocales = eleDisplayName.getChildren("Locale");
                                if (listLocales != null) {
                                    boolean keyLocale = true;
                                    if (!this.utility.getTestedString(locale.getLanguage()).equals("")) {
                                        for (int j = 0; j < listLocales.size(); j++) {
                                            Element eleLocale = (Element) listLocales.get(j);
                                            String localeValue = this.utility.getTestedString(eleLocale.getAttributeValue("value"));
                                            if (localeValue.equals(locale.getLanguage() + "_" + locale.getCountry())) {
                                                areaDisplayName = this.utility.getTestedString(eleLocale.getText());
                                                keyLocale = false;
                                            }
                                        }
                                    }
                                    if (keyLocale) {
                                        for (int j = 0; j < listLocales.size(); j++) {
                                            Element eleLocale = (Element) listLocales.get(j);
                                            String localeValue = this.utility.getTestedString(eleLocale.getAttributeValue("value"));
                                            if (localeValue.equals("")) {
                                                areaDisplayName = this.utility.getTestedString(eleLocale.getText());
                                            }
                                        }
                                    }
                                }
                                List listTags = eleArea.getChildren("Tag");
                                if (listTags != null && listTags.size() > 0) {
                                    htTags = new Hashtable();
                                    for (int j = 0; j < listTags.size(); j++) {
                                        Element eleTag = (Element) listTags.get(j);
                                        if (eleTag != null) {
                                            Hashtable htTag = null;
                                            String tagSequence = this.utility.getTestedString(eleTag.getAttributeValue("sequence"));
                                            String tagValue = "";
                                            String i1Value = "";
                                            String i2Value = "";
                                            String repSep = "";
                                            String tagSep = "";
                                            Boolean tagSecurity = true;
                                            tagValue = this.utility.getTestedString(eleTag.getChildText("Value"));
                                            i1Value = this.utility.getTestedString(eleTag.getChildText("I1FilterValue"));
                                            i2Value = this.utility.getTestedString(eleTag.getChildText("I2FilterValue"));
                                            repSep = this.utility.getTestedString(eleTag.getChildText("RepetitionSeparator"));
                                            tagSep = this.utility.getTestedString(eleTag.getChildText("TagSeparator"));
                                            Element eleSecurity = eleTag.getChild("Security");
                                            if (eleSecurity != null) {
                                            }
                                            htTag = new Hashtable();
                                            htTag.put("Value", tagValue);
                                            htTag.put("I1FilterValue", i1Value);
                                            htTag.put("I2FilterValue", i2Value);
                                            htTag.put("RepetitionSeparator", repSep);
                                            htTag.put("TagSeparator", tagSep);
                                            if (tagSecurity) {
                                                htTag.put("TagSecurity", "true");
                                            } else {
                                                htTag.put("TagSecurity", "false");
                                            }
                                            htTags.put(tagSequence, htTag);
                                        }
                                    }
                                }
                            }
                            vArea.add(areaDisplayName);
                            vArea.add(htTags);
                            htAreas.put(areaSequence, vArea);
                        }
                    }
                }
                vDP.add(dpName);
                vDP.add(dpType);
                vDP.add(htAreas);
            }
        } catch (Exception e) {
            System.out.println("Exception in getDisplayProfileStructure method : " + e);
        }
        return vDP;
    }

    public String getDisplayProfileXml(String fileNameOfDisplayProfile) {
        String xmlDisplayProfile = "";
        try {
            String localPath = this.getFileLocalPath(fileNameOfDisplayProfile);
            if (!localPath.equals("")) {
                BufferedReader in = new BufferedReader(new FileReader(localPath));
                String str = "";
                while ((str = in.readLine()) != null) {
                    xmlDisplayProfile += str + "\n";
                }
                in.close();
            }
        } catch (Exception e) {
            System.out.println("Exception in getDisplayProfileXml method : " + e);
        }
        return xmlDisplayProfile;
    }

    private String getFileLocalPath(String file) {
        String localPath = "";
        NewGenLibRoot newGenLibRoot = (org.verus.ngl.sl.utilities.NewGenLibRoot) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("newGenLibRoot");
        String defaultLocalPath = newGenLibRoot.getRoot();
        String localDPpath = defaultLocalPath + "/MarcDictionary/" + file + ".xml";
        File dpFile = new File(localDPpath);
        if (dpFile.exists()) {
            long lastModified = dpFile.lastModified();
            String lmDate = Long.toString(lastModified);
            Vector vFile = this.getFileFromServer(file, lmDate);
            if (vFile.size() > 0) {
                String content = (String) vFile.get(0);
                if (content != null && !content.equals("")) {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(dpFile));
                        out.write(content);
                        out.close();
                    } catch (Exception e) {
                        System.out.println("Exception in getFileLocalPath method : " + e);
                    }
                } else {
                }
            } else {
            }
        } else {
            Vector vFile = this.getFileFromServer(file, "0");
            if (vFile.size() > 0) {
                String content = (String) vFile.get(0);
                if (content != null && !content.equals("")) {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(dpFile));
                        out.write(content);
                        out.close();
                    } catch (Exception e) {
                        System.out.println("Exception in getFileLocalPath method : " + e);
                    }
                } else {
                }
            } else {
            }
        }
        localPath = localDPpath;
        return localPath;
    }

    private Vector getFileFromServer(String file, String lmDate) {
        Element root = new org.jdom.Element("Root");
        root.setAttribute("process", "5");
        Element eleFileName = new Element("FileName");
        eleFileName.setText(file);
        root.addContent(eleFileName);
        Element eleLastModified = new Element("LastModified");
        eleLastModified.setText(lmDate);
        root.addContent(eleLastModified);
        String requestXml = NGLXMLUtility.getInstance().generateXML(root);
        Vector vFile = (Vector) new org.verus.ngl.web.administration.technicalprocessing.MARCDictionaryHandler().processRequest(requestXml);
        return vFile;
    }

    public String viewAuthorisedHeading() {
        clearSelectedRecords();
        System.out.println("@@@@@@@@@ this is in viewAuthorisedHeading");
        List<AuthorityFile> tempList = new ArrayList<AuthorityFile>();
        AuthorityFile authorityFile = this.getAuthorityFileM();
        if (authorityFile != null) {
            tempList.add(authorityFile);
            if (authorityFile.getSeeTerms().equalsIgnoreCase("ST")) {
                String authorityXml = authorityFile.getAuthorityXML();
                NGLTag nGLTag = this.getAuthorisedHeading(authorityXml);
                if (nGLTag != null) {
                    String displayString = nGLTag.getDisplayString();
                    AuthorityFile af = new AuthorityFile();
                    af.setId(authorityFile.getId());
                    af.setDisplay(displayString);
                    af.setAuthorityType(authorityFile.getAuthorityType());
                    af.setSeeTerms("AH");
                    af.setAuthorityXML(authorityXml);
                    tempList.add(af);
                }
            }
        }
        this.setAuthoritySearchResultsTemp(tempList);
        this.selectedIds.removeAllElements();
        return null;
    }

    private NGLTag getAuthorisedHeading(String authorityXml) {
        NGLMARCRecord nGLMARCRecords[] = NGLConverter.getInstance().getMarcModel(authorityXml);
        if (nGLMARCRecords != null && nGLMARCRecords.length > 0) {
            NGLTag nGLTags[] = nGLMARCRecords[0].getTags();
            if (nGLTags != null && nGLTags.length > 0) {
                for (int i = 0; i < nGLTags.length; i++) {
                    if (nGLTags[i] != null) {
                        String tag = nGLTags[i].getTag();
                        if (tag.startsWith("1")) {
                            return nGLTags[i];
                        }
                    }
                }
            }
        }
        return null;
    }

    private void clearSelectedRecords() {
        selectedRecordsCount = 0;
        selectedAHRecordsCount = 0;
        selectedObjects = new Vector();
        selectedIds = new Vector();
    }

    public String viewOtherTerms() {
        clearSelectedRecords();
        List<AuthorityFile> tempList = new ArrayList<AuthorityFile>();
        AuthorityFile authorityFile = this.getAuthorityFileM();
        if (authorityFile != null) {
            tempList.add(authorityFile);
            if (authorityFile.getSeeTerms().equalsIgnoreCase("AH")) {
                String authorityXml = authorityFile.getAuthorityXML();
                NGLTag nGLTags[] = this.getSeeTerms(authorityXml);
                if (nGLTags != null && nGLTags.length > 0) {
                    for (int i = 0; i < nGLTags.length; i++) {
                        if (nGLTags[i] != null) {
                            String displayString = utility.getTestedString(nGLTags[i].getDisplayString());
                            AuthorityFile af = new AuthorityFile();
                            af.setId(authorityFile.getId());
                            af.setDisplay(displayString);
                            af.setAuthorityType(authorityFile.getAuthorityType());
                            af.setSeeTerms("ST");
                            af.setAuthorityXML(authorityXml);
                            tempList.add(af);
                        }
                    }
                }
            }
        }
        this.selectedIds.removeAllElements();
        this.setAuthoritySearchResultsTemp(tempList);
        return null;
    }

    private NGLTag[] getSeeTerms(String authorityXml) {
        NGLTag[] nGLTagsResult = null;
        NGLMARCRecord nGLMARCRecords[] = NGLConverter.getInstance().getMarcModel(authorityXml);
        if (nGLMARCRecords != null && nGLMARCRecords.length > 0) {
            NGLTag nGLTags[] = nGLMARCRecords[0].getTags();
            if (nGLTags != null && nGLTags.length > 0) {
                Vector vNGLTags = new Vector();
                for (int i = 0; i < nGLTags.length; i++) {
                    if (nGLTags[i] != null) {
                        String tag = utility.getTestedStringWithTrim(nGLTags[i].getTag());
                        if (tag.startsWith("4")) {
                            vNGLTags.addElement(nGLTags[i]);
                        }
                    }
                }
                nGLTagsResult = new NGLTag[vNGLTags.size()];
                nGLTagsResult = (NGLTag[]) vNGLTags.toArray(nGLTagsResult);
            }
        }
        return nGLTagsResult;
    }

    public String pagePrevious() {
        Integer recPerPage = Integer.parseInt(recordsPerPage);
        from = from - recPerPage;
        to = to - recPerPage;
        if (from <= 1) {
            from = 1;
            to = recPerPage;
            bnPrev.setDisabled(true);
        }
        bnNext.setDisabled(false);
        authoritySearchResults = search(from, to);
        return null;
    }

    public String pageNext() {
        from += Integer.parseInt(recordsPerPage);
        to += Integer.parseInt(recordsPerPage);
        authoritySearchResults = search(from, to);
        List list = search(from + Integer.parseInt(recordsPerPage), to + Integer.parseInt(recordsPerPage));
        if (list == null || list.size() == 0) {
            bnNext.setDisabled(true);
        }
        bnPrev.setDisabled(false);
        System.out.println("........ " + bnPrev.isDisabled());
        return null;
    }

    public int getCurrentPage() {
        int rows = authorityResultTable.getRows();
        int first = authorityResultTable.getFirst();
        int count = authorityResultTable.getRowCount();
        return (count / rows) - ((count - first) / rows) + 1;
    }

    public int getTotalPages() {
        int rows = authorityResultTable.getRows();
        int count = authorityResultTable.getRowCount();
        return (count / rows) + ((count % rows != 0) ? 1 : 0);
    }

    public String deleteYESModeAction() {
        System.out.println("in deleteYESModeAction.......");
        System.out.println("@@@@@@@@@@@@@@@:" + this.getDeleteMode());
        try {
            System.out.println(".......this is in delete authority file method..");
            org.jdom.Element root = new org.jdom.Element("Root");
            if (selectedIds.size() > 0) {
                for (int i = 0; i < selectedIds.size(); i++) {
                    org.jdom.Element id = new org.jdom.Element("Id");
                    id.setText(selectedIds.elementAt(i).toString());
                    root.addContent(id);
                }
            }
            String xml = NGLXMLUtility.getInstance().generateXML(root);
            System.out.println("=====Request XML:" + xml);
            String respXml = authorityFilesInt.bm_deleteAuthorityFile(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadAuthorityTypes() {
        setItemGroup(new SelectItemGroup());
        try {
            NGLUserSession userSession = new NGLUserSession();
            NGLAuthoritySearch nGLAuthoritySearch = (NGLAuthoritySearch) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("nGLAuthoritySearch");
            List authorityList = nGLAuthoritySearch.bm_getAuthorityIndexes(userSession.getLibraryId(), userSession.getDatabaseId());
            if (authorityList != null && authorityList.size() > 0) {
                SelectItem selectItems[] = new SelectItem[authorityList.size()];
                for (int i = 0; i < authorityList.size(); i++) {
                    AUTHORITY_CUSTOM_INDEX local = (AUTHORITY_CUSTOM_INDEX) authorityList.get(i);
                    selectItems[i] = new SelectItem(local.getIndex_definition(), local.getIndex_display());
                    getIndexHash().put(local.getIndex_name(), local.getIndex_display());
                }
                getItemGroup().setSelectItems(selectItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SelectItemGroup getRecordsPerPageList() {
        try {
            String[] nums = new String[] { "10", "20", "30", "40", "50" };
            SelectItemGroup ig = new SelectItemGroup();
            SelectItem selectItems[] = new SelectItem[nums.length];
            for (int i = 0; i < nums.length; i++) {
                selectItems[i] = new SelectItem(nums[i], nums[i]);
            }
            ig.setSelectItems(selectItems);
            return ig;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Element getIndexQuery(Element eleQuery, String indexDefinition) {
        try {
            Element eleIndex = NGLXMLUtility.getInstance().getRootElementFromXML(indexDefinition);
            eleIndex.setAttribute("booleanForAdjacentIndexes", "OR");
            Element copyIndex = (Element) eleIndex.clone();
            eleQuery.addContent(copyIndex);
        } catch (Exception e) {
        }
        return eleQuery;
    }

    private String getIndexDefinitionWithTerm(String indexDefinition, String term) {
        try {
            Element root = NGLXMLUtility.getInstance().getRootElementFromXML(indexDefinition);
            if (root != null) {
                List listIndexes = root.getChildren("Index");
                if (listIndexes != null && listIndexes.size() > 0) {
                    for (int i = 0; i < listIndexes.size(); i++) {
                        Element eleIndex = (Element) listIndexes.get(i);
                        if (eleIndex != null) {
                            List<Element> listFields = eleIndex.getChildren("Field");
                            if (listFields != null && listFields.size() > 0) {
                                for (int j = 0; j < listFields.size(); j++) {
                                    Element eleField = listFields.get(j);
                                    if (eleField != null) {
                                        String hardCodedValue = utility.getTestedString(eleField.getAttributeValue("hardCodedValue"));
                                        if (hardCodedValue.equalsIgnoreCase("")) {
                                            eleField.getAttribute("hardCodedValue").setValue(term);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                org.jdom.Element copy = (org.jdom.Element) root.clone();
                indexDefinition = NGLXMLUtility.getInstance().generateXML(copy);
            }
        } catch (Exception e) {
        }
        return indexDefinition;
    }

    public void reset() {
        clickedButton = BUTTON_CANCEL;
        searchTerm = "";
        htmlSelectOneMenuIT.setDisabled(false);
        chkBoxAll.setDisabled(false);
        chkBoxAll.setSelected(false);
        checkall = "false";
        setAuthoritySearchResults(new ArrayList<AuthorityFile>());
        authoritySearchResultsTemp = new ArrayList<AuthorityFile>();
        displayResults = new ArrayList<AuthorityFile>();
        validationTag = null;
        selectedAHRecordsCount = 0;
        selectedIds.removeAllElements();
        selectedObjects.removeAllElements();
        selectedRecordsCount = 0;
    }

    public String getAuthorityFileType() {
        return authorityFileType;
    }

    public void setAuthorityFileType(String authorityFileType) {
        this.authorityFileType = authorityFileType;
    }

    public String getCheckall() {
        return checkall;
    }

    public void setCheckall(String checkall) {
        this.checkall = checkall;
    }

    public HtmlDataTable getAuthorityResultTable() {
        return authorityResultTable;
    }

    public void setAuthorityResultTable(HtmlDataTable authorityResultTable) {
        this.authorityResultTable = authorityResultTable;
    }

    public String getPageno() {
        return pageno;
    }

    public void setPageno(String pageno) {
        this.pageno = pageno;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public HtmlSelectOneMenu getHtmlSelectOneMenuIT() {
        return htmlSelectOneMenuIT;
    }

    public void setHtmlSelectOneMenuIT(HtmlSelectOneMenu htmlSelectOneMenuIT) {
        this.htmlSelectOneMenuIT = htmlSelectOneMenuIT;
    }

    public UISelectItems getIndexTypesSelectItems() {
        return indexTypesSelectItems;
    }

    public void setIndexTypesSelectItems(UISelectItems indexTypesSelectItems) {
        this.indexTypesSelectItems = indexTypesSelectItems;
    }

    public SelectItemGroup getItemGroup() {
        if (itemGroup == null) {
            loadAuthorityTypes();
        }
        return itemGroup;
    }

    public void setItemGroup(SelectItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(String recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    private NGLTag scanAuthorityRecord(NGLMARCRecord nGLMARCRecord, String indexQuery, String term) {
        try {
            Element root = NGLXMLUtility.getInstance().getRootElementFromXML(indexQuery);
            if (root != null) {
                List listIndexes = root.getChildren("Index");
                if (listIndexes != null && listIndexes.size() > 0) {
                    for (int i = 0; i < listIndexes.size(); i++) {
                        Element eleIndex = (Element) listIndexes.get(i);
                        if (eleIndex != null) {
                            List listFields = eleIndex.getChildren("Field");
                            if (listFields != null && listFields.size() > 0) {
                                for (int j = 0; j < listFields.size(); j++) {
                                    Element eleField = (Element) listFields.get(j);
                                    if (eleField != null) {
                                        String field = utility.getTestedStringWithTrim(eleField.getAttributeValue("tag"));
                                        NGLTag nGLTag = this.scanAuthorityRecord(this.getSearchTerm(), field, nGLMARCRecord);
                                        if (nGLTag != null) {
                                            return nGLTag;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    private NGLTag scanAuthorityRecord(String term, String field, NGLMARCRecord nGLMARCRecord) {
        try {
            if (field.length() == 5) {
                String tag = field.substring(0, 3);
                String code = field.substring(4, 5);
                term = term.toLowerCase();
                if (term.startsWith("*")) {
                    term = term.substring(1, term.length());
                } else if (term.endsWith("*")) {
                    term = term.substring(0, term.length() - 1);
                }
                NGLTag nGLTags[] = nGLMARCRecord.getTags(tag);
                if (nGLTags != null && nGLTags.length > 0) {
                    for (int i = 0; i < nGLTags.length; i++) {
                        if (nGLTags[i] != null) {
                            NGLSubfield nGLSubfields[] = nGLTags[i].getSubfields(code);
                            if (nGLSubfields != null && nGLSubfields.length > 0) {
                                for (int j = 0; j < nGLSubfields.length; j++) {
                                    if (nGLSubfields[j] != null) {
                                        String data = utility.getTestedString(nGLSubfields[j].getData());
                                        data = data.toLowerCase();
                                        if (data.contains(term)) {
                                            return nGLTags[i];
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String getDeleteMode() {
        return deleteMode;
    }

    public void setDeleteMode(String deleteMode) {
        this.deleteMode = deleteMode;
    }

    public HtmlSelectBooleanCheckbox getHtmlSelectBooleanCheckbox() {
        return htmlSelectBooleanCheckbox;
    }

    public void setHtmlSelectBooleanCheckbox(HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox) {
        this.htmlSelectBooleanCheckbox = htmlSelectBooleanCheckbox;
    }

    public HtmlMenuItem getMiViewAuthorisedHeading() {
        return miViewAuthorisedHeading;
    }

    public void setMiViewAuthorisedHeading(HtmlMenuItem miViewAuthorisedHeading) {
        this.miViewAuthorisedHeading = miViewAuthorisedHeading;
    }

    public HtmlMenuItem getMiViewOtherTerms() {
        return miViewOtherTerms;
    }

    public void setMiViewOtherTerms(HtmlMenuItem miViewOtherTerms) {
        this.miViewOtherTerms = miViewOtherTerms;
    }

    public AuthorityFile getAuthorityFileM() {
        return authorityFileM;
    }

    public void setAuthorityFileM(AuthorityFile authorityFileM) {
        this.authorityFileM = authorityFileM;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private List search(Integer from, Integer to) {
        String indexDefinition = "";
        if (getCheckall().equalsIgnoreCase("true")) {
            Element eleQuery = new Element("Query");
            eleQuery.setAttribute("from", from.toString());
            eleQuery.setAttribute("to", to.toString());
            SelectItem selectItem[] = itemGroup.getSelectItems();
            for (int i = 0; i < selectItem.length; i++) {
                String def = selectItem[i].getValue().toString();
                eleQuery = getIndexQuery(eleQuery, def);
            }
            indexDefinition = NGLXMLUtility.getInstance().generateXML(eleQuery);
        } else {
            Element eleQuery = new Element("Query");
            eleQuery.setAttribute("from", from.toString());
            eleQuery.setAttribute("to", to.toString());
            String def = this.getAuthorityFileType();
            eleQuery = getIndexQuery(eleQuery, def);
            indexDefinition = NGLXMLUtility.getInstance().generateXML(eleQuery);
        }
        indexDefinition = getIndexDefinitionWithTerm(indexDefinition, this.getSearchTerm());
        System.out.println("indexDefinition ============ " + indexDefinition);
        String xmlResp = "";
        NGLAuthoritySearch nGLAuthoritySearch = (org.verus.ngl.sl.bprocess.technicalprocessing.NGLAuthoritySearch) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("nGLAuthoritySearch");
        xmlResp = nGLAuthoritySearch.bm_searchAuthorityRecords(indexDefinition, new NGLUserSession().getDatabaseId());
        Element root = NGLXMLUtility.getInstance().getRootElementFromXML(xmlResp);
        if (root != null) {
            Element eleRecords = root.getChild("Records");
            if (eleRecords != null) {
                List listRecords = eleRecords.getChildren("Record");
                if (listRecords != null && listRecords.size() > 0) {
                    List<AuthorityFile> listResults = new ArrayList();
                    for (int i = 0; i < listRecords.size(); i++) {
                        Element eleRecord = (Element) listRecords.get(i);
                        if (eleRecord != null) {
                            String id = utility.getTestedStringWithTrim(eleRecord.getChildText("Id"));
                            String headingType = utility.getTestedStringWithTrim(eleRecord.getChildText("HeadingType"));
                            String authorityXml = utility.getTestedStringWithTrim(eleRecord.getChildText("AuthorityXml"));
                            String authorityType = this.getIndexHash().get(headingType).toString();
                            NGLMARCRecord nGLMARCRecords[] = NGLConverter.getInstance().getMarcModel(authorityXml);
                            if (nGLMARCRecords != null && nGLMARCRecords.length > 0) {
                                if (nGLMARCRecords[0] != null) {
                                    NGLTag nGLTag = this.scanAuthorityRecord(nGLMARCRecords[0], indexDefinition, getSearchTerm());
                                    if (nGLTag != null) {
                                        String tag = utility.getTestedStringWithTrim(nGLTag.getTag());
                                        String resultType = "";
                                        if (tag.startsWith("1") && tag.length() == 3) {
                                            resultType = "AH";
                                        } else if (tag.startsWith("4") && tag.length() > 0) {
                                            resultType = "ST";
                                        }
                                        String displayString = nGLTag.getDisplayString();
                                        AuthorityFile af = new AuthorityFile();
                                        af.setId(id);
                                        af.setSelect("false");
                                        if (this.getCheckall().equalsIgnoreCase("true")) {
                                            displayString = displayString.concat("[" + authorityType + "]");
                                        }
                                        af.setDisplay(displayString);
                                        af.setAuthorityType(authorityType);
                                        af.setSeeTerms(resultType);
                                        af.setAuthorityXML(authorityXml);
                                        listResults.add(af);
                                    }
                                }
                            }
                        }
                    }
                    return listResults;
                }
            }
        }
        return null;
    }

    public String authoritySearchResultsNew() {
        clearSelectedRecords();
        try {
            if (this.getSearchTerm() != null) {
                this.selectedIds.removeAllElements();
                this.setAuthoritySearchResults(new ArrayList<AuthorityFile>());
                from = 1;
                Integer recPerPage = Integer.parseInt(recordsPerPage);
                to = recPerPage;
                bnPrev.setDisabled(true);
                bnNext.setDisabled(true);
                setAuthoritySearchResults((List<AuthorityFile>) search(from, to));
                if (getAuthoritySearchResults() != null) {
                    List list = search(from + recPerPage, to + recPerPage);
                    if (list != null) {
                        bnNext.setDisabled(false);
                    }
                }
                System.out.println("@@@@@@@@@@@@@@@@@@@======List Size:" + getAuthoritySearchResults().size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getMiViewAuthorisedHeading().setDisabled(true);
        this.getMiViewOtherTerms().setDisabled(true);
        return null;
    }

    public String bnOkMethod() {
        clickedButton = BUTTON_OK;
        return null;
    }

    public List<AuthorityFile> getAuthoritySearchResultsTemp() {
        this.setMessage("Please select atleast one record...");
        this.setNote(" ");
        this.setBnYesValue("Ok");
        this.setBnNoValue("Cancel");
        return authoritySearchResultsTemp;
    }

    public void setAuthoritySearchResultsTemp(List<AuthorityFile> authoritySearchResultsTemp) {
        this.authoritySearchResultsTemp = authoritySearchResultsTemp;
    }

    public String getBnYesValue() {
        return bnYesValue;
    }

    public void setBnYesValue(String bnYesValue) {
        this.bnYesValue = bnYesValue;
    }

    public String getBnNoValue() {
        return bnNoValue;
    }

    public void setBnNoValue(String bnNoValue) {
        this.bnNoValue = bnNoValue;
    }

    public List<AuthorityFile> getDisplayResults() {
        System.out.println("%%%%%%%%%========== this is in getDisplayResults:" + displayResults.size());
        return displayResults;
    }

    public void setDisplayResults(List<AuthorityFile> displayResults) {
        this.displayResults = displayResults;
    }

    public HtmlSelectBooleanCheckbox getHtmlSelectBooleanCheckbox1() {
        return htmlSelectBooleanCheckbox1;
    }

    public void setHtmlSelectBooleanCheckbox1(HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox1) {
        this.htmlSelectBooleanCheckbox1 = htmlSelectBooleanCheckbox1;
    }

    public HtmlDataTable getAuthorityDisplayResultTable() {
        return authorityDisplayResultTable;
    }

    public void setAuthorityDisplayResultTable(HtmlDataTable authorityDisplayResultTable) {
        this.authorityDisplayResultTable = authorityDisplayResultTable;
    }

    public String getMode() {
        if (displayResults.size() > 0) {
            return "1";
        } else {
            return "0";
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getClickedButton() {
        return clickedButton;
    }

    public void setClickedButton(Integer clickedButton) {
        this.clickedButton = clickedButton;
    }

    public Integer getSelectedAHRecordsCount() {
        return selectedAHRecordsCount;
    }

    public void setSelectedAHRecordsCount(Integer selectedAHRecordsCount) {
        this.selectedAHRecordsCount = selectedAHRecordsCount;
    }

    public Integer getAfId() {
        return afId;
    }

    public void setAfId(Integer afId) {
        this.afId = afId;
    }

    public NGLTag getValidationTag() {
        return validationTag;
    }

    public void setValidationTag(NGLTag validationTag) {
        this.validationTag = validationTag;
    }

    public HtmlSelectBooleanCheckbox getChkBoxAll() {
        return chkBoxAll;
    }

    public void setChkBoxAll(HtmlSelectBooleanCheckbox chkBoxAll) {
        this.chkBoxAll = chkBoxAll;
    }

    public Hashtable getIndexHash() {
        return indexHash;
    }

    public void setIndexHash(Hashtable indexHash) {
        this.indexHash = indexHash;
    }

    public HtmlCommandButton getBnPrev() {
        return bnPrev;
    }

    public void setBnPrev(HtmlCommandButton bnPrev) {
        this.bnPrev = bnPrev;
    }

    public HtmlCommandButton getBnNext() {
        return bnNext;
    }

    public void setBnNext(HtmlCommandButton bnNext) {
        this.bnNext = bnNext;
    }

    public List<AuthorityFile> getAuthoritySearchResults() {
        this.setMessage("Please select atleast one record...");
        this.setNote(" ");
        this.setBnYesValue("Ok");
        this.setBnNoValue("Cancel");
        return authoritySearchResults;
    }

    public void setAuthoritySearchResults(List<AuthorityFile> authoritySearchResults) {
        this.authoritySearchResults = authoritySearchResults;
    }
}
